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
import com.microservice.dao.entity.crawler.housing.baoji.HousingBaojiHtml;
import com.microservice.dao.entity.crawler.housing.baoji.HousingBaojiPay;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.baoji.HousingBaojiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.baoji.HousingBaojiUserInfoRepository;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingBaojiParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.baoji")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.baoji")
public class HousingBaojiInfoService extends HousingBasicService {

	@Autowired
	private HousingBaojiParse housingBaojiParse;

	@Autowired
	private HousingBaojiHtmlRepository housingBaojiHtmlRepository;

	@Autowired
	private HousingBaojiUserInfoRepository housingBaojiUserInfoRepository;
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
		tracer.addTag("housingBaojiInfoService.getUserInfo", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingBaojiParse.getUserInfo(taskHousing, infoParam);
			if (null != webParam) {
				housingBaojiUserInfoRepository.save(webParam.getHousingBaojiUserInfo());
				tracer.addTag("housingBaojiInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("housingBaojiInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingBaojiHtml housingBaojiHtml = new HousingBaojiHtml(taskHousing.getTaskid(),
					"housing_Baoji_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingBaojiHtmlRepository.save(housingBaojiHtml);
			tracer.addTag("housingBaojiInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("housingBaojiInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
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
		tracer.addTag("housingBaojiInfoService.getPay", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebParam<HousingBaojiPay> webParam = housingBaojiParse.getPay(messageLoginForHousing, taskHousing,
					infoParam);
			if (null != webParam) {
				List<String> datalist = webParam.getDatalist();
				for (String string : datalist) {
					housingBaojiParse.getPaydata(messageLoginForHousing, taskHousing, infoParam, string);
				}
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("housingBaojiInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("housingBaojiInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}