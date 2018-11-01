package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomJilinParser;
import app.service.common.TelecomCommonService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.jilin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.jilin")
public class TelecomJiLinService {

	@Autowired
	private JiLinGetDataService jiLinGetDataService;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TelecomJilinParser telecomJilinParser;
	@Autowired
	private TracerLog tracer;
	
	@Async
	public TaskMobile getAllData(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		tracer.addTag("parser.telecom.crawler.getAllData", taskMobile.getTaskid());
		HtmlPage checked1 = telecomJilinParser.getCheckOneHtml(messageLogin,taskMobile);
		if(null != checked1){
			tracer.addTag("parser.telecom.crawler.getAllData.check1", "checked");
			WebParam webParam = telecomJilinParser.getCheckTwoHtml(messageLogin, checked1);
			if(null != webParam.getPage()){
				tracer.addTag("parser.telecom.crawler.getAllData.check2", "checked");
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
				taskMobile.setFamilyMsgStatus(201);
				taskMobile.setIntegralMsgStatus(201);
				taskMobile.setCookies(cookies);
				telecomCommonService.save(taskMobile);
				tracer.addTag("parser.telecom.crawler.getAllData.taskMobile", taskMobile.toString());
				
				taskMobile = jiLinGetDataService.getAllData(messageLogin);
			}else{
				taskMobile.setPhase("VALIDATE");
				taskMobile.setPhase_status("FAIL");
				taskMobile.setDescription(webParam.getHtml());
				telecomCommonService.save(taskMobile);
				tracer.addTag("parser.telecom.crawler.getAllData.check2", "fail"+taskMobile.toString());
			}
		}else{
			taskMobile.setPhase("VALIDATE");
			taskMobile.setPhase_status("FAIL");
			taskMobile.setDescription("身份认证请求超时，请稍后重试。");
			taskMobile.setError_code(2);
			telecomCommonService.save(taskMobile);
			tracer.addTag("parser.telecom.crawler.getAllData.check1", "fail"+taskMobile.toString());
		}
		return taskMobile;
		
	}
	
}
