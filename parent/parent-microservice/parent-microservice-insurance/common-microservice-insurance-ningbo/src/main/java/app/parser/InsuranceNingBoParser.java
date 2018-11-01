package app.parser;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboBear;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboEndowment;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboHtml;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboHurt;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboLost;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboMedical;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboEndowmentRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.ningbo.InsuranceNingboUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceNingBoParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceNingboHtmlRepository insuranceNingboHtmlRepository;
	@Autowired
	private InsuranceNingboMedicalRepository insuranceNingboMedicalRepository;
	@Autowired
	private InsuranceNingboEndowmentRepository insuranceNingboEndowmentRepository;
	@Autowired
	private InsuranceNingboUserInfoRepository insuranceNingboUserInfoRepository;

	public static String getCurrentDate() {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date date = new Date();
		String currentDate = f.format(date).substring(0, 6);
		return currentDate;
	}

	/**
	 * @param insuranceUserInfo
	 * @Des 登录
	 * @param page
	 * @param code
	 * @throws Exception
	 */
	/**
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	public void login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		String url = "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/index.jsp";
		String url1 = "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-grxx.jsp";
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		// 正在登陆状态
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getDescription(), InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhase(), 200, taskInsurance);
		taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeLoginStatusDoing(taskInsurance);

		tracer.addTag("parser.login.url", url);

		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);

		int status = page.getWebResponse().getStatusCode();

		tracer.addTag("parser.crawler.login", status + "");

		if (200 == status) {

			HtmlImage image = page.getFirstByXPath("//img[@id='yzmJsp']");

			HtmlTextInput cardno = (HtmlTextInput) page.getFirstByXPath("//input[@id='loginid']");

			HtmlPasswordInput perpwd = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='pwd']");

			HtmlTextInput verify = (HtmlTextInput) page.getFirstByXPath("//input[@id='yzm']");

			//HtmlElement sub = (HtmlElement) page.getFirstByXPath("//input[@id='btnLogin']");
			HtmlButtonInput sub = page.querySelector("#btnLogin");

			cardno.setText(insuranceRequestParameters.getUsername());

			perpwd.setText(insuranceRequestParameters.getPassword());

			String code = chaoJiYingOcrService.getVerifycode(image, "4004");

			verify.setText(code);

			HtmlPage loggedPage = sub.click();

			int statusCode = loggedPage.getWebResponse().getStatusCode();

			if (200 == statusCode) {

				// webParam.setCode(statusCode);
				// webParam.setPage(loggedPage);

				String html = loggedPage.getWebResponse().getContentAsString();
				//System.out.println(html);
				WebRequest requestSettings = new WebRequest(new URL(url1), HttpMethod.GET);
				webClient.waitForBackgroundJavaScript(1000);
				HtmlPage page1 = webClient.getPage(requestSettings);
				System.out.println(page1);
				
				DomNode querySelector = loggedPage.querySelector("#errDiv");
				if (querySelector.asText().contains("账号或者密码不正确")) {
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getDescription(), InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getPhase(), 103, taskInsurance);
					tracer.addTag("parser.login.name.pwd", taskInsurance.getTaskid());

				} else if (querySelector.asText().contains("验证码输入错误 ")) {
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription(), InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase(), 103, taskInsurance);
					//insuranceService.changeLoginStatusCaptError(taskInsurance);
					tracer.addTag("parser.login.img", taskInsurance.getTaskid());

				} 
				else if (querySelector.asText().contains("E1001")) {
					
					//taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getDescription());
					//taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getPhase());
					//taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_ERROR.getPhasestatus());
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					tracer.addTag("parser.login.name.pwd", taskInsurance.getTaskid());

				} 
				else if(querySelector.asText().contains("您的账号累计输错密码3次，请30分钟后重新登陆"))
				{
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeLoginStatusException(taskInsurance);
					tracer.addTag("parser.login.out_times", taskInsurance.getTaskid());
				}
				
				else {
					if(querySelector.asText().contains("请妥善保管好您的用户名和  密码，谨防泄露！"))
					{
						//taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
						//taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
						//taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
						taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
						insuranceService.changeLoginStatusSuccess(taskInsurance, loggedPage);
						// 获取个人信息
						htmlParser(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						// 获取医疗流水
						getMedical(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						// 获取养老流水
						getEndowment(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						// 获取页面源码
						getHtml(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						taskInsurance.setShiyeStatus(201);
						taskInsurance.setGongshangStatus(201);
						taskInsurance.setShengyuStatus(201);

						insuranceService.changeCrawlerStatusSuccess(taskInsurance);
					}
					else
					{
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
						taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
						insuranceService.changeLoginStatusSuccess(taskInsurance, loggedPage);
						// 获取个人信息
						htmlParser(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						// 获取医疗流水
						getMedical(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						// 获取养老流水
						getEndowment(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						// 获取页面源码
						getHtml(webClient, taskInsurance, insuranceRequestParameters, url, html, url1);
						taskInsurance.setShiyeStatus(201);
						taskInsurance.setGongshangStatus(201);
						taskInsurance.setShengyuStatus(201);

						insuranceService.changeCrawlerStatusSuccess(taskInsurance);
					}
					
				}

				
				// webParam.setInsuranceNingboEndowment(insuranceEndowment);
				// webParam.setInsuranceNingboUserInfo(insuranceUserInfo);
				// webParam.setInsuranceNingboMedical(insuranceMedical);
				// webParam.setInsuranceNingboHtml(insuranceHtml);
				// return webParam;
			}
		}
		// return null;
	}

	private void getHtml(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters insuranceRequestParameters, String url, String html, String url1)
			throws Exception {
		WebRequest requestSettings = new WebRequest(new URL(url1), HttpMethod.GET);
		webClient.waitForBackgroundJavaScript(2000);
		HtmlPage page1 = webClient.getPage(requestSettings);
		int statusCode = page1.getWebResponse().getStatusCode();
		tracer.addTag("parser.crawler.getHtml", taskInsurance.getTaskid());
		if (200 == statusCode) {
			String asXml = page1.asXml();

			HtmlListItem querySelector = page1.querySelector("#ylbxNew");
			Page page2 = querySelector.click();
			String ss = ((DomNode) page2).asXml();
			InsuranceNingboHtml insuranceNingboHtml = new InsuranceNingboHtml();
			insuranceNingboHtml.setPageCount(1);
			insuranceNingboHtml.setType("userinfo");
			insuranceNingboHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceNingboHtml.setUrl(url + url1);
			insuranceNingboHtml.setHtml(asXml + ss);
			if (null != insuranceNingboHtml) {
				insuranceNingboHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNingboHtmlRepository.save(insuranceNingboHtml);
				tracer.addTag("parser.crawler.getHtml", taskInsurance.getTaskid());
			}
		}

		// insuranceNingboHtmlRepository.save(insuranceNingboHtml);
		// tracer.addTag("getUserInfo ==>", "个人信息源码表入库!");
		// return insuranceNingboHtml;
	}

	private void getEndowment(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters insuranceRequestParameters, String url, String html, String url1)
			throws Exception {
		// 获取并解析养老流水
		WebRequest requestSettings = new WebRequest(new URL(url1), HttpMethod.GET);
		webClient.waitForBackgroundJavaScript(2000);
		HtmlPage page1 = webClient.getPage(requestSettings);
		int statusCode = page1.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			HtmlListItem querySelector = page1.querySelector("#ylbxNew");
			Page page2 = querySelector.click();
			String ss = ((DomNode) page2).asXml();
			Document document2 = Jsoup.parse(ss);
			Element endowmentTableData = document2.getElementById("mytable");
			Elements elements2 = endowmentTableData.getElementsByTag("tr");

			List<InsuranceNingboEndowment> endowments = new ArrayList<InsuranceNingboEndowment>();
			for (int i = 1; i < elements2.size(); i++) {
				Element element = elements2.get(i);
				Elements allElements = element.select("td");
				InsuranceNingboEndowment endowment = new InsuranceNingboEndowment();
				List<String> list = new ArrayList<String>();
				for (Element element2 : allElements) {
					list.add(element2.text());
					//System.out.println(element2.text());
				}
				endowment.setPayDate(list.get(0));
				endowment.setInsuranceBase(list.get(1));
				endowment.setPersonMoney(list.get(2));
				endowment.setGetStatus(list.get(3));
				if (null != endowment) {
					endowment.setTaskid(taskInsurance.getTaskid());
					endowments.add(endowment);
				}

			}
			if (null != endowments) {
				insuranceNingboEndowmentRepository.saveAll(endowments);
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhase(), 200, taskInsurance);
				tracer.addTag("parser.crawler.savePension", taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.savePension.ERROR", taskInsurance.getTaskid());
			}
		}

	}

	// 医疗流水
	private void getMedical(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters insuranceRequestParameters, String url, String html, String url1)
			throws Exception {
		// 获取并解析医疗流水
		WebRequest requestSettings = new WebRequest(new URL(url1), HttpMethod.GET);
		webClient.waitForBackgroundJavaScript(1000);
		HtmlPage page1 = webClient.getPage(requestSettings);
		int status = page1.getWebResponse().getStatusCode();
		tracer.addTag("parser.crawler.getMedical", status + "");

		if (200 == status) {
			HtmlListItem queryMedical = page1.querySelector("#yilbxNew");
			Page medicalPage = queryMedical.click();
			String asXml2 = ((DomNode) medicalPage).asXml();
			Document document3 = Jsoup.parse(asXml2);
			Element medicalTableData = document3.getElementById("mytable");
			Elements elements = medicalTableData.getElementsByTag("tr");

			List<InsuranceNingboMedical> medicals = new ArrayList<InsuranceNingboMedical>();
			for (int i = 1; i < elements.size(); i++) {
				Element element = elements.get(i);
				Elements allElements = element.select("td");
				InsuranceNingboMedical medical = new InsuranceNingboMedical();
				List<String> list = new ArrayList<String>();
				for (Element element2 : allElements) {
					list.add(element2.text());
				}
				medical.setPayDate(list.get(0));
				medical.setInsuranceBase(list.get(1));
				medical.setPersonMoney(list.get(2));
				medical.setGetStatus(list.get(3));
				if (null != medical) {
					medical.setTaskid(taskInsurance.getTaskid());
					medicals.add(medical);
				}
			}
			if (null != medicals) {
				insuranceNingboMedicalRepository.saveAll(medicals);
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhase(), 200, taskInsurance);
				tracer.addTag("parser.crawler.saveNingboMedical", taskInsurance.getTaskid());
			} else {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.saveNingboMedical.ERROR", taskInsurance.getTaskid());
			}

		}

		// return medicals;
	}

	/**
	 * @Des 获取个人信息1
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 *//*
		 * public WebParam getUserInfo(TaskInsurance taskInsurance, Set<Cookie>
		 * cookies) throws Exception {
		 * 
		 * WebParam webParam = new WebParam();
		 * 
		 * String url =
		 * "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-grxx.jsp";
		 * // String url2 = //
		 * "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-ylbx.jsp";
		 * tracer.addTag("InsuranceNingBoParser getInfo  url", url); WebClient
		 * webClient = insuranceService.getWebClient(cookies);
		 * 
		 * WebRequest requestSettings = new WebRequest(new URL(url),
		 * HttpMethod.GET); webClient.waitForBackgroundJavaScript(30000);
		 * 
		 * // WebClient webClient1 = insuranceService.getWebClient(cookies); //
		 * WebRequest requestSettings2 = new WebRequest(new URL(url2), //
		 * HttpMethod.GET); // webClient1.waitForBackgroundJavaScript(30000);
		 * 
		 * HtmlPage page = webClient.getPage(requestSettings);
		 * webClient.waitForBackgroundJavaScript(30000);
		 * 
		 * // System.out.println(page.asXml());
		 * 
		 * // HtmlPage page2 = webClient1.getPage(requestSettings2); //
		 * webClient1.waitForBackgroundJavaScript(30000);
		 * 
		 * int statusCode = page.getWebResponse().getStatusCode(); // int
		 * statusCode2 = page2.getWebResponse().getStatusCode();
		 * 
		 * if (200 == statusCode) { String html =
		 * page.getWebResponse().getContentAsString(); // System.out.println(
		 * "---------------------6666666666666666------------------"); //
		 * System.out.println(html); // String html2 =
		 * page2.getWebResponse().getContentAsString(); InsuranceNingboUserInfo
		 * insuranceNingboUserInfo = htmlParser(webClient, html, url,
		 * taskInsurance); // List<InsuranceNingboEndowment>
		 * insuranceNingboEndowment = // getEndowmentHtml(html2, taskInsurance,
		 * url2);
		 * 
		 * webParam.setCode(statusCode);
		 * webParam.setInsuranceNingboUserInfo(insuranceNingboUserInfo); //
		 * webParam.setInsuranceNingboEndowment(insuranceNingboEndowment);
		 * webParam.setPage(page);
		 * 
		 * InsuranceNingboHtml insuranceNingboHtml1 = new InsuranceNingboHtml();
		 * insuranceNingboHtml1.setTaskid(taskInsurance.getTaskid());
		 * insuranceNingboHtml1.setHtml(html);
		 * insuranceNingboHtml1.setType("个人信息");
		 * insuranceNingboHtml1.setUrl(url);
		 * insuranceNingboHtml1.setPageCount(0);
		 * webParam.setInsuranceNingboHtml(insuranceNingboHtml1);
		 * tracer.addTag("InsuranceNingboParser.getInfoData.html", "<xmp>" +
		 * html + "<xmp>");
		 * 
		 * return webParam; } return null; }
		 */

	/**
	 * @Des 获养老流水
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	// public WebParam getEndowmentHtml(String page1) throws Exception {
	// WebParam webParam = new WebParam();

	// String url2 =
	// "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-ylbx.jsp";

	// WebClient webClient = insuranceService.getWebClient(cookies);
	// webClient.waitForBackgroundJavaScript(30000);
	// WebRequest requestSettings2 = new WebRequest(new URL(url2),
	// HttpMethod.POST);
	// HtmlPage page2 = webClient.getPage(requestSettings2);
	// int statusCode2 = page2.getWebResponse().getStatusCode();

	/*
	 * if (200 == statusCode2) { String html2 =
	 * page2.getWebResponse().getContentAsString();
	 * 
	 * List<InsuranceNingboEndowment> insuranceNingboEndowment =
	 * getEndowmentHtml(html2, taskInsurance, url2);
	 * webParam.setInsuranceNingboEndowment(insuranceNingboEndowment);
	 * 
	 * InsuranceNingboHtml insuranceNingboHtml2 = new InsuranceNingboHtml();
	 * insuranceNingboHtml2.setTaskid(taskInsurance.getTaskid());
	 * insuranceNingboHtml2.setHtml(html2);
	 * insuranceNingboHtml2.setType("养老流水"); insuranceNingboHtml2.setUrl(url2);
	 * insuranceNingboHtml2.setPageCount(0);
	 * webParam.setInsuranceNingboHtml(insuranceNingboHtml2);
	 * tracer.addTag("InsuranceNingboParser.getMedicalData.html", "<xmp>" +
	 * html2 + "<xmp>");
	 * 
	 * return webParam;
	 * 
	 * }
	 */
	// return null;
	/// }

	/**
	 * @Des 获取医疗流水
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	/*
	 * public WebParam getMedicalHtml(TaskInsurance taskInsurance, Set<Cookie>
	 * cookies) throws Exception {
	 * 
	 * WebParam webParam = new WebParam();
	 * 
	 * String url3 =
	 * "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-yilbx.jsp";
	 * 
	 * WebClient webClient = insuranceService.getWebClient(cookies);
	 * webClient.waitForBackgroundJavaScript(30000); WebRequest requestSettings3
	 * = new WebRequest(new URL(url3), HttpMethod.POST); HtmlPage page3 =
	 * webClient.getPage(requestSettings3); int statusCode3 =
	 * page3.getWebResponse().getStatusCode();
	 * 
	 * if (200 == statusCode3) { String html3 =
	 * page3.getWebResponse().getContentAsString(); List<InsuranceNingboMedical>
	 * insuranceNingboMedical = getMedicalHtml(html3, taskInsurance, url3);
	 * webParam.setInsuranceNingboMedical(insuranceNingboMedical);
	 * 
	 * InsuranceNingboHtml insuranceNingboHtml3 = new InsuranceNingboHtml();
	 * insuranceNingboHtml3.setTaskid(taskInsurance.getTaskid());
	 * insuranceNingboHtml3.setHtml(html3);
	 * insuranceNingboHtml3.setType("医疗流水"); insuranceNingboHtml3.setUrl(url3);
	 * insuranceNingboHtml3.setPageCount(0);
	 * webParam.setInsuranceNingboHtml(insuranceNingboHtml3);
	 * tracer.addTag("InsuranceNingboParser.getMedicalData.html", "<xmp>" +
	 * html3 + "<xmp>");
	 * 
	 * return webParam; } return null; }
	 */

	/**
	 * 解析养老流水
	 * 
	 * @param html
	 * @param taskInsurance
	 * @param url2
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	/*
	 * private List<InsuranceNingboEndowment> getEndowmentHtml(String html2,
	 * TaskInsurance taskInsurance, String url2) throws
	 * FailingHttpStatusCodeException, MalformedURLException, IOException {
	 * List<InsuranceNingboEndowment> insuranceNingboEndowment = new
	 * ArrayList<InsuranceNingboEndowment>(); Document document =
	 * Jsoup.parse(html2); Element tabledata =
	 * document.getElementById("mytable"); Elements elements =
	 * tabledata.getElementsByTag("tr"); List<InsuranceNingboEndowment>
	 * endowments = new ArrayList<InsuranceNingboEndowment>(); for (int i = 1; i
	 * < elements.size(); i++) { Element element = elements.get(i); Elements
	 * allElements = element.select("td"); InsuranceNingboEndowment endowment =
	 * new InsuranceNingboEndowment(); List<String> list = new
	 * ArrayList<String>(); for (Element element2 : allElements) {
	 * list.add(element2.text()); System.out.println(element2.text()); }
	 * endowment.setPayDate(list.get(0));
	 * endowment.setInsuranceBase(list.get(1));
	 * endowment.setPersonMoney(list.get(2));
	 * endowment.setGetStatus(list.get(3));
	 * endowment.setTaskid(taskInsurance.getTaskid());
	 * 
	 * endowments.add(endowment); }
	 * tracer.addTag("InsuranceNingboEndowment getEndowmentHtml  status",
	 * endowments.toString());
	 * 
	 * return insuranceNingboEndowment; }
	 */

	/**
	 * 解析医疗流水
	 * 
	 * @param html
	 * @param taskInsurance
	 * @param url3
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	/*
	 * private List<InsuranceNingboMedical> getMedicalHtml(String html3,
	 * TaskInsurance taskInsurance, String url3) throws
	 * FailingHttpStatusCodeException, MalformedURLException, IOException {
	 * List<InsuranceNingboMedical> insuranceNingboMedical = new
	 * ArrayList<InsuranceNingboMedical>(); Document document =
	 * Jsoup.parse(html3); Element tabledata =
	 * document.getElementById("mytable"); Elements elements =
	 * tabledata.getElementsByTag("tr"); List<InsuranceNingboMedical> medicals =
	 * new ArrayList<InsuranceNingboMedical>(); for (int i = 1; i <
	 * elements.size(); i++) { Element element = elements.get(i); Elements
	 * allElements = element.select("td"); InsuranceNingboMedical medical = new
	 * InsuranceNingboMedical(); List<String> list = new ArrayList<String>();
	 * for (Element element2 : allElements) { list.add(element2.text());
	 * System.out.println(element2.text()); } medical.setPayDate(list.get(0));
	 * medical.setInsuranceBase(list.get(1));
	 * medical.setPersonMoney(list.get(2)); medical.setGetStatus(list.get(3));
	 * medical.setTaskid(taskInsurance.getTaskid());
	 * 
	 * medicals.add(medical); }
	 * tracer.addTag("InsuranceNingboMedical getMedicalHtml  status",
	 * medicals.toString());
	 * 
	 * return insuranceNingboMedical; }
	 */

	/**
	 * 
	 * @Des 解析个人信息之公司
	 * @throws FailingHttpStatusCodeException
	 * @throws IOException
	 *//*
		 * private InsuranceNingboEndowment htmlCompanyParser(WebClient
		 * webClient,TaskInsurance taskInsurance,String url) throws
		 * FailingHttpStatusCodeException, IOException{ HtmlPage searchPage1 =
		 * webClient.getPage(url); // System.out.println(searchPage1);
		 * webClient.waitForBackgroundJavaScript(30000);
		 * 
		 * // HtmlListItem querySelector =
		 * searchPage1.querySelector("#ylbxNew"); //
		 * webClient.waitForBackgroundJavaScript(50000); // searchPage1 =
		 * querySelector.click();
		 * 
		 * System.out.println(searchPage1.asXml());
		 * 
		 * String ss = searchPage1.asXml(); Document doc = Jsoup.parse(ss);
		 * String unit =
		 * doc.getElementsByTag("td").get(2).toString().substring(10, 22);//单位名称
		 * InsuranceNingboEndowment insuranceNingboEndowment = new
		 * InsuranceNingboEndowment(); insuranceNingboEndowment.setUnit(unit);
		 * System.out.println(unit);
		 * 
		 * return insuranceNingboEndowment; }
		 */

	/**
	 * @Des 解析个人信息
	 * @param html
	 * @throws IOException
	 * @throws FailingHttpStatusCodeException
	 */
	private void htmlParser(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters insuranceRequestParameters, String url, String html, String url1)
			throws Exception {
		// 获取并解析个人信息

		insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		WebRequest requestSettings = new WebRequest(new URL(url1), HttpMethod.GET);
		webClient.waitForBackgroundJavaScript(2000);
		HtmlPage page1 = webClient.getPage(requestSettings);

		int statusCode = page1.getWebResponse().getStatusCode();
		tracer.addTag("parser.crawler.getstatusCode", statusCode + "");
		if (200 == statusCode) {
			String asXml = page1.asXml();
			// System.out.println(page1.asXml());

			Document document = Jsoup.parse(asXml);

			HtmlListItem querySelector = page1.querySelector("#ylbxNew");
			Page page2 = querySelector.click();
			String ss = ((DomNode) page2).asXml();
			Document document2 = Jsoup.parse(ss);

			String canbaoStatus = document2.getElementsByTag("td").get(3).toString().substring(10, 15); // 参保状态
			String unit = document2.getElementsByTag("td").get(2).toString().substring(10, 22);// 单位名称
			String name = document.getElementById("xm").text();
			String sex = document.getElementById("xb").text();
			String cardNum = document.getElementById("sfz").text();
			String national = document.getElementById("gj").text();
			String insuranceNum = document.getElementById("sbkh").text();
			String cardStatus = document.getElementById("kzt").text();
			String cardMoney = document.getElementById("yhkh").text();
			String cardDate = document.getElementById("fkrq").text();
			String phoneNum = document.getElementById("sjhm").text();
			String landlineNum = document.getElementById("gddh").text();
			String addr = document.getElementById("czdz").text();
			String postalcode = document.getElementById("yzbm").text();

			InsuranceNingboUserInfo insuranceNingboInfo = new InsuranceNingboUserInfo();
			insuranceNingboInfo.setCanbaoStatus(canbaoStatus);
			insuranceNingboInfo.setUnit(unit);
			insuranceNingboInfo.setName(name);
			insuranceNingboInfo.setSex(sex);
			insuranceNingboInfo.setCardNum(cardNum);
			insuranceNingboInfo.setNational(national);
			insuranceNingboInfo.setInsuranceNum(insuranceNum);
			insuranceNingboInfo.setCardStatus(cardStatus);
			insuranceNingboInfo.setCardMoney(cardMoney);
			insuranceNingboInfo.setCardDate(cardDate);
			insuranceNingboInfo.setPhoneNum(phoneNum);
			insuranceNingboInfo.setPhoneNum(phoneNum);
			insuranceNingboInfo.setLandlineNum(landlineNum);
			insuranceNingboInfo.setAddr(addr);
			insuranceNingboInfo.setPostalcode(postalcode);

			if (null != insuranceNingboInfo) {

				insuranceNingboInfo.setTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhase(), 200, taskInsurance);
				tracer.addTag("parser.crawler.saveUserInfo", taskInsurance.getTaskid());
				insuranceNingboUserInfoRepository.save(insuranceNingboInfo);
			} else {
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase(), 201, taskInsurance);
				tracer.addTag("parser.crawler.saveUserInfo.ERROR", taskInsurance.getTaskid());
			}
		}

		// tracer.addTag("InsuranceNingboParser.getInfoData.html", "<xmp>" +
		// html + "<xmp>");
		// System.out.println(insuranceNingboInfo);
		// insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 200);

		// return insuranceNingboInfo;
	}

	public WebParam loginlogin(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		String url="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/index.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		HtmlTextInput loginid = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginid']");
		loginid.reset();
		loginid.setText(insuranceRequestParameters.getUsername());
		
		HtmlPasswordInput pwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='pwd']");
		pwd.reset();
		pwd.setText(insuranceRequestParameters.getPassword());
		
		HtmlImage img = page.getFirstByXPath("//img[@id='yzmJsp']");
		
		//String imageName = "111.jpg";
		//File file = new File("D:\\img\\" + imageName);
		//img.saveAs(file); 
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "4004");
		
		//String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzm']");
		yzm.reset();
		yzm.setText(verifycode);
		
		HtmlElement button = page.getFirstByXPath("//input[@id='btnLogin']");
		HtmlPage page2 = button.click();
//		String string = page2.getWebResponse().getContentAsString();
//		System.out.println(string);
		WebClient webClient2 = page2.getWebClient();
		
		String imgurl="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/comm/checkYzm.jsp?yzm="+verifycode+"&client=NBHRSS_WEB";
		Page page4 = webClient2.getPage(imgurl);
		
		String loginurl="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/rzxt/nbhrssLogin.parser?id="+insuranceRequestParameters.getUsername()+"&password="+insuranceRequestParameters.getUsername()+"&phone=&client=NBHRSS_WEB";
		Page page5 = webClient2.getPage(loginurl);
		
        String url2="https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/query/query-grxx.jsp";
        HtmlPage page3 = webClient2.getPage(url2);
        WebParam webParam = new WebParam();
        if(null != page3)
        {
        	int code = page3.getWebResponse().getStatusCode();
        	if(code==200)
        	{
        		webParam.setCode(code);
        		webParam.setHtml(page3.getWebResponse().getContentAsString());
        		webParam.setWebClient(webClient2);
        		webParam.setPage(page3);
        		webParam.setUrl(insuranceRequestParameters.getPassword());
        		return webParam;
        	}
        }
		return null;
	}

	
	//个人信息
	public WebParam<InsuranceNingboUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<InsuranceNingboUserInfo> webParam = new WebParam<InsuranceNingboUserInfo>();
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string = null;
		for (Cookie cookie : cookies) {
			webClient2.getCookieManager().addCookie(cookie);
			if(cookie.getName().equals("__rz__k"))
			{
				string = cookie.getValue();
			}
		}
       
        String url3="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery112407393386748973498_1508834036437&access_token="+string+"&api=10S005&bustype=01&refresh=true&param=%7B%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
        Page page6 = webClient2.getPage(url3);
        
        
        String ylurl1="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery1124005830392679333207_1508917573491&access_token="+string+"&api=91S001&bustype=01&refresh=true&param=%7B%22AAB301%22%3A%22330282%22%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
		Page page = webClient2.getPage(ylurl1);
		String asString = page.getWebResponse().getContentAsString();
	    int t = asString.indexOf("(");
        String substring3 = asString.substring(t+1, asString.length()-2);
        JSONObject ylfromObject3 = JSONObject.fromObject(substring3);
        String ylstring3 = ylfromObject3.getString("result");
        JSONObject fromObject3 = JSONObject.fromObject(ylstring3);
        
        
        
        if(null != page6)
        {
        	int code = page6.getWebResponse().getStatusCode();
        	if(code==200)
        	{
        		 String string2 = page6.getWebResponse().getContentAsString();
                 int i = string2.indexOf("(");
                 String string3 = string2.substring(i+1,string2.length()-2);
                 String replaceAll = string3.replaceAll("\\\\", "");
                 String substring = replaceAll.substring(11,replaceAll.length()-12);
                 JSONObject fromObject2 = JSONObject.fromObject(substring);
                 
                 InsuranceNingboUserInfo insuranceNingboInfo = new InsuranceNingboUserInfo();
                 insuranceNingboInfo.setCanbaoStatus(fromObject3.getString("AAC008"));//参保状态
                 insuranceNingboInfo.setName(fromObject3.getString("AAC003"));//姓名
                 insuranceNingboInfo.setUnit(fromObject3.getString("AAB004"));//公司
                 insuranceNingboInfo.setCardNum(fromObject3.getString("AAC002"));//证件号
                 
                 insuranceNingboInfo.setInsuranceNum(fromObject2.getString("AAZ500"));//社保卡号
                 insuranceNingboInfo.setCardMoney(fromObject2.getString("AAE010"));//银行卡号
                 insuranceNingboInfo.setNational(fromObject2.getString("AZA103"));//国籍
                 insuranceNingboInfo.setCardDate(fromObject2.getString("AAZ503"));//开卡日期
                 String sex =null;
                 if(fromObject2.getString("AAC004").equals("1"))
                 {
                 	 sex = "男";
                 }
                 else{
                 	 sex ="女";
                 }
                 insuranceNingboInfo.setSex(sex);//性别
                 insuranceNingboInfo.setPostalcode(fromObject2.getString("AAZ220"));//邮政编码
                 insuranceNingboInfo.setPhoneNum(fromObject2.getString("AAE004"));//手机号
                 String status=null;
                 if(fromObject2.getString("AAZ502").equals("1"))
                 {
                 	status="正常有卡状态";
                 }
                 else if(fromObject2.getString("AAZ502").equals("4"))
                 {
                 	status="临时挂失状态";
                 }
                 else if(fromObject2.getString("AAZ502").equals("2"))
                 {
                 	status="正式挂失状态";
                 }
                 
                 insuranceNingboInfo.setCardStatus(status);//社保卡状态
                 insuranceNingboInfo.setAddr(fromObject2.getString("AAE006"));//地址
                 insuranceNingboInfo.setLandlineNum(fromObject2.getString("AAE005"));//固定号码
                 
                 insuranceNingboInfo.setTaskid(taskInsurance.getTaskid());
                 webParam.setCode(code);
                 webParam.setUrl(url3);
                 webParam.setHtml(page6.getWebResponse().getContentAsString());
                 webParam.setInsuranceNingboUserInfo(insuranceNingboInfo);
                 return webParam;
        	}
        }
		return null;
	}

	
	//医疗保险
	public WebParam<InsuranceNingboMedical> getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<InsuranceNingboMedical> webParam = new WebParam<InsuranceNingboMedical>();
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string = null;
		for (Cookie cookie : cookies) {
			webClient2.getCookieManager().addCookie(cookie);
			if(cookie.getName().equals("__rz__k"))
			{
				string = cookie.getValue();
			}
		}
		String url ="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery1124029196725603637286_1508918506421&access_token="+string+"&api=91S012&bustype=01&refresh=true&param=%7B%22AAB301%22%3A%22330282%22%2C%22PAGENO%22%3A1%2C%22PAGESIZE%22%3A100%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
		
		Page page7 = webClient2.getPage(url);
        if(null != page7)
        {
        	int code = page7.getWebResponse().getStatusCode();
        	if(code==200)
        	{
        		//System.out.println(page7.getWebResponse().getContentAsString());
                String ylstring = page7.getWebResponse().getContentAsString();
                if(ylstring.contains("COSTLIST"))
                {
                	int j = ylstring.indexOf("(");
                    String substring2 = ylstring.substring(j+1, ylstring.length()-2);
                    JSONObject ylfromObject = JSONObject.fromObject(substring2);
                    String ylstring4 = ylfromObject.getString("result");
                    JSONObject fromObject = JSONObject.fromObject(ylstring4);
                    String string4 = fromObject.getString("COSTLIST");
                    JSONObject fromObject3 = JSONObject.fromObject(string4);
                    System.out.println(fromObject3);
                    String string5 = fromObject3.getString("COST");
                    JSONArray fromObject4 = JSONArray.fromObject(string5);
                    System.out.println(fromObject4);
                    InsuranceNingboMedical insuranceNingboMedical=null;
                    List<InsuranceNingboMedical> list = new ArrayList<InsuranceNingboMedical>();
                    for (int k = 0; k < fromObject4.size(); k++) {
                    	insuranceNingboMedical = new InsuranceNingboMedical();
            			JSONObject fromObject5 = JSONObject.fromObject(fromObject4.get(k));
            			insuranceNingboMedical.setPayDate(fromObject5.getString("AAE002"));
            			insuranceNingboMedical.setInsuranceBase(fromObject5.getString("AAE180"));
            			insuranceNingboMedical.setPersonMoney(fromObject5.getString("AAE022"));
            			insuranceNingboMedical.setGetStatus(fromObject5.getString("AAE078"));
            			insuranceNingboMedical.setTaskid(taskInsurance.getTaskid());
            			list.add(insuranceNingboMedical);
            		}
                    webParam.setList(list);
                    webParam.setUrl(url);
                    webParam.setHtml(page7.getWebResponse().getContentAsString());
                    return webParam;
                }
        	}
        }
        return null;
	}

	
	//养老保险
	public WebParam<InsuranceNingboEndowment> getEndowmentInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<InsuranceNingboEndowment> webParam = new WebParam<InsuranceNingboEndowment>();
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string = null;
		for (Cookie cookie : cookies) {
			webClient2.getCookieManager().addCookie(cookie);
			if(cookie.getName().equals("__rz__k"))
			{
				string = cookie.getValue();
			}
		}
		
		String ylurl="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery112402946609875493056_1508899402245&access_token="+string+"&api=91S012&bustype=01&refresh=true&param=%7B%22AAB301%22%3A%22330282%22%2C%22PAGENO%22%3A1%2C%22PAGESIZE%22%3A100%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
        Page page7 = webClient2.getPage(ylurl);
        if(null != page7)
        {
        	int code = page7.getWebResponse().getStatusCode();
        	if(code==200)
        	{
        		//System.out.println(page7.getWebResponse().getContentAsString());
                String ylstring = page7.getWebResponse().getContentAsString();
                if(ylstring.contains("COSTLIST"))
                {
                	int j = ylstring.indexOf("(");
                    String substring2 = ylstring.substring(j+1, ylstring.length()-2);
                    JSONObject ylfromObject = JSONObject.fromObject(substring2);
                    String ylstring4 = ylfromObject.getString("result");
                    JSONObject fromObject = JSONObject.fromObject(ylstring4);
                    String string4 = fromObject.getString("COSTLIST");
                    JSONObject fromObject3 = JSONObject.fromObject(string4);
                    System.out.println(fromObject3);
                    String string5 = fromObject3.getString("COST");
                    JSONArray fromObject4 = JSONArray.fromObject(string5);
                    System.out.println(fromObject4);
                    InsuranceNingboEndowment insuranceNingboEndowment = null;
                    List<InsuranceNingboEndowment> list = new ArrayList<InsuranceNingboEndowment>();
                    for (int k = 0; k < fromObject4.size(); k++) {
                    	insuranceNingboEndowment = new InsuranceNingboEndowment();
            			JSONObject fromObject5 = JSONObject.fromObject(fromObject4.get(k));
            			insuranceNingboEndowment.setPayDate(fromObject5.getString("AAE002"));
            			insuranceNingboEndowment.setInsuranceBase(fromObject5.getString("AAE180"));
            			insuranceNingboEndowment.setPersonMoney(fromObject5.getString("AAE022"));
            			insuranceNingboEndowment.setGetStatus(fromObject5.getString("AAE078"));
            			insuranceNingboEndowment.setTaskid(taskInsurance.getTaskid());
            			list.add(insuranceNingboEndowment);
            		}
                    webParam.setList(list);
                    webParam.setUrl(ylurl);
                    webParam.setHtml(page7.getWebResponse().getContentAsString());
                    return webParam;
                }
        	}
        }
        return null;
		
	}

	
	//工伤保险 
	public WebParam<InsuranceNingboHurt> getHurtInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<InsuranceNingboHurt> webParam = new WebParam<InsuranceNingboHurt>();
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string = null;
		for (Cookie cookie : cookies) {
			webClient2.getCookieManager().addCookie(cookie);
			if(cookie.getName().equals("__rz__k"))
			{
				string = cookie.getValue();
			}
		}
		
		String ylurl="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery112402946609875493056_1508899402245&access_token="+string+"&api=91S018&bustype=01&refresh=true&param=%7B%22AAB301%22%3A%22330282%22%2C%22PAGENO%22%3A1%2C%22PAGESIZE%22%3A100%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
        Page page7 = webClient2.getPage(ylurl);
        if(null != page7)
        {
        	int code = page7.getWebResponse().getStatusCode();
        	if(code==200)
        	{
        		//System.out.println(page7.getWebResponse().getContentAsString());
                String ylstring = page7.getWebResponse().getContentAsString();
                if(ylstring.contains("COSTLIST"))
                {
                	  int j = ylstring.indexOf("(");
                      String substring2 = ylstring.substring(j+1, ylstring.length()-2);
                      JSONObject ylfromObject = JSONObject.fromObject(substring2);
                      String ylstring4 = ylfromObject.getString("result");
                      JSONObject fromObject = JSONObject.fromObject(ylstring4);
                      String string4 = fromObject.getString("COSTLIST");
                      JSONObject fromObject3 = JSONObject.fromObject(string4);
                      System.out.println(fromObject3);
                      String string5 = fromObject3.getString("COST");
                      JSONArray fromObject4 = JSONArray.fromObject(string5);
                      System.out.println(fromObject4);
                      InsuranceNingboHurt insuranceNingboHurt = null;
                      List<InsuranceNingboHurt> list = new ArrayList<InsuranceNingboHurt>();
                      for (int k = 0; k < fromObject4.size(); k++) {
                      	insuranceNingboHurt = new InsuranceNingboHurt();
              			JSONObject fromObject5 = JSONObject.fromObject(fromObject4.get(k));
              			insuranceNingboHurt.setPayDate(fromObject5.getString("AAE002"));
              			insuranceNingboHurt.setInsuranceBase(fromObject5.getString("AAE180"));
              			insuranceNingboHurt.setPersonMoney(fromObject5.getString("AAE022"));
              			insuranceNingboHurt.setGetStatus(fromObject5.getString("AAE078"));
              			insuranceNingboHurt.setTaskid(taskInsurance.getTaskid());
              			list.add(insuranceNingboHurt);
              		}
                      webParam.setList(list);
                      webParam.setUrl(ylurl);
                      webParam.setHtml(page7.getWebResponse().getContentAsString());
                      return webParam;
                }
        	}
        }
        return null;
		
	}

	//生育保险  
	public WebParam<InsuranceNingboBear> getBearInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<InsuranceNingboBear> webParam = new WebParam<InsuranceNingboBear>();
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string = null;
		for (Cookie cookie : cookies) {
			webClient2.getCookieManager().addCookie(cookie);
			if(cookie.getName().equals("__rz__k"))
			{
				string = cookie.getValue();
			}
		}
		
		String ylurl="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery112402946609875493056_1508899402245&access_token="+string+"&api=91S019&bustype=01&refresh=true&param=%7B%22AAB301%22%3A%22330282%22%2C%22PAGENO%22%3A1%2C%22PAGESIZE%22%3A100%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
        Page page7 = webClient2.getPage(ylurl);
        if(null != page7)
        {
        	int code = page7.getWebResponse().getStatusCode();
        	if(code==200)
        	{
        		//System.out.println(page7.getWebResponse().getContentAsString());
                String ylstring = page7.getWebResponse().getContentAsString();
                if(ylstring.contains("COSTLIST"))
                {
                	 int j = ylstring.indexOf("(");
                     String substring2 = ylstring.substring(j+1, ylstring.length()-2);
                     JSONObject ylfromObject = JSONObject.fromObject(substring2);
                     String ylstring4 = ylfromObject.getString("result");
                     JSONObject fromObject = JSONObject.fromObject(ylstring4);
                     String string4 = fromObject.getString("COSTLIST");
                     JSONObject fromObject3 = JSONObject.fromObject(string4);
                     System.out.println(fromObject3);
                     String string5 = fromObject3.getString("COST");
                     JSONArray fromObject4 = JSONArray.fromObject(string5);
                     System.out.println(fromObject4);
                     InsuranceNingboBear insuranceNingboBear = null;
                     List<InsuranceNingboBear> list = new ArrayList<InsuranceNingboBear>();
                     for (int k = 0; k < fromObject4.size(); k++) {
                     	insuranceNingboBear = new InsuranceNingboBear();
             			JSONObject fromObject5 = JSONObject.fromObject(fromObject4.get(k));
             			insuranceNingboBear.setPayDate(fromObject5.getString("AAE002"));
             			insuranceNingboBear.setInsuranceBase(fromObject5.getString("AAE180"));
             			insuranceNingboBear.setPersonMoney(fromObject5.getString("AAE022"));
             			insuranceNingboBear.setGetStatus(fromObject5.getString("AAE078"));
             			insuranceNingboBear.setTaskid(taskInsurance.getTaskid());
             			list.add(insuranceNingboBear);
             		}
                     webParam.setList(list);
                     webParam.setUrl(ylurl);
                     webParam.setHtml(page7.getWebResponse().getContentAsString());
                     return webParam;
                }
        	}
        }
        return null;
		
	}

	//失业保险
	public WebParam<InsuranceNingboLost> getLostInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<InsuranceNingboLost> webParam = new WebParam<InsuranceNingboLost>();
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string = null;
		for (Cookie cookie : cookies) {
			webClient2.getCookieManager().addCookie(cookie);
			if(cookie.getName().equals("__rz__k"))
			{
				string = cookie.getValue();
			}
		}
		
		String ylurl="https://app.nbhrss.gov.cn/nbykt/rest/commapi?callback=jQuery112402946609875493056_1508899402245&access_token="+string+"&api=91S020&bustype=01&refresh=true&param=%7B%22AAB301%22%3A%22330282%22%2C%22PAGENO%22%3A1%2C%22PAGESIZE%22%3A100%7D&client=NBHRSS_WEB&_="+System.currentTimeMillis();
        Page page7 = webClient2.getPage(ylurl);
        if(null != page7)
        {
        	int code = page7.getWebResponse().getStatusCode();
        	if(code==200)
        	{
        		//System.out.println(page7.getWebResponse().getContentAsString());
                String ylstring = page7.getWebResponse().getContentAsString();
                if(ylstring.contains("COSTLIST"))
                {
                	int j = ylstring.indexOf("(");
                    String substring2 = ylstring.substring(j+1, ylstring.length()-2);
                    JSONObject ylfromObject = JSONObject.fromObject(substring2);
                    String ylstring4 = ylfromObject.getString("result");
                    JSONObject fromObject = JSONObject.fromObject(ylstring4);
                    String string4 = fromObject.getString("COSTLIST");
                    JSONObject fromObject3 = JSONObject.fromObject(string4);
                    System.out.println(fromObject3);
                    String string5 = fromObject3.getString("COST");
                    JSONArray fromObject4 = JSONArray.fromObject(string5);
                    System.out.println(fromObject4);
                    InsuranceNingboLost insuranceNingboLost = null;
                    List<InsuranceNingboLost> list = new ArrayList<InsuranceNingboLost>();
                    for (int k = 0; k < fromObject4.size(); k++) {
                    	insuranceNingboLost = new InsuranceNingboLost();
            			JSONObject fromObject5 = JSONObject.fromObject(fromObject4.get(k));
            			insuranceNingboLost.setPayDate(fromObject5.getString("AAE002"));
            			insuranceNingboLost.setInsuranceBase(fromObject5.getString("AAE180"));
            			insuranceNingboLost.setPersonMoney(fromObject5.getString("AAE022"));
            			insuranceNingboLost.setGetStatus(fromObject5.getString("AAE078"));
            			insuranceNingboLost.setTaskid(taskInsurance.getTaskid());
            			list.add(insuranceNingboLost);
            		}
                    webParam.setList(list);
                    webParam.setUrl(ylurl);
                    webParam.setHtml(page7.getWebResponse().getContentAsString());
                    return webParam;
                }
        	}
        }
        return null;
	}
}
