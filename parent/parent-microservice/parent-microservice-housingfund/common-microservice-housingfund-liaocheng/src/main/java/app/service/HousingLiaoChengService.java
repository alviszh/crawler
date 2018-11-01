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
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.liaocheng.HousingLiaoChengHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.liaocheng.HousingLiaoChengHtmlRepository;
import com.microservice.dao.repository.crawler.housing.liaocheng.HousingLiaoChengPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.liaocheng.HousingLiaoChengUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundLiaoChengHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.liaocheng")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.liaocheng")
public class HousingLiaoChengService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingLiaoChengService.class);
	@Autowired
	private HousingLiaoChengHtmlRepository housingLiaoChengHtmlRepository;
	@Autowired
	private HousingLiaoChengUserInfoRepository housingLiaoChengUserInfoRepository;
	@Autowired
	private HousingLiaoChengPaydetailsRepository housingLiaoChengPaydetailsRepository;
	@Autowired
	private HousingFundLiaoChengHtmlunit housingFundLiaoChengHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String grzh) {
		tracer.addTag("parser.HousingLiaoChengService.getUserInfo.taskid", taskHousing.getTaskid());
		tracer.addTag("parser.HousingLiaoChengService.getUserInfo.grzh", grzh);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundLiaoChengHtmlunit.getUserInfo(messageLoginForHousing, taskHousing,grzh,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingLiaoChengHtml housingLiaoChengHtml = new HousingLiaoChengHtml();
					housingLiaoChengHtml.setPageCount(1);
					housingLiaoChengHtml.setHtml(webParam.getHtml());
					housingLiaoChengHtml.setType("userInfo");
					housingLiaoChengHtml.setUrl(webParam.getUrl());
					housingLiaoChengHtml.setTaskid(taskHousing.getTaskid());
					housingLiaoChengHtmlRepository.save(housingLiaoChengHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingLiaoChengUserInfoRepository.save(webParam.getUserInfo());					
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
			e.printStackTrace();
		}
	}
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,String grzh){		
		tracer.addTag("parser.HousingLiaoChengService.getPaydetails.taskid", taskHousing.getTaskid());
		tracer.addTag("parser.HousingLiaoChengService.getPaydetails.grzh", grzh);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundLiaoChengHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,grzh,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingLiaoChengHtml housingLiaoChengHtml = new HousingLiaoChengHtml();
					housingLiaoChengHtml.setPageCount(1);
					housingLiaoChengHtml.setHtml(webParam.getHtml());
					housingLiaoChengHtml.setType("paydetails");
					housingLiaoChengHtml.setUrl(webParam.getUrl());
					housingLiaoChengHtml.setTaskid(taskHousing.getTaskid());
					housingLiaoChengHtmlRepository.save(housingLiaoChengHtml);				
				}				
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingLiaoChengPaydetailsRepository.saveAll(webParam.getPaydetails());
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
				tracer.addTag("parser.housing.crawler.getPaydetails", "获取缴存明细信息失败");
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息采集失败");
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
		
			e.printStackTrace();
		}
	}
}