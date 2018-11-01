package app.service;

import java.net.URL;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinMaternityRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinPensionRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceTianjinParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.tianjin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.tianjin" })
public class InsuranceTianjinService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceTianjinParser insuranceTianjinParser;
	@Autowired
	private InsuranceTianjinUserInfoRepository insuranceTianjinUserInfoRepository;
	@Autowired
	private InsuranceTianjinHtmlRepository insuranceTianjinHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceTianjinInjuryRepository insuranceTianjinInjuryRepository;
	@Autowired
	private InsuranceTianjinUnemploymentRepository insuranceTianjinUnemploymentRepository;
	@Autowired
	private InsuranceTianjinPensionRepository insuranceTianjinPensionRepository;
	@Autowired
	private InsuranceTianjinMaternityRepository insuranceTianjinMaternityRepository;
	@Autowired
	private InsuranceTianjinMedicalRepository insuranceTianjinMedicalRepository;

	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public void getUserInfo(TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("parser.crawler.getUserinfo", taskInsurance.getTaskid());
		// TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(
		// insuranceRequestParameters.getTaskId());

		WebParam webParam = insuranceTianjinParser.htmlParserUserInfo(taskInsurance);

		if (null != webParam) {
			insuranceTianjinUserInfoRepository.save(webParam.getInsuranceTianjinUserInfo());
			tracer.addTag("getUserInfo 个人信息", "个人信息已入库!");
			String html = webParam.getPage1().getWebResponse().getContentAsString();
			if (null != webParam.getPage1()) {

				InsuranceTianjinHtml insuranceTianjinHtml = new InsuranceTianjinHtml();
				insuranceTianjinHtml.setPageCount(1);
				insuranceTianjinHtml.setType("userinfo");
				insuranceTianjinHtml.setTaskid(taskInsurance.getTaskid());
				insuranceTianjinHtml.setUrl(webParam.getUrl());
				insuranceTianjinHtml.setHtml(html);
				insuranceTianjinHtmlRepository.save(insuranceTianjinHtml);
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());

				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), webParam.getCode(),
						taskInsurance);

				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				

				tracer.addTag("getUserInfo 个人信息源码", "个人信息源码表入库!");
			}
		}
	}

	/**
	 * @Des 获取医疗信息
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public void getMedicalInfo(TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("InsuranceTianjinService.getMedical", taskInsurance.getTaskid());

		// TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(
		// insuranceRequestParameters.getTaskId());

		WebParam webParam = insuranceTianjinParser.getMedicalInfo(taskInsurance);
		if (null != webParam) {
			insuranceTianjinMedicalRepository.saveAll(webParam.getInsuranceTianjinMedical());
			tracer.addTag("InsuranceTianjinService.getMedical:SUCCESS", taskInsurance.getTaskid());
			String html = webParam.getPage1().getWebResponse().getContentAsString();
			if (null != webParam.getPage1()) {
				InsuranceTianjinHtml insuranceTianjinHtml = new InsuranceTianjinHtml();
				insuranceTianjinHtml.setPageCount(1);
				insuranceTianjinHtml.setTaskid(taskInsurance.getTaskid());
				insuranceTianjinHtml.setType("MedicalInfo");
				insuranceTianjinHtml.setUrl(webParam.getUrl());
				insuranceTianjinHtml.setHtml(html);
				insuranceTianjinHtmlRepository.save(insuranceTianjinHtml);
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), webParam.getCode(),
						taskInsurance);

				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.savePensionInfo.ERROR", taskInsurance.getTaskid());
			}
		}

	}

	/**
	 * @Des 获取养老信息
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public void getPensionInfo(TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("InsuranceTianjinService.getPension", taskInsurance.getTaskid());
		// TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(
		// insuranceRequestParameters.getTaskId());

		WebParam webParam = insuranceTianjinParser.getPension(taskInsurance);
		if (null != webParam) {
			insuranceTianjinPensionRepository.saveAll(webParam.getInsuranceTianjinPension());
			tracer.addTag("InsuranceTianjinService.getPension:SUCCESS", taskInsurance.getTaskid());
			String html = webParam.getPage1().getWebResponse().getContentAsString();
			if (null != webParam.getPage1()) {
				InsuranceTianjinHtml insuranceTianjinHtml = new InsuranceTianjinHtml();
				insuranceTianjinHtml.setPageCount(1);
				insuranceTianjinHtml.setTaskid(taskInsurance.getTaskid());
				insuranceTianjinHtml.setType("PensionInfo");
				insuranceTianjinHtml.setUrl(webParam.getUrl());
				insuranceTianjinHtml.setHtml(html);
				insuranceTianjinHtmlRepository.save(insuranceTianjinHtml);
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), webParam.getCode(),
						taskInsurance);

				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.saveMedicalInfo.ERROR", taskInsurance.getTaskid());
			}
		}

	}

	/**
	 * @Des 获取工伤信息
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public void getInjuryInfo(TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("InsuranceTianjinService.getInjuryInfo", taskInsurance.getTaskid());
		// TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(
		// insuranceRequestParameters.getTaskId());

		WebParam webParam = insuranceTianjinParser.getInjuryInfo(taskInsurance);
		if (null != webParam) {
			insuranceTianjinInjuryRepository.saveAll(webParam.getInsuranceTianjinInjury());
			tracer.addTag("InsuranceTianjinService.getInjuryInfo:SUCCESS", taskInsurance.getTaskid());
			String html = webParam.getPage1().getWebResponse().getContentAsString();
			if (null != webParam.getPage1()) {

				InsuranceTianjinHtml insuranceTianjinHtml = new InsuranceTianjinHtml();
				insuranceTianjinHtml.setPageCount(1);
				insuranceTianjinHtml.setTaskid(taskInsurance.getTaskid());
				insuranceTianjinHtml.setType("InjuryInfo");
				insuranceTianjinHtml.setUrl(webParam.getUrl());
				insuranceTianjinHtml.setHtml(html);
				insuranceTianjinHtmlRepository.save(insuranceTianjinHtml);
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), webParam.getCode(),
						taskInsurance);

				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.saveInjuryInfo.ERROR", taskInsurance.getTaskid());
			}
		}

	}

	/**
	 * @Des 获取生育信息
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public void getMaternityInfo(TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("InsuranceTianjinService.getMaternityInfo", taskInsurance.getTaskid());

		WebParam webParam = insuranceTianjinParser.getMaternityInfo(taskInsurance);
		if (null != webParam) {
			if(null != webParam.getInsuranceTianjinMaternity()){
				insuranceTianjinMaternityRepository.saveAll(webParam.getInsuranceTianjinMaternity());
				tracer.addTag("InsuranceTianjinService.getMaternity:SUCCESS", taskInsurance.getTaskid());
			}
			String html = webParam.getPage1().getWebResponse().getContentAsString();
			if (null != webParam.getPage1()) {

				InsuranceTianjinHtml insuranceTianjinHtml = new InsuranceTianjinHtml();
				insuranceTianjinHtml.setPageCount(1);
				insuranceTianjinHtml.setTaskid(taskInsurance.getTaskid());
				insuranceTianjinHtml.setType("MaternityInfo");
				insuranceTianjinHtml.setUrl(webParam.getUrl());
				insuranceTianjinHtml.setHtml(html);
				insuranceTianjinHtmlRepository.save(insuranceTianjinHtml);
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), webParam.getCode(),
						taskInsurance);

				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.saveMaternityInfo.ERROR", taskInsurance.getTaskid());
			}
		}
	}

	/**
	 * @Des 获取失业信息
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public void getUnemploymentInfo(TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("InsuranceTianjinService.getUnemploymentInfo", taskInsurance.getTaskid());
		WebParam webParam = insuranceTianjinParser.getUnemploymentInfo(taskInsurance);
		if (null != webParam) {
			insuranceTianjinUnemploymentRepository.saveAll(webParam.getInsuranceTianjinUnemployment());
			tracer.addTag("InsuranceTianjinService.getunemployment:SUCCESS", taskInsurance.getTaskid());
			String html = webParam.getPage1().getWebResponse().getContentAsString();
			if (null != webParam.getPage1()) {

				InsuranceTianjinHtml insuranceTianjinHtml = new InsuranceTianjinHtml();
				insuranceTianjinHtml.setPageCount(1);
				insuranceTianjinHtml.setTaskid(taskInsurance.getTaskid());
				insuranceTianjinHtml.setType("unemploymentInfo");
				insuranceTianjinHtml.setUrl(webParam.getUrl());
				insuranceTianjinHtml.setHtml(html);
				insuranceTianjinHtmlRepository.save(insuranceTianjinHtml);
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), webParam.getCode(),
						taskInsurance);

				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.saveUnemploymentInfo.ERROR", taskInsurance.getTaskid());
			}

		}

	}

//	@HystrixCommand(fallbackMethod = "fallback")
//	public String hystrix() {
//		tracer.addTag("InsuranceChengduService hystrix", "start");
//		String url = "http://jypt.cdhrss.gov.cn:8048/portal.php?id=1";
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//
//		try {
//			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			HtmlPage page = webClient.getPage(webRequest);
//			int status = page.getWebResponse().getStatusCode();
//			tracer.addTag("hystrix 成都社保登录页状态码", String.valueOf(status));
//			if (200 == status) {
//				String html = page.getWebResponse().getContentAsString();
//				tracer.addTag("hystrix 成都社保登录页", html);
//				return "SUCCESS";
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "ERROR";
//	}

	public String fallback() {
		tracer.addTag("InsuranceChengduService.hystrix.fallback", "ERROR");
		return "ERROR";
	}

	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @Des 更新taskInsurance
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}
}
