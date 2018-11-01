package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * 内蒙古自治区的社保，传参数时候，登录验证码识别错了也可以登录，只要captchaId对了就行，调研发现
 */
@Component
@Transactional
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.neimenggu"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.neimenggu"})
public class InsuranceSZNeiMengGuService extends InsuranceService implements InsuranceLogin {
	public static final Logger log = LoggerFactory.getLogger(InsuranceSZNeiMengGuService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZNeiMengGuCrawlerService insuranceSZNeiMengGuCrawlerService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceFiveInsurChangeStatus neiMengGuChangeStatus;
	@Autowired
	private InsuranceSZNeiMengGuHtmlRepository insuranceSZNeiMengGuHtmlRepository;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try{
			String url="http://login.12333k.cn/Cas/login?service=http%3A%2F%2Fcard.12333k.cn%2Fsiweb%2Flogin.do%3Fmethod%3Dbegin";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				String html=hPage.asXml();
				Document doc = Jsoup.parse(html);  //获取隐藏域的三个参数
				String lt =doc.getElementsByAttributeValue("name", "lt").get(0).val();
				String eventId =doc.getElementsByAttributeValue("name", "_eventId").get(0).val();
				String captchaId =doc.getElementsByAttributeValue("name", "captchaId").get(0).val();
				HtmlImage image = hPage.getFirstByXPath("//img[@id='jcaptcha']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1902");
				//用验证链接验证登录信息的正确性
				url="http://login.12333k.cn/Cas/login?service=http%3A%2F%2Fcard.12333k.cn%2Fsiweb%2Flogin.do%3Fmethod%3Dbegin";
				String requestBody=""
						+ "username="+insuranceRequestParameters.getUsername().trim()+""
						+ "&password="+insuranceRequestParameters.getPassword().trim()+""
						+ "&checkCode="+code+""
//						+ "&checkCode=1234"  
						+ "&lt="+lt+""
						+ "&_eventId="+eventId+""
						+ "&captchaId="+captchaId+"";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					InsuranceSZNeiMengGuHtml insuranceSZNeiMengGuHtml = new InsuranceSZNeiMengGuHtml();
					insuranceSZNeiMengGuHtml.setPagenumber(1);
					insuranceSZNeiMengGuHtml.setType("登陆信息验证结果源码页");
					insuranceSZNeiMengGuHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceSZNeiMengGuHtml.setUrl(url);
					insuranceSZNeiMengGuHtml.setHtml(html);
					insuranceSZNeiMengGuHtmlRepository.save(insuranceSZNeiMengGuHtml);			
			    	tracer.addTag("登陆信息验证结果源码页：","已经入库");
					tracer.addTag(taskInsurance.getTaskid()+"用链接进行登录信息的校验，返回的页面是", html);
					if(html.contains("status11")){   //登陆出现错误提示信息，响应的页面中将会有这个关键字
						doc = Jsoup.parse(html);
						Element statusElement = doc.getElementById("status11");  //登陆错误信息展示的地方
						String errMsg = "";
						if(null!=statusElement){
							errMsg=statusElement.text();
							if(errMsg.contains("用户名或者密码输入错误")){
								insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
							}else if(errMsg.contains("用户名或密码错误")){
								insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
							}else{
								insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
										errMsg,taskInsurance);
							}
						}
					}else{
						if(html.contains("http://www.12333k.cn/ecdomain/framework/nmsbksite/jhhkokbbfoinbbobjplikhoddigkkkal.jsp")){
							taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
							taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
							taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
							String logincookies = CommonUnit.transcookieToJson(webClient);
						    taskInsurance.setCookies(logincookies);
						    taskInsuranceRepository.save(taskInsurance);				    
						    Thread.sleep(1000);  
						}else{
							tracer.addTag("出现了其他类型的登陆错误", "详见登陆信息验证结果响应页面");
							insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
									InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
									"系统繁忙，请稍后再试！",taskInsurance);
						}
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录过程出现异常，异常信息是：", e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					"系统繁忙，请稍后再试！",taskInsurance);
		}
		return taskInsurance;
	}	
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=insuranceSZNeiMengGuCrawlerService.getUserInfo(taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		try {
			Future<String> future=insuranceSZNeiMengGuCrawlerService.getInsurInfo(taskInsurance);
			listfuture.put("getInsurInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInsurInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
		}
		try {
			Future<String> future=insuranceSZNeiMengGuCrawlerService.getChargeDetail(taskInsurance);
			listfuture.put("getChargeDetail", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeDetail.e", taskInsurance.getTaskid()+"===>"+e.toString());
			neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
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
			tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
			changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		}
		return taskInsurance;
	}
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
