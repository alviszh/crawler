package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.qinghai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.qinghai")
public class TelecomAsyncQingHaiService implements ISms,ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(TelecomAsyncQingHaiService.class);

	@Autowired
	private TelecomQingHaiService telecomQingHaiService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			
		try {
			telecomQingHaiService.getUserInfo(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("parser.crawler.auth", "getUserInfo" + e.toString());
		}

		try {
			telecomQingHaiService.getintegraResult(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getintegraResult" + e.toString());
		}

		try {
			telecomQingHaiService.getpayResult(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getpayResult" + e.toString());
		}
		
		try {
			telecomQingHaiService.getSMSBill(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getSMSThrem" + e.toString());			
		}
		
		try {
			telecomQingHaiService.getphoneBill(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getCallThrem" + e.toString());
		}
		
		try {
			telecomQingHaiService.getBill(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("parser.crawler.auth", "getBill" + e.toString());
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());

		
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	
}