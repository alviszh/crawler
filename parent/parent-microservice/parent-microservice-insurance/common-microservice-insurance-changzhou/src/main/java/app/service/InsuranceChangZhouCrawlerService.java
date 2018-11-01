package app.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.bean.AssociatedPersons;
import app.bean.WebParamInsurance;
import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EnableAsync
public class InsuranceChangZhouCrawlerService extends InsuranceService implements InsuranceLogin {

	public static final Logger log = LoggerFactory.getLogger(InsuranceChangZhouCrawlerService.class);

	@Autowired
	private InsuranceChangZhouFutureService insuranceChangZhouFutureService;

	private WebParamInsurance<?> webParamInsurance = null;

	@Autowired
	private TracerLog tracerLog;

	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {

		webParamInsurance = null;

		TaskInsurance taskInsurance = findTaskInsurance(insuranceRequestParameters.getTaskId());
		tracerLog.output("taskid", insuranceRequestParameters.getTaskId());
		tracerLog.output("开始登录 insuranceRequestParameters", insuranceRequestParameters.toString());

		webParamInsurance = insuranceChangZhouFutureService.login(insuranceRequestParameters, taskInsurance);
		if (webParamInsurance == null || webParamInsurance.getWebClient() == null) {
			taskInsurance.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskInsurance.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskInsurance.setDescription(webParamInsurance.getErrormessage());
			taskInsurance.setError_message(webParamInsurance.getErrormessage());
			taskInsurance.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
			taskInsurance = save(taskInsurance);
			return taskInsurance;
		}
		taskInsurance = changeLoginStatusSuccess(taskInsurance);

		return taskInsurance;

	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = findTaskInsurance(insuranceRequestParameters.getTaskId());

		List<AssociatedPersons> listPayNeed = insuranceChangZhouFutureService
				.getUserNeed(webParamInsurance.getWebClient(), taskInsurance);

		if (listPayNeed == null) {
			taskInsurance.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getPhase());
			taskInsurance.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getPhasestatus());
			taskInsurance.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getDescription());
			taskInsurance.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getError_code());
			taskInsurance.setError_message(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getDescription());
			taskInsurance = save(taskInsurance);
			return taskInsurance;
		}

		for (AssociatedPersons associatedPersons : listPayNeed) {
			// tracerLog.output("AssociatedPersons",
			// associatedPersons.toString());

			insuranceChangZhouFutureService.getUser(webParamInsurance.getWebClient(), taskInsurance,
					associatedPersons.getId() + "");
			insuranceChangZhouFutureService.getPayResult(webParamInsurance.getWebClient(), taskInsurance,
					associatedPersons.getId() + "");
		}
		taskInsurance = changeCrawlerStatusSuccess(taskInsurance);
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}