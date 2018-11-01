package test.insurance.yichang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.yichang.HousingYichangPay;
import com.microservice.dao.entity.crawler.housing.yichang.HousingYichangUserInfo;
import com.microservice.dao.entity.crawler.insurance.yichang.InsuranceYichangGeneral;
import com.microservice.dao.entity.crawler.insurance.yichang.InsuranceYichangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import app.util.EncryptionUtils;
import app.util.JsEncryption;

public class TestService {

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String username = "420529198610174513";
			String password = "861017";
			// String username = "420529198610174513";
			// String password = "86101";
			InsuranceRequestParameters insuranceRequestParameters = new InsuranceRequestParameters();
			insuranceRequestParameters.setCity("宜昌市");
			insuranceRequestParameters.setUsername(username);
			insuranceRequestParameters.setPassword(password);
			insuranceRequestParameters.setTaskId("yichang111");
			// webClient = login(insuranceRequestParameters);
			// loginIndex(insuranceRequestParameters);

			 webClient = getInfo(webClient,insuranceRequestParameters);
			// getloginInfo(webClient);

			// password = password.replace("+", "%2B");
			// password = password.replace("&", "%26");
			// String pwd =
			// MD5Utils.StringToMd5(insuranceRequestParameters.getUsername()+password).toUpperCase();
			// System.out.println("pwd-------"+pwd);

			// 社保用户信息
//			 InsuranceYichangUserInfo sBuserinfo = getSBuserinfo("", "");
//			 JSONObject fromObject = JSONObject.fromObject(sBuserinfo);
//			 System.out.println(fromObject.toString());

			// 社保流水
//			 List<InsuranceYichangGeneral> list = getSBliushui("", "");
//			 JSONArray fromObject = JSONArray.fromObject(list);
//			 System.out.println(fromObject.toString());

			// 公积金用户信息
//			HousingYichangUserInfo sBuserinfo = getGjjuserinfo("", "");
//			JSONObject fromObject = JSONObject.fromObject(sBuserinfo);
//			System.out.println(fromObject.toString());
			
			// 公积金流水
//			List<HousingYichangPay> list = getGjjliushui("", "");
//			JSONArray fromObject = JSONArray.fromObject(list);
//			System.out.println(fromObject.toString());
			
//			String xmlStr = "<NewDataSet><Flag><SucessFlag>11</SucessFlag><Error>登录验证成功，但登陆手机号码不是注册手机号码！</Error></Flag><ds><USERNAME>wo7Cqn5xwotrwpXCqWloaHXCkMKqaW9xbA==</USERNAME><PHONE>Y2p3cHZvcGxqaWo=</PHONE><TOKEN>45382A899196EAE1E6BD155AE0A0B450</TOKEN><NEWIDCARD>Zmd2bndwam1raGRyZWxnbWlo</NEWIDCARD><NOTE>420501</NOTE><LOGINTIME>ZGV3cXJnbWFkaF9zbG9kcXJqdw==</LOGINTIME></ds></NewDataSet>";
//			xmlStr = xmlStr.toLowerCase();
//			Document doc = Jsoup.parse(xmlStr);
//			String gr_num = doc.getElementsByTag("Error").text();
//			System.out.println(gr_num);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 公积金流水
	 * 
	 * @param taskid
	 * @param html
	 * @return
	 */
	public static List<HousingYichangPay> getGjjliushui(String taskid, String html) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\gjjliushui.txt");
		String xmlStr = txt2String(file);
		xmlStr = xmlStr.toLowerCase();
		List<HousingYichangPay> list = new ArrayList<HousingYichangPay>();
		try {
			Document doc = Jsoup.parse(xmlStr);
			Elements ds = doc.getElementsByTag("ds");
			for (Element element : ds) {
				String date = element.getElementsByTag("TRANSDATE").text();
				System.out.println(date);
				String money = element.getElementsByTag("AMT").text();
				System.out.println(money);
				String business_type = element.getElementsByTag("SUMMARYDESC").text();
				System.out.println(business_type);
				
				HousingYichangPay housingYichangPay = new HousingYichangPay(taskid, date, money, business_type);
				list.add(housingYichangPay);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 公积金用户信息
	 * 
	 * @param taskid
	 * @param html
	 * @return
	 */
	public static HousingYichangUserInfo getGjjuserinfo(String taskid, String html) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\gjjUserInfo.txt");
		String xmlStr = txt2String(file);
		try {
			xmlStr = xmlStr.toLowerCase();
			Document doc = Jsoup.parse(xmlStr);
			String gr_num = doc.getElementsByTag("USERID").text();
			System.out.println(gr_num);
			String name = doc.getElementsByTag("USERNAME").text();
			System.out.println(name);
			String company = doc.getElementsByTag("DWMC").text();
			System.out.println(company);
			String total = doc.getElementsByTag("PRICEACCOUNT").text();
			System.out.println(total);
			String monthly_payment = doc.getElementsByTag("PRICECOST").text();
			System.out.println(monthly_payment);
			String year_month = doc.getElementsByTag("LASTPAYDATE").text();
			System.out.println(year_month);
			String dw_monthly_pay = doc.getElementsByTag("DWYJCE").text();
			System.out.println(dw_monthly_pay);
			String gr_Monthly_Pay = doc.getElementsByTag("GRYJCE").text();
			System.out.println(gr_Monthly_Pay);

			HousingYichangUserInfo housingYichangUserInfo = new HousingYichangUserInfo(taskid, gr_num, name, company,
					total, monthly_payment, year_month, dw_monthly_pay, gr_Monthly_Pay);
			return housingYichangUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 社保流水
	 * 
	 * @param taskid
	 * @param html
	 * @return
	 */
	public static List<InsuranceYichangGeneral> getSBliushui(String taskid, String html) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\SBliushui.txt");
		String xmlStr = txt2String(file);
		xmlStr = xmlStr.toLowerCase();
		List<InsuranceYichangGeneral> list = new ArrayList<InsuranceYichangGeneral>();
		try {
			Document doc = Jsoup.parse(xmlStr);
			Elements ds = doc.getElementsByTag("ds");
			for (Element element : ds) {
				String period_of_payment = element.getElementsByTag("AAE003").text();
				System.out.println(period_of_payment);
				String insurance_type = element.getElementsByTag("AAE140").text();
				System.out.println(insurance_type);
				String pay_num = element.getElementsByTag("AAC150").text();
				System.out.println(pay_num);
				String payable = element.getElementsByTag("AAC123").text();
				System.out.println(payable);
				String money = element.getElementsByTag("AAE210").text();
				System.out.println(money);
				String payment_type = element.getElementsByTag("AAE143").text();
				System.out.println(payment_type);
				String pay_mark = element.getElementsByTag("AAE114").text();
				System.out.println(pay_mark);
				InsuranceYichangGeneral insuranceYichangGeneral = new InsuranceYichangGeneral(taskid, period_of_payment,
						insurance_type, pay_num, payable, money, payment_type, pay_mark);
				list.add(insuranceYichangGeneral);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 社保用户信息
	 * 
	 * @param taskid
	 * @param html
	 * @return
	 */
	public static InsuranceYichangUserInfo getSBuserinfo(String taskid, String html) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\SBUserInfo.txt");
		String xmlStr = txt2String(file);
		try {

			Document doc = Jsoup.parse(xmlStr);
			String dw_name = doc.getElementsByTag("aab004").text();
			System.out.println(dw_name);
			String idcard = doc.getElementsByTag("aac002").text();
			System.out.println(idcard);
			String gr_num = doc.getElementsByTag("aac001").text();
			System.out.println(gr_num);
			String name = doc.getElementsByTag("aac003").text();
			System.out.println(name);
			String sex = doc.getElementsByTag("aac004").text();
			System.out.println(sex);
			String birthday = doc.getElementsByTag("aac006").text();
			System.out.println(birthday);
			String state = doc.getElementsByTag("aac008").text();
			System.out.println(state);
			String insurance_num = doc.getElementsByTag("akc020").text();
			System.out.println(insurance_num);
			String medical_category = doc.getElementsByTag("aac008").text();
			System.out.println(medical_category);

			InsuranceYichangUserInfo insuranceYichangUserInfo = new InsuranceYichangUserInfo(taskid, dw_name, idcard,
					gr_num, name, sex, birthday, state, insurance_num, medical_category);
			return insuranceYichangUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static WebClient getloginInfo(WebClient webClient, String token, String sfzhm, String loginType)
			throws Exception {
		
		webClient = WebCrawler.getInstance().getNewWebClient();
		
		// SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd
		// HH:mm:ss");
		// String time = format.format(new Date());
		String time = EncryptionUtils.Base64(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		String loginPWD = EncryptionUtils.StringToMd5(token + time).toUpperCase();

		String url = "http://61.136.223.44/web2/src/personal/Service.asmx/smcxLoginNew";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		// 公积金用户信息
		// paramsList.add(new NameValuePair("inType", "201"));
		// 公积金明细
		// paramsList.add(new NameValuePair("inType", "202"));
		// 社保用户信息
		paramsList.add(new NameValuePair("inType", "101"));
		// 社保流水
		// paramsList.add(new NameValuePair("inType", "102"));

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
		paramsList.add(new NameValuePair("loginType", loginType));

		Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept", "application/xml, text/xml, */*; q=0.01");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Accept-Language", "zh-CN,zh;q=0.9");
		// map.put("Connection", "keep-alive");
		// map.put("Content-Type", "application/x-www-form-urlencoded;
		// charset=UTF-8");
		// map.put("Host", "61.136.223.44");
		// map.put("Origin", "http://61.136.223.44");
		// map.put("Referer",
		// "http://61.136.223.44/web2/src/personal/smcx/sb/gjj_content_1.html");
		// map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84
		// Safari/537.36");
		// map.put("X-Requested-With", "XMLHttpRequest");
//		map.put("Cookie",
//				"__guid=139110004.1963608735569918200.1523441577354.4753; ASP.NET_SessionId=usqntkdagkdnv230yrh3fo2t; CheckCode=17404; key={%22sfzhm%22:%22Zmd2bndwam1raGRyZWxnbWlo%22%2C%22token%22:%226252DA27D8CFC9C632D463CF7FBD0859%22%2C%22logintime%22:%22ZGV3cXJnbWFkY19zbG9kbnJqew==%22%2C%22name%22:%22wo7Cqn5xwotrwpXCqWloaHXCkMKqaW9xbA==%22%2C%22tel%22:%22Y2p3cHZvcGxqaWo=%22%2C%22type%22:%221%22%2C%22email%22:%22Ug==%22%2C%22note%22:%22420501%22}; targetUrl={%22type%22:%221%22%2C%22url%22:%22ehome.html%22}; monitor_count=12");

		Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);
		String string = page.getWebResponse().getContentAsString();
		System.out.println("公积金用户信息----------------" + string);
		
		String cookies2 = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookies22222222222222222222222222"+cookies2);

		return webClient;

	}

	public static WebClient getInfo(WebClient webClient, InsuranceRequestParameters insuranceRequestParameters)
			throws Exception {

		String key1 = "123456789";
		String username = insuranceRequestParameters.getUsername();
		String password = insuranceRequestParameters.getPassword();
		password = password.replace("+", "%2B");
		password = password.replace("&", "%26");

		String nLoginPWD = EncryptionUtils.StringToMd5("smejweb" + username.toUpperCase() + "dzzw321").toUpperCase();
		String pwd = EncryptionUtils.StringToMd5(username + password).toUpperCase();
		String nSfzhm = "";
		try {
			nSfzhm = JsEncryption.encrypted(username, key1);
			nSfzhm = EncryptionUtils.Base64(nSfzhm);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("nLoginPWD-------" + nLoginPWD);
		System.out.println("pwd-------" + pwd);
		System.out.println("nSfzhm-------" + nSfzhm);

		String url = "http://61.136.223.44/Service.asmx/LoginCheck";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("nLoginID", "smejweb"));
		paramsList.add(new NameValuePair("nLoginPWD", nLoginPWD));
		paramsList.add(new NameValuePair("nSfzhm", nSfzhm));
		paramsList.add(new NameValuePair("pwd", pwd));
		paramsList.add(new NameValuePair("phoneNO", ""));
		paramsList.add(new NameValuePair("VerifiCode", ""));
		paramsList.add(new NameValuePair("system", ""));
		paramsList.add(new NameValuePair("versions", ""));

		Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Accept-Language", "zh-CN,zh;q=0.9");
		// map.put("Cache-Control", "max-age=0");
		// map.put("Connection", "keep-alive");
		// map.put("Host", "61.136.223.44");
		// map.put("Upgrade-Insecure-Requests", "1");
		// map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84
		// Safari/537.36");

		Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);
		String string = page.getWebResponse().getContentAsString();
		System.out.println("登录信息----------------" + string);
		String cookies = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookies11111111111111"+cookies);
		
		if (string.contains("TOKEN")) {
			Document doc = Jsoup.parse(string.toLowerCase());
			Elements tdValue = doc.getElementsByTag("token");
			String token = tdValue.get(0).text();
			System.out.println("token----" + token);

			getloginInfo(webClient, token.toUpperCase(), nSfzhm, "1");

		}

		String cookies3 = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookies333333333333333333333333"+cookies3);
		return webClient;
	}

	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			// BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
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
	public static Page getPage(WebClient webClient, String taskid, String url, HttpMethod type,
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {
		// tracerLog.output("CmbChinaService.getPage---url:", url + "---taskId:"
		// + taskid);
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
		// tracerLog.output("CmbChinaService.getPage.statusCode:" + statusCode,
		// url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int monthCount, int dateCount) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	public static void save(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);
		int byteCount = 0;
		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);

		}
		inputStream.close();
		outputStream.close();
	}

	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type, String body) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			return searchPage;
		}

		return null;
	}
	public static WebClient loginIndex(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String loginUrl = "http://61.136.223.44/web2/src/index/index.html";
		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		HtmlPage searchPage = getHtmlPage(webClient, loginUrl, HttpMethod.GET, null);
		if (null != searchPage) {

			String xmlStr1 = searchPage.getWebResponse().getContentAsString();
			System.out.println(xmlStr1);
			System.out.println("------------------");

			HtmlAnchor menudl = (HtmlAnchor) searchPage.querySelector("#menu-dl");

			searchPage = menudl.click();

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("#sfz");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :#sfz");
			} else {
				inputUserName.reset();
				inputUserName.setText(insuranceRequestParameters.getUsername());
			}
			HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector("#password");
			if (inputPassword == null) {
				throw new Exception("password input text can not found : #password");
			} else {
				inputPassword.reset();
				inputPassword.setText(insuranceRequestParameters.getPassword());
			}

			String imgUrl = "http://61.136.223.44/ValidateCode.aspx";
			Page pageImg = getPage(webClient, null, imgUrl, HttpMethod.GET, null, null, null, null);
			InputStream contentAsStream = pageImg.getWebResponse().getContentAsStream();
			File codeImageFile = ocrService.getImageLocalPath();
			String imgagePath = codeImageFile.getAbsolutePath();
			save(contentAsStream, imgagePath);
			String verifycode = ocrService.callChaoJiYingService(imgagePath, "1005");
			System.out.println("qqq====" + verifycode);

			HtmlTextInput inputcaptcha = (HtmlTextInput) searchPage.querySelector("#captcha");
			if (inputcaptcha == null) {
				throw new Exception("code input text can not found : #captcha");
			} else {
				inputcaptcha.reset();
				inputcaptcha.setText(verifycode);
			}

			HtmlButtonInput loginButton = searchPage.getFirstByXPath("//*[@id='personalLoginBtn']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				Page page = loginButton.click();
				String xmlStr = page.getWebResponse().getContentAsString();
				System.out.println(xmlStr);
				System.out.println("------------------------------------------");

				// String url =
				// "http://61.136.223.44/web2/src/personal/smcx/sb/gjj_content_1.html";
				// HtmlPage searchPage1 = getHtmlPage(webClient, url, null,
				// null);
				// if (null != searchPage1) {
				// System.out.println(searchPage1.getWebResponse().getContentAsString());
				// System.out.println("---------------------------------------");
				// System.out.println(searchPage1.asXml());
				// }

				String cookies = CommonUnit.transcookieToJson(webClient);
				System.out.println(cookies);

				// getloginInfo(webClient);
			}
			return webClient;
		}
		return null;
	}

	public static WebClient login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String loginUrl = "http://61.136.223.44/web2/src/index/login.html";
		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Accept-Language", "zh-CN,zh;q=0.9");
		// map.put(" Cache-Control", "max-age=0");
		// map.put("Connection", "keep-alive");
		// map.put("Host", "61.136.223.44");
		// map.put("If-Modified-Since", "Thu, 29 Mar 2018 03:26:53 GMT");
		// map.put("If-None-Match", "\"803c21c8dc7d31:0\"");
		// map.put("Upgrade-Insecure-Requests", "1");
		// map.put("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like
		// Gecko) Chrome/63.0.3239.84 Safari/537.36");
		// HtmlPage searchPage = (HtmlPage) getPage(webClient, null, loginUrl,
		// HttpMethod.GET, null, null, null, map);

		HtmlPage searchPage = getHtmlPage(webClient, loginUrl, null, null);
		// webClient.waitForBackgroundJavaScript(20000);
		if (null != searchPage) {

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("#sfz");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :#sfz");
			} else {
				inputUserName.reset();
				inputUserName.setText(insuranceRequestParameters.getUsername());
			}
			HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector("#password");
			if (inputPassword == null) {
				throw new Exception("password input text can not found : #password");
			} else {
				inputPassword.reset();
				inputPassword.setText(insuranceRequestParameters.getPassword());
			}
			String verifycode = "";

			// HtmlImage image =
			// searchPage.getFirstByXPath("//*[@id=\"loginTable\"]/tbody/tr/td/table/tbody/tr[5]/td[3]/img");
			// String imageName = "111.jpg";
			// File file = new File("D:\\img\\" + imageName);
			// try {
			// image.saveAs(file);
			// String imgagePath = file.getAbsolutePath();
			// verifycode = ocrService.callChaoJiYingService(imgagePath,
			// "1005");
			// System.out.println("验证码=========="+verifycode);
			// } catch (Exception e) {
			// System.out.println("图片有误");
			// }

			String imgUrl = "http://61.136.223.44/ValidateCode.aspx";
			Page pageImg = getPage(webClient, null, imgUrl, HttpMethod.GET, null, null, null, null);
			InputStream contentAsStream = pageImg.getWebResponse().getContentAsStream();
			File codeImageFile = ocrService.getImageLocalPath();
			String imgagePath = codeImageFile.getAbsolutePath();
			save(contentAsStream, imgagePath);
			verifycode = ocrService.callChaoJiYingService(imgagePath, "1005");
			System.out.println("qqq====" + verifycode);

			HtmlTextInput inputcaptcha = (HtmlTextInput) searchPage.querySelector("#captcha");
			if (inputcaptcha == null) {
				throw new Exception("code input text can not found : #captcha");
			} else {
				inputcaptcha.reset();
				inputcaptcha.setText(verifycode);
			}

			HtmlButtonInput loginButton = searchPage.getFirstByXPath("//*[@id='personalLoginBtn']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				// searchPage = loginButton.click();
				loginButton.click();

				String xmlStr = searchPage.getWebResponse().getContentAsString();
				System.out.println(xmlStr);
				System.out.println("------------------------------------------");

				// System.out.println("Response content:"
				// + new String(xmlStr.getBytes("ISO-8859-1"),"UTF-8"));
				// System.out.println("Response content:"
				// + new String(xmlStr.getBytes("GBK"),"UTF-8"));

				// String outStr = new String(xmlStr.getBytes("UTF-8"), "GBK");
				// System.out.println(outStr);

				// String output = URLDecoder.decode(xmlStr, "iso-8859-1");
				// System.out.println(output);

				Thread.sleep(10000);

				// String url =
				// "http://61.136.223.44/web2/src/index/login.html";
				// HtmlPage searchPage1 = getHtmlPage(webClient, url, null,
				// null);
				// if (null != searchPage1) {
				// System.out.println(searchPage1.getWebResponse().getContentAsString());
				// }

				Thread.sleep(10000);

				String cookies = CommonUnit.transcookieToJson(webClient);
				System.out.println(cookies);

				// getloginInfo(webClient);
			}
			return webClient;
		}
		return null;
	}


}
