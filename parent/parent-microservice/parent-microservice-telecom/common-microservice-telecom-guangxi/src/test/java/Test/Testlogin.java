
package Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.domain.crawler.WebParam;
import app.service.ChaoJiYingOcrService;

public class Testlogin {
	@Autowired
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	public static void main(String[] args) throws Exception {
		WebParam webParam = login();
//		WebClient webClient = webParam.getWebClient();
//		String cookieString = CommonUnit.transcookieToJson(webClient);
//		System.out.println(cookieString);
		
		
//		getphone();
//		setphone();
//		crawler();
	}
	
	// 通过URL获得HTMLpage
		public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		}
		
		
		
		//登陆
	public static WebParam login() throws Exception
	{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		// 正常页面登陆
//		String url = "http://login.189.cn/web/login";
//		HtmlPage html = getHtml(url, webClient);
//		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");// logon_name
//		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");// logon_passwd
//		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
//
//		HtmlImage imgCaptcha = (HtmlImage)html.getFirstByXPath("//img[@id='imgCaptcha']");//verify_key
//		
//		username.setText("18172055939");
//		passwordInput.setText("741258");
//		String[] split = imgCaptcha.asXml().split("src");
//		String[] split2 = split[1].split("id");
//		String substring = split2[0].substring(1);
//		if(substring.length() != 3)
//		{
//			HtmlTextInput txtCaptcha = (HtmlTextInput)html.getFirstByXPath("//input[@id='txtCaptcha']");//logon_valid
//			String code = chaoJiYingOcrService.getVerifycode(imgCaptcha,"1902");
//			txtCaptcha.setText(code);
//		}
//		
//		
//		
//		
//		HtmlPage htmlpage2 = button.click();
//		// System.out.println("---------------------"+htmlpage2.getWebResponse().getContentAsString());
//		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
//			System.out.println("=======失败==============");
//		}
//		webClient = htmlpage2.getWebClient();
//
//		// 第一次登陆进去的界面
//		String url33 = "http://www.189.cn/gx/";
//		HtmlPage page = webClient.getPage(url33);
//		webClient = page.getWebClient();
		// 登陆广西
		String url3 = "http://gx.189.cn/chaxun/iframe/user_center.jsp";
		HtmlPage page3 = webClient.getPage(url3);
		HtmlTextInput username1 = (HtmlTextInput) page3.getFirstByXPath("//input[@id='logon_name']");// logon_name
		HtmlPasswordInput passwordInput1 = (HtmlPasswordInput) page3.getFirstByXPath("//input[@id='logon_passwd']");// logon_passwd
		username1.setText("18172055939");
		passwordInput1.setText("741258");
		HtmlAnchor firstByXPath = page3.getFirstByXPath("//a[@class='dl']");
		HtmlPage page4 = firstByXPath.click();
		String cookieString = CommonUnit.transcookieToJson(page4.getWebClient());
		String shenfen1 = "C:\\Users\\Administrator\\Desktop\\logined.txt";
		savefile(shenfen1,page4.getWebResponse().getContentAsString());
		String urlData ="http://gx.189.cn/service/bill/getRand.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode=1100&ACC_NBR=18172055939&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME=18172055939&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE=201710&ACCT_DATE_1=201710&PASSWORD=&CUST_NAME=&CARD_TYPE=1&CARD_NO=";
//		String urlData ="http://gx.189.cn/service/bill/getRand.jsp?MOBILE_NAME=18172055939&RAND_TYPE=025&OPER_TYPE=CR1&PRODTYPE=2100297";//025

		WebClient client = addcookie(cookieString);
		Page pageData = client.getPage(urlData);
		WebParam webParam = new WebParam();
		webParam.setWebClient(client);
		return webParam;
		
	}
	
	//cookie-webclient
	public static WebClient addcookie(String cookie) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}
	
	public static void getphone() throws Exception{
		String urlData ="http://gx.189.cn/service/bill/getRand.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode=1100&ACC_NBR=18172055939&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME=18172055939&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE=201710&ACCT_DATE_1=201710&PASSWORD=&CUST_NAME=&CARD_TYPE=1&CARD_NO=";

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Page pageData = webClient.getPage(urlData);
		System.out.println(pageData.getWebResponse().getContentAsString());
	}
	
	public static void setphone() throws Exception{
		String cookie="[{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"18172055939\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"h535SJLMB1bkNxlx0W3X48MeQ0pHwP7hCQDSsN35xzoPWMvaeCtuvyM20y1hhxmbGYQfTkoIBOriE3tyEvetSGJReKEbuTO1fs0dI7ZxRjNsbns4rbWOGpgu5YzmlMttUGEWmp4Pqkw%3D\"},{\"domain\":\".189.cn\",\"key\":\"pgv_pvi\",\"value\":\"457834496\"},{\"domain\":\".189.cn\",\"key\":\"pgv_si\",\"value\":\"s5005926400\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX1%2FYCkQ5bDXSKLxujHYPkq%2B92%2FpQLc90DkqbA3NGKS4is6rfamm0QahcIRtozYm%2Fjc1T7e%2BWJ%2Bj2PDENHa45GYsZTMrD%2BlR3bXU%3D\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"gx.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lpvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1508120554\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmt\",\"value\":\"1\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"118\"},{\"domain\":\".omniture.cn\",\"key\":\"svid\",\"value\":\"1659DBEFD9248F3A\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmb\",\"value\":\"234928087.4.10.1508120550\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmc\",\"value\":\"234928087\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"574F29FC37F63195E1909A4B433782DA-n1\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"BO9sANXH%2F69mL%2B002qhLS%2FWYZ8UxrhwdcrEMNNTdcx8Y4WlojL%2Fqqg%3D%3D\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"7110048A1061501F-32678DA7CACBA9D0\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"D0214A65EF6A2316381680BA513CE166\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\".hm.baidu.com\",\"key\":\"HMACCOUNT\",\"value\":\"C724B2D8E2D5B233\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"4244640896\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"CEB52EAB9459DEF5\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"0FBCAE76-C8FC-4BFB-A56F-F4BC6ADB10A6\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"gx\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10021\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000005069414\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1508120551\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmz\",\"value\":\"234928087.1508120550.1.1.utmcsr\u003d(direct)|utmccn\u003d(direct)|utmcmd\u003d(none)\"},{\"domain\":\"gx.189.cn\",\"key\":\"JSESSIONID\",\"value\":\"sTwi_BdYbMDiN6qix59wHOE_9LgRFpIcYUXFIEICBucywq0ewt1r!-703393688\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"110%2C4855%2C6148\"},{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"c99ab2edbc889e619f18fffeb1f5ebe9\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\".189.cn\",\"key\":\"s_sq\",\"value\":\"%5B%5BB%5D%5D\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utma\",\"value\":\"234928087.929478530.1508120550.1508120550.1508120550.1\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"51\"},{\"domain\":\"www.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"}]";
//		String cookie="";
//		String cookie="";
		String	mytext2 = java.net.URLEncoder.encode("杨磊","utf-8");
		String pwd="961942";
		String urlData = "http://gx.189.cn/public/realname/checkRealName.jsp?NUM=18172055939&V_PASSWORD="+pwd+"&CUST_NAME=" +mytext2+ "&CARD_NO=340602199307040416&CARD_TYPE=1&RAND_TYPE=002";
		 WebClient webClient = addcookie(cookie);
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		 Page page =  getHtmlPage(urlData, webClient);
		 System.out.println(page.getWebResponse().getContentAsString());
		 if(page.getWebResponse().getContentAsString().contains("系统繁忙，请稍候重试"))
		 {
			 String cookie1 = CommonUnit.transcookieToJson(webClient);
			 System.out.println(cookie1+"+++++++++++");
		 }
	}
	
	//爬取
	public static void crawler() throws Exception
	{
//		String cookie="[{\"domain\":\".189.cn\",\"key\":\"pgv_pvi\",\"value\":\"8765007872\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1507967160\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"5D5C80213AE35CB4\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"h535SJLMB1bkNxlx0W3X48MeQ0pHwP7hCQDSsN35xzoPWMvaeCtuvyM20y1hhxmbVXPePtJcMFK%2Faxu6t3k4AMr5EqzZn%2FNvNSwW5hUDlbZIdeB8qo9ij6tysrmNeAxxZGsL2SsKZHw%3D\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"64\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX18yqwMmMc5wY50zMNVKz6BSnF%2FSn%2BbYe89zCo16YuMytauK65l%2FSWoNxD46PdzhCZqSHjUIm1HuYbRgZGXvwGFMyF4Pgw%2FO%2Fz0%3D\"},{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"7b1d32ae53308c72e529ebd1617771fa\"},{\"domain\":\"gx.189.cn\",\"key\":\"JSESSIONID\",\"value\":\"HscZ14ssiW6IU-M5EakY5GkIY_pZrGwRrJbvL7mvq6zFp38TyMTt!-703393688\"},{\"domain\":\"www.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"gx\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"25B98525-7871-4862-A995-BD7AACB61629\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmt\",\"value\":\"1\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"D0214A65EF6A2316381680BA513CE166\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"614420555\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"B9EzEI7zon%2BAk9D4Tj6GGtDc%2BgwefQazzNPpd7wIJ56LgMVLBTeUBA%3D%3D\"},{\"domain\":\".hm.baidu.com\",\"key\":\"HMACCOUNT\",\"value\":\"37404A28952A9203\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmb\",\"value\":\"234928087.4.10.1507967159\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"3B5ED709A2E6490B-128014997D3C4AE8\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utma\",\"value\":\"234928087.1909340381.1507967159.1507967159.1507967159.1\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmz\",\"value\":\"234928087.1507967159.1.1.utmcsr\u003d(direct)|utmccn\u003d(direct)|utmcmd\u003d(none)\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lpvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1507967163\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"18172055939\"},{\"domain\":\"gx.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmc\",\"value\":\"234928087\"},{\"domain\":\".omniture.cn\",\"key\":\"svid\",\"value\":\"6D149A8CBF1CE52F\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"s_sq\",\"value\":\"%5B%5BB%5D%5D\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"3D172C26B6FA71A9105B473E0FCB7DA6-n1\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10021\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"pgv_si\",\"value\":\"s4867102720\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"979\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"110%2C4855%2C6148\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000005069414\"}]";
//		String cookie = "[{\"domain\":\"gx.189.cn\",\"key\":\"JSESSIONID\",\"value\":\"tH4aVmL7TCL-CagqkH0YdE1ixnS3fQfYnUDaLtrfxnDxQoXmGh4r!-1500419135\"}]";
//		String cookie = "[{\"domain\":\"www.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"D0214A65EF6A2316381680BA513CE166\"},{\"domain\":\"gx.189.cn\",\"key\":\"JSESSIONID\",\"value\":\"ojYaaq17uxyBJUI10y8KIWr3MfqYMFdW1MT_f8fsSsVkOiTOxMMd!1540194181\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"2ADB4AB9EFC4D7DBA70CD92A11DCD962-n3\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"9251362540\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"124\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX1%2B9uiuaXC2VYt0wNvPLGT2tcC9fiWqPyQhuqaFqrJJAGaKHjkcFEqK6t1BSmJxcnXzWP7A%2B9Gk84JJF9He6OB23Ul0HUWw6K0o%3D\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000005069414\"},{\"domain\":\".omniture.cn\",\"key\":\"svid\",\"value\":\"521BFD8FB8AB7599\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"vrOu1qqRoF06%2FAsrVVRQ%2BOnYdASMy%2B3fg4gbhrxGkJl8A7XWseD8eg%3D%3D\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"240%2C90%2C605\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"15C83850-A023-42EA-B3ED-B36BF7932241\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"407\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"701A92C4F64E3A6C\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10021\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"18172055939\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"h535SJLMB1bkNxlx0W3X48MeQ0pHwP7hCQDSsN35xzoPWMvaeCtuvyM20y1hhxmbVXPePtJcMFKgfxyx9xfnqjhdLVyiiElJnmWhnBuOL8dBGhck%2FmiDhfkB05kRIvJHEQBj68ottxI%3D\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"5FD0404DB371B44B-0C213224D66985C4\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"gx\"},{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"6636361b50514d22f14bb3f4f7f89a54\"}]";
		String cookie="[{\"domain\":\".189.cn\",\"key\":\"pgv_pvi\",\"value\":\"457834496\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1508120551\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"CEB52EAB9459DEF5\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"h535SJLMB1bkNxlx0W3X48MeQ0pHwP7hCQDSsN35xzoPWMvaeCtuvyM20y1hhxmbGYQfTkoIBOriE3tyEvetSGJReKEbuTO1fs0dI7ZxRjNsbns4rbWOGpgu5YzmlMttUGEWmp4Pqkw%3D\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"118\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\"gx.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\"gx.189.cn\",\"key\":\"JSESSIONID\",\"value\":\"sTwi_BdYbMDiN6qix59wHOE_9LgRFpIcYUXFIEICBucywq0ewt1r!-703393688\"},{\"domain\":\"www.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"gx\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"BO9sANXH%2F69mL%2B002qhLS%2FWYZ8UxrhwdcrEMNNTdcx8Y4WlojL%2Fqqg%3D%3D\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmt\",\"value\":\"1\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"D0214A65EF6A2316381680BA513CE166\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"4244640896\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX1%2FYCkQ5bDXSKLxujHYPkq%2B92%2FpQLc90DkqbA3NGKS4is6rfamm0QahcIRtozYm%2Fjc1T7e%2BWJ%2Bj2PDENHa45GYsZTMrD%2BlR3bXU%3D\"},{\"domain\":\".hm.baidu.com\",\"key\":\"HMACCOUNT\",\"value\":\"C724B2D8E2D5B233\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmb\",\"value\":\"234928087.4.10.1508120550\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"7110048A1061501F-32678DA7CACBA9D0\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utma\",\"value\":\"234928087.929478530.1508120550.1508120550.1508120550.1\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmz\",\"value\":\"234928087.1508120550.1.1.utmcsr\u003d(direct)|utmccn\u003d(direct)|utmcmd\u003d(none)\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lpvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1508120554\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"18172055939\"},{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"c99ab2edbc889e619f18fffeb1f5ebe9\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmc\",\"value\":\"234928087\"},{\"domain\":\".omniture.cn\",\"key\":\"svid\",\"value\":\"1659DBEFD9248F3A\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"0FBCAE76-C8FC-4BFB-A56F-F4BC6ADB10A6\"},{\"domain\":\".189.cn\",\"key\":\"s_sq\",\"value\":\"%5B%5BB%5D%5D\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"574F29FC37F63195E1909A4B433782DA-n1\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10021\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"pgv_si\",\"value\":\"s5005926400\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"51\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"110%2C4855%2C6148\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000005069414\"}]";
		String pwd="961942";
		//String	mytext2 = java.net.URLEncoder.encode("杨磊","utf-8");
		//String urlData = "http://gx.189.cn/public/realname/checkRealName.jsp?NUM=18172055939&V_PASSWORD="+pwd+"&CUST_NAME=" +mytext2+ "&CARD_NO=340602199307040416&CARD_TYPE=1&RAND_TYPE=002";
			String urlcx = "http://gx.189.cn/chaxun/iframe/inxxall_new.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode=1100&ACC_NBR=18172055939&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME=18172055939&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE=201709&ACCT_DATE_1=201709&PASSWORD="+pwd+"&CUST_NAME=%E6%9D%A8%E7%A3%8A&CARD_TYPE=1&CARD_NO=340602199307040416";
			//String urlcx ="http://gx.189.cn/chaxun/iframe/myserv_payList.jsp?REFRESH_FLAG=2&IPAGE_INDEX=4&AccNbr=&OPEN_TYPE=&Logon_Name=&USER_FLAG=001&USE_PROTOCOL=&LOGIN_TYPE=&USER_NO=&ESFlag=8&REDIRECT_URL=%2Fchaxun%2F%3FSERV_NO%3DFCX-1&AccNbr11=18172055939&START_ASK_DATE=2017-10-01&END_ASK_DATE=2017-10-31";

			 WebClient webClient = addcookie(cookie);
	        HtmlPage page2 = webClient.getPage(urlcx);
	        System.out.println("=================="+page2.getWebResponse().getContentAsString()+"======================-------");
		    
	}
	
	public static  Page getHtmlPage(String url, WebClient webClient) throws Exception, IOException {
		WebRequest webRequest;
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;

	}
	
	//将String保存到本地
		public static void savefile(String filePath, String fileTxt) throws Exception{
			File fp=new File(filePath);
	        PrintWriter pfp= new PrintWriter(fp);
	        pfp.print(fileTxt);
	        pfp.close();
		}
	
}
