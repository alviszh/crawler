package app.service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingInjury;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingInjuryRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceChongqingInjuryParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.chongqing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.chongqing"})
public class AsyncChongqingInjuryService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceChongqingHtmlRepository insuranceChongqingHtmlRepository;
	@Autowired
	private InsuranceChongqingInjuryParser insuranceChongqingInjuryParser;
	@Autowired
	private InsuranceChongqingInjuryRepository insuranceChongqingInjuryRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取工伤信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getInjuryInfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getInjuryInfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Calendar calendar = Calendar.getInstance();
		int temp=0;
		try {
			for (int i = 0; i < 5; i++) {
				String year = String.valueOf(calendar.get(Calendar.YEAR) - i);
				WebParam webParam = insuranceChongqingInjuryParser.getInjuryInfo(taskInsurance, cookies, year, "1");
				if (null != webParam) {
					List<InsuranceChongqingInjury> injuryList = webParam.getInjuryList();
					if (null != injuryList && !injuryList.isEmpty()) {
						insuranceChongqingInjuryRepository.saveAll(injuryList);
						temp++;
						InsuranceChongqingHtml insuranceChongqingHtml = new InsuranceChongqingHtml();
						insuranceChongqingHtml.setPageCount(1);
						insuranceChongqingHtml.setType("injuryInfo");
						insuranceChongqingHtml.setTaskid(insuranceRequestParameters.getTaskId());
						insuranceChongqingHtml.setUrl(webParam.getUrl());
						insuranceChongqingHtml.setHtml(webParam.getHtml());
						insuranceChongqingHtmlRepository.save(insuranceChongqingHtml);
						String html = webParam.getHtml();
						if (null != html) {
							Document doc = Jsoup.parse(html);
							if (null != doc) {
								JsonParser parser = new JsonParser();
								JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
								JsonObject pageObject = object.get("page").getAsJsonObject();
								String pageCount = pageObject.get("pageCount").getAsString();
								if ("2".equals(pageCount)) {
									WebParam webParam2 = insuranceChongqingInjuryParser.getInjuryInfo(taskInsurance,
											cookies, year, "2");
									List<InsuranceChongqingInjury> injuryList2 = webParam2.getInjuryList();
									if (null != injuryList2 && !injuryList2.isEmpty()) {
										insuranceChongqingInjuryRepository.saveAll(injuryList2);
										InsuranceChongqingHtml insuranceChongqingHtml2 = new InsuranceChongqingHtml();
										insuranceChongqingHtml2.setPageCount(2);
										insuranceChongqingHtml2.setType("injuryInfo");
										insuranceChongqingHtml2.setTaskid(insuranceRequestParameters.getTaskId());
										insuranceChongqingHtml2.setUrl(webParam2.getUrl());
										insuranceChongqingHtml2.setHtml(webParam2.getHtml());
										insuranceChongqingHtmlRepository.save(insuranceChongqingHtml2);
									}
								}
							}
						}
					}

				}
			}
			if (temp > 0) {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				tracer.addTag("action.getInjury.ERROR",taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.getInjury.Exception",e.getMessage());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}
	}
	
}
