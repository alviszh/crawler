package test.webdriver.boc;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

public class STest {

	public static void main(String[] args) throws Exception {
		
		
		JsoupTest();
		
		//ChromeTest();
		
		//HtmlunitTest();
	}
	
	public static void JsoupTest() throws Exception{
	  
		//String targeturl = "https://login.10086.cn/login.html";
		String targeturl = "https://uac.10010.com/portal/mallLogin.jsp";
		URL url = new URL(targeturl);   
		Document doc = Jsoup.parse(url, 3*1000);    
		
		String html = doc.html();
		System.out.println(html);

		String md5 = StringToMd5(html);
		//1 .网页源码的md5值
		System.out.println("htmlmd5---------"+md5);
		 
		
		Elements eles = doc.select("script[src]");
		
		System.out.println("eles.size()--------------"+eles.size());
		
		for(Element jsElement:eles){
			String jsPath = jsElement.attr("abs:src"); 
			System.out.println("jsPath-------------"+jsPath);
			URL url2 = new URL(jsPath);     
		    InputStream ins = url2.openStream();    
		    String jsmd5 = DigestUtils.md5Hex(ins); 
		    System.out.println("jsmd5-------"+jsmd5);
		}
		
	}
	
	
	public static void HtmlunitTest() throws Exception{
		WebClient  webClient = new WebClient(BrowserVersion.CHROME);
		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(true);  
 
		HtmlPage page = (HtmlPage)webClient.getPage("http://login.189.cn/web/login");
		Thread.sleep(5000);
		
		String html = page.asXml();
		System.out.println(html);

		String md5 = StringToMd5(html);
		//1 .网页源码的md5值
		System.out.println("htmlmd5---------"+md5);
		
		DomNodeList<DomNode> eles = page.querySelectorAll("script[src]");
		
		System.out.println("eles.size()---------------"+eles.size());
		
		for(DomNode ele:eles){
			HtmlScript hs = (HtmlScript)ele;
			String src = hs.getSrcAttribute() ;
			
			System.out.println(hs.getBaseURI()+"--------------"+src);
		}
		
		
	}
	
	public static void ChromeTest() throws Exception{
		WebDriver driver = intiChrome();

		EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
		// 注册事件
		eventDriver.register(new MyWebDriverListener());

		//driver.get("https://login.10086.cn/login.html"); 
		driver.get("http://login.189.cn/web/login"); 
		
		String html = driver.getPageSource();
		System.out.println(html);

		String md5 = StringToMd5(html);
		
		//1 .网页源码的md5值
		System.out.println("htmlmd5---------"+md5);
		
		//2 。网页的js文件数
		List<WebElement> eles = driver.findElements(By.cssSelector("script[src]")); 
		System.out.println("eles.getSize()------------"+eles.size());
		
		
		for(WebElement jsElement:eles){
			String jsPath = jsElement.getAttribute("src"); 
			System.out.println("jsPath-------------"+jsPath);
			URL url = new URL(jsPath);     
		    InputStream ins = url.openStream();    
		    String jsmd5 = DigestUtils.md5Hex(ins); 
		    System.out.println("jsmd5-------"+jsmd5);
		}
		
		
	}

	public static ChromeDriver intiChrome() throws Exception {
		String driverPath = "C:\\chromedriver.exe";
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	public static String StringToMd5(String psw) {

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(psw.getBytes("UTF-8"));
			byte[] encryption = md5.digest();

			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}

			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}

	}

}
