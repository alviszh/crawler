package app.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheBear;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheHtml;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheInjury;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheMedical;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuohePension;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheUnemployment;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.luohe.InsuranceLuoheBearRepository;
import com.microservice.dao.repository.crawler.insurance.luohe.InsuranceLuoheHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.luohe.InsuranceLuoheInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.luohe.InsuranceLuoheMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.luohe.InsuranceLuohePensionRepository;
import com.microservice.dao.repository.crawler.insurance.luohe.InsuranceLuoheUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.luohe.InsuranceLuoheUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.EUtils;
import app.parser.InsuranceLuoheParser;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.luohe" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.luohe" })
public class InsuranceLuoheService  implements InsuranceLogin{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private InsuranceLuoheParser insuranceLuoheParser;
	@Autowired
	private InsuranceLuoheUserInfoRepository insuranceLuoheUserInfoRepository;
	@Autowired
	private InsuranceLuohePensionRepository insuranceLuohePensionRepository;
	@Autowired
	private InsuranceLuoheMedicalRepository insuranceLuoheMedicalRepository;
	@Autowired
	private InsuranceLuoheInjuryRepository insuranceLuoheInjuryRepository;
	@Autowired
	private InsuranceLuoheBearRepository insuranceLuoheBearRepository;
	@Autowired
	private InsuranceLuoheUnemploymentRepository insuranceLuoheUnemploymentRepository;
	@Autowired
	private InsuranceLuoheHtmlRepository insuranceLuoheHtmlRepository;

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
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.addTag("InsuranceLuoheService.login", insuranceRequestParameters.getTaskId());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			String url = "http://www.halh.lss.gov.cn:9000/loginAction.action?from=&redirect=" + "&username="
					+ insuranceRequestParameters.getUsername() + "&password=" + insuranceRequestParameters.getPassword()
					+ "&phoneNumber=&smsVerificationCode=&loginMode=0";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			Page searchPage = webClient.getPage(webRequest);
			String string = searchPage.getWebResponse().getContentAsString();
			String msg = JSONObject.fromObject(string).getString("message");
			if (msg.equals("登录成功")) {
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setCookies(cookies);
				taskInsurance.setTesthtml("Username:" + insuranceRequestParameters.getUsername() + ";password:"
						+ insuranceRequestParameters.getPassword());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);
			} else {
				tracer.addTag("登录失败:", msg);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setDescription(msg);
				taskInsurance.setTesthtml("Username:" + insuranceRequestParameters.getUsername() + ";password:"
						+ insuranceRequestParameters.getPassword());
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceLuoheService.login---Taskid--", taskInsurance.getTaskid() + eutils.getEDetail(e));
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
			taskInsurance.setTesthtml("Username:" + insuranceRequestParameters.getUsername() + ";password:"
					+ insuranceRequestParameters.getPassword());
			taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}

	/**
	 * 个人信息
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	@Async
	public Future<String> getuserinfo(TaskInsurance taskInsurance) {

		try {
			WebClient webClient = addcookie(taskInsurance.getCookies());

			tracer.addTag("insuranceLuoheService.crawler.getuserinfo", taskInsurance.getTaskid());
			String url = "http://www.halh.lss.gov.cn:9000/grpt/zgbx/zgbxJbxxcxAction001.action";
			Page page = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			// 网页信息
			InsuranceLuoheHtml insuranceLuoheHtml = new InsuranceLuoheHtml(taskInsurance.getTaskid(), "个人信息", 1, url,
					html);
			insuranceLuoheHtmlRepository.save(insuranceLuoheHtml);

			InsuranceLuoheUserInfo insuranceLuoheUserInfo = insuranceLuoheParser.getuserinfo(taskInsurance.getTaskid(),
					html);
			if (null != insuranceLuoheUserInfo) {
				insuranceLuoheUserInfoRepository.save(insuranceLuoheUserInfo);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 404, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoheService.crawler.getuserinfo.error---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 养老保险个人缴费流水
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	@Async
	public Future<String> getPension(TaskInsurance taskInsurance) {
		try {
			WebClient webClient = addcookie(taskInsurance.getCookies());
			tracer.addTag("insuranceLuoheService.crawler.getPension", taskInsurance.getTaskid());
			String url = "http://www.halh.lss.gov.cn:9000/grpt/zgbx/zgbxYlbxjfxxcxAction001.action";
			Page page = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			// 网页信息
			InsuranceLuoheHtml insuranceLuoheHtml = new InsuranceLuoheHtml(taskInsurance.getTaskid(), "养老保险个人缴费", 1,
					url, html);
			insuranceLuoheHtmlRepository.save(insuranceLuoheHtml);

			List<InsuranceLuohePension> list = insuranceLuoheParser.getPension(html, taskInsurance.getTaskid());
			if (list != null) {
				insuranceLuohePensionRepository.saveAll(list);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 404, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoheService.crawler.getPension.error---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 医疗保险个人缴费流水
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	@Async
	public Future<String> getMedical(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("insuranceLuoheService.crawler.getMedical", taskInsurance.getTaskid());
			WebClient webClient = addcookie(taskInsurance.getCookies());
			String url = "http://www.halh.lss.gov.cn:9000/grpt/zgbx/zgbxMlbxjfxxcxAction001.action";
			Page page = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			// 网页信息
			InsuranceLuoheHtml insuranceLuoheHtml = new InsuranceLuoheHtml(taskInsurance.getTaskid(), "医疗保险个人缴费", 1,
					url, html);
			insuranceLuoheHtmlRepository.save(insuranceLuoheHtml);

			List<InsuranceLuoheMedical> list = insuranceLuoheParser.getMedical(taskInsurance.getTaskid(), html);
			if (list != null) {
				insuranceLuoheMedicalRepository.saveAll(list);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 404, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoheService.crawler.getMedical.error---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 工伤保险个人缴费流水
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	@Async
	public Future<String> getInjury(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("insuranceLuoheService.crawler.getInjury", taskInsurance.getTaskid());
			WebClient webClient = addcookie(taskInsurance.getCookies());
			String url = "http://www.halh.lss.gov.cn:9000/grpt/zgbx/zgbxGsbxjfxxcxAction001.action";
			Page page = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			// 网页信息
			InsuranceLuoheHtml insuranceLuoheHtml = new InsuranceLuoheHtml(taskInsurance.getTaskid(), "工伤保险个人缴费", 1,
					url, html);
			insuranceLuoheHtmlRepository.save(insuranceLuoheHtml);

			List<InsuranceLuoheInjury> list = insuranceLuoheParser.getInjury(taskInsurance.getTaskid(), html);
			if (list != null) {
				insuranceLuoheInjuryRepository.saveAll(list);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 404, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoheService.crawler.getInjury.error---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 失业保险个人缴费流水
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	@Async
	public Future<String> getUnemployment(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("insuranceLuoheService.crawler.getUnemployment", taskInsurance.getTaskid());
			WebClient webClient = addcookie(taskInsurance.getCookies());
			String url = "http://www.halh.lss.gov.cn:9000/grpt/zgbx/zgbxSybxjfxxcxAction001.action";
			Page page = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			// 网页信息
			InsuranceLuoheHtml insuranceLuoheHtml = new InsuranceLuoheHtml(taskInsurance.getTaskid(), "失业保险个人缴费", 1,
					url, html);
			insuranceLuoheHtmlRepository.save(insuranceLuoheHtml);

			List<InsuranceLuoheUnemployment> list = insuranceLuoheParser.getUnemployment(taskInsurance.getTaskid(),
					html);
			if (list != null) {
				insuranceLuoheUnemploymentRepository.saveAll(list);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 404, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoheService.crawler.getUnemployment.error---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 生育保险个人缴费流水
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	@Async
	public Future<String> getBear(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("insuranceLuoheService.crawler.getBear", taskInsurance.getTaskid());
			WebClient webClient = addcookie(taskInsurance.getCookies());
			String url = "http://www.halh.lss.gov.cn:9000/grpt/zgbx/zgbxMybxjfxxcxAction001.action";
			Page page = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			// 网页信息
			InsuranceLuoheHtml insuranceLuoheHtml = new InsuranceLuoheHtml(taskInsurance.getTaskid(), "生育保险个人缴费", 1,
					url, html);
			insuranceLuoheHtmlRepository.save(insuranceLuoheHtml);

			List<InsuranceLuoheBear> list = insuranceLuoheParser.getBear(taskInsurance.getTaskid(), html);
			if (list != null) {
				insuranceLuoheBearRepository.saveAll(list);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200,
						taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 404,
						taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoheService.crawler.getBear.error---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 500,
					taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	public WebClient addcookie(String cookieString) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, String taskid, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
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

}
