package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduMedical;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestService {

	public static void main(String[] args) {

		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//			webClient = login("022150730", "06084141");
			// login111("022150730", "06084141");
			// getUserInfo1(webClient);
			// userdata();
			// baoxiandata();
			// yanglaodata();
			// yanglaogerenmingxi();
			// File file = new
			// File("C:\\Users\\Administrator\\Desktop\\SS\\chengdu\\yiliao.txt");
			// String xmlStr = txt2String(file);
			// parserMedical(xmlStr);
			// gettest();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WebClient login(String name, String password) throws Exception {

		String url = "http://jypt.cdhrss.gov.cn:8045/yhjypt/oauth/authorizeNoCaAction.do?response_type=code&redirect_uri=http://insurance.cdhrss.gov.cn/GetTokenAction.do&client_id=yhtest";

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setAdditionalHeader("", "");
		HtmlPage searchPage = webClient.getPage(webRequest);
		// webClient.waitForBackgroundJavaScript(30000);

		HtmlImage image = searchPage.getFirstByXPath("//*[@id=\"codeimg\"]");

		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		try {
			image.saveAs(file);
		} catch (Exception e) {
			System.out.println("图片有误");
		}

		HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("#usn");
		inputUserName.reset();
		inputUserName.setText(name);

		HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector("#pwd");
		inputPassword.reset();
		inputPassword.setText(password);

		HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("#checkCode");
		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		String imgagePath = file.getAbsolutePath();
		String verifycode = ocrService.callChaoJiYingService(imgagePath, "1902");
		System.out.println("qqq====" + verifycode);

		inputuserjym.reset();
		inputuserjym.setText(verifycode);

		HtmlDivision loginButton = searchPage.getFirstByXPath("//*[@id=\"loginBtn\"]");
		if (loginButton == null) {
			throw new Exception("login button can not found : null");
		} else {
			searchPage = loginButton.click();
			// webClient.waitForBackgroundJavaScript(30000);
			String xmlStr = searchPage.asXml();
			System.out.println("------------------------------------------");
			System.out.println(xmlStr);
			if (xmlStr.contains("参保人员基本信息")) {
				System.out.println("登录成功");
			} else {
				System.out.println("登录失败");
			}
			String msg = WebCrawler.getAlertMsg();
			System.out.println("login:msg------------------------------------------" + msg);
		}

//		String html = getInfo(webClient);
//		if (html.contains("参保人员基本信息")) {
//			System.out.println("登录成功");
//		} else {
//			System.out.println("登录失败");
//		}
		return webClient;

	}

	public static WebClient login111(String name, String password) throws Exception {

		String url = "http://jypt.cdhrss.gov.cn:8048/portal.php?id=1";

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		HtmlPage searchPage = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(30000);

		System.out.println(searchPage.getWebResponse().getContentAsString());
		// webClient.getOptions().setJavaScriptEnabled(false);
		// HtmlPage searchPage = webClient.getPage(url);
		// System.out.println(searchPage.asXml());
		HtmlImage image = searchPage.getFirstByXPath("//div[@class='qrcode']/img");

		// HtmlImage image =
		// searchPage.getFirstByXPath("//form[@id='login_form2']//div[@class='qrcode']/img");
		// HtmlImage image =
		// searchPage.getFirstByXPath("//form[@id='login_form"+count+"']//img");

		// HtmlImage image = searchPage.getFirstByXPath("div[@id='main_content']
		// div:not(div[@style='display:none'])//img");

		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		try {
			image.saveAs(file);
		} catch (Exception e) {
			System.out.println("图片有误");
		}
		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		String imgagePath = file.getAbsolutePath();
		String verifycode = ocrService.callChaoJiYingService(imgagePath, "1902");
		System.out.println("code=========" + verifycode);

		// String urlT = "http://insurance.cdhrss.gov.cn/insurance/index.jsp";
		String urlT = "http://jypt.cdhrss.gov.cn:8045/yhjypt/oauth/authorizeNoCaAction!getCode.do";

		// String urlT =
		// "http://insurance.cdhrss.gov.cn/GetTokenAction.do?username=022150730&state=null&code=fe629b77cb0ba18ec2921193c0994687";

		String urlData = "http://insurance.cdhrss.gov.cn/insurance/index.jsp?" + "username=022150730"
				+ "&password=06084141" + "&checkCode=" + verifycode
				+ "&redirect_uri=http%3A%2F%2Finsurance.cdhrss.gov.cn%2FGetTokenAction.do" + "&client_id=yhtest"
				+ "&response_type=code" + "&password1=+" + "&state=null" + "&e=null" + "&m=null" + "&sfz=+";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", "022150730"));
		paramsList.add(new NameValuePair("password", "06084141"));
		paramsList.add(new NameValuePair("checkCode", verifycode));
		paramsList.add(new NameValuePair("redirect_uri", "http://insurance.cdhrss.gov.cn/GetTokenAction.do"));
		paramsList.add(new NameValuePair("client_id", "yhtest"));
		paramsList.add(new NameValuePair("response_type", "code"));
		paramsList.add(new NameValuePair("password1", "+"));
		paramsList.add(new NameValuePair("state", "null"));
		paramsList.add(new NameValuePair("e", "null"));
		paramsList.add(new NameValuePair("m", "null"));
		paramsList.add(new NameValuePair("sfz", "+"));

		Page page = getPage(webClient, "taskId", urlT, HttpMethod.POST, paramsList, null, null, null);
		System.out.println(page.getWebResponse().getContentAsString());

		// String html = getUserInfo1(webClient);
		// if(html.contains("参保人员基本信息")){
		// System.out.println("登录成功");
		// }else{
		// System.out.println("登录失败");
		// }
		return webClient;

	}

	public static void test() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\qwe.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);
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
				System.out.print(year);
				System.out.print("\t");
				System.out.print(months);
				System.out.print("\t");
				System.out.print(yearCompanySum);
				System.out.print("\t");
				System.out.print(yearPersonalSum);
				System.out.print("\t");
				System.out.print(moneySum);
				System.out.print("\t");
				System.out.print(companyBalance);
				System.out.print("\t");
				System.out.print(personnelBalance);
				System.out.print("\t");
				System.out.print(yearCompanyInterest);
				System.out.print("\t");
				System.out.print(yearPersonalInterest);
				System.out.print("\t");
				System.out.print(lateLastYearPersonalInterest);
				System.out.print("\t");
				System.out.print(lateLastYearCompanyInterest);
				System.out.print("\t");
				System.out.print(yearRepairMonths);
				System.out.print("\t");
				System.out.print(yearRepairCompanySum);
				System.out.print("\t");
				System.out.println(yearRepairPersonalSum);
			}
		}

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

	private static void gettest() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\aaa.txt");
		String json = txt2String(file);

		JSONObject jsonObj = JSONObject.fromObject(json);

		JSONArray jsonArray2 = jsonObj.getJSONArray("content");
		for (Object object : jsonArray2) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			String baz001 = jsonObject.getString("baz001");
			System.out.println(baz001);
			String aac001 = jsonObject.getString("aac001");
			System.out.println(aac001);
		}

		// String content = jsonObj.getString("content");
		// Object obj = new JSONTokener(content).nextValue();
		// if (obj instanceof JSONObject) {
		// JSONObject jsonObject = (JSONObject) obj;
		// String baz001 = jsonObject.getString("baz001");
		// System.out.println(baz001);
		// String aac001 = jsonObject.getString("aac001");
		// System.out.println(aac001);
		//
		// } else if (obj instanceof JSONArray) {
		// JSONArray jsonArray = (JSONArray) obj;
		// for (Object object : jsonArray) {
		// JSONObject jsonObject = JSONObject.fromObject(object);
		// String baz001 = jsonObject.getString("baz001");
		// System.out.println(baz001);
		// String aac001 = jsonObject.getString("aac001");
		// System.out.println(aac001);
		// }
		// }

	}

	private static List<InsuranceChengduMedical> parserMedical(String html) {

		List<InsuranceChengduMedical> list = new ArrayList<InsuranceChengduMedical>();
		InsuranceChengduMedical insuranceChengduMedical = null;

		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");

		System.out.println("----------" + link1.size());

		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			System.out.println("----------" + link2.size());
			if (link2.size() > 9) {
				String payMonth = link2.get(0).text();
				String company = link2.get(1).text();
				String payBase = link2.get(2).text();
				String companyPay = link2.get(3).text();
				String personalPay = link2.get(4).text();
				String includedMoney = link2.get(5).text();
				String companyProportion = link2.get(6).text();
				String personnelProportion = link2.get(7).text();
				String receivablesDate = link2.get(8).text();

				insuranceChengduMedical = new InsuranceChengduMedical("", payMonth, company, payBase, companyPay,
						personalPay, includedMoney, companyProportion, personnelProportion, receivablesDate);

				System.out.println(insuranceChengduMedical.toString());

				list.add(insuranceChengduMedical);
			}
		}

		return list;
	}

	/**
	 * http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=25 养老保险个人账户明细信息
	 */
	public static void yanglaogerenmingxi() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\SS\\chengdu\\yanglaogeren.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);
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
				System.out.print(year);
				System.out.print("\t");
				System.out.print(months);
				System.out.print("\t");
				System.out.print(yearCompanySum);
				System.out.print("\t");
				System.out.print(yearPersonalSum);
				System.out.print("\t");
				System.out.print(moneySum);
				System.out.print("\t");
				System.out.print(companyBalance);
				System.out.print("\t");
				System.out.print(personnelBalance);
				System.out.print("\t");
				System.out.print(yearCompanyInterest);
				System.out.print("\t");
				System.out.print(yearPersonalInterest);
				System.out.print("\t");
				System.out.print(lateLastYearPersonalInterest);
				System.out.print("\t");
				System.out.print(lateLastYearCompanyInterest);
				System.out.print("\t");
				System.out.print(yearRepairMonths);
				System.out.print("\t");
				System.out.print(yearRepairCompanySum);
				System.out.print("\t");
				System.out.println(yearRepairPersonalSum);
			}
		}

	}

	/**
	 * http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=3 养老缴费明细
	 */
	public static void yanglaodata() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\SS\\chengdu\\unemployment.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);
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
				System.out.print(payMonth);
				System.out.print("\t");
				System.out.print(company);
				System.out.print("\t");
				System.out.print(payBase);
				System.out.print("\t");
				System.out.print(companyPay);
				System.out.print("\t");
				System.out.print(personalPay);
				System.out.print("\t");
				System.out.print(companyProportion);
				System.out.print("\t");
				System.out.print(personnelProportion);
				System.out.print("\t");
				System.out.println(receivablesDate);
			}
		}

	}

	/**
	 * http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=2
	 * 获取InsuranceChengduSummary
	 */
	public static void baoxiandata() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\SS\\chengdu\\shebao.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);
		Elements link1 = doc.getElementsByTag("tr");

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
				System.out.print(insuranceType);
				System.out.print("\t");
				System.out.print(companyNum);
				System.out.print("\t");
				System.out.print(company);
				System.out.print("\t");
				System.out.print(insuranceState);
				System.out.print("\t");
				System.out.print(firstInsuranceDate);
				System.out.print("\t");
				System.out.print(personnelPayType);
				System.out.print("\t");
				System.out.println(handlingAgency);
			}
		}

	}

	public static String getInfo(WebClient webClient) throws Exception {
		String urlData = "http://insurance.cdhrss.gov.cn/insurance/index.jsp";
		// String urlData =
		// "http://insurance.cdhrss.gov.cn/insurance/index1111.jsp";
		WebRequest webRequest1 = new WebRequest(new URL(urlData), HttpMethod.GET);
		HtmlPage searchPage1 = webClient.getPage(webRequest1);
		// webClient.waitForBackgroundJavaScript(30000);
		int status = searchPage1.getWebResponse().getStatusCode();
		System.out.println("status------------------------------------------" + status);
		String html = searchPage1.asXml();
		// System.out.println("getUserInfo1------------------------------------------"+html);
		return html;
	}

	/**
	 * 获取个人信息
	 * 
	 * @param webClient
	 * @throws Exception
	 */
	public static void getUserInfo(WebClient webClient) throws Exception {
		String urlData = "http://insurance.cdhrss.gov.cn/QueryInsuranceInfo.do?flag=1";
		WebRequest webRequest1 = new WebRequest(new URL(urlData), HttpMethod.GET);
		HtmlPage searchPage1 = webClient.getPage(webRequest1);
		System.out.println("------------------------------------------");
		System.out.println(searchPage1.asXml());
	}

	/**
	 * 个人信息解析
	 */
	public static void userdata() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\SS\\chengdu\\geren.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);
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
		System.out.println("个人编码：" + personalNumber);
		System.out.println("姓名：" + name);
		System.out.println("身份证号码：" + idNum);
		System.out.println("性别：" + sex);
		System.out.println("出生日期：" + birthdate);
		System.out.println("参工时间：" + workDate);
		System.out.println("人员状态：" + personnelStatus);
		System.out.println("个人身份：" + personalIdentity);
		System.out.println("经办机构：" + handlingAgency);
		System.out.println("开户银行：" + openingBank);
		System.out.println("银行账户：" + bankAccount);
		System.out.println("移动电话：" + mobilePhone);

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

}
