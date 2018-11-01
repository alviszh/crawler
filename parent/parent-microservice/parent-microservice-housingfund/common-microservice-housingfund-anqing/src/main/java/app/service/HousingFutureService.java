package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.anqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.anqing")
public class HousingFutureService extends HousingBasicService implements ICrawlerLogin {

	public static final Logger log = LoggerFactory.getLogger(HousingFutureService.class);

	@Autowired
	private LoginAndGetService loginAndGetService;

	@Autowired
	private HousingAsyncService housingAsyncService;

	private WebParamHousing<?> webParamHousing = null;

	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		try {
			webParamHousing = loginAndGetService.login(webClient, messageLoginForHousing.getNum().trim(),
					messageLoginForHousing.getPassword().trim());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getDescription());

			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			tracer.addTag("parser.login.auth", e.getMessage());
			save(taskHousing);
			return null;
		}
		if (webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("验证码不正确") != -1) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

			save(taskHousing);
			return taskHousing;
		}
		if (webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("查询出错") != -1) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

			save(taskHousing);
			return taskHousing;
		}
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

		taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
		taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
		taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
		save(taskHousing);
//		getAllData(messageLoginForHousing);
		return taskHousing;

	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		try {
			housingAsyncService.getAccountInfor(webParamHousing, messageLoginForHousing, taskHousing);
			housingAsyncService.getAccountDetails(webParamHousing.getWebClient(), messageLoginForHousing, taskHousing);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		taskHousing = findTaskHousing(taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());

		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getError_code());
		taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());

		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}