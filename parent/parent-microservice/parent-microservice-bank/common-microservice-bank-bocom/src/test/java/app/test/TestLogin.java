package app.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.collections.map.HashedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.ocr.utils.AbstractChaoJiYingHandler;


public class TestLogin extends AbstractChaoJiYingHandler{
	
	private static String driverPath = "D:\\software\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
	private static WebDriver driver = null;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) throws Exception {
		
		Properties props=System.getProperties(); 
		String osName = props.getProperty("os.name"); //操作系统名称
		String osArch = props.getProperty("os.arch"); //操作系统构架
		String osVersion = props.getProperty("os.version"); //操作系统版本
		
		System.out.println("当前操作系统名称："+osName);
		System.out.println("当前操作系统构架："+osArch);
		System.out.println("当前操作系统版本："+osVersion);
		
		String loginUrl = "https://pbank.95559.com.cn/personbank/logon.jsp";
		login(loginUrl);
	}

	private static void login(String loginUrl) throws Exception{
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		driver.get(loginUrl);
		Thread.sleep(3000L);
		
		WebElement ele = driver.findElement(By.id("bannerLogin"));
		Point point = ele.getLocation();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		int pX = point.getX();
		int pY = point.getY();
		
		driver.switchTo().frame("bannerLogin");
		String pageSource = driver.getPageSource();
		System.out.println(pageSource);
		
		
		WebElement usernameInput = driver.findElement(By.id("alias"));
		WebElement button = driver.findElement(By.id("login"));
		
		if(pageSource.contains("var captchaEnable = 'true'")){
			
			String path = WebDriverUnit.saveImg(driver,By.className("captchas-img-bg"),pX,pY); 
			WebElement imageInput = driver.findElement(By.id("input_captcha"));
			String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
			System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
			System.out.println("code ====>>"+code); 			
			usernameInput.click();
			
//		String[] username = {"6", "2", "2", "2", "6", "0", "0", "9", "1", "0", "0", "3", "5", "3", "1", "7", "3", "0","2"};
//		String[] password = {369258};
			
//			VirtualKeyBoard.KeyPressEx("420106198410028419", 500);
			usernameInput.sendKeys("420106198410028419");
			
			InputTab();
			Thread.sleep(1000);
			VirtualKeyBoard.KeyPressEx("369258", 1000);
			
//			VirtualKeyBoard.KeyPressEx("3", 300);
//			VirtualKeyBoard.KeyPressEx("6", 100);
//			VirtualKeyBoard.KeyPressEx("9", 100);
//			VirtualKeyBoard.KeyPressEx("2", 200);
//			VirtualKeyBoard.KeyPressEx("5", 100);
//			VirtualKeyBoard.KeyPressEx("8", 300);
			InputTab();
			Thread.sleep(1000);
//			VirtualKeyBoard.KeyPressEx(code, 100);
			imageInput.sendKeys("ASDGS");
		}else{
			usernameInput.click();
				
			VirtualKeyBoard.KeyPressEx("420106198410028419", 500);
				
			InputTab();
			Thread.sleep(1000);
			VirtualKeyBoard.KeyPressEx("369258", 2000);
			
//			VirtualKeyBoard.KeyPressEx("3", 300);
//			VirtualKeyBoard.KeyPressEx("6", 100);
//			VirtualKeyBoard.KeyPressEx("9", 100);
//			VirtualKeyBoard.KeyPressEx("2", 200);
//			VirtualKeyBoard.KeyPressEx("5", 100);
//			VirtualKeyBoard.KeyPressEx("8", 300);
		}
		
		
		button.click();
		button.click();
		Thread.sleep(5000);
		
		System.out.println("***********************************    点击之后的页面");
		writer(driver.getPageSource(),"C:/home/login1.txt");
		
		if(driver.getPageSource().contains("动态密码有效时间为5分钟，请尽快完成相关操作")){
			WebElement sms = driver.findElement(By.id("authSMSSendBtn"));
			sms.click();
			
			WebElement smsInput = driver.findElement(By.id("mobileCode"));
			WebElement button2 = driver.findElement(By.id("btnConf2"));
			
			Scanner scanner = new Scanner(System.in);
			String line = scanner.nextLine(); 
			smsInput.sendKeys(line);
			
			button2.click();
			
			String pageStr = driver.getPageSource();
			if(pageStr.contains("是否设为常用电脑")){
				WebElement button3 = driver.findElement(By.id("next"));
				button3.click();
				
				System.out.println("*****************************************************   最终登录页");
				System.out.println(driver.getPageSource());
			}
			
		}else{
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement button2 = null;
			try {
				button2 = wait.until(new Function<WebDriver, WebElement>() {  
					public WebElement apply(WebDriver driver) {   
						return driver.findElement(By.id("btnConf1"));  
					}  
				});  
			} catch (Exception e) {		
				System.out.println("未找到温馨提示页面"+ "有可能需要发送短信");
			}
			
			button2.click();
			
			System.out.println(driver.getCurrentUrl());
			
			System.out.println("*****************************************************   最终登录页");
			System.out.println(driver.getPageSource());
			writer(driver.getPageSource(),"C:/home/login2.txt");
//			writer(driver.getPageSource());
			
		    //点击账户查询
			driver.switchTo().frame("frameMain");
			
			System.out.println("***************************************** iframe");
			writer(driver.getPageSource(),"C:/home/frameMain.txt");
//			System.out.println(driver.getPageSource());
			
			driver.switchTo().frame("tranArea");
			
			System.out.println("***************************************** tranArea");
			writer(driver.getPageSource(),"C:/home/tranArea.txt");
//			System.out.println(driver.getPageSource());
			
			WebElement a = driver.findElement(By.xpath("//a[@title='账户查询']"));
			System.out.println(a.toString());
			
			a.click();
			writer(driver.getPageSource(),"C:/home/zhanghu.txt");
//			System.out.println(driver.getPageSource());
//			writer(driver.getPageSource());
			
			List<WebElement> mingxis = driver.findElements(By.xpath("//TD[a='明细']/a[1]"));
			
			
			Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
			for(org.openqa.selenium.Cookie cookie : cookiesDriver){
				Cookie cookieWebClient = new Cookie("pbank.95559.com.cn", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);
			}

			for(WebElement mingxi:mingxis){
				
				List<String> names = new ArrayList<>();
				List<String> values = new ArrayList<>();
				
				WebElement pessionIdE = driver.findElement(By.name("PSessionId"));
				String onclick = mingxi.getAttribute("onclick");
				System.out.println("onclick==============>>"+onclick);
				
				String[] strs = onclick.split(",");
				String aa = strs[0].substring(strs[0].indexOf("'")+1, strs[0].length()-1);
				String bb = strs[1].replaceAll("'", "").replace(")", "");
				System.out.println("aa ==>"+aa+"         ,bb ===>>"+bb);
				
				String pessionId = pessionIdE.getAttribute("value");
				System.out.println("pessionId  =================>>"+pessionId);
				
				String url = "https://pbank.95559.com.cn/personbank/account/acTranRecordQuery.do?PSessionId="+pessionId+"&x-channel=0&menuCode=P002000&step=conf&cardNo="+aa+"&selectCardNo="+bb+"&startDate=20170101"
						+ "&endDate=20180526&acoAcRecord=&queryType=&serialNo=&page=1&queryFlag=&cardNumber=&isCurrDep=";
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				writer(page.getWebResponse().getContentAsString(),"C:/home/明细1,"+aa+".txt");
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				
				Elements inputs = doc.select("[name^=pageUpList]");
				if(null == inputs || inputs.size()<=0){
					continue;
				}
				
				for(Element input : inputs){
					names.add(input.attr("name"));
					values.add(input.attr("value"));
				}			
				String str = "&"+URLEncoder.encode(names.get(0))+"="+values.get(0)
				+ "&"+URLEncoder.encode(names.get(1))+"="+values.get(1)
				+ "&"+URLEncoder.encode(names.get(2))+"="+values.get(2)
				+ "&"+URLEncoder.encode(names.get(3))+"="+values.get(3)
				+ "&"+URLEncoder.encode(names.get(4))+"="+values.get(4);
				
				
				String lastStr = str;
				for(int i=2;i<50;i++){
					
					
					String param = "PSessionId="+pessionId
							+ "&x-channel=0"
							+ "&menuCode=P002000"
							+ "&step=conf"
							+ "&cardNo="+aa
							+ "&selectCardNo="+bb
							+ "&startDate=20170101"
							+ "&endDate=20180526"
							+ "&acoAcRecord="
							+ "&queryType="
							+ "&serialNo="
							+ lastStr
							+ "&page="+String.valueOf(i)
							+ "&begTme="
							+ "&endTme="
							+ "&txnKnd=";
					String url1 = "https://pbank.95559.com.cn/personbank/account/acTranRecordQuery.do?"+param;
					WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
					Page page1 = webClient.getPage(webRequest1);
					writer(page1.getWebResponse().getContentAsString(),"C:/home/明细"+i+" ,"+aa+".txt");
					Document doc1 = Jsoup.parse(page1.getWebResponse().getContentAsString());
					System.out.println("==========================================================");
					System.out.println(url1);
					System.out.println("==========================================================");
					
					
					String change = "pageUpList%5B"+(i-1)+"%5D";
					String var = str.replaceAll("pageUpList%5B\\d%5D", change);
					
					System.out.println("==================================================");
					System.out.println("当前"+i+"的var="+var);
					
					lastStr+=var;
					System.out.println("当前"+i+"的str="+lastStr);
					System.out.println("==================================================");
					if(page1.getWebResponse().getContentAsString().contains("未知系统异常")){
						break;
					}
					boolean isCrawler = parser(doc1);
					if(!isCrawler){
						break;
					}
					
				}

//			writer(page.getWebResponse().getContentAsString(),"C:/home/end.txt");
				
				//点击明细按钮
//			WebElement mingxi = driver.findElements(By.xpath("//TD[a='明细']/a[1]")).get(1);
//			System.out.println(mingxi.getText());
////			System.out.println(mingxi.toString());
////			
//			mingxi.click();
//			writer(driver.getPageSource(),"C:/home/mingxi.txt");
				names = null;
				values = null;
			}
			
			
		}	
		
	}
	
	
	public static boolean parser(Document doc){
		try{
			Element tbody = doc.getElementById("recordtbody");
			Elements trs = tbody.select("tr");
			if(null == trs || trs.size()<=0){
				System.out.println("数据已全部显示，或没有数据   。。");
				return false;
			}else{
				for(Element tr : trs){
					String transDate = tr.child(0).text();
					String transType = tr.child(1).text();
					String currency = tr.child(2).text();
					String expend = tr.child(3).child(0).text();
					String income = tr.child(4).child(0).text();
					String balance = tr.child(5).child(0).text();	
					String transSite = tr.child(6).text();
					
					System.out.println("交易时间    ：    "+transDate);
					System.out.println("交易方式    ：    "+transType);
					System.out.println("币种    ：    "+currency);
					System.out.println("支出金额    ：    "+expend);
					System.out.println("收入金额    ：    "+income);
					System.out.println("余额    ：    "+balance);
					System.out.println("交易地点    ：    "+transSite);
					
					
				}
				return true;
			}		
		}catch(Exception e){
			return false;
		}
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
	
	
	public static void writer(String page, String path){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 byte bt[] = new byte[1024];  
	        bt = page.getBytes();  
	        try {  
	            FileOutputStream in = new FileOutputStream(file);  
	            try {  
	                in.write(bt, 0, bt.length);  
	                in.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        }  
	}
	

}
