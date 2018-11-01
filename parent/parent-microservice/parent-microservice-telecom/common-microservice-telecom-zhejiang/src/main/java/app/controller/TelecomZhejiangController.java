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
import app.commontracerlog.TracerLog;
import app.contoller.TelecomBasicController;
import app.service.TelecomZhejiangCrawlerService;
import app.service.TelecomZhejiangService;

@RestController
@RequestMapping("/carrier")
public class TelecomZhejiangController extends TelecomBasicController {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomZhejiangService telecomZhejiangService;
	@Autowired
	private TelecomZhejiangCrawlerService telecomZhejiangCrawlerService;
	
	
	/**
	 * @Des 登录后到发送短信直接所需的步骤
	 * @param messageLogin
	 * @return
	 */
	@RequestMapping(value = "/intermediate", method = RequestMethod.POST)
	public TaskMobile intermediate(@RequestBody MessageLogin messageLogin) {
		
		tracer.addTag("cralwer.telecom.zhejiang.intermediate", messageLogin.getTask_id());
		
		//task表改为所需步骤的状态
		TaskMobile taskMobile = telecomZhejiangService.changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_DOING.getPhase(),
				StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_DOING.getPhasestatus(),
				StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_DOING.getDescription(),messageLogin.getTask_id());
		
		try{
			telecomZhejiangService.intermediate(messageLogin,taskMobile);		
		}catch(Exception e){
			tracer.addTag("cralwer.telecom.zhejiang.intermediate.error", e.getMessage());
			taskMobile = telecomZhejiangService.changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getPhase(),
					StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getDescription(),messageLogin.getTask_id());					
		}
		return taskMobile;
		
	}
	
	@RequestMapping(value = "/sendsms", method = RequestMethod.POST)
	public ResultData<TaskMobile> sendSms(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("cralwer.telecom.zhejiang.sendsms", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();	
		TaskMobile taskMobile = telecomZhejiangService.sendSmsTwice(messageLogin);
		data.setData(taskMobile);
		return data;
		
	}
	
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public ResultData<TaskMobile> validate(@RequestBody MessageLogin messageLogin) {	
		tracer.addTag("cralwer.telecom.zhejiang.validate", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();
		TaskMobile taskMobile =	telecomZhejiangService.verifySmsTwice(messageLogin);
		data.setData(taskMobile);		
		return data;
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("cralwer.telecom.zhejiang.crawler", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();
		TaskMobile taskMobile = telecomZhejiangCrawlerService.getAllData(messageLogin);	
		data.setData(taskMobile);	
		return data;
	}

}
