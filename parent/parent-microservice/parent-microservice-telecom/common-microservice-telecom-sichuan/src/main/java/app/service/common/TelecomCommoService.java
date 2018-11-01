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
import com.crawler.mobile.json.MessageResult;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;


@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.sichuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.sichuan")
public class TelecomCommoService {
	

	public static final Logger log = LoggerFactory.getLogger(TelecomCommoService.class);

	@Autowired
	private TelecomUnitCommonService telecomUnitCommonService;
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
 	
	// 模拟登陆并获取手机短信验证码
		@Async
		public void login(MessageLogin messageLogin, TaskMobile taskMobile) {
			tracer.addTag("parser.login.taskid",taskMobile.getTaskid());
			tracer.addTag("parser.login.auth",messageLogin.getName());
			
			
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
			taskMobile.setError_message(null);
			save(taskMobile);

			try {
				 WebParamTelecom<MessageLogin> login = telecomUnitCommonService.login(messageLogin);
				if (login.getPage() == null) {

					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());

					taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
					// 登录失败状态存储
					save(taskMobile);
				}else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
				String cookieString = CommonUnit.transcookieToJson(login.getPage().getWebClient());
				taskMobile.setCookies(cookieString);
				// 登录成功状态更新
				save(taskMobile);}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());

				// 登录失败状态更新
				save(taskMobile);
			}
		}

	
	
	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}


	public TaskMobile findtaskMobile(String task_id) {
		return taskMobileRepository.findByTaskid(task_id);
	}



	
}
