package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuxi")
public class HousingWuXiFutureService extends HousingBasicService {

	public static final Logger log = LoggerFactory.getLogger(HousingWuXiFutureService.class);
	@Autowired
	private HousingWuXiCrawlerService  housingWuXiCrawlerService;
	@Autowired
	private TracerLog tracer;
	@Async
	public TaskHousing crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		housingWuXiCrawlerService.login(messageLoginForHousing);	
		if(taskHousing.getPhase().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus())!=-1){
				tracer.addTag("parser.HousingWuXiFutureService.crawler", "begin");
				housingWuXiCrawlerService.getAllData(messageLoginForHousing);
		}
		taskHousing = findTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}

}