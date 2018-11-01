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
import com.microservice.dao.entity.crawler.housing.yanan.HousingYananHtml;
import com.microservice.dao.entity.crawler.housing.yanan.HousingYananPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.yanan.HousingYananHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yanan.HousingYananUserInfoRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingYananParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.Yanan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.Yanan")
public class HousingYananInfoService extends HousingBasicService {

	@Autowired
	private HousingYananParse housingYananParse;
	@Autowired
	private HousingYananHtmlRepository housingYananHtmlRepository;
	@Autowired
	private HousingYananUserInfoRepository housingYananUserInfoRepository;	
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
		tracer.addTag("housingYananInfoService.getUserInfo", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingYananParse.getUserInfo(taskHousing, infoParam);
			if (null != webParam) {
				housingYananUserInfoRepository.save(webParam.getHousingYananUserInfo());
				tracer.addTag("housingYananInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("housingYananInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingYananHtml housingYananHtml = new HousingYananHtml(taskHousing.getTaskid(),
					"housing_Yanan_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingYananHtmlRepository.save(housingYananHtml);
			tracer.addTag("housingYananInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("housingYananInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
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
		tracer.addTag("housingYananInfoService.getPay", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam<HousingYananPay> webParam = housingYananParse.getPay(messageLoginForHousing, taskHousing,
					infoParam);
			if (null != webParam) {
				List<String> datalist = webParam.getDatalist();
				for (String string : datalist) {
					housingYananParse.getPaydata(messageLoginForHousing, taskHousing, infoParam, string);
				}
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("housingYananInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("housingYananInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}