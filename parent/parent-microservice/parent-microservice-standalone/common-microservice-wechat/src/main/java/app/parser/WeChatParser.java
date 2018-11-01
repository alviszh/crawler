package app.parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.wechat.json.WeChatRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.zxing.Result;
import com.microservice.dao.entity.crawler.wechat.TaskWeChat;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.QrcodeService;
import sun.misc.BASE64Encoder;
@Component
public class WeChatParser {
	WebDriver driver;
	
	@Value("${spring.application.name}") 
	String appName;
	
	@Value("${webdriver.chrome.driver.path}")
	String path;
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private QrcodeService qrcodeService;

	public WebParam getCode(WeChatRequestParameters weChatRequestParameters, TaskWeChat taskWeChat)throws Exception {
		System.setProperty("webdriver.chrome.driver", path);
		ChromeOptions chromeOptions = new ChromeOptions();
		driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		
		String baseUrl = "https://wx.qq.com/";
		driver.get(baseUrl);
//		driver.manage().window().maximize();
		String json = driver.getPageSource();
		System.out.println(json);
		
		String[] split = json.split("/head");
//		System.out.println(split[0]);
		String[] split2 = split[0].split("head");
//		System.out.println(split2[1]);
		String substring = split2[1].substring(23, 156);
//		System.out.println(substring);
		String replace = substring.replace("amp;", "");
		System.out.println(replace);
		String replaceAll = replace.replaceAll("tip=1", "tip=1");
		System.out.println(replaceAll);
		
		
		 WebDriverWait wait=new WebDriverWait(driver, 60);
		 WebElement qrCodeImg= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return driver.findElement(By.className("qrcode")); 
			            } 
			        });
		 WebParam webParam = new WebParam();
		 if(qrCodeImg!=null)
		 {
			 webParam = encodeImgageToBase64ByWebElement(qrCodeImg, webParam);
			 webParam.setUrl(replaceAll);
		 }
		 return webParam;
	}
	
	
	
	private WebParam encodeImgageToBase64ByWebElement(WebElement element, WebParam webParam) throws Exception{
    	File qrcode = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    	byte[] data = null;
    	try {
            Point p = element.getLocation();
            int width = element.getSize().getWidth();
            int height = element.getSize().getHeight();
            BufferedImage img = ImageIO.read(qrcode);
            BufferedImage dest = img.getSubimage(p.getX(), p.getY(),
                    width, height);
            ImageIO.write(dest, "png", qrcode);
            Thread.sleep(1000);
            Result result = qrcodeService.getQRresult(dest);
            webParam.setQrUrl(result.getText());
            FileInputStream inputStream = new FileInputStream(qrcode);
			// 读取图片字节数组
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		String base64 = encoder.encode(data);
		tracer.output("二维码登录，二维码图片base64Code", base64);
		webParam.setBaseCode(base64);
		return webParam;
	}



	public WebParam login(WeChatRequestParameters weChatRequestParameters, TaskWeChat taskWeChat) throws Exception {
		 WebDriverWait wait=new WebDriverWait(driver, 180);
		 WebElement search_bar= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return driver.findElement(By.id("search_bar")); 
			            } 
			        });
		WebParam webParam = new WebParam();
		System.out.println(driver.getPageSource());
		if(driver.getPageSource().contains("account.NickName"))
		{
//			System.out.println("333333333333333"+driver.getPageSource());
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			for (org.openqa.selenium.Cookie cookie : cookies) {
				Cookie cookieWebClient = new Cookie("", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);
			}
			System.out.println("333333333333333"+taskWeChat.getTesthtml());
			WebRequest webRequest = new WebRequest(new URL(taskWeChat.getTesthtml()), HttpMethod.POST);
			Page page2 = webClient.getPage(webRequest);
			System.out.println(page2.getWebResponse().getContentAsString());
			String substring2 = page2.getWebResponse().getContentAsString().substring(38, page2.getWebResponse().getContentAsString().length()-2);
			
			substring2+="&fun=new&version=v2";
			System.out.println("substring2"+substring2);                                   
			webRequest = new WebRequest(new URL(substring2), HttpMethod.POST);
			Page page3 = webClient.getPage(webRequest);
			System.out.println("wechat1234"+page3.getWebResponse().getContentAsString());
			
			int indexOf = page3.getWebResponse().getContentAsString().indexOf("<skey>");
			int indexOf2 = page3.getWebResponse().getContentAsString().indexOf("</skey>");
			String substring3 = page3.getWebResponse().getContentAsString().substring(indexOf, indexOf2);
			
			int indexOf3 = page3.getWebResponse().getContentAsString().indexOf("<pass_ticket>");
			int indexOf4 = page3.getWebResponse().getContentAsString().indexOf("</pass_ticket>");
			String substring4 = page3.getWebResponse().getContentAsString().substring(indexOf3, indexOf4);
			
			
			String url1="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?pass_ticket="+substring4+"&r=1539670736426&seq=0&skey="+substring3;
			webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page page4 = webClient.getPage(webRequest);
			System.out.println("++++++++++++++页面+++++++++++++++++"+page4.getWebResponse().getContentAsString());
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setUrl(url1);
		}
		webParam.setHtml(driver.getPageSource());
		return webParam;
	}




}
