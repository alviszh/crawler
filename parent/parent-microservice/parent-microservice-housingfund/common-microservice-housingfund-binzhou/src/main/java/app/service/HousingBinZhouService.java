package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.binzhou.HousingBinZhouHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.binzhou.HousingBinZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.binzhou.HousingBinZhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundBinZhouHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.binzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.binzhou")
public class HousingBinZhouService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingBinZhouService.class);
	@Autowired
	private HousingBinZhouHtmlRepository housingBinZhouHtmlRepository;
	@Autowired
	private HousingBinZhouUserInfoRepository housingBinZhouUserInfoRepository;
	@Autowired 
	private HousingFundBinZhouHtmlunit housingFundBinZhouHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;

	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("获取用户信息", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			 webParam = housingFundBinZhouHtmlunit.getUserInfo(messageLoginForHousing, taskHousing);	
			if (null !=webParam.getHtml()) {
				HousingBinZhouHtml housingBinZhouHtml = new HousingBinZhouHtml();
				housingBinZhouHtml.setPageCount(1);
				housingBinZhouHtml.setHtml(webParam.getHtml());
				housingBinZhouHtml.setType("userInfo");
				housingBinZhouHtml.setUrl(webParam.getUrl());
				housingBinZhouHtml.setTaskid(taskHousing.getTaskid());
				housingBinZhouHtmlRepository.save(housingBinZhouHtml);	
			}				
			if (null != webParam.getUserInfo()) {
				housingBinZhouUserInfoRepository.save(webParam.getUserInfo());			
				tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库" + webParam.getUserInfo());
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());	
			} else {
				tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");			
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 201,
						taskHousing.getTaskid());
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
}