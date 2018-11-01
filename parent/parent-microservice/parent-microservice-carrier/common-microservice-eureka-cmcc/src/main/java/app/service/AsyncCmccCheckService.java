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
import com.microservice.dao.repository.crawler.cmcc.CmccCheckMsgResultRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.parser.CmccCrawlerParser;
import app.unit.CmccUnit;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.cmcc","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(
		basePackages={"com.microservice.dao.repository.crawler.cmcc","com.microservice.dao.repository.crawler.mobile"})
public class AsyncCmccCheckService {
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmccCheckMsgResultRepository cmccCheckMsgResultRepository;
	@Autowired
	private AsyncCmccPayService asyncCmccPayService;
	@Autowired
	private CmccCrawlerParser crawlerParser;
	
	/**
	 * @Description: 获取月账单（半年）
	 * @param messageLogin
	 */
	@Async
	public void getCheckMsg(MessageLogin messageLogin) {
		tracer.addTag("AsyncCmccCheckService getCheckMsg",messageLogin.getTask_id());	
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		Set<Cookie> cookies = CmccUnit.transferJsonToSet(taskMobile);
		
		WebCmccParam webCmccParam = crawlerParser.getCheckMsg(messageLogin,cookies);
		
		if(null != webCmccParam && 403 == webCmccParam.getCode()){
			taskMobile.setError_code(403);
			taskMobile.setAccountMsgStatus(201);
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
			taskMobileRepository.save(taskMobile);
			
			asyncCmccPayService.getPayMsg(messageLogin);
		}
		
		if(null != webCmccParam.getCmccCheckMsgResults()){
			cmccCheckMsgResultRepository.saveAll(webCmccParam.getCmccCheckMsgResults());
			tracer.addTag("getCheckMsg","月账单入库");
			
			taskMobile.setAccountMsgStatus(webCmccParam.getCode());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			asyncCmccPayService.getPayMsg(messageLogin);
		}else{
			taskMobile.setAccountMsgStatus(webCmccParam.getCode());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);
			
			asyncCmccPayService.getPayMsg(messageLogin);
		}
		
	}


}
