package app.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yichang.InsuranceYichangGeneral;
import com.microservice.dao.entity.crawler.insurance.yichang.InsuranceYichangHtml;
import com.microservice.dao.entity.crawler.insurance.yichang.InsuranceYichangUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.yichang.InsuranceYichangGeneralRepository;
import com.microservice.dao.repository.crawler.insurance.yichang.InsuranceYichangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.yichang.InsuranceYichangUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceYichangParser;
import app.service.aop.InsuranceLogin;
import app.util.EncryptionUtils;
import app.util.JsEncryption;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.yichang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.yichang" })
public class InsuranceYichangService implements InsuranceLogin{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private InsuranceYichangParser insuranceYichangParser;
	@Autowired
	private InsuranceYichangUserInfoRepository insuranceYichangUserInfoRepository;
	@Autowired
	private InsuranceYichangGeneralRepository insuranceYichangGeneralRepository;
	@Autowired
	private InsuranceYichangHtmlRepository insuranceYichangHtmlRepository;
	
	@Autowired
	private TracerLog tracer;

	@Value("${datasource.key}")
	String key;

	private String url = "http://61.136.223.44/web2/src/personal/Service.asmx/smcxLoginNew";
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceYichangService.login", insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String loginUrl = "http://61.136.223.44/Service.asmx/LoginCheck";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String username = insuranceRequestParameters.getUsername();
			String password = insuranceRequestParameters.getPassword();
			password = password.replace("+", "%2B");
			password = password.replace("&", "%26");
			String nLoginPWD = EncryptionUtils.StringToMd5("smejweb" + username.toUpperCase() + "dzzw321")
					.toUpperCase();
			String pwd = EncryptionUtils.StringToMd5(username + password).toUpperCase();
			String nSfzhm = "";
			try {
				nSfzhm = JsEncryption.encrypted(username, key);
				nSfzhm = EncryptionUtils.Base64(nSfzhm);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("nLoginID", "smejweb"));
			paramsList.add(new NameValuePair("nLoginPWD", nLoginPWD));
			paramsList.add(new NameValuePair("nSfzhm", nSfzhm));
			paramsList.add(new NameValuePair("pwd", pwd));
			paramsList.add(new NameValuePair("phoneNO", ""));
			paramsList.add(new NameValuePair("VerifiCode", ""));
			paramsList.add(new NameValuePair("system", ""));
			paramsList.add(new NameValuePair("versions", ""));
			Page page = getPage(webClient, loginUrl, HttpMethod.POST, paramsList, null, null, null);
			String xmlStr = page.getWebResponse().getContentAsString();
			xmlStr = xmlStr.toLowerCase();
			Document doc = Jsoup.parse(xmlStr);
			String msg = doc.getElementsByTag("Error").text();
			if (msg.contains("登录验证成功")) {
				tracer.addTag("登录验证成功:", msg);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
//				String cookies = CommonUnit.transcookieToJson(webClient);
//				taskInsurance.setCookies(cookies);
//				taskInsurance.setWebdriverHandle(xmlStr);
				// 保存登录成功信息获取token
				taskInsurance.setCookies(xmlStr);
				taskInsurance.setCrawlerHost(nSfzhm);
				taskInsurance.setTesthtml(JSONObject.fromObject(insuranceRequestParameters).toString());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);
			} else {
				tracer.addTag("登录失败:", msg);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setDescription(msg);
				taskInsurance.setTesthtml(JSONObject.fromObject(insuranceRequestParameters).toString());
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登录报错:", e.toString());
			taskInsurance.setCrawlerHost("");
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
			taskInsurance.setTesthtml(JSONObject.fromObject(insuranceRequestParameters).toString());
			taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}

	/**
	 * 更新task表（doing 正在登录状态）
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
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
	 * 获取返回数据
	 * 
	 * @param taskInsurance
	 * @param inType
	 * @return
	 */
	public String getdata(TaskInsurance taskInsurance, String inType) {

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			tracer.addTag("insuranceYichangService.getuserinfo", taskInsurance.getTaskid());

			Document doc = Jsoup.parse(taskInsurance.getCookies());
			Elements tdValue = doc.getElementsByTag("token");
			String token = tdValue.get(0).text().toUpperCase();
			String sfzhm = taskInsurance.getCrawlerHost();
			String time = EncryptionUtils.Base64(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			String loginPWD = EncryptionUtils.StringToMd5(token + time).toUpperCase();

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			// 公积金用户信息 201
			// 公积金明细 202
			// 社保用户信息 101
			// 社保流水 102
			paramsList.add(new NameValuePair("inType", inType));
			paramsList.add(new NameValuePair("BillTime", ""));
			paramsList.add(new NameValuePair("carNO", ""));
			paramsList.add(new NameValuePair("carUnint", ""));
			paramsList.add(new NameValuePair("carVCode", ""));
			paramsList.add(new NameValuePair("loginPhone", ""));
			paramsList.add(new NameValuePair("NOStart", "1"));
			paramsList.add(new NameValuePair("NOEnd", "100000"));
			paramsList.add(new NameValuePair("loginID", "smejweb"));
			paramsList.add(new NameValuePair("loginPWD", loginPWD));
			paramsList.add(new NameValuePair("sfzhm", sfzhm));
			paramsList.add(new NameValuePair("time", time));
			paramsList.add(new NameValuePair("loginType", "1"));

			Page page = getPage(webClient, url, HttpMethod.POST, paramsList, null, null, null);
			String html = page.getWebResponse().getContentAsString();

			return html;
		} catch (Exception e) {
			tracer.addTag("insuranceYichangService.getdata" + url, e.getMessage());
		}
		return null;
	}

	/**
	 * 社保用户信息 101
	 * @param taskInsurance
	 * @param inType
	 * @return
	 */
	@Async
	public Future<String> getInsuranceUserinfo(TaskInsurance taskInsurance, String inType) {

		try {

			tracer.addTag("insuranceYichangService.getInsuranceUserinfo", taskInsurance.getTaskid());
			String html = getdata(taskInsurance, inType);
			// 网页信息
			InsuranceYichangHtml insuranceYichangHtml = new InsuranceYichangHtml(taskInsurance.getTaskid(), "社保个人信息",
					inType, url, html);
			insuranceYichangHtmlRepository.save(insuranceYichangHtml);

			InsuranceYichangUserInfo insuranceYichangUserInfo = insuranceYichangParser
					.getuserinfo(taskInsurance.getTaskid(), html);
			if (null != insuranceYichangUserInfo) {
				insuranceYichangUserInfoRepository.save(insuranceYichangUserInfo);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 404, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceYichangService.getInsuranceUserinfo.error", e.getMessage());
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 社保流水 102
	 * @param taskInsurance
	 * @param inType
	 * @return
	 */
	@Async
	public Future<String> getInsuranceGeneral(TaskInsurance taskInsurance, String inType) {
		try {
			tracer.addTag("insuranceYichangService.getInsuranceGeneral", taskInsurance.getTaskid());
			String html = getdata(taskInsurance, inType);
			// 网页信息
			InsuranceYichangHtml insuranceYichangHtml = new InsuranceYichangHtml(taskInsurance.getTaskid(), "社保流水信息",
					inType, url, html);
			insuranceYichangHtmlRepository.save(insuranceYichangHtml);

			List<InsuranceYichangGeneral> list = insuranceYichangParser.getInsuranceGeneral(html, taskInsurance.getTaskid());
			//养老险标注社保流水信息
			if (list != null) {
				insuranceYichangGeneralRepository.saveAll(list);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 404, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 404, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 404, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 404, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 404, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceYichangService.getInsuranceGeneral.error", e.getMessage());
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 500, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 500, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 500, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 500, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 500, taskInsurance);
		}
		return new AsyncResult<String>("200");
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
	public Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList, String code,
			String body, Map<String, String> map) throws Exception {
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
