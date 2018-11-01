package app.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanGeneral;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanHtml;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanInsuranceRecord;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.dongguan.InsuranceDongguanGeneralRepository;
import com.microservice.dao.repository.crawler.insurance.dongguan.InsuranceDongguanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.dongguan.InsuranceDongguanInsuranceRecordRepository;
import com.microservice.dao.repository.crawler.insurance.dongguan.InsuranceDongguanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.exceptiondetail.EUtils;
import app.parser.InsuranceDongguanParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.dongguan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.dongguan" })
public class InsuranceDongguanService implements InsuranceLogin{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private InsuranceDongguanParser insuranceDongguanParser;

	@Autowired
	private InsuranceDongguanUserInfoRepository insuranceDongguanUserInfoRepository;

	@Autowired
	private InsuranceDongguanHtmlRepository insuranceDongguanHtmlRepository;

	@Autowired
	private InsuranceDongguanInsuranceRecordRepository insuranceDongguanInsuranceRecordRepository;

	@Autowired
	private InsuranceDongguanGeneralRepository insuranceDongguanGeneralRepository;

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

		tracer.addTag("InsuranceDongguanService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		if (null != taskInsurance) {

			WebParam webParam = insuranceDongguanParser.login(insuranceRequestParameters);

			if (null == webParam) {
				tracer.addTag("InsuranceDongguanService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			} else {
				String html = webParam.getPage().getWebResponse().getContentAsString();
				tracer.addTag("InsuranceDongguanService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				if (!html.contains("登录失败")) {
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					return taskInsurance;
				} else if (html.contains("不存在")) {
					taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					return taskInsurance;
				} else if (html.contains("密码错误")) {
					taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
					return taskInsurance;
				} else if (html.contains("输入的校验码不正确")) {
					tracer.addTag("InsuranceDongguanService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败次数" + count);
					if (count < 4) {
						loginRetry(insuranceRequestParameters, ++count);
					} else {
						taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
						return taskInsurance;
					}
				} else {
					taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
					return taskInsurance;
				}

			}

		}

		return null;
	}

	/**
	 * 获取Menu
	 * 
	 * @param insuranceRequestParameters
	 */
	public String getMenu(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) {
		tracer.addTag("InsuranceDongguanService.getMenu", insuranceRequestParameters.getTaskId());

		String html = insuranceDongguanParser.getMenu(taskInsurance, taskInsurance.getCookies());
		if (null != html && !"".equals(html)) {
			tracer.addTag("InsuranceDongguanService.getMenu:SUCCESS" + insuranceRequestParameters.getTaskId(),
					"<xmp>" + html + "</xmp>");
			return html;
		}
		return null;

	}

	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	@Async
	public Future<String> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, String htmlUrl,
			TaskInsurance taskInsurance) {
		tracer.addTag("InsuranceDongguanService.getUserinfo", insuranceRequestParameters.getTaskId());

		tracer.addTag("parser.crawler.getUserinfo", insuranceRequestParameters.getTaskId());
		try {
			WebParam<InsuranceDongguanHtml> webParam = insuranceDongguanParser.getUserInfo(taskInsurance, htmlUrl);

			if (null != webParam) {
				insuranceDongguanUserInfoRepository.save(webParam.getInsuranceDongguanUserInfo());

				tracer.addTag("InsuranceDongguanService.getUserInfo 个人信息", "个人信息已入库!");

				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200,
						taskInsurance);

				insuranceDongguanHtmlRepository.saveAll(webParam.getList());
				tracer.addTag("InsuranceDongguanService.getUserInfo 个人信息源码", "个人信息源码表入库!");

			} else {
				tracer.addTag("InsuranceDongguanService.getUserInfo.webParam is null 个人信息", taskInsurance.getTaskid());

				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceDongguanService.getUserInfo---Taskid--"+
					insuranceRequestParameters.getTaskId() , eutils.getEDetail(e));
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");

	}
	
	public TaskInsurance update(String taskId,int code) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskId);
		// 修改五险状态
		// 养老
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),code,
				taskInsurance);
		// 生育
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), code,
				taskInsurance);
		// 工商
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), code,
				taskInsurance);
		// 医疗
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), code,
				taskInsurance);
		// 失业
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), code,
				taskInsurance);
		return null;
	}
	

	/**
	 * 东莞社保参保记录
	 * 
	 * @param insuranceRequestParameters
	 * @param htmlUrl
	 */
	@Async
	public Future<String> getInsuranceRecord(InsuranceRequestParameters insuranceRequestParameters, String htmlUrl,TaskInsurance taskInsurance) {
		tracer.addTag("InsuranceDongguanService.getInsuranceRecord", insuranceRequestParameters.getTaskId());

		WebParam<InsuranceDongguanInsuranceRecord> webParam = insuranceDongguanParser.getInsuranceRecord(taskInsurance,
				htmlUrl);
		if (null != webParam) {
			insuranceDongguanInsuranceRecordRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceDongguanService.getInsuranceRecord东莞社保参保记录", "东莞社保参保记录!");

			tracer.addTag("InsuranceDongguanService.getInsuranceRecord:SUCCESS",
					insuranceRequestParameters.getTaskId());

			InsuranceDongguanHtml insuranceChengduHtml = new InsuranceDongguanHtml(
					insuranceRequestParameters.getTaskId(), "insurance_dongguan_insurancerecord", "1",
					webParam.getUrl(), webParam.getHtml());
			insuranceDongguanHtmlRepository.save(insuranceChengduHtml);

			tracer.addTag("InsuranceDongguanService.getInsuranceRecord东莞社保参保记录", "东莞社保参保记录源码表入库!");
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 东莞社保月缴费情况查询
	 * 
	 * @param insuranceRequestParameters
	 * @param htmlUrl
	 */
	@Async
	public Future<String> getGeneral(InsuranceRequestParameters insuranceRequestParameters, String htmlUrl,
			String yearMonth,TaskInsurance taskInsurance) {
		tracer.addTag("InsuranceDongguanService.getGeneral",
				insuranceRequestParameters.getTaskId() + "------yearMonth:" + yearMonth);
		
		WebParam<InsuranceDongguanGeneral> webParam = insuranceDongguanParser.getGeneral(taskInsurance, htmlUrl,
				yearMonth);
		if (null != webParam) {
			insuranceDongguanGeneralRepository.saveAll(webParam.getList());

			tracer.addTag("InsuranceDongguanService.getGeneral东莞社保月缴费情况查询", "东莞社保月缴费情况查询");

			tracer.addTag("InsuranceDongguanService.getGeneral:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceDongguanHtml insuranceChengduHtml = new InsuranceDongguanHtml(
					insuranceRequestParameters.getTaskId(), "insurance_dongguan_insurancerecord", yearMonth,
					webParam.getUrl(), webParam.getHtml());
			insuranceDongguanHtmlRepository.save(insuranceChengduHtml);
			tracer.addTag("InsuranceDongguanService.getGeneral东莞社保月缴费情况查询", "东莞社保月缴费情况查询源码表入库!");
		} else {
			tracer.addTag("InsuranceDongguanService.getGeneral.webParam is null",
					insuranceRequestParameters.getTaskId());
		}
		return new AsyncResult<String>("200");

	}

//	@HystrixCommand(fallbackMethod = "fallback")
//	public String hystrix() {
//		tracer.addTag("InsuranceDongguanService hystrix", "start");
//		String url = "https://grcx.dgsi.gov.cn/";
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//
//		try {
//			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			HtmlPage page = webClient.getPage(webRequest);
//			int status = page.getWebResponse().getStatusCode();
//			tracer.addTag("hystrix 东莞社保登录页状态码", String.valueOf(status));
//			if (200 == status) {
//				String html = page.getWebResponse().getContentAsString();
//				tracer.addTag("hystrix 东莞社保登录页", html);
//				return "SUCCESS";
//			}
//
//		} catch (Exception e) {
//			tracer.addTag("InsuranceDongguanService.hystrix---ERROR:",
//					eutils.getEDetail(e));
//		}
//		return "ERROR";
//	}

	public String fallback() {
		tracer.addTag("InsuranceDongguanService.hystrix.fallback", "ERROR");
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
			tracer.addTag("InsuranceDongguanService.login---Taskid--",
					insuranceRequestParameters.getTaskId() + eutils.getEDetail(e));
		}
		return taskInsurance;
	}

}
