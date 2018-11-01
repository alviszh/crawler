package app.service;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.lanzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.lanzhou"})
public class InsuranceLanZhouService  implements InsuranceLogin{
	public static final Logger log = LoggerFactory.getLogger(InsuranceLanZhouService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceLanZhouCrawlerService insuranceLanZhouCrawlerService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	private static int captchaErrorCount;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String url="http://wssb.lzmohrss.org.cn/siweb/login.do?method=logmain";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(1000);
			if(null!=hPage){
				HtmlImage image = hPage.getFirstByXPath("//img[@id='jcaptcha']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1902");   //识别图片验证码
				//成功获取登录页面，继续接下来验证相关信息输入正确性的操作：
				url="http://wssb.lzmohrss.org.cn/siweb/j_unieap_security_check.do?logtype=1";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody="j_username="+insuranceRequestParameters.getUsername().trim()+""
						+ "&j_password="+insuranceRequestParameters.getPassword().trim()+""
						+ "&jcaptcha_response="+code+"";
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
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
							tracer.addTag("登录信息有误：", "用户名不存在或密码错误");
							insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
									InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
									InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
						}else{
							tracer.addTagWrap("登录信息有误,详见验证登录信息之后返回的html", html);
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
					    taskInsuranceRepository.save(taskInsurance);				    
					    Thread.sleep(1000);  
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
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId().trim());
		try {
			Future<String> future=insuranceLanZhouCrawlerService.getUserInfo(taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		try {
			Future<String> future=insuranceLanZhouCrawlerService.getPension(taskInsurance);
			listfuture.put("getPension", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insuranceLanZhouCrawlerService.getMedical(taskInsurance);
			listfuture.put("getMedical", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
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
			changeOtherCrawlerStatusTrue(taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} catch (Exception e) {
			tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
			changeOtherCrawlerStatusTrue(taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return taskInsurance;
	}
	
	//由于兰州社保只能够爬取养老和医疗保险信息，故决定将这两种信息爬取完成后，也更新其他三种类型保险的状态码
	public void changeOtherCrawlerStatusTrue(TaskInsurance taskInsurance){
		// 生育
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201,
				taskInsurance);
		// 工伤
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201,
				taskInsurance);
		// 失业
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201,
				taskInsurance);

	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
