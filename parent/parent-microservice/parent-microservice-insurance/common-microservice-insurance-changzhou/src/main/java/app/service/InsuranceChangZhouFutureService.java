package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.changzhou.InsuranceChangZhouBasicUser;
import com.microservice.dao.entity.crawler.insurance.changzhou.InsuranceChangZhouPay;
import com.microservice.dao.repository.crawler.insurance.changzhou.InsuranceChangZhouBasicUserRepository;
import com.microservice.dao.repository.crawler.insurance.changzhou.InsuranceChangZhouPayRepository;
import app.bean.AssociatedPersons;
import app.bean.InsuranceBasicSuQianChangZhouLianYunGangBean;

import app.bean.WebParamInsurance;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.InsuranceChangZhouParse;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.insurance.changzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.insurance.changzhou")
public class InsuranceChangZhouFutureService extends InsuranceService {

	public static final Logger log = LoggerFactory.getLogger(InsuranceChangZhouFutureService.class);

	@Autowired
	private LoginAngGetService loginAngGetService;

	@Autowired
	private InsuranceChangZhouBasicUserRepository insuranceChangZhouBasicUserRepository;

	@Autowired
	private InsuranceChangZhouPayRepository insuranceChangZhouPayRepository;
	
	@Autowired
	private TracerLog tracerLog;
	

	public WebParamInsurance<?> login(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {
			return loginAngGetService.loginChrome(insuranceRequestParameters, taskInsurance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<AssociatedPersons> getUserNeed(WebClient webClient, TaskInsurance taskInsurance) {
		try {
			Page page = loginAngGetService.getUserNeed(webClient);

			InsuranceBasicSuQianChangZhouLianYunGangBean result = InsuranceChangZhouParse
					.UserNeedParse(page.getWebResponse().getContentAsString());

			List<AssociatedPersons> listPayNeed = result.getAssociatedPersons();

			return listPayNeed;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;

	}

	public void getUser(WebClient webClient, TaskInsurance taskInsurance, String persinoId) {
		try {
			Page page = loginAngGetService.getUser(webClient, persinoId);

			InsuranceChangZhouBasicUser result = InsuranceChangZhouParse
					.UserParse(page.getWebResponse().getContentAsString());
			tracerLog.output("getUser  result" + persinoId, result.toString());
			insuranceChangZhouBasicUserRepository.save(result);
			// 基本情况 解析完成
			taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS.getError_code());

			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());

			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}

	}

	public void getPayResult(WebClient webClient, TaskInsurance taskInsurance, String persinoId) {

		taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
		LocalDate nowdate = LocalDate.now();

		String startDate = nowdate.plusYears(-10).getYear() + "01";

		String month = nowdate.getMonthValue() + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String endDate = nowdate.getYear() + month;

		List<String> list = new ArrayList<String>();

		// 110 养老保险 310 医疗保险 410 工伤保险 510 生育保险 210 失业保险 380 居民医疗保险
		try {
			list.add(loginAngGetService.getPay(webClient, 110 + "", persinoId, startDate, endDate).getWebResponse()
					.getContentAsString());
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE);
			tracerLog.output("养老保险错误", e.getMessage());
		}
		try {
			list.add(loginAngGetService.getPay(webClient, 310 + "", persinoId, startDate, endDate).getWebResponse()
					.getContentAsString());
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE);
		
			tracerLog.output("医疗保险错误", e.getMessage());

		}
		try {
			list.add(loginAngGetService.getPay(webClient, 410 + "", persinoId, startDate, endDate).getWebResponse()
					.getContentAsString());
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE);
		
			tracerLog.output("工伤保险错误", e.getMessage());
		}
		try {
			list.add(loginAngGetService.getPay(webClient, 510 + "", persinoId, startDate, endDate).getWebResponse()
					.getContentAsString());
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE);
		
			tracerLog.output("生育保险错误", e.getMessage());
		}
		try {
			list.add(loginAngGetService.getPay(webClient, 210 + "", persinoId, startDate, endDate).getWebResponse()
					.getContentAsString());
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE);
		
			tracerLog.output("失业保险 错误", e.getMessage());
		}
		try {
			list.add(loginAngGetService.getPay(webClient, 380 + "", persinoId, startDate, endDate).getWebResponse()
					.getContentAsString());
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskInsurance = changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE);
		
			tracerLog.output("居民医疗保险错误", e.getMessage());
		}

		List<InsuranceChangZhouPay> resultlist = new ArrayList<>();
		for (String html : list) {
			try {
				resultlist.addAll(InsuranceChangZhouParse.PayParse(html));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				tracerLog.output("解析错误", html);
			}
		}

		if (resultlist.size() > 0) {

			tracerLog.output("getPayResult " + persinoId, "缴费信息共" + list.size() + "条");
			for (InsuranceChangZhouPay insuranceChangZhouPayresult : resultlist) {
				insuranceChangZhouPayresult.setTaskId(taskInsurance.getTaskid());
				insuranceChangZhouPayRepository.save(insuranceChangZhouPayresult);
			}

			changeCrawlerStatusSuccess(taskInsurance);
		} else {
			tracerLog.output("getPayResult " + persinoId, "没有缴费信息");
			changeCrawlerStatusSuccess(taskInsurance);
		}

	}

}