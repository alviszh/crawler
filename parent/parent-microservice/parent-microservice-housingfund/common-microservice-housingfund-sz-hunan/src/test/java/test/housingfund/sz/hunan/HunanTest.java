package test.housingfund.sz.hunan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.sz.hunan.HousingSZHunanPay;
import com.microservice.dao.entity.crawler.housing.sz.hunan.HousingSZHunanUserInfo;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class HunanTest {

	public static void main(String[] args) {
		try {
			String cookie = "[{\"domain\":\"wsbs.dggjj.cn\",\"key\":\"JSESSIONID\",\"value\":\"2SvNZyJG1ztJnnJT3nthkwKWnW0Yz8Jx8M0GR3X52lyNQyhhVPKr!131932556\"}]";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// cookie = login(webClient, "431222199005070069", "111111");
//			cookie = loginhidden(webClient, "431222199005070069", "11111111", "0");
			cookie = loginhidden(webClient, "123", "321", "0");
			webClient = addcookie(cookie);
			// getPay(webClient,"0909149484");
			// getPayS(webClient);
//			 getUserInfo();
			// getPayjsonParam();
			// getPayEnd();
//			 String par = "%B5%B1%C7%B0%C4%EA%B6%C8";
//			 String output = URLDecoder.decode(par, "GBK");
//			 System.out.println(output);

//			getPay();

			// List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
			// paramsList1.add(new NameValuePair("sfzh", "431222199005070069"));
			// paramsList1.add(new NameValuePair("zgxm", "张祎璐"));
			// paramsList1.add(new NameValuePair("zgzh", "05000200655752"));
			// paramsList1.add(new NameValuePair("dwbm", "02068710"));
			// paramsList1.add(new NameValuePair("cxyd", "当前年度"));
			// paramsList1.add(new NameValuePair("zgzt", "正常"));

//			for (int i = 0; i < 17; i++) {
//				String dateBefore = getDateBefore("yyyy", i);
//				System.out.println(dateBefore);
//			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// https://persons.shgjj.com/MainServlet?username=celina428&password=ss123456&imagecode=7375&SUBMIT.x=42&SUBMIT.y=15&password_md5=BAF0F0AFD319BF489C5FE7D5A44B7A52&ID=0
			e.printStackTrace();
		}
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mone = format.format(m);
		c.setTime(m);
		c.add(Calendar.YEAR, -1);
		m = c.getTime();
		String mons = format.format(m);
		return mons + "-" + mone;
	}

	private static void getPay() throws Exception {
		File file = new File("C:\\Users\\Administrator\\Desktop\\mingxi.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
//		Elements option = doc.getElementsByTag("option");
//		for (Element element : option) {
//			String text = element.text();
//			System.out.println(text);
//		}
		Elements elementsByAttributeValue = doc.getElementsByAttributeValue("name", "totalpages");
		String text = elementsByAttributeValue.get(0).val();
		System.out.println(text);

		// List<HousingSZHunanPay> list = new ArrayList<>();
		// HousingSZHunanPay housingSZHunanPay = null;
		// Elements tr = doc.getElementsByTag("tr");
		// for (Element element : tr) {
		// Elements td = element.getElementsByTag("td");
		// if (td.size() == 6) {
		// String date = td.get(0).text();
		// if (!"日期".equals(date)) {
		// String debitAmount = td.get(1).text();
		// String creditAmount = td.get(2).text();
		// String balance = td.get(3).text();
		// String lendingDirection = td.get(4).text();
		// String summary = td.get(5).text();
		// housingSZHunanPay = new HousingSZHunanPay("", date, debitAmount,
		// creditAmount, balance,
		// lendingDirection, summary);
		// list.add(housingSZHunanPay);
		// }
		//
		// }
		// }

//		JSONArray jsonObject = JSONArray.fromObject(list);
//		System.out.println("jsonObject-----------" + jsonObject);

	}

	private static void getUserInfo() throws Exception {
		File file = new File("C:\\Users\\Administrator\\Desktop\\ttt.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);

		String name = getNextLabelByKeyword(doc, "职工姓名");
		String bank = getNextLabelByKeyword(doc, "银行账号");
		String idNum = getNextLabelByKeyword(doc, "身份证号");
		String staffAccount = getNextLabelByKeyword(doc, "职工账号");
		String company = getNextLabelByKeyword(doc, "所在单位");
		String office = getNextLabelByKeyword(doc, "所属办事处");
		String openingDate = getNextLabelByKeyword(doc, "开户日期");
		String state = getNextLabelByKeyword(doc, "当前状态");
		String basemny = getNextLabelByKeyword(doc, "月缴基数");
		String proportion = getNextLabelByKeyword(doc, "个人/单位");// 缴存比例
		String monthlyPay = getNextLabelByKeyword(doc, "月缴金额");
		String yearBalance = getNextLabelByKeyword(doc, "上年余额");
		String companyMonthlyPay = getNextLabelByKeyword(doc, "单位月缴额");
		String yearPay = getNextLabelByKeyword(doc, "本年补缴");
		String psnMonthlyPay = getNextLabelByKeyword(doc, "个人月缴额");
		String yearDraw = getNextLabelByKeyword(doc, "本年支取");
		String yearPayable = getNextLabelByKeyword(doc, "本年缴交");
		String interest = getNextLabelByKeyword(doc, "本年利息");
		String yearInto = getNextLabelByKeyword(doc, "本年转入");
		String balance = getNextLabelByKeyword(doc, "公积金余额");
		String yearMonth = getNextLabelByKeyword(doc, "缴至年月");

		HousingSZHunanUserInfo housingSZHunanUserInfo = new HousingSZHunanUserInfo("", name, bank, idNum, staffAccount,
				company, office, openingDate, state, basemny, proportion, monthlyPay, yearBalance, companyMonthlyPay,
				yearPay, psnMonthlyPay, yearDraw, yearPayable, interest, yearInto, balance, yearMonth);

		JSONArray jsonObject = JSONArray.fromObject(housingSZHunanUserInfo);
		System.out.println("jsonObject-----------" + jsonObject);

	}

	public static String loginhidden(WebClient webClient, String username, String password, String loginType)
			throws Exception {

		String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/zfbzsq/login_hidden.jsp";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("password", password));
		paramsList.add(new NameValuePair("sfzh", username));
		paramsList.add(new NameValuePair("cxyd", "当前年度"));
		paramsList.add(new NameValuePair("dbname", "gjjmx9"));
		paramsList.add(new NameValuePair("dlfs", loginType));

		Page searchPage = getPage(webClient, url, null, paramsList, "GBK", null, null);

		if (null != searchPage) {
			String searchPageString = searchPage.getWebResponse().getContentAsString();
			System.out.println("searchPageString-------------" + searchPageString);
			Document doc = Jsoup.parse(searchPageString);

			Elements selectzgzh = doc.select("input[name=zgzh]");
			String zgzh = selectzgzh.get(0).val();
			System.out.println("zgzh-------------" + zgzh);

			Elements selectsfzh = doc.select("input[name=sfzh]");
			String sfzh = selectsfzh.get(0).val();
			System.out.println("sfzh-------------" + sfzh);

			Elements selectzgxm = doc.select("input[name=zgxm]");
			String zgxm = selectzgxm.get(0).val();
			System.out.println("zgxm-------------" + zgxm);

			Elements selectdwbm = doc.select("input[name=dwbm]");
			String dwbm = selectdwbm.get(0).val();
			System.out.println("dwbm-------------" + dwbm);

			Elements selectzgzt = doc.select("input[name=zgzt]");
			String zgzt = selectzgzt.get(0).val();
			System.out.println("zgzt-------------" + zgzt);

			String alert = WebCrawler.getAlertMsg();
			System.out.println("alert-------------" + alert);

			String url1 = "http://www.xzgjj.com:7001/wscx/zfbzgl/gjjxxcx/gjjxx_cx.jsp";
			List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
			paramsList1.add(new NameValuePair("sfzh", sfzh));
			paramsList1.add(new NameValuePair("zgxm", zgxm));
			paramsList1.add(new NameValuePair("zgzh", zgzh));
			paramsList1.add(new NameValuePair("dwbm", dwbm));
			paramsList1.add(new NameValuePair("cxyd", "当前年度"));
			paramsList1.add(new NameValuePair("zgzt", zgzt));

			Page searchPage1 = getPage(webClient, url1, null, paramsList1, "GBK", null, null);
			if (null != searchPage1) {
				String searchPageString1 = searchPage1.getWebResponse().getContentAsString();
				System.out.println("searchPageString1-------------" + searchPageString1);
			}

		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookieJson-------------" + cookieJson);
		return cookieJson;

	}

	public static String login(WebClient webClient, String username, String password) throws Exception {

		String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/zfbzsq/login.jsp";

		HtmlPage searchPage = getHtmlPage(webClient, url, null, null);
		if (null != searchPage) {
			String searchPageString = searchPage.getWebResponse().getContentAsString();
			System.out.println("searchPageString-------------" + searchPageString);
			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[name='sfzh']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :input[name='sfzh']");
			} else {
				inputUserName.reset();
				// inputUserName.setText(username);
			}
			HtmlPasswordInput inputpassword = (HtmlPasswordInput) searchPage.querySelector("input[name='password']");
			if (inputpassword == null) {
				throw new Exception("password input text can not found :input[name='password']");
			} else {
				inputpassword.reset();
				// inputpassword.setText(password);
			}

			HtmlImageInput loginButton = (HtmlImageInput) searchPage.querySelector("input[onclick='ok_onclick()']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				Page page = loginButton.click();
				String login = page.getWebResponse().getContentAsString();
				String alert = WebCrawler.getAlertMsg();
				System.out.println("login-------------" + login);
				System.out.println("alert-------------" + alert);
				if (login.contains("退出系统")) {
					System.out.println("登录成功");
				} else {
					System.out.println("登录失败");
				}
			}
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookieJson-------------" + cookieJson);
		return cookieJson;

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

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
		// webRequest.setAdditionalHeader("Accept", "*/*");
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		// webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		// webRequest.setAdditionalHeader("ajaxRequest", "true");
		// webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Type",
		// "multipart/form-data");
		// webRequest.setAdditionalHeader("Host", "wsbs.dggjj.cn");
		// webRequest.setAdditionalHeader("Origin", "http://wsbs.dggjj.cn");
		// webRequest.setAdditionalHeader("Referer",
		// "http://wsbs.dggjj.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnAccInfo.jsp?psnAcc=0909149484&psnName=%25E4%25BD%2595%25E4%25BD%25A9%25E7%258E%25B2&certNo=441900198907160626&orgName=%25E4%25B8%259C%25E8%258E%259E%25E5%25B8%2582%25E6%2598%2593%25E6%2589%258D%25E4%25BA%25BA%25E5%258A%259B%25E8%25B5%2584%25E6%25BA%2590%25E9%25A1%25BE%25E9%2597%25AE%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8&psnAccSt=%25E6%25AD%25A3%25E5%25B8%25B8&bal=8580.48&orgEndPayTime=2017-09&pay=152&originalBase=1520");
		// webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT
		// 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
		// Chrome/61.0.3163.100 Safari/537.36");
		// webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

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

	public static WebClient addcookie(String cookie) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
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
		Elements es = document.select("tr[class=jtpsoft] td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

}
