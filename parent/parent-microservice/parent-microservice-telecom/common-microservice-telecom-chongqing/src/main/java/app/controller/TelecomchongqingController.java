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
import app.service.AsyncchongqingGetAllDataService;
import app.service.TelecomchongqingService;
import app.service.common.TelecomCommonLoginService;

@RestController
@RequestMapping("/carrier") 
public class TelecomchongqingController {

	@Autowired
	private TelecomchongqingService telecomService;
	
	@Autowired
	private AsyncchongqingGetAllDataService asyncchongqingGetAllDataService;
	
	@Autowired
	private TelecomCommonLoginService telecomCommonService;
		
	@Autowired
	private TracerLog tracer;

	@RequestMapping(value = "/chongqing/login", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {

		tracer.addTag("Telecomchongqing1Controller.login", messageLogin.getTask_id());

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
//		
//		tracer.addTag("parser.verificationName.taskid",messageLogin.getTask_id());
//		TaskMobile taskMobile = telecomService.findtaskMobile(messageLogin.getTask_id());
//
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		if(null != taskMobile){
//			
//			telecomService.verification(messageLogin, taskMobile);
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
		asyncchongqingGetAllDataService.getAllData(messageLogin);
		return null;
		
	}
	
	
	
	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> sendSms(@RequestBody   MessageLogin messageLogin) {
		tracer.qryKeyValue("parser.sendSms.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		
		TaskMobile taskMobile = telecomService.sendSms(messageLogin);
		
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	
	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> verifySms(@RequestBody   MessageLogin messageLogin) {
		tracer.qryKeyValue("parser.verifySms.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		
		TaskMobile taskMobile = telecomService.verifySms(messageLogin);
		
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	
	

}
