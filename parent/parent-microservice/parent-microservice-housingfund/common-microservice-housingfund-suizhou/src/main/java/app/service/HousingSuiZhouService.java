package app.service;

import java.util.List;

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
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouHtml;
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.suizhou.HousingSuiZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.suizhou.HousingSuiZhouPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.suizhou.HousingSuiZhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.HousingFundSuiZhouHtmlunit;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.suizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.suizhou")
public class HousingSuiZhouService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingSuiZhouService.class);
	@Autowired
	private HousingSuiZhouHtmlRepository housingSuiZhouHtmlRepository;
	@Autowired
	private HousingSuiZhouUserInfoRepository housingSuiZhouUserInfoRepository;
	@Autowired
	private HousingSuiZhouPaydetailsRepository housingSuiZhouPaydetailsRepository;
	@Autowired
	private HousingFundSuiZhouHtmlunit housingFundSuiZhouHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
    @Async
	public void getPaydetails( MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing, List<String> datalist) {
		tracer.addTag("parser.HousingSuiZhouService.getPaydetails.taskid", taskHousing.getTaskid());
		taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			int tempPay = 0;
			int tempUser=0;
			for (String dataText : datalist) {
			 WebParam webParam = housingFundSuiZhouHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,dataText);
				if (null != webParam.getHtml()) {
					HousingSuiZhouHtml housingSuiZhouHtml = new HousingSuiZhouHtml();
					housingSuiZhouHtml.setPageCount(1);
					housingSuiZhouHtml.setHtml(webParam.getHtml());
					housingSuiZhouHtml.setType("paydetails");
					housingSuiZhouHtml.setUrl(webParam.getUrl());
					housingSuiZhouHtml.setTaskid(taskHousing.getTaskid());
					housingSuiZhouHtmlRepository.save(housingSuiZhouHtml);
				}										
				if (null !=webParam.getUserInfo()) {
					tempUser++;
					HousingSuiZhouUserInfo userInfo=housingSuiZhouUserInfoRepository.findTopByTaskid(taskHousing.getTaskid());
					if (userInfo==null) {					
						housingSuiZhouUserInfoRepository.save(webParam.getUserInfo());	
					}								
					tracer.addTag("parser.housing.crawler.getUserInfo"+dataText, "用户信息入库"+webParam.getUserInfo());					
				}else{
					tracer.addTag("parser.housing.crawler.getUserInfo"+dataText, "用户信息为空");					
				}					
				if (null != webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingSuiZhouPaydetailsRepository.saveAll(webParam.getPaydetails());
					tempPay++;
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库" + webParam.getPaydetails());
				} else {
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");

				}
			}
			if (tempUser>0) {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						200 , taskHousing.getTaskid());
			}else{
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						201 , taskHousing.getTaskid());
			}		
			if (tempPay > 0) {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
						200 , taskHousing.getTaskid());
			} else {
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