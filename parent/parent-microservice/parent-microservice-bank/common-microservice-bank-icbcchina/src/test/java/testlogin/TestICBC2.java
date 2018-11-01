package testlogin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.JNativeService;


public class TestICBC2  extends AbstractChaoJiYingHandler{

	static String driverPath = "D:\\IE\\32\\IEDriverServer.exe";
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) throws Exception {
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
       
		JNativeService jNativeService = new JNativeService();
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_index.jsp";
//		String baseUrl = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/frame/frame_guide.jsp";
		driver.get(baseUrl);
		driver.get(baseUrl);
		/*Actions action = new Actions(driver);
		action.click(driver.findElement(By.xpath("//*[@id='onemap']/area[2]")));
		InputEnter();//
		Thread.sleep(500);
		InputTab(); //
		Thread.sleep(500);
		InputTab(); //
		Thread.sleep(500);
		InputEnter();//
		Thread.sleep(5000);
		
		String js = "document.form.submit();";
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript(js);*/
		
		String currentUrl = driver.getCurrentUrl();
		System.out.println("currentUrl--11--"+currentUrl); 
		Thread.sleep(1000L); 
		//driver.findElement(By.id("username")).sendKeys("6222030200002485674");
//		VirtualKeyBoard.KeyPressEx("6212262902006897511",50);// 
		jNativeService.Input("6212262902006897511");
		//Thread.sleep(1000L);
		InputTab(); //                  
		//Thread.sleep(1000L);
//		VirtualKeyBoard.KeyPressEx("nishuoshenme2727",50);//  
		jNativeService.Input("nishuoshenme2727");
		InputTab(); //  
		 
		//String path = WebDriverUnit.saveImg(driver, By.id("ICBC_login_frame")); 
		//driver.switchTo().frame("ICBC_login_frame"); 
		//String path = WebDriverUnit.saveImg(driver, By.id("VerifyimageFrame"));
		
		String path = WebDriverUnit.saveImg(driver,"ICBC_login_frame", By.id("VerifyimageFrame")); 
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 
//		VirtualKeyBoard.KeyPressEx(code,50);// 
		jNativeService.Input(code);
		//输入回车登录
		InputEnter();//
		
		Thread.sleep(5000);
		String currentUrl2 = driver.getCurrentUrl();
		System.out.println("currentUrl--22--"+currentUrl2); 
		String htmlsource2 = driver.getPageSource(); 
		System.out.println("htmlsource--22--"+htmlsource2);   
//		savefile("C:\\Users\\Administrator\\Desktop\\icbclogined.txt", driver.getPageSource());
		String sessionId = getSessionId(htmlsource2);
		System.out.println("-------"+sessionId+"-------");
//		getUserInfoPage(sessionId);
//		driver.quit();
		
		getTrans2(sessionId);
	}
	
	public static void getTrans2(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/includes/mybank.jsp?dse_sessionId="+sessionId;
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String txt = page.getWebResponse().getContentAsString();
		
		int i = txt.indexOf("var cardlistdata =");
		int j = txt.indexOf(";", i);
		System.out.println(i+"|"+j);
		String substring = txt.substring(i+19, j);
		System.out.println(substring);
		
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(substring); // 创建JsonObject对象
		JsonArray accountCardList = object.get("accountCardList").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String cardNum = account.get("cardNum").getAsString();
			String acctNo0 = account.get("acctNo0").getAsString();
			System.out.println("-----------------------------");
			System.out.println(cardNum);
			System.out.println("..............");
			System.out.println(acctNo0);
			
			gettrans2(sessionId, cardNum, acctNo0);
		}
	}
	
	public static void gettrans2(String sessionId, String cardNum, String acctNo0) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+sessionId+"&YETYPE=0&cardNum="+cardNum+"&acctNum="+acctNo0+"&begDate=2017-08-18&endDate=2017-11-17&Tran_flag=2&queryType=4&dse_operationName=per_AccountQueryHisdetailOp";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		InputStream contentAsStream = page.getWebResponse().getContentAsStream();
		save1(contentAsStream);
	}
	
	public static void save1(InputStream inputStream) throws Exception{

		OutputStream  outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\icbcdownload.csv");

		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1)
		{
			outputStream.write(bytes, 0, byteCount);
		}
		inputStream.close();
		outputStream.close();
    }
	
	public static void InputEnter() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter"));
		}
	}
	
	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
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
	
	//将String保存到本地
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}

	public static String getSessionId(String pageSource) throws Exception{
		Document document = Jsoup.parse(pageSource);
		Elements sessionIds = document.getElementsByAttributeValue("name", "dse_sessionId");
		String sessionId = sessionIds.get(0).val();
		return sessionId;
	}
	
	public static void getUserInfoPage(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_operationName=per_ServiceModifyCustInfoOp&jspTag=11&dse_sessionId="+sessionId;
		System.out.println(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		savefile("C:\\Users\\Administrator\\Desktop\\icbcUserInfo.txt", page.getWebResponse().getContentAsString());
	}
	
	//获取含有AccNumList的页面
	public static void details(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/includes/mybank.jsp?dse_sessionId="+sessionId;
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
//		savefile("C:\\Users\\Administrator\\Desktop\\icbcDetails.txt", page.getWebResponse().getContentAsString());
		getAccNumList(page.getWebResponse().getContentAsString());
	}
	
	public static void getAccNumList(String txt) throws Exception{
		int i = txt.indexOf("var cardlistdata =");
		int j = txt.indexOf(";", i);
		System.out.println(i+"|"+j);
		String substring = txt.substring(i+19, j);
		System.out.println(substring);
		
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(substring); // 创建JsonObject对象
		JsonArray accountCardList = object.get("accountCardList").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String cardNum = account.get("cardNum").getAsString();
			String acctNo0 = account.get("acctNo0").getAsString();
			System.out.println("-----------------------------");
			System.out.println(cardNum);
			System.out.println("..............");
			System.out.println(acctNo0);
			
		}
	}
}
