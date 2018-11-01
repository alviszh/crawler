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
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinBasic;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinDetail;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinHtml;
import com.microservice.dao.repository.crawler.housing.yulin.HousingYulinBasicRepository;
import com.microservice.dao.repository.crawler.housing.yulin.HousingYulinDetailRepository;
import com.microservice.dao.repository.crawler.housing.yulin.HousingYulinHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundYuLinParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yulin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yulin")
public class HousingFundYuLinCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundYuLinParser housingFundYuLinParser;
	@Autowired
	private HousingYulinBasicRepository basicRepository;
	@Autowired
	private HousingYulinHtmlRepository htmlRepository;
	@Autowired
	private HousingYulinDetailRepository detailRepository;
	
	


	@Async
	public void getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingYuLinDetail> webParam = housingFundYuLinParser.getDetailInfo(messageLoginForHousing,taskHousing);
			if(null!=webParam){
				tracer.addTag("dalibaizu.action.getDetail.SUCCESS", taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 200, taskHousing.getTaskid());
				HousingYuLinHtml  html = new HousingYuLinHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				detailRepository.saveAll(webParam.getList());
				updateTaskHousing(taskHousing.getTaskid());
			}else{
				tracer.addTag("dalibaizu.action.getDetail.ERROR", taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】 无数据", 201, taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	public void getBasicInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingYuLinBasic> webParam = housingFundYuLinParser.getBasicInfo(messageLoginForHousing,taskHousing);
			if(null!=webParam){
				tracer.addTag("action.yulin.getBasicInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【公积金基本】已采集完成！", 200, taskHousing.getTaskid());
				HousingYuLinHtml html = new HousingYuLinHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				basicRepository.save(webParam.getYulinBasic());
				htmlRepository.save(html);
				updateTaskHousing(taskHousing.getTaskid());
			}else{
				updateUserInfoStatusByTaskid("【公积金基本情况】 无数据", 201, taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
