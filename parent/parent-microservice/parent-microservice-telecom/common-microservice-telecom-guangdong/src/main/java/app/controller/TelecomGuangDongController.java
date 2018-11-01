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
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.TelecomGuangDongService;
@RestController
@RequestMapping("/carrier/guangdong")
public class TelecomGuangDongController{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomGuangDongService telecomGuangDongService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@RequestMapping(value = "/gdlogin",method = {RequestMethod.POST})
	public  ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin){
		tracer.addTag("parser.login.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.login.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomGuangDongService.login(messageLogin);
		taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData; 
	}

	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST )
	public ResultData<TaskMobile> telecomgetcode(@RequestBody MessageLogin messageLogin){
		tracer.addTag("parser.crawler.taskid 发送手机验证码", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomGuangDongService.sendSms(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData;
	}

	@RequestMapping(value = "/setphonecode",method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomsetcode(@RequestBody MessageLogin messageLogin){
		tracer.addTag("parser.crawler.taskid 验证手机验证码", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomGuangDongService.verifySms(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData;
	}

	@RequestMapping(value = "/crawler",method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin){

		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomGuangDongService.getAllData(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData;
	}


}
