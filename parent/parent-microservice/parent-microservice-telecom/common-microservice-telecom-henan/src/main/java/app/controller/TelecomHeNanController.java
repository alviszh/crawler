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
import app.service.TelecomHenanService;
import app.service.common.TelecomCommonService;

@RestController
@RequestMapping("/carrier") 
public class TelecomHeNanController {

	@Autowired
	private TelecomHenanService telecomHenanService;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TracerLog tracer;
	
	/*@RequestMapping(value = "/henan", method = { RequestMethod.POST })
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
	
	//发送短信验证码接口
	@RequestMapping(value = "/henan/sendSms", method = RequestMethod.POST)
	public ResultData<TaskMobile> getSmscode(@RequestBody  MessageLogin messageLogin) throws Exception{
		tracer.addTag("parser.telecom.crawler.taskid 发送短信验证码", messageLogin.getTask_id());

		tracer.qryKeyValue("parser.telecom.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.telecom.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
	
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getError_code());
		telecomCommonService.save(taskMobile);
		tracer.addTag("parser.telecom.crawler.sendcode","begin");
		telecomHenanService.sendSms(messageLogin);
		taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		result.setData(taskMobile);
		return result;
	}
	
	//验证短信验证码接口
	@RequestMapping(value = "/henan/setSmscode", method = RequestMethod.POST)
	public ResultData<TaskMobile> setSmscode(@RequestBody  MessageLogin messageLogin) throws Exception{
		tracer.qryKeyValue("parser.telecom.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.telecom.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		telecomCommonService.save(taskMobile);
		tracer.addTag("parser.telecom.crawler.sendcode","begin");
		telecomHenanService.verifySms(messageLogin);
		taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		result.setData(taskMobile);
		return result;
	}
	
	@RequestMapping(value = "/henan/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin) throws Exception{
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());

		tracer.qryKeyValue("parser.telecom.crawler.taskid", messageLogin.getTask_id());
		taskMobile = telecomHenanService.getAllData(messageLogin);
		
		return null;
	}
}
