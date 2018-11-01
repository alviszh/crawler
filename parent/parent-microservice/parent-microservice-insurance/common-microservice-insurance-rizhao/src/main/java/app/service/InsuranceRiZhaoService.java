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

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.rizhao.InsuranceRiZhaoHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.rizhao"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.rizhao"})
public class InsuranceRiZhaoService implements InsuranceLogin{
	public static final Logger log = LoggerFactory.getLogger(InsuranceRiZhaoService.class);
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}")
	public String loginhost;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceRiZhaoCrawlerService insuranceRiZhaoCrawlerService;
	@Autowired
	private InsuranceRiZhaoHtmlRepository riZhaoHtmlRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	private static int captchaErrorCount;
//	异步爬取所有数据
	@Async   
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=insuranceRiZhaoCrawlerService.getUserInfo(taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		try {
			Future<String> future=insuranceRiZhaoCrawlerService.getPension(taskInsurance);
			listfuture.put("getPension", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceRiZhaoCrawlerService.getMedical(taskInsurance);
			listfuture.put("getMedical", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceRiZhaoCrawlerService.getInjury(taskInsurance);
			listfuture.put("getInjury", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInjury.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceRiZhaoCrawlerService.getUnemployment(taskInsurance);
			listfuture.put("getUnemployment", future);
		} catch (Exception e) {
			System.out.println("action.crawler.getUnemployment.e:"+e.toString());
			tracer.addTag("action.crawler.getUnemployment.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceRiZhaoCrawlerService.getBear(taskInsurance);
			listfuture.put("getBear", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBear.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
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
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} catch (Exception e) {
			System.out.println("更新最终状态时候出现了异常："+e.toString());
			tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String url="http://"+loginhost+"/rz_query/logAction.do?method=logmain";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				HtmlImage image = hPage.getFirstByXPath("//img[@id='jcaptcha']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1902");   //识别图片验证码
				//成功获取登录页面，继续接下来验证相关信息输入正确性的操作：
				url="http://"+loginhost+"/rz_query/j_unieap_security_check.do?logtype=1";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody="j_username="+insuranceRequestParameters.getUsername().trim()+""
						+ "&j_password="+insuranceRequestParameters.getPassword().trim()+""
						+ "&jcaptcha_response="+code+"";
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					InsuranceRiZhaoHtml htmlStore = new InsuranceRiZhaoHtml();
					htmlStore.setPagenumber(1);
					htmlStore.setType("登陆信息验证结果源码页");
					htmlStore.setTaskid(insuranceRequestParameters.getTaskId());
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					riZhaoHtmlRepository.save(htmlStore);			
			    	tracer.addTag("登陆信息验证结果源码页：","已经入库");
					if(html.contains("setEsgStyle")){   //登录信息有误返回的html页面中就会有这个信息
						if(html.contains("错误的验证码")){
							tracer.addTag("登录信息有误：", "错误的验证码");
							captchaErrorCount++;
							tracer.addTag(taskInsurance.getTaskid()+"登录验证码解析错误的次数为：",captchaErrorCount+"次");
							if (captchaErrorCount<=2){
								tracer.addTag("action.login.captchaErrorCount","解析图片验证码失败"+captchaErrorCount+"次，重新执行登录方法");
								login(insuranceRequestParameters);
							} else {
								captchaErrorCount=0;
								insuranceService.changeLoginStatusCaptError(taskInsurance);
								return taskInsurance;
							}
						}else if(html.contains("用户名不存在或密码错误")){
							System.out.println("用户名不存在或密码错误");
							tracer.addTag("登录信息有误：", "用户名不存在或密码错误");
							insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
									InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
									InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
						}else{
							System.out.println("出现了其他错误"); 
							tracer.addTag("登录信息有误,详见验证登录信息之后返回的html", html);
							insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
									InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
									"系统繁忙，请稍后再试！",taskInsurance);
						}
					}else{
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
						String logincookies = CommonUnit.transcookieToJson(webClient);
					    taskInsurance.setCookies(logincookies);
					    taskInsurance = taskInsuranceRepository.save(taskInsurance);				    
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录发生异常，异常信息是：", e.toString());
			insuranceService.changeLoginStatusException(taskInsurance);
		}
		return taskInsurance;
	}
}
