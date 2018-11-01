package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBill;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBusiness;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiCall;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiHtml;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiMessage;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiPay;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiScore;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryBill;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryBusiness;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryCall;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryHtml;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryMessage;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryPay;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryScore;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryUserInfo;

import app.commontracerlog.TracerLog;
import app.domain.crawler.WebParam;
import app.parser.TelecomGuangxiParser;


@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guangxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guangxi")
public class TelecomGuangxiService {
	@Autowired
	private CrawlerStatusMobileService c;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomGuangxiParser telecomGuangxiParser;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomGuangxiRepositoryBusiness telecomGuangxiRepositoryBusiness;
	@Autowired
	private TelecomGuangxiRepositoryBill telecomGuangxiRepositoryBill;
	@Autowired
	private TelecomGuangxiRepositoryCall telecomGuangxiRepositoryCall;
	@Autowired
	private TelecomGuangxiRepositoryHtml telecomGuangxiRepositoryHtml;
	@Autowired
	private TelecomGuangxiRepositoryMessage telecomGuangxiRepositoryMessage;
	@Autowired
	private TelecomGuangxiRepositoryPay telecomGuangxiRepositoryPay;
	@Autowired
	private TelecomGuangxiRepositoryScore telecomGuangxiRepositoryScore;
	@Autowired
	private TelecomGuangxiRepositoryUserInfo telecomGuangxiRepositoryUserInfo;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	
	
	public String getFirstDay(String fmt,int i) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -i);  
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	// 登陆
	public TaskMobile login(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			WebParam webParam = telecomGuangxiParser.loginlogin(messageLogin, taskMobile);
			if (null != webParam.getHtml()) {
				Document doc = Jsoup.parse((webParam.getHtml()));
					if(doc.toString().contains("不同意"))
					{
						taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_EIGHT.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_EIGHT.getPhasestatus());
						taskMobile.setDescription("请您先去电信网站登陆，并同意用户协议，谢谢");
						taskMobile.setTesthtml(messageLogin.getPassword());
					}
					else{
						if (webParam.getHtml().indexOf("密码过于简单") != -1) {
							taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_SEVEN.getPhase());
							taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_SEVEN.getPhasestatus());
							taskMobile.setDescription("密码过于简单");
							taskMobile.setTesthtml(messageLogin.getPassword());
							taskMobileRepository.save(taskMobile);
						}
						else if((webParam.getHtml().contains("UAM")))
						{
							taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhase());
							taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhasestatus());
							taskMobile.setDescription("UAM密码认证失败！密码错误");
							taskMobile.setTesthtml(messageLogin.getPassword());
							taskMobileRepository.save(taskMobile);
						}
						else if((webParam.getHtml().contains("如连续错误")))
						{
							taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhase());
							taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhasestatus());
							taskMobile.setDescription("对不起，您已经连续输错了多次密码，如连续错误达5次，您的账号将被限制登陆6小时。");
							taskMobile.setTesthtml(messageLogin.getPassword());
							taskMobileRepository.save(taskMobile);
						}
						else if((webParam.getHtml().contains("被限制登陆")))
						{
							taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhase());
							taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhasestatus());
							taskMobile.setDescription("对不起，您已经连续5次输错密码。您的账号在6小时内被限制登陆。");
							taskMobile.setTesthtml(messageLogin.getPassword());
							taskMobileRepository.save(taskMobile);
						}
						else if (webParam.getHtml().contains(messageLogin.getName())) {
							String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
							tracer.addTag("parser.setcookie.login", taskMobile.getTaskid());
							taskMobile.setCookies(cookieString);
							taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
							taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
							taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
//							taskMobile.setTesthtml(messageLogin.getPassword());
							taskMobile.setTrianNum(Integer.parseInt(webParam.getUrl()));
							taskMobile.setTesthtml(webParam.getProdType());
							taskMobileRepository.save(taskMobile);
							tracer.addTag("parser.crawler.telecom.login.Success", messageLogin.getTask_id());
						}
					}
				}
				else
				{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
					taskMobile.setTesthtml(messageLogin.getPassword());
					taskMobileRepository.save(taskMobile);
				}
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getDescription());
			taskMobile.setTesthtml(messageLogin.getPassword());
			taskMobileRepository.save(taskMobile);
			tracer.addTag("parser.crawler.telecom.login.ERROR", messageLogin.getTask_id());
			e.printStackTrace();
		}
		return taskMobile;

	}
	
	
	// 2获取验证码
	public TaskMobile sendSmsTwice(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());

//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
//				// 发送验证码状态更新
//				save(taskMobile);
			WebParam<TelecomGuangxiParser> webParam = telecomGuangxiParser.getphonecodeTwo(messageLogin, taskMobile);

			if (webParam == null) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				taskMobileRepository.save(taskMobile);
			} else if (webParam.getHtml().contains("0")) {
				tracer.addTag("电信获取验证码---taskId:" + taskMobile.getTaskid(), webParam.getHtml());
				WebClient webClient = webParam.getWebClient();
				String cookie = CommonUnit.transcookieToJson(webClient);
				
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
				tracer.addTag("parser.setcookie.getphonecodeTwo", taskMobile.getTaskid());
				taskMobile.setCookies(cookie);
				// 发送验证码状态更新
				taskMobileRepository.save(taskMobile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomGuangxiService.getPhoneCode---ERROR",taskMobile.getTaskid() + "---ERROR:" + e.toString());
			
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
		}
		return taskMobile;

	}
	
	
	// 验证验证码2
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
//			// 手机验证码验证状态更新
//			save(taskMobile);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String endtime = dateFormat.format(calendar.getTime());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
		String starttime = dateFormat.format(calendar.getTime());

		try {
			WebParam webParam = telecomGuangxiParser.setphonecodeTwo(taskMobile, messageLogin, starttime, endtime);
			tracer.addTag("TelecomGuangxiService.verificationcode", taskMobile.getTaskid() + "---htmlpage:" + webParam.getHtml());
			
			if (webParam.getHtml().contains("验证码错误，请重新获取")) {
				tracer.addTag("parser.TelecomSetPhone.SMS.ERROR", taskMobile.getTaskid());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription("验证码错误，请重新获取");
				taskMobileRepository.save(taskMobile);
			} 
			else if(webParam.getHtml().contains("对不起,您输入的名字或证件号码不正确，请重新输入"))
			{
				tracer.addTag("parser.TelecomSetPhone.Name.ERROR", taskMobile.getTaskid());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("对不起,您输入的名字或证件号码不正确，请重新输入");
				taskMobileRepository.save(taskMobile);
			}
			else if(webParam.getHtml().contains("系统繁忙，请稍候重试")){
				tracer.addTag("parser.TelecomSetPhone.SMS.SUCCESS", taskMobile.getTaskid());
				//String url2 = "http://gx.189.cn/chaxun/iframe/user_center.jsp?SERV_NO=FCX-2";// 主界面
				//HtmlPage page2 = webClient.getPage(url2);
				//webParam.setHtml(page2.getWebResponse().getContentAsString());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhasestatus());
				taskMobile.setDescription("账号验证成功！");
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				tracer.addTag("parser.TelecomSetPhone.SMS.SUCCESS.cookies", cookies);
//					System.out.println("-------------------======================="+cookies+"====================----------------------");
				tracer.addTag("parser.setcookie.setphonecodeTwo", taskMobile.getTaskid());
				taskMobile.setCookies(cookies);
				taskMobileRepository.save(taskMobile);
				
			}
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
			e.printStackTrace();
			tracer.addTag("TelecomGuangxiService.verificationcode---ERROR",taskMobile.getTaskid() + "---ERROR:" + e.toString());
			taskMobileRepository.save(taskMobile);
		}
		return taskMobile;
	}
	
	
	//第一次获取验证码
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());

			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
			WebParam<TelecomGuangxiParser> webParam = telecomGuangxiParser.getPhoneCodeFirst(messageLogin, taskMobile);

			if (webParam == null) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				taskMobileRepository.save(taskMobile);
			} else if (webParam.getHtml().contains("0")) {
				tracer.addTag("电信获取验证码---taskId:" + taskMobile.getTaskid(), webParam.getHtml());
				WebClient webClient = webParam.getWebClient();
				String cookie = CommonUnit.transcookieToJson(webClient);
				
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
				tracer.addTag("parser.setNexturl.getphonecode", taskMobile.getTaskid());
				taskMobile.setNexturl(cookie);
				// 发送验证码状态更新
				taskMobileRepository.save(taskMobile);
			}
			else if(webParam.getHtml().contains("您短时间内不能重复获取随机密码"))
			{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("对不起，您短时间内不能获取随机密码，请等候五分钟获取");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				taskMobileRepository.save(taskMobile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomGuangxiService.getPhoneCode---ERROR",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
			// TODO Auto-generated catch block
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
		}
		return taskMobile;

	}
	
	// 第1次验证
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		// 手机验证码验证状态更新
		taskMobileRepository.save(taskMobile);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String endtime = dateFormat.format(calendar.getTime());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
		String starttime = dateFormat.format(calendar.getTime());

		try {
			WebParam webParam = telecomGuangxiParser.setphonecodeFirst(taskMobile, messageLogin, starttime, endtime);
			tracer.addTag("TelecomGuangxiService.verificationcode", taskMobile.getTaskid() + "---htmlpage:" + webParam.getHtml());
			if (null == webParam.getHtml() | webParam.getHtml().contains("用户未登录") | webParam.getHtml().contains("系统忙")) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 手机验证码验证失败状态更新
				taskMobileRepository.save(taskMobile);
			}
			else if(webParam.getHtml().contains("您短时间内不能重复获取随机密码"))
			{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(webParam.getHtml());
				// 手机验证码验证成功状态更新
				taskMobileRepository.save(taskMobile);
			}
			else if (webParam.getHtml().contains("2")) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhasestatus());
				taskMobile.setDescription("账号验证成功！");
				String cookie = CommonUnit.transcookieToJson(webParam.getWebClient());
				tracer.addTag("parser.setNexturl.setphonecodeFirst", taskMobile.getTaskid());
				taskMobile.setNexturl(cookie);
				// 手机验证码验证成功状态更新
				taskMobileRepository.save(taskMobile);
				getCrawlerFirst(messageLogin, taskMobile);
			} else {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhasestatus());
				taskMobile.setDescription("账号验证成功！");
				// 手机验证码验证成功状态更新
				taskMobileRepository.save(taskMobile);
				getCrawlerFirst(messageLogin, taskMobile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomGuangxiService.verificationcode---ERROR",taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}
		return taskMobile;
	}
	
	
	//第一次爬取
	public void getCrawlerFirst(MessageLogin messageLogin, TaskMobile taskMobile) {
        // 个人信息
		try {
			getUserInfo(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getUserInfo" + e.toString());
		}

		// 缴费信息
		try {
			getPay(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getpayResult" + e.toString());
		}
		// 账单
		try {
			getBill(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getphoneBill" + e.toString());
		}
	}
	
	//短信详单
	@Async
	public void getSMS(MessageLogin messageLogin, TaskMobile taskMobile) {
		       int i = 0;
		try {
			   int time=0;
			for (i = 0; i <6; i++) {
				WebParam<TelecomGuangxiMessage> webParam = telecomGuangxiParser.getSMS(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					tracer.addTag("parser.telecom.crawler.getSMS", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
					tracer.addTag("parser.telecom.crawler.getSMS.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getSMS.taskmobile", taskMobile.toString());
					if(null ==webParam.getList())
					{
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",i),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "无数据", 1);
//						mobileDataErrRecRepository.save(m);
						//taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_ERROR.getDescription());
						tracer.addTag("parser.telecom.crawler.getSMS.ERROR",i+messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
					}
					else
					{
						TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
						telecomGuangxiHtml.setHtml(webParam.getHtml());
						telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
						telecomGuangxiHtml.setPageCount(1);
						telecomGuangxiHtml.setType("getSMS");
						telecomGuangxiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomGuangxiHtml);
						telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
						//taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
						telecomGuangxiRepositoryMessage.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getSMS.SUCCESS",i+ messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id()); 
						time++;
					}
				}
				else
				{
//					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",i),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时1", 1);
//					mobileDataErrRecRepository.save(m);
					//taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_ERROR.getDescription());
					tracer.addTag("parser.telecom.crawler.getSMS.ERROR1",i+messageLogin.getTask_id());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time==0)
			{
				taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getSMS.ERROR2",messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getSMS.SUCCESS",messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id()); 
			}
			
		} catch (Exception e) {
			MobileDataErrRec m = null;
			try {
				m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",i),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时2", 1);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mobileDataErrRecRepository.save(m);
			taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getSMS.ERROR3",i+messageLogin.getTask_id()+e.toString());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		
	}
	
	//业务
	@Async
	public void getBusiness(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			WebParam<TelecomGuangxiBusiness> webParam = telecomGuangxiParser.getBusiness(messageLogin,taskMobile);
			if(null != webParam)
			{
				tracer.addTag("parser.telecom.crawler.getBusiness", messageLogin.getTask_id());
				taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
				tracer.addTag("parser.telecom.crawler.getBusiness.html", "<xmp>" + webParam.getHtml() + "</xmp>");
				tracer.addTag("parser.telecom.crawler.getBusiness.taskmobile", taskMobile.toString());
				if(null ==webParam.getList())
				{
					taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_ERROR.getDescription());
					tracer.addTag("parser.telecom.crawler.getBusiness.ERROR1",messageLogin.getTask_id());
					c.updateTaskMobile(messageLogin.getTask_id());
				}
				else
				{
					TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
					telecomGuangxiHtml.setHtml(webParam.getHtml());
					telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
					telecomGuangxiHtml.setPageCount(1);
					telecomGuangxiHtml.setType("getBusiness");
					telecomGuangxiHtml.setUrl(webParam.getUrl());
					System.out.println(telecomGuangxiHtml);
					telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
					taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
					telecomGuangxiRepositoryBusiness.saveAll(webParam.getList());
					tracer.addTag("parser.telecom.crawler.getBusiness.SUCCESS", messageLogin.getTask_id());
					c.updateTaskMobile(messageLogin.getTask_id());  
				}
			}else
			{
				taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getBusiness.ERROR2",messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getBusiness.ERROR3",messageLogin.getTask_id()+e.toString());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
	}
	
	
	//个人信息
	@Async
	public void getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			WebParam<TelecomGuangxiUserInfo> webParam = telecomGuangxiParser.getUserInfo(messageLogin,taskMobile);
			if(null != webParam)
			{
				tracer.addTag("parser.telecom.crawler.getUserInfo", messageLogin.getTask_id());
				taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//				tracer.addTag("parser.telecom.crawler.getUserInfo.html", "<xmp>" + webParam.getHtml() + "</xmp>");
				tracer.addTag("parser.telecom.crawler.getUserInfo.taskmobile", taskMobile.toString());
				if(webParam.getTelecomGuangxiUserInfo()==null)
				{
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR1",messageLogin.getTask_id());
				}
				else
				{
					TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
					telecomGuangxiHtml.setHtml(webParam.getHtml());
					telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
					telecomGuangxiHtml.setPageCount(1);
					telecomGuangxiHtml.setType("getUserInfo");
					telecomGuangxiHtml.setUrl(webParam.getUrl());
					telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
					telecomGuangxiRepositoryUserInfo.save(webParam.getTelecomGuangxiUserInfo());
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getUserInfo.SUCCESS", messageLogin.getTask_id());
				}
			}
			else
			{
				taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR2",messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR3",messageLogin.getTask_id()+e.toString());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}
	
	
	//缴费信息
	@Async
	public void getPay(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int j = 0; j < 6; j++) {
				WebParam<TelecomGuangxiPay> webParam = telecomGuangxiParser.getPay(messageLogin,taskMobile,j);
				if(null != webParam)
				{
					tracer.addTag("parser.telecom.crawler.getPay", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//					tracer.addTag("parser.telecom.crawler.getPay.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getPay.taskmobile", taskMobile.toString());
					if(webParam.getList()==null)
					{
						//taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
						tracer.addTag("parser.telecom.crawler.getPay.ERROR1",messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
					}
					else
					{
						TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
						telecomGuangxiHtml.setHtml(webParam.getHtml());
						telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
						telecomGuangxiHtml.setPageCount(1);
						telecomGuangxiHtml.setType("getPay");
						telecomGuangxiHtml.setUrl(webParam.getUrl());
//						System.out.println(telecomGuangxiHtml);
						telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
						telecomGuangxiRepositoryPay.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getPay.SUCCESS1", messageLogin.getTask_id());
						time++;
					}
				}
				else
				{
					//taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getPay.ERROR2",messageLogin.getTask_id());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time>0)
			{
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getPay.SUCCESS2", messageLogin.getTask_id());
//				c.updateTaskMobile(messageLogin.getTask_id()); 
			}
			else
			{
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getPay.ERROR3",messageLogin.getTask_id());
//				c.updateTaskMobile(messageLogin.getTask_id());
			}
		}	catch (Exception e) {
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getPay.ERROR4",messageLogin.getTask_id()+e.toString());
//			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}
	
	
	//账单
	@Async
	public void getBill(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int i = 0; i < 6; i++) {
				WebParam<TelecomGuangxiBill> webParam = telecomGuangxiParser.getBill(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					tracer.addTag("parser.telecom.crawler.getBill", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//					tracer.addTag("parser.telecom.crawler.getBill.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getBill.taskmobile", taskMobile.toString());
					if(null ==webParam.getList())
					{
						//taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
						tracer.addTag("parser.telecom.crawler.getBill.ERROR1",messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
					}
					else
					{
						TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
						telecomGuangxiHtml.setHtml(webParam.getHtml());
						telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
						telecomGuangxiHtml.setPageCount(1);
						telecomGuangxiHtml.setType("getBill");
						telecomGuangxiHtml.setUrl(webParam.getUrl());
//						System.out.println(telecomGuangxiHtml);
						telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
						telecomGuangxiRepositoryBill.saveAll(webParam.getList());
						//taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
						tracer.addTag("parser.telecom.crawler.getBill.SUCCESS1", messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
						time++;
					}
				}else
				{
					//taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getBill.ERROR2",messageLogin.getTask_id());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time>0)
			{
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getBill.SUCCESS2", messageLogin.getTask_id());
//				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getBill.ERROR3",messageLogin.getTask_id());
//				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getBill.ERROR4",messageLogin.getTask_id()+e.toString());
//			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}
	
//	//余额
//	@Async
//	public void getBalance(MessageLogin messageLogin, TaskMobile taskMobile) {
//		try {
//			WebParam<TelecomGuangxiUserInfo> webParam = telecomGuangxiParser.getBalance(messageLogin,taskMobile);
//			if(null !=webParam)
//			{
//				tracer.addTag("parser.telecom.crawler.getBalance", messageLogin.getTask_id());
//				taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//				tracer.addTag("parser.telecom.crawler.getBalance.html", "<xmp>" + webParam.getHtml() + "</xmp>");
//				tracer.addTag("parser.telecom.crawler.getBalance.taskmobile", taskMobile.toString());
//				if(null == webParam.getTelecomGuangxiUserInfo())
//				{
//					taskMobile.setDescription("数据采集中，【余额信息】爬取完成");
//					tracer.addTag("parser.telecom.crawler.getBalance.ERROR",messageLogin.getTask_id());
//				}
//				else
//				{
//					TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
//					telecomGuangxiHtml.setHtml(webParam.getHtml());
//					telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
//					telecomGuangxiHtml.setPageCount(1);
//					telecomGuangxiHtml.setType("getBalance");
//					telecomGuangxiHtml.setUrl(webParam.getUrl());
//					System.out.println(telecomGuangxiHtml);
//					telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
//					telecomGuangxiRepositoryUserInfo.save(webParam.getTelecomGuangxiUserInfo());
//					taskMobile.setDescription("数据采集中，【余额信息】爬取成功");
//					tracer.addTag("parser.telecom.crawler.getUserInfo.SUCCESS", messageLogin.getTask_id());
//				}
//			}else
//			{
//				taskMobile.setDescription("数据采集中，【余额信息】爬取完成");
//				tracer.addTag("parser.telecom.crawler.getBalance.ERROR",messageLogin.getTask_id());
//			}
//		} catch (Exception e) {
//			taskMobile.setDescription("数据采集中，【余额信息】爬取完成");
//			tracer.addTag("parser.telecom.crawler.getBalance.ERROR",messageLogin.getTask_id());
//			e.printStackTrace();
//		}
//		
//	}
	
	//积分
	@Async
	public void getScore(MessageLogin messageLogin, TaskMobile taskMobile) {
//		try {
//			WebParam<TelecomGuangxiScore> webParam = telecomGuangxiParser.getScore(messageLogin,taskMobile);
//			if(null != webParam)
//			{
//				tracer.addTag("parser.telecom.crawler.getScore", messageLogin.getTask_id());
//				taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//				tracer.addTag("parser.telecom.crawler.getScore.html", "<xmp>" + webParam.getHtml() + "</xmp>");
//				tracer.addTag("parser.telecom.crawler.getScore.taskmobile", taskMobile.toString());
//				if(null != webParam.getHtml()){
//					TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
//					telecomGuangxiHtml.setHtml(webParam.getHtml());
//					telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
//					telecomGuangxiHtml.setPageCount(1);
//					telecomGuangxiHtml.setType("getScore");
//					telecomGuangxiHtml.setUrl(webParam.getUrl());
//					telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
//				}
//				if(null ==webParam.getList())
//				{
//					
//					taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
//					tracer.addTag("parser.telecom.crawler.getScore.ERROR1",messageLogin.getTask_id());
//					c.updateTaskMobile(messageLogin.getTask_id());
//				}
//				else
//				{
//					telecomGuangxiRepositoryScore.saveAll(webParam.getList());
//					taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
//					tracer.addTag("parser.telecom.crawler.getScore.SUCCESS", messageLogin.getTask_id());
//					c.updateTaskMobile(messageLogin.getTask_id());
//				}
//			}else
//			{
//				taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
//				tracer.addTag("parser.telecom.crawler.getScore.ERROR2",messageLogin.getTask_id());
//				c.updateTaskMobile(messageLogin.getTask_id());
//			}
//		} catch (Exception e) {
//			taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
//			tracer.addTag("parser.telecom.crawler.getScore.ERROR3",messageLogin.getTask_id()+e.toString());
//			c.updateTaskMobile(messageLogin.getTask_id());
//			e.printStackTrace();
//		}
		taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
		tracer.addTag("parser.telecom.crawler.getScore.ERROR", messageLogin.getTask_id());
		c.updateTaskMobile(messageLogin.getTask_id()); 
	}

	//电话详单   
	@Async
	public void getAllCall(MessageLogin messageLogin, TaskMobile taskMobile) {
		int time=0;
		for (int i = 0; i < 6; i++) {
             try {
				WebParam<TelecomGuangxiCall> webParam = telecomGuangxiParser.getCall(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					tracer.addTag("parser.telecom.crawler.getCall", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//					tracer.addTag("parser.telecom.crawler.getCall.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getCall.taskmobile", taskMobile.toString());
					if(null ==webParam.getList())
					{
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "漫游通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
//						mobileDataErrRecRepository.save(m);
						taskMobile.setDescription("数据采集中，【本地通话详单 】爬取完成");
		                //taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
						tracer.addTag("parser.telecom.crawler.getCall1.ERROR",i+messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
					}
					else
					{
						TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
						telecomGuangxiHtml.setHtml(webParam.getHtml());
						telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
						telecomGuangxiHtml.setPageCount(1);
						telecomGuangxiHtml.setType("getCall");
						telecomGuangxiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomGuangxiHtml);
						telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
						taskMobile.setDescription("数据采集中，【本地通话详单 】爬取成功");
		                //taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
						telecomGuangxiRepositoryCall.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getCall2.SUCCESS",i+ messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());  
						time++;
					}
				}else
				{
//					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "本地通话详单 ",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时1", 1);
//					mobileDataErrRecRepository.save(m);
					taskMobile.setDescription("数据采集中，【本地通话详单 】爬取完成");
	               // taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
					tracer.addTag("parser.telecom.crawler.getCall3.ERROR",i+messageLogin.getTask_id());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			} catch (Exception e) {
				MobileDataErrRec m = null;
				try {
					m = new MobileDataErrRec(taskMobile.getTaskid(), "本地通话详单 ",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时2", 1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mobileDataErrRecRepository.save(m);
				taskMobile.setDescription("数据采集中，【本地通话详单 】爬取完成");
               // taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
				tracer.addTag("parser.telecom.crawler.getCall.ERROR4",i+messageLogin.getTask_id()+e.toString());
				//c.updateTaskMobile(messageLogin.getTask_id());
				e.printStackTrace();
			}
			
             
            //国际
			try {
				WebParam<TelecomGuangxiCall> webParam = telecomGuangxiParser.getNationalCall(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					tracer.addTag("parser.telecom.crawler.getNationalCall", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//					tracer.addTag("parser.telecom.crawler.getNationalCall.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getNationalCall.taskmobile", taskMobile.toString());
					if(null ==webParam.getList())
					{
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "国际通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
//						mobileDataErrRecRepository.save(m);
						taskMobile.setDescription("数据采集中，【国际通讯信息】爬取完成");
		               // taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
						tracer.addTag("parser.telecom.crawler.getNationalCall.ERROR1",i+messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
					}
					else
					{
						TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
						telecomGuangxiHtml.setHtml(webParam.getHtml());
						telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
						telecomGuangxiHtml.setPageCount(1);
						telecomGuangxiHtml.setType("getCall");
						telecomGuangxiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomGuangxiHtml);
						telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
						taskMobile.setDescription("数据采集中，【国际通讯信息】爬取成功");
		              //  taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
						telecomGuangxiRepositoryCall.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getNationalCall.SUCCESS",i+ messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());  
						time++;
					}
				}else
				{
//					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "国际通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时1", 1);
//					mobileDataErrRecRepository.save(m);
					taskMobile.setDescription("数据采集中，【国际通讯信息】爬取完成");
//	                taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
					tracer.addTag("parser.telecom.crawler.getNationalCall.ERROR2",i+messageLogin.getTask_id());
//					c.updateTaskMobile(messageLogin.getTask_id());
				}
			} catch (Exception e) {
				MobileDataErrRec m = null;
				try {
					m = new MobileDataErrRec(taskMobile.getTaskid(), "国际通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时2", 1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mobileDataErrRecRepository.save(m);
				taskMobile.setDescription("数据采集中，【国际通讯信息】爬取完成");
               // taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
				tracer.addTag("parser.telecom.crawler.getNationalCall.ERROR3",i+messageLogin.getTask_id()+e.toString());
				//c.updateTaskMobile(messageLogin.getTask_id());
				e.printStackTrace();
			}
			
			
			//本地打异地
			try {
				WebParam<TelecomGuangxiCall> webParam = telecomGuangxiParser.getLongCall(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					tracer.addTag("parser.telecom.crawler.getLongCall", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//					tracer.addTag("parser.telecom.crawler.getLongCall.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getLongCall.taskmobile", taskMobile.toString());
					if(null ==webParam.getList())
					{
						
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "长途通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
//						mobileDataErrRecRepository.save(m);
						taskMobile.setDescription("数据采集中，【长途信息】爬取完成");
		               // taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
						tracer.addTag("parser.telecom.crawler.getLongCall.ERROR1",i+messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
					}
					else
					{
						TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
						telecomGuangxiHtml.setHtml(webParam.getHtml());
						telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
						telecomGuangxiHtml.setPageCount(1);
						telecomGuangxiHtml.setType("getLongCall");
						telecomGuangxiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomGuangxiHtml);
						telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
						taskMobile.setDescription("数据采集中，【长途信息】爬取成功");
		               // taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
						telecomGuangxiRepositoryCall.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getLongCall.SUCCESS",i+ messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());  
						time++;
					}
				}else
				{
//					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "长途通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时1", 1);
//					mobileDataErrRecRepository.save(m);
					taskMobile.setDescription("数据采集中，【长途信息】爬取完成");
	               // taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
					tracer.addTag("parser.telecom.crawler.getLongCall.ERROR2",i+messageLogin.getTask_id());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			} catch (Exception e) {
				MobileDataErrRec m = null;
				try {
					m = new MobileDataErrRec(taskMobile.getTaskid(), "长途通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时2", 1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mobileDataErrRecRepository.save(m);
				taskMobile.setDescription("数据采集中，【长途信息】爬取完成");
                //taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
				tracer.addTag("parser.telecom.crawler.getLongCall.ERROR3",i+messageLogin.getTask_id()+e.toString());
				//c.updateTaskMobile(messageLogin.getTask_id());
				e.printStackTrace();
			}
			

			//异地通话
			try {
				WebParam<TelecomGuangxiCall> webParam = telecomGuangxiParser.getCityCall(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					tracer.addTag("parser.telecom.crawler.getCityCall", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//					tracer.addTag("parser.telecom.crawler.getCityCall.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getCityCall.taskmobile", taskMobile.toString());
					if(null ==webParam.getList())
					{
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "市话通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
//						mobileDataErrRecRepository.save(m);
						taskMobile.setDescription("数据采集中，【异地通话】爬取完成");
		                //taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
						tracer.addTag("parser.telecom.crawler.getCityCall.ERROR1",i+messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());
					}
					else
					{
						TelecomGuangxiHtml telecomGuangxiHtml = new TelecomGuangxiHtml();
						telecomGuangxiHtml.setHtml(webParam.getHtml());
						telecomGuangxiHtml.setTaskid(messageLogin.getTask_id());
						telecomGuangxiHtml.setPageCount(1);
						telecomGuangxiHtml.setType("getCityCall");
						telecomGuangxiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomGuangxiHtml);
						telecomGuangxiRepositoryHtml.save(telecomGuangxiHtml);
						taskMobile.setDescription("数据采集中，【异地通话】爬取成功");
		                //taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
						telecomGuangxiRepositoryCall.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getCityCall.SUCCESS",i+ messageLogin.getTask_id());
						//c.updateTaskMobile(messageLogin.getTask_id());  
						time++;
					}
				}else
				{
//					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "异地通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时1", 1);
//					mobileDataErrRecRepository.save(m);
					taskMobile.setDescription("数据采集中，【异地通话】爬取完成");
	                //taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
					tracer.addTag("parser.telecom.crawler.getCityCall.ERROR2",i+messageLogin.getTask_id());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			} catch (Exception e) {
				MobileDataErrRec m = null;
				try {
					m = new MobileDataErrRec(taskMobile.getTaskid(), "异地通话记录",getFirstDay("yyyyMM",i), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时2", 1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mobileDataErrRecRepository.save(m);
				taskMobile.setDescription("数据采集中，【异地通话】爬取完成");
                //taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_ERROR.getDescription());
				tracer.addTag("parser.telecom.crawler.getCityCall.ERROR3",i+messageLogin.getTask_id()+e.toString());
				//c.updateTaskMobile(messageLogin.getTask_id());
				e.printStackTrace();
			}
		}
            if(time > 0)
            {
            	taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getAllCall.ERROR1",messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
            }
            else 
            {
            	taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getAllCall.ERROR2",messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
            }
		
	}

	//亲情号
	@Async
	public void getfamily1(MessageLogin messageLogin,TaskMobile taskMobile) {
		taskMobileRepository.updateFamilyMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_FAMILY_MSG_SUCCESS.getDescription());
		tracer.addTag("parser.telecom.crawler.getfamily.ERROR", messageLogin.getTask_id());
		c.updateTaskMobile(messageLogin.getTask_id()); 
	}
}
