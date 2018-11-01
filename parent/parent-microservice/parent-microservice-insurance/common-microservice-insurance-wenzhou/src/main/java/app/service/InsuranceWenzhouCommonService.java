package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

/**
 * Created by Mu on 2017/9/18.
 */
@SuppressWarnings("all")
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.wenzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.wenzhou"})
public class InsuranceWenzhouCommonService implements InsuranceLogin, InsuranceCrawler{
	
	@Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceWenzhouService insuranceWenzhouService;
	@Autowired
    private InsuranceService insuranceService;
	@Autowired
    private TracerLog tracer;

	@Override
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = insuranceWenzhouService.login(insuranceRequestParameters, 1);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.login.Exception", e.getMessage());
			taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
		}
		return taskInsurance;
	}
	
	@Override
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		//更改状态完正在爬取
		TaskInsurance taskInsurance = insuranceWenzhouService.updateTaskInsurance(insuranceRequestParameters);
        try {
			insuranceWenzhouService.getUserInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.getUserinfo.Exception", e.getMessage());
			taskInsurance = insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 404, taskInsurance);
		}
        try {
			insuranceWenzhouService.getPensionInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.getUserinfo.Exception", e.getMessage());
			taskInsurance = insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 404, taskInsurance);
		}
        try {
			insuranceWenzhouService.getMedicalInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.getUserinfo.Exception", e.getMessage());
			taskInsurance = insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 404, taskInsurance);
		}
        try {
			insuranceWenzhouService.getGongshangInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.getUserinfo.Exception", e.getMessage());
			taskInsurance = insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 404, taskInsurance);
		}
        try {
			insuranceWenzhouService.getBirthInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.getUserinfo.Exception", e.getMessage());
			taskInsurance = insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 404, taskInsurance);
		}
        try {
			insuranceWenzhouService.getShiyeInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.getUserinfo.Exception", e.getMessage());
			taskInsurance = insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 404, taskInsurance);
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
