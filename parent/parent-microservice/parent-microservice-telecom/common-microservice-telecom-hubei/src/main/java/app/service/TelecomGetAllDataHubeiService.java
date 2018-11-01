package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomGetAllDataHubeiService {
	
	@Autowired
	private TaskMobileRepository  taskMobileRepository;
	@Autowired
	private TelecomHubeiService  telecomHubeiService;
    public void getAllData(MessageLogin messageLogin) throws Exception{
	   TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		if (null != taskMobile) {
			telecomHubeiService.getUserInfo(messageLogin, taskMobile);
			telecomHubeiService.getAccountInfo(messageLogin, taskMobile);//获取账户信息
			telecomHubeiService.getPaymonths(messageLogin, taskMobile);//月账单信息
			telecomHubeiService.getVoiceRecord(messageLogin, taskMobile);//详细通话信息
			telecomHubeiService.getSmsRecord(messageLogin, taskMobile);//短信详单信息
			telecomHubeiService.getRechargeRecord(messageLogin, taskMobile);//充值缴费信息
			telecomHubeiService.getServiceinfo(messageLogin, taskMobile);//套餐信息
			//telecomHubeiService.getPointrecords(messageLogin, taskMobile);//积分信息
		}
   }
}
