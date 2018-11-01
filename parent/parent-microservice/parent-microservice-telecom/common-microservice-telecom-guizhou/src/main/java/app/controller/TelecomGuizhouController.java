package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.TelecomGuizhouCrawlerService;

@RestController
@RequestMapping("/carrier") 
public class TelecomGuizhouController {

	@Autowired
	private TelecomGuizhouCrawlerService  telecomGuizhouCrawlerService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) throws Exception {
		tracer.addTag("parser.crawler", "start");
		tracer.addTag("telecomGuizhouController.crawler", messageLogin.getTask_id());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile=taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());		
		taskMobile.setUserMsgStatus(201);
		taskMobile.setFamilyMsgStatus(201);
		taskMobile.setIntegralMsgStatus(201);
		taskMobileRepository.save(taskMobile);
		telecomGuizhouCrawlerService.getAllData(messageLogin);					
		result.setData(taskMobile);		 
		return result;	 
	}
	@RequestMapping(value = "/telecomgetcode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomgetcode(@RequestBody   MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid 发送手机验证码",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		TaskMobile taskMobile = telecomGuizhouCrawlerService.sendSms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	@RequestMapping(value = "/telecomsetcode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomsetcode(@RequestBody  MessageLogin messageLogin) throws Exception {
		tracer.addTag("parser.crawler.taskid 验证手机验证码",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());	
		TaskMobile taskMobile =telecomGuizhouCrawlerService.verifySms(messageLogin);
     	ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

}
