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

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomHubeiService {

	@Autowired
	private TelecomUnitHubeiService telecomUnitHubeiService;
	@Async
	public Boolean getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		return telecomUnitHubeiService.getUserInfo(messageLogin, taskMobile);
	}	
	@Async
	public void getAccountInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		telecomUnitHubeiService.getAccountInfo(messageLogin, taskMobile);
	}
	@Async
	public void getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		    telecomUnitHubeiService.getRechargeRecord(messageLogin, taskMobile);
	}
	@Async
	public void getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
			telecomUnitHubeiService.getVoiceRecord(messageLogin, taskMobile);
	}
	@Async
	public void getSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
			telecomUnitHubeiService.getSmsRecord(messageLogin, taskMobile);
	}
	@Async
	public void getPaymonths(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
			telecomUnitHubeiService.getPaymonths(messageLogin, taskMobile);
	}
	@Async
	public void getPointrecords(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
			telecomUnitHubeiService.getPointrecords(messageLogin, taskMobile);
		
	}
	@Async
	public void getServiceinfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {		
		telecomUnitHubeiService.getServiceInfo(messageLogin, taskMobile);
	}
}
