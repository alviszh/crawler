package test.webdriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard; 


public class CMBChinaTest3 {

	static String driverPath = "D:\\software\\IEDriverServer_x64\\IEDriverServer.exe";
	
	//static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	//static String driverPath = "C:\\Program Files\\Internet Explorer\\iexplore.exe";

	public static void main(String[] args) throws Exception {
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
       
        
		System.out.println("launching IE browser");
		System.setProperty("webdriver.ie.driver", driverPath );

		WebDriver driver = new InternetExplorerDriver();
		//RemoteWebDriver driver = new RemoteWebDriver(new URL("http://10.167.120.30:4444/wd/hub/"), DesiredCapabilities.internetExplorer());
		
		 driver = new InternetExplorerDriver(ieCapabilities);
		//��ʱ30s
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";
		// String baseUrl = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";
		driver.get(baseUrl);
		String currentUrl = driver.getCurrentUrl();
		//System.out.println("currentUrl--11--"+currentUrl); 
		String htmlsource = driver.getPageSource(); 
		//System.out.println("htmlsource--11--"+htmlsource); 
		

		String[] accountNum = { "6", "2", "1", "4", "8", "3", "0", "1", "6", "1", "3", "0", "0", "9", "2", "5" };
		
		String[] password = { "3", "3", "1", "4", "8", "3" }; 

		Thread.sleep(1000L);

		Input(accountNum);// �����˻�

		InputTab(); // ���� Tab �л��������

		Input(password);// ��������
		
		driver.findElement(By.id("LoginBtn")).click();//�����¼�ύ��ť
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSendCode")));//�ȴ����Ͷ��Ű�ť����
			String currentUrl2 = driver.getCurrentUrl();
			//System.out.println("currentUrl--22--"+currentUrl2); 
			String htmlsource2 = driver.getPageSource(); 
			//System.out.println("htmlsource--22--"+htmlsource2);   
			
			Thread.sleep(1500L);
			driver.findElement(By.id("btnSendCode")).click();//������Ͷ�����֤�밴ť 
		    System.out.print("������֤��.[Enter]:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String code = br.readLine(); 
			System.out.println("code: " + code); 
			driver.findElement(By.id("txtSendCode")).sendKeys(code);   //���������֤�� 
			
			Thread.sleep(1500L);
			driver.findElement(By.id("btnVerifyCode")).click();//����ύ��ť
			
		}catch (Exception e) {
			System.out.println("�쳣��Ϣ-------"+e.toString());
		}
		
		JavascriptExecutor driver_js= ((JavascriptExecutor) driver); 
		Thread.sleep(1500L);
		System.out.println("ִ��JavaScript");
		driver_js.executeScript("CallFuncEx2('A','CBANK_DEBITCARD_ACCOUNTMANAGER','AccountQuery/am_QuerySubAccount.aspx','FORM',null)");
		Thread.sleep(1000L);
		
		driver.switchTo().frame("mainWorkArea");    
		
		String currentUrl3 = driver.getCurrentUrl();
		//System.out.println("currentUrl--33--"+currentUrl3); 
		String htmlsource3 = driver.getPageSource(); 
		//System.out.println("htmlsource--33--"+htmlsource3); 
		
		System.out.println("------������ײ�ѯ------"); 
		//WebElement eleTriggerFunc = driver.findElement(By.cssSelector("a[onclick|=triggerFunc]"));
		WebElement eleTriggerFunc = driver.findElement(By.cssSelector("a[onclick]"));
		
		String onclick = eleTriggerFunc.getAttribute("onclick");
		System.out.println("onclick-------------"+onclick);
		
		Thread.sleep(1000L);
		driver_js.executeScript(onclick);
		
		
		/*
		eleTriggerFunc.click();//����ύ��ť
		
		System.out.println("------������ײ�ѯ  ���------"); 
		//getData(ClientNo,__VIEWSTATE,__EVENTVALIDATION);
		
		Thread.sleep(3000L);
		//driver.quit();
		*/
		 String dataHtml = "";
		 String currentWindow = driver.getWindowHandle(); 
         //�õ����д��ڵľ��
         Set<String> handles = driver.getWindowHandles(); 
         Iterator<String> it = handles.iterator();
         while(it.hasNext()){
             String handle = it.next(); 
             if(currentWindow.equals(handle)) continue;
             WebDriver window = driver.switchTo().window(handle);
             System.out.println("title,url = "+window.getTitle()+","+window.getCurrentUrl()+","+window.getPageSource());
             if("https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryTodayTrans.aspx".equals(window.getCurrentUrl())){
            	JavascriptExecutor driver_window= ((JavascriptExecutor) window); 
         		System.out.println("driver_windowִ��JavaScript");
         		driver_window.executeScript("triggerFunc('../AccountQuery/am_QueryHistoryTrans.aspx','FORM','_self')");
         		Thread.sleep(1000L);
         		String currentUrl4 = driver.getCurrentUrl();
        		System.out.println("currentUrl--44--"+currentUrl4); 
        		String htmlsource4 = driver.getPageSource(); 
        		System.out.println("htmlsource--44--"+htmlsource4);  
        		dataHtml = htmlsource4;
             }
         }
         
         System.out.println("------��ȡ��ˮ------"); 
         
         
         String ClientNo = Jsoup.parse(dataHtml).select("#ClientNo").val();
         String __VIEWSTATE = Jsoup.parse(dataHtml).select("#__VIEWSTATE").val();
         String __EVENTVALIDATION = Jsoup.parse(dataHtml).select("#__EVENTVALIDATION").val();
         
 		 String html = getData(ClientNo,__VIEWSTATE,__EVENTVALIDATION);
 		 
 		 
 		 System.out.println("��ˮ======"+html);
         
	}
	
	public static String getData(String ClientNo,String __VIEWSTATE,String __EVENTVALIDATION) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx"; 
		System.out.println(url); 
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);  
		requestSettings.setAdditionalHeader("Host", "pbsz.ebank.cmbchina.com");
		requestSettings.setAdditionalHeader("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx");
		requestSettings.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		requestSettings.setAdditionalHeader("DNT", "1");
		
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","WEBTRENDS_ID", "123.126.87.162-2358226944.30605561"));
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
		requestSettings.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
		requestSettings.getRequestParameters().add(new NameValuePair("BeginDate", "20140808"));
		requestSettings.getRequestParameters().add(new NameValuePair("EndDate", "20170802"));
		requestSettings.getRequestParameters().add(new NameValuePair("BtnOK", "�� ѯ"));
		requestSettings.getRequestParameters().add(new NameValuePair("ClientNo", ClientNo));
		
		Page page = webClient.getPage(requestSettings);  
		String html = page.getWebResponse().getContentAsString();  
		System.out.println("html ===="+html);	
		return html;
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

}
