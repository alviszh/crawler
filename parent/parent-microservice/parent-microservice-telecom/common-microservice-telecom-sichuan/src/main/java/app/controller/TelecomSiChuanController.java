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
import app.service.TelecomSiChuanService;
import app.service.common.TelecomCommonLoginService;
/**
 * 
 * @author lzh
 * 2017年9月6日
 */

@RestController
@RequestMapping("/carrier/sichuan")
public class TelecomSiChuanController {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomSiChuanService telecomSiChuanService;
	@Autowired
	private TelecomCommonLoginService telecomCommonService;
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin){
		tracer.addTag("parser.login.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.login.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService.login(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData; 
	}

	@RequestMapping(value = "/crawler",method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin){
		tracer.qryKeyValue("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomSiChuanService.getAllData(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData;
	}


	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomgetcode(@RequestBody MessageLogin messageLogin){
		tracer.qryKeyValue("parser.login.sendSMS", messageLogin.getTask_id());
		tracer.addTag("parser.login.sendSMS.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomSiChuanService.sendSms(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData; 
	}

	@RequestMapping(value = "/setphonecode",method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomsetcode(@RequestBody MessageLogin messageLogin){
		tracer.qryKeyValue("parser.login.attestation", messageLogin.getTask_id());
		tracer.addTag("parser.login.attestation.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomSiChuanService.verifySms(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData; 
	}
}
