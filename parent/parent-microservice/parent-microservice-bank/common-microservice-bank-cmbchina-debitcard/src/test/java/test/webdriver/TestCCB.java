package test.webdriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;
import test.winio.User32;
import test.winio.VKMapping;
import test.winio.VirtualKeyBoard;

public class TestCCB {
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static String SKEY = "";
	private static WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
	private static WebDriver driver = null;
	
	public static void main(String[] args) throws Exception{

		String baseUrl = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_00?SERVLET_NAME=B2CMainPlat_00&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=CLOGIN&DESKTOP=0&EXIT_PAGE=login.jsp&WANGZHANGLOGIN=&FORMEPAY=2"; 
		seleniumLogin(baseUrl);
//		htmlUnitLogin(baseUrl);
		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Map<String,String> map = new HashMap<String, String>();
		map.put("X-Requested-With", "XMLHttpRequest");
//		map.put("Accept", "*/*");
//		map.put("Content-Type", "application/x-www-form-urlencoded");
//		map.put("Accept-Encoding", "gzip, deflate, sdch, br");
//		map.put("Cache-Control", "no-cache");
//		map.put("Accept-Language", "zh-CN");
		
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url1 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY+"&TXCODE=NCST02&BLKTYPE=ALL&ISPRIVATE=0&_="+System.currentTimeMillis();
		getHtml(url1,null,null);
		
		String url2 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N31010&SKEY="+SKEY+"&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY;
		getHtml(url2,null,null);
		
/*		HtmlElement mingxi = page2.getFirstByXPath("//span[@data_id='mingxi']");
		System.out.println("明细按钮  =="+mingxi.asXml());
		
		HtmlPage html = mingxi.click();
		System.out.println("详情页 ===》 "+html.asXml());*/
		String url3 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N60003&SKEY="+SKEY+"&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY;
		getHtml(url3,null,null);
		
		String url4 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY+"&TXCODE=JF0000";		//积分与星级
		getHtml(url4,null,map);
				
		String url5 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY+"&TXCODE=N00998";
		getHtml(url5,null,map);
		
		String url6 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY+"&TXCODE=EBD001&ADNO=FULLMENU";
		getHtml(url6,null,map);
		
		String url7 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N12000&SKEY="+SKEY+"&USERID=411422198812083950&BRANCHID=410000000&Udt_Ind=0&isAjaxRequest=true&getTime="+System.currentTimeMillis();
		getHtml(url7,null,null);
		
		String url8 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY+"&TXCODE=EBD001&ADNO=IDXFRM06&PREFIXONLY=1";
		getHtml(url8,null,null);
		
		String url9 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY+"&TXCODE=EBD001&ADNO=IDXFRM03&PREFIXONLY=1";
		getHtml(url9,null,null);
		
		String url11 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID=411422198812083950&BRANCHID=410000000&SKEY="+SKEY+"&TXCODE=310103";
		getHtml(url11,null,null);
		
		String url12 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N31000&SKEY="+SKEY+"&USERID=411422198812083950&BRANCHID=410000000&SELECT_TYPE=all";
		HtmlPage page = getHtml(url12,null,null);
		
		getParam(page);
		
		String url13 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N31001&SKEY="+SKEY+"&USERID=411422198812083950&BRANCHID=110000000&ACC_NO=6217000010077013236&ACC_TYPE_FLAG=lct&CurTypeDesc=%E4%BA%BA%E6%B0%91%E5%B8%81&AccAlias=&AgreeType=N&BranchDesc=%E5%8C%97%E4%BA%AC%E5%B8%82&SEND_USERID=&NOACCTJ=1";
		HtmlPage page13 = getHtml(url13,null,null);
		HtmlElement mingxi = (HtmlElement) page.getElementsByTagName("a").get(0);
		System.out.println("明细按钮  =="+mingxi.asXml());
		
		HtmlPage html = mingxi.click();
		System.out.println("日志查询  ==》"+html.getWebResponse().getContentAsString());
		
		getSignAndCode(html);
		
		String url14 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&SKEY="+SKEY+"&USERID=411422198812083950&BRANCHID=110000000&TXCODE=310201&PAGE=1&ACC_NO=6217000010077013236&ACC_SIGN=0101010&PDT_CODE=0101&STR_USERID=411422198812083950&SEND_USERID=&TXTYPE=1";
		HtmlPage searchPage = getHtml(url14,null,null);
		String param = getResultParam(searchPage);

		String url15 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=310204&isAjaxRequest=true&SKEY="+SKEY+"&ACC_TYPE=0&TXTYPE=0&BRANCHID=110000000&USERID=411422198812083950&ACC_NO=6217000010077013236";
		getHtml(url15,null,null);
		
		String url16 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1";
		HtmlPage resultPage = getHtml(param,url16);
		
		Document doc = Jsoup.parse(resultPage.getWebResponse().getContentAsString());
		Element form = doc.getElementById("jhform");
		Elements trs = form.getElementsByClass("td_span");
		if(trs.first().attr("zcsr").equals("|")){
			
		}else{
			getResult(trs);
		}
	}
	
	
	private static void htmlUnitLogin(String baseUrl) {
		
		WebRequest webRequest = null;
		try {
			webRequest = new WebRequest(new URL(baseUrl), HttpMethod.GET);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}	
		
		webRequest.setAdditionalHeader("Host", "ibsbjstar.ccb.com.cn");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

		HtmlPage page = null;
		try {
			page = webClient.getPage(webRequest);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 		
		int status = page.getWebResponse().getStatusCode();
		
		if(200 == status){
			HtmlTextInput loginName = page.getFirstByXPath("//input[@id='USERID']");
			HtmlPasswordInput password = page.getFirstByXPath("//input[@id='LOGPASS']");
			HtmlSubmitInput button = page.getFirstByXPath("//input[@id='loginButton']");
			
			loginName.setText("411422198812083950");
			password.setText("198812083950mm");
			try {
				HtmlPage loginPage = button.click();
				System.out.println("登录后  ==》"+loginPage.getWebResponse().getContentAsString());
				
				getSKEY(loginPage.getWebResponse().getContentAsString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


	private static void getResult(Elements trs) {
		
		for(Element tr : trs){

			Elements tds = tr.getElementsByTag("td");
			System.out.println("记账日 ： "+tds.get(0).text());
			
			String time = parserData(tds.get(1));
			System.out.println("交易时间 ； "+tds.get(1).text()+time);
			
			String zhichu = parserData(tds.get(2));
			System.out.println("支出  ： "+zhichu);
			
			String shouru = parserData(tds.get(3));
			System.out.println("收入 ： "+shouru);
			
			String yue = parserData(tds.get(4));
			System.out.println("余额 ： "+yue);
			
//			System.out.println("*********************************************************");
//			System.out.println(tr.toString());
//			System.out.println("*********************************************************");
			String otherAccount = parserData(tr.select("tr>script").first());
			System.out.println("对方账户  ： "+otherAccount);
			
			String otherName = parserData(tds.get(5));
			System.out.println("对方名称  : "+otherName);
			
			String bizhong = parserData(tds.get(6));
			System.out.println("币种  ： "+bizhong);
			
			String detail = parserData(tds.get(7));
			System.out.println("摘要  ： "+detail);

			String location = parserData(tds.get(8));
			System.out.println("交易地点  ： "+location);
		}
		
	}
	
	public static String parserData(Element td){
		
		if(StringUtils.isBlank(td.attr("title"))){
			Elements scripts = td.getElementsByTag("script");
			if(null != scripts && scripts.size()>0){
				
				if(td.tagName().equals("script")){					
					String script = td.toString();
					Pattern pattern = Pattern.compile("\\d{15}");
					Matcher m = pattern.matcher(script);
					if(m.find()){
						return m.group(0);
					}else{
						return null;
					}
					
				}else{
					String script = td.getElementsByTag("script").first().toString();
					String data = script.substring(script.indexOf("(")+2, script.indexOf(")")-1);
					return data;					
				}
			}else{
				return td.text();
			}			
		}else{
			String title = td.attr("title");
			return title;
		}
		
	}


	private static void getSignAndCode(HtmlPage html) {
		Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());
		
		Element es = doc.select("a:contains(明细)").first();
		System.out.println(es.toString());
		
		String onclick = es.attr("onclick");
		
		System.out.println(onclick);
		
		String last = onclick.substring(onclick.indexOf("|")+1, onclick.indexOf(")")-1);
		
		System.out.println(last);
		
		String[] str1 = last.split("\\|");
	}


	private static void getParam(HtmlPage page) {
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		
		Elements lis = doc.select("[names=ACC_NO|BRANCHID|ACC_TYPE_FLAG|CurTypeDesc|AccAlias|AgreeType|BranchDesc|TXCODE]");
		if(null != lis){
			for(Element li : lis){
				if(li.text().contains("信用卡")){
					System.out.println("这个卡是信用卡！");
				}else{
					String value = li.attr("values");
					System.out.println("参数获取 == >"+value);
				}
			}
		}
	}


	private static HtmlPage getHtml(String param, String url16) {
		HtmlPage page = null;
		try {
			WebRequest request = null;
			request = new WebRequest(new URL(url16),HttpMethod.POST);
			
			request.setAdditionalHeader("Connection", "keep-alive");
			request.setAdditionalHeader("Host", "ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("Origin", "https://ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			request.setAdditionalHeader("Connection", "keep-alive");	
			request.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			request.setAdditionalHeader("Pragma", "no-cache");
			request.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			
			
			
			request.setRequestBody(param);
			page = webClient.getPage(request);			
			System.out.println("此账户所有卡号  ==》"+page.getWebResponse().getContentAsString());			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;
	}


	private static String getResultParam(HtmlPage searchPage) {
		
//		Document doc = Jsoup.parse(searchPage.getWebResponse().getContentAsString());
//		CCBParamBean ccbParamBean = new CCBParamBean();
		
		Document doc = Jsoup.parse(searchPage.getWebResponse().getContentAsString());
		Element username = doc.getElementById("l_username");
		System.out.println(username);
		String l_username = username.attr("value");
		System.out.println(l_username);
		
		String param = "ACC_NO=6217000010077013236&"
				+ "ACCSIGN=0101010|0101|人民币|0|0&"
				+ "START_DATE=20170801&"
				+ "END_DATE=20170823&"
				+ "xiaxian=&"
				+ "shangxian=&"
				+ "select=&"
				+ "yzacc=&"
				+ "yzflag=0&"
				+ "yztxcode=310200&"
				+ "flagnext=1&"
				+ "INOUTFLAG=&"
				+ "QUEFlag=1&"
				+ "AMTDOWN=&"
				+ "AMTUP=&"
				+ "TXCODE=310200&"
				+ "SKEY="+SKEY+"&"
				+ "USERID=411422198812083950&"
				+ "SEND_USERID=&"
				+ "STR_USERID=411422198812083950&"
				+ "BRANCHID=110000000&"
				+ "PAGE=1&"
				+ "CURRENT_PAGE=1&"
				+ "PDT_CODE=0101&"
				+ "BANK_NAME=北京市&"
				+ "ACC_TYPE_NAME=&"
				+ "CUR_NAME=&"
				+ "ACC_ALIAS=&"
				+ "A_STR=&"
				+ "A_STR_TEMP=&"
				+ "IS_UPDATE=1&"
				+ "UPDATE_DETAIL=1&"
				+ "v_acc=6217000010077013236&"
				+ "v_sign=0101010|0101&"
				+ "DEPOSIT_BKNO=0&"
				+ "SEQUENCE_NO=0&"
				+ "ACCTYPE2=12&"
				+ "LUGANGTONG=0&"
				+ "CURRENCE_NAME=人民币&"
				+ "B_STR=&"
				+ "v_acc2=6217000010077013236&"
				+ "v_sign2=0101010&"
				+ "QUERY_ACC_DETAIL_FLAG=310201&"
				+ "v_acc_type=0&"
				+ "ACC_SIGN=0101010&"
				+ "ACC_SIGN_TEM=0101010&"
				+ "COMPLETENESS=&"
				+ "FILESEARCHSTR=&"
				+ "zcAllTmp=0.00&"
				+ "srAllTmp=0.00&"
				+ "clientFileName=&"
				/*+ "l_acc_no=6217000010043278798&"
				+ "l_acc_no_u=6217000010043278798&"
				+ "l_acc_al=&"
				+ "l_branch=北京市&"
				+ "l_branchcode=110000000&"
				+ "l_acc_sign=6217000010043278798&"
				+ "l_acc_type=12&"
				+ "l_cur_desc=人民币&"
				+ "l_acc_e=0&"
				+ "l_userid=411422198812083950&"
				+ "l_username=闫永军&"*/
				+ "l_acc_no=6217000010077013236&"
				+ "l_acc_no_u=6217000010077013236&"
				+ "l_acc_al=&"
				+ "l_branch=北京市&"
				+ "l_branchcode=110000000&"
				+ "l_acc_sign=6217000010077013236&"
				+ "l_acc_type=12&"
				+ "l_cur_desc=人民币&"
				+ "l_acc_e=0&"
				+ "l_userid=411422198812083950&"
				+ "l_username=闫永军&";
				/*+ "l_acc_no=6227002432210422719&"
				+ "l_acc_no_u=6227002432210422719&"
				+ "l_acc_al=&"
				+ "l_branch=河南省&"
				+ "l_branchcode=410000000&"
				+ "l_acc_sign=6227002432210422719&"
				+ "l_acc_type=12&"
				+ "l_cur_desc=人民币&"
				+ "l_acc_e=0&"
				+ "l_userid=411422198812083950&"
				+ "l_username=闫永军";	*/	
		
		
		return param;
	}


	public static WebClient seleniumLogin(String url) throws Exception{
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(url);
		
		WebElement usernameInput = driver.findElement(By.id("USERID"));
		usernameInput.click();
		WebElement button = driver.findElement(By.id("loginButton"));
		
		String[] username = {"4", "1", "1", "4", "2", "2", "1", "9", "8", "8", "1", "2", "0", "8", "3", "9", "5", "0"};
		String[] password = {"1", "9", "8", "8", "1", "2", "0", "8", "3", "9", "5", "0", "m", "m"};
		
		Input(username);
		InputTab();
		Input(password);
		
		button.click();
		Thread.sleep(5000);
		
		getSKEY(driver.getPageSource());

		Set<org.openqa.selenium.Cookie> cookies1 = driver.manage().getCookies();
		for(org.openqa.selenium.Cookie cookie : cookies1){
			System.out.println("执行js后 的 cookie = "+cookie.getName()+" : "+cookie.getValue());
			Cookie cookieWebClient = new Cookie("ibsbjstar.ccb.com.cn", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		driver.quit();
		return webClient;
		
	}
	
	
	private static void getSKEY(String pageSource) {
		Document doc = Jsoup.parse(pageSource);
		Element form = doc.getElementById("form4Switch");
		Element input = form.select("[name=SKEY]").first();
		
		SKEY = input.attr("value");
		System.out.println(SKEY);
		
		Element script = doc.getElementsByTag("script").first();		
		System.out.println("script ==>"+script);
		
		String json = script.toString();
		String lastJson = json.substring(json.indexOf("{"), json.indexOf("}")+1);
		System.out.println("lastJson ==>"+lastJson);
	}


	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}
	
	public static HtmlPage getHtml(String url, List<NameValuePair> params, Map<String,String> headers){
		
		HtmlPage page = null;
		try {
			WebRequest request = null;
			if(null == params){
				request = new WebRequest(new URL(url),HttpMethod.GET);				
			}else{
				request = new WebRequest(new URL(url),HttpMethod.POST);
				for(NameValuePair pair : params){
					request.getRequestParameters().add(pair);
				}
			}
			
			if(null != headers){				
				for (Map.Entry<String, String> entry : headers.entrySet()) {  						  
					request.setAdditionalHeader(entry.getKey(), entry.getValue());					  
				}
			}
			request.setAdditionalHeader("Connection", "keep-alive");
			request.setAdditionalHeader("Host", "ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("Origin", "https://ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				
			page = webClient.getPage(request);			
			System.out.println("此账户所有卡号  ==》"+page.getWebResponse().getContentAsString());			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;
		
	}

}
