package test.housingfund.dongguan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanPay;
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanUserInfo;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class DongguanTest {

	public static void main(String[] args) {
		try {
			String cookie = "[{\"domain\":\"wsbs.dggjj.cn\",\"key\":\"JSESSIONID\",\"value\":\"2SvNZyJG1ztJnnJT3nthkwKWnW0Yz8Jx8M0GR3X52lyNQyhhVPKr!131932556\"}]";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			cookie = login(webClient, "0909149484", "441900198907160626");
			webClient = addcookie(cookie);
//			 getUserInfo(webClient);
//			getPay(webClient,"0909149484");
//			getPayS(webClient);
//			getUserInfo();
//			getPayjsonParam();
//			getPayEnd();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// https://persons.shgjj.com/MainServlet?username=celina428&password=ss123456&imagecode=7375&SUBMIT.x=42&SUBMIT.y=15&password_md5=BAF0F0AFD319BF489C5FE7D5A44B7A52&ID=0
			e.printStackTrace();
		}
	}
	private static void getPayEnd() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\B.txt");
		String json = txt2String(file);
		
		List<HousingDongguanPay> list = new ArrayList<>();
		
		HousingDongguanPay housingDongguanPay = null;
		
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject body = jsonObj.getJSONObject("body");
		JSONObject dataStores = body.getJSONObject("dataStores");
		JSONObject psnInfoDetailDs = dataStores.getJSONObject("psnInfoDetailDs");
		JSONObject rowSet = psnInfoDetailDs.getJSONObject("rowSet");
		String primary = rowSet.getString("primary");

		Object obj = new JSONTokener(primary).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 日期
			 String acctDate = jsonObject.getString("ACCT_DATE");
			 // 摘要
			 String absCodeName= jsonObject.getString("ABS_CODE_NAME");
			 // 发生额（元）
			 String ttlAmt= jsonObject.getString("TTL_AMT");
			 // 余额
			 String curBal= jsonObject.getString("CUR_BAL");
			 // 汇缴年月
			 String payYmon= jsonObject.getString("PAY_YMON");
			housingDongguanPay = new HousingDongguanPay("", acctDate, absCodeName, ttlAmt, curBal, payYmon);
			list.add(housingDongguanPay);
			
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 日期
				 String acctDate = jsonObject.getString("ACCT_DATE");
				 // 摘要
				 String absCodeName= jsonObject.getString("ABS_CODE_NAME");
				 // 发生额（元）
				 String ttlAmt= jsonObject.getString("TTL_AMT");
				 // 余额
				 String curBal= jsonObject.getString("CUR_BAL");
				 // 汇缴年月
				 String payYmon= jsonObject.getString("PAY_YMON");
				housingDongguanPay = new HousingDongguanPay("", acctDate, absCodeName, ttlAmt, curBal, payYmon);
				list.add(housingDongguanPay);
			}
		}
		
		JSONArray jsonObject = JSONArray.fromObject(list);
		System.out.println(jsonObject);

	}
	
	
	
	private static String getPayjsonParam(String json) throws Exception {

//		File file = new File("C:\\Users\\Administrator\\Desktop\\Firstpage.txt");
//		String json = txt2String(file);
		
		JSONObject jsonObj = JSONObject.fromObject(json);

		JSONObject body = jsonObj.getJSONObject("body");
		
		JSONObject dataStores = body.getJSONObject("dataStores");
		
		JSONObject psnInfoDetailDs = dataStores.getJSONObject("psnInfoDetailDs");
		
		// 名：psnInfoDetailDs
		String name = psnInfoDetailDs.getString("name");
		// 前面应该是公积金账号，后面不知道啥玩意  ：[["0909149484","12"]]
		String conditionValues = psnInfoDetailDs.getString("conditionValues");
		// 不知道啥玩意：websys.psnInfoDetailQry
		String statementName = psnInfoDetailDs.getString("statementName");
		// 不知道啥玩意： ["  PSN_ACC ","12"]
		JSONObject attributes = psnInfoDetailDs.getJSONObject("attributes");
		String psnAcc = attributes.getString("psnAcc");
		// 不知道啥玩意： hafmis
		String pool = psnInfoDetailDs.getString("pool");
		
		// 不知道啥玩意：不知道啥玩意：{"synCount":"true"}
		String parameters = "{\"synCount\":\"true\"}";
		
		
		String jsonParam = "{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"psnInfoDetailDs\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\""+name+"\",pageNumber:1,pageSize:900000,recordCount:999999,conditionValues:"+conditionValues+",parameters:{},statementName:\""+statementName+"\",attributes:{\"psnAcc\":"+psnAcc+"},pool:\""+pool+"\"}},parameters:{\"synCount\":\"true\"}}}";
		return jsonParam;

	}
	
	
	public static String getPayS(WebClient webClient,String json) throws Exception {

//		String url = "http://wsbs.dggjj.cn/web_psn/rpc.do?method=doQuery";
		
		String url = "http://wsbs.dggjj.gov.cn/web_psn/rpc.do?method=doQuery";

//		String body = "{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"psnInfoDetailDs\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"psnInfoDetailDs\",pageNumber:1,pageSize:900000,recordCount:999999,conditionValues:[[\"0909149484\",\"12\"]],parameters:{},statementName:\"websys.psnInfoDetailQry\",attributes:{\"psnAcc\":[\"  PSN_ACC \",\"12\"]},pool:\"hafmis\"}},parameters:{\"synCount\":\"true\"}}}";
		String body = getPayjsonParam(json);
		
		Map<String, String> map = new HashMap<>();
		map.put("Content-Type", "multipart/form-data");
		
		Page searchPage = getPage(webClient, url, HttpMethod.POST, null, false, body,map);
		if (null != searchPage) {
			System.out.println("------Zzzz-------------");
			System.out.println(searchPage.getWebResponse().getContentAsString());
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}

	public static String getPay(WebClient webClient,String psnAcc) throws Exception {

		 webClient = WebCrawler.getInstance().getNewWebClient();
//		String url = "http://wsbs.dggjj.cn/web_psn/psnloanquery_psnLoanAccount.do?method=psnInfoDetailQry";
		
		String url = "http://wsbs.dggjj.gov.cn/web_psn/psnloanquery_psnLoanAccount.do?method=psnInfoDetailQry";

		String body = "{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{},parameters:{\"psnAcc\":\""+psnAcc+"\"}}}";

		Map<String, String> map = new HashMap<>();
		map.put("Content-Type", "multipart/form-data");
		
		Page searchPage = getPage(webClient, url, HttpMethod.POST, null, false, body,map);
		if (null != searchPage) {
			System.out.println("-----Yzzzzz--------------");
			String contentAsString = searchPage.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			getPayS(webClient, contentAsString);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}

	private static void getUserInfo() throws Exception {

//		String json = "http://wsbs.dggjj.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnAccInfo.jsp?psnAcc=0909149484&psnName=何佩玲&certNo=441900198907160626&orgName=东莞市易才人力资源顾问有限公司&psnAccSt=正常&bal=8580.48&orgEndPayTime=2017-09&pay=152&originalBase=1520";
		String json = "http://wsbs.dggjj.gov.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnAccInfo.jsp?psnAcc=0909149484&psnName=何佩玲&certNo=441900198907160626&orgName=东莞市易才人力资源顾问有限公司&psnAccSt=正常&bal=8580.48&orgEndPayTime=2017-09&pay=152&originalBase=1520";
		
		
		Map<String, String> map = new HashMap<>();
		if (json.contains("?")) {
			String jsonData = json.substring(json.lastIndexOf("?") + 1);
			String[] data = jsonData.split("&");
			for (String string : data) {
				String[] split = string.split("=");
				map.put(split[0], split[1]);
			}
			HousingDongguanUserInfo housingDongguanUserInfo = new HousingDongguanUserInfo("", map.get("psnAcc"),
					map.get("psnName"), map.get("certNo"), map.get("orgName"), map.get("psnAccSt"), map.get("bal"),
					map.get("orgEndPayTime"), map.get("pay"), map.get("originalBase"));
			JSONObject jsonObject = JSONObject.fromObject(housingDongguanUserInfo);
			System.out.println(jsonObject);

		}

	}

	public static String getUserInfo(WebClient webClient) throws Exception {

		String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");

//		String url = "http://wsbs.dggjj.cn/web_psn/psnloanquery_psnLoanAccount.do?method=psnInfoQryOnline";
		
		String url = "http://wsbs.dggjj.gov.cn/web_psn/psnloanquery_psnLoanAccount.do?method=psnInfoQryOnline";

		String body = "{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{},parameters:{\"psnAcc\":\"0909149484\",\"certNo\":\"441900198907160626\",\"checkCodeInput\":\""
				+ inputuserjymtemp + "\"}}}";

		Page searchPage = getPage(webClient, url, HttpMethod.POST, null, false, body,null);
		if (null != searchPage) {
			System.out.println(searchPage.getWebResponse().getContentAsString());
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}
	

	public static String login(WebClient webClient, String username, String password) throws Exception {

//		String url = "http://wsbs.dggjj.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnInfoQryOnline1.jsp";

		String url = "http://wsbs.dggjj.gov.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnInfoQryOnline1.jsp";
		
		
		HtmlPage searchPage = getHtmlPage(webClient, url, null, null);
		if (null != searchPage) {
			HtmlImage image = searchPage.getFirstByXPath("//img[@id='jcaptcha']");
			try {
				String imageName = "111.jpg";
				File file = new File("D:\\img\\" + imageName);
				try {
					image.saveAs(file);
				} catch (Exception e) {
					System.out.println("图片有误");
				}
				// code = chaoJiYingOcrService.getVerifycode(image, "1902");

			} catch (Exception e) {
				e.printStackTrace();
			}

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='psnAcc']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :" + "input[name='psnAcc']");
			} else {
				inputUserName.reset();
				inputUserName.setText(username);
			}
			HtmlTextInput inputcertNo = (HtmlTextInput) searchPage.querySelector("input[id='certNo']");
			if (inputcertNo == null) {
				throw new Exception("password input text can not found :" + "input[name='password']");
			} else {
				inputcertNo.reset();
				inputcertNo.setText(password);
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='checkCode']");
			if (inputuserjym == null) {
				throw new Exception("code input text can not found :" + "input[name='imagecode']");
			} else {
				String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");
				inputuserjym.reset();
				inputuserjym.setText(inputuserjymtemp);
			}
			HtmlImage loginButton = (HtmlImage) searchPage.querySelector("img[onclick='okFun();']");
			// HtmlTextInput loginButton =
			// searchPage.getFirstByXPath("//input[@name='SUBMIT']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				Page page = loginButton.click();
				// HtmlPage page = (HtmlPage) loginButton.click();
				String alert = WebCrawler.getAlertMsg();
				System.out.println("alert-------------" + alert);
				String login = page.getWebResponse().getContentAsString();
				// System.out.println("login-------------"+login);
				if (login.contains("公积金账户基本信息")) {
					System.out.println("登录成功");
					String urldata = page.getUrl().toString();
					String output = URLDecoder.decode(URLDecoder.decode(urldata, "UTF-8"), "UTF-8");
					System.out.println(output);
				} else {
					System.out.println("登录失败");
				}
			}
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookieJson-------------"+cookieJson);
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
			Boolean code, String body,Map<String, String> map) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		
		if(null != map){
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
//		webRequest.setAdditionalHeader("Accept", "*/*");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest.setAdditionalHeader("ajaxRequest", "true");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
//		webRequest.setAdditionalHeader("Host", "wsbs.dggjj.cn");
//		webRequest.setAdditionalHeader("Origin", "http://wsbs.dggjj.cn");
//		webRequest.setAdditionalHeader("Referer", "http://wsbs.dggjj.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnAccInfo.jsp?psnAcc=0909149484&psnName=%25E4%25BD%2595%25E4%25BD%25A9%25E7%258E%25B2&certNo=441900198907160626&orgName=%25E4%25B8%259C%25E8%258E%259E%25E5%25B8%2582%25E6%2598%2593%25E6%2589%258D%25E4%25BA%25BA%25E5%258A%259B%25E8%25B5%2584%25E6%25BA%2590%25E9%25A1%25BE%25E9%2597%25AE%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8&psnAccSt=%25E6%25AD%25A3%25E5%25B8%25B8&bal=8580.48&orgEndPayTime=2017-09&pay=152&originalBase=1520");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		
		
		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
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

}
