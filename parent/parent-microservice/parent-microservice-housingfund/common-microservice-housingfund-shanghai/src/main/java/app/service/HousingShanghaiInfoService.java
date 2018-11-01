package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;

@Component
public class HousingShanghaiInfoService{

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private HousingShanghaiFutureService housingShanghaiFutureService;
	
	@Async
	public void crawler(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = housingShanghaiFutureService.login(messageLoginForHousing);
		if (taskHousing.getPhase().equals(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase()) && taskHousing
				.getPhase_status().equals(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus())) {
			housingShanghaiFutureService.getAllData(messageLoginForHousing);
		}else{
			tracer.addTag("登录失败，taskHousing", taskHousing.toString());
		}
	}
	

}