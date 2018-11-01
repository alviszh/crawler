package app.service;

import java.util.Calendar;

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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yangzhou.HousingYangzhouHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.yangzhou.HousingYangzhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yangzhou.HousingYangzhouPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.yangzhou.HousingYangzhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.HousingFundYangzhouHtmlunit;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yangzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yangzhou")
public class HousingYangzhouService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingYangzhouService.class);
	@Autowired
	private HousingYangzhouHtmlRepository housingYangzhouHtmlRepository;
	@Autowired
	private HousingYangzhouPaydetailsRepository housingYangzhouPaydetailsRepository;
	@Autowired
	private HousingYangzhouUserInfoRepository housingYangzhouUserInfoRepository;
	@Autowired
	private HousingFundYangzhouHtmlunit housingFundYangzhouHtmlunit;
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
			 webParam = housingFundYangzhouHtmlunit.getUserInfo(messageLoginForHousing, taskHousing);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingYangzhouHtml housingYangzhouHtml = new HousingYangzhouHtml();
					housingYangzhouHtml.setPageCount(1);
					housingYangzhouHtml.setHtml(webParam.getHtml());
					housingYangzhouHtml.setType("userInfo");
					housingYangzhouHtml.setUrl(webParam.getUrl());
					housingYangzhouHtml.setTaskid(taskHousing.getTaskid());
					housingYangzhouHtmlRepository.save(housingYangzhouHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingYangzhouUserInfoRepository.save(webParam.getUserInfo());
					updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					tracer.addTag("action.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
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
			Calendar calendar = Calendar.getInstance();
			int temp=0;
			for (int i = 0; i < 3; i++) {
				String year = String.valueOf(calendar.get(Calendar.YEAR) - i);
				webParam = housingFundYangzhouHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,year);
				if (null != webParam.getHtml()) {
					HousingYangzhouHtml housingYangzhouHtml = new HousingYangzhouHtml();
					housingYangzhouHtml.setPageCount(1);
					housingYangzhouHtml.setHtml(webParam.getHtml());
					housingYangzhouHtml.setType("paydetails");
					housingYangzhouHtml.setUrl(webParam.getUrl());
					housingYangzhouHtml.setTaskid(taskHousing.getTaskid());
					housingYangzhouHtmlRepository.save(housingYangzhouHtml);
				}
				if (null != webParam.getPaydetails()) {
					housingYangzhouPaydetailsRepository.saveAll(webParam.getPaydetails());
					temp++;
					tracer.addTag("action.housing.crawler.getPaydetails year="+year, "缴存明细信息入库" + webParam.getPaydetails());					
				} else {
					tracer.addTag("action.housing.crawler.getPaydetails  year="+year, "缴存明细信息为空");
				}
		       if (webParam.getHtml().contains("下页")) {
		    	   housingFundYangzhouHtmlunit.getPaydetailsforPageTwo(messageLoginForHousing, taskHousing, year);
			    }				
			}
			if (temp > 0) {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息入库成功");
				updateTaskHousing(taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 201,
						taskHousing.getTaskid());
				tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息为空");
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