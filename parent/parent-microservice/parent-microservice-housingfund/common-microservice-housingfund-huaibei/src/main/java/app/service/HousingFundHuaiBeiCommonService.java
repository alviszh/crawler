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
import com.microservice.dao.entity.crawler.housing.huaibei.HousingHuaiBeiBase;
import com.microservice.dao.entity.crawler.housing.huaibei.HousingHuaiBeiHtml;
import com.microservice.dao.repository.crawler.housing.huaibei.HousingHuaiBeiBaseRepository;
import com.microservice.dao.repository.crawler.housing.huaibei.HousingHuaiBeiHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundHuaiBeiParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.huaibei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.huaibei")
public class HousingFundHuaiBeiCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundHuaiBeiParser housingFundHuaiBeiParser;
	@Autowired
	private HousingHuaiBeiBaseRepository baseRepository;
	@Autowired
	private HousingHuaiBeiHtmlRepository htmlRepository;



	public void crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingHuaiBeiBase> webParam = housingFundHuaiBeiParser.crawler(messageLoginForHousing,taskHousing);
			if(null!=webParam){
				tracer.addTag("huaibei.action.getUserInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【个人基本信息】采集完成！", 200, taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 201, taskHousing.getTaskid());
				HousingHuaiBeiHtml  html = new HousingHuaiBeiHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				baseRepository.save(webParam.getHuaiBeiBase());
				updateTaskHousing(taskHousing.getTaskid());
			}else{
				tracer.addTag("huaibei.action.getUserInfo.ERROR", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getDescription());
				taskHousing.setPaymentStatus(102);
				save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
