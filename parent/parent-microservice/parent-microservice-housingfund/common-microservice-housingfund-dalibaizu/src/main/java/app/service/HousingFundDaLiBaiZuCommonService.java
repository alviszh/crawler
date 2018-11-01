package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.dalibaizu.HousingDaLiBaiZuDetail;
import com.microservice.dao.entity.crawler.housing.dalibaizu.HousingDaLiBaiZuHtml;
import com.microservice.dao.repository.crawler.housing.dalibaizu.HousingDaLiBaiZuDetailRepository;
import com.microservice.dao.repository.crawler.housing.dalibaizu.HousingDaLiBaiZuHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundDaLiBaiZuParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.dalibaizu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.dalibaizu")
public class HousingFundDaLiBaiZuCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundDaLiBaiZuParser housingFundDalibaizuParser;
	@Autowired
	private HousingDaLiBaiZuHtmlRepository htmlRepository;
	@Autowired
	private HousingDaLiBaiZuDetailRepository detailRepository;
	
	
	


	@Async
	public void getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingDaLiBaiZuDetail> webParam = housingFundDalibaizuParser.getDetailInfo(messageLoginForHousing,taskHousing);
			tracer.addTag("dalibaizu.action.getDetail.SUCCESS", taskHousing.getTaskid());
			updateUserInfoStatusByTaskid("【个人基本信息】系统无数据！", 201, taskHousing.getTaskid());
			updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 200, taskHousing.getTaskid());
			HousingDaLiBaiZuHtml  html = new HousingDaLiBaiZuHtml();
			html.setHtml(webParam.getHtml());
			html.setTaskid(messageLoginForHousing.getTask_id());
			html.setUrl(webParam.getUrl());
			htmlRepository.save(html);
			detailRepository.saveAll(webParam.getList());
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
