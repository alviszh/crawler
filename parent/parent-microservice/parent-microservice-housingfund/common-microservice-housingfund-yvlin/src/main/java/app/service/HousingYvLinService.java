package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;

@Component
@EnableAsync
public class HousingYvLinService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingYvLinService.class);
	
	@Autowired
	private HousingYvLinCrawlerService housingYvLinCrawlerService;
	
	@Async
	public void crawler(MessageLoginForHousing messageLoginForHousing){
		TaskHousing taskHousing = housingYvLinCrawlerService.login(messageLoginForHousing);
		if(taskHousing.getPhase().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus())!=-1){
			housingYvLinCrawlerService.getAllData(messageLoginForHousing);
		}
	}
}