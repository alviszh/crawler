package test.housingfund.jiuquan;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanUserInfo;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class JiuquanTest {

	public static void main(String[] args) {
		try {
			String cookie = "[{\"domain\":\"www.tygjj.com\",\"key\":\"SESSION\",\"value\":\"529cd9dc-67b6-4a37-961e-dd69cdc83445\"},{\"domain\":\"www.tygjj.com\",\"key\":\"td_cookie\",\"value\":\"18446744072343004340\"},{\"domain\":\"www.tygjj.com\",\"key\":\"token\",\"value\":\"d2438602005b4da58e20f6461189fd9a\"}]";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// cookie = login(webClient, "140723198907130066", "135135");
//			cookie = login(webClient, "142429198310130040", "312312");
			cookie = login(webClient, "140723198907130066", "312312");
			webClient = addcookie(cookie);
//			 getUserInfo(webClient);
			// getUserInfo();
			 getPay(webClient);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static String getPay(WebClient webClient) throws Exception {

		String url = "http://www.tygjj.com/wt-web/jcr/jcrxxcxzhmxcx_.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2000-01-01&jsrq=2017-11-02&grxx=21900468455&fontSize=13px&pageNum=1&pageSize=10&totalcount=45&pages=5&random=0.7650020131579691";

		Map<String, String> map = new HashMap<>();
		map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Page searchPage = getPage(webClient, url, HttpMethod.GET, null, null, null, map);
		System.out.println("-----Qqqqqqqq--------------");
		if (null != searchPage) {
			System.out.println("-----Yzzzzz--------------");
			String contentAsString = searchPage.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}

	private static void getUserInfo() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\userInfoJson.txt");
		String json = txt2String(file);

		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONObject jsonObject = jsonObj.getJSONObject("data");
		// 姓名
		String xingming = jsonObject.getString("xingming");
		// 出生年月
		String csny = jsonObject.getString("csny");
		// 性别
		String xingbie = jsonObject.getString("xingbie");
		// 证件类型
		String zjlx = jsonObject.getString("zjlx");
		// 证件号码
		String zjhm = jsonObject.getString("zjhm");
		// 手机号码
		String sjhm = jsonObject.getString("sjhm");
		// 固定电话号码
		String gddhhm = jsonObject.getString("gddhhm");
		// 邮政编码
		String yzbm = jsonObject.getString("yzbm");
		// 家庭月收入
		String jtysr = jsonObject.getString("jtysr");
		// 家庭住址
		String jtzz = jsonObject.getString("jtzz");
		// 婚姻状况
		String hyzk = jsonObject.getString("hyzk");
		// 贷款情况
		String dkqk = jsonObject.getString("dkqk");
		// 账户账号
		String grzh = jsonObject.getString("grzh");
		// 账户状态
		String grzhzt = jsonObject.getString("grzhzt");
		// 账户余额
		String grzhye = jsonObject.getString("grzhye");
		// 开户日期
		String djrq = jsonObject.getString("djrq");
		// 单位名称
		String dwmc = jsonObject.getString("dwmc");
		// 缴存比例
		String jcbl = jsonObject.getString("jcbl");
		// 个人缴存基数
		String grjcjs = jsonObject.getString("grjcjs");
		// 月缴存额
		String yjce = jsonObject.getString("yjce");
		// 个人存款账户开户银行名称
		String grckzhkhyhmc = jsonObject.getString("grckzhkhyhmc");
		// 个人存款账户号码
		String grckzhhm = jsonObject.getString("grckzhhm");
		HousingJiuquanUserInfo housingJiuquanUserInfo = new HousingJiuquanUserInfo("", xingming, csny, xingbie, zjlx,
				zjhm, sjhm, gddhhm, yzbm, jtysr, jtzz, hyzk, dkqk, grzh, grzhzt, grzhye, djrq, dwmc, jcbl, grjcjs, yjce,
				grckzhkhyhmc, grckzhhm);

		JSONArray jsonObject1 = JSONArray.fromObject(housingJiuquanUserInfo);
		System.out.println(jsonObject1);

	}

	public static String getUserInfo(WebClient webClient) throws Exception {

		String url = "http://www.tygjj.com/wt-web/jcr/jcrkhxxcx_mh.service";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("ffbm", "01"));
		paramsList.add(new NameValuePair("ywfl", "01"));
		paramsList.add(new NameValuePair("ywlb", "99"));
		paramsList.add(new NameValuePair("cxlx", "01"));

		Page searchPage = getPage(webClient, url, HttpMethod.POST, paramsList, null, null, null);
		if (null != searchPage) {
			String html = searchPage.getWebResponse().getContentAsString();
			System.out.println("html----------------------" + html);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}

	public static String login(WebClient webClient, String username, String password) throws Exception {

		String url = "http://www.tygjj.com/wt-web/grlogin";

		HtmlPage searchPage = getHtmlPage(webClient, url, null, null);
		if (null != searchPage) {
			HtmlImage image = searchPage.getFirstByXPath("//img[@id='captcha_img']");
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

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='username']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :" + "input[id='username']");
			} else {
				inputUserName.reset();
				inputUserName.setText(username);
			}
			HtmlPasswordInput inputpassword = (HtmlPasswordInput) searchPage.querySelector("input[id='in_password']");
			if (inputpassword == null) {
				throw new Exception("password input text can not found :" + "input[id='in_password']");
			} else {
				inputpassword.reset();
				inputpassword.setText(password);
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='captcha']");
			if (inputuserjym == null) {
				throw new Exception("code input text can not found :" + "input[id='captcha']");
			} else {
				String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");
				inputuserjym.reset();
				inputuserjym.setText(inputuserjymtemp);
			}
			HtmlButtonInput loginButton = (HtmlButtonInput) searchPage.querySelector("input[id='gr_login']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				Page page = loginButton.click();
//				 Thread.sleep(10000);
//				webClient.waitForBackgroundJavaScript(30000);
				String login = page.getWebResponse().getContentAsString();
				System.out.println("login-------------" + login);
				if (!login.contains("个人网厅登录页")) {
					System.out.println("=================+成功");
				} else {
					System.out.println("=================+失败");
				}
				Document doc = Jsoup.parse(login);
				String username_tip = doc.getElementById("username_tip").text();
				System.out.println("username_tip-------------" + username_tip);

				String pwd_tip = doc.getElementById("pwd_tip").text();
				System.out.println("pwd_tip-------------" + pwd_tip);

				String yzm_tip = doc.getElementById("yzm_tip").text();
				System.out.println("yzm_tip-------------" + yzm_tip);
				
				if(pwd_tip.contains("错误")){
					System.out.println("===================错误");
				}
				if(yzm_tip.contains("验证码错误")){
					System.out.println("===================验证码错误");
				}

				// if (login.contains("公积金账户基本信息")) {
				// System.out.println("登录成功");
				// String urldata = page.getUrl().toString();
				// String output = URLDecoder.decode(URLDecoder.decode(urldata,
				// "UTF-8"), "UTF-8");
				// System.out.println(output);
				// } else {
				// System.out.println("登录失败");
				// }
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

}
