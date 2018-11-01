package app.service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set; 
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.cmcc.domain.json.WebCmccParam;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.cmcc.CmccRemedyParameter;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.cmcc.CmccRemedyParameterRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccUserInfoRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.CookieBean;
import app.client.aws.AwsApiClient;
import app.commontracerlog.TracerLog;
import app.parser.CmccCrawlerParser;
import app.parser.CmccLoginParser;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISmsTwice;
import app.service.aop.impl.CrawlerImpl;
import app.unit.CmccUnit;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.cmcc","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(
		basePackages={"com.microservice.dao.repository.crawler.cmcc","com.microservice.dao.repository.crawler.mobile"})
public class EurekaCmccService  implements ISmsTwice,ICrawlerLogin{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private CmccRemedyParameterRepository cmccRemedyParameterRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private CmccUserInfoRepository cmccUserInfoRepository;
	@Autowired
	private AsyncCmccCallMsgService asyncCmccCallMsgService;
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private CmccCrawlerParser crawlerParser;
	@Autowired
	private CmccLoginParser loginParser;  
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	
	
	@Value("${webdriver.chrome.driver.path}")
	String driverPathChrome;
	
    @Autowired
    private AwsApiClient awsApiClient;

	private WebDriver driver; 
	
	
	@Value("${webdriver.proxy}")
	int useProxy;
	
	private Gson gson = new Gson();
	
	/**
	 * @Description 登录更改状态
	 * @param messageLogin
	 * @return String
	 */
	public TaskMobile loginResult(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
		taskMobile.setTesthtml(gson.toJson(messageLogin));
		taskMobile = taskMobileRepository.save(taskMobile);
		tracer.addTag("----------------loginResult change phase and phase_status------------------","-");
	
		return taskMobile;
		
	}


	/**
	 * @Description 第二次验证 改状态
	 * @param messageLogin
	 * @return String
	 * 此方法转到二次短信验证的切面类
	 */
	/*public TaskMobile secondAttestation(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getDescription());
		taskMobileRepository.save(taskMobile);
		tracer.addTag("secondAttestation ==>","change phase and phase_status");
				
		return taskMobile;
	}*/
	 
	
	/**
	 * @Description 第二次验证
	 * @param messageLogin
	 * @return String
	 */
	@Async
	@Override
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
			
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		Boolean isCorrect = false;
		String codeOCR = null;
		WebCmccParam webCmccParam = null;
		String cookieJson = null;
		Set<Cookie> cookies = transferJsonToSet(taskMobile);
		
		//图片验证码认证3次机制，3次都不成功则错误抛出
		for(int i=0;i<3;i++){
			String imgUrl = "https://shop.10086.cn/i/authImg?t=0.549787972251977";
			codeOCR = chaoJiYingOcrService.getVerifycodeBySelenium(imgUrl, cookies, "1006");
			tracer.addTag("codeOCR   ========>>",codeOCR);
			
			isCorrect = crawlerParser.preCheckCaptcha(codeOCR,cookies,messageLogin.getName());
			if(isCorrect){				
				tracer.addTag("preCheckCaptcha ==>","图片认证成功");
				break;
			}			
		}
		
		if(!isCorrect){
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_IMG_THR_ERROR.getCode());
			taskMobile.setError_message(StatusCodeRec.MOBILE_VERIFY_IMG_THR_ERROR.getMessage());
			taskMobile.setDescription("当前网络波动，请重新再试。");
			taskMobile = taskMobileRepository.save(taskMobile);
		}else{
			webCmccParam = crawlerParser.secondAttestation(messageLogin,cookies,codeOCR);				
			
		}
		if(null != webCmccParam){
			
			if("570005".equals(webCmccParam.getCode_second())){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SEC_SMS_ERROR.getCode());
				taskMobile.setError_message("二次验证随机密码错误！请重新获取！");
				taskMobile.setDescription("二次验证随机密码错误！请重新获取！");
				taskMobile = taskMobileRepository.save(taskMobile);
			}else if("000000".equals(webCmccParam.getCode_second())){
				
				tracer.addTag("secondAttestation ==>","二次认证成功");
							
				cookies = webCmccParam.getWebClient().getCookieManager().getCookies();
				
				Map<String,String> cookieMap = new HashMap<String,String>();
				for(Cookie cookie:cookies){
					cookieMap.put(cookie.getName(), cookie.getValue());
				}
				
				cookieJson = gson.toJson(cookieMap);
				
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_QUERY_SUCCESS.getDescription());
				taskMobile.setCookies(cookieJson);
				taskMobile = taskMobileRepository.save(taskMobile);
				
			}else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SEC_SMS_ERROR.getCode());
				taskMobile.setError_message("网络超时！请稍后再试");
				taskMobile.setDescription("网络超时！请稍后再试");
				taskMobile = taskMobileRepository.save(taskMobile);
			}
		}else{	
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SEC_SMS_ERROR.getCode());
			taskMobile.setError_message("出现未知错误！请稍后再试");
			taskMobile.setDescription("出现未知错误！请稍后再试");
			taskMobile = taskMobileRepository.save(taskMobile);
		}
		
		return taskMobile;
	}
	

	/**
	 * @Des 登录认证(旧登录，已过期)
	 * @param messageLogin
	 */
	/*@Deprecated
	@Async
    public void loginCrawler(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		
		WebCmccParam webCmccParam = loginParser.loginAuthentication(messageLogin);
		if(null != webCmccParam){
			
			if("6001".equals(webCmccParam.getLoginAuthJson().getCode())){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getMessage());
				taskMobile.setDescription(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getMessage());
				taskMobileRepository.save(taskMobile);
			}else if("2036".equals(webCmccParam.getLoginAuthJson().getCode())){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getMessage());
				taskMobile.setDescription(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getMessage());
				taskMobileRepository.save(taskMobile);
			}else if("6002".equals(webCmccParam.getLoginAuthJson().getCode())){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS_OVER.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS_OVER.getMessage());
				taskMobile.setDescription(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS_OVER.getMessage());
				taskMobileRepository.save(taskMobile);
			}else if("2046".equals(webCmccParam.getLoginAuthJson().getCode())){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS_OVER.getCode());
				taskMobile.setError_message("密码输入超过次数账号已锁定，详情垂询10086");
				taskMobile.setDescription("密码输入超过次数账号已锁定，详情垂询10086");
				taskMobileRepository.save(taskMobile);
				
			}else if("9".equals(webCmccParam.getLoginAuthJson().getResult())){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_REPEAT.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_REPEAT.getMessage());
				taskMobile.setDescription(StatusCodeRec.MOBILE_LOGIN_REPEAT.getMessage());
				Set<Cookie> cookies = webCmccParam.getWebClient().getCookieManager().getCookies();
				
				Map<String,String> cookieMap = new HashMap<String,String>();
				for(Cookie cookie:cookies){
					cookieMap.put(cookie.getName(), cookie.getValue());
				}
				
				String cookieJson = gson.toJson(cookieMap);
				taskMobile.setCookies(cookieJson);
				taskMobile.setUpdateTime(new Date());
				taskMobileRepository.save(taskMobile);
			}else if("0000".equals(webCmccParam.getLoginAuthJson().getCode())){
				tracer.addTag("loginAuthentication ==>","success");
				
				WebClient webClient = loginParser.getLoginResult(webCmccParam.getWebClient(),webCmccParam.getLoginAuthJson().getArtifact());
				if(null == webClient){
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SESSION.getCode());
					taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SESSION.getMessage());
					taskMobile.setDescription(StatusCodeRec.MOBILE_LOGIN_ERROR_SESSION.getMessage());
					taskMobileRepository.save(taskMobile);
				}else{
					
					tracer.addTag("getLoginResult ==>","success");
					
					Set<Cookie> cookies = webClient.getCookieManager().getCookies();
					
					Map<String,String> cookieMap = new HashMap<String,String>();
					for(Cookie cookie:cookies){
						cookieMap.put(cookie.getName(), cookie.getValue());
					}
					
					String cookieJson = gson.toJson(cookieMap);
					
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());		
					taskMobile.setCookies(cookieJson);
					taskMobile.setUpdateTime(new Date());
					taskMobileRepository.save(taskMobile);
										
				}
			}else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getMessage());
				taskMobile.setDescription("网络超时，请稍后再试");
				taskMobileRepository.save(taskMobile);
			}
			
		}else{
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getCode());
			taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getMessage());
			taskMobile.setDescription("网络超时，请稍后再试");
			taskMobileRepository.save(taskMobile);
		}
	 
		
    }*/


	/**
	 * @Description: 发送登录短信随机码
	 * @param messageLogin
	 * @return String
	 */
	public TaskMobile sendSms(MessageLogin messageLogin) {
		
/*		注释部分转移到切面类
 * TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
		taskMobile = taskMobileRepository.save(taskMobile);*/ 
		
		String isSend = doSend(messageLogin);
		
//		String isSend = loginParser.sendSMS(messageLogin.getName());
//		tracer.addTag("isSend.html", isSend);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		if(null == isSend){
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY.getCode());
			taskMobile.setError_message("短信发送超时，请稍后再试！");
			taskMobile.setDescription("短信发送超时，请稍后再试！");
			taskMobile = taskMobileRepository.save(taskMobile);
			return taskMobile;
		}else if(isSend.equals("success")){
			tracer.addTag("sendSMS ==>","SUCCESS");
			
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
			taskMobile.setError_code(0);
			taskMobile.setError_message("");
			taskMobile = taskMobileRepository.save(taskMobile);
			
			return taskMobile;
		}else{
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY.getCode());
			taskMobile.setError_message("发送短信验证码失败,请检查!");
			taskMobile.setDescription("发送短信验证码失败,请检查!");
			taskMobile = taskMobileRepository.save(taskMobile);
			return taskMobile;
		}
	}
	  
	/**
	 * @Description: 发送第二次认证短信随机码
	 * @param messageLogin
	 * @return
	 */
	@Async
	public TaskMobile sendSmsTwice(MessageLogin messageLogin) {
		tracer.addTag("第二次认证短信开始发送。", "start");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		long updateTime = taskMobile.getUpdateTime().getTime();
		long currentTime = System.currentTimeMillis();				
		String cookie = taskMobile.getCookies();
		
		long day = currentTime-updateTime;				//50秒间隔
		if(day<=50000){
			try {
				Thread.sleep(50000-day);
				tracer.addTag("第二次发短信等待50秒", "success");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}else{
			tracer.addTag("第二次发送短信updateTime", String.valueOf(updateTime));
			tracer.addTag("第二次发送短信currentTime", String.valueOf(currentTime));
			tracer.addTag("两个时间间隔：", String.valueOf(day));
		}
		
		String code = crawlerParser.sendVerifySMS(cookie,messageLogin);
		if("000000".equals(code)){
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS.getError_code());
			taskMobile = taskMobileRepository.save(taskMobile);
			tracer.addTag("sendVerifySMS ==>","SUCCESS");
		}else if("555001".equals(code)){
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY.getCode());
			taskMobile.setError_message(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY.getMessage());
			taskMobile.setDescription(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY.getMessage());
			taskMobile = taskMobileRepository.save(taskMobile);
		}else if("555002".equals(code)){
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY_TODAY.getCode());
			taskMobile.setError_message(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY_TODAY.getMessage());
			taskMobile.setDescription(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY_TODAY.getMessage());
			taskMobile = taskMobileRepository.save(taskMobile);
		}else if("500003".equals("code")){
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_NEED_LOGIN.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_NEED_LOGIN.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_NEED_LOGIN.getDescription());
			taskMobile = taskMobileRepository.save(taskMobile);
		}else if("550011".equals(code)){
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY_TODAY.getCode());
			taskMobile.setError_message(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY_TODAY.getMessage());
			taskMobile.setDescription("尊敬的用户,请勿在1分钟内重复下发短信");
			taskMobile = taskMobileRepository.save(taskMobile);
		}else{
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_NEED_LOGIN.getError_code());
			taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getMessage());
			taskMobile.setDescription("网络超时，请稍后再试");
			taskMobile = taskMobileRepository.save(taskMobile);
			
		}
			
		return taskMobile;
	}
	
	
	public Set<Cookie> transferJsonToSet(TaskMobile taskMobile){
		String cookieJson = taskMobile.getCookies();
		Set<Cookie> set = new HashSet<Cookie>();
		Gson gson = new Gson();
	    Type cookieType = new TypeToken<List<CookieBean>>(){}.getType();
	    List<CookieBean> cookies = gson.fromJson(cookieJson,cookieType);
	    for(CookieBean cookieBean : cookies){
	    	Cookie cookie = new Cookie(cookieBean.getDomain(),cookieBean.getKey(),cookieBean.getValue());
	    	set.add(cookie);
	    }
		return set;
		
	}


	/**
	 * @Description: 获取用户信息(积分、实时金额、账户状态)
	 * @param messageLogin
	 * @return String
	 */
	public void getUserMessage(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		Set<Cookie> cookies = transferJsonToSet(taskMobile);
		Set<Cookie> cookies = CmccUnit.transferJsonToSet(taskMobile);
	
		WebCmccParam webCmccParam = crawlerParser.getUserMessage(messageLogin,cookies);
		
		if(403 == webCmccParam.getCode()){
			taskMobile.setError_code(403);
			taskMobile.setUserMsgStatus(201);
			taskMobile.setAccountMsgStatus(201);
			taskMobile.setBusinessMsgStatus(201);
			taskMobile.setIntegralMsgStatus(201);
			taskMobile.setFamilyMsgStatus(201);
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getDescription());
			taskMobileRepository.save(taskMobile);
			
		}
		
		if(null != webCmccParam && null != webCmccParam.getCmccUserInfo()){
			cmccUserInfoRepository.save(webCmccParam.getCmccUserInfo());	  		
			tracer.addTag("getUserMessage","用户信息入库");
			taskMobile.setUserMsgStatus(webCmccParam.getCode());
			taskMobile.setAccountMsgStatus(webCmccParam.getAccountMsgStatus());
			taskMobile.setBusinessMsgStatus(webCmccParam.getBusiPlanStatus());
			taskMobile.setIntegralMsgStatus(webCmccParam.getPointValueCode());
			taskMobile.setFamilyMsgStatus(201);
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			try{
				asyncCmccCallMsgService.getCallRecord(messageLogin);				
			}catch(Exception e){
				tracer.addTag("crawler.cmcc.callrecord.error",e.getMessage());
			}
		}else{
			taskMobile.setError_code(403);
			taskMobile.setUserMsgStatus(201);
			taskMobile.setAccountMsgStatus(201);
			taskMobile.setBusinessMsgStatus(201);
			taskMobile.setIntegralMsgStatus(201);
			taskMobile.setFamilyMsgStatus(201);
			taskMobile.setError_message("超时！");
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getDescription());
			taskMobileRepository.save(taskMobile);
			
			try{
				asyncCmccCallMsgService.getCallRecord(messageLogin);				
			}catch(Exception e){
				tracer.addTag("crawler.cmcc.callrecord.error",e.getMessage());
			}
		}
		
	}

	
	public void changeStatus(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobileRepository.save(taskMobile);
	}


	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		if(null == taskMobile){
			return true;
		}
		if("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())){
			return true;
		}
		return false;
	}


	public void updateStatus(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
		taskMobileRepository.save(taskMobile);
	} 

	/**
	 * 通过selenium登录
	 * @param messageLogin
	 */
	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		tracer.addTag("taskid", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		
		driver = getHtml(messageLogin);
		tracer.addTag("点击过后页面源码：", "<xmp>"+driver.getPageSource()+"</xmp>");
		tracer.addTag("点击过后页面url：", "<xmp>"+driver.getCurrentUrl()+"</xmp>");
		if(null == driver){
			tracer.addTag("driver","null");
			System.out.println("-----------driver为空-----------");
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getCode());
			taskMobile.setError_message("网络超时，请稍后再试!");
			taskMobile.setDescription("网络超时，请稍后再试!");
			taskMobileRepository.save(taskMobile);
			driver.quit();
		}else{
			
			if(driver.getPageSource().contains("短信随机码不正确或已过期")){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getMessage());
				taskMobile.setDescription(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getMessage());
				taskMobileRepository.save(taskMobile);
				driver.quit();

//			}else if(driver.getPageSource().contains("7次后您的账户将被锁定")){
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
//				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getCode());
//				taskMobile.setError_message("对不起，您的账户被锁定，24小时候后可重新登录，如有问题请咨询客服。");
//				taskMobile.setDescription("对不起，您的账户被锁定，24小时候后可重新登录，如有问题请咨询客服。");
//				taskMobileRepository.save(taskMobile);
//				driver.quit();
			}else if(driver.getPageSource().contains("您的账户名与密码不匹配")){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getMessage());
				taskMobile.setDescription(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getMessage());
				taskMobileRepository.save(taskMobile);
				driver.quit();
			}else if(driver.getPageSource().contains("密码输入超过次数账号已锁定")){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS_OVER.getCode());
				taskMobile.setError_message("密码输入超过次数账号已锁定，详情垂询10086");
				taskMobile.setDescription("密码输入超过次数账号已锁定，详情垂询10086");
				taskMobileRepository.save(taskMobile);
				driver.quit();
			}else if(driver.getCurrentUrl().contains("//shop.10086.cn/i")){
				WebClient webClient = WebCrawler.getInstance().getNewWebClient();
				Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
				for(org.openqa.selenium.Cookie cookie : cookiesDriver){
					Cookie cookieWebClient = new Cookie("shop.10086.cn", cookie.getName(), cookie.getValue());
					webClient.getCookieManager().addCookie(cookieWebClient);
				}
				
				String cookieJson = CommonUnit.transcookieToJson(webClient);
				
				taskMobile.setCookies(cookieJson);
				taskMobile.setUpdateTime(new Date());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
				taskMobileRepository.save(taskMobile);
				tracer.addTag("登录成功selenium", "success");
				driver.quit();
			}else{
				tracer.addTag("driver","null");
				System.out.println("-----------driver为空-----------");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getCode());
				taskMobile.setError_message("网络超时，请稍后再试.");
				taskMobile.setDescription("网络超时，请稍后再试.");
				taskMobileRepository.save(taskMobile);
				driver.quit();
			}
		}
		
		return taskMobile;
	}
	
	private String doSend(MessageLogin messageLogin){
		String url = "https://login.10086.cn/login.html";
		try {
			driver = getPageByChrome(url);
			tracer.addTag("selenium打开登录页面成功-发送短信", messageLogin.getTask_id());
			//点击短信随机码登录选择项
			WebElement loginType = driver.findElement(By.id("sms_login_1"));
			loginType.click();		
			//手机号输入框
			WebElement phone = driver.findElement(By.id("sms_name"));
			phone.sendKeys(messageLogin.getName());
			//点击噶送短信按钮
			WebElement button = driver.findElement(By.id("getSMSPwd1"));
			button.click();
			
			driver.close();
		} catch (Exception e) {
			tracer.addTag("发送短信出现异常", e.getMessage());
			return null;
		}
		return "success";
	}


	private WebDriver getHtml(MessageLogin messageLogin) {
		
		//String loginUrl = "https://login.10086.cn/login.html";// modify by meidi 20180710 增加backUrl参数，否则页面登录成功后会陷入死循环，增加channelID，否则登录一直提示系统繁忙
		String loginUrl = "https://login.10086.cn/login.html?channelID=12003&backUrl=https://shop.10086.cn/i/?f=billdetailqry";
		try {
			driver = getPageByChrome(loginUrl);
			tracer.addTag("selenium打开登录页面成功", messageLogin.getTask_id());
		} catch (Exception e2) {
			tracer.addTag("打开登录页面出现异常", "selenium");
			tracer.addTag("selenium打开登录页面失败。", e2.getMessage());
			TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getCode());
			taskMobile.setError_message("网络超时，请稍后再试");
			taskMobile.setDescription("网络超时，请稍后再试");
			taskMobileRepository.save(taskMobile);
			driver.quit();
		}
		
		//手机号输入框
		WebElement phone = driver.findElement(By.id("p_name"));
		tracer.addTag("手机号输入框：", phone.getText());
		//密码输入框
		WebElement password = driver.findElement(By.id("p_pwd"));
		tracer.addTag("密码输入框：", password.getText());
		//点击登录按钮
		WebElement button = driver.findElement(By.id("submit_bt"));
		tracer.addTag("点击登录按钮：", button.getText());
				
		phone.sendKeys(messageLogin.getName());
		password.sendKeys(messageLogin.getPassword());
		button.click();
		
		//短信输入框	
		try{  
			//等待10秒，如果还没有登录成功的标识div#mainFrame(跳转到登录成功页面) 则表示登录失败
	        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
	                .pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
	        WebElement sms = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sms_pwd")));
			//WebDriverWait wait = new WebDriverWait(driver, 30);  
			//WebElement sms = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sms_pwd")));			
			sms.sendKeys(messageLogin.getSms_code());
			tracer.addTag("短信随机码   ====》",messageLogin.getSms_code());
			tracer.addTag("短信输入框   ====》",sms.getText());
			button.click();
		}catch(Exception e){
			tracer.addTag("第一次点击后的url： ", driver.getCurrentUrl());
			tracer.addTag("短信输入框获取出错了。", e.getMessage());
			TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getCode());
			taskMobile.setError_message("网络超时，请稍后再试");
			taskMobile.setDescription("网络超时，请稍后再试");
			taskMobileRepository.save(taskMobile);
			driver.quit();
		}		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		return driver;
	}
	
	public WebDriver getPageByChrome(String url) throws Exception{

		System.out.println("launching chrome browser"+driverPathChrome);
		System.setProperty("webdriver.chrome.driver", driverPathChrome);

		if(driverPathChrome==null){
			tracer.addTag("WebDriverIEService initChrome RuntimeException", "webdriver.chrome.driver 初始化失败！");
			System.out.println("webdriver.chrome.driver 初始化失败！");
			throw new RuntimeException("webdriver.chrome.driver 初始化失败！");
		}


		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		//chromeOptions.addArguments("disable-gpu");
		
		if(useProxy==1){
			Proxy proxy = new Proxy();
			HttpProxyBean httpProxyBean = getProxy();
            String PROXY = ""+httpProxyBean.getIp()+":"+httpProxyBean.getPort()+"";
            System.out.println("使用代理IP  "+PROXY);
            proxy.setHttpProxy(PROXY);
            proxy.setFtpProxy(PROXY);
            proxy.setSslProxy(PROXY);
            capabilities.setCapability(CapabilityType.PROXY, proxy);
		}else{
			System.out.println("未使用代理IP  ");
		}

		driver = new ChromeDriver(capabilities);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		driver.get(url);


		return driver;

	}


	/**
	 * 用于请求补救措施
	 * @param task_id
	 * @param type			类型
	 * @param url			
	 */
	public void addRemedy(String task_id, String type, String url,String year) {
		CmccRemedyParameter parameter = new CmccRemedyParameter();
		parameter.setType(type);
		parameter.setUrl(url);
		parameter.setTaskId(task_id);	
		parameter.setYear(year);
		cmccRemedyParameterRepository.save(parameter);
		tracer.addTag("补救的url已入库", url);
		
	}
	
	 //获取代理IP、端口
    public HttpProxyBean getProxy(){
    	HttpProxyBean httpProxyBean = awsApiClient.getProxy();
        return httpProxyBean;
    }
 

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		tracer.addTag("@Async getAllData", messageLogin.toString());
		tracer.addTag("taskid",messageLogin.getTask_id());	
		try{		
			tracer.addTag("EurekaCmccService getUserMessage",messageLogin.getTask_id());			
			getUserMessage(messageLogin);					
		}catch(Exception e){
			tracer.addTag("EurekaCmccService getUserMessage",e.getMessage());
			updateStatus(messageLogin);
		}
		
		TaskMobile taskMobile = crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	
		tracer.addTag("EurekaCmccService getAllData",taskMobile.toString());
		return taskMobile;
	}


	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


	


	 
	

}
