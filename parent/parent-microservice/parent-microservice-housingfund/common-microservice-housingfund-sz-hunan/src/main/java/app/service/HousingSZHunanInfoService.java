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
import com.microservice.dao.entity.crawler.housing.sz.hunan.HousingSZHunanHtml;
import com.microservice.dao.entity.crawler.housing.sz.hunan.HousingSZHunanPay;
import com.microservice.dao.repository.crawler.housing.sz.hunan.HousingSZHunanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.sz.hunan.HousingSZHunanUserInfoRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingSZHunanParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.sz.hunan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.sz.hunan")
public class HousingSZHunanInfoService extends HousingBasicService {

	@Autowired
	private HousingSZHunanParse housingSZHunanParse;

	@Autowired
	private HousingSZHunanHtmlRepository housingSZHunanHtmlRepository;

	@Autowired
	private HousingSZHunanUserInfoRepository housingSZHunanUserInfoRepository;


	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(TaskHousing taskHousing, InfoParam infoParam) {
		tracer.addTag("HousingSZHunanInfoService.getUserInfo", taskHousing.getTaskid());

		try {

			WebParam webParam = housingSZHunanParse.getUserInfo(taskHousing, infoParam);

			if (null != webParam) {

				housingSZHunanUserInfoRepository.save(webParam.getHousingSZHunanUserInfo());
				tracer.addTag("HousingSZHunanInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingSZHunanInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingSZHunanHtml housingSZHunanHtml = new HousingSZHunanHtml(taskHousing.getTaskid(),
					"housing_szhunan_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingSZHunanHtmlRepository.save(housingSZHunanHtml);

			tracer.addTag("HousingSZHunanInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingSZHunanInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
		tracer.addTag("HousingSZHunanInfoService.getPay", taskHousing.getTaskid());
		try {
			WebParam<HousingSZHunanPay> webParam = housingSZHunanParse.getPay(messageLoginForHousing, taskHousing,
					infoParam);

			if (null != webParam) {
				List<String> datalist = webParam.getDatalist();
				for (String string : datalist) {
					housingSZHunanParse.getPaydata(messageLoginForHousing, taskHousing, infoParam, string);
				}
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingSZHunanInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingSZHunanInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}