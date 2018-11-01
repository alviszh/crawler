package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.TelecomAnhuiParser;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.anhui")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.anhui")
public class TelecomCommonAnhuiService implements ICrawlerLogin,ISms{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomAnhuiParser telecomAnhuiParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomAnhuiService telecomAnhuiService;
	
	
	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
		
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

//	//爬取
//	public void getAllData(MessageLogin messageLogin, TaskMobile taskMobile) {
//		// 个人信息
//		try {   
//			telecomAnhuiService.getUserInfo(messageLogin, taskMobile);// 成功
//		} catch (Exception e) {
//			tracer.addTag("parser.crawler.auth", "getUserInfo" + e.toString());
//		}
//
//		// 缴费信息
//		try {
//				telecomAnhuiService.getPay(messageLogin, taskMobile);// 成功
//		} catch (Exception e) {
//			tracer.addTag("parser.crawler.auth", "getpayResult" + e.toString());
//		}
//		// 账单
//		try {
//				telecomAnhuiService.getBill(messageLogin, taskMobile);// 成功
//		} catch (Exception e) {
//			tracer.addTag("parser.crawler.auth", "getphoneBill" + e.toString());
//		}
//		
//		//亲情号码
//		telecomAnhuiService.getfamily(messageLogin, taskMobile);
//		// 积分
//		try {
//			telecomAnhuiService.getScore(messageLogin, taskMobile);
//		} catch (Exception e) {
//			tracer.addTag("parser.crawler.auth", "getintegraResult" + e.toString());
//		}
//		// 业务
//		try {
//			telecomAnhuiService.getBusiness(messageLogin, taskMobile);// 成功
//		} catch (Exception e) {
//			tracer.addTag("parser.crawler.auth", "getPointsAndCharges" + e.toString());
//		}
//	}
	
	//登陆
	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		tracer.addTag("login.anhui.start", messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
		    WebParam webParam = telecomAnhuiParser.login(messageLogin, taskMobile);
		    if(null != webParam.getHtml())
		    {
		    	String html = webParam.getHtml();
		    	if(html.contains("密码错误"))
			    {
			    	taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getDescription());
					taskMobile.setTesthtml(messageLogin.getPassword());
					taskMobileRepository.save(taskMobile);
					tracer.addTag("parser.crawler.telecom.login.pwd", messageLogin.getTask_id());
			    }
			    
			    else if(html.contains("验证码错误"))
			    {
					taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhasestatus());
					taskMobile.setDescription("网络异常，请重试");
					taskMobile.setTesthtml(messageLogin.getPassword());
					taskMobileRepository.save(taskMobile);
					tracer.addTag("parser.crawler.telecom.login.img", messageLogin.getTask_id());
			    }
			    else if(html.contains("密码过于简单,请使用随机码登录或者重置密码"))
			    {
			    	taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_SEVEN.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_SEVEN.getPhasestatus());
					taskMobile.setDescription("密码过于简单,请使用随机码登录或者重置密码");
					taskMobile.setTesthtml(messageLogin.getPassword());
					taskMobileRepository.save(taskMobile);
					tracer.addTag("parser.crawler.telecom.login.ERROR.simgple", messageLogin.getTask_id());
			    }
			    else if(html.contains("0")) {
			    	String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setCookies(cookieString);
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
					taskMobile.setTesthtml(messageLogin.getPassword());
					taskMobileRepository.save(taskMobile);
					tracer.addTag("parser.crawler.telecom.login.Success", messageLogin.getTask_id());
				}
		    }
		    else
		    {
				taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getDescription());
				taskMobile.setTesthtml(messageLogin.getPassword());
				taskMobileRepository.save(taskMobile);
				tracer.addTag("parser.crawler.telecom.login.ERROR.timeout", messageLogin.getTask_id());
		    }
		} catch (Exception e) {
			tracer.addTag("parser.crawler.telecom.login.ERROR.TIMEOUT", messageLogin.getTask_id());
			taskMobile.setTesthtml(messageLogin.getPassword());
			taskMobileRepository.save(taskMobile);
			e.printStackTrace();
		}
		return taskMobile;
		
	}

	//第一次发送验证码
	public void getPhoneCodeFirst(MessageLogin messageLogin, TaskMobile taskMobile) {
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
		try {
			WebParam webParam = telecomAnhuiParser.getPhoneCodeFirst(messageLogin,taskMobile);
			if(null != webParam.getHtml())
			{
				if(webParam.getHtml().contains("0"))
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					String cookie = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setCookies(cookie);
					taskMobileRepository.save(taskMobile);
				}
				else if(webParam.getHtml().contains("1"))
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR2.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR2.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR2.getDescription());
				}
				else
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				}
			}
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			e.printStackTrace();
		}
		
	}
	
	
	//第二次发送验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
		try {
			WebParam webParam = telecomAnhuiParser.getphonecodeTwo(messageLogin,taskMobile);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("0"))
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					String cookie = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setNexturl(cookie);
					taskMobileRepository.save(taskMobile);
				}
				else if(webParam.getHtml().contains("-1"))
				{
					if(webParam.getHtml().contains("短信发送限额已满"))
					{
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
						taskMobile.setDescription("短信发送限额已满");
						taskMobileRepository.save(taskMobile);
					}
					else if(webParam.getHtml().contains("一分钟"))
					{
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
						taskMobile.setDescription("短信不能在一分钟多次发送");
						taskMobileRepository.save(taskMobile);
					}
					else
					{
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
						taskMobile.setDescription("网站维护，短信异常，请您关注安徽电信官网，稍后再试");
						String cookie = CommonUnit.transcookieToJson(webParam.getWebClient());
						taskMobile.setNexturl(cookie);
						taskMobileRepository.save(taskMobile);
					}
				}
				else
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
					taskMobileRepository.save(taskMobile);
				}
			}
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			e.printStackTrace();
			taskMobileRepository.save(taskMobile);
		}
		return taskMobile;
	}
	
	/*//第二次发送验证码
		public void getphonecodeTwo(MessageLogin messageLogin, TaskMobile taskMobile) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
			try {
				WebParam webParam = telecomAnhuiParser.getphonecodeTwo(messageLogin,taskMobile);
				if(null != webParam)
				{
					if(webParam.getHtml().contains("true"))
					{
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
						taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
						String cookie = CommonUnit.transcookieToJson(webParam.getWebClient());
						taskMobile.setCookies(cookie);
						taskMobileRepository.save(taskMobile);
					}
					else
					{
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
						taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
					}
				}
			} catch (Exception e) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				e.printStackTrace();
			}
		}*/
	
	

	//第二次爬取
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		// 电话详单
		telecomAnhuiService.getAllCall(messageLogin, taskMobile);
		// 短信详单
		telecomAnhuiService.getSMS(messageLogin, taskMobile);// 成功
		// 个人信息
		telecomAnhuiService.getWapUserInfo(messageLogin, taskMobile);// 成功
	    // 缴费信息
	    telecomAnhuiService.getWapPay(messageLogin, taskMobile);// 成功
	    // 账单
		telecomAnhuiService.getWapBill(messageLogin, taskMobile);// 成功
	    //亲情号码
	    telecomAnhuiService.getfamily(messageLogin, taskMobile);
	    // 积分
		telecomAnhuiService.getWapScore(messageLogin, taskMobile);
	    // 业务
		telecomAnhuiService.getWapBusiness(messageLogin, taskMobile);// 成功
		return taskMobile;
		
	}

	//验证验证码
	@Async
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			telecomAnhuiService.setphonecode(messageLogin,taskMobile);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return taskMobile;
	}

	
	//net端第3次获取验证码  用于爬取详单的
	public void getphonecodeThird(MessageLogin messageLogin, TaskMobile taskMobile) {
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
		try {
			WebParam webParam = telecomAnhuiParser.getphonecodeThird(messageLogin,taskMobile);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("true"))
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					String cookie = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setCookies(cookie);
					taskMobileRepository.save(taskMobile);
				}
				else
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				}
			}
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			e.printStackTrace();
		}
	}

	
	//登陆net
	public void loginNet(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
		    WebParam webParam = telecomAnhuiParser.loginNet(messageLogin, taskMobile);
			if (null != webParam) {
				if(webParam.getHtml().contains("尊敬的"))
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
					taskMobile.setDescription("抱歉您的号码没有进行查询的功能");
					taskMobileRepository.save(taskMobile);
					
					tracer.addTag("parser.crawler.telecom.login.ERROR。whilePage", messageLogin.getTask_id());
				}
				else if(webParam.getHtml().contains("我的189"))
				{
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setNexturl(cookieString);
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getDescription());
					taskMobileRepository.save(taskMobile);
					tracer.addTag("parser.crawler.telecom.login.Success", messageLogin.getTask_id());
				}
				else
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
					taskMobileRepository.save(taskMobile);
					tracer.addTag("parser.crawler.telecom.login.ERROR1", messageLogin.getTask_id());
				}
			}
			else
			{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
				taskMobileRepository.save(taskMobile);
				tracer.addTag("parser.crawler.telecom.login.ERROR2", messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("parser.crawler.telecom.login.TIMEOUT", messageLogin.getTask_id());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
			taskMobileRepository.save(taskMobile);
			tracer.addTag("parser.crawler.telecom.login.TIMEOUT", messageLogin.getTask_id());
			e.printStackTrace();
		}
		
	}

	
	/**
	 * wap   
	 * @param messageLogin
	 * @param 2018/01/05
	 */
	public void getThreeData(MessageLogin messageLogin, TaskMobile taskMobile) {
        // 个人信息
		telecomAnhuiService.getWapUserInfo(messageLogin, taskMobile);// 成功
	    // 缴费信息
	    telecomAnhuiService.getWapPay(messageLogin, taskMobile);// 成功
	    // 账单
		telecomAnhuiService.getWapBill(messageLogin, taskMobile);// 成功
	    //亲情号码
	    telecomAnhuiService.getfamily(messageLogin, taskMobile);
	    // 积分
		telecomAnhuiService.getWapScore(messageLogin, taskMobile);
	    // 业务
		telecomAnhuiService.getWapBusiness(messageLogin, taskMobile);// 成功
		
	}

	

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
