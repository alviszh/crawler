package app.service.wap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomHubeiWapService {

	@Autowired
	private TelecomUnitHubeiWapService telecomUnitHubeiWapService;
	
	public void getUserInfo(MessageLogin messageLogin){
		 telecomUnitHubeiWapService.getUserInfo(messageLogin);
	}	
	
	@Async
	public void getRechargeRecord(MessageLogin messageLogin)  {
		telecomUnitHubeiWapService.getRechargeRecord(messageLogin);
	}
	@Async
	public void getVoiceRecord(MessageLogin messageLogin){
		telecomUnitHubeiWapService.getVoiceRecord(messageLogin);
	}
	
	@Async
	public void getPaymonths(MessageLogin messageLogin)  {
		telecomUnitHubeiWapService.getPaymonths(messageLogin);
	}
	@Async
	public void getPointrecords(MessageLogin messageLogin) {
		telecomUnitHubeiWapService.getPointrecords(messageLogin);
		
	}
	
}
