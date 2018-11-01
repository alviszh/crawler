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
public class TelecomGetAllDataHubeiWapService {

	@Autowired
	private TelecomUnitHubeiWapService TelecomUnitHubeiWapService;

	@Async
	public void getAllData(MessageLogin messageLogin) {
		TelecomUnitHubeiWapService.getUserInfo(messageLogin);// 用户信息
		TelecomUnitHubeiWapService.getPaymonths(messageLogin);// 月账单信息
		TelecomUnitHubeiWapService.getVoiceRecord(messageLogin);// 详细通话信息
		TelecomUnitHubeiWapService.getRechargeRecord(messageLogin);// 充值缴费信息
		TelecomUnitHubeiWapService.getPointrecords(messageLogin);// 积分信息
	}
}
