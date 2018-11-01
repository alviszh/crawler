package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.qingdao"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.qingdao"})
public class InsuranceQingdaoService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceQingdaoHtmlRepository qingDaoHtmlRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceQingdaoCrawlerService qingdaoCrawlerService;
	private static int captchaErrorCount=0;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String url="http://12333.qingdao.gov.cn/grcx2/pages/login_zg.jsp";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				HtmlImage image = hPage.getFirstByXPath("//img[@id='validationCode']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1902");
//				用如下链接验证相关信息输入是否正确
				//先验证图片验证码是否正确
				url="http://12333.qingdao.gov.cn/grcx2/validationCode/equalsCode.action";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody="checkCode="+code+"";
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					if(html.contains("success")){
						//图片验证码验证成功的前提下，验证登录信息的正确性
						url="http://12333.qingdao.gov.cn/grcx2/login.action";
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						requestBody="loginname="+insuranceRequestParameters.getUsername().trim()+""
								+ "&password="+DigestUtils.md5Hex(""+insuranceRequestParameters.getPassword().trim()+"")+""
								+ "&pid=1001&checkCode="+code+"";
						webRequest.setRequestBody(requestBody);
						hPage=webClient.getPage(webRequest);
						if(null!=page){
							html=hPage.getWebResponse().getContentAsString();
							//存储登陆信息验证结果源码页
							InsuranceQingdaoHtml qingdaoHtml = new InsuranceQingdaoHtml();
							qingdaoHtml.setPageCount(1);
							qingdaoHtml.setType("登陆信息验证结果源码页");
							qingdaoHtml.setTaskid(insuranceRequestParameters.getTaskId());
							qingdaoHtml.setUrl(url);
							qingdaoHtml.setHtml(html);
							qingDaoHtmlRepository.save(qingdaoHtml);			
					    	tracer.addTag("登陆信息验证结果源码页：","已经入库");
							if(html.contains("个人查询首页")){ //登陆成功之后页面中会包含
								insuranceService.changeLoginStatusSuccess(taskInsurance,hPage);
							}else {
								Document doc = Jsoup.parse(html);
								Elements errObj = doc.select("[color=red]");  //此方法可定位
//								Elements errObj = doc.select("font[color]");  //此方法亦可定位
								//官网调研：用户名或者密码输入错误，提示的信息都是用户名或密码错误
								if(errObj!=null){
									String errMsg =errObj.text();
									insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
											InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
											errMsg,taskInsurance);
								}else{
									tracer.addTag("登陆失败，错误信息在开发调研过程中没有遇到", "请根据本次任务taskid到库中查看对应源码页");
									insuranceService.changeLoginStatusCaptError(taskInsurance);
								}
							}
						}
					}else{ 
						captchaErrorCount++;
						if (captchaErrorCount<=2){
							tracer.addTag("action.login.captchacaptchaErrorCount","解析图片验证码失败"+captchaErrorCount+"次，重新执行登录方法");
							login(insuranceRequestParameters);
						} else {
							captchaErrorCount=0;
							insuranceService.changeLoginStatusCaptError(taskInsurance);
							return taskInsurance;
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
	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters){
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		tracer.addTag("action.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		//没有生育险和工伤险，为了最后更改状态成功，此处设置为定值
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),201, taskInsurance);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),201, taskInsurance);
		try {
			Future<String> future=qingdaoCrawlerService.getUserInfo(taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(),201, taskInsurance);
		}
		try {
			Future<String> future=qingdaoCrawlerService.getMedical(taskInsurance);
			listfuture.put("getMedicalParam", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		try {
			Future<String> future=qingdaoCrawlerService.getPension(taskInsurance);
			listfuture.put("getPensionParam", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=qingdaoCrawlerService.getUnemployment(taskInsurance);
			listfuture.put("getUnemploymentParam", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUnemployment.e", e.toString());
			insuranceService.changeCrawlerStatus( InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=qingdaoCrawlerService.getCompInfo(taskInsurance);
			listfuture.put("getCompInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getCompInfo.e", e.toString());
		}
		try {
			Future<String> future=qingdaoCrawlerService.getPayGeneral(taskInsurance);
			listfuture.put("getPayGeneral", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPayGeneral.e", e.toString());
		}
		
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
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
