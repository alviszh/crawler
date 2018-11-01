package app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.qiqihaer.HousingQiqihaerAccount;
import com.microservice.dao.entity.crawler.housing.qiqihaer.HousingQiqihaerHtml;
import com.microservice.dao.repository.crawler.housing.qiqihaer.HousingQiqihaerAccountRepository;
import com.microservice.dao.repository.crawler.housing.qiqihaer.HousingQiqihaerHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundQiqihaerParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.qiqihaer")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.qiqihaer")
public class HousingFundQiqihaerCommonService extends HousingBasicService{
	@Autowired
	private HousingFundQiqihaerParser housingFundQiqihaerParser;
	@Autowired
	private HousingQiqihaerAccountRepository accountRepository;
	@Autowired
	private HousingQiqihaerHtmlRepository htmlRepository;
	
	
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception {
		try {
			WebParam<HousingQiqihaerAccount> webParam = housingFundQiqihaerParser.crawler(messageLoginForHousing,taskHousing);
			if(null!=webParam){
				tracer.addTag("qiqihaer.action.getDetail.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【个人基本信息】采集完成！", 200, taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 201, taskHousing.getTaskid());
				taskHousing.setFinished(true);
				HousingQiqihaerHtml  html = new HousingQiqihaerHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				accountRepository.save(webParam.getHousingQiqihaerAccount());
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
