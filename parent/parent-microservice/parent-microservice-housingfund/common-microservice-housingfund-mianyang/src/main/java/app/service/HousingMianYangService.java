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
import com.microservice.dao.entity.crawler.housing.mianyang.HousingMianYangHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.mianyang.HousingMianYangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.mianyang.HousingMianYangPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.mianyang.HousingMianYangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundMianYangHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.mianyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.mianyang")
public class HousingMianYangService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingMianYangService.class);
	@Autowired
	private HousingMianYangHtmlRepository housingMianYangHtmlRepository;
	@Autowired
	private HousingMianYangPaydetailsRepository housingMianYangPaydetailsRepository;
	@Autowired
	private HousingMianYangUserInfoRepository housingMianYangUserInfoRepository;
	@Autowired
	private HousingFundMianYangHtmlunit housingFundMianYangHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingMianYangService.getUserInfo.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundMianYangHtmlunit.getUserInfo(messageLoginForHousing, taskHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingMianYangHtml housingMianYangHtml = new HousingMianYangHtml();
					housingMianYangHtml.setPageCount(1);
					housingMianYangHtml.setHtml(webParam.getHtml());
					housingMianYangHtml.setType("userInfo");
					housingMianYangHtml.setUrl(webParam.getUrl());
					housingMianYangHtml.setTaskid(taskHousing.getTaskid());
					housingMianYangHtmlRepository.save(housingMianYangHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingMianYangUserInfoRepository.save(webParam.getUserInfo());					
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
					taskHousing.setUserinfoStatus(200);
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getError_code());
					save(taskHousing);
				}else{
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
					taskHousing.setUserinfoStatus(201);
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getError_code());
					save(taskHousing);
				}
			}else{			
				tracer.addTag("parser.housing.crawler.getUserInfo", "获取用户信息失败");
				taskHousing.setUserinfoStatus(500);
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getError_code());
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getUserInfo", "获取用户信息失败");
			taskHousing.setUserinfoStatus(500);
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getError_code());
			save(taskHousing);
			e.printStackTrace();
		}
	}
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing){		
		tracer.addTag("parser.HousingMianYangService.getPaydetails.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundMianYangHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingMianYangHtml housingMianYangHtml = new HousingMianYangHtml();
					housingMianYangHtml.setPageCount(1);
					housingMianYangHtml.setHtml(webParam.getHtml());
					housingMianYangHtml.setType("paydetails");
					housingMianYangHtml.setUrl(webParam.getUrl());
					housingMianYangHtml.setTaskid(taskHousing.getTaskid());
					housingMianYangHtmlRepository.save(housingMianYangHtml);				
				}				
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingMianYangPaydetailsRepository.saveAll(webParam.getPaydetails());
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					updateTaskHousing(taskHousing.getTaskid());
				}else{
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
					updateTaskHousing(taskHousing.getTaskid());
				}
			}else{
				tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息采集失败");
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						500 , taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getPaydetails.Exception", e.toString());
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid());
		}
	}
}