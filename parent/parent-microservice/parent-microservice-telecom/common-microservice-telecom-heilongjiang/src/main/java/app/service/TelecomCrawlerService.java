package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;
import app.crawler.telecom.htmlunit.LoginAndGet;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.heilongjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.heilongjiang")
public class TelecomCrawlerService implements ISms,ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(TelecomCrawlerService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TracerLog tracerLog;
	
	// 获取手机验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		tracerLog.addTag("parser.crawler.taskid", taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		// 发送验证码状态更新
		save(taskMobile);
		WebParamTelecom<?> webParamTelecom = LoginAndGet.getphonecode(taskMobile);
		if (webParamTelecom.getErrormessage() != null) {
			tracerLog.addTag("电信获取验证码  错误", "=="+webParamTelecom.getErrormessage());
			tracerLog.addTag("电信获取验证码  错误","=="+ webParamTelecom.getHtml());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(webParamTelecom.getErrormessage());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(webParamTelecom.getErrormessage());
			// 发送验证码状态更新
			save(taskMobile);
			return null;
		}

		String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
		taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
		taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
		taskMobile.setCookies(cookieString);
		// 发送验证码状态更新
		save(taskMobile);
		tracerLog.addTag("手机验证码获取完成", webParamTelecom.getHtml());

		log.info("======================手机验证码获取完成====================");

		return taskMobile;

	}

	// 手机短信验证码验证
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		// 手机验证码验证状态更新
		save(taskMobile);
		WebParamTelecom<?> webParamTelecom = LoginAndGet.setphonecode(messageLogin, taskMobile);
		if (webParamTelecom.getErrormessage() != null) {
			tracerLog.addTag("电信验证验证码  错误", webParamTelecom.getErrormessage());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 手机验证码验证失败状态更新
			save(taskMobile);
			return taskMobile;
		}
		if (webParamTelecom.getHtml().indexOf("对不起，系统忙，请稍后再试") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			tracerLog.addTag("电信验证验证码  错误", webParamTelecom.getHtml());
			return taskMobile;
		}
		if (webParamTelecom.getHtml().indexOf("验证码错误") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			tracerLog.addTag("电信获取验证码  错误", webParamTelecom.getHtml());
			return taskMobile;
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
		String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());
		taskMobile.setCookies(cookieString);
		// 手机验证码验证成功状态更新
		save(taskMobile);
			
		return taskMobile;

	}	
	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}


}