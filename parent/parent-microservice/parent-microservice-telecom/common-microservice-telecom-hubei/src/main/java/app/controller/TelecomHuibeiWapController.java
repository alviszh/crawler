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
import app.service.wap.TelecomHubeiWapCrawlerService;

@RestController
@RequestMapping("/carrier") 
public class TelecomHuibeiWapController {
	
	@Autowired
	private TelecomHubeiWapCrawlerService  telecomHubeiWapCrawlerService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@RequestMapping(value = "/getAllData", method = RequestMethod.POST)
	public ResultData<TaskMobile> getAllData(@RequestBody MessageLogin messageLogin) throws Exception {
		tracer.addTag("parser.crawler", "start");
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile=taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		result.setData(taskMobile);		 
		return result;
	}
	@RequestMapping(value = "/sendCode", method = RequestMethod.POST)
	public ResultData<TaskMobile> sendCode(@RequestBody   MessageLogin messageLogin) throws Exception{
		tracer.addTag("parser.crawler.sendCode.taskid 发送手机验证码",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.sendCode.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.sendCode.auth",messageLogin.getName());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());	
		taskMobile=telecomHubeiWapCrawlerService.sendSms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	@RequestMapping(value = "/verifiCode", method = RequestMethod.POST)
	public ResultData<TaskMobile> verifiCode(@RequestBody  MessageLogin messageLogin) throws Exception {
		tracer.addTag("parser.crawler.verifiCode.taskid 验证手机验证码",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.verifiCode.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.verifiCode.auth",messageLogin.getName());
		TaskMobile taskMobile=telecomHubeiWapCrawlerService.verifySms(messageLogin);
     	ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	@RequestMapping(value = "/sendCodeTwo", method = RequestMethod.POST)
	public ResultData<TaskMobile> sendCodeTwo(@RequestBody   MessageLogin messageLogin) throws Exception{
		tracer.addTag("parser.crawler.sendCodeTwo.taskid 发送手机验证码",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.sendCodeTwo.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.sendCodeTwo.auth",messageLogin.getName());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());	
		taskMobile=telecomHubeiWapCrawlerService.sendSmsTwice(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	@RequestMapping(value = "/verifiCodeTwo", method = RequestMethod.POST)
	public ResultData<TaskMobile> verifiCodeTwo(@RequestBody  MessageLogin messageLogin) throws Exception {
		tracer.addTag("parser.crawler.verifiCodeTwo.taskid 验证手机验证码",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.verifiCodeTwo.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.verifiCodeTwo.auth",messageLogin.getName());
		TaskMobile taskMobile=telecomHubeiWapCrawlerService.verifySmsTwice(messageLogin);
     	ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
}
