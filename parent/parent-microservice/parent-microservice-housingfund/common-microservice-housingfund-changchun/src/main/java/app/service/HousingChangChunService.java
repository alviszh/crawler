package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync

public class HousingChangChunService extends HousingBasicService {

	@Autowired
	private HousingChangChunFutureService housingChangChunFutureService;
	
	@Async
	public void crawler(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = housingChangChunFutureService.login(messageLoginForHousing);

		if (taskHousing.getPhase().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase()) != -1 && taskHousing
				.getPhase_status().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus()) != -1) {
			housingChangChunFutureService.getAllData(messageLoginForHousing);
		}
	}
}
