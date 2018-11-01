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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.rizhao.HousingRizhaoHtml;
import com.microservice.dao.entity.crawler.housing.rizhao.HousingRizhaoPay;
import com.microservice.dao.repository.crawler.housing.rizhao.HousingRizhaoHtmlRepository;
import com.microservice.dao.repository.crawler.housing.rizhao.HousingRizhaoUserInfoRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingRizhaoParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.rizhao")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.rizhao")
public class HousingRizhaoInfoService extends HousingBasicService {

	@Autowired
	private HousingRizhaoParse housingRizhaoParse;

	@Autowired
	private HousingRizhaoHtmlRepository housingRizhaoHtmlRepository;

	@Autowired
	private HousingRizhaoUserInfoRepository housingRizhaoUserInfoRepository;

	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, InfoParam infoParam) {
		tracer.addTag("housingRizhaoInfoService.getUserInfo", messageLoginForHousing.getTask_id());
		tracer.addTag("housingRizhaoInfoService.getUserInfo.infoParam",infoParam.toString());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingRizhaoParse.getUserInfo(messageLoginForHousing, infoParam);
			if (null != webParam) {
				housingRizhaoUserInfoRepository.save(webParam.getHousingRizhaoUserInfo());
				tracer.addTag("housingRizhaoInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("housingRizhaoInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingRizhaoHtml housingRizhaoHtml = new HousingRizhaoHtml(taskHousing.getTaskid(),
					"housing_Rizhao_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingRizhaoHtmlRepository.save(housingRizhaoHtml);

			tracer.addTag("housingRizhaoInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("housingRizhaoInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
		tracer.addTag("housingRizhaoInfoService.getPay", messageLoginForHousing.getTask_id());
		tracer.addTag("housingRizhaoInfoService.getUserInfo.infoParam",infoParam.toString());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam<HousingRizhaoPay> webParam = housingRizhaoParse.getPay(messageLoginForHousing,
					infoParam);
			if (null != webParam) {
				List<String> datalist = webParam.getDatalist();
				for (String string : datalist) {
					housingRizhaoParse.getPaydata(messageLoginForHousing, taskHousing, infoParam, string);
				}
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("housingRizhaoInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("housingRizhaoInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}