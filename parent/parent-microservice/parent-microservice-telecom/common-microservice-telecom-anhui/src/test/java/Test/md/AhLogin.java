package Test.md;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class AhLogin extends AbstractChaoJiYingHandler {

	public static void main(String[] args) throws Exception {
		//WebClient webClient = login();
		String a = null;
		boolean equals = a.equals(null);
		System.out.println(equals);
		
		
		//WebClient webClientSms = sendSmsCode(webClient);
//		String dateBefore = getDateBefore("yyyyMM", 1);
//		System.out.println(dateBefore);
//		String decode = URLDecoder.decode("%E6%89%8B%E6%9C%BA%3A", "UTF-8");
//		System.out.println(decode);

	}
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	
//	public static String getDateBefore(String fmt, int i) throws Exception {
//		SimpleDateFormat format = new SimpleDateFormat(fmt);
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.add(Calendar.MONTH, -i);
//		Date m = c.getTime();
//		String mon = format.format(m);
//		return mon;
//	}
	
	public static WebClient sendSmsCode(WebClient webClient) throws Exception {
		WebRequest requestSettings2 = new WebRequest(new URL("http://ah.189.cn/service/bill/sendValidReq.parser"), HttpMethod.POST);
		requestSettings2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings2.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings2.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings2.setAdditionalHeader("Referer","http://ah.189.cn/service/bill/fee.parser?type=phoneAndInternetDetail");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("_v", "6a99060b9275945ae507aecef6ae5403f232629caad95ef844940e487dbda51e6f242844eddc953bc98306eb5849f14520b5c6ca275f897ea59d9f6294f76e67"));
		
		requestSettings2.setRequestParameters(params); 
		requestSettings2.setCharset(Charset.forName("UTF-8")); 
		Page hPage2 = webClient.getPage(requestSettings2);

		System.out.println("sendSmsCode---" + hPage2.getWebResponse().getContentAsString());
		
		
		return webClient;
	}

	public static WebClient login() throws Exception {
		// org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
		// "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		// 正常页面登陆 17718194181 119110
		String url = "http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.parser";
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(requestSettings);
		// System.out.println(loginPage.asXml());
		Set<Cookie> cookies0 = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies0) {
			System.out.println("登录Page获取到的cookie是：" + cookie.toString());
		}

		HtmlImage image = (HtmlImage) loginPage.querySelector("#vImg");
		String imageName = "D:\\img\\" + UUID.randomUUID() + ".jpg";
		System.out.println("imageName-------" + imageName);
		File file = new File(imageName);
		image.saveAs(file);
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", imageName);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code---------" + code);

		WebRequest requestSettings2 = new WebRequest(new URL("http://ah.189.cn/sso/LoginServlet"), HttpMethod.POST);
		requestSettings2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings2.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings2.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings2.setAdditionalHeader("Referer",
				"http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.parser");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("latnId", "551"));
		params.add(new NameValuePair("loginType", "4"));
		params.add(new NameValuePair("passWord",
				"17b3497f726221657ae2414cb02fa9ffd8d8902b0be120c2824a168b3d4f319974012589dbed1766ac0bf5cd707238f5ad6159533f874e1e8c3c691dca143198"));
		params.add(new NameValuePair("result", "true"));
		params.add(new NameValuePair("accountType", "9"));
		params.add(new NameValuePair("serviceNbr", "17718194181"));
		params.add(new NameValuePair("validCode", code));

		params.add(new NameValuePair("loginName", "17718194181"));
		params.add(new NameValuePair("passType", "0"));
		params.add(new NameValuePair("csrftoken", ""));
		params.add(new NameValuePair("ssoAuth", "0"));
		params.add(new NameValuePair("returnUrl", "%2Fservice%2Faccount%2Finit.parser"));
		params.add(new NameValuePair("sysId", "1003"));

		requestSettings2.setRequestParameters(params);

		requestSettings2.setCharset(Charset.forName("UTF-8"));

		Page hPage2 = webClient.getPage(requestSettings2);

		System.out.println("hPage2---" + hPage2.getWebResponse().getContentAsString());

		Set<Cookie> cookies1 = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies1) {
			System.out.println("登录后获取到的cookie是：" + cookie.toString());
		}

		WebRequest requestSettings3 = new WebRequest(new URL("http://ah.189.cn/service/account/usedBalance.parser"),
				HttpMethod.POST);

		requestSettings3.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings3.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings3.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings3.setAdditionalHeader("Referer", "http://ah.189.cn/service/account/init.parser");
		requestSettings3.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

		List<NameValuePair> params2 = new ArrayList<NameValuePair>();
		params2.add(new NameValuePair("serviceNum", "17718194181"));

		requestSettings3.setCharset(Charset.forName("UTF-8"));

		Page hPage3 = webClient.getPage(requestSettings3);

		String userurl = "http://ah.189.cn/service/manage/showCustInfo.parser";
		Page page = webClient.getPage(userurl);
		System.out.println("hPage3---" + hPage3.getWebResponse().getContentAsString());
		System.out.println(page.getWebResponse().getContentAsString());

		return webClient;
	}

}
