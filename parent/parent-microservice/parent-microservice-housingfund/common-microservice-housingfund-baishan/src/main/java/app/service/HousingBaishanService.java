package app.service;

import java.util.List;

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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanHtml;
import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanPaydetails;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.baishan.HousingBaishanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.baishan.HousingBaishanPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.baishan.HousingBaishanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.InfoParam;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundBaishanHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.baishan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.baishan")
public class HousingBaishanService extends HousingBasicService {


	@Autowired
	private HousingBaishanHtmlRepository housingBaishanHtmlRepository;

	@Autowired
	private HousingBaishanUserInfoRepository housingBaishanUserInfoRepository;
	@Autowired
	private HousingBaishanPaydetailsRepository housingBaishanPaydetailsRepository;
	@Autowired
	private HousingFundBaishanHtmlunit housingFundBaishanHtmlunit;
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
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, InfoParam infoParam) {
		tracer.addTag("HousingBaishanInfoService.getUserInfo", taskHousing.getTaskid());
		try {
			WebParam webParam = housingFundBaishanHtmlunit.getUserInfo(messageLoginForHousing,taskHousing, infoParam);
			if (null != webParam) {
				housingBaishanUserInfoRepository.save(webParam.getUserInfo());
				tracer.addTag("HousingBaishanInfoService.getUserInfo---用户信息", "用户信息已入库!" + webParam.getUserInfo());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 201,
						taskHousing.getTaskid());
				tracer.addTag("HousingBaishanInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingBaishanHtml housingBaishanHtml = new HousingBaishanHtml(taskHousing.getTaskid(),
					"housing_Baishan_userinfo", 1, webParam.getUrl(), webParam.getHtml());
			housingBaishanHtmlRepository.save(housingBaishanHtml);
			tracer.addTag("HousingBaishanService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingBaishanService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
	public void getPay(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, InfoParam infoParam) {
		tracer.addTag("HousingBaishanService.getPay", taskHousing.getTaskid());
		try {
			WebParam webParam = housingFundBaishanHtmlunit.getPay(messageLoginForHousing, taskHousing,
					infoParam);
			if (null != webParam) {
				List<HousingBaishanPaydetails> paydetails = webParam.getPaydetails();
				housingBaishanPaydetailsRepository.saveAll(paydetails);
				tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息入库"+paydetails);
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(),
						200 , taskHousing.getTaskid());		
			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 201,
						taskHousing.getTaskid());
				tracer.addTag("HousingBaishanService.getPay.webParam is null", taskHousing.getTaskid());
			}
			HousingBaishanHtml housingBaishanHtml = new HousingBaishanHtml(taskHousing.getTaskid(),
					"housing_Baishan_payinfo", 1, webParam.getUrl(), webParam.getHtml());
			housingBaishanHtmlRepository.save(housingBaishanHtml);
			tracer.addTag("HousingBaishanService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingBaishanService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}