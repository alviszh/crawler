package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiGeneral;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiHtml;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.exceptiondetail.EUtils;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceShanghaiParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private EUtils eutils;

	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		tracer.addTag("InsuranceShanghaiParser.login", insuranceRequestParameters.getTaskId());
		//登录日志
		tracer.addTag("parser.login",insuranceRequestParameters.getTaskId());

		String loginUrl = "http://www.12333sh.gov.cn/sbsjb/wzb/129.jsp";
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int status = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("InsuranceShanghaiParser.login.status",
				insuranceRequestParameters.getTaskId() + "---status:" + status);
		if (200 == status) {
			HtmlTableDataCell td = searchPage.getFirstByXPath("//td[@onclick='javascript:reloadImage();']");
			td.click();
			HtmlImage image = searchPage.getFirstByXPath("//img[@id='pic']");
			// 超级鹰解析验证码
			String code = chaoJiYingOcrService.getVerifycode(image, "1902");
			tracer.addTag("InsuranceShanghaiParser.login.code",
					insuranceRequestParameters.getTaskId() + "---超级鹰解析code:" + code);
			String selector_username = "[name=userid]";
			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector(selector_username);
			if (inputUserName == null) {
				tracer.addTag("InsuranceShanghaiParser.login", insuranceRequestParameters.getTaskId()
						+ "username input text can not found :" + selector_username);
				throw new Exception("username input text can not found :" + selector_username);
			} else {
				inputUserName.reset();
				inputUserName.setText(insuranceRequestParameters.getUsername());
				tracer.addTag("InsuranceShanghaiParser.login.getUsername", insuranceRequestParameters.getTaskId()
						+ "---getUsername:" + insuranceRequestParameters.getUsername());
			}
			String selector_password = "[name=userpw]";
			HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector(selector_password);
			if (inputPassword == null) {
				tracer.addTag("InsuranceShanghaiParser.login",
						insuranceRequestParameters.getTaskId() + "password input text can not found :" + inputPassword);
				throw new Exception("password input text can not found :" + selector_password);
			} else {
				inputPassword.reset();
				inputPassword.setText(insuranceRequestParameters.getPassword());
				tracer.addTag("InsuranceShanghaiParser.login.getPassword", insuranceRequestParameters.getTaskId()
						+ "---getPassword:" + insuranceRequestParameters.getPassword());
			}
			String selector_userjym = "[name=userjym]";
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector(selector_userjym);

			if (inputuserjym == null) {
				tracer.addTag("InsuranceShanghaiParser.login",
						insuranceRequestParameters.getTaskId() + "code input text can not found :" + selector_userjym);
				throw new Exception("code input text can not found :" + selector_userjym);
			} else {
				inputuserjym.reset();
				inputuserjym.setText(code);
			}
			HtmlTableDataCell loginButton = searchPage.getFirstByXPath("//td[@onclick='checkForm();']");
			HtmlPage loggedPage;

			if (loginButton == null) {
				tracer.addTag("InsuranceShanghaiParser.login",
						insuranceRequestParameters.getTaskId() + "login button can not found : null");
				throw new Exception("login button can not found : null");
			} else {
				loggedPage = loginButton.click();
			}
			webParam.setCode(loggedPage.getWebResponse().getStatusCode());
			webParam.setPage(loggedPage);
			return webParam;
		}

		return null;
	}

	/**
	 * @Des 获取信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public WebParam getAllData(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {
		// 获取个人信息日志
		// 获取养老信息日志
		// 获取医疗信息日志
		// 获取失业信息日志
		tracer.addTag("parser.crawler.getUserinfo---getPension---getMedical---getUnemployment",
				taskInsurance.getTaskid());
		WebParam webParam = new WebParam();
		try {
			// 个人信息与流水
			String url = "http://www.12333sh.gov.cn/sbsjb/wzb/sbsjbcx.jsp";
			// 个人信息
			String url2 = "http://www.12333sh.gov.cn/sbsjb/wzb/psnl_regxg.jsp";
			WebClient webClient = insuranceService.getWebClient(cookies);
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			WebRequest requestSettings2 = new WebRequest(new URL(url2), HttpMethod.POST);
			HtmlPage page = webClient.getPage(requestSettings);
			HtmlPage page2 = webClient.getPage(requestSettings2);
			int statusCode = page.getWebResponse().getStatusCode();
			int statusCode2 = page2.getWebResponse().getStatusCode();
			tracer.addTag("InsuranceShanghaiParser.login.status", taskInsurance.getTaskid() + "---个人信息与流水statusCode:"
					+ statusCode + "----个人信息statusCode" + statusCode2);
			if (200 == statusCode || 200 == statusCode2) {
				String html = "";
				String html2 = "";
				if (200 == statusCode) {
					html = page.getWebResponse().getContentAsString();
				}
				if (200 == statusCode2) {
					html2 = page2.getWebResponse().getContentAsString();
				}
				tracer.addTag("InsuranceShanghaiParser.getAllData.html,taskId:" + taskInsurance.getTaskid()
						+ "---个人信息与流水html:", "<xmp>" + html + "</xmp>");
				tracer.addTag(
						"InsuranceShanghaiParser.getAllData.html2,taskId:" + taskInsurance.getTaskid() + "---个人信息html:",
						"<xmp>" + html2 + "</xmp>");

				List<InsuranceShanghaiHtml> insuranceShanghaiHtmls = new ArrayList<InsuranceShanghaiHtml>();
				insuranceShanghaiHtmls
						.add(new InsuranceShanghaiHtml(taskInsurance.getTaskid(), "general-userInfo", 0, url, html));
				insuranceShanghaiHtmls
						.add(new InsuranceShanghaiHtml(taskInsurance.getTaskid(), "userInfo", 0, url2, html2));
				InsuranceShanghaiUserInfo insuranceShanghaiUserInfo = htmlParser(html, html2, taskInsurance);
				tracer.addTag("InsuranceShanghaiParser.getGeneral.insuranceShanghaiUserInfo",
						taskInsurance.getTaskid() + "---insuranceShanghaiUserInfo:" + insuranceShanghaiUserInfo);
				List<InsuranceShanghaiGeneral> insuranceShanghaiGeneral = getGeneral(html, taskInsurance);
				tracer.addTag("InsuranceShanghaiParser.getGeneral.insuranceShanghaiGeneral",
						taskInsurance.getTaskid() + "---insuranceShanghaiGeneral:" + insuranceShanghaiGeneral);
				webParam.setCode(statusCode);
				webParam.setInsuranceShanghaiUserInfo(insuranceShanghaiUserInfo);
				webParam.setPage(page);
				webParam.setInsuranceShanghaiGeneral(insuranceShanghaiGeneral);
				webParam.setInsuranceShanghaiHtml(insuranceShanghaiHtmls);
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceShanghaiParser.getAllData---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
		}
		return webParam;
	}

	/**
	 * 解析个人流水
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 * @throws Exception 
	 */
	public List<InsuranceShanghaiGeneral> getGeneral(String html, TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("InsuranceShanghaiParser.getGeneral", taskInsurance.getTaskid());
		try {
			List<InsuranceShanghaiGeneral> insuranceShanghaiGeneral = new ArrayList<InsuranceShanghaiGeneral>();
			Document document = Jsoup.parse(html);
			Element xmldata = document.getElementById("dataisxxb_sum2");
			Elements jsjs = xmldata.getElementsByTag("jsjs");
			InsuranceShanghaiGeneral socialsecurity = null;
			for (Element element : jsjs) {
				String yearMonth = element.getElementsByTag("jsjs1").get(0).text();
				String payBase = element.getElementsByTag("jsjs3").get(0).text();
				String pension = element.getElementsByTag("jsjs4").get(0).text();
				String medical = element.getElementsByTag("jsjs6").get(0).text();
				String unemployment = element.getElementsByTag("jsjs8").get(0).text();
				socialsecurity = new InsuranceShanghaiGeneral(taskInsurance.getTaskid(), yearMonth, payBase, pension,
						medical, unemployment);
				insuranceShanghaiGeneral.add(socialsecurity);
			}
			return insuranceShanghaiGeneral;
		} catch (Exception e) {
			tracer.addTag("InsuranceShanghaiParser.getGeneral---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private InsuranceShanghaiUserInfo htmlParser(String html, String html2, TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("InsuranceShanghaiParser.htmlParser", taskInsurance.getTaskid());
		try {
			Document document = Jsoup.parse(html);
			Element userdata = document.getElementById("dataisxxb_sum1");
			// 姓名
			String name = userdata.getElementsByTag("xm").get(0).text();
			// 身份证号
			String idNumber = userdata.getElementsByTag("zjhm").get(0).text();
			Element userdata2 = document.getElementById("dataisxxb_sum12");
			// 参保单位
			String company = userdata2.getElementsByTag("jfdw").get(0).text();

			Document document2 = Jsoup.parse(html2);
			Element xmldata = document2.getElementById("dataispsnl_reg1");
			// 手机号
			String mobile = xmldata.getElementsByTag("sjh").get(0).text();
			// 电话
			String telephone = xmldata.getElementsByTag("lxdh").get(0).text();
			// 区县
			String area = xmldata.getElementsByTag("qx_id_dmfy").get(0).text();
			// 街道
			String street = xmldata.getElementsByTag("jd_id_dmfy").get(0).text();
			// 地址
			String address = xmldata.getElementsByTag("lxdz").get(0).text();
			// 邮编
			String postal = xmldata.getElementsByTag("lxdzyb").get(0).text();

			InsuranceShanghaiUserInfo insuranceShanghaiUserInfo = new InsuranceShanghaiUserInfo(taskInsurance.getTaskid(),
					name, idNumber, mobile, telephone, company, area, street, address, postal);

			return insuranceShanghaiUserInfo;
		} catch (Exception e) {
			tracer.addTag("InsuranceShanghaiParser.htmlParser---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
		}
		return null;
	}

}
