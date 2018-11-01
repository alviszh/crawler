package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanGeneral;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanHtml;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanInsuranceRecord;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.exceptiondetail.EUtils;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceDongguanParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private EUtils eutils;

	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		tracer.addTag("InsuranceDongguanParser.login", insuranceRequestParameters.getTaskId());
		// 登录日志
		tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String loginUrl = "https://grcx.dgsi.gov.cn/";
		WebParam webParam = new WebParam();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(30000);
		int status = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("InsuranceDongguanParser.login.status",
				insuranceRequestParameters.getTaskId() + "---status:" + status);
		if (200 == status) {
			HtmlImage image = searchPage.getFirstByXPath("//img[@class='checkimage']");
			String code = "";
			try {
				code = chaoJiYingOcrService.getVerifycode(image, "1902");
			} catch (Exception e) {
				tracer.addTag("InsuranceDongguanParser.login---Taskid--"+
						insuranceRequestParameters.getTaskId() , eutils.getEDetail(e));
			}
			tracer.addTag("InsuranceDongguanParser.login.code",
					insuranceRequestParameters.getTaskId() + "---超级鹰解析code:" + code);

			String selector_username = "input[name='SFZHM']";
			String selector_password = "input[name='PASSWORD']";
			String selector_userjym = "input[name='imagecheck']";
			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector(selector_username);
			if (inputUserName == null) {
				tracer.addTag("InsuranceDongguanParser.login", insuranceRequestParameters.getTaskId()
						+ "username input text can not found :" + selector_username);
				throw new Exception("username input text can not found :" + selector_username);
			} else {
				inputUserName.reset();
				inputUserName.setText(insuranceRequestParameters.getUsername());
				tracer.addTag("InsuranceDongguanParser.login.getUsername", insuranceRequestParameters.getTaskId()
						+ "---getUsername:" + insuranceRequestParameters.getUsername());
			}
			HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector(selector_password);
			if (inputPassword == null) {
				tracer.addTag("InsuranceDongguanParser.login",
						insuranceRequestParameters.getTaskId() + "password input text can not found :" + inputPassword);
				throw new Exception("password input text can not found :" + selector_password);
			} else {
				inputPassword.reset();
				inputPassword.setText(insuranceRequestParameters.getPassword());
				tracer.addTag("InsuranceDongguanParser.login.getPassword", insuranceRequestParameters.getTaskId()
						+ "---getPassword:" + insuranceRequestParameters.getPassword());
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector(selector_userjym);
			if (inputuserjym == null) {
				tracer.addTag("InsuranceDongguanParser.login",
						insuranceRequestParameters.getTaskId() + "code input text can not found :" + selector_userjym);
				throw new Exception("code input text can not found :" + selector_userjym);
			} else {
				inputuserjym.reset();
				inputuserjym.setText(code);
			}
			HtmlAnchor loginButton = searchPage.getFirstByXPath("//a[@onclick='login();return false;']");
			if (loginButton == null) {
				tracer.addTag("InsuranceDongguanParser.login",
						insuranceRequestParameters.getTaskId() + "login button can not found : null");
				throw new Exception("login button can not found : null");
			} else {
				searchPage = loginButton.click();
			}
			webParam.setCode(searchPage.getWebResponse().getStatusCode());
			webParam.setPage(searchPage);
			return webParam;
		}

		return null;
	}

	/**
	 * 通过url获取 HtmlPage
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public HtmlPage getHtmlPage(TaskInsurance taskInsurance, String cookies, String url, HttpMethod type)
			throws Exception {
		tracer.addTag("InsuranceDongguanParser.getHtmlPage---url:" + url, taskInsurance.getTaskid());
		WebClient webClient = taskInsurance.getClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("InsuranceDongguanParser.getHtmlPage---url:" + url + "---taskid:" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
			if (html.contains("没有此人的月缴费记录")) {
				return null;
			}
			return searchPage;
		}

		return null;
	}

	/**
	 * 获取URL路径
	 * 
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getParentLabelByKeyword(Document document, String keyword) {
		String url = "https://grcx.dgsi.gov.cn/";
		Elements es = document.select("span:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element parentElement = element.parent();
			if (null != parentElement) {
				String src = parentElement.attr("href");
				String srcurl = src.replace("\\", "/");
				return url + srcurl;
			}
		}
		return null;
	}

	/**
	 * 获取Menu
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public String getMenu(TaskInsurance taskInsurance, String cookies) {

		String urlData = "https://grcx.dgsi.gov.cn/pages/navigation.jsp";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getSummary 社保总信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				return html;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceDongguanParser.getMenu---Taskid--"+
					taskInsurance.getTaskid() , eutils.getEDetail(e));
		}

		return null;
	}

	/**
	 * 获取个人信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public WebParam<InsuranceDongguanHtml> getUserInfo(TaskInsurance taskInsurance, String htmlUrl) throws Exception {

		tracer.addTag("parser.crawler.getUserinfo", taskInsurance.getTaskid());
		WebParam<InsuranceDongguanHtml> webParam = new WebParam<InsuranceDongguanHtml>();
		List<InsuranceDongguanHtml> list = new ArrayList<InsuranceDongguanHtml>();
		try {
			Document doc = Jsoup.parse(htmlUrl);
			String url1 = getParentLabelByKeyword(doc, "修改个人信息");
			String url2 = getParentLabelByKeyword(doc, "个人基本资料查询");
			String html1 = "";
			String html2 = "";
			tracer.addTag("InsuranceDongguanParser.getUserInfo" + taskInsurance.getTaskid(),
					"修改个人信息URL:" + url1 + "------------个人基本资料查询URL:" + url2);
			try {
				HtmlPage page = getHtmlPage(taskInsurance, taskInsurance.getCookies(), url1, null);
				if (null != page) {
					html1 = page.getWebResponse().getContentAsString();
					tracer.addTag("InsuranceDongguanParser.getUserinfo 个人信息:html1" + taskInsurance.getTaskid(),
							"<xmp>" + html1 + "</xmp>");
					InsuranceDongguanHtml insuranceChengduHtml1 = new InsuranceDongguanHtml(taskInsurance.getTaskid(),
							"insurance_dongguan_userinfo", "1", url1, html1);
					list.add(insuranceChengduHtml1);
				}
			} catch (Exception e) {
				tracer.addTag("InsuranceDongguanParser.getUserInfo-个人信息:html1--Taskid--"+
						taskInsurance.getTaskid() , eutils.getEDetail(e));
			}

			try {
				HtmlPage page = getHtmlPage(taskInsurance, taskInsurance.getCookies(), url2, null);
				if (null != page) {
					html2 = page.getWebResponse().getContentAsString();
					tracer.addTag("InsuranceDongguanParser.getUserinfo 个人信息:html2" + taskInsurance.getTaskid(),
							"<xmp>" + html2 + "</xmp>");
					InsuranceDongguanHtml insuranceChengduHtml2 = new InsuranceDongguanHtml(taskInsurance.getTaskid(),
							"insurance_dongguan_userinfo", "2", url2, html2);
					list.add(insuranceChengduHtml2);
				}
			} catch (Exception e) {
				tracer.addTag("InsuranceDongguanParser.getUserInfo---Taskid--"+
						taskInsurance.getTaskid() , eutils.getEDetail(e));
			}

			InsuranceDongguanUserInfo insuranceDongguanUserInfo = htmlParser(html1, html2, taskInsurance);

			webParam.setInsuranceDongguanUserInfo(insuranceDongguanUserInfo);
			webParam.setList(list);
			webParam.setCode(200);
			return webParam;
		} catch (Exception e) {
			tracer.addTag("InsuranceDongguanParser.getUserInfo---Taskid--"+
					taskInsurance.getTaskid() , eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析个人信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private InsuranceDongguanUserInfo htmlParser(String html1, String html2, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html1);
		String name = insuranceService.getNextLabelByKeyword(doc, "姓名");
		String idNum = insuranceService.getNextLabelByKeyword(doc, "证件号码");
		String sex = insuranceService.getNextLabelByKeyword(doc, "性别");
		String birthdate = insuranceService.getNextLabelByKeyword(doc, "出生日期");
		String workDate = insuranceService.getNextLabelByKeyword(doc, "参加工作日期");
		String mobilePhone = insuranceService.getNextLabelByKeyword(doc, "手机号码");

		Document doc1 = Jsoup.parse(html2);
		String insuranceAgency = insuranceService.getNextLabelByKeyword(doc1, "社保机构");
		String company = insuranceService.getNextLabelByKeyword(doc1, "当前参保单位");
		String pensionMoney = insuranceService.getNextLabelByKeyword(doc1, "基本养老帐户本金");
		String pensionInterest = insuranceService.getNextLabelByKeyword(doc1, "基本养老帐户利息");
		String bank = insuranceService.getNextLabelByKeyword(doc1, "发卡银行及网点");
		String firstHospital = insuranceService.getNextLabelByKeyword(doc1, "第一就医点");
		String firstHospitalAddress = insuranceService.getNextLabelByKeyword(doc1, "第一就医点地址");
		String twoHospital = insuranceService.getNextLabelByKeyword(doc1, "第二就医点名称");
		String effectiveTime = insuranceService.getNextLabelByKeyword(doc1, "有效时间");
		String twoHospitalAddress = insuranceService.getNextLabelByKeyword(doc1, "第二就医点地址");

		InsuranceDongguanUserInfo insuranceDongguanUserInfo = new InsuranceDongguanUserInfo(taskInsurance.getTaskid(),
				name, idNum, sex, birthdate, workDate, mobilePhone, insuranceAgency, company, pensionMoney,
				pensionInterest, bank, firstHospital, firstHospitalAddress, twoHospital, effectiveTime,
				twoHospitalAddress);

		return insuranceDongguanUserInfo;
	}

	/**
	 * 获取东莞社保参保记录
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceDongguanInsuranceRecord> getInsuranceRecord(TaskInsurance taskInsurance, String htmlUrl) {
		WebParam<InsuranceDongguanInsuranceRecord> webParam = new WebParam<InsuranceDongguanInsuranceRecord>();

		Document doc = Jsoup.parse(htmlUrl);
		String urlData = getParentLabelByKeyword(doc, "参保险种信息查询");
		tracer.addTag("InsuranceDongguanParser.getInsuranceRecord" + taskInsurance.getTaskid(),
				"参保险种信息查询URL:" + urlData);
		try {
			HtmlPage page = getHtmlPage(taskInsurance, taskInsurance.getCookies(), urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceDongguanParser.getInsuranceRecord东莞社保参保记录" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceDongguanInsuranceRecord> listInsuranceRecord = parserInsuranceRecord(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listInsuranceRecord);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceDongguanParser.getInsuranceRecord---Taskid--"+
					taskInsurance.getTaskid() , eutils.getEDetail(e));
		}

		return null;
	}

	/**
	 * 解析东莞社保参保记录
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceDongguanInsuranceRecord> parserInsuranceRecord(String html, TaskInsurance taskInsurance) {

		tracer.addTag("InsuranceDongguanParser.parserInsuranceRecord", taskInsurance.getTaskid());

		List<InsuranceDongguanInsuranceRecord> list = new ArrayList<InsuranceDongguanInsuranceRecord>();

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tbody");

		InsuranceDongguanInsuranceRecord insuranceDongguanInsuranceRecord = null;

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("tr");

			for (Element element2 : link2) {
				Elements link3 = element2.getElementsByTag("td");
				if (link3.size() == 3) {
					String insuranceType = link3.get(1).text();
					String insuranceState = link3.get(2).text();
					insuranceDongguanInsuranceRecord = new InsuranceDongguanInsuranceRecord(taskInsurance.getTaskid(),
							insuranceType, insuranceState, "", "");
					list.add(insuranceDongguanInsuranceRecord);
				} else if (link3.size() == 5) {
					String insuranceType = link3.get(1).text();
					String insuranceState = link3.get(2).text();
					String firstInsuranceDate = link3.get(3).text();
					String insuranceMonth = link3.get(4).text();
					insuranceDongguanInsuranceRecord = new InsuranceDongguanInsuranceRecord(taskInsurance.getTaskid(),
							insuranceType, insuranceState, firstInsuranceDate, insuranceMonth);
					list.add(insuranceDongguanInsuranceRecord);
				}

			}
		}

		return list;
	}

	/**
	 * 获取东莞社保月缴费情况查询
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceDongguanGeneral> getGeneral(TaskInsurance taskInsurance, String htmlUrl,
			String yearMonth) {
		WebParam<InsuranceDongguanGeneral> webParam = new WebParam<InsuranceDongguanGeneral>();

		Document doc = Jsoup.parse(htmlUrl);
		String urlData = getParentLabelByKeyword(doc, "月缴费情况查询");

		urlData = urlData + "&jfny=" + yearMonth;

		tracer.addTag("InsuranceDongguanParser.getGeneral" + taskInsurance.getTaskid(), "月缴费情况查询URL:" + urlData);
		try {
			HtmlPage page = getHtmlPage(taskInsurance, taskInsurance.getCookies(), urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceDongguanParser.getGeneral东莞社保月缴费情况查询" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceDongguanGeneral> list = parserGeneral(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceDongguanParser.getGeneral---Taskid--"+
					taskInsurance.getTaskid() , eutils.getEDetail(e));
		}

		return null;
	}

	/**
	 * 解析东莞社保月缴费情况查询
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceDongguanGeneral> parserGeneral(String html, TaskInsurance taskInsurance) {

		tracer.addTag("InsuranceDongguanParser.parserGeneral", taskInsurance.getTaskid());

		List<InsuranceDongguanGeneral> list = new ArrayList<InsuranceDongguanGeneral>();

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tbody");

		InsuranceDongguanGeneral insuranceDongguanGeneral = null;

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("tr");

			for (Element element2 : link2) {
				Elements link3 = element2.getElementsByTag("td");
				if (link3.size() > 11 && !link3.get(0).text().contains("合计")) {
					String company = link3.get(1).text();
					String payMonth = link3.get(2).text();
					String insuranceType = link3.get(3).text();
					String payBase = link3.get(4).text();
					String companyPay = link3.get(5).text();
					String companyProportion = link3.get(6).text();
					String personalPay = link3.get(7).text();
					String personnelProportion = link3.get(8).text();
					String financialAid = link3.get(9).text();
					String financialAidProportion = link3.get(10).text();
					insuranceDongguanGeneral = new InsuranceDongguanGeneral(taskInsurance.getTaskid(), company,
							payMonth, insuranceType, payBase, companyPay, companyProportion, personalPay,
							personnelProportion, financialAid, financialAidProportion);
					list.add(insuranceDongguanGeneral);
				}

			}
		}

		return list;
	}

}
