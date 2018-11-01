package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.bean.WebParamInsurance;
import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EnableAsync
public class InsuranceLianYunGangCrawlerService extends InsuranceService implements InsuranceLogin{

	public static final Logger log = LoggerFactory.getLogger(InsuranceLianYunGangCrawlerService.class);

	@Autowired
	private InsuranceLianYunGangFutureService insuranceLianYunGangFutureService;
	
	@Autowired
	private TracerLog tracerLog;
	
	private WebParamInsurance<?> webParamInsurance = null;

	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {

		webParamInsurance = null;

		TaskInsurance taskInsurance = findTaskInsurance(insuranceRequestParameters.getTaskId());
		tracerLog.output("taskid", insuranceRequestParameters.getTaskId());
		tracerLog.output("开始登录 insuranceRequestParameters", insuranceRequestParameters.toString());

		webParamInsurance = insuranceLianYunGangFutureService.login(insuranceRequestParameters,
				taskInsurance);
		if (webParamInsurance == null ||webParamInsurance.getDriver() == null) {
			taskInsurance.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskInsurance.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskInsurance.setDescription(webParamInsurance.getErrormessage());
			taskInsurance.setError_message(webParamInsurance.getErrormessage());
			taskInsurance.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
			taskInsurance = save(taskInsurance);
			tracerLog.output("登录 失败 TaskInsurance", taskInsurance.toString());

			return taskInsurance;
		}
		taskInsurance = changeLoginStatusSuccess(taskInsurance);
		tracerLog.output("登录 成功 TaskInsurance", taskInsurance.toString());
		
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = findTaskInsurance(insuranceRequestParameters.getTaskId());

		try {
			taskInsurance = insuranceLianYunGangFutureService.getUserResult(webParamInsurance.getDriver(),
					taskInsurance);

		} catch (Exception e) {
			taskInsurance = changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 404, taskInsurance);

			e.printStackTrace();
		}

		try {
			taskInsurance = insuranceLianYunGangFutureService.getPayResult(webParamInsurance.getDriver(),
					taskInsurance);

		} catch (Exception e) {
			e.printStackTrace();

			taskInsurance = changeCrawlerStatusError(taskInsurance);

		}

		webParamInsurance.getDriver().close();
		return null;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}