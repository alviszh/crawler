package Test;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

public class Testlogin {

    static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
	
//	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	private static Robot robot;
	public static void main(String[] args) throws Exception {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

		System.setProperty("webdriver.ie.driver", driverPath);

		WebDriver driver = new InternetExplorerDriver();
		 
		driver = new InternetExplorerDriver(ieCapabilities);

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://i.bank.ecitic.com/perbank6/signIn.do";
		driver.get(baseUrl);
		driver.manage().window().maximize();
		String pageSource = driver.getPageSource();
		System.out.println(pageSource);
		int indexOf = pageSource.indexOf("createSessionKey");
		String substring = pageSource.substring(indexOf);
		String substring2 = substring.substring(28);
		String[] split = substring2.split("checkVeriyUrl=");
		String string2 = split[0].toString();
		String substring3 = string2.substring(0,40);
		WebElement username = driver.findElement(By.name("logonNoCert"));
		
		
		Actions action = new Actions(driver);
		action.keyDown(Keys.ALT);
		
		Thread.sleep(1000);
		
		username.click();
		Thread.sleep(1000);
		username.clear();
		Thread.sleep(1000);
		username.sendKeys("6217680704617328");
		Thread.sleep(1000);
//		InputTab();
		VK.Tab();
		
		
//		WebElement password1 = driver.findElement(By.id("logonPwdId"));
//		password1.sendKeys("yl405232");
		String password = "yl4052320";
//		VirtualKeyBoard.KeyPressEx(password,500);
		VK.KeyPress(password);

		driver.findElement(By.id("logonButton")).click();
		Thread.sleep(1000);
		String currentUrl = driver.getCurrentUrl();
		System.out.println(currentUrl);
		if (baseUrl.contains(currentUrl)) {
			System.out.println("登录失败！");
		} else {
			
			System.out.println("登录成功！");
			System.out.println(driver.getPageSource());
			
			
			
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			for(org.openqa.selenium.Cookie cookie : cookies){
				Cookie cookieWebClient = new Cookie("i.bank.ecitic.com", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);
			}
			
			String urll="https://i.bank.ecitic.com/perbank6/firstPageSearch.do?EMP_SID="+substring3+"&searchtxt=个人信息";
			Page page2 = webClient.getPage(urll);
			System.out.println(page2.getWebResponse().getContentAsString());
			String userURL3="https://i.bank.ecitic.com/perbank6/pb8110_qryCstInfo.do?EMP_SID="+substring3;
			Page page = webClient.getPage(userURL3);
			System.out.println("-----------"+page.getWebResponse().getContentAsString()+"--=================");
//			String url4="https://edata.bank.ecitic.com/collection/img/dot-writePageClickPoint.gif?lt=255&tp=87&ww=1349&wh=1083&bt=MSIE&ep=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ln=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ucHash=87FF67A1F52B16D8BECDC76B8582045F&url=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.domain&sid=&id=dcf23af7-283a-4d10-83cd-3593c82da303&ak=2a9d95aa526df6932bf5edf69d8dbd2c&si=2a9d95aa526df6932bf5edf69d8dbd2c80511807-fd2f-436a-aecb-ed19f0eff384&pageId=dcf23af7-283a-4d10-83cd-3593c82da303&ck=5e2dbb98-b941-476a-b378-931b8f0beb4b&ver=1.0&time="+System.currentTimeMillis();
//			Page page4 = webClient.getPage(url4);
//			System.out.println("---------------4-----------"+page4.getWebResponse().getContentAsString());
//			Thread.sleep(1000);
//			String url5="https://edata.bank.ecitic.com/collection/img/dot-rawMouseClickLog.gif?pth=%2F%2F*%5B%40id%3D%22m1_1%22%5D%2FA&lt=255&tp=87&ep=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ln=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ucHash=87FF67A1F52B16D8BECDC76B8582045F&url=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.domain&sid=&id=dcf23af7-283a-4d10-83cd-3593c82da303&ak=2a9d95aa526df6932bf5edf69d8dbd2c&si=2a9d95aa526df6932bf5edf69d8dbd2c80511807-fd2f-436a-aecb-ed19f0eff384&pageId=dcf23af7-283a-4d10-83cd-3593c82da303&ck=5e2dbb98-b941-476a-b378-931b8f0beb4b&ver=1.0&time="+System.currentTimeMillis();
//			Page page5 = webClient.getPage(url5);
//			System.out.println("--------------5-------------"+page5.getWebResponse().getContentAsString());
//			Thread.sleep(1000);
//			String url6="https://edata.bank.ecitic.com/collection/img/dot-writePageClickPoint.gif?lt=157&tp=247&ww=1349&wh=1083&bt=MSIE&ep=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ln=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ucHash=87FF67A1F52B16D8BECDC76B8582045F&url=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.domain&sid=&id=dcf23af7-283a-4d10-83cd-3593c82da303&ak=2a9d95aa526df6932bf5edf69d8dbd2c&si=2a9d95aa526df6932bf5edf69d8dbd2c80511807-fd2f-436a-aecb-ed19f0eff384&pageId=dcf23af7-283a-4d10-83cd-3593c82da303&ck=5e2dbb98-b941-476a-b378-931b8f0beb4b&ver=1.0&time="+System.currentTimeMillis();
//			Page page6 = webClient.getPage(url6);
//			System.out.println("--------------6-------------"+page6.getWebResponse().getContentAsString());
//			Thread.sleep(1000);
//			String url7="https://edata.bank.ecitic.com/collection/img/dot-rawMouseClickLog.gif?pth=%2F%2F*%5B%40id%3D%22m3_54%22%5D%2FA&lt=157&tp=247&ep=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ln=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.do&ucHash=87FF67A1F52B16D8BECDC76B8582045F&url=https%3A%2F%2Fi.bank.ecitic.com%2Fperbank6%2Fgotomain.domain&sid=&id=dcf23af7-283a-4d10-83cd-3593c82da303&ak=2a9d95aa526df6932bf5edf69d8dbd2c&si=2a9d95aa526df6932bf5edf69d8dbd2c80511807-fd2f-436a-aecb-ed19f0eff384&pageId=dcf23af7-283a-4d10-83cd-3593c82da303&ck=5e2dbb98-b941-476a-b378-931b8f0beb4b&ver=1.0&time="+System.currentTimeMillis();
//			Page page7 = webClient.getPage(url7);
//			System.out.println("--------------7-------------"+page7.getWebResponse().getContentAsString());
//			Thread.sleep(1000);
			
			Thread.sleep(1000);
			String url10="https://i.bank.ecitic.com/perbank6/trans_3063s.do?EMP_SID="+substring3+"&accountNo=6217680704617328&selectSubAccount=null";
			HtmlPage page10 = webClient.getPage(url10);
			//System.out.println("--------------10--------------"+page10.getWebResponse().getContentAsString());
			Thread.sleep(1000);
			String url11="https://i.bank.ecitic.com/perbank6/pb1310_account_detail.do?EMP_SID="+substring3+"&payAcctxt=6217680704617328&isubAccInfo.judFrozenSFlag=1&isubAccInfo.extendFlag=null&isubAccInfo.frozenSFlag=1&isubAccInfo.lossFlag=null&isubAccInfo.saveType=01&isubAccInfo.hostAccType=null&isubAccInfo.currencyType=001&isubAccInfo.savePeriod=null&isubAccInfo.recordState=1&beginDate=20001029&endDate=20171029&beginAmtText=&endAmtText=&accountNo=6217680704617328&stdessbgdt=20001106&stdesseddt=20171106&stdesssbno=&CashFlag=&recordStart=1&recordNum=10&std400chnn=&std400dcfg=&std400pgqf=N&std400pgtk=&std400pgts=&stdudfcyno=001&opFlag=0&stkessmnam=&largeAmount=&queryType=nearTenTab&targetPage=1&beginAmt=&endAmt=&recordSize=&queryDays=1&startPageFlag=&pageType=&beforePageMap=";
			HtmlPage page11 = webClient.getPage(url11);
			System.out.println("-------------------11-----------------"+page11.getWebResponse().getContentAsString());
			
			String url9="https://i.bank.ecitic.com/perbank6/pb1310_account_detail_query.do?EMP_SID="+substring3;
			Page page9 = webClient.getPage(url9);
			System.out.println("--------------9-------------"+page9.getWebResponse().getContentAsString());
//			String url12="https://i.bank.ecitic.com/perbank6/pb1110_query_detail.do?EMP_SID="+substring3+"&accountNo=6217680704617328&index=0ff0";
//			Page page12 = webClient.getPage(url12);
//			System.out.println("=========---------"+page12.getWebResponse().getContentAsString());
		}
	}
	public static void InputTab() throws IllegalAccessException, NativeException, Exception { 
		Thread.sleep(1000L); 
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) { 
		VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab")); 
		} 
		} 
	
	public static void InputEnter() throws IllegalAccessException, NativeException, Exception { 
		Thread.sleep(1000L); 
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) { 
		VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter")); 
		} 
		} 

}
