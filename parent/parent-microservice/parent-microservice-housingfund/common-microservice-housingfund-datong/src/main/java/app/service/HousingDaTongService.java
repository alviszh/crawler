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
import com.microservice.dao.entity.crawler.housing.datong.HousingDaTongHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.datong.HousingDaTongHtmlRepository;
import com.microservice.dao.repository.crawler.housing.datong.HousingDaTongPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.datong.HousingDaTongUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundDaTongHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.datong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.datong")
public class HousingDaTongService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingDaTongService.class);
	@Autowired
	private HousingDaTongHtmlRepository housingDaTongHtmlRepository;
	@Autowired
	private HousingDaTongUserInfoRepository housingDaTongUserInfoRepository;
	@Autowired
	private HousingDaTongPaydetailsRepository housingDaTongPaydetailsRepository;
	@Autowired
	private HousingFundDaTongHtmlunit housingFundDaTongHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingDaTongService.getUserInfo.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			 webParam = housingFundDaTongHtmlunit.getUserInfo(messageLoginForHousing, taskHousing);	
				if (null !=webParam.getHtml()) {
					HousingDaTongHtml housingDaTongHtml = new HousingDaTongHtml();
					housingDaTongHtml.setPageCount(1);
					housingDaTongHtml.setHtml(webParam.getHtml());
					housingDaTongHtml.setType("userInfo");
					housingDaTongHtml.setUrl(webParam.getUrl());
					housingDaTongHtml.setTaskid(taskHousing.getTaskid());
					housingDaTongHtmlRepository.save(housingDaTongHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingDaTongUserInfoRepository.save(webParam.getUserInfo());					
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
		tracer.addTag("parser.HousingDaTongService.getPaydetails.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			 webParam = housingFundDaTongHtmlunit.getPaydetails(messageLoginForHousing, taskHousing);	
			if (null !=webParam.getHtml()) {
				HousingDaTongHtml housingDaTongHtml = new HousingDaTongHtml();
				housingDaTongHtml.setPageCount(1);
				housingDaTongHtml.setHtml(webParam.getHtml());
				housingDaTongHtml.setType("paydetails");
				housingDaTongHtml.setUrl(webParam.getUrl());
				housingDaTongHtml.setTaskid(taskHousing.getTaskid());
				housingDaTongHtmlRepository.save(housingDaTongHtml);				
			}				
			if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
				housingDaTongPaydetailsRepository.saveAll(webParam.getPaydetails());
				tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						200 , taskHousing.getTaskid());			
			}else{
				tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						201 , taskHousing.getTaskid());				
			}
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getPaydetails.Exception", e.toString());
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());			
			e.printStackTrace();
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
}