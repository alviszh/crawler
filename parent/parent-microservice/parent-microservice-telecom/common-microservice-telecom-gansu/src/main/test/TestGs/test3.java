package TestGs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.codehaus.jettison.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuCall;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

public class test3 {
public static void main(String[] args) throws Exception {
		
		WebClient webClient = 	login();
		getJiFenInfo(webClient);
		
		//System.out.println(getSixMonth());

		//panjs2(webClient);
		
		//panjs();
	
	//File file = new File("C:\\Users\\Administrator\\Desktop\\qqq.txt"); 
	//String xmlStr = txt2String(file);
	
	
	
	
//	String getSignInfo = xmlStr.substring(xmlStr.indexOf("<script type="text\\javascript" language="+javascript+">") + 8, xmlStr.indexOf("</script>"));
	
	
	
//	Document doc = Jsoup.parse(xmlStr);
//	
//	Elements elementsByTag = doc.getElementsByTag("script");
//	System.out.println(elementsByTag.size());
//	for (Element element : elementsByTag) {
//		System.out.println("---------");
//		System.out.println(element);
		
		
		
		
//		Elements elements = element.getElementsByTag("td");
//		System.out.println(elements.size());
//		for (Element element2 : elements) {
//			System.out.println("=============================");
//			System.out.println(element2);
//		}
		
		
		
	}
	
	
	
	
	
	
//	String personalNumber = getNextLabelByKeyword(doc, "联系人");
//	String lineNum = doc.select("table.colum4").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();
//	String cardType = doc.getElementsByClass("colum4").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(2).text();
//	System.out.println(personalNumber+"---"+lineNum+"-------"+cardType);
	
	
	


public static void getJiFenInfo(WebClient webClient) throws Exception {
	
	//余额
	String url1="http://gs.189.cn/web/v7/fee/getBalanceFeeInfo.parser?productInfo=4%3A18093808808&checkedProductName=%E6%89%8B%E6%9C%BA%3A18093808808&typeName=%E5%AE%9E%E6%97%B6%E4%BD%99%E9%A2%9D%E6%9F%A5%E8%AF%A2&functionCode=010001001003";
	//积分
	String url = "http://gs.189.cn/web/self/scoreSearchV7.parser?productGroup=4%3A18093808808&beginDate=201701&endDate=201708";
	//业务
	String url2 ="http://gs.189.cn/web/change/functionChoiceUnsubscribeQueryV7.parser?productInfo=4%3A18093808808&checkedProductName=%25E6%2589%258B%25E6%259C%25BA%25EF%25BC%259A18093808808&functionEnterId=4&businessId=null";
	//账单
    String url3 = "http://gs.189.cn/web/v7/fee/getBillPay.parser?productInfo=4%3A18093808808&checkedProductName=%E6%89%8B%E6%9C%BA%3A18093808808";
    //缴费
	String url4 ="http://gs.189.cn/web/pay2/dealCheckV7.parser?numberType=4%3A18093808808&beginTime=201708&endTime=2017-08&productInfo=4%3A18093808808&validatecode=&busitype=3&mobilenum=&rand=Name";

	String url5 ="http://gs.189.cn/web/fee/preDetailedFee.parser?randT=916492&productGroup=4:18093808808&orderDetailType=6&queryMonth=201709###";
	
	//短信
	String url6="http://gs.189.cn/web/json/searchDetailedFee.parser?timestamp=1504257402885&productGroup=4:18093808808&orderDetailType=8&queryMonth=201708";
	//语音
	String url7="http://gs.189.cn/web/json/searchDetailedFee.parser?timestamp=1504257402885&productGroup=4:18093808808&orderDetailType=6&queryMonth=2017-08";
	
	String url8="http://gs.189.cn/web/fee/getBillPayDetail.parser?t=1504593616554&detailMonth=201708&productInfo=4%3A18093808808";

	WebRequest webRequest = new WebRequest(new URL(url4), HttpMethod.POST);
	
	HtmlPage page = webClient.getPage(webRequest);
	System.out.println(page.getWebResponse().getContentAsString()+"--------------------");
//	WebRequest webRequest3 = page.getWebResponse().getWebRequest();
//	
//      webRequest = new WebRequest(new URL(url7), HttpMethod.POST);
//	
//	Page page2 = webClient.getPage(webRequest);
//	
	
	//WebClient webClient3 = WebCrawler.getInstance().getWebClient();
	 
//	System.out.println(page.getWebResponse().getContentAsString());
//	String xml = page.asXml();
	
//	Document document = Jsoup.parse(xml);
//	Element div = document.select("#hiddenresult").first();
//	Elements trs = div.select("tr");
//	for (Element tr : trs) {
//		Elements tds = tr.select("td");
//		List<String> txt = new ArrayList<>();
//		for (Element td : tds) {
//			String text = td.text();
//			txt.add(text);
//		}
//		System.out.println(txt.toString());
//	}
//	DomElement elementById = page.getElementById("hiddenresult").getFirstElementChild().getFirstElementChild();
//	String textContent = elementById.getTextContent();
//	String[] split = textContent.split("");
//	for (String string : split) {
//		System.out.println(string);
//	}
//	String dstring=null;
//	WebRequest webRequest2 = page.getWebResponse().getWebRequest();
	
//	WebClient webClient2 = page.getWebClient();
//	for (int i = 0; i <6; i++) {
//		 dstring = getDateBefore("yyyy-MM",i);
//	}
//	String url88="http://gs.189.cn/web/fee/getBillPayDetail.action?t=1504593616554&detailMonth="+dstring+"&productInfo=4%3A18093808808";
	
	//Thread.sleep(5000);
	
//	Page page2 = webClient2.getPage(url8);
//	System.out.println(page2.getWebResponse().getContentAsString());
	//JSONObject json3 = new JSONObject();
	
//	JSONObject jsonObject = JSONObject.fromObject(page2.getWebResponse().getContentAsString());
//	System.out.println(jsonObject);
//	JSONObject json = JSONObject.fromObject(page2.getWebResponse().getContentAsString());
//	String string = json.getString("jsonResult").replace("\\", "");
//	JSONObject json1 = JSONObject.fromObject(string);
//	String string2 = json1.getString("trList");
//	List<TelecomGansuCall> list = new ArrayList<TelecomGansuCall>();
//	JSONArray json2 = new JSONArray(string2);
//	TelecomGansuCall telecomGansuCall=null;
//	for (int i = 0; i < json2.length(); i++) {
//		JSONObject j  = JSONObject.fromObject(json2.get(i)+"");
//		telecomGansuCall = new TelecomGansuCall();
//		telecomGansuCall.setCallDate(j.getString("val0"));
//		telecomGansuCall.setCallStatus(j.getString("val1"));
//		telecomGansuCall.setHisNum(j.getString("val2"));
//		telecomGansuCall.setCallTime(j.getString("val3"));
//		list.add(telecomGansuCall);
//		
//    }
//	System.out.println(list.toString()+"333333333333333333333333333333333333333333333333");
	//System.out.println(page2.getWebResponse().getContentAsString());
	
//	Page page2 = webClient2.getPage(url1);
//	System.out.println(page2.getWebResponse().getContentAsString());
	
	
  //  String url8 ="http://gs.189.cn/web/fee/getBillPayDetail.action?t=1504684720515&detailMonth=2017-08&productInfo=4%3A18093808808";
	
//		HtmlTable object = page2.getFirstByXPath("//table[@class='bill_list']");
//		Document doc = Jsoup.parse(object.asXml());
//		
//		String netMoney = getNextLabelByKeyword(doc,"流量包费");
//		System.out.println(netMoney);
	
	
	//System.out.println(sb7.replaceAll("</td>+","").replaceAll("\\+", "").replaceAll("<td align='center'>", "").replaceAll("\\s*", "").replaceAll(";", ""));
//	StringBuilder sb4 = new StringBuilder();
//	for (String string : split3) {
//		sb4.append(string);
//	}
//	String sb5 = sb4.toString();
//	String[] split4 = sb5.split("</p>");
//	for (int i = 1; i < split4.length; i++) {
//		System.out.println(split4[i]);
//	}
	
	
	
//	System.out.println(page2.getWebResponse().getContentAsString());
	//Document doc = Jsoup.parse(page2.getWebResponse().getContentAsString());
//	System.out.println(page2.getWebResponse().getContentAsString());
//	String string = page2.getWebResponse().getContentAsString();
//	String[] split = string.split("detailStr");
//	StringBuilder sb = new StringBuilder();
//	for (String string2 : split) {
//		sb.append(string2);
//	}
//	String[] split2 = sb.toString().split("'<tr>'");
//	for (int j = 1; j < split2.length; j++) {
//		System.out.println(split2[1]+"-------------------------------------------------------------");
//	}
	
	
	
 	//Document doc = Jsoup.parse(page2.getWebResponse().getContentAsString());
	//System.out.println(page2.getWebResponse().getContentAsString());
	
//	String string = page2.getWebResponse().getContentAsString();
//	//System.out.println(string);
//	WebClient client = page2.getWebClient();
//	Page page3 = client.getPage(url7);
//	System.out.println(page3.getWebResponse().getContentAsString());
	//System.out.println(object.asXml());
	//System.out.println(element1+"------------------------------------------");
	
//	String string = json.getString("billPayList");
//	JSONArray json2 =new JSONArray(string);
//	for (int i = 0; i < json2.length(); i++) {
//		System.out.println(json2.get(i));
//	}
	
//	String string2 = string.substring(1, string.length()-1);
//	System.out.println(string2);
//	JSONObject json1= JSONObject.fromObject(string2);
//	System.out.println(json1);
//	System.out.println(string2);
	
		//JSONObject fromObject = JSONObject.fromObject(string3);
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
	Elements es = document.select("td:contains(" + keyword + ")");
	if (null != es && es.size() > 0) {
		Element element = es.first();
		Element nextElement = element.nextElementSibling();
		if (null != nextElement) {
			return nextElement.text();
		}
	}
	return null;
}

	public static WebClient login() throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getCache().setMaxSize(0);
		String url = "http://login.189.cn/web/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtShowPwd']");
		
		HtmlImage image = html.getFirstByXPath("//img[@id='imgCaptcha']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		image.saveAs(file);
		HtmlTextInput inputuserjym = html.getFirstByXPath("//input[@id='txtCaptcha']");
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		inputuserjym.setText(inputValue);	
		
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("18093808808");
		passwordInput.setText("566041");

		HtmlPage htmlpage2 = button.click();
		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
		}
		url = "http://www.189.cn/dqmh/my189/initMy189home.do"; 
		webClient = htmlpage2.getWebClient();
		HtmlPage page = getHtml(url, webClient);
		WebClient webClient2 = page.getWebClient();
		System.out.println(page.getWebResponse().getContentAsString());
		return webClient2;
	}

//	public static void panjs( ) {
//		
//		// 设置必要参数
//		DesiredCapabilities dcaps = new DesiredCapabilities();
//		// ssl证书支持
//		dcaps.setCapability("acceptSslCerts", true);
//		// 截屏支持
//		dcaps.setCapability("takesScreenshot", false);
//		// css搜索支持
//		 dcaps.setCapability("cssSelectorsEnabled", true);
//		// js支持
//		dcaps.setJavascriptEnabled(true);
//		// 驱动支持
//		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "E:\\phantomjs\\phantomjs.exe");
//		// 创建无界面浏览器对象
//		PhantomJSDriver driver = new PhantomJSDriver(dcaps);
//
//		driver.get("http://login.189.cn/login");
//		//System.out.println(driver.getPageSource());
//
//		WebElement userNameElement = driver.findElementByXPath("//input[@id='txtAccount']");
//		WebElement pwdElement = driver.findElementByXPath("//*[@id='txtShowPwd']");
//		
//		userNameElement.sendKeys("13366777357");
//		pwdElement.sendKeys("591414");
//		
//		// 获取登录按钮
//		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//		WebElement loginButton = driver.findElementByXPath("//a[@id='loginbtn']");
//
//	//	System.out.println(driver.getPageSource());
//		
//		loginButton.click();
//		/*// 设置线程休眠时间等待页面加载完成
//		try {
//			Thread.sleep(10000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String windowHandle = driver.getWindowHandle();
//		driver.switchTo().window(windowHandle);
//		System.out.println(driver.getPageSource());*/
//		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do");
//		
//		
//		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01390638");
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//System.out.println(driver.getPageSource());
//		
//		driver.get("http://bj.189.cn/iframe/feequery/detailBillIndex.action");
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(driver.getPageSource());
//		
//		  Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();  
//		  for(org.openqa.selenium.Cookie cookie : cookies){
//			  System.out.println(cookie.getDomain()+":"+cookie.getName()+":"+cookie.getValue());
//		  }
//		/*WebElement callthremClieck = driver.findElementByXPath("//*[@id='01390638']");
//		callthremClieck.click();
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("============================================");
//		System.out.println(driver.getPageSource());
//		String windowHandle = driver.getWindowHandle();
//		try {
//			Thread.sleep(20000L);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		driver.switchTo().window(windowHandle);
//		System.out.println("============================================");
//		System.out.println(driver.getPageSource());
//		*/
//	}
//
//	public static void panjs2(WebClient webClient){
//		
//		// 设置必要参数
//				DesiredCapabilities dcaps = new DesiredCapabilities();
//				// ssl证书支持
//				dcaps.setCapability("acceptSslCerts", true);
//				// 截屏支持
//				dcaps.setCapability("takesScreenshot", false);
//				// css搜索支持
//				 dcaps.setCapability("cssSelectorsEnabled", true);
//				// js支持
//				dcaps.setJavascriptEnabled(true);
//				// 驱动支持
//				dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "E:\\phantomjs\\phantomjs.exe");
//				// 创建无界面浏览器对象
//				PhantomJSDriver driver = new PhantomJSDriver(dcaps);
//				
//				Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//				for (Cookie cookie : cookies) {
//					org.openqa.selenium.Cookie cookiesele = new org.openqa.selenium.Cookie( cookie.getName(),
//							cookie.getValue());
//					driver.manage().addCookie(cookiesele);
//				}
//				/*
//				 * File file=new File("E:/phantomjs");
//				 * System.setProperty("phantomjs.binary.path",file+"/phantomjs.exe");
//				 * WebDriver driver=new PhantomJSDriver();
//				 */
//				driver.get("http://www.189.cn/dqmh/my189/initMy189home.do");
//				System.out.println(driver.getPageSource());
//
//			
//	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static  String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
