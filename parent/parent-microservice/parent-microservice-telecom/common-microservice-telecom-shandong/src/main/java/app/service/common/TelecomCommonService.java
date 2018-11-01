package app.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.MessageResult;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.BasicUserRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;


@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shandong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shandong")
public class TelecomCommonService {

	public static final Logger log = LoggerFactory.getLogger(TelecomCommonService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private BasicUserRepository basicUserRepository;
	
	@Autowired
	private TelecomUnitCommomService telecomUnitCommomService;
	
	@Autowired
	private TracerLog tracer;

	private MessageResult messageresult;
	
	// 模拟登陆并获取手机短信验证码
	/*@Async
	public void login(MessageLogin messageLogin, TaskMobile taskMobile) {
		tracer.addTag("parser.telecom.login.taskid",taskMobile.getTaskid());
		tracer.addTag("parser.telecom.login.auth",messageLogin.getName());
		
		
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		MobileLogin mobileLogin = new MobileLogin();
		messageresult = new MessageResult();

		mobileLogin.setName(messageLogin.getName().trim());
		mobileLogin.setPassword(messageLogin.getPassword().trim());
		mobileLogin.setUsrid(messageLogin.getUser_id());

		messageresult.setUser_id(mobileLogin.getUsrid());

		try {
			mobileLogin = telecomUnitCommomService.login(mobileLogin);
			if (mobileLogin.getHtmlpage() == null) {
				messageresult.setUsernum(mobileLogin.getName());
				messageresult.setUser_id(mobileLogin.getUsrid());

				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());

				taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
				// 登录失败状态存储
				save(taskMobile);
			}
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
			String cookieString = CommonUnit.transcookieToJson(mobileLogin.getHtmlpage().getWebClient());
			taskMobile.setCookies(cookieString);
			// 登录成功状态更新
			save(taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());

			// 登录失败状态更新
			save(taskMobile);
		}
	}*/

	
	public void isCrawlerSuccess(TaskMobile taskMobile) throws Exception{
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("isCrawlerSuccess", taskMobile.getTaskid());
		
		if(null != taskMobile.getUserMsgStatus() 
				&& null != taskMobile.getAccountMsgStatus()
				&& null != taskMobile.getPayMsgStatus() 
				&& null != taskMobile.getCallRecordStatus()
				&& null != taskMobile.getSmsRecordStatus() 
				&& null != taskMobile.getIntegralMsgStatus()
				&& null != taskMobile.getBusinessMsgStatus()){
			taskMobile.setDescription("数据采集成功！");
			taskMobile.setPhase("CRAWLER");
			taskMobile.setPhase_status("SUCCESS");
			taskMobile.setFinished(true);
			taskMobileRepository.save(taskMobile);
			
			tracer.addTag("changeCrawlerStatusSuccess success", taskMobile.getTaskid());
		}else{
			tracer.addTag("changeCrawlerStatusSuccess fail",taskMobile.toString());
		}
		
	}
	
	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("正在验证上次爬取是否完成",taskMobile.toString());
		if(null == taskMobile){
			return true;
		}
		if("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())){
			return true;
		}
		return false;
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}
	
	/*public BasicUser findBasicUser(Long id) {
		return basicUserRepository.findById(id);
	}*/
}