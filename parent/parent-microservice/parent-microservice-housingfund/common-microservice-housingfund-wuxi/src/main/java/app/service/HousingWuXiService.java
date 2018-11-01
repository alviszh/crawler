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

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuxi.HousingWuxiHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.wuxi.HousingWuxiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.wuxi.HousingWuxiPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.wuxi.HousingWuxiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundWuXiHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuxi")
public class HousingWuXiService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingWuXiService.class);
	@Autowired
	private HousingWuxiHtmlRepository housingWuxiHtmlRepository;
	@Autowired
	private HousingWuxiPaydetailsRepository housingWuxiPaydetailsRepository;
	@Autowired
	private HousingWuxiUserInfoRepository housingWuxiUserInfoRepository;
	@Autowired
	private HousingFundWuXiHtmlunit housingFundWuXiHtmlunit;
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
			 webParam = housingFundWuXiHtmlunit.getUserInfo(messageLoginForHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingWuxiHtml housingWuxiHtml = new HousingWuxiHtml();
					housingWuxiHtml.setPageCount(1);
					housingWuxiHtml.setHtml(webParam.getHtml());
					housingWuxiHtml.setType("userInfo");
					housingWuxiHtml.setUrl(webParam.getUrl());
					housingWuxiHtml.setTaskid(taskHousing.getTaskid());
					housingWuxiHtmlRepository.save(housingWuxiHtml);		
				}				
				if (null !=webParam.getUserInfo()) {
					housingWuxiUserInfoRepository.save(webParam.getUserInfo());
					updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
					updateTaskHousing(taskHousing.getTaskid());
				}else{
					updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
					tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
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
			tracer.addTag("parser.housing.crawler.getUserInfo", "获取用户信息失败");
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
			 webParam = housingFundWuXiHtmlunit.getPaydetails( messageLoginForHousing,count);	
			 if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					HousingWuxiHtml housingWuxiHtml = new HousingWuxiHtml();
					housingWuxiHtml.setPageCount(1);
					housingWuxiHtml.setHtml(webParam.getHtml());
					housingWuxiHtml.setType("paydetails");
					housingWuxiHtml.setUrl(webParam.getUrl());
					housingWuxiHtml.setTaskid(taskHousing.getTaskid());
					housingWuxiHtmlRepository.save(housingWuxiHtml);			
				}				
				if (null !=webParam.getPaydetails()) {
					housingWuxiPaydetailsRepository.saveAll(webParam.getPaydetails());
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
			tracer.addTag("parser.housing.crawler.getPaydetails", "获取缴存明细信息失败");
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
}