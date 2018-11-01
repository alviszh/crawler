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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingHtml;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingMedical;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingMedicalRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceChongqingMedicalParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.chongqing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.chongqing"})
public class AsyncChongqingMedicalService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceChongqingMedicalParser insuranceChongqingMedicalParser;
	@Autowired
	private InsuranceChongqingMedicalRepository insuranceChongqingMedicalRepository;
	@Autowired
	private InsuranceChongqingHtmlRepository insuranceChongqingHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取医疗信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getMedicalInfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Calendar calendar = Calendar.getInstance();
		int temp=0;
		try {
			for (int i = 0; i < 5; i++) {
				String year = String.valueOf(calendar.get(Calendar.YEAR) - i);
				WebParam webParam = insuranceChongqingMedicalParser.getMedicalInfo(taskInsurance, cookies, year, "1");
				if (null != webParam) {
					List<InsuranceChongqingMedical> medicalList = webParam.getMedicalList();
					if (null != medicalList && !medicalList.isEmpty()) {
						insuranceChongqingMedicalRepository.saveAll(medicalList);
						temp++;
						InsuranceChongqingHtml insuranceChongqingHtml = new InsuranceChongqingHtml();
						insuranceChongqingHtml.setPageCount(1);
						insuranceChongqingHtml.setType("medicalInfo");
						insuranceChongqingHtml.setTaskid(insuranceRequestParameters.getTaskId());
						insuranceChongqingHtml.setUrl(webParam.getUrl());
						insuranceChongqingHtml.setHtml(webParam.getHtml());
						insuranceChongqingHtmlRepository.save(insuranceChongqingHtml);
						String html = webParam.getHtml();
						if (null != html) {
							JsonParser parser = new JsonParser();
							JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
							JsonObject pageObject = object.get("page").getAsJsonObject();
							String pageCount = pageObject.get("pageCount").getAsString();
							if ("2".equals(pageCount)) {
								WebParam webParam2 = insuranceChongqingMedicalParser.getMedicalInfo(taskInsurance,
										cookies, year, "2");
								List<InsuranceChongqingMedical> medicalList2 = webParam2.medicalList;
								if (null != medicalList2 && !medicalList2.isEmpty()) {
									insuranceChongqingMedicalRepository.saveAll(medicalList2);
									InsuranceChongqingHtml insuranceChongqingHtml2 = new InsuranceChongqingHtml();
									insuranceChongqingHtml2.setPageCount(2);
									insuranceChongqingHtml2.setType("medicalInfo");
									insuranceChongqingHtml2.setTaskid(insuranceRequestParameters.getTaskId());
									insuranceChongqingHtml2.setUrl(webParam2.getUrl());
									insuranceChongqingHtml2.setHtml(webParam2.getHtml());
									insuranceChongqingHtmlRepository.save(insuranceChongqingHtml2);
								}
							}
						}
					} else {
						tracer.addTag("getMedicalInfo",
								insuranceRequestParameters.getTaskId() + "没有当前条件所对应的养老信息" + "查询年份" + year);
					}
				}
			}
			if (temp > 0) {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.getMedicalInfo.Exception", e.getMessage());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}
	}
}
