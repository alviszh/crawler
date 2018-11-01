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
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouDetail;
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouHtml;
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.bozhou.HousingBoZhouDetailRepository;
import com.microservice.dao.repository.crawler.housing.bozhou.HousingBoZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.bozhou.HousingBoZhouUserInfoRepository;

import app.common.WebParam;
import app.parser.HousingFundBozhouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.bozhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.bozhou")
public class HousingFundBozhouCommonService extends HousingBasicService{
	@Autowired
	private HousingFundBozhouParser housingFundBoZhouParser;
	@Autowired
	private HousingBoZhouDetailRepository  bozhouDetailRepository;
	@Autowired
	private HousingBoZhouHtmlRepository  bozhouhtmlRepository;
	@Autowired
	private HousingBoZhouUserInfoRepository bozhouUserInfoRepository;
	
	@Async
	public void crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingBoZhouUserInfo> webParam = housingFundBoZhouParser.getUserInfo(messageLoginForHousing,taskHousing);
			if(null!=webParam){
			tracer.addTag("bozhou.action.getUserInfo.SUCCESS", taskHousing.getTaskid());
			updateUserInfoStatusByTaskid("【个人基本信息】已采集完成！", 200, taskHousing.getTaskid());
			updatePayStatusByTaskid("【缴费明细信息】系统无数据！", 201, taskHousing.getTaskid());
			HousingBoZhouHtml html = new HousingBoZhouHtml();
			html.setHtml(webParam.getHtml());
			html.setTaskid(messageLoginForHousing.getTask_id());
			html.setUrl(webParam.getUrl());
			bozhouhtmlRepository.save(html);
			bozhouUserInfoRepository.saveAll(webParam.getList());
			updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Async
	public void crawlerDetail(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingBoZhouDetail> webParam = housingFundBoZhouParser.getDetail(messageLoginForHousing,taskHousing);
			if(null!=webParam){
				tracer.addTag("bozhou.action.getDetail.SUCCESS", taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】系统无数据！", 200, taskHousing.getTaskid());
				HousingBoZhouHtml html = new HousingBoZhouHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				bozhouhtmlRepository.save(html);
				bozhouDetailRepository.saveAll(webParam.getList());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	
	
}
