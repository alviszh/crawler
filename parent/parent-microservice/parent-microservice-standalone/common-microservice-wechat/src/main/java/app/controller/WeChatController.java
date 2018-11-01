package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.wechat.json.WeChatRequestParameters;
import com.microservice.dao.entity.crawler.wechat.TaskWeChat;
import com.microservice.dao.repository.crawler.wechat.TaskWeChatRepository;

import app.commontracerlog.TracerLog;
import app.service.WeChatCommonService;
  

@RestController
@Configuration      
@RequestMapping("/wechat")
public class WeChatController {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskWeChatRepository taskWeChatRepository;
	
	@Autowired
	private WeChatCommonService weChatCommonService;
	@Value("${spring.application.name}") 
	String appName;
	
//	@PostMapping(path = "/loginAgent")
//	public TaskWeChat loginAgent(@RequestBody WeChatRequestParameters weChatRequestParameters){
//		TaskWeChat taskWeChat=null;
//		try {
//			taskWeChat =  agentService.postAgent(weChatRequestParameters, "/wechat/login",300000L); 
//		} catch (Exception e) {
//			tracer.qryKeyValue("RuntimeException", e.toString());
//		}
//		return taskWeChat;
//	}

	 
	@PostMapping(value="/getCode")
	public TaskWeChat getCode(@RequestBody WeChatRequestParameters weChatRequestParameters){
		tracer.addTag("WeChatController getCode", weChatRequestParameters.getTaskid());
		TaskWeChat taskWeChat = taskWeChatRepository.findByTaskid(weChatRequestParameters.getTaskid());
		try {
			taskWeChat = weChatCommonService.getCode(weChatRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskWeChat;
	}
	
	@PostMapping(value="/login")
	public TaskWeChat login(@RequestBody WeChatRequestParameters weChatRequestParameters){
		tracer.addTag("WeChatController login", weChatRequestParameters.getTaskid());
		TaskWeChat taskWeChat = taskWeChatRepository.findByTaskid(weChatRequestParameters.getTaskid());
		try {
			taskWeChat = weChatCommonService.login(weChatRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskWeChat;
	}
	
//	@PostMapping(path = "/crawlerAgent")
//	public TaskWeChat crawlerAgent(@RequestBody WeChatRequestParameters weChatRequestParameters){
//		TaskWeChat taskWeChat=null;
//		try {
//			taskWeChat =  agentService.postAgent(weChatRequestParameters, "/wechat/crawler",300000L); 
//		} catch (Exception e) {
//			tracer.qryKeyValue("RuntimeException", e.toString());
//		}
//		return taskWeChat;
//	}
	
	@PostMapping(value="/crawler")
	public TaskWeChat crawler(@RequestBody WeChatRequestParameters weChatRequestParameters){
		tracer.addTag("TaskWeChatController crawler", weChatRequestParameters.getTaskid());
		TaskWeChat taskWeChat = taskWeChatRepository.findByTaskid(weChatRequestParameters.getTaskid());
		taskWeChat = weChatCommonService.getAllData(weChatRequestParameters);
		return taskWeChat;
	}
}
