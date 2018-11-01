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
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingLostwork;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingLostworkRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceChongqingLostworkParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.chongqing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.chongqing"})
public class AsyncChongqingLostworkService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceChongqingHtmlRepository insuranceChongqingHtmlRepository;
	@Autowired
	private InsuranceChongqingLostworkRepository insuranceChongqingLostworkRepository;
	@Autowired
	private InsuranceChongqingLostworkParser insuranceChongqingLostworkParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取失业信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getLostworkInfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getLostworkInfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Calendar calendar = Calendar.getInstance();
		int temp=0;
		try {
			for (int i = 0; i < 5; i++) {
				String year = String.valueOf(calendar.get(Calendar.YEAR) - i);
				WebParam webParam = insuranceChongqingLostworkParser.getLostworkInfo(taskInsurance, cookies, year, "1");
				if (null != webParam) {
					List<InsuranceChongqingLostwork> lostworkList = webParam.getLostworkList();
					if (null != lostworkList && !lostworkList.isEmpty()) {
						insuranceChongqingLostworkRepository.saveAll(lostworkList);
						temp++;
						InsuranceChongqingHtml insuranceChongqingHtml = new InsuranceChongqingHtml();
						insuranceChongqingHtml.setPageCount(1);
						insuranceChongqingHtml.setType("lostworkInfo");
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
								WebParam webParam2 = insuranceChongqingLostworkParser.getLostworkInfo(taskInsurance,
										cookies, year, "2");
								List<InsuranceChongqingLostwork> lostworkList2 = webParam2.getLostworkList();
								if (null != lostworkList2 && !lostworkList2.isEmpty()) {
									insuranceChongqingLostworkRepository.saveAll(lostworkList2);
									InsuranceChongqingHtml insuranceChongqingHtml2 = new InsuranceChongqingHtml();
									insuranceChongqingHtml2.setPageCount(2);
									insuranceChongqingHtml2.setType("lostworkInfo");
									insuranceChongqingHtml2.setTaskid(insuranceRequestParameters.getTaskId());
									insuranceChongqingHtml2.setUrl(webParam2.getUrl());
									insuranceChongqingHtml2.setHtml(webParam2.getHtml());
									insuranceChongqingHtmlRepository.save(insuranceChongqingHtml2);
								}
							}
						}
					} else {
						tracer.addTag("getLostworkInfo",
								insuranceRequestParameters.getTaskId() + "没有当前条件所对应的失业信息" + "查询年份" + year);
					}
				}
			}
			if (temp > 0) {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.getLostworkInfo.Exception",e.getMessage());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}
		
	}
}
