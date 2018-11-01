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
import com.microservice.dao.entity.crawler.housing.taiyuan.HousingTaiyuanHtml;
import com.microservice.dao.entity.crawler.housing.taiyuan.HousingTaiyuanMsg;
import com.microservice.dao.repository.crawler.housing.taiyuan.HousingTaiyuanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.taiyuan.HousingTaiyuanMsgRepository;
import com.microservice.dao.repository.crawler.housing.taiyuan.HousingTaiyuanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingTaiyuanParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.taiyuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.taiyuan")
public class HousingTaiyuanInfoService extends HousingBasicService {

	@Autowired
	private HousingTaiyuanParse housingTaiyuanParse;

	@Autowired
	private HousingTaiyuanHtmlRepository housingTaiyuanHtmlRepository;

	@Autowired
	private HousingTaiyuanUserInfoRepository housingTaiyuanUserInfoRepository;
	
	@Autowired
	private HousingTaiyuanMsgRepository housingTaiyuanMsgRepository;

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
		tracer.addTag("HousingTaiyuanInfoService.getUserInfo", taskHousing.getTaskid());

		try {
			WebParam webParam = housingTaiyuanParse.getUserInfo(taskHousing);

			if (null != webParam) {

				housingTaiyuanUserInfoRepository.save(webParam.getHousingTaiyuanUserInfo());
				tracer.addTag("HousingTaiyuanInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingTaiyuanInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingTaiyuanHtml housingTaiyuanHtml = new HousingTaiyuanHtml(taskHousing.getTaskid(),
					"housing_taiyuan_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingTaiyuanHtmlRepository.save(housingTaiyuanHtml);

			tracer.addTag("HousingTaiyuanInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingTaiyuanInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
	public void getMsg(TaskHousing taskHousing) {
		tracer.addTag("HousingTaiyuanInfoService.getMsg", taskHousing.getTaskid());
		try {
			WebParam<HousingTaiyuanMsg> webParam = housingTaiyuanParse.getMsg(taskHousing);

			if (null != webParam) {

				housingTaiyuanMsgRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingTaiyuanInfoService.getMsg---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
				
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				HousingTaiyuanHtml housingTaiyuanHtml = new HousingTaiyuanHtml(taskHousing.getTaskid(),
						"housing_taiyuan_msg", "1", webParam.getUrl(), webParam.getHtml());
				housingTaiyuanHtmlRepository.save(housingTaiyuanHtml);

				tracer.addTag("HousingTaiyuanInfoService.getMsg---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingTaiyuanInfoService.getMsg.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingTaiyuanInfoService.getMsg---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

}