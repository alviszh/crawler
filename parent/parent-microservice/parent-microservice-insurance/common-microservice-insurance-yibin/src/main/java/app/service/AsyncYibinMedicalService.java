package app.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYibinHtml;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYinbinMedical;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.yibin.InsuranceYibinHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.yibin.InsuranceYinbinMedicalRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceYibinMedicalParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.yibin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.yibin"})
public class AsyncYibinMedicalService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceYibinMedicalParser insuranceYibinMedicalParser;
	@Autowired
	private InsuranceYinbinMedicalRepository insuranceYinbinMedicalRepository;
	@Autowired
	private InsuranceYibinHtmlRepository insuranceYibinHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取医疗信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("getMedicalInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam = insuranceYibinMedicalParser.getMedicalInfo(taskInsurance, cookies);
		List<InsuranceYinbinMedical> medicalList = webParam.getMedicalList();
		if (null != medicalList && !medicalList.isEmpty()) {
			insuranceYinbinMedicalRepository.saveAll(medicalList);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			tracer.addTag("getMedicalInfo", "社保医疗信息已入库"+medicalList.toString());
			InsuranceYibinHtml insuranceYibinHtml = new InsuranceYibinHtml();
			insuranceYibinHtml.setPageCount(1);
			insuranceYibinHtml.setType("medicalInfo");
			insuranceYibinHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceYibinHtml.setUrl(webParam.getUrl());
			insuranceYibinHtml.setHtml(webParam.getHtml());
			insuranceYibinHtmlRepository.save(insuranceYibinHtml);
		} else {
			tracer.addTag("getMedicalInfo", insuranceRequestParameters.getTaskId() + "没有当前条件所对应的养老信息");
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	}
}
