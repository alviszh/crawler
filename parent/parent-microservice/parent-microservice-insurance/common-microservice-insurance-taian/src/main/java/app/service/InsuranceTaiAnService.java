package app.service;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.taian.InsuranceTaiAnParams;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.taian.InsuranceTaiAnParamsRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.taian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.taian"})
public class InsuranceTaiAnService  implements InsuranceLogin{
	public static final Logger log = LoggerFactory.getLogger(InsuranceTaiAnService.class);
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}")
	public String loginhost;   //虽然ip会变，但是不能用注入的方式，否则模拟登录响应的页面是404
	@Value("${filesavepath}")
	public String filesavepath;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceTaiAnCrawlerService insuranceTaiAnCrawlerService;
	@Autowired
	private InsuranceTaiAnParamsRepository taiAnParamsRepository;
	@Value("${appversion}") 
	public String appversion;
	private static int captchaErrorCount;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try{
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url="http://124.130.146.14:8082/hsp/logon.do";  
			WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("method", "writeMM2Temp"));
			webRequest.getRequestParameters().add(new NameValuePair("_xmlString",
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s tempmm=\""+insuranceRequestParameters.getPassword().trim()+"\"/></p>"));
			webRequest.getRequestParameters().add(new NameValuePair("_random", "0.20038958982881083"));
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				tracer.addTag("用链接进行第一轮登录信息的校验，返回的校验信息是:", html);
				if(html.contains("true")){
					//成功获取登录页面，继续接下来获取图片验证码的操作：
					url="http://124.130.146.14:8082/hsp/authcode";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					page=webClient.getPage(webRequest);
					if(null!=page){						
						String imgagePath = InsuranceTaiAnImageService.getImagePath(page,filesavepath);
						String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1902");
						url="http://124.130.146.14:8082/hsp/logon.do";
						webRequest = new WebRequest(new URL(url),HttpMethod.POST);
						webRequest.setRequestParameters(new ArrayList<NameValuePair>());
						webRequest.getRequestParameters().add(new NameValuePair("method", "doLogon"));
						webRequest.getRequestParameters().add(new NameValuePair("_xmlString",
							"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s userid=\""+insuranceRequestParameters.getUsername().trim()+"\"/>"
									+ "<s usermm=\""+DigestUtils.md5Hex(insuranceRequestParameters.getPassword().trim())+"\"/><s authcode=\""
									+ code
									+ "\"/><s yxzjlx=\"A\"/><s appversion=\""+appversion+"\"/><s dlfs=\"1\"/></p>"));
						//注意：如果登陆的过程中出现提示：系统版本与数据库版本两者不一致，说明上述参数中的appversion需要改正，去官网尝试登陆，获取之后替换如上appversion的值
						webRequest.getRequestParameters().add(new NameValuePair("_random", "0.20038958982881083"));
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							tracer.addTag("用链接进行第二轮登录信息的校验，返回的校验信息是:", html);
							if(html.contains("true")){
								taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
								taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
								taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
								String logincookies = CommonUnit.transcookieToJson(webClient);
							    taskInsurance.setCookies(logincookies);
							    taskInsurance = taskInsuranceRepository.save(taskInsurance);				    
							    Thread.sleep(1000); 
							    String usersession_uuid = JSONObject.fromObject(html).getString("__usersession_uuid");
							    //存储获取成功的idcard
							    InsuranceTaiAnParams params=new InsuranceTaiAnParams();
							    params.setTaskid(insuranceRequestParameters.getTaskId().trim());
							    params.setUsersession(usersession_uuid);
							    taiAnParamsRepository.save(params);
							    tracer.addTag("登陆成功之后，获取的用于爬取数据的参数已经入库", usersession_uuid);
							}else{
								if(html.contains("验证码不正确")){
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
								}else if(html.contains("用户名或密码不正确")){  //密码输入错误
									insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
											InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
											InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
								}else if(html.contains("查不到用户信息，请先注册")){  //用户名输入错误
									insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhase(),
											InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhasestatus(),
											InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getDescription(),taskInsurance);
								}else{
									insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
											InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
											html,taskInsurance);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录过程中出现异常：", e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					"系统繁忙，请稍后再试！",taskInsurance);
		}
		return taskInsurance;
	}
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		//爬取数据之前先获取登陆成功之后保存的参数
		InsuranceTaiAnParams params = taiAnParamsRepository.findTopByTaskidOrderByCreatetimeDesc(insuranceRequestParameters.getTaskId().trim());
		String usersession_uuid=null;
		if(null!=params){
			usersession_uuid = params.getUsersession().trim();
		}
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId().trim());
		try {
			Future<String> future=insuranceTaiAnCrawlerService.getUserInfo(taskInsurance,usersession_uuid);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
//		如下是除了生育险之外的其他险缴费信息的爬取(虽然页面上是分页显示的，但是用如下链接返回的是所有的数据，所以，用如下链接加参数返回的就是所有的数据)
		try {
			Future<String> future=insuranceTaiAnCrawlerService.getPension(taskInsurance,usersession_uuid);
			listfuture.put("getPension", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceTaiAnCrawlerService.getMedical(taskInsurance,usersession_uuid);
			listfuture.put("getMedical", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceTaiAnCrawlerService.getInjury(taskInsurance,usersession_uuid);
			listfuture.put("getInjury", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInjury.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceTaiAnCrawlerService.getUnemployment(taskInsurance,usersession_uuid);
			listfuture.put("getUnemployment", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUnemployment.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		try {
			Future<String> future=insuranceTaiAnCrawlerService.getBear(taskInsurance,usersession_uuid);
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
}
