package app.service;

import java.net.URL;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduBear;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduHtml;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduInjury;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduMedical;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduMedicalconsumption;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduPension;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduPensionAccount;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduSeriousIllnessMedical;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduSummary;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduUnemployment;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduBearRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduMedicalconsumptionRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduPensionAccountRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduPensionRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduSeriousIllnessMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduSummaryRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.chengdu.InsuranceChengduUserInfoRepository;
import com.module.htmlunit.WebCrawler;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.exceptiondetail.EUtils;
import app.parser.InsuranceChengduParser;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.chengdu" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.chengdu" })
public class InsuranceChengduService implements InsuranceLogin{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceChengduParser insuranceChengduiParser;
	@Autowired
	private InsuranceChengduUserInfoRepository insuranceChengduUserInfoRepository;
	@Autowired
	private InsuranceChengduHtmlRepository insuranceChengduHtmlRepository;

	@Autowired
	private InsuranceChengduSummaryRepository insuranceChengduSummaryRepository;
	@Autowired
	private InsuranceChengduPensionRepository insuranceChengduPensionRepository;

	@Autowired
	private InsuranceChengduBearRepository insuranceChengduBearRepository;

	@Autowired
	private InsuranceChengduInjuryRepository insuranceChengduInjuryRepository;

	@Autowired
	private InsuranceChengduMedicalRepository insuranceChengduMedicalRepository;

	@Autowired
	private InsuranceChengduUnemploymentRepository insuranceChengduUnemploymentRepository;

	@Autowired
	private InsuranceChengduPensionAccountRepository insuranceChengduPensionAccountRepository;

	@Autowired
	private InsuranceChengduSeriousIllnessMedicalRepository insuranceChengduSeriousIllnessMedicalRepository;

	@Autowired
	private InsuranceChengduMedicalconsumptionRepository insuranceChengduMedicalconsumptionRepository;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskInsurance loginRetry(InsuranceRequestParameters insuranceRequestParameters, Integer count) throws Exception {

		tracer.addTag("InsuranceChengduService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		if (null != taskInsurance) {

			WebParam webParam = insuranceChengduiParser.login(insuranceRequestParameters);

			if (null == webParam) {
				tracer.addTag("InsuranceChengduService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			} else {
				String html = webParam.getPage().getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				if (html.contains("参保人员基本信息")) {
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					return taskInsurance;
				} else {
					tracer.addTag("InsuranceChengduService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败次数" + count);
					if (count < 4) {
						loginRetry(insuranceRequestParameters, ++count);
					} else {
						taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
						return taskInsurance;
					}
				}

			}

		}

		return null;
	}

	/**
	 * 更新task表（doing 正在登录状态）
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		JSONObject jsonObject = JSONObject.fromObject(insuranceRequestParameters);
		taskInsurance.setTesthtml(jsonObject.toString());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @Des 更新task表（doing 正在采集）
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}

	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	@Async
	public Future<String> getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("InsuranceChengduService.getUserinfo", insuranceRequestParameters.getTaskId());

		tracer.addTag("parser.crawler.getUserinfo", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		@SuppressWarnings("rawtypes")
		WebParam webParam = insuranceChengduiParser.getUserInfo(taskInsurance, taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduUserInfoRepository.save(webParam.getInsuranceChengduUserInfo());
			try {
				// 医疗保险账户信息:账户余额
				getMedicalBalance(insuranceRequestParameters);
			} catch (Exception e) {
				tracer.addTag("InsuranceChengduService.getUserInfo医疗保险账户信息:账户余额", "ERROR!");
			}
			tracer.addTag("InsuranceChengduService.getUserInfo 个人信息", "个人信息已入库!");

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), webParam.getCode(),
					taskInsurance);

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_userinfo", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);
			tracer.addTag("InsuranceChengduService.getUserInfo 个人信息源码", "个人信息源码表入库!");

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

		} else {
			tracer.addTag("InsuranceChengduService.getUserInfo.webParam个人信息  is null",
					insuranceRequestParameters.getTaskId());

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取社保总信息
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getSummary(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getSummary", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduSummary> webParam = insuranceChengduiParser.getSummary(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduSummaryRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getSummary 社保总信息", "社保总信息已入库!");

			tracer.addTag("InsuranceChengduService.getSummary:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_summary", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

			tracer.addTag("InsuranceChengduService.getSummary 社保总信息", "社保总信息源码表入库!");
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取养老缴费明细
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getPension(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getPension", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduPension> webParam = insuranceChengduiParser.getPension(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduPensionRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getPension:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_pension", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), webParam.getCode(),
					taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} else {

			tracer.addTag("InsuranceChengduService.getPension.webParam is null",
					insuranceRequestParameters.getTaskId());

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 500, taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取生育保险缴费明细
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getBear(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getPension", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduBear> webParam = insuranceChengduiParser.getBear(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduBearRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getBear:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_bear", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), webParam.getCode(),
					taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} else {
			tracer.addTag("InsuranceChengduService.getBear.webParam is null", insuranceRequestParameters.getTaskId());

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 500, taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取工伤保险缴费明细
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getInjury(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getInjury", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduInjury> webParam = insuranceChengduiParser.getInjury(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduInjuryRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getInjury:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_injury", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), webParam.getCode(),
					taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

		} else {

			tracer.addTag("InsuranceChengduService.getInjury.webParam is null", insuranceRequestParameters.getTaskId());

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 500, taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取医疗保险缴费明细
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getMedical(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getMedical", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduMedical> webParam = insuranceChengduiParser.getMedical(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduMedicalRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getMedical:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_medical", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), webParam.getCode(),
					taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} else {

			tracer.addTag("InsuranceChengduService.getMedical.webParam is null",
					insuranceRequestParameters.getTaskId());

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 500, taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取失业保险缴费明细
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getUnemployment(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getUnemployment", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduUnemployment> webParam = insuranceChengduiParser.getUnemployment(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduUnemploymentRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getUnemployment:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_unemployment", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), webParam.getCode(),
					taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} else {

			tracer.addTag("InsuranceChengduService.getUnemployment.webParam is null",
					insuranceRequestParameters.getTaskId());

			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 500, taskInsurance);

//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取养老保险个人账户明细信息
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getPensionAccount(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getPensionAccount", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduPensionAccount> webParam = insuranceChengduiParser.getPensionAccount(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduPensionAccountRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getPensionAccount:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_pensionaccount", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取大病补充医疗保险缴费明细
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getSeriousIllnessMedical(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getSeriousIllnessMedical", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduSeriousIllnessMedical> webParam = insuranceChengduiParser
				.getSeriousIllnessMedical(taskInsurance, taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduSeriousIllnessMedicalRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getSeriousIllnessMedical:SUCCESS",
					insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_seriousillnessmedical", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 获取医疗账户消费明细
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public Future<String> getMedicalconsumption(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChengduService.getMedicalconsumption", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceChengduMedicalconsumption> webParam = insuranceChengduiParser
				.getMedicalconsumption(taskInsurance, taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduMedicalconsumptionRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceChengduService.getMedicalconsumption:SUCCESS",
					insuranceRequestParameters.getTaskId());

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_medicalconsumption", 1, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);

		}
		return new AsyncResult<String>("200");
	}

	/**
	 * @Des 获取医疗保险账户信息:账户余额
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	public void getMedicalBalance(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("InsuranceChengduService.getMedicalBalance", insuranceRequestParameters.getTaskId());

		tracer.addTag("parser.crawler.getUserinfo", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		@SuppressWarnings("rawtypes")
		WebParam webParam = insuranceChengduiParser.getMedicalBalance(taskInsurance, taskInsurance.getCookies());
		if (null != webParam) {
			insuranceChengduUserInfoRepository.updateMedicalBalanceByTaskid(webParam.getMedicalBalance(),
					taskInsurance.getTaskid());

			tracer.addTag("InsuranceChengduService.getMedicalBalance", "医疗保险账户信息:账户余额");

			InsuranceChengduHtml insuranceChengduHtml = new InsuranceChengduHtml(insuranceRequestParameters.getTaskId(),
					"insurance_chengdu_userinfo", 2, webParam.getUrl(), webParam.getHtml());
			insuranceChengduHtmlRepository.save(insuranceChengduHtml);
			tracer.addTag("InsuranceChengduService.getMedicalBalance医疗保险账户信息:账户余额", "医疗保险账户信息:账户余额源码表入库!");

		}

	}

	@HystrixCommand(fallbackMethod = "fallback")
	public String hystrix() {
		tracer.addTag("InsuranceChengduService hystrix", "start");
		String url = "http://jypt.cdhrss.gov.cn:8048/portal.php?id=1";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("hystrix 成都社保登录页状态码", String.valueOf(status));
			if (200 == status) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("hystrix 成都社保登录页", html);
				return "SUCCESS";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	public String fallback() {
		tracer.addTag("InsuranceChengduService.hystrix.fallback", "ERROR");
		return "ERROR";
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = null;
		try {
			taskInsurance = loginRetry(insuranceRequestParameters, 1);
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduService.login---Taskid--",
					insuranceRequestParameters.getTaskId() + eutils.getEDetail(e));
		}
		return taskInsurance;
	}

}
