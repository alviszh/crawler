package app.service;

import java.net.URL;
import java.util.Calendar;

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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.beijing.InsuranceBeijingUserInfoRepository;
import com.module.htmlunit.WebCrawler;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import app.bean.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceBeijingParser;
import app.service.aop.InsuranceLogin;
import app.service.aop.InsuranceSms;

/**
 * @author zz
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.beijing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.beijing"})
public class InsuranceBeijingService implements InsuranceLogin,InsuranceSms{
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceBeijingService.class);
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceBeijingParser insuranceBeijingParser;
	@Autowired
	private InsuranceBeijingUserInfoRepository insuranceBeijingUserInfoRepository;
	@Autowired
	private InsuranceBeijingHtmlRepository insuranceBeijingHtmlRepository;
	@Autowired
	private TracerLog tracer;
//	@Autowired
//	private InsuranceBeijingService insuranceBeijingService;
	@Autowired
	private AsyncBeijingPensionService asyncBeijingPensionService;
	@Autowired
	private AsyncBeijingUnemploymentService asyncBeijingUnemploymentService;
	@Autowired
	private AsyncBeijingInjuryService asyncBeijingInjuryService;
	@Autowired
	private AsyncBeijingMaternityService asyncBeijingMaternityService;
	@Autowired
	private AsyncBeijingMedicalService asyncBeijingMedicalService;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception 
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		if(null != taskInsurance){

			WebParam webParam = null;
			try {
				webParam = insuranceBeijingParser.login(insuranceRequestParameters,taskInsurance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null == webParam){
				tracer.addTag("ERROR ==>","登录页获取超时！");
				insuranceService.changeLoginStatusTimeOut(taskInsurance);			
			}else{
				HtmlPage page = webParam.getPage();
				if(null != page){
					String html = page.getWebResponse().getContentAsString();
						
					if(html.contains("短信验证码错误")){
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_SMS_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_SMS_ERROR.getPhasestatus(), 
								InsuranceStatusCode.INSURANCE_LOGIN_SMS_ERROR.getDescription(), taskInsurance);
					}else if(html.contains("附加码有误")){
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
								InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription(), taskInsurance);
					}else if(html.contains("北京市社会保险网上服务平台-城镇职工用户登录")){
						insuranceService.changeLoginStatusIdnumError(taskInsurance);
					}else{					
						insuranceService.changeLoginStatusSuccess(taskInsurance,page);					
					}
					
				}
			}
						
		}
		return taskInsurance;
	}

	/**
	 * @Des 更新taskInsurance
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {	
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}

	
//	/**
//	 * @Des 获取个人信息
//	 * @param insuranceRequestParameters
//	 * @throws Exception 
//	 */
//	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
//		
//		tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
//		@SuppressWarnings("rawtypes")
//		WebParam webParam = insuranceBeijingParser.getUserInfo(taskInsurance,webClient);
//		if(null != webParam){
//			insuranceBeijingUserInfoRepository.save(webParam.getInsuranceBeijingUserInfo());
//			tracer.addTag("getUserInfo 个人信息","个人信息已入库!");
//			
//			insuranceService.changeCrawlerStatusUserInfo(taskInsurance,webParam.getCode());
//			
//			InsuranceBeijingHtml insuranceBeijingHtml = new InsuranceBeijingHtml();
//			insuranceBeijingHtml.setPageCount(1);
//			insuranceBeijingHtml.setType("userinfo");
//			insuranceBeijingHtml.setTaskid(insuranceRequestParameters.getTaskId());
//			insuranceBeijingHtml.setUrl(webParam.getUrl());
//			insuranceBeijingHtml.setHtml(webParam.getHtml());
//			insuranceBeijingHtmlRepository.save(insuranceBeijingHtml);
//			tracer.addTag("getUserInfo 个人信息源码","个人信息源码表入库!");		
//			
//		}
//		
//	}

	

	@HystrixCommand(fallbackMethod = "fallback")
	public String hystrix() {
		tracer.addTag("InsuranceBeijingService hystrix", "start");
		String url = "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		try{
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Host", "www.bjrbj.gov.cn");
//			webRequest.setAdditionalHeader("Referer", "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp");
//			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			
			tracer.addTag("hystrix 获取页面之前",url);
			HtmlPage page = webClient.getPage(webRequest);	
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("hystrix 北京社保登录页状态码",String.valueOf(status));
			if(200 == status){
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("hystrix 北京社保登录页",html);	
				if(html.contains("在此期间系统暂停服务")){
					tracer.addTag("hystrix 获取页面","网站维护");
					return "ERROR";
				}else{
					return "SUCCESS";
				}
			}
			
		}catch(Exception e){
			tracer.addTag("hystrix 北京社保登录页exception",e.getMessage());
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	public String fallback() {
		tracer.addTag("hystrix.fallback", "ERROR");
		return "ERROR";
	}

	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @Des 更改为正在校验及准备发送短信状态
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatusSendSms(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_DOING.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_DOING.getPhasestatus());
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_DOING.getDescription());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @Des 校验及发送短信
	 * @param insuranceRequestParameters
	 * @param taskInsurance
	 */
	@Async
	@Override//
	public TaskInsurance sendSms(InsuranceRequestParameters insuranceRequestParameters) {
		//
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.validate", taskInsurance.getTaskid());
		
		WebParam webParam = insuranceBeijingParser.validate(insuranceRequestParameters,taskInsurance);
		if(null == webParam){
			tracer.addTag("parser.validate.error","登录页获取超时！");
			insuranceService.changeLoginStatusTimeOut(taskInsurance);
		}else{
			if(102 == webParam.getCode()){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
						InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription(), taskInsurance);				
			}
			else if(101 == webParam.getCode()){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhasestatus(), 
						InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getDescription(), taskInsurance);				
			}
			else if(103 == webParam.getCode()){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getPhasestatus(), 
						InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getDescription(), taskInsurance);				
			}
			else if(104 == webParam.getCode()){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus(), 
						InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getDescription(), taskInsurance);				
			}
			else if(199 == webParam.getCode()){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus(), 
						"您的短信验证码还在有效期内！", taskInsurance);				
			}
			else if(200 == webParam.getCode()){
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getDescription());
				taskInsurance.setCookies(CommonUnit.transcookieToJson(webParam.getWebClient()));
				taskInsurance.setPid(webParam.getHtml());
				taskInsuranceRepository.save(taskInsurance);
			}
			else{
				insuranceService.changeLoginStatusTimeOut(taskInsurance);
			}
		}
		return taskInsurance;
	}


	@Override
	public TaskInsurance verifySms(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		System.out.println("jinlaile ");
		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			asyncBeijingPensionService.getUserInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		for(int i=0;i<10;i++){
			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
			asyncBeijingPensionService.getPensionMsg(insuranceRequestParameters,searchYear);
		}
		
		for(int i=0;i<10;i++){
			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
			asyncBeijingUnemploymentService.getUnemployment(insuranceRequestParameters,searchYear);
		}
		
		for(int i=0;i<10;i++){
			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
			asyncBeijingInjuryService.getInjury(insuranceRequestParameters,searchYear);
		}
		
		for(int i=0;i<10;i++){
			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
			asyncBeijingMaternityService.getBear(insuranceRequestParameters,searchYear);
		}
		
		for(int i=0;i<10;i++){
			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
			asyncBeijingMedicalService.getMedical(insuranceRequestParameters,searchYear);
		}
//		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
