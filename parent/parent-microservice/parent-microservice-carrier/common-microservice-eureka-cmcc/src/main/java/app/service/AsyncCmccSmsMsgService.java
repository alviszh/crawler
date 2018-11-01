package app.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan; 
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.crawler.cmcc.domain.json.WebCmccParam;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.cmcc.CmccSMSMsgResultRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.parser.CmccCrawlerParser;
import app.unit.CmccUnit;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.cmcc","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(
		basePackages={"com.microservice.dao.repository.crawler.cmcc","com.microservice.dao.repository.crawler.mobile"})
public class AsyncCmccSmsMsgService {
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmccSMSMsgResultRepository cmccSMSMsgResultRepository;
	@Autowired
	private AsyncCmccCheckService asyncCmccCheckService;
	@Autowired
	private CmccCrawlerParser crawlerParser;
	
	
	
	/**
	 * @Description: 获取短信信息（半年）
	 * @param messageLogin
	 */
	@Async
	public void getSMSMsg(MessageLogin messageLogin) throws Exception{
		
		System.out.println("AsyncgetSMSMsg:"+messageLogin.getTask_id());
		
		tracer.addTag("AsyncgetSMSMsg",messageLogin.getTask_id());
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		Set<Cookie> cookies = CmccUnit.transferJsonToSet(taskMobile);
		
		WebCmccParam webCmccParam = crawlerParser.getSMSMsg(messageLogin,cookies,taskMobile);		
		if(null == webCmccParam){
			taskMobile.setError_code(201);
			taskMobile.setSmsRecordStatus(201);
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			asyncCmccCheckService.getCheckMsg(messageLogin);
			
		}else if(null != webCmccParam.getCode() && 201 == webCmccParam.getCode()){
			taskMobile.setError_code(201);
			taskMobile.setSmsRecordStatus(201);
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			asyncCmccCheckService.getCheckMsg(messageLogin);

//		}else if(null != webCmccParam.getCode() && 403 == webCmccParam.getCode()){
//			taskMobile.setError_code(403);
//			taskMobile.setSmsRecordStatus(201);
//			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskMobileRepository.save(taskMobile);
//			
//			asyncCmccCheckService.getCheckMsg(messageLogin);
		}else if(null != webCmccParam.getCmccSMSMsgResults()){
			
			System.out.println(">>>>>>>>>>>>getSMSRecord短信信息入库<<<<<<<<<<<<<<");
			
			cmccSMSMsgResultRepository.saveAll(webCmccParam.getCmccSMSMsgResults());
			tracer.addTag("getSMSRecord","短信信息入库");
			tracer.addTag("短信信息入库当前时间：", System.currentTimeMillis()+"");
			taskMobile.setSmsRecordStatus(webCmccParam.getCode());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			asyncCmccCheckService.getCheckMsg(messageLogin);
		}else{
			taskMobile.setSmsRecordStatus(201);
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			asyncCmccCheckService.getCheckMsg(messageLogin);
		}
		
		
	}
	

}
