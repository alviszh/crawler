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
import com.microservice.dao.entity.crawler.housing.bayanzhuoer.HousingBayanzhuoerHtml;
import com.microservice.dao.repository.crawler.housing.bayanzhuoer.HousingBayanzhuoerHtmlRepository;
import com.microservice.dao.repository.crawler.housing.bayanzhuoer.HousingBayanzhuoerUserInfoRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingBayanzhuoerParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.bayanzhuoer")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.bayanzhuoer")
public class HousingBayanzhuoerInfoService extends HousingBasicService {

	@Autowired
	private HousingBayanzhuoerParse housingBayanzhuoerParse;

	@Autowired
	private HousingBayanzhuoerHtmlRepository housingBayanzhuoerHtmlRepository;

	@Autowired
	private HousingBayanzhuoerUserInfoRepository housingBayanzhuoerUserInfoRepository;
	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, InfoParam infoParam,String loginType) {
		tracer.addTag("HousingBayanzhuoerInfoService.getUserInfo", taskHousing.getTaskid());
		try {
			WebParam webParam = housingBayanzhuoerParse.getUserInfo(messageLoginForHousing,taskHousing, infoParam,loginType);
			if (null != webParam) {
				housingBayanzhuoerUserInfoRepository.save(webParam.getHousingBayanzhuoerUserInfo());
				tracer.addTag("HousingBayanzhuoerInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingBayanzhuoerInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingBayanzhuoerHtml housingBayanzhuoerHtml = new HousingBayanzhuoerHtml(taskHousing.getTaskid(),
					"housing_bayanzhuoer_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingBayanzhuoerHtmlRepository.save(housingBayanzhuoerHtml);

			tracer.addTag("HousingBayanzhuoerInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingBayanzhuoerInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}