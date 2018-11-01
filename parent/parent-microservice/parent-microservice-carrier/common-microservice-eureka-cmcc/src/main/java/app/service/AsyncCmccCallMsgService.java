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
import com.microservice.dao.repository.crawler.cmcc.CmccUserCallResultRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.parser.CmccCrawlerParser;
import app.unit.CmccUnit;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.cmcc","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(
		basePackages={"com.microservice.dao.repository.crawler.cmcc","com.microservice.dao.repository.crawler.mobile"})
public class AsyncCmccCallMsgService {
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private CmccUserCallResultRepository cmccUserCallResultRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private AsyncCmccSmsMsgService asyncCmccSmsMsgService;
	@Autowired
	private CmccCrawlerParser crawlerParser;
	
	/**
	 * @Description: 获取通话记录（半年）
	 * @param messageLogin
	 * @return
	 */
	@Async
	public void getCallRecord(MessageLogin messageLogin) {
		tracer.addTag("EurekaCmccController getCallRecord",messageLogin.getTask_id());
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		Set<Cookie> cookies = CmccUnit.transferJsonToSet(taskMobile);
		
		WebCmccParam webCmccParam = crawlerParser.getMobileRecord(messageLogin,cookies,taskMobile);
		
//		if(null != webCmccParam && 403 == webCmccParam.getCode()){
//			taskMobile.setError_code(403);
//			taskMobile.setCallRecordStatus(201);
//			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskMobileRepository.save(taskMobile);
//			
//		}
		
		if(null != webCmccParam && null != webCmccParam.getResults()){				
			cmccUserCallResultRepository.saveAll(webCmccParam.getResults());
			tracer.addTag("getCallRecord","通话记录入库");
			tracer.addTag("通话记录入库当前时间：", System.currentTimeMillis()+"");
			
			Integer code = webCmccParam.getCode();
			tracer.addTag("webCmccParam", webCmccParam.getCode()+"");
			if(null == code){
				code = 404;
			}
			taskMobile.setCallRecordStatus(code);
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
				
			System.out.println(">>>>>>>>>>>>爬取短信详单    开始<<<<<<<<<<<<<<");
			try{
				tracer.addTag("爬取短信详单", "开始");
				asyncCmccSmsMsgService.getSMSMsg(messageLogin);			
			}catch(Exception e){
				System.out.println("getSMSMsgException"+e.toString());
				tracer.addTag("getSMSMsgException",e.toString());
				taskMobile.setSmsRecordStatus(404);
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
				taskMobileRepository.save(taskMobile);
			}
				
		}else{
			taskMobile.setCallRecordStatus(404);
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			try {
				asyncCmccSmsMsgService.getSMSMsg(messageLogin);
			} catch (Exception e) {
				taskMobile.setSmsRecordStatus(404);
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
				taskMobileRepository.save(taskMobile);
			}
		}
		
		
	}

}
