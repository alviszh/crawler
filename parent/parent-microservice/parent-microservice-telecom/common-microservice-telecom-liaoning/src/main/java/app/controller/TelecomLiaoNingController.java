package app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.common.TelecomCommonLoginService;
import app.service.common.TelecomLiaoNingCommon;
@RestController
@Configuration
@RequestMapping("/carrier/liaoning")
public class TelecomLiaoNingController {

	@Autowired
	private TelecomCommonLoginService telecomCommonService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomLiaoNingCommon telecomLiaoNingCommon;
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin){
		tracer.addTag("parser.login.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.login.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService.login(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData;
	}

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> getAllData(@RequestBody MessageLogin messageLogin){
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
//		TaskMobile taskMobile = iCrawler.getAllData(messageLogin);
		TaskMobile taskMobile = telecomLiaoNingCommon.getAllData(messageLogin);
		ResultData<TaskMobile> resultData = new ResultData<TaskMobile>();
		resultData.setData(taskMobile);
		return resultData;
	}

}
