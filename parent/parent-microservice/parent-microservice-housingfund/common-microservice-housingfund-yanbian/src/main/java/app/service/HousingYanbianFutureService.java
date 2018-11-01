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

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yanbian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yanbian")
public class HousingYanbianFutureService extends HousingBasicService {

	public static final Logger log = LoggerFactory.getLogger(HousingYanbianFutureService.class);
	@Autowired
	private HousingYanbianCrawlerService housingYanbianCrawlerService;
	@Async
	public TaskHousing crawler(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("action.HousingYanbianFutureService.crawler.taskid", messageLoginForHousing.getTask_id());		
	    TaskHousing taskHousing = housingYanbianCrawlerService.login(messageLoginForHousing);
	    if(taskHousing.getPhase().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus())!=-1){			
	    	housingYanbianCrawlerService.getAllData(messageLoginForHousing);			
		}			
		taskHousing = findTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}

}