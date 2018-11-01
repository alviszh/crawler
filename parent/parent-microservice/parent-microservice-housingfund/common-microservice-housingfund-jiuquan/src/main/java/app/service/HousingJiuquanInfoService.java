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
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanHtml;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanMsg;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanPay;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanUserInfo;
import com.microservice.dao.repository.crawler.housing.jiuquan.HousingJiuquanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jiuquan.HousingJiuquanMsgRepository;
import com.microservice.dao.repository.crawler.housing.jiuquan.HousingJiuquanPayRepository;
import com.microservice.dao.repository.crawler.housing.jiuquan.HousingJiuquanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingJiuquanParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jiuquan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jiuquan")
public class HousingJiuquanInfoService extends HousingBasicService {

	@Autowired
	private HousingJiuquanParse housingJiuquanParse;

	@Autowired
	private HousingJiuquanHtmlRepository housingJiuquanHtmlRepository;

	@Autowired
	private HousingJiuquanUserInfoRepository housingJiuquanUserInfoRepository;
	
	@Autowired
	private HousingJiuquanMsgRepository housingJiuquanMsgRepository;
	
	@Autowired
	private HousingJiuquanPayRepository housingJiuquanPayRepository;

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
		tracer.addTag("HousingJiuquanInfoService.getUserInfo", taskHousing.getTaskid());

		try {
			WebParam webParam = housingJiuquanParse.getUserInfo(taskHousing);

			if (null != webParam) {

				housingJiuquanUserInfoRepository.save(webParam.getHousingJiuquanUserInfo());
				tracer.addTag("HousingJiuquanInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingJiuquanInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingJiuquanHtml housingJiuquanHtml = new HousingJiuquanHtml(taskHousing.getTaskid(),
					"housing_jiuquan_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingJiuquanHtmlRepository.save(housingJiuquanHtml);

			tracer.addTag("HousingJiuquanInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingJiuquanInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
	public void getPay(TaskHousing taskHousing,HousingJiuquanUserInfo housingJiuquanUserInfo) {
		tracer.addTag("HousingJiuquanInfoService.getPay", taskHousing.getTaskid());
		try {
			WebParam<HousingJiuquanPay> webParam = housingJiuquanParse.getPay(taskHousing,housingJiuquanUserInfo);

			if (null != webParam) {

				housingJiuquanPayRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingJiuquanInfoService.getPay---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
				
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				
				HousingJiuquanHtml housingJiuquanHtml = new HousingJiuquanHtml(taskHousing.getTaskid(),
						"housing_jiuquan_pay", "1", webParam.getUrl(), webParam.getHtml());
				housingJiuquanHtmlRepository.save(housingJiuquanHtml);

				tracer.addTag("HousingJiuquanInfoService.getPay---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingJiuquanInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingJiuquanInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	
	
	/**
	 * 缴费信息-----消息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getMsg(TaskHousing taskHousing) {
		tracer.addTag("HousingJiuquanInfoService.getMsg", taskHousing.getTaskid());
		try {
			WebParam<HousingJiuquanMsg> webParam = housingJiuquanParse.getMsg(taskHousing);

			if (null != webParam) {

				housingJiuquanMsgRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingJiuquanInfoService.getMsg---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
				
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				HousingJiuquanHtml housingJiuquanHtml = new HousingJiuquanHtml(taskHousing.getTaskid(),
						"housing_Jiuquan_msg", "1", webParam.getUrl(), webParam.getHtml());
				housingJiuquanHtmlRepository.save(housingJiuquanHtml);

				tracer.addTag("HousingJiuquanInfoService.getMsg---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingJiuquanInfoService.getMsg.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingJiuquanInfoService.getMsg---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	
	
	
	
	
	

}