package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.ganzhou.HousingGanZhouHtml;
import com.microservice.dao.entity.crawler.housing.ganzhou.HousingGanZhouPaydetails;
import com.microservice.dao.repository.crawler.housing.ganzhou.HousingGanZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.ganzhou.HousingGanZhouUserInfoRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingGanZhouParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.ganzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.ganzhou")
public class HousingGanZhouInfoService extends HousingBasicService {

	@Autowired
	private HousingGanZhouParse housingGanZhouParse;

	@Autowired
	private HousingGanZhouHtmlRepository housingGanZhouHtmlRepository;

	@Autowired
	private HousingGanZhouUserInfoRepository housingGanZhouUserInfoRepository;

	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(TaskHousing taskHousing, InfoParam infoParam) {
		tracer.addTag("HousingGanZhouInfoService.getUserInfo", taskHousing.getTaskid());
		tracer.addTag("HousingGanZhouInfoService.getUserInfo.infoParam", infoParam.toString());
		try {
			WebParam webParam = housingGanZhouParse.getUserInfo(taskHousing, infoParam);
			if (null != webParam) {
				housingGanZhouUserInfoRepository.save(webParam.getHousingGanZhouUserInfo());
				tracer.addTag("HousingGanZhouInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingGanZhouInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingGanZhouHtml housingGanZhouHtml = new HousingGanZhouHtml(taskHousing.getTaskid(),
					"housing_ganzhou_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingGanZhouHtmlRepository.save(housingGanZhouHtml);
			tracer.addTag("HousingGanZhouInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingGanZhouInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
	public void getPay(TaskHousing taskHousing, InfoParam infoParam) {
		tracer.addTag("HousingGanZhouInfoService.getPay", taskHousing.getTaskid());
		tracer.addTag("HousingGanZhouInfoService.getPay.infoParam", infoParam.toString());
		try {
			WebParam<HousingGanZhouPaydetails> webParam = housingGanZhouParse.getPay(taskHousing,
					infoParam);
			if (null != webParam) {
				List<String> datalist = webParam.getDatalist();
				for (String string : datalist) {
					housingGanZhouParse.getPaydata(taskHousing, infoParam, string);
				}
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingGanZhouInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingGanZhouInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}