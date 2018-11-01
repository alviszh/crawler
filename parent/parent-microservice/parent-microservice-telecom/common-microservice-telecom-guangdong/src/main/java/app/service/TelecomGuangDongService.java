package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import app.service.common.LoginAndGetGuangDong;
import app.service.common.TelecomUnitCommonService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guangdong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guangdong")
public class TelecomGuangDongService implements ICrawlerLogin,ISms{

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomGuangDongUnitService telecomGuangDongUnitService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	Map<String, Future<String>> listfuture = new HashMap<>();
	public TaskMobile findtaskMobile(String taskid) {

		return taskMobileRepository.findByTaskid(taskid);
	}
	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
	
	@Autowired
	private TelecomUnitCommonService telecomUnitCommonService;

	private WebParamTelecom messageresult;

	//登录
	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		tracer.addTag("parser.login.taskid", taskMobile.getTaskid());
		tracer.addTag("parser.login.auth", messageLogin.getName());
		messageresult = new WebParamTelecom();

		try{
			messageresult = telecomUnitCommonService.login(messageLogin);
			HtmlPage htmlpage = (HtmlPage) messageresult.getPage();
			System.out.println(htmlpage);
			if (htmlpage == null) {
				taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getDescription());

				taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
				taskMobile.setTesthtml(messageLogin.getPassword().trim());
				// 登录失败状态存储
				save(taskMobile);
			}
			else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
				String cookieString = CommonUnit.transcookieToJson(((HtmlPage) messageresult.getPage()).getWebClient());
				taskMobile.setCookies(cookieString);
				taskMobile.setTesthtml(messageLogin.getPassword().trim());

				System.out.println("登录成功");

				// 登录成功状态更新
				save(taskMobile);
				return taskMobile;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
			taskMobile.setTesthtml(messageLogin.getPassword().trim());
			// 登录失败状态更新
			save(taskMobile);
		}
		return taskMobile;
	}

	//获取手机验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		try {
			tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
			WebParamTelecom webParamTelecom = LoginAndGetGuangDong.getphonecode(messageLogin,taskMobile);
			if (webParamTelecom == null) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
			} else if (webParamTelecom.getHtml().indexOf("发送失败") != -1) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
				// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
				save(taskMobile);
			}  else if (webParamTelecom.getHtml().indexOf("您的号码发送短信超过限制次数，请明天再试！") != -1) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription("您的号码发送短信超过限制次数，请明天再试！");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
				save(taskMobile);
			} else if (webParamTelecom.getHtml().indexOf("获取IP和号码的发送短信次数异常,请稍后再试") != -1) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription("获取IP和号码的发送短信次数异常,请稍后再试！");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
				save(taskMobile);
			}else{
				tracer.addTag("电信 广东获取验证码", webParamTelecom.getHtml());
				String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());

				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
				taskMobile.setCookies(cookieString);
				// 发送验证码状态更新
				save(taskMobile);
			}
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			save(taskMobile);
		}
		return taskMobile;
	}

	//手机短信验证码验证
	@Async
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		tracer.addTag("parser.crawler.taskid",taskMobile.getTaskid());
		WebParamTelecom  webParamTelecom = LoginAndGetGuangDong.setphonecode(messageLogin, taskMobile);

		if (webParamTelecom == null) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 手机验证码验证失败状态更新
			save(taskMobile);
			System.out.println("失败");
			return taskMobile;
		} else if (webParamTelecom.getHtml().indexOf("验证码不正确") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			System.out.println("失败");
			return taskMobile; 
		}  else if (webParamTelecom.getHtml().indexOf("用户未进行实名登记!") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getPhasestatus());
			taskMobile.setDescription("尊敬的用户，根据实名登记的相关法律法规和监管要求，按照国家统一部署，所有未实名登记或实名信息不全的用户应该要求进行补登记。请您移步到就近营业厅进行补登记之后再进行相关业务。感谢您的使用。");
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			System.out.println("失败");
			return taskMobile;
		}else if (webParamTelecom.getHtml().indexOf("未登录") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			System.out.println("失败");
			return taskMobile;
		}else {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
			String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());
			taskMobile.setCookies(cookieString);
			// 手机验证码验证成功状态更新
			save(taskMobile);
			System.out.println("手机验证码验证成功");
		}				
		return taskMobile;
	}

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		/**
		 * 功能
		 */
		//个人信息
		try {
			Future<String> userInfo = telecomGuangDongUnitService.getUserInfo(messageLogin,taskMobile);//成功
			listfuture.put("getUserInfo", userInfo);
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getUserInfo"+e.toString());
		}
		//套餐
		try {
			Future<String> businessmessage = telecomGuangDongUnitService.getBusinessmessage(messageLogin,taskMobile);//成功
			listfuture.put("getBusinessmessage", businessmessage);
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getBusinessmessage"+e.toString());
		}
		//缴费信息
		try {
			for(int i = 0 ; i < 6; i++){
				Thread.sleep(2000);
				try {
					Future<String> payMent = telecomGuangDongUnitService.getPayMent(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getPayMent", payMent);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("action.crawler.auth", "getPayMent"+e.toString());
		}

		//新增积分
		try {
			Future<String> statusCode = telecomGuangDongUnitService.getStatusCode(messageLogin,taskMobile);//成功
			listfuture.put("getStatusCode", statusCode);
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getStatusCode"+e.toString());
		}

		//通话详单
		try {
			for(int i = 0 ; i < 6; i++){
				Thread.sleep(5000);
				try {
					Future<String> callThrem = telecomGuangDongUnitService.getCallThrem(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getCallThrem", callThrem);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getCallThrem"+e.toString());
		}
		//短信详单
		try {
			for(int i = 0 ; i < 6; i++){
				Thread.sleep(5000);
				try {
					Future<String> smsThrem = telecomGuangDongUnitService.getSMSThrem(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getSMSThrem", smsThrem);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getSMSThrem"+e.toString());
		}
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
//						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
//						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			taskMobile = crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			return taskMobile;
		} catch (Exception e) {
			e.printStackTrace();
			taskMobile = crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			tracer.addTag("inTelecomService-listfuture--ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
			return taskMobile;
		}
	}
	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
