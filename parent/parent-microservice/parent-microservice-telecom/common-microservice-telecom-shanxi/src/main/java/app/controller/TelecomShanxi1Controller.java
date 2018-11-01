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
import app.service.AsyncShanxi1GetAllDataService;
import app.service.TelecomShanxi1Service;
import app.service.common.TelecomCommonLoginService;

@RestController
@RequestMapping("/carrier") 
public class TelecomShanxi1Controller {

	@Autowired
	private TelecomShanxi1Service telecomService;
	
	@Autowired
	private AsyncShanxi1GetAllDataService asyncShanxi1GetAllDataService;
	
	@Autowired
	private TelecomCommonLoginService telecomCommonService;
		
	@Autowired
	private TracerLog tracer;

	@RequestMapping(value = "/shanxi1/login", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {

		tracer.addTag("TelecomShanxi1Controller.login", messageLogin.getTask_id());

		tracer.addTag("parser.login.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.login.auth", messageLogin.getName());

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();

		TaskMobile taskMobile = telecomCommonService.login(messageLogin);
		result.setData(taskMobile);
		return result;

	}
	
	/**
	 * 实名认证
	 * @param messageLogin
	 * @return
	 */
//	@RequestMapping(value = "/verification", method = { RequestMethod.POST })
//	public ResultData<TaskMobile> verificationName(@RequestBody MessageLogin messageLogin) {
//		tracer.qryKeyValue("parser.crawler.verificationName",messageLogin.getTask_id());
//		
//		tracer.addTag("TelecomShanxi1Controller.verificationName","taskId:"+messageLogin.getTask_id()+"---name:"+messageLogin.getName());
//		
//		TaskMobile taskMobile = telecomService.findtaskMobile(messageLogin.getTask_id());
//
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		if(null != taskMobile){
//			
//			telecomService.verificationName(taskMobile);
//			
//			result.setData(taskMobile);
//			return result;
//		}
//		
//		return null;
//		
//	}
	
	/**
	 * 爬取数据接口
	 * @param messageLogin
	 * @return
	 */
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin) {
		tracer.qryKeyValue("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		
		
		asyncShanxi1GetAllDataService.getAllData(messageLogin);
		
		return null;
		
	}
	
	
	
	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomgetcode(@RequestBody   MessageLogin messageLogin) {
		tracer.qryKeyValue("parser.crawler.taskid",messageLogin.getTask_id());

		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		
		TaskMobile taskMobile = telecomService.sendSms(messageLogin);
		
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	
	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> verificationcode(@RequestBody   MessageLogin messageLogin) {
		tracer.qryKeyValue("parser.crawler.taskid",messageLogin.getTask_id());

		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		
		TaskMobile taskMobile = telecomService.verifySms(messageLogin);
		
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	
	

}
