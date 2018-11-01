package app.service;

import java.util.List;

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
import com.microservice.dao.entity.crawler.housing.taian.HousingTaianHtml;
import com.microservice.dao.entity.crawler.housing.taian.HousingTaianPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.taian.HousingTaianHtmlRepository;
import com.microservice.dao.repository.crawler.housing.taian.HousingTaianUserInfoRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingTaianParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.taian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.taian")
public class HousingTaianInfoService extends HousingBasicService {

	@Autowired
	private HousingTaianParse housingTaianParse;
	@Autowired
	private HousingTaianHtmlRepository housingTaianHtmlRepository;
	@Autowired
	private HousingTaianUserInfoRepository housingTaianUserInfoRepository;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;	
	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, InfoParam infoParam) {
		tracer.addTag("HousingTaianInfoService.getUserInfo", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingTaianParse.getUserInfo(taskHousing, infoParam);
			if (null != webParam) {

				housingTaianUserInfoRepository.save(webParam.getHousingTaianUserInfo());
				tracer.addTag("HousingTaianInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingTaianInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingTaianHtml housingTaianHtml = new HousingTaianHtml(taskHousing.getTaskid(),
					"housing_Taian_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingTaianHtmlRepository.save(housingTaianHtml);

			tracer.addTag("HousingTaianInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingTaianInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
	public void getPay(MessageLoginForHousing messageLoginForHousing, InfoParam infoParam) {
		tracer.addTag("HousingTaianInfoService.getPay", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam<HousingTaianPay> webParam = housingTaianParse.getPay(messageLoginForHousing, taskHousing,
					infoParam);
			if (null != webParam) {
				List<String> datalist = webParam.getDatalist();
				for (String string : datalist) {
					housingTaianParse.getPaydata(messageLoginForHousing, taskHousing, infoParam, string);
				}
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingTaianInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingTaianInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}