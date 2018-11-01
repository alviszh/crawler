package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chenzhou.HousingChenZhouHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.chenzhou.HousingChenZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.chenzhou.HousingChenZhouPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.chenzhou.HousingChenZhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.HousingFundChenZhouHtmlunit;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chenzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chenzhou")
public class HousingChenZhouService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingChenZhouService.class);
	@Autowired
	private HousingChenZhouHtmlRepository housingChenZhouHtmlRepository;
	@Autowired
	private HousingChenZhouUserInfoRepository housingChenZhouUserInfoRepository;
	@Autowired
	private HousingChenZhouPaydetailsRepository housingChenZhouPaydetailsRepository;
	@Autowired
	private HousingFundChenZhouHtmlunit  housingFundChenZhouHtmlunit;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingChenZhouService.getUserInfo.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			 webParam = housingFundChenZhouHtmlunit.getUserInfo(messageLoginForHousing, taskHousing);	
				if (null !=webParam.getHtml()) {
					HousingChenZhouHtml housingChenZhouHtml = new HousingChenZhouHtml();
					housingChenZhouHtml.setPageCount(1);
					housingChenZhouHtml.setHtml(webParam.getHtml());
					housingChenZhouHtml.setType("userInfo");
					housingChenZhouHtml.setUrl(webParam.getUrl());
					housingChenZhouHtml.setTaskid(taskHousing.getTaskid());
					housingChenZhouHtmlRepository.save(housingChenZhouHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingChenZhouUserInfoRepository.save(webParam.getUserInfo());					
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());				
				}else{
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());													
				}		
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getUserInfo.Exception", e.toString());
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());								
			e.printStackTrace();
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing){		
		tracer.addTag("parser.HousingChenZhouService.getPaydetails.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {					
			 webParam = housingFundChenZhouHtmlunit.getPaydetails(messageLoginForHousing, taskHousing);	
			if (null !=webParam.getHtml()) {
				HousingChenZhouHtml housingChenZhouHtml = new HousingChenZhouHtml();
				housingChenZhouHtml.setPageCount(1);
				housingChenZhouHtml.setHtml(webParam.getHtml());
				housingChenZhouHtml.setType("paydetails");
				housingChenZhouHtml.setUrl(webParam.getUrl());
				housingChenZhouHtml.setTaskid(taskHousing.getTaskid());
				housingChenZhouHtmlRepository.save(housingChenZhouHtml);				
			}				
			if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
				housingChenZhouPaydetailsRepository.saveAll(webParam.getPaydetails());
				tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						200 , taskHousing.getTaskid());
			}else{
				tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						201 , taskHousing.getTaskid());
			}
			
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息采集失败");
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			e.printStackTrace();
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
}