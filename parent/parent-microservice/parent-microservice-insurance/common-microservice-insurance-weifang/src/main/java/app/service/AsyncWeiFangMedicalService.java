package app.service;

import java.util.Calendar;
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
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangHtml;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangMedical;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangMedicalRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceWeiFangMedicalParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.weifang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.weifang"})
public class AsyncWeiFangMedicalService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceWeiFangMedicalParser insuranceWeiFangMedicalParser;
	@Autowired
	private InsuranceWeiFangMedicalRepository insuranceWeiFangMedicalRepository;
	@Autowired
	private InsuranceWeiFangHtmlRepository insuranceWeiFangHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取医疗信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("getMedicalInfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Calendar calendar = Calendar.getInstance();
		try {
			int temp = 0;
			for (int i = 0; i < 5; i++) {
				String year = String.valueOf(calendar.get(Calendar.YEAR) - i);
				WebParam webParam = insuranceWeiFangMedicalParser.getMedicalInfo(taskInsurance, cookies, year);
				if (null != webParam) {
					List<InsuranceWeiFangMedical> medicalList = webParam.getMedicalList();
					if (null != medicalList && !medicalList.isEmpty()) {
						insuranceWeiFangMedicalRepository.saveAll(medicalList);
						temp++;
						InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
						insuranceWeiFangHtml.setPageCount(1);
						insuranceWeiFangHtml.setType("medicalInfo");
						insuranceWeiFangHtml.setTaskid(insuranceRequestParameters.getTaskId());
						insuranceWeiFangHtml.setUrl(webParam.getUrl());
						insuranceWeiFangHtml.setHtml(webParam.getHtml());
						insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);
					} else {
						tracer.addTag("getMedicalInfo",
								insuranceRequestParameters.getTaskId() + "没有当前条件所对应的养老信息" + "查询年份" + year);
					}
				}
			}
			if (temp > 0) {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("getMedicalInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	}
}
