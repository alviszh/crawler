package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.dezhou.HousingDeZhouDepositInformation;
import com.microservice.dao.entity.crawler.housing.dezhou.HousingDezhouHtml;
import com.microservice.dao.repository.crawler.housing.dezhou.HousingDezhouDepositRepository;
import com.microservice.dao.repository.crawler.housing.dezhou.HousingDezhouHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundDezhouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.dezhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.dezhou")
public class HousingFundDezhouCommonService extends HousingBasicService{
	@Autowired
	private HousingFundDezhouParser housingFundDeZhouParser;
	@Autowired
	private HousingDezhouDepositRepository  dezhouDepositRepository;
	@Autowired
	private HousingDezhouHtmlRepository  dezhouhtmlRepository;
	
	
	public void crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingDeZhouDepositInformation> webParam = housingFundDeZhouParser.getDetailInfo(messageLoginForHousing,taskHousing);
			if(null!=webParam){
			tracer.addTag("dezhou.action.getDetail.SUCCESS", taskHousing.getTaskid());
			updateUserInfoStatusByTaskid("【个人基本信息】已采集完成！", 200, taskHousing.getTaskid());
			updatePayStatusByTaskid("【缴费明细信息】系统无数据！", 201, taskHousing.getTaskid());
			HousingDezhouHtml html = new HousingDezhouHtml();
			html.setHtml(webParam.getHtml());
			html.setTaskid(messageLoginForHousing.getTask_id());
			html.setUrl(webParam.getUrl());
			dezhouhtmlRepository.save(html);
			dezhouDepositRepository.save(webParam.getHousingDezhouDepositInfo());
			updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	
}
