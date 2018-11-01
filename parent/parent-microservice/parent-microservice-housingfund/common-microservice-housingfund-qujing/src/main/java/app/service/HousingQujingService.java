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
import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.qujing.HousingQujingHtmlRepository;
import com.microservice.dao.repository.crawler.housing.qujing.HousingQujingPayDetailsRepository;
import com.microservice.dao.repository.crawler.housing.qujing.HousingQujingUserInfoRepository;

import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundQujingHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.qujing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.qujing")
public class HousingQujingService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingQujingService.class);
	@Autowired
	private HousingQujingHtmlRepository housingQujingHtmlRepository;
	@Autowired
	private HousingQujingPayDetailsRepository housingQujingPayDetailsRepository;
	@Autowired
	private HousingQujingUserInfoRepository housingQujingUserInfoRepository;
	@Autowired
	private HousingFundQujingHtmlunit housingFundQujingHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("parser.HousingQujingService.getUserInfo.taskid", taskHousing.getTaskid());
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundQujingHtmlunit.getUserInfo(messageLoginForHousing, taskHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingQujingHtml housingQujingHtml = new HousingQujingHtml();
					housingQujingHtml.setPageCount("1");
					housingQujingHtml.setHtml(webParam.getHtml());
					housingQujingHtml.setType("userInfo");
					housingQujingHtml.setUrl(webParam.getUrl());
					housingQujingHtml.setTaskid(taskHousing.getTaskid());
					housingQujingHtmlRepository.save(housingQujingHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingQujingUserInfoRepository.save(webParam.getUserInfo());					
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
			tracer.addTag("parser.housing.crawler.getUserInfo.Exception", e.toString());
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
     
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){		
		tracer.addTag("parser.HousingQujingService.getPaydetails.taskid", taskHousing.getTaskid());
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int temp=0;
			int count=0;
			 webParam = housingFundQujingHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,count);	
				if (null !=webParam.getHtml()) {
					HousingQujingHtml housingQujingHtml = new HousingQujingHtml();
					housingQujingHtml.setPageCount("1");
					housingQujingHtml.setHtml(webParam.getHtml());
					housingQujingHtml.setType("paydetails");
					housingQujingHtml.setUrl(webParam.getUrl());
					housingQujingHtml.setTaskid(taskHousing.getTaskid());
					housingQujingHtmlRepository.save(housingQujingHtml);				
				}				
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingQujingPayDetailsRepository.saveAll(webParam.getPaydetails());
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
					temp++;
				}else{
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");					
				}				
				if (null != webParam.getTotalpage()) {
					int maxPage = Integer.parseInt(webParam.getTotalpage());
					tracer.addTag("parser.telecom.crawler.getPaydetails", maxPage + "");
					if (maxPage > 1) {
						for (int i= 1;i < maxPage; i++) {
						WebParam webParam2 = housingFundQujingHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,i,count);
						if (null !=webParam2.getPaydetails() && !webParam2.getPaydetails().isEmpty()) {
							housingQujingPayDetailsRepository.saveAll(webParam.getPaydetails());
							tracer.addTag("parser.housing.crawler.getPaydetails"+maxPage+"页", "缴存明细信息入库"+webParam.getPaydetails());
						}							
						}
					}
				}								
				if (temp>0) {
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					updateTaskHousing(taskHousing.getTaskid());
				}else{
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
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