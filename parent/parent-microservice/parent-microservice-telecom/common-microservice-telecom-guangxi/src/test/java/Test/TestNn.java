package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBill;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiScore;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.domain.crawler.WebParam;
import net.sf.json.JSONObject;

public class TestNn {

	
	public static void main(String[] args) throws Exception {
//		File file = new File("C:\\Users\\Administrator\\Desktop\\d.txt"); 
//		String json = txt2String(file);
		
		
		String a = "{\"respCode\":\"0\",\"PaymentList\":[{\"AccNbr\":\"18977162692\",\"PaySource\":\"场景三充值\",\"PayDate\":\"2017-09-16 09:26:34\",\"PayCharge\":\"10000\"}]}';";
		JSONObject fromObject = JSONObject.fromObject(a.substring(0, a.length()-2));
		System.out.println(fromObject);
		
		String json = fromObject.getString("PaymentList");
		String string2 = json.substring(1, json.length()-1);
		JSONObject fromObject2 = JSONObject.fromObject(string2);
		String num = fromObject2.getString("AccNbr");
		String type = fromObject2.getString("PaySource");
		String datea = fromObject2.getString("PayDate");
		String money = fromObject2.getString("PayCharge");
		System.out.println(type+num);
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
	public static String getData()
	{
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -2);
			Date m = c.getTime();
			String mon = format.format(m);
			return mon;
	}
	
	
	
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Elements element, String keyword, String tag) {
		Elements es = element.select(tag+":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	
	
	
//	Elements elements = doc.getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
//	TelecomGuangxiScore t = new TelecomGuangxiScore();
//	List l = new ArrayList();
//	for (int i = 1; i < elements.size(); i++) {
//		Elements element = elements.get(i).getElementsByTag("td");
//		for (Element element2 : element) {
//			l.add(element2.text());
//		}
//		
//	}System.out.println(l);
	//System.out.println(elements);
		//JSONObject fromObject = JSONObject.fromObject(string2+"");
		//System.out.println(fromObject);
	  //  WebClient webClient = addcookie(cookie);
	  // sendCode = sendCode(webClient);
	//	System.out.println(pageHtml.getWebResponse().getContentAsString());
	//	String cookie="[{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"7061712624\"},{\"domain\":\".189.cn\",\"key\":\"pgv_si\",\"value\":\"s6570080256\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"01B640C2D429D41387A05B4B1A59D24A\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"18977162692\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"gx.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".omniture.cn\",\"key\":\"svid\",\"value\":\"AFC50D0369FAA0C\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmt\",\"value\":\"1\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"138\"},{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"6558ed08e4995545c4f888ab51e06451\"},{\"domain\":\".189.cn\",\"key\":\"pgv_pvi\",\"value\":\"7086036992\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utma\",\"value\":\"234928087.12839109.1505296089.1505296089.1505296089.1\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"110%2C4773%2C6066\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmz\",\"value\":\"234928087.1505296089.1.1.utmcsr\u003d(direct)|utmccn\u003d(direct)|utmcmd\u003d(none)\"},{\"domain\":\".hm.baidu.com\",\"key\":\"HMACCOUNT\",\"value\":\"636A273268F6B637\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"9B67867EDF663B25\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1505296089\"},{\"domain\":\".189.cn\",\"key\":\"s_sq\",\"value\":\"%5B%5BB%5D%5D\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170000000004473345\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"3E6A4ADDDD2664B4055A2E01DD9A92AC-n1\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmb\",\"value\":\"234928087.4.10.1505296089\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"h535SJLMB1Z6Xn1b28cVuYHuFwno9kSMf9AtctROZfL%2FMstKTCuNbcSl6TwX9I2yNtuZ3JrjSsYOo2MysBXL9ujiU8jrFaJJDqeUKucct8XDJR6eBxOg4cfAS%2FUob8TbDWaq1U5X60c%3D\"},{\"domain\":\".gx.189.cn\",\"key\":\"__utmc\",\"value\":\"234928087\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10021\"},{\"domain\":\"www.189.cn\",\"key\":\"insight_v\",\"value\":\"20151104\"},{\"domain\":\"gx.189.cn\",\"key\":\"JSESSIONID\",\"value\":\"lBl6onHqQUTB07vzdsSw0VviYCblR3IQ6JikGP9dI5Wvr8STXEdS!895323461\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"114\"},{\"domain\":\".gx.189.cn\",\"key\":\"Hm_lpvt_8b5c429f5193dc4a670ff0814155f3fe\",\"value\":\"1505296094\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"689D4170-EC85-4485-93C9-593BDE2CF7CA\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX196VyFxGoENWd3Kq94YIeRQxM7tIdJPz%2BkD%2Bf3pnDWkxlnz%2B72RvzCEWlLXpwraGtjsWK7lit5%2FXOrF9QwUHd9KM3qh7uBQf%2BE%3D\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"11D439388AF6177A-10455DD966F382E3\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"UWWDUTLcUhhFbNTi7%2B03%2F23Ot0abfc1cZBp4GGP0LcnIDUM8eCmKEA%3D%3D\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"gx\"}]";
	//	WebClient webClient = addcookie(cookie);
		
		//账单
	//	String url="http://gx.189.cn/service/bill_detail_analysis.jsp";
//		
//		
//		String url1="http://gx.189.cn/chaxun/iframe/actioncenter.jsp";
//		
	//	String url2="http://gx.189.cn/service/manage/contactslist.jsp";
//		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do";
//		
//		String url3="http://gx.189.cn/chaxun/iframe/actioncenter.jsp?SERV_NO=FCX-10&QRY_NUM=18977162692&QRY_AREA_CODE=1100&BUREAUCODE=1100&ACCNBR=18977162692&PROD_TYPE=2020966";
	//	Page page = webClient.getPage(url2);
	//	System.out.println(page.getWebResponse().getContentAsString());
//		String string = page.getWebResponse().getContentAsString();
//		System.out.println("-------------------------======"+string);
//		int i = string.indexOf("total");
//		System.out.println("_____----------------=============="+i);
//		String substring = string.substring(548, string.length()-1);
//		System.out.println(substring);
		
		// getJiFenInfo(webClient);
		
	
	private static WebClient getPageHtml(WebClient webClient) throws Exception
	{

			//WebClient webClient = addcookie(taskMobile);

			String urlData = "http://gx.189.cn/service/bill/getRand.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode=1100&ACC_NBR=18172055939&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME=18172055939&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE=201709&ACCT_DATE_1=201709&PASSWORD=&CUST_NAME=&CARD_TYPE=1&CARD_NO=";
			//Page page = getPage(webClient, taskMobile, urlData, null);
			webClient.getPage(urlData);
			
			String cookie = CommonUnit.transcookieToJson(webClient);
			System.out.println("--------"+cookie);
			// String url =
			// "http://www.189.cn/service/my189/costQuery/4G-flow.html?fastcode=01841174&cityCode=sx";
			//String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01841174";
			//Page htmlpage = getHtml(urlData, webClient);
			//tracer.addTag("TelecomNanningParser.getphonecode---验证码返回数据:",taskMobile.getTaskid() + "---html:" + htmlpage.getWebResponse().getContentAsString());

			// tracer.addTag("TelecomShanxiParser.getphonecode---验证码返回数据cookie:",
			// taskMobile.getTaskid() + "---cookie:" +
			// CommonUnit.transcookieToJson(webClient));
			return webClient;
	}
	
	private static String sendCode(WebClient webClient) throws Exception
	{
		//String urlData = "http://gx.189.cn/service/bill/getRand.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode="+taskMobile.getAreacode()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE="+getTime()+"&ACCT_DATE_1="+getTime()+"&PASSWORD=&CUST_NAME=&CARD_TYPE=1&CARD_NO=";
		//String url="http://gx.189.cn/service/bill/getRand.jsp?NUM=18172055939&V_PASSWORD=770349&CUST_NAME=%E6%9D%A8%E7%A3%8A&CARD_NO=340602199307040416&CARD_TYPE=1&RAND_TYPE=002";
		String url0="http://gx.189.cn/public/realname/checkRealName.jsp?NUM=18172055939&V_PASSWORD=794917&CUST_NAME=%E6%9D%A8%E7%A3%8A&CARD_NO=340602199307040416&CARD_TYPE=1&RAND_TYPE=003";
		Page page = webClient.getPage(url0);
		System.out.println(page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("系统繁忙，请稍候重试！"))
		{
			String url2="http://gx.189.cn/chaxun/iframe/inxxall_new.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode=1100&ACC_NBR=18172055939&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME=18172055939&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE=201709&ACCT_DATE_1=201709&PASSWORD=794917&CUST_NAME=%E6%9D%A8%E7%A3%8A&CARD_TYPE=1&CARD_NO=340602199307040416";
			Page page2 = webClient.getPage(url2);
			System.out.println("---------------------------==============================="+page2.getWebResponse().getContentAsString());
		}
		return null;
	}
	private static String getTime()
	{
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");//可以方便地修改日期格式
		String hehe = dateFormat.format( now );
		System.out.println(hehe);
		return hehe;
	}

//	private static void getJiFenInfo(WebClient webClient)
//			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
//		String url = "http://gx.189.cn/chaxun/iframe/user_center.jsp?SERV_NO=FCX-1";
//		String url1 = "http://www.189.cn/dqmh/my189/initMy189home.do";
//		HtmlPage page = webClient.getPage(url);
//		System.out.println(page.getWebResponse().getContentAsString());
//
//		// LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
//		// "org.apache.commons.logging.impl.NoOpLog");
//		// java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
//		// java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
//	}

	private static WebClient login() throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getCache().setMaxSize(0);
		String url = "http://login.189.cn/web/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");// logon_name
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");// logon_passwd
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("18172055939");
		passwordInput.setText("741258");
		
	//	String url1="http://login.189.cn/web/captcha?undefined&source=login&width=100&height=37&0.8627952227967222";
		
		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
		}
		webClient = htmlpage2.getWebClient();

		// 第一次登陆进去的界面
		String url33 = "http://www.189.cn/gx/";
		HtmlPage page = webClient.getPage(url33);
		WebClient webClient2 = page.getWebClient();
		// HtmlAnchor a = page.getFirstByXPath("//a[@id='00260329']");
		// HtmlPage page2 = a.click();
		String url4="http://gx.189.cn/chaxun/iframe/zyywcx_new_div.jsp";
		HtmlPage page2 = webClient2.getPage(url4);
		WebClient webClient3 = page2.getWebClient();
		
		
//		String url3 = "http://gx.189.cn/chaxun/iframe/user_center.jsp";
//		HtmlPage page3 = webClient.getPage(url3);
//		HtmlTextInput username1 = (HtmlTextInput) page3.getFirstByXPath("//input[@id='logon_name']");// logon_name
//		HtmlPasswordInput passwordInput1 = (HtmlPasswordInput) page3.getFirstByXPath("//input[@id='logon_passwd']");// logon_passwd
//		username1.setText("18977162692");
//		passwordInput1.setText("162534");
//		// Document doc =
//		// Jsoup.parse(page3.getWebResponse().getContentAsString());
//		// Elements elements = doc.getElementsByClass("dl");
//		HtmlAnchor firstByXPath = page3.getFirstByXPath("//a[@class='dl']");
//		HtmlPage page4 = firstByXPath.click();
//		// Document doc =
//		// Jsoup.parse(page4.getWebResponse().getContentAsString());
//		// Elements elements = doc.getElementsByClass("name_xz");
//		// System.out.println("======================--------"+elements);
//		// System.out.println("-----------======================------------"+page4.getWebResponse().getContentAsString());
//		WebClient webClient2 = page4.getWebClient();
//		String urlHtml="http://gx.189.cn/chaxun/iframe/qdcx.jsp?ACC_NBR=18172055939&PROD_TYPE=2100297";
//		HtmlPage page2 = webClient2.getPage(urlHtml);
//		System.out.println("--------------------------========================"+page2.getWebResponse().getContentAsString());
//		WebClient webClient3 = page2.getWebClient();
//		String cookieString = CommonUnit.transcookieToJson(page2.getWebClient());
//		System.out.println("cookieString---------------------->" + cookieString);
//		
//		WebClient weClient = addcookie(cookieString);
//		String xingurl = "http://xjfw.189.cn/tykf-itr-services/modules/starService/medalWall/indexPC.html";
//		HtmlPage page2 = webClient.getPage(xingurl);
//		System.out.println("__________==============================--------------" + page2.getWebResponse().getContentAsString());

		return webClient3;
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

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	
	
	public static  String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
}