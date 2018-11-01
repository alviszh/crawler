package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.service.common.aop.ISms;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.haerbin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.haerbin")
public class HousingFundHaErBinService extends HousingBasicService implements ICrawlerLogin,ISms{

	@Autowired
	private HousingFundHaErBinCommonService housingHaErBinCommonService;
	//登陆
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingHaErBinCommonService.login(messageLoginForHousing,taskHousing);
		return taskHousing;
	}

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingHaErBinCommonService.getUserInfo(messageLoginForHousing,taskHousing);
		housingHaErBinCommonService.getAccount(messageLoginForHousing,taskHousing);
		return taskHousing;
	}

	@Async
	@Override
	public TaskHousing sendSms(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingHaErBinCommonService.getCode(messageLoginForHousing,taskHousing);
		return taskHousing;
		
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


	
	@Override
	public TaskHousing verifySms(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}

}
