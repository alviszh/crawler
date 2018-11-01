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
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduBear;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduInjury;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduMedical;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduMedicalconsumption;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduPension;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduPensionAccount;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduSeriousIllnessMedical;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduSummary;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduUnemployment;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class InsuranceChengduParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		tracer.addTag("InsuranceChengduiParser.login", insuranceRequestParameters.getTaskId());
		// 登录日志
		tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());
		String loginUrl = "http://jypt.cdhrss.gov.cn:8045/yhjypt/oauth/authorizeNoCaAction.do?response_type=code&redirect_uri=http://insurance.cdhrss.gov.cn/GetTokenAction.do&client_id=yhtest";
		
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
//		webClient.waitForBackgroundJavaScript(30000);
		int status = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("InsuranceChengduiParser.login.status",
				insuranceRequestParameters.getTaskId() + "---status:" + status);
		if (200 == status) {
			HtmlImage image = searchPage.getFirstByXPath("//*[@id=\"codeimg\"]");
			// 超级鹰解析验证码
			String code = "";
			try {
				code = chaoJiYingOcrService.getVerifycode(image, "1902");
			} catch (Exception e) {
				tracer.addTag("ERROR:InsuranceChengduiParser.login.code",
						insuranceRequestParameters.getTaskId() + "-----ERROR:" + e);
				e.printStackTrace();
			}
			tracer.addTag("InsuranceChengduiParser.login.code",
					insuranceRequestParameters.getTaskId() + "---超级鹰解析code:" + code);
			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("#usn");
			if (inputUserName == null) {
				tracer.addTag("InsuranceChengduiParser.login", insuranceRequestParameters.getTaskId()
						+ "username input text can not found :#usn");
				throw new Exception("username input text can not found : #usn");
			} else {
				inputUserName.reset();
				inputUserName.setText(insuranceRequestParameters.getUsername());
			}
			HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector("#pwd");
			if (inputPassword == null) {
				tracer.addTag("InsuranceChengduiParser.login",
						insuranceRequestParameters.getTaskId() + "password input text can not found :" + inputPassword);
				throw new Exception("password input text can not found : #pwd");
			} else {
				inputPassword.reset();
				inputPassword.setText(insuranceRequestParameters.getPassword());
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("#checkCode");
			if (inputuserjym == null) {
				tracer.addTag("InsuranceChengduiParser.login",
						insuranceRequestParameters.getTaskId() + "code input text can not found : #checkCode");
				throw new Exception("code input text can not found  #checkCode:");
			} else {
				inputuserjym.reset();
				inputuserjym.setText(code);
			}
			HtmlDivision loginButton = searchPage.getFirstByXPath("//*[@id=\"loginBtn\"]");
			if (loginButton == null) {
				tracer.addTag("InsuranceChengduiParser.login",
						insuranceRequestParameters.getTaskId() + "login button can not found : null");
				throw new Exception("login button can not found : null");
			} else {
				searchPage = loginButton.click();
				try {
					tracer.addTag("InsuranceChengduiParser.login",
							insuranceRequestParameters.getTaskId() + "<xmp>" + searchPage.asXml() + "</xmp>");
				} catch (Exception e) {
					tracer.addTag("InsuranceChengduiParser.login.searchPage.asXml():ERROR" , insuranceRequestParameters.getTaskId()+"---ERROR:"+e);
				}
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
		tracer.addTag("InsuranceChengduiParser.getHtmlPage---url:" + url + " ", taskInsurance.getTaskid());
		WebClient webClient = taskInsurance.getClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("InsuranceChengduiParser.getHtmlPage---url:" + url + "---taskid:" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
			if (html.contains("没有查询出相关数据")) {
				return null;
			}
			return searchPage;
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
	@SuppressWarnings("rawtypes")
	public WebParam getUserInfo(TaskInsurance taskInsurance, String cookies) throws Exception {

		tracer.addTag("parser.crawler.getUserinfo", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=1";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getUserinfo 个人信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				InsuranceChengduUserInfo insuranceChengduUserInfo = htmlParser(html, taskInsurance);
				webParam.setInsuranceChengduUserInfo(insuranceChengduUserInfo);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getUserInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
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
	private InsuranceChengduUserInfo htmlParser(String html, TaskInsurance taskInsurance) {

		Document doc = Jsoup.parse(html);
		String personalNumber = getNextLabelByKeyword(doc, "个人编码：");
		String name = getNextLabelByKeyword(doc, "姓");
		String idNum = getNextLabelByKeyword(doc, "身份证号码：");
		String sex = getNextLabelByKeyword(doc, "性");
		String birthdate = getNextLabelByKeyword(doc, "出生日期：");
		String workDate = getNextLabelByKeyword(doc, "参工时间：");
		String personnelStatus = getNextLabelByKeyword(doc, "人员状态：");
		String personalIdentity = getNextLabelByKeyword(doc, "个人身份：");
		String handlingAgency = getNextLabelByKeyword(doc, "经办机构：");
		String openingBank = getNextLabelByKeyword(doc, "开户银行：");
		String bankAccount = getNextLabelByKeyword(doc, "银行账户：");
		String mobilePhone = getNextLabelByKeyword(doc, "移动电话：");
		InsuranceChengduUserInfo insuranceChengduUserInfo = new InsuranceChengduUserInfo(taskInsurance.getTaskid(),
				personalNumber, name, idNum, sex, birthdate, workDate, personnelStatus, personalIdentity,
				handlingAgency, openingBank, bankAccount, mobilePhone);

		return insuranceChengduUserInfo;
	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("th:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	/**
	 * 获取社保总信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduSummary> getSummary(TaskInsurance taskInsurance, String cookies) {
		WebParam<InsuranceChengduSummary> webParam = new WebParam<InsuranceChengduSummary>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=2";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getSummary 社保总信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduSummary> listSummary = parserSummary(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listSummary);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getSummary---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析社保总信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduSummary> parserSummary(String html, TaskInsurance taskInsurance) {

		List<InsuranceChengduSummary> list = new ArrayList<InsuranceChengduSummary>();

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");
		InsuranceChengduSummary insuranceChengduSummary = null;
		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 7) {
				String insuranceType = link2.get(1).text();
				String companyNum = link2.get(2).text();
				String company = link2.get(3).text();
				String insuranceState = link2.get(4).text();
				String firstInsuranceDate = link2.get(5).text();
				String personnelPayType = link2.get(6).text();
				String handlingAgency = link2.get(7).text();
				insuranceChengduSummary = new InsuranceChengduSummary(taskInsurance.getTaskid(), insuranceType,
						companyNum, company, insuranceState, firstInsuranceDate, personnelPayType, handlingAgency);
				list.add(insuranceChengduSummary);

			}
		}

		return list;
	}

	/**
	 * 获取养老缴费明细
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduPension> getPension(TaskInsurance taskInsurance, String cookies) {
		WebParam<InsuranceChengduPension> webParam = new WebParam<InsuranceChengduPension>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=3";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getPension 养老缴费明细" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduPension> listSummary = parserPension(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listSummary);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getPension---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析养老缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduPension> parserPension(String html, TaskInsurance taskInsurance) {

		List<InsuranceChengduPension> list = new ArrayList<InsuranceChengduPension>();
		InsuranceChengduPension insuranceChengduPension = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 8) {
				String payMonth = link2.get(1).text();
				String company = link2.get(2).text();
				String payBase = link2.get(3).text();
				String companyPay = link2.get(4).text();
				String personalPay = link2.get(5).text();
				String companyProportion = link2.get(6).text();
				String personnelProportion = link2.get(7).text();
				String receivablesDate = link2.get(8).text();
				insuranceChengduPension = new InsuranceChengduPension(taskInsurance.getTaskid(), payMonth, company,
						payBase, companyPay, personalPay, companyProportion, personnelProportion, receivablesDate);
				list.add(insuranceChengduPension);
			}
		}

		return list;
	}

	/**
	 * 获取生育保险缴费明细
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduBear> getBear(TaskInsurance taskInsurance, String cookies) {
		WebParam<InsuranceChengduBear> webParam = new WebParam<InsuranceChengduBear>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=10";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getBear 生育保险缴费明细" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduBear> listSummary = parserBear(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listSummary);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getBear---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析生育保险缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduBear> parserBear(String html, TaskInsurance taskInsurance) {

		List<InsuranceChengduBear> list = new ArrayList<InsuranceChengduBear>();
		InsuranceChengduBear insuranceChengduBear = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 8) {
				String payMonth = link2.get(1).text();
				String company = link2.get(2).text();
				String payBase = link2.get(3).text();
				String companyPay = link2.get(4).text();
				String personalPay = link2.get(5).text();
				String companyProportion = link2.get(6).text();
				String personnelProportion = link2.get(7).text();
				String receivablesDate = link2.get(8).text();

				insuranceChengduBear = new InsuranceChengduBear(taskInsurance.getTaskid(), payMonth, company, payBase,
						companyPay, personalPay, companyProportion, personnelProportion, receivablesDate);

				list.add(insuranceChengduBear);
			}
		}

		return list;
	}

	/**
	 * 获取工伤保险缴费明细
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduInjury> getInjury(TaskInsurance taskInsurance, String cookies) {
		WebParam<InsuranceChengduInjury> webParam = new WebParam<InsuranceChengduInjury>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=7";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getInjury工伤保险缴费明细" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduInjury> listInjury = parserInjury(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listInjury);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getInjury---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析工伤保险缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduInjury> parserInjury(String html, TaskInsurance taskInsurance) {

		List<InsuranceChengduInjury> list = new ArrayList<InsuranceChengduInjury>();
		InsuranceChengduInjury insuranceChengduInjury = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 8) {
				String payMonth = link2.get(1).text();
				String company = link2.get(2).text();
				String payBase = link2.get(3).text();
				String companyPay = link2.get(4).text();
				String personalPay = link2.get(5).text();
				String companyProportion = link2.get(6).text();
				String personnelProportion = link2.get(7).text();
				String receivablesDate = link2.get(8).text();

				insuranceChengduInjury = new InsuranceChengduInjury(taskInsurance.getTaskid(), payMonth, company,
						payBase, companyPay, personalPay, companyProportion, personnelProportion, receivablesDate);

				list.add(insuranceChengduInjury);
			}
		}

		return list;
	}

	/**
	 * 获取医疗保险缴费明细
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduMedical> getMedical(TaskInsurance taskInsurance, String cookies) {
		WebParam<InsuranceChengduMedical> webParam = new WebParam<InsuranceChengduMedical>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=5";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getMedical医疗保险缴费明细" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduMedical> listMedical = parserMedical(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listMedical);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getMedical---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析医疗保险缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduMedical> parserMedical(String html, TaskInsurance taskInsurance) {

		List<InsuranceChengduMedical> list = new ArrayList<InsuranceChengduMedical>();
		InsuranceChengduMedical insuranceChengduMedical = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 8) {
				String payMonth = link2.get(0).text();
				String company = link2.get(1).text();
				String payBase = link2.get(2).text();
				String companyPay = link2.get(3).text();
				String personalPay = link2.get(4).text();
				String includedMoney = link2.get(5).text();
				String companyProportion = link2.get(6).text();
				String personnelProportion = link2.get(7).text();
				String receivablesDate = link2.get(8).text();

				insuranceChengduMedical = new InsuranceChengduMedical(taskInsurance.getTaskid(), payMonth, company,
						payBase, companyPay, personalPay, includedMoney, companyProportion, personnelProportion,
						receivablesDate);

				list.add(insuranceChengduMedical);
			}
		}

		return list;
	}

	/**
	 * 获取失业保险缴费明细
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduUnemployment> getUnemployment(TaskInsurance taskInsurance, String cookies) {
		WebParam<InsuranceChengduUnemployment> webParam = new WebParam<InsuranceChengduUnemployment>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=8";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getUnemployment失业保险缴费明细" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduUnemployment> listUnemployment = parserUnemployment(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listUnemployment);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getUnemployment---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析失业保险缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduUnemployment> parserUnemployment(String html, TaskInsurance taskInsurance) {

		List<InsuranceChengduUnemployment> list = new ArrayList<InsuranceChengduUnemployment>();
		InsuranceChengduUnemployment insuranceChengduUnemployment = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 8) {
				String payMonth = link2.get(1).text();
				String company = link2.get(2).text();
				String payBase = link2.get(3).text();
				String companyPay = link2.get(4).text();
				String personalPay = link2.get(5).text();
				String companyProportion = link2.get(6).text();
				String personnelProportion = link2.get(7).text();
				String receivablesDate = link2.get(8).text();

				insuranceChengduUnemployment = new InsuranceChengduUnemployment(taskInsurance.getTaskid(), payMonth,
						company, payBase, companyPay, personalPay, companyProportion, personnelProportion,
						receivablesDate);

				list.add(insuranceChengduUnemployment);
			}
		}

		return list;
	}

	/**
	 * 获取养老保险个人账户明细信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduPensionAccount> getPensionAccount(TaskInsurance taskInsurance, String cookies) {
		WebParam<InsuranceChengduPensionAccount> webParam = new WebParam<InsuranceChengduPensionAccount>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=25";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getPensionAccount养老保险个人账户明细信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduPensionAccount> listPensionAccount = parserPensionAccount(html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listPensionAccount);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getPensionAccount---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析失业保险缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduPensionAccount> parserPensionAccount(String html, TaskInsurance taskInsurance) {

		List<InsuranceChengduPensionAccount> list = new ArrayList<InsuranceChengduPensionAccount>();
		InsuranceChengduPensionAccount insuranceChengduPensionAccount = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 14) {
				String year = link2.get(1).text();
				String months = link2.get(2).text();
				String yearCompanySum = link2.get(3).text();
				String yearPersonalSum = link2.get(4).text();
				String moneySum = link2.get(5).text();
				String companyBalance = link2.get(6).text();
				String personnelBalance = link2.get(7).text();
				String yearCompanyInterest = link2.get(8).text();

				String yearPersonalInterest = link2.get(9).text();
				String lateLastYearPersonalInterest = link2.get(10).text();
				String lateLastYearCompanyInterest = link2.get(11).text();
				String yearRepairMonths = link2.get(12).text();
				String yearRepairCompanySum = link2.get(13).text();
				String yearRepairPersonalSum = link2.get(14).text();
				insuranceChengduPensionAccount = new InsuranceChengduPensionAccount(taskInsurance.getTaskid(), year,
						months, yearCompanySum, yearPersonalSum, moneySum, companyBalance, personnelBalance,
						yearCompanyInterest, yearPersonalInterest, lateLastYearPersonalInterest,
						lateLastYearCompanyInterest, yearRepairMonths, yearRepairCompanySum, yearRepairPersonalSum);

				list.add(insuranceChengduPensionAccount);
			}
		}

		return list;
	}

	/**
	 * 获取大病补充医疗保险缴费明细
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduSeriousIllnessMedical> getSeriousIllnessMedical(TaskInsurance taskInsurance,
			String cookies) {
		WebParam<InsuranceChengduSeriousIllnessMedical> webParam = new WebParam<InsuranceChengduSeriousIllnessMedical>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=11";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag(
						"InsuranceChengduiParser.getSeriousIllnessMedical大病补充医疗保险缴费明细" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduSeriousIllnessMedical> listSeriousIllnessMedical = parserSeriousIllnessMedical(
						html, taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listSeriousIllnessMedical);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getSeriousIllnessMedical---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析大病补充医疗保险缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduSeriousIllnessMedical> parserSeriousIllnessMedical(String html,
			TaskInsurance taskInsurance) {

		List<InsuranceChengduSeriousIllnessMedical> list = new ArrayList<InsuranceChengduSeriousIllnessMedical>();
		InsuranceChengduSeriousIllnessMedical insuranceChengduSeriousIllnessMedical = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 7) {
				String payMonth = link2.get(1).text();
				String company = link2.get(2).text();
				String payBase = link2.get(3).text();
				String moneyPay = link2.get(4).text();
				String payType = link2.get(5).text();
				String payProportion = link2.get(6).text();
				String receivablesDate = link2.get(7).text();

				insuranceChengduSeriousIllnessMedical = new InsuranceChengduSeriousIllnessMedical(
						taskInsurance.getTaskid(), payMonth, company, payBase, moneyPay, payType, payProportion,
						receivablesDate);

				list.add(insuranceChengduSeriousIllnessMedical);
			}
		}

		return list;
	}

	/**
	 * 获取医疗账户消费明细
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceChengduMedicalconsumption> getMedicalconsumption(TaskInsurance taskInsurance,
			String cookies) {
		WebParam<InsuranceChengduMedicalconsumption> webParam = new WebParam<InsuranceChengduMedicalconsumption>();

		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=18";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getMedicalconsumption医疗账户消费明细" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<InsuranceChengduMedicalconsumption> listMedicalconsumption = parserMedicalconsumption(html,
						taskInsurance);
				webParam.setHtml(html);
				webParam.setList(listMedicalconsumption);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getMedicalconsumption---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析大病补充医疗保险缴费明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<InsuranceChengduMedicalconsumption> parserMedicalconsumption(String html,
			TaskInsurance taskInsurance) {

		List<InsuranceChengduMedicalconsumption> list = new ArrayList<InsuranceChengduMedicalconsumption>();
		InsuranceChengduMedicalconsumption insuranceChengduMedicalconsumption = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() > 4) {
				String payData = link2.get(1).text();
				String medicalName = link2.get(2).text();
				String payMoney = link2.get(3).text();
				String payType = link2.get(4).text();

				insuranceChengduMedicalconsumption = new InsuranceChengduMedicalconsumption(taskInsurance.getTaskid(),
						payData, medicalName, payMoney, payType);

				list.add(insuranceChengduMedicalconsumption);
			}
		}

		return list;
	}

	/**
	 * 获取医疗保险账户信息:账户余额
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam getMedicalBalance(TaskInsurance taskInsurance, String cookies) throws Exception {

		tracer.addTag("InsuranceChengduiParser.getMedicalBalance", taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=6";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChengduiParser.getMedicalBalance医疗保险账户信息:账户余额" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				String medicalBalance = parserMedicalBalance(html);

				webParam.setMedicalBalance(medicalBalance);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceChengduiParser.getMedicalBalance---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析医疗保险账户信息:账户余额
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private String parserMedicalBalance(String html) {

		Document doc = Jsoup.parse(html);
		String medicalBalance = getNextLabelByKeyword(doc, "账户余额");

		return medicalBalance;
	}

}
