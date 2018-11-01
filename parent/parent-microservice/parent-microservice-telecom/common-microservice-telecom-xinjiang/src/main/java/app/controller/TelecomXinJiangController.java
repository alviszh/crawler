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
import app.service.TelecomXinJiangCrawlerService;

@RestController
@RequestMapping("/carrier") 
public class TelecomXinJiangController {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomXinJiangCrawlerService telecomXinJiangCrawlerService;
	@Autowired
	private TracerLog tracer;
//	@RequestMapping(value = "/login", method = { RequestMethod.POST })
//	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) throws Exception{
//		tracer.addTag("parser.login", "start");
//		tracer.addTag("parser.login.taskid", messageLogin.getTask_id());
//		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id().trim());
//		
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getError_code());
//		
//		telecomCommonService.login(messageLogin, taskMobile);		
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		result.setData(taskMobile);		
//		return result;
//	}
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) throws Exception {
		tracer.addTag("parser.crawler", "start");
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setFamilyMsgStatus(201);
		taskMobile.setIntegralMsgStatus(201);
		taskMobileRepository.save(taskMobile);		
		taskMobile=telecomXinJiangCrawlerService.getAllData(messageLogin);		
		taskMobile=taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		result.setData(taskMobile);		 
		return result;
	}
//	@RequestMapping(value = "/telecomgetcode", method = RequestMethod.POST)
//	public ResultData<TaskMobile> telecomgetcode(@RequestBody   MessageLogin messageLogin) {
//		tracer.addTag("parser.crawler.taskid 发送手机验证码",messageLogin.getTask_id());
//		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
//		tracer.addTag("parser.crawler.auth",messageLogin.getName());
//		TaskMobile taskMobile=telecomXinJiangCrawlerService.sendSms(messageLogin);
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		result.setData(taskMobile);
//		return result;
//	}
//	
//	@RequestMapping(value = "/telecomsetcode", method = RequestMethod.POST)
//	public ResultData<TaskMobile> telecomsetcode(@RequestBody  MessageLogin messageLogin) throws Exception {
//		tracer.addTag("parser.crawler.taskid 验证手机验证码",messageLogin.getTask_id());
//		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
//		tracer.addTag("parser.crawler.auth",messageLogin.getName());
//		TaskMobile taskMobile= telecomXinJiangCrawlerService.verifySms(messageLogin);
//    	ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		result.setData(taskMobile);
//		return result;
//	}
}
