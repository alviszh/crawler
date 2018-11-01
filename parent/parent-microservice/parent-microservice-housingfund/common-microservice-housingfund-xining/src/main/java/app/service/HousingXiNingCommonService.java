package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xining")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xining")
public class HousingXiNingCommonService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private HousingXiNingInfoService HousingXiNingInfoService;	
	@Autowired
	private HousingXiNingCrawlerService  housingXiNingCrawlerService;
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingXiNingCommonService.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);		
		try {
			housingXiNingCrawlerService.login(messageLoginForHousing, 1);
		} catch (Exception e) {
			tracer.addTag("HousingXiNingCommonService.login", messageLoginForHousing.getTask_id());
			e.printStackTrace();
		}
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingXiNingCommonService.getAllData.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		HousingXiNingInfoService.getUserInfo(messageLoginForHousing);
		HousingXiNingInfoService.getPay(messageLoginForHousing);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		return null;
	}
}