package app.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonHtml;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonPointsAndCharges;
import app.bean.WebParamTelecom;
import app.service.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.common")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.common")
public class TelecomCommonLoginService extends TelecomBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(TelecomCommonLoginService.class);


	@Autowired
	private TelecomAyscCommonService telecomAyscCommonService;

	// 模拟登陆并获取手机短信验证码
	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		
		tracerLog.addTag("taskid", taskMobile.getTaskid());
		tracerLog.addTag("parser.login.auth", messageLogin.getName());
		taskMobile = findtaskMobile(messageLogin.getTask_id().trim());

		try {
			WebParamTelecom<?> webParamTelecom = telecomUnitCommomService.login(messageLogin);
			if (webParamTelecom.getPage() == null) {

				taskMobile.setPhase(webParamTelecom.getStatusCodeEnum().getPhase());
				taskMobile.setPhase_status(webParamTelecom.getStatusCodeEnum().getPhasestatus());
				taskMobile.setDescription(webParamTelecom.getErrormessage());

				taskMobile.setError_code(webParamTelecom.getStatusCodeRec().getCode());
				taskMobile.setError_message(webParamTelecom.getErrormessage());
				tracerLog.addTag("parser.login.auth", webParamTelecom.getStatusCodeRec().toString());

				// 登录失败状态存储
				save(taskMobile);
				return taskMobile;
			}
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
			String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());
			taskMobile.setCookies(cookieString);

			// 登录成功状态更新
			save(taskMobile);

			try {
				telecomAyscCommonService.getPointsAndCharges(messageLogin, taskMobile);// 成功
			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", "getPointsAndCharges" + e.toString());
			}
			
			try {
				telecomAyscCommonService.getStarlevel(messageLogin, taskMobile);// 成功
			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", "getStarlevel" + e.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getMessage());
			taskMobile.setTesthtml(messageLogin.getPassword().trim());
			tracerLog.addTag("parser.login.auth", e.getMessage());
			// 登录失败状态更新
			save(taskMobile);
		}
		return taskMobile;
	}


	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracerLog.addTag("正在进行上次未完成的爬取任务。。。", taskMobile.toString());
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}

	protected void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	protected TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	protected void save(TelecomCommonPointsAndCharges result) {
		telecomCommonPointsAndChargesRepository.save(result);
	}
	
	protected void save(TelecomCommonHtml result) {
		telecomCommonHtmlRepository.save(result);
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


}