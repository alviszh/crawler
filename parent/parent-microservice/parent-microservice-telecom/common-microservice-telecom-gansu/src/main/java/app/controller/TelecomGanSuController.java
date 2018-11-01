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
import app.service.TelecomGansuCookieService;
import app.service.aop.ICrawlerLogin;
import app.service.common.TelecomCommonLoginService;

@RestController
@RequestMapping("/carrier") 
public class TelecomGanSuController {
  
	@Autowired
	private TelecomGansuCookieService telecomGansuCookieService;
//	@Autowired
//	private TelecomGansuCookieService telecomGansuCookieService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomCommonLoginService telecomCommonLoginService;
	
	@RequestMapping(value = "/gansu/login", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.login.ready", messageLogin.getTask_id());
		telecomCommonLoginService.login(messageLogin);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin){
		tracer.addTag("parser.crawler.doing", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		telecomGansuCookieService.getAllData(messageLogin);
		result.setData(taskMobile);
		return result;		
	}
	
	
}
