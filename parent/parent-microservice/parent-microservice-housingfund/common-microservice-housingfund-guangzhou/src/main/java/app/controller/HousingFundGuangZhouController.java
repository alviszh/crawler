package app.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.service.HousingFundGuangZhouService;
import app.service.common.AgentService;

@RestController
@Configuration
@RequestMapping("/housing/guangzhou") 
public class HousingFundGuangZhouController extends HousingBasicController {
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingFundGuangZhouService loginService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private AgentService agentService;
	@Value("${spring.application.name}") 
	String appName;
	
	@PostMapping(path = "/loginAgent")
	public TaskHousing loginAgent(@RequestBody MessageLoginForHousing messageLoginForHousing) throws  Exception { 
		tracer.addTag("action.housingfund.login", messageLoginForHousing.getTask_id());   
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			agentService.postAgent(messageLoginForHousing, "/housing/guangzhou/login",300000L); 
		} catch (Exception e) {
			tracer.addTag("HousingFundGuangZhouController.loginAgent.exception", e.getMessage()); 
		}
		return taskHousing;
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.login.taskid",messageLoginForHousing.getTask_id());
		tracer.addTag("action.login.auth", "登录号码是：====>"+messageLoginForHousing.getNum()+"密码是：====>"+messageLoginForHousing.getPassword());
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		loginService.login(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
	}
	@PostMapping(path = "/crawlerAgent")
	public TaskHousing crawlerAgent(@RequestBody MessageLoginForHousing messageLoginForHousing) throws  Exception {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		messageLoginForHousing.setIp(taskHousing.getCrawlerHost());
		messageLoginForHousing.setPort(taskHousing.getCrawlerPort());
		taskHousing = agentService.postAgentCombo(messageLoginForHousing, "/housing/guangzhou/crawler");
		return taskHousing;
	}
	@PostMapping(path = "/crawler")
	public TaskHousing debitcardCrawlerChina(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		taskHousing = loginService.getAllData(messageLoginForHousing);
		return taskHousing;
	}
}
