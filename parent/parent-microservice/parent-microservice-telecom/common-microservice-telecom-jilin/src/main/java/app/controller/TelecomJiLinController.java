package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.TelecomJiLinService;
import app.service.common.TelecomCommonService;

@RestController
@RequestMapping("/carrier") 
public class TelecomJiLinController {

	@Autowired
	private TelecomJiLinService telecomJiLinService;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TracerLog tracer;
	
	/*@RequestMapping(value = "/jilin", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) throws Exception{
		tracer.addTag("parser.telecom.login", "start");
		tracer.addTag("parser.telecom.login.taskid", messageLogin.getTask_id());
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getError_code());
		
		telecomCommonService.login(messageLogin, taskMobile);
		
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		
		return result;
	}*/
	
	
	@RequestMapping(value = "/jilin/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin) throws Exception{
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());

		tracer.qryKeyValue("taskid",messageLogin.getTask_id());
		telecomJiLinService.getAllData(messageLogin, taskMobile);
		
		return null;
	}
}
