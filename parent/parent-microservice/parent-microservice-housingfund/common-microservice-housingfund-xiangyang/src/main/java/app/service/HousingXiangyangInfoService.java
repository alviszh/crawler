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
import com.microservice.dao.entity.crawler.housing.xiangyang.HousingXiangyangHtml;
import com.microservice.dao.entity.crawler.housing.xiangyang.HousingXiangyangPay;
import com.microservice.dao.entity.crawler.housing.xiangyang.HousingXiangyangUserInfo;
import com.microservice.dao.repository.crawler.housing.xiangyang.HousingXiangyangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.xiangyang.HousingXiangyangPayRepository;
import com.microservice.dao.repository.crawler.housing.xiangyang.HousingXiangyangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingXiangyangParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xiangyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xiangyang")
public class HousingXiangyangInfoService extends HousingBasicService {

	@Autowired
	private HousingXiangyangParse housingXiangyangParse;

	@Autowired
	private HousingXiangyangHtmlRepository housingXiangyangHtmlRepository;

	@Autowired
	private HousingXiangyangUserInfoRepository housingXiangyangUserInfoRepository;
	
	@Autowired
	private HousingXiangyangPayRepository housingXiangyangPayRepository;
	
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
		tracer.addTag("HousingXiangyangInfoService.getUserInfo", taskHousing.getTaskid());

		try {
			WebParam webParam = housingXiangyangParse.getUserInfo(taskHousing);

			if (null != webParam) {

				housingXiangyangUserInfoRepository.save(webParam.getHousingXiangyangUserInfo());
				tracer.addTag("HousingXiangyangInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingXiangyangInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingXiangyangHtml housingXiangyangHtml = new HousingXiangyangHtml(taskHousing.getTaskid(),
					"housing_Xiangyang_userinfo", "1", webParam.getUrl(), webParam.getHtml());
			housingXiangyangHtmlRepository.save(housingXiangyangHtml);

			tracer.addTag("HousingXiangyangInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingXiangyangInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
//		updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 201,
//				taskHousing.getTaskid());
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
	public void getPay(TaskHousing taskHousing,HousingXiangyangUserInfo housingXiangyangUserInfo) {
		tracer.addTag("HousingXiangyangInfoService.getPay", taskHousing.getTaskid());
		try {
			WebParam<HousingXiangyangPay> webParam = housingXiangyangParse.getPay(taskHousing,housingXiangyangUserInfo);

			if (null != webParam) {

				housingXiangyangPayRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingXiangyangInfoService.getPay---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
				
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				
				HousingXiangyangHtml housingXiangyangHtml = new HousingXiangyangHtml(taskHousing.getTaskid(),
						"housing_xiangyang_pay", "1", webParam.getUrl(), webParam.getHtml());
				housingXiangyangHtmlRepository.save(housingXiangyangHtml);

				tracer.addTag("HousingXiangyangInfoService.getPay---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingXiangyangInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingXiangyangInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
}