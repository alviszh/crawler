package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.TelecomCommonService;
import app.service.TelecomService;


@RestController
@RequestMapping("/carrier") 
public class TelecomHuhehaoteController {
	
	@Autowired
	private TelecomCommonService telecomCommonService;
	
	@Autowired
	private TelecomService telecomService;
	
	@Autowired 
	private TracerLog tracer;
	
	
	@RequestMapping(value = "/getphonecode", method = { RequestMethod.POST })
	public ResultData<TaskMobile> getYzm(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id().trim());
		//调用发送验证码业务方法
		telecomCommonService.sendSms(messageLogin);
		
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	@RequestMapping(value = "/setphonecode", method = { RequestMethod.POST })
	public ResultData<TaskMobile> setYzm(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id().trim());
		//调用验证验证码业务方法
		telecomCommonService.verifySms(messageLogin);
		
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	@RequestMapping(value = "/crawler", method = { RequestMethod.POST })
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id().trim());
        //准备开始
		taskMobile.setFamilyMsgStatus(200);
		taskMobile.setBusinessMsgStatus(200);
		taskMobile.setIntegralMsgStatus(200);
		telecomCommonService.save(taskMobile);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		
		//所有爬取业务的入口
		telecomService.getAllData(messageLogin);

		return result;
	}
	
	
}
