package app.service;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangParams;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zhenjiang.InsuranceZhenJiangParamsRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

/**
 * 登陆页面没有变化，期初开发的时候相当于跳过了图片验证码
 * 
 * 后来系统升级测试的过程中发现重新开发登陆，在图片验证码验证成功的条件下，在验证其他登录信息
 */
@Transactional
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhenjiang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhenjiang"})
public class InsuranceZhenJiangService extends InsuranceService  implements InsuranceLogin{
	public static final Logger log = LoggerFactory.getLogger(InsuranceZhenJiangService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceZhenJiangCrawlerService insuranceZhenJiangCrawlerService;
	@Value("${filesavepath}") 
	public String fileSavePath;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceZhenJiangParamsRepository zhenJiangParamsRepository;
	@Autowired
	private InsuranceFiveInsurChangeStatus zhenJiangChangeStatus;
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	
	
	
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			//先检验图片验证码的正确性
			//获取图片验证码
			String url="http://www.hrsszj.gov.cn/PublicServicePlatform/auth/captcha/getCaptcha.action?key=logon_user";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String imagePath = getImagePath(page,fileSavePath);
				String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1902");
				//校验图片验证码
				url="http://www.hrsszj.gov.cn/PublicServicePlatform/auth/captcha/validateCaptcha.action?key=logon_user&code="+code+"";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				page = webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					String imageResult = JSONObject.fromObject(html).getString("success");  
					if(imageResult.contains("true")){
						//验证登陆信息的正确性
						url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/user/userLogin.action";
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						String requestBody="userVO.logName="+insuranceRequestParameters.getUsername().trim()+""
								+ "&userVO.password="+insuranceRequestParameters.getPassword().trim()+""
										+ "&userVO.verifyCode1="+code+"";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							tracer.addTagWrap("验证登陆信息正确性响应的页面是：", html);
							if(html.contains("idcard")){
								taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
								taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
								taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
								String logincookies = CommonUnit.transcookieToJson(webClient);
							    taskInsurance.setCookies(logincookies);
							    taskInsuranceRepository.save(taskInsurance);	
							    //将登录成功之后的身份证号解析出来作为参数
							    String idNum = JSONObject.fromObject(html).getString("idcard");
							    //存储获取成功的idcard
							    InsuranceZhenJiangParams params=new InsuranceZhenJiangParams();
							    params.setTaskid(insuranceRequestParameters.getTaskId().trim());
							    params.setIdcard(idNum);
							    zhenJiangParamsRepository.save(params);
							    tracer.addTag("登陆成功之后，获取的用于爬取数据的参数已经入库", idNum);
							}else if(html.contains("false")){   //登录信息有误返回的html页面中就会有这个信息
								if(html.contains("用户不存在")){
									System.out.println("用户不存在");
									tracer.addTag("登录信息有误：", "用户不存在");
									insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
											InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
											"用户不存在！",taskInsurance);
								}else if(html.contains("密码错误")){
									tracer.addTag("登录信息有误：", "密码错误");
									insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
											InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
											"密码错误！",taskInsurance);
								}else{
									System.out.println("出现了其他错误"); 
									tracer.addTag("登录信息有误,详见验证登录信息之后返回的html", html);
									insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
											InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
											"系统繁忙，请稍后再试！",taskInsurance);
								}
							}else{
								System.out.println("出现了其他错误"); 
								tracer.addTag("登录信息有误,详见验证登录信息之后返回的html", html);
								insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
										InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
										"系统繁忙，请稍后再试！",taskInsurance);
							}
						}
					}else{   //false
						captchaErrorCount++;
						if(captchaErrorCount>3){
							 tracer.addTag("输入的验证码错误，当前重试识别次数：",captchaErrorCount+"");
							 insuranceService.changeLoginStatusCaptError(taskInsurance);
							 captchaErrorCount=0;
						}else{
							login(insuranceRequestParameters);  
						}
					}	
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("登录发生异常，异常信息是：", e.toString());
			insuranceService.changeLoginStatusException(taskInsurance);
		}
		return taskInsurance;
	}
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=insuranceZhenJiangCrawlerService.getUserInfo(taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		try {
			Future<String> future=insuranceZhenJiangCrawlerService.getInsurInfo(taskInsurance);
			listfuture.put("getInsurInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInsurInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
		}
		try {
			Future<String> future=insuranceZhenJiangCrawlerService.getChargeDetail(taskInsurance);
			listfuture.put("getChargeDetail", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeDetail.e", taskInsurance.getTaskid()+"===>"+e.toString());
			zhenJiangChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
		}
//		最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
//					 判断是否执行完毕
					if (entry.getValue().isDone()) { 
						tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		} catch (Exception e) {
			System.out.println("更新最终状态时候出现了异常："+e.toString());
			tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
			changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
