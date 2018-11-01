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
import com.microservice.dao.entity.crawler.housing.yanbian.HousingYanbianHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.yanbian.HousingYanbianHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yanbian.HousingYanbianPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.yanbian.HousingYanbianUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundYanbianHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yanbian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yanbian")
public class HousingYanbianService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingYanbianService.class);
	@Autowired
	private HousingYanbianHtmlRepository housingYanbianHtmlRepository;
	@Autowired
	private HousingYanbianPaydetailsRepository housingYanbianPaydetailsRepository;
	@Autowired
	private HousingYanbianUserInfoRepository housingYanbianUserInfoRepository;
	@Autowired
	private HousingFundYanbianHtmlunit housingFundYanbianHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;	
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("获取用户信息", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundYanbianHtmlunit.getUserInfo(messageLoginForHousing, taskHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingYanbianHtml housingYanbianHtml = new HousingYanbianHtml();
					housingYanbianHtml.setPageCount(1);
					housingYanbianHtml.setHtml(webParam.getHtml());
					housingYanbianHtml.setType("userInfo");
					housingYanbianHtml.setUrl(webParam.getUrl());
					housingYanbianHtml.setTaskid(taskHousing.getTaskid());
					housingYanbianHtmlRepository.save(housingYanbianHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingYanbianUserInfoRepository.save(webParam.getUserInfo());
					tracer.addTag("action.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());		
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					updateTaskHousing(taskHousing.getTaskid());					
				}else{
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
					tracer.addTag("action.housing.crawler.getUserInfo", "用户信息为空");	
					updateTaskHousing(taskHousing.getTaskid());
				}
			}else{
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());
				tracer.addTag("action.housing.crawler.getUserInfo", "获取用户信息失败");
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			tracer.addTag("action.housing.crawler.getUserInfo", "获取用户信息失败");
			updateTaskHousing(taskHousing.getTaskid());
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
			 webParam = housingFundYanbianHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingYanbianHtml housingYanbianHtml = new HousingYanbianHtml();
					housingYanbianHtml.setPageCount(1);
					housingYanbianHtml.setHtml(webParam.getHtml());
					housingYanbianHtml.setType("paydetails");
					housingYanbianHtml.setUrl(webParam.getUrl());
					housingYanbianHtml.setTaskid(taskHousing.getTaskid());
					housingYanbianHtmlRepository.save(housingYanbianHtml);			
				}				
				if (null !=webParam.getPaydetails()) {
					housingYanbianPaydetailsRepository.saveAll(webParam.getPaydetails());
					tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());			
					updateTaskHousing(taskHousing.getTaskid());
				}else{
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
					tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息为空");	
					updateTaskHousing(taskHousing.getTaskid());
				}
			}else{
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());
				tracer.addTag("action.housing.crawler.getPaydetails", "获取缴存明细信息失败");
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			tracer.addTag("action.housing.crawler.getPaydetails", "获取缴存明细信息失败");
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
}