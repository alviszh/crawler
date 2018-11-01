package app.service;

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
import com.microservice.dao.entity.crawler.housing.xining.HousingXiningHtml;
import com.microservice.dao.entity.crawler.housing.xining.HousingXiningPaydetails;
import com.microservice.dao.repository.crawler.housing.xining.HousingXiningHtmlRepository;
import com.microservice.dao.repository.crawler.housing.xining.HousingXiningPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.xining.HousingXiningUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingXiNingParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xining")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xining")
public class HousingXiNingInfoService extends HousingBasicService {

	@Autowired
	private HousingXiNingParse housingXiNingParse;
	@Autowired
	private HousingXiningHtmlRepository housingXiningHtmlRepository;
	@Autowired
	private HousingXiningUserInfoRepository housingXiningUserInfoRepository;	
	@Autowired
	private HousingXiningPaydetailsRepository housingXiningPaydetailsRepository;	
	@Autowired
	private TracerLog tracer;
	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingXiningInfoService.getUserInfo", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingXiNingParse.getUserInfo(taskHousing);
			if (null != webParam) {
				housingXiningUserInfoRepository.save(webParam.getHousingXiningUserInfo());				
				tracer.addTag("HousingXiningInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());				
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());				
				HousingXiningHtml housingXiningHtml = new HousingXiningHtml(taskHousing.getTaskid(),
						"housing_Xining_user", "1", webParam.getUrl(), webParam.getHtml());
				housingXiningHtmlRepository.save(housingXiningHtml);
				tracer.addTag("HousingXiningInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingXiningInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingXiningInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	/**
	 * 缴费信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getPay(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingXiningInfoService.getPay", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam<HousingXiningPaydetails> webParam = housingXiNingParse.getPay(taskHousing);

			if (null != webParam) {
				
				if (webParam.getList() !=null && !webParam.getList().isEmpty()) {
					housingXiningPaydetailsRepository.saveAll(webParam.getList());							
					tracer.addTag("HousingXiningInfoService.getPay---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
							taskHousing.getTaskid());					
				}else{
					tracer.addTag("HousingXiningInfoService.getPay---缴费信息", "缴费信息为空!" + taskHousing.getTaskid());
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 201,
							taskHousing.getTaskid());	
				}	
				HousingXiningHtml housingXiningHtml = new HousingXiningHtml(taskHousing.getTaskid(),
						"housing_Xining_pay", "1", webParam.getUrl(), webParam.getHtml());
				housingXiningHtmlRepository.save(housingXiningHtml);

				tracer.addTag("HousingXiningInfoService.getPay---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingXiningInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingXiningInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
}