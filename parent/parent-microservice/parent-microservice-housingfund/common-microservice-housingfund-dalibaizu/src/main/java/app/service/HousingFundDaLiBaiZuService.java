package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.dalibaizu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.dalibaizu")
public class HousingFundDaLiBaiZuService extends HousingBasicService{

	@Autowired
	private HousingFundDaLiBaiZuCommonService housingFundDalibaizuCommonService;

	public void crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		housingFundDalibaizuCommonService.getDetailInfo(messageLoginForHousing,taskHousing);
	}
	
}
