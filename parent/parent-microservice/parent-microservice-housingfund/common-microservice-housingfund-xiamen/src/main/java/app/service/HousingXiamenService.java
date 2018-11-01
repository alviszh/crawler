package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xiamen.HousingXiamenHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.xiamen.HousingXiamenHtmlRepository;
import com.microservice.dao.repository.crawler.housing.xiamen.HousingXiamenPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.xiamen.HousingXiamenUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundXiamenHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xiamen")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xiamen")
public class HousingXiamenService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingXiamenService.class);
	@Autowired
	private HousingXiamenHtmlRepository housingXiamenHtmlRepository;
	@Autowired
	private HousingXiamenPaydetailsRepository housingXiamenPaydetailsRepository;
	@Autowired
	private HousingXiamenUserInfoRepository housingXiamenUserInfoRepository;
	@Autowired
	private HousingFundXiamenHtmlunit housingFundXiamenHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingXiamenService.getUserInfo.taskid", messageLoginForHousing.getTask_id());
		TaskHousing	taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundXiamenHtmlunit.getUserInfo(messageLoginForHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingXiamenHtml housingXiamenHtml = new HousingXiamenHtml();
					housingXiamenHtml.setPageCount(1);
					housingXiamenHtml.setHtml(webParam.getHtml());
					housingXiamenHtml.setType("userInfo");
					housingXiamenHtml.setUrl(webParam.getUrl());
					housingXiamenHtml.setTaskid(taskHousing.getTaskid());
					housingXiamenHtmlRepository.save(housingXiamenHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingXiamenUserInfoRepository.save(webParam.getUserInfo());					
					updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());					
					updateTaskHousing(taskHousing.getTaskid());
				}else{
					updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
					updateTaskHousing(taskHousing.getTaskid());
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
			tracer.addTag("parser.housing.crawler.getUserInfo.Exception", e.toString());
			updateTaskHousing(taskHousing.getTaskid());
		}
	}
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing,String accountnum){		
		tracer.addTag("parser.HousingXiamenService.getPaydetails.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {			
			int count=0;
			 webParam = housingFundXiamenHtmlunit.getPaydetails(messageLoginForHousing,accountnum,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingXiamenHtml housingXiamenHtml = new HousingXiamenHtml();
					housingXiamenHtml.setPageCount(1);
					housingXiamenHtml.setHtml(webParam.getHtml());
					housingXiamenHtml.setType("paydetails");
					housingXiamenHtml.setUrl(webParam.getUrl());
					housingXiamenHtml.setTaskid(taskHousing.getTaskid());
					housingXiamenHtmlRepository.save(housingXiamenHtml);				
				}				
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingXiamenPaydetailsRepository.saveAll(webParam.getPaydetails());
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