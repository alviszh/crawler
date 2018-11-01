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
import com.microservice.dao.entity.crawler.housing.xinxiang.HousingXinxiangHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.xinxiang.HousingXinxiangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.xinxiang.HousingXinxiangPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.xinxiang.HousingXinxiangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundXinxiangHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xinxiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xinxiang")
public class HousingXinxiangService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingXinxiangService.class);
	@Autowired
	private HousingXinxiangHtmlRepository housingXinxiangHtmlRepository;
	@Autowired
	private HousingXinxiangPaydetailsRepository housingXinxiangPaydetailsRepository;
	@Autowired
	private HousingXinxiangUserInfoRepository housingXinxiangUserInfoRepository;
	@Autowired
	private HousingFundXinxiangHtmlunit housingFundXinxiangHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;	
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingXinxiangService.getUserInfo.taskid", messageLoginForHousing.getTask_id());
		TaskHousing	taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			 webParam = housingFundXinxiangHtmlunit.getUserInfo(messageLoginForHousing, taskHousing);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingXinxiangHtml housingXinxiangHtml = new HousingXinxiangHtml();
					housingXinxiangHtml.setPageCount(1);
					housingXinxiangHtml.setHtml(webParam.getHtml());
					housingXinxiangHtml.setType("userInfo");
					housingXinxiangHtml.setUrl(webParam.getUrl());
					housingXinxiangHtml.setTaskid(taskHousing.getTaskid());
					housingXinxiangHtmlRepository.save(housingXinxiangHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingXinxiangUserInfoRepository.save(webParam.getUserInfo());					
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
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing){		
		tracer.addTag("parser.HousingXinxiangService.getPaydetails.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			 webParam = housingFundXinxiangHtmlunit.getPaydetails(messageLoginForHousing, taskHousing);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingXinxiangHtml housingXinxiangHtml = new HousingXinxiangHtml();
					housingXinxiangHtml.setPageCount(1);
					housingXinxiangHtml.setHtml(webParam.getHtml());
					housingXinxiangHtml.setType("paydetails");
					housingXinxiangHtml.setUrl(webParam.getUrl());
					housingXinxiangHtml.setTaskid(taskHousing.getTaskid());
					housingXinxiangHtmlRepository.save(housingXinxiangHtml);				
				}				
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingXinxiangPaydetailsRepository.saveAll(webParam.getPaydetails());
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