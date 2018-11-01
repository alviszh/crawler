package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboBear;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboEndowment;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboHtml;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboHurt;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboLost;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboMedical;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboBearRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboEndowmentRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboHurtRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboLostRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboUserInfoRepository;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceNingBoParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.ningbo" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.ningbo" })
public class InsuranceNingBoService{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceNingBoParser insuranceNingBoParser;
	@Autowired
	private InsuranceNingboUserInfoRepository insuranceNingboUserInfoRepository;
	@Autowired
	private InsuranceNingboHtmlRepository insuranceNingboHtmlRepository;
	@Autowired
	private InsuranceNingboMedicalRepository insuranceNingboMedicalRepository;
	@Autowired
	private InsuranceNingboEndowmentRepository insuranceNingboEndowmentRepository;
	@Autowired
	private InsuranceNingboHurtRepository insuranceNingboHurtRepository;
	@Autowired
	private InsuranceNingboBearRepository insuranceNingboBearRepository;
	@Autowired
	private InsuranceNingboLostRepository insuranceNingboLostRepository;
	
//	/**
//	 * @Des 登录
//	 * @param insuranceRequestParameters
//	 * @return TaskInsurance
//	 * @throws Exception
//	 */
//	@Async
//	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
//
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		if (null != taskInsurance) {
//
//			insuranceNingBoParser.login(insuranceRequestParameters);
//			/*if (null == webParam) {
//
//				tracer.addTag("Error", "登录页获取超时！");
//				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
//				return taskInsurance;
//			} else {
//
//				
//				return taskInsurance;
//			}
//*/
//			
//		}
//
//		return null;
//	}

	/**
	 * @Des 更新taskInsurance
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}

	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	/*
	 * @Async public void getUserInfo(InsuranceRequestParameters
	 * insuranceRequestParameters) throws Exception {
	 * 
	 * TaskInsurance taskInsurance =
	 * taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId
	 * ()); Set<Cookie> cookies =
	 * CommonUnit.transferJsonToSet(taskInsurance.getCookies()); WebParam
	 * webParam = insuranceNingBoParser.getUserInfo(taskInsurance, cookies);
	 * 
	 * if(null != webParam){ insuranceNingboUserInfoRepository.save(webParam.
	 * getInsuranceNingboUserInfo());
	 * tracer.addTag("getUserInfo ==>","个人信息已入库!");
	 * 
	 * insuranceService.changeCrawlerStatusUserInfo(taskInsurance,webParam.
	 * getCode());
	 * 
	 * InsuranceNingboHtml insuranceNingboHtml = new InsuranceNingboHtml();
	 * insuranceNingboHtml.setPageCount(1);
	 * insuranceNingboHtml.setType("userinfo");
	 * insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
	 * insuranceNingboHtml.setUrl(webParam.getInsuranceNingboHtml().getUrl());
	 * insuranceNingboHtml.setHtml(webParam.getInsuranceNingboHtml().getHtml());
	 * insuranceNingboHtmlRepository.save(insuranceNingboHtml);
	 * tracer.addTag("getUserInfo ==>","个人信息源码表入库!"); }
	 * 
	 * tracer.addTag("InsuranceNingboService", "个人信息录入");
	 * 
	 * }
	 */

//	@HystrixCommand(fallbackMethod = "fallback")
//	public String hystrix() {
//
//		String url = "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/index.jsp";
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
//		try {
//			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			HtmlPage page = webClient.getPage(webRequest);
//			int status = page.getWebResponse().getStatusCode();
//			if (200 == status) {
//				String html = page.getWebResponse().getContentAsString();
//				if (html.contains("在此期间系统暂停服务")) {
//					throw new RuntimeException(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getDescription());
//				} else {
//					return "SUCCESS";
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public String fallback() {
		return "ERROR";
	}

	/**
	 * 更新task表（doing 正在登录状态）
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}
	
	
	//登陆
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters)
	{
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			tracer.addTag("parser.insurance.login.start", insuranceRequestParameters.getTaskId());
			WebParam webParam = insuranceNingBoParser.loginlogin(insuranceRequestParameters);
			if(null != webParam.getHtml())
			{
				//DomNode querySelector = html.querySelector("#errDiv");
				if (webParam.getHtml().contains("账号或者密码不正确")) {
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getDescription(), InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getPhase(), 103, taskInsurance);
					tracer.addTag("parser.login.name.pwd.ERROR", taskInsurance.getTaskid());

				} else if (webParam.getHtml().contains("验证码输入错误 ")) {
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription(), InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase(), 103, taskInsurance);
					tracer.addTag("parser.login.img.ERROR", taskInsurance.getTaskid());

				} 
				else if (webParam.getHtml().contains("E1001")) {
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					tracer.addTag("parser.login.name.pwd", taskInsurance.getTaskid());

				} 
				else if(webParam.getHtml().contains("您的账号累计输错密码3次，请30分钟后重新登陆"))
				{
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeLoginStatusException(taskInsurance);
					tracer.addTag("parser.login.out_times.ERROR", taskInsurance.getTaskid());
				}
				
				else {
					    tracer.addTag("parser.login.SUCCESS", taskInsurance.getTaskid());
						taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
						insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
						String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
						taskInsurance.setCookies(cookieString);
						taskInsurance.setTesthtml(webParam.getUrl());
						taskInsuranceRepository.save(taskInsurance);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskInsurance;
	}

	//个人信息
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) {
		try {
			tracer.addTag("parser.getUserInfo.start", insuranceRequestParameters.getTaskId());
			WebParam<InsuranceNingboUserInfo> webParam = insuranceNingBoParser.getUserInfo(insuranceRequestParameters);
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			if(null != webParam.getInsuranceNingboUserInfo())
			{
				tracer.addTag("parser.getUserInfo.SUCCESS", insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				InsuranceNingboHtml insuranceNingboHtml =new InsuranceNingboHtml();
				insuranceNingboHtml.setHtml(webParam.getHtml());
				insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceNingboHtml.setUrl(webParam.getUrl());
				insuranceNingboHtml.setPageCount(1);
				insuranceNingboHtml.setType("UserInfo");
				insuranceNingboHtmlRepository.save(insuranceNingboHtml);
				//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				//taskInsurance.setCookies(cookieString);
				insuranceNingboUserInfoRepository.save(webParam.getInsuranceNingboUserInfo());
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR", insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				//taskInsurance.setCookies(cookieString);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//医疗保险
	@Async
	public void getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters) {
		try {
			tracer.addTag("parser.getUserInfo.start", insuranceRequestParameters.getTaskId());
			WebParam<InsuranceNingboMedical> webParam = insuranceNingBoParser.getMedicalInfo(insuranceRequestParameters);
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("parser.getUserInfo.SUCCESS", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
					InsuranceNingboHtml insuranceNingboHtml =new InsuranceNingboHtml();
					insuranceNingboHtml.setHtml(webParam.getHtml());
					insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceNingboHtml.setUrl(webParam.getUrl());
					insuranceNingboHtml.setPageCount(1);
					insuranceNingboHtml.setType("MedicalInfo");
					insuranceNingboHtmlRepository.save(insuranceNingboHtml);
					insuranceNingboMedicalRepository.saveAll(webParam.getList());
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
				else
				{
					tracer.addTag("parser.getUserInfo.ERROR", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR", insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				//taskInsurance.setCookies(cookieString);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//养老保险
	@Async
	public void getEndowmentInfo(InsuranceRequestParameters insuranceRequestParameters) {
		try {
			tracer.addTag("parser.getUserInfo.start", insuranceRequestParameters.getTaskId());
			WebParam<InsuranceNingboEndowment> webParam = insuranceNingBoParser.getEndowmentInfo(insuranceRequestParameters);
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("parser.getUserInfo.SUCCESS", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
					InsuranceNingboHtml insuranceNingboHtml =new InsuranceNingboHtml();
					insuranceNingboHtml.setHtml(webParam.getHtml());
					insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceNingboHtml.setUrl(webParam.getUrl());
					insuranceNingboHtml.setPageCount(1);
					insuranceNingboHtml.setType("EndowmentInfo");
					insuranceNingboHtmlRepository.save(insuranceNingboHtml);
					insuranceNingboEndowmentRepository.saveAll(webParam.getList());
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
				else
				{
					tracer.addTag("parser.getUserInfo.ERROR", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR", insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				//taskInsurance.setCookies(cookieString);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//工伤保险
	@Async
	public void getHurtInfo(InsuranceRequestParameters insuranceRequestParameters) {
		try {
			tracer.addTag("parser.getUserInfo.start", insuranceRequestParameters.getTaskId());
			WebParam<InsuranceNingboHurt> webParam = insuranceNingBoParser.getHurtInfo(insuranceRequestParameters);
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("parser.getUserInfo.SUCCESS", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance);
					InsuranceNingboHtml insuranceNingboHtml =new InsuranceNingboHtml();
					insuranceNingboHtml.setHtml(webParam.getHtml());
					insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceNingboHtml.setUrl(webParam.getUrl());
					insuranceNingboHtml.setPageCount(1);
					insuranceNingboHtml.setType("HurtInfo");
					insuranceNingboHtmlRepository.save(insuranceNingboHtml);
					insuranceNingboHurtRepository.saveAll(webParam.getList());
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
				else
				{
					tracer.addTag("parser.getUserInfo.ERROR1", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance);
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR2", insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				//taskInsurance.setCookies(cookieString);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//生育保险
	@Async
	public void getBearInfo(InsuranceRequestParameters insuranceRequestParameters) {
		try {
			tracer.addTag("parser.getUserInfo.start", insuranceRequestParameters.getTaskId());
			WebParam<InsuranceNingboBear> webParam = insuranceNingBoParser.getBearInfo(insuranceRequestParameters);
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("parser.getUserInfo.SUCCESS", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200, taskInsurance);
					InsuranceNingboHtml insuranceNingboHtml =new InsuranceNingboHtml();
					insuranceNingboHtml.setHtml(webParam.getHtml());
					insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceNingboHtml.setUrl(webParam.getUrl());
					insuranceNingboHtml.setPageCount(1);
					insuranceNingboHtml.setType("BearInfo");
					insuranceNingboHtmlRepository.save(insuranceNingboHtml);
					insuranceNingboBearRepository.saveAll(webParam.getList());
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
				else
				{
					tracer.addTag("parser.getUserInfo.ERROR1", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR2", insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				//taskInsurance.setCookies(cookieString);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//失业保险
	@Async
	public void getLostInfo(InsuranceRequestParameters insuranceRequestParameters) {
		try {
			tracer.addTag("parser.getLostInfo.start", insuranceRequestParameters.getTaskId());
			WebParam<InsuranceNingboLost> webParam = insuranceNingBoParser.getLostInfo(insuranceRequestParameters);
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("parser.getLostInfo.SUCCESS", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),200, taskInsurance);
					InsuranceNingboHtml insuranceNingboHtml =new InsuranceNingboHtml();
					insuranceNingboHtml.setHtml(webParam.getHtml());
					insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceNingboHtml.setUrl(webParam.getUrl());
					insuranceNingboHtml.setPageCount(1);
					insuranceNingboHtml.setType("LostInfo");
					insuranceNingboHtmlRepository.save(insuranceNingboHtml);
					insuranceNingboLostRepository.saveAll(webParam.getList());
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
				else
				{
					tracer.addTag("parser.getLostInfo.ERROR1", insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
					//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					//taskInsurance.setCookies(cookieString);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
			}
			else
			{
				tracer.addTag("parser.getLostInfo.ERROR2", insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				//String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				//taskInsurance.setCookies(cookieString);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
