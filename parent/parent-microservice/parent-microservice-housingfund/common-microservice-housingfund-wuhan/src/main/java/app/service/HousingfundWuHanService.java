package app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.service.common.aop.ISms;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuhan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuhan")
public class HousingfundWuHanService extends HousingBasicService implements ICrawlerLogin,ISms{

	@Autowired
	private LoginAndGetService loginAndGetService;
	@Autowired
	private HousingfundWuHanUnitService housingfundWuHanUnitService;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {

		WebParamHousing webParamHousing = null;
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);//----------------?
		taskHousing.setLoginMessageJson(messageLoginJson);
		System.out.println(messageLoginForHousing.toString());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription("登录方式只支持身份证 ，手机号登录");
		save(taskHousing);
		loginAndGetService.Idcard(taskHousing,messageLoginForHousing);
		
		return null;
	}

	@Override
	public TaskHousing verifySms(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhasestatus());
		taskHousing.setDescription("正在验证短信验证码");
		save(taskHousing);
		loginAndGetService.setPhoneCode(messageLoginForHousing,taskHousing);
		return taskHousing;
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("parser.crawler.taskid",taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		save(taskHousing);
		
		housingfundWuHanUnitService.getUserinfo(messageLoginForHousing,taskHousing);
		
		housingfundWuHanUnitService.getPayStatus(messageLoginForHousing,taskHousing);
		
		return null;
	}




	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TaskHousing sendSms(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}




}





