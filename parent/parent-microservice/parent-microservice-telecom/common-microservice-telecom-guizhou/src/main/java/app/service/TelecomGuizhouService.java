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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guizhou")
public class TelecomGuizhouService {

	@Autowired
	private TelecomUnitGuizhouService telecomUnitGuizhouService;
	@Async
	public void getAccountInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		telecomUnitGuizhouService.getAccountInfo(messageLogin, taskMobile);
	}
	@Async
	public void getPoint(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		telecomUnitGuizhouService.getPoint(messageLogin, taskMobile);
	}
	@Async
	public void getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		    telecomUnitGuizhouService.getRechargeRecord(messageLogin, taskMobile);
	}
	@Async
	public void getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
			telecomUnitGuizhouService.getVoiceRecord(messageLogin, taskMobile);		
	}
	@Async
	public void getSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
			telecomUnitGuizhouService.getSmsRecord(messageLogin, taskMobile);
	}
	@Async
	public void getPaymonths(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
			telecomUnitGuizhouService.getPaymonths(messageLogin, taskMobile);		
	}
}
