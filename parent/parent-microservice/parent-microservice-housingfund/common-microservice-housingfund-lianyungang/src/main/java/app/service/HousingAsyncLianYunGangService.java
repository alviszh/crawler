package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;

@Component
public class HousingAsyncLianYunGangService extends HousingBasicService {

	@Autowired
	private HousingLianYunGangService housingLianYunGangService;

	public void crawler(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = housingLianYunGangService.login(messageLoginForHousing);

		if (taskHousing.getPhase().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase()) != -1 && taskHousing
				.getPhase_status().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus()) != -1) {
			housingLianYunGangService.getAllData(messageLoginForHousing);
		}
	}

}