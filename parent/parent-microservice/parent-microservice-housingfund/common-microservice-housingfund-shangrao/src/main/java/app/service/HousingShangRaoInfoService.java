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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shangrao.HousingShangRaoHtml;
import com.microservice.dao.entity.crawler.housing.shangrao.HousingShangRaoPaydetails;
import com.microservice.dao.repository.crawler.housing.shangrao.HousingShangRaoHtmlRepository;
import com.microservice.dao.repository.crawler.housing.shangrao.HousingShangRaoUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparser.HousingShangRaoParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shangrao")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shangrao")
public class HousingShangRaoInfoService extends HousingBasicService {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingShangRaoParse housingShangRaoParse;
	@Autowired
	private HousingShangRaoHtmlRepository housingShangRaoHtmlRepository;
	@Autowired
	private HousingShangRaoUserInfoRepository housingShangRaoUserInfoRepository;
	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, InfoParam infoParam) {
		tracer.addTag("HousingShangRaoInfoService.getUserInfo", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingShangRaoParse.getUserInfo(taskHousing, infoParam);
			if (null != webParam) {
				housingShangRaoUserInfoRepository.save(webParam.getHousingShangRaoUserInfo());
				tracer.addTag("HousingShangRaoInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingShangRaoInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingShangRaoHtml housingShangRaoHtml = new HousingShangRaoHtml(taskHousing.getTaskid(),
					"housing_ShangRao_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingShangRaoHtmlRepository.save(housingShangRaoHtml);
			tracer.addTag("HousingShangRaoInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingShangRaoInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
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
		tracer.addTag("HousingShangRaoInfoService.getPay", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam<HousingShangRaoPaydetails> webParam = housingShangRaoParse.getPay(taskHousing,infoParam);
			if (null != webParam) {
				List<String> datalist = webParam.getDatalist();
				for (String string : datalist) {
					housingShangRaoParse.getPaydata(webParam.getWebClient(), taskHousing, infoParam, string);
				}
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingShangRaoInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingShangRaoInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}