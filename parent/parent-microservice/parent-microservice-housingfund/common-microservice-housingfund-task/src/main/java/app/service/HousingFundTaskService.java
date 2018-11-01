package app.service;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingFundJsonBean;
import com.microservice.dao.entity.crawler.housing.basic.AreaCode;
import com.microservice.dao.entity.crawler.housing.basic.BasicUserHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.AreaCodeRepository;
import com.netflix.appinfo.InstanceInfo;

import app.service.common.HousingBasicService;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.basic" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.basic" })
public class HousingFundTaskService extends HousingBasicService {

	public static final Logger log = LoggerFactory.getLogger(HousingFundTaskService.class);

	@Autowired
	private AreaCodeRepository areaCodeRepository;

	/**
	 * @Description: 创建用户及生成taskID ()
	 * @param insuranceJsonBean
	 * @return TaskInsurance
	 */
	public TaskHousing createTask(HousingFundJsonBean housingFundJsonBean) {
		TaskHousing taskHousing = new TaskHousing();
		if (null == housingFundJsonBean) {
			log.info("housingFundJsonBean is null !");
			throw new RuntimeException("housingFundJsonBean is null");
		} else if (null == housingFundJsonBean.getIdnum()) {
			log.info("insuranceJsonBean idNum is null !");
			throw new RuntimeException("insuranceJsonBean field(idNum) is null");
		} else if (null == housingFundJsonBean.getUsername()) {
			log.info("housingFundJsonBean username is null !");
			throw new RuntimeException("housingFundJsonBean field(username) is null");
		} else {
			BasicUserHousing basicUserHousing = basicUserHousingRepository
					.findByNameAndIdnum(housingFundJsonBean.getUsername(), housingFundJsonBean.getIdnum());
			if (null == basicUserHousing) {
				basicUserHousing = new BasicUserHousing();
				basicUserHousing.setIdnum(housingFundJsonBean.getIdnum());
				basicUserHousing.setName(housingFundJsonBean.getUsername());
				taskHousing.setCity(housingFundJsonBean.getCity());
				basicUserHousingRepository.save(basicUserHousing);
				log.info("用户不存在   :" + basicUserHousing.toString());

				String uuid = UUID.randomUUID().toString();
				taskHousing.setTaskid(uuid);
				taskHousing.setBasicUserHousing(basicUserHousing);
				taskHousing.setCity(housingFundJsonBean.getCity());
				taskHousing = taskHousingRepository.save(taskHousing);
				log.info("taskHousing 生成taskid  :" + taskHousing.getTaskid());
			} else {
				log.info("用户已存在   :" + basicUserHousing.toString());
				String uuid = UUID.randomUUID().toString();
				taskHousing.setTaskid(uuid);
				taskHousing.setBasicUserHousing(basicUserHousing);
				taskHousing.setCity(housingFundJsonBean.getCity());
				taskHousing = taskHousingRepository.save(taskHousing);
				log.info("taskHousing 生成taskid  :" + taskHousing.getTaskid());
			}
		}

		return taskHousing;
	}
	
	public TaskHousing getTaskHousing(String taskid) {
		tracer.addTag("taskHousingRepository getTaskHousing",taskid);
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(taskid);
		return taskHousing;
	}
	
	public TaskHousing saveTaskHousing(TaskHousing taskHousing) {
		tracer.addTag("taskHousingRepository getTaskHousing",taskHousing.getTaskid());
		  
		return taskHousingRepository.save(taskHousing);
	}

	public List<AreaCode> getCitys() {
		List<AreaCode> list=  areaCodeRepository.findByIsHousingfundFinishedLessThan(3);
		for(AreaCode areaCode:list){
			String nameEn = areaCode.getRegionNameEn();
			String regionIn = nameEn.trim().substring(0, 1);
			areaCode.setRegionNameInitial(regionIn);			
		}
		return list;
	}
	
	public AreaCode getCitysByTaskName(String taskname) {
		return areaCodeRepository.findByTaskname(taskname);
	}
	
	public List<AreaCode> updateCitys(InstanceInfo instanceInfo) {
		return areaCodeRepository.findByIsHousingfundFinishedLessThan(0);
	}
	
	public AreaCode save(AreaCode areaCode){
		return areaCodeRepository.save(areaCode);
	}

}
