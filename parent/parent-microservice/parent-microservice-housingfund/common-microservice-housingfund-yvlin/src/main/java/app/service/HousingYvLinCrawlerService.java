package app.service;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.bean.WebParamHousing;
import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
public class HousingYvLinCrawlerService extends HousingBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(HousingYvLinCrawlerService.class);

	@Autowired
	private LoginAngGetService loginAngGetService;

	@Autowired
	private HousingYvLinFutureService housingYvLinFutureService;
	
	@Autowired
	private TracerLog tracerLog;

	private WebParamHousing<?> webParamHousing = null;
	
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		tracerLog.output("messageLoginForHousing", messageLoginForHousing.toString());
		webParamHousing = null;
		try {
			webParamHousing = loginAngGetService.loginChrome(messageLoginForHousing);

			if (webParamHousing.getErrormessage() != null) {
				taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
				taskHousing.setDescription(webParamHousing.getErrormessage());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setError_message(webParamHousing.getErrormessage());
				save(taskHousing);
				return taskHousing;
			} else {
				taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(webParamHousing.getErrormessage());

				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
				save(taskHousing);
				return taskHousing;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
			taskHousing.setDescription("登录超时");

			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message("登录超时");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
		}

		return null;

	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		WebDriver driver = webParamHousing.getDriver();
		try {
			housingYvLinFutureService.getUserBasic(driver, messageLoginForHousing.getTask_id());
		} catch (Exception e) {

			e.printStackTrace();
			 taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());

			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
			save(taskHousing);
		}

		try {
			housingYvLinFutureService.getPayResult(driver, messageLoginForHousing.getTask_id());
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());

			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getError_code());
			save(taskHousing);
		}
		updateTaskHousing(taskHousing.getTaskid());
		driver.close();
		
		return null;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}