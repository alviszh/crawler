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
import com.microservice.dao.entity.crawler.housing.yichun.HousingYiChunHtml;
import com.microservice.dao.entity.crawler.housing.zhaotong.HousingZhaoTongBase;
import com.microservice.dao.repository.crawler.housing.yichun.HousingYiChunBaseRepository;
import com.microservice.dao.repository.crawler.housing.yichun.HousingYiChunHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundYiChunParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yichun")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yichun")
public class HousingFundYiChunCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundYiChunParser housingFundYiChunParser;
	@Autowired
	private HousingYiChunBaseRepository baseRepository;
	@Autowired
	private HousingYiChunHtmlRepository htmlRepository;


	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundYiChunParser.login(messageLoginForHousing,taskHousing);
			if(null==webParam){
				tracer.addTag("parser.login.ERROR.NUMORPASSWORD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(102);
				save(taskHousing);
			}else if (webParam.getHtml().contains("单位月交")){
				tracer.addTag("yichun.action.getBase.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【个人基本信息】采集完成！", 200, taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】无数据", 201, taskHousing.getTaskid());
				taskHousing.setFinished(true);
				HousingYiChunHtml  html = new HousingYiChunHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				baseRepository.save(webParam.getYichunBase());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

}
