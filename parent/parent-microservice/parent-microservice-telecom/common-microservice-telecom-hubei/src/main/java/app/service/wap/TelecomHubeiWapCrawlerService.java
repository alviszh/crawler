package app.service.wap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.htmlunit.wap.TelecomHubeiWapHtmlUnit;
import app.service.aop.ICrawler;
import app.service.aop.ISms;
import app.service.aop.ISmsTwice;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomHubeiWapCrawlerService implements ISms,ISmsTwice,ICrawler{

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomSmsHubeiWapService telecomSmsHubeiWapService;
	@Autowired
	private TelecomGetAllDataHubeiWapService telecomGetAllDataHubeiWapService;
	@Autowired 
	private TracerLog tracer;
	
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
	   tracer.addTag("sendSms", messageLogin.getTask_id());
	   TaskMobile taskMobile=telecomSmsHubeiWapService.getPhoneCode(messageLogin);
	   return taskMobile;
	}

	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		tracer.addTag("verifySms", messageLogin.getTask_id());
		TaskMobile taskMobile = telecomSmsHubeiWapService.setPhoneCode(messageLogin);
		return taskMobile;
	}
	
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		tracer.addTag("@Async getAllData", messageLogin.toString());
		tracer.addTag("taskid",messageLogin.getTask_id());	
		try{		
			tracer.addTag("TelecomGuizhouAspectService getAllData",messageLogin.getTask_id());			
			telecomGetAllDataHubeiWapService.getAllData(messageLogin);
		}catch(Exception e){
			tracer.addTag("TelecomGuizhouAspectService getAllData",e.getMessage());
			updateStatus(messageLogin);
		}		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());	
		tracer.addTag("TelecomGuizhouAspectService getAllData",taskMobile.toString());
		return taskMobile;
	
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateStatus(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
		taskMobileRepository.save(taskMobile);
	}

	@Override
	public TaskMobile sendSmsTwice(MessageLogin messageLogin) {
		tracer.addTag("sendSmsTwice", messageLogin.getTask_id());
		TaskMobile taskMobile = telecomSmsHubeiWapService.getPhoneCodeTwo(messageLogin);
		return taskMobile;
	}

	@Override
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		tracer.addTag("verifySmsTwice", messageLogin.getTask_id());
		TaskMobile taskMobile = telecomSmsHubeiWapService.setPhoneCodeTwo(messageLogin);
		return taskMobile;
	} 
	
}
