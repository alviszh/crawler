package test.housingfund;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiAccountInfo;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiPayDetailed;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiPayRecord;
import com.microservice.dao.entity.crawler.housing.xiangyang.HousingXiangyangPay;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class Tianshui {

	private static String fileSavePath = "D:\\img\\ls";

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String cookie = "[{\"domain\":\"www.tygjj.com\",\"key\":\"SESSION\",\"value\":\"529cd9dc-67b6-4a37-961e-dd69cdc83445\"},{\"domain\":\"www.tygjj.com\",\"key\":\"td_cookie\",\"value\":\"18446744072343004340\"},{\"domain\":\"www.tygjj.com\",\"key\":\"token\",\"value\":\"d2438602005b4da58e20f6461189fd9a\"}]";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// cookie = login(webClient, "620502198901281359", "633567ln");
			webClient = addcookie(cookie);
			// getgrjczh(webClient);
			// getHousingTianshuiAccountInfo();
//			getHousingTianshuiPayRecord();
//			getHousingTianshuiPayDetailed();
//			getUserInfo();
//			String ksrq = getDateBefore("yyyy", -1, 0, 0);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	private static void getUserInfo() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\TSuserinfo.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		// 个人编号
		String percode = doc.getElementById("percode").val();
		System.out.println("个人编号" + percode);

		// 个人姓名
		String pername = doc.getElementById("pername_a").val();
		System.out.println("个人姓名" + pername);

		// 证件类型
		String codetype = "";
		if (json.contains("slectInput_codetype_a[0]")) {
			String slectInput_codetype_a = json.substring(json.indexOf("slectInput_codetype_a[0]"));
			String[] split = slectInput_codetype_a.split("\"");
			if (split.length > 1) {
				codetype = split[1];
				System.out.println("证件类型" + codetype);
			}
		}

		// 证件号码
		String codeno = doc.getElementById("codeno_a").val();
		System.out.println("证件号码" + codeno);

		// 性别
		String sex = "";
		if (json.contains("slectInput_sex_a[0]")) {
			String slectInput_sex_a = json.substring(json.indexOf("slectInput_sex_a[0]"));
			String[] split = slectInput_sex_a.split("\"");
			if (split.length > 1) {
				sex = split[1];
				System.out.println("性别" + sex);
			}
		}
		
		// 出生日期
		String birthday = doc.getElementById("birthday_a").val();
		System.out.println("出生日期" + birthday);

		// 个人邮箱
		String email = doc.getElementById("email_a").val();
		System.out.println("个人邮箱" + email);

		// 移动电话
		String phone = doc.getElementById("phone_a").val();
		System.out.println("移动电话" + phone);

		// 民族
		String nation = "";
		if (json.contains("slectInput_nation_a[0]")) {
			String slectInput_nation_a = json.substring(json.indexOf("slectInput_nation_a[0]"));
			String[] split = slectInput_nation_a.split("\"");
			if (split.length > 1) {
				nation = split[1];
				System.out.println("民族" + nation);
			}
		}

		// 国籍
		String country = "";
		if (json.contains("slectInput_country_a[0]")) {
			String slectInput_country_a = json.substring(json.indexOf("slectInput_country_a[0]"));
			String[] split = slectInput_country_a.split("\"");
			if (split.length > 1) {
				country = split[1];
				System.out.println("国籍" + country);
			}
		}

		// 文化程度
		String edulev = "";
		if (json.contains("slectInput_edulev_a[0]")) {
			String slectInput_edulev_a = json.substring(json.indexOf("slectInput_edulev_a[0]"));
			String[] split = slectInput_edulev_a.split("\"");
			if (split.length > 1) {
				edulev = split[1];
				System.out.println("文化程度" + edulev);
			}
		}
		
		// 婚姻状况
		String marstate = "";
		if (json.contains("slectInput_marstate_a[0]")) {
			String slectInput_marstate_a = json.substring(json.indexOf("slectInput_marstate_a[0]"));
			String[] split = slectInput_marstate_a.split("\"");
			if (split.length > 1) {
				marstate = split[1];
				System.out.println("婚姻状况" + marstate);
			}
		}

		// 邮政编码
		String postcode = doc.getElementById("postcode_a").val();
		System.out.println("邮政编码" + postcode);
	}
	

	private static void getHousingTianshuiPayDetailed() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\TSzhmx.txt");
		String json = txt2String(file);
		List<HousingTianshuiPayDetailed> list = new ArrayList<HousingTianshuiPayDetailed>();
		HousingTianshuiPayDetailed housingTianshuiPayDetailed = null;
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject lists = jsonObj.getJSONObject("lists");
		JSONObject dataList = lists.getJSONObject("dataList");
		String listjson = dataList.getString("list");
		Object obj = new JSONTokener(listjson).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 单位编号
			String corpcode = jsonObject.getString("corpcode");
			// 缴款单位名称
			String corpname = jsonObject.getString("corpname");
			// 交易日期
			String acctime = jsonObject.getString("acctime");
			// 代理机构
			String depname = jsonObject.getString("depname");
			// 摘要
			String remark = jsonObject.getString("remark");
			// 发生金额
			String depbal = jsonObject.getString("depbal");
			// 余额
			String accbal = jsonObject.getString("accbal");

			housingTianshuiPayDetailed = new HousingTianshuiPayDetailed("", corpcode, corpname, acctime, depname,
					remark, depbal, accbal);
			list.add(housingTianshuiPayDetailed);
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 单位编号
				String corpcode = jsonObject.getString("corpcode");
				// 缴款单位名称
				String corpname = jsonObject.getString("corpname");
				// 交易日期
				String acctime = jsonObject.getString("acctime");
				// 代理机构
				String depname = jsonObject.getString("depname");
				// 摘要
				String remark = jsonObject.getString("remark");
				// 发生金额
				String depbal = jsonObject.getString("depbal");
				// 余额
				String accbal = jsonObject.getString("accbal");

				housingTianshuiPayDetailed = new HousingTianshuiPayDetailed("", corpcode, corpname, acctime, depname,
						remark, depbal, accbal);
				list.add(housingTianshuiPayDetailed);
			}
		}
		JSONArray jsonObject1 = JSONArray.fromObject(list);
		System.out.println(jsonObject1);

	}

	
	private static void getHousingTianshuiPayRecord() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\TSjcjl.txt");
		String json = txt2String(file);
		List<HousingTianshuiPayRecord> list = new ArrayList<HousingTianshuiPayRecord>();
		HousingTianshuiPayRecord housingTianshuiPayRecord = null;
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject lists = jsonObj.getJSONObject("lists");
		JSONObject dataList = lists.getJSONObject("dataList");
		String listjson = dataList.getString("list");
		Object obj = new JSONTokener(listjson).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 单位编号
			String corpcode = jsonObject.getString("corpcode");
			// 单位名称
			String corpname = jsonObject.getString("corpname");
			// 业务类型
			String paybustype = jsonObject.getString("paybustype");
			// 缴存类型
			String deptype = jsonObject.getString("deptype");
			// 缴款月份起
			String starmnh = jsonObject.getString("starmnh");
			// 缴款月份止
			String endmnh = jsonObject.getString("endmnh");
			// 单位缴存额
			String corpdepmny = jsonObject.getString("corpdepmny");
			// 个人缴存额
			String perdepmny = jsonObject.getString("perdepmny");
			// 合计缴存额
			String depmny = jsonObject.getString("depmny");
			// 业务受理时间
			String dotime = jsonObject.getString("dotime");
			// 到账时间时间
			String suertime = "";

			housingTianshuiPayRecord = new HousingTianshuiPayRecord("", corpcode, corpname, paybustype, deptype,
					starmnh, endmnh, corpdepmny, perdepmny, depmny, dotime, suertime);
			list.add(housingTianshuiPayRecord);
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 单位编号
				String corpcode = jsonObject.getString("corpcode");
				// 单位名称
				String corpname = jsonObject.getString("corpname");
				// 业务类型
				String paybustype = jsonObject.getString("paybustype");
				// 缴存类型
				String deptype = jsonObject.getString("deptype");
				// 缴款月份起
				String starmnh = jsonObject.getString("starmnh");
				// 缴款月份止
				String endmnh = jsonObject.getString("endmnh");
				// 单位缴存额
				String corpdepmny = jsonObject.getString("corpdepmny");
				// 个人缴存额
				String perdepmny = jsonObject.getString("perdepmny");
				// 合计缴存额
				String depmny = jsonObject.getString("depmny");
				// 业务受理时间
				String dotime = jsonObject.getString("dotime");
				// 到账时间时间
				String suertime = "";

				housingTianshuiPayRecord = new HousingTianshuiPayRecord("", corpcode, corpname, paybustype, deptype,
						starmnh, endmnh, corpdepmny, perdepmny, depmny, dotime, suertime);
				list.add(housingTianshuiPayRecord);
			}
		}
		JSONArray jsonObject1 = JSONArray.fromObject(list);
		System.out.println(jsonObject1);

	}

	public static String login(WebClient webClient, String username, String password) throws Exception {

		String url = "http://www.tsgjj.gov.cn:7003/login.jsp";

		HtmlPage searchPage = getHtmlPage(webClient, url, null, null);
		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		if (null != searchPage) {
			HtmlListItem gr = searchPage.getFirstByXPath("//*[@id=\"gr\"]");
			gr.click();
			String adrOptionString = "#aType option[value='4']";
			// if(StatusCodeLogin.ACCOUNT_NUM.equals("ACCOUNT_NUM")){
			// adrOptionString = "#aType option[value='3']";
			// }else
			// if(StatusCodeLogin.CO_BRANDED_CARD.equals("CO_BRANDED_CARD")){
			// adrOptionString = "#aType option[value='5']";
			// }
			HtmlOption htmlOption = (HtmlOption) searchPage.querySelector(adrOptionString);
			htmlOption.click();

			String verifycode = "1902";
			// HtmlImage image =
			// searchPage.getFirstByXPath("//img[@id='codeimg']");
			// String imageName = "111.jpg";
			// File file = new File("D:\\img\\" + imageName);
			// try {
			// image.saveAs(file);
			// String imgagePath = file.getAbsolutePath();
			//// verifycode = ocrService.callChaoJiYingService(imgagePath,
			// "1902");
			// System.out.println("验证码==========" + verifycode);
			// } catch (Exception e) {
			// System.out.println("图片有误");
			// }

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='j_username']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :" + "input[id='j_username']");
			} else {
				inputUserName.reset();
				inputUserName.setText(username);
			}
			HtmlPasswordInput inputpassword = (HtmlPasswordInput) searchPage.querySelector("input[id='j_password']");
			if (inputpassword == null) {
				throw new Exception("password input text can not found :" + "input[id='j_password']");
			} else {
				inputpassword.reset();
				inputpassword.setText(password);
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='checkCode']");
			if (inputuserjym == null) {
				throw new Exception("code input text can not found :" + "input[id='checkCode']");
			} else {
				inputuserjym.reset();
				inputuserjym.setText(verifycode);
			}
			HtmlAnchor loginA = (HtmlAnchor) searchPage.querySelector("a[onclick='login();']");
			if (loginA == null) {
				throw new Exception("login button can not found : null");
			} else {
				searchPage = loginA.click();
				String login = searchPage.getWebResponse().getContentAsString();
				System.out.println("login-------------" + login);
				HtmlSpan htmlSpan = (HtmlSpan) searchPage.querySelector("#error");
				if (htmlSpan == null) {
					System.out.println("=================+成功");
				} else {
					String textContent = htmlSpan.getTextContent();
					System.out.println("=================+失败");
					System.out.println("textContent-------------" + textContent);
				}
			}
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookieJson-------------" + cookieJson);
		return cookieJson;
	}

	/**
	 * 个人缴存账户
	 * 
	 * @throws Exception
	 */
	private static void getHousingTianshuiAccountInfo() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\TSgrjczh.txt");
		String json = txt2String(file);
		List<HousingTianshuiAccountInfo> list = new ArrayList<HousingTianshuiAccountInfo>();
		HousingTianshuiAccountInfo housingTianshuiAccountInfo = null;
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject lists = jsonObj.getJSONObject("lists");
		JSONObject dataList = lists.getJSONObject("dataList");
		String listjson = dataList.getString("list");
		Object obj = new JSONTokener(listjson).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 单位编号
			String corpcode = jsonObject.getString("corpcode");
			// 单位名称
			String corpname = jsonObject.getString("corpname");
			// 缴存基数
			String bmny = jsonObject.getString("bmny");
			// 单位月缴额
			String corpdepmny = jsonObject.getString("corpdepmny");
			// 个人月缴额
			String perdepmny = jsonObject.getString("perdepmny");
			// 合计月缴额
			String depmny = jsonObject.getString("depmny");
			// 缴止月份
			String payendmnh = jsonObject.getString("payendmnh");
			// 缴存状态
			String depstate = jsonObject.getString("depstate");
			// 账户余额
			String accbal = jsonObject.getString("accbal");

			housingTianshuiAccountInfo = new HousingTianshuiAccountInfo("", corpcode, corpname, bmny, corpdepmny,
					perdepmny, depmny, payendmnh, depstate, accbal);
			list.add(housingTianshuiAccountInfo);
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 单位编号
				String corpcode = jsonObject.getString("corpcode");
				// 单位名称
				String corpname = jsonObject.getString("corpname");
				// 缴存基数
				String bmny = jsonObject.getString("bmny");
				// 单位月缴额
				String corpdepmny = jsonObject.getString("corpdepmny");
				// 个人月缴额
				String perdepmny = jsonObject.getString("perdepmny");
				// 合计月缴额
				String depmny = jsonObject.getString("depmny");
				// 缴止月份
				String payendmnh = jsonObject.getString("payendmnh");
				// 缴存状态
				String depstate = jsonObject.getString("depstate");
				// 账户余额
				String accbal = jsonObject.getString("accbal");

				housingTianshuiAccountInfo = new HousingTianshuiAccountInfo("", corpcode, corpname, bmny, corpdepmny,
						perdepmny, depmny, payendmnh, depstate, accbal);
				list.add(housingTianshuiAccountInfo);
			}
		}
		JSONArray jsonObject1 = JSONArray.fromObject(list);
		System.out.println(jsonObject1);

	}

	/**
	 * 个人缴存账户
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	public static String getgrjczh(WebClient webClient) throws Exception {

		String url = "http://www.tsgjj.gov.cn:7003/per/perAccDetailsQueryAction!getPerAccQueryDetails.do";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		paramsList.add(new NameValuePair("ffbm", "01"));
		paramsList.add(new NameValuePair("ywfl", "01"));
		paramsList.add(new NameValuePair("ywlb", "99"));
		paramsList.add(new NameValuePair("cxlx", "01"));

		Map<String, String> map = new HashMap<String, String>();
		// map.put("Cookie",
		// "SESSION=bef95916-90dd-4858-b8b2-59655e6d03af;
		// token=2a919ff9b9db44d5bd925a93400cffd7");

		Page searchPage = getPage(webClient, url, HttpMethod.POST, null, null, null, null);
		if (null != searchPage) {
			String html = searchPage.getWebResponse().getContentAsString();
			System.out.println("html----------------------" + html);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);
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
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt,int yearCount, int monthCount, int dateCount) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, yearCount);
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

}
