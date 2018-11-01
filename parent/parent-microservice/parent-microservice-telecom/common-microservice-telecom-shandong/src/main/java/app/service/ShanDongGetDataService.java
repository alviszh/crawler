package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.service.aop.ICrawler;
import app.service.common.TelecomCommonService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shandong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shandong")
public class ShanDongGetDataService implements ICrawler{

	@Autowired
	private AsyncGetDataService asyncGetDataService;
	@Autowired
	private TelecomCommonService telecomCommonService;
	
	@Override
	@Async
	public TaskMobile getAllData(MessageLogin messageLogin){
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		if(null != taskMobile){
			asyncGetDataService.getUserInfo(taskMobile);				//获取个人信息
			
			asyncGetDataService.getPaymentHistory(taskMobile);			//获取缴费信息
			
			asyncGetDataService.getBillData(taskMobile);				//获取账单信息
			
			asyncGetDataService.getIntegral(taskMobile);				//获取积分信息
			
			asyncGetDataService.getCallDetails(taskMobile);				//获取通话详单
			
			asyncGetDataService.getIncrement(taskMobile);				//获取增值详单
			
			asyncGetDataService.getSMSDetails(taskMobile);				//获取短信详单
			
		}
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
