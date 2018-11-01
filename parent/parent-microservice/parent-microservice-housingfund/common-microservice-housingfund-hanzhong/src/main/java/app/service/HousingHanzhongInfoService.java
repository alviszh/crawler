package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.hanzhong.HousingHanzhongHtml;
import com.microservice.dao.entity.crawler.housing.hanzhong.HousingHanzhongPay;
import com.microservice.dao.entity.crawler.housing.hanzhong.HousingHanzhongUserInfo;
import com.microservice.dao.repository.crawler.housing.hanzhong.HousingHanzhongHtmlRepository;
import com.microservice.dao.repository.crawler.housing.hanzhong.HousingHanzhongPayRepository;
import com.microservice.dao.repository.crawler.housing.hanzhong.HousingHanzhongUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingHanzhongParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.hanzhong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.hanzhong")
public class HousingHanzhongInfoService extends HousingBasicService {

	@Autowired
	private HousingHanzhongParse housingHanzhongParse;

	@Autowired
	private HousingHanzhongHtmlRepository housingHanzhongHtmlRepository;

	@Autowired
	private HousingHanzhongUserInfoRepository housingHanzhongUserInfoRepository;
	
	@Autowired
	private HousingHanzhongPayRepository housingHanzhongPayRepository;
	
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
	public void getUserInfo(TaskHousing taskHousing) {
		tracer.addTag("HousingHanzhongInfoService.getUserInfo", taskHousing.getTaskid());

		try {
			WebParam<HousingHanzhongUserInfo> webParam = housingHanzhongParse.getUserInfo(taskHousing);

			if (null != webParam) {
				housingHanzhongUserInfoRepository.saveAll(webParam.getList());
				tracer.addTag("HousingHanzhongInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingHanzhongInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingHanzhongHtml housingHanzhongHtml = new HousingHanzhongHtml(taskHousing.getTaskid(),
					"housing_Hanzhong_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingHanzhongHtmlRepository.save(housingHanzhongHtml);

			tracer.addTag("HousingHanzhongInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingHanzhongInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
	public void getPay(TaskHousing taskHousing,HousingHanzhongUserInfo housingHanzhongUserInfo) {
		tracer.addTag("HousingHanzhongInfoService.getPay", taskHousing.getTaskid());
		try {
			WebParam<HousingHanzhongPay> webParam = housingHanzhongParse.getPay(taskHousing,housingHanzhongUserInfo);

			if (null != webParam) {

				housingHanzhongPayRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingHanzhongInfoService.getPay---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
				
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				
				HousingHanzhongHtml housingHanzhongHtml = new HousingHanzhongHtml(taskHousing.getTaskid(),
						"housing_Hanzhong_pay", "1", webParam.getUrl(), webParam.getHtml());
				housingHanzhongHtmlRepository.save(housingHanzhongHtml);

				tracer.addTag("HousingHanzhongInfoService.getPay---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingHanzhongInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingHanzhongInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
}