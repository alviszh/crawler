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

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.chongqing.HousingChongqingAccountInfoRepository;
import com.microservice.dao.repository.crawler.housing.chongqing.HousingChongqingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.chongqing.HousingChongqingPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.chongqing.HousingChongqingUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundChongQingHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chongqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chongqing")
public class HousingChongQingService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingChongQingService.class);
	
	@Autowired
	private HousingChongqingAccountInfoRepository housingChongqingAccountInfoRepository;
	@Autowired
	private HousingChongqingHtmlRepository housingChongqingHtmlRepository;
	@Autowired
	private HousingChongqingPaydetailsRepository housingChongqingPaydetailsRepository;
	@Autowired
	private HousingChongqingUserInfoRepository housingChongqingUserInfoRepository;
	@Autowired
	private HousingFundChongQingHtmlunit housingFundChongQingHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;	
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("获取用户信息", messageLoginForHousing.getTask_id());
		TaskHousing	taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundChongQingHtmlunit.getUserInfo(messageLoginForHousing, count);
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingChongqingHtml housingChongqingHtml = new HousingChongqingHtml();
					housingChongqingHtml.setPageCount(1);
					housingChongqingHtml.setHtml(webParam.getHtml());
					housingChongqingHtml.setType("userInfo");
					housingChongqingHtml.setUrl(webParam.getUrl());
					housingChongqingHtml.setTaskid(taskHousing.getTaskid());
					housingChongqingHtmlRepository.save(housingChongqingHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingChongqingUserInfoRepository.save(webParam.getUserInfo());
					updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
					updateTaskHousing(taskHousing.getTaskid());
				}else{
					updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());								
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
					updateTaskHousing(taskHousing.getTaskid());
				}
			}else{
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());						
				tracer.addTag("parser.housing.crawler.getUserInfo", "获取用户信息失败");
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
				500 , taskHousing.getTaskid());
			tracer.addTag("parser.housing.crawler.getUserInfo", "获取用户信息失败");
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
	@Async
	public void getAccountInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("获取账户信息", messageLoginForHousing.getTask_id());		
		TaskHousing	taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());		
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundChongQingHtmlunit.getAccountInfo(messageLoginForHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingChongqingHtml housingChongqingHtml = new HousingChongqingHtml();
					housingChongqingHtml.setPageCount(1);
					housingChongqingHtml.setHtml(webParam.getHtml());
					housingChongqingHtml.setType("accountInfo");
					housingChongqingHtml.setUrl(webParam.getUrl());
					housingChongqingHtml.setTaskid(taskHousing.getTaskid());
					housingChongqingHtmlRepository.save(housingChongqingHtml);		
				}				
				if (null !=webParam.getAccountInfo()) {
					housingChongqingAccountInfoRepository.save(webParam.getAccountInfo());					
					tracer.addTag("parser.housing.crawler.getAccountInfo", "账户信息入库"+webParam.getAccountInfo());
				}else{			
					tracer.addTag("parser.housing.crawler.getAccountInfo", "账户信息为空");
				}
			}else{				
				tracer.addTag("parser.housing.crawler.getAccountInfo", "获取账户信息失败");
			}
		} catch (Exception e) {			
			tracer.addTag("parser.housing.crawler.getAccountInfo.Exception", e.toString());
			e.printStackTrace();
		}
	}
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing){		
		tracer.addTag("获取账户缴存明细信息", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundChongQingHtmlunit.getPaydetails(messageLoginForHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingChongqingHtml housingChongqingHtml = new HousingChongqingHtml();
					housingChongqingHtml.setPageCount(1);
					housingChongqingHtml.setHtml(webParam.getHtml());
					housingChongqingHtml.setType("paydetails");
					housingChongqingHtml.setUrl(webParam.getUrl());
					housingChongqingHtml.setTaskid(taskHousing.getTaskid());
					housingChongqingHtmlRepository.save(housingChongqingHtml);		
				}				
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingChongqingPaydetailsRepository.saveAll(webParam.getPaydetails());
					updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
					updateTaskHousing(taskHousing.getTaskid());
				}else{
					updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");
					updateTaskHousing(taskHousing.getTaskid());
				}
			}else{
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());
				tracer.addTag("parser.housing.crawler.getPaydetails", "获取缴存明细信息失败");
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			tracer.addTag("parser.housing.crawler.getPaydetails.Exception", e.toString());
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
}