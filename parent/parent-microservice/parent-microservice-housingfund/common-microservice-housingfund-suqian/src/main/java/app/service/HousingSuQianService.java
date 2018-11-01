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
import com.microservice.dao.entity.crawler.housing.suqian.HousingSuQianHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.suqian.HousingSuQianHtmlRepository;
import com.microservice.dao.repository.crawler.housing.suqian.HousingSuQianPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.suqian.HousingSuQianUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.unit.HousingFundSuQianHtmlunit;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.suqian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.suqian")
public class HousingSuQianService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingSuQianService.class);
	@Autowired
	private HousingSuQianHtmlRepository housingSuQianHtmlRepository;
	@Autowired
	private HousingSuQianUserInfoRepository housingSuQianUserInfoRepository;
	@Autowired
	private HousingSuQianPaydetailsRepository housingSuQianPaydetailsRepository;
	@Autowired
	private HousingFundSuQianHtmlunit housingFundSuQianHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, String grzh) {
		tracer.addTag("parser.HousingSuQianService.getUserInfo.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {			
			int count=0;
			 webParam = housingFundSuQianHtmlunit.getUserInfo(webClient, messageLoginForHousing, taskHousing,grzh,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingSuQianHtml housingSuQianHtml = new HousingSuQianHtml();
					housingSuQianHtml.setPageCount(1);
					housingSuQianHtml.setHtml(webParam.getHtml());
					housingSuQianHtml.setType("userInfo");
					housingSuQianHtml.setUrl(webParam.getUrl());
					housingSuQianHtml.setTaskid(taskHousing.getTaskid());
					housingSuQianHtmlRepository.save(housingSuQianHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingSuQianUserInfoRepository.save(webParam.getUserInfo());					
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
				
					updateTaskHousing(taskHousing.getTaskid());
					
				}else{
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());												
					updateTaskHousing(taskHousing.getTaskid());
				}
			}else{			
				tracer.addTag("parser.housing.crawler.getUserInfo", "获取用户信息失败");
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());												
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getUserInfo", "获取用户信息失败");
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());												
			updateTaskHousing(taskHousing.getTaskid());
		}
	}
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing,String grzh){		
		tracer.addTag("parser.HousingSuQianService.getPaydetails.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundSuQianHtmlunit.getPaydetails(webClient, messageLoginForHousing, taskHousing,grzh,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingSuQianHtml housingSuQianHtml = new HousingSuQianHtml();
					housingSuQianHtml.setPageCount(1);
					housingSuQianHtml.setHtml(webParam.getHtml());
					housingSuQianHtml.setType("paydetails");
					housingSuQianHtml.setUrl(webParam.getUrl());
					housingSuQianHtml.setTaskid(taskHousing.getTaskid());
					housingSuQianHtmlRepository.save(housingSuQianHtml);				
				}				
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingSuQianPaydetailsRepository.saveAll(webParam.getPaydetails());
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
				    updateTaskHousing(taskHousing.getTaskid());
				}else{
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
				    updateTaskHousing(taskHousing.getTaskid());
				}
			}else{
				tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息采集失败");
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());
			    updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getPaydetails.Exception", e.toString());
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
		    updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
}