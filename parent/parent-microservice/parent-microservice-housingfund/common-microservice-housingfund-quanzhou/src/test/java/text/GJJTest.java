package text;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class GJJTest {

	//static String url = "http://www.qzgjj.com/PAFundQuery/Index";

	private static WebClient webClient = null;

	public static void main(String[] args) throws Exception {
		ChromeDriver driver = intiChrome();
//		driver.manage().window().maximize();
		String url = "http://www.qzgjj.com/PAFundQuery/Index";
//		driver.manage().window().maximize();

		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.get(url);
		
		webClient = WebCrawler.getInstance().getNewWebClient();

//		HtmlPage htmlPage = (HtmlPage) getHtml(url, webClient);
//		
//		url = "http://www.qzgjj.com/Index/GetCheckCode?type=qt_lg&r=0.415358323056934";
////		HtmlPage htmlPage2 = (HtmlPage) getHtml(url, webClient);
////
////		System.out.println(htmlPage2.asXml());
////
////		url = "http://www.qzgjj.com/Index/UserLogin?rand=0.6776736386389215";
//		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.POST);
//		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='img_CheckCode_Layout']");
//		ImageReader imageReader = valiCodeImg.getImageReader();
//		BufferedImage bufferedImage = imageReader.read(0);
//
//		JFrame f2 = new JFrame();
//		JLabel l = new JLabel();
//		l.setIcon(new ImageIcon(bufferedImage));
//		f2.getContentPane().add(l);
//		f2.setSize(100, 100);
//		f2.setTitle("验证码");
//		f2.setVisible(true);
//
//		String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

		driver.findElement(By.id("t_user")).sendKeys("350500199211303027");
		Thread.sleep(1000);
		driver.findElement(By.id("t_pass")).sendKeys("holiday11");
		Thread.sleep(1000);
//		WebElement image = driver.findElement(By.id("img_CheckCode_Layout"));
//		String imgurl = image.getAttribute("src");
//		System.out.println(imgurl);
		 Scanner input = new Scanner(System.in);
		 System.out.print("请输入：");
		 String valicodeStr = input.next();
		driver.findElement(By.id("Tb_CheckCode_Layout")).sendKeys(valicodeStr.trim());
		driver.findElement(By.id("btn_Login")).click();
		Thread.sleep(1000);
		String html = driver.getPageSource();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("div.verify-sub-block");
		String style = ele.attr("style").trim();
//		System.out.println(style);
		String s = style.substring(style.lastIndexOf(":")).trim();
		String ss = s.substring(s.indexOf("-")+1,s.indexOf("px")).trim();
		System.out.println(ss);
		
		float a = Float.parseFloat(ss);
		Actions action = new Actions(driver);
		// 获取滑动滑块的标签元素
		WebElement source = driver.findElement(By.xpath("//*[@class='verify-sub-block']"));
		// 确保每次拖动的像素不同，故而使用随机数
		action.clickAndHold(source).moveByOffset((int)a, 0);
		Thread.sleep(2000);
		// 拖动完释放鼠标
		action.moveToElement(source).release();
		Thread.sleep(2000);
		// 组织完这些一系列的步骤，然后开始真实执行操作
		Action actions = action.build();
		actions.perform();
		
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("l_loginName", "350500199211303027"));
//		paramsList.add(new NameValuePair("l_password", "holiday11"));
//		paramsList.add(new NameValuePair("lg_CheckCode", valicodeStr.trim()));
//		webRequest1.setRequestParameters(paramsList);
		// webRequest1.setRequestBody("{'l_loginName':'11fffffff','l_password':'lklk','lg_CheckCode':'ffff'}");
//		Page page = webClient.getPage(webRequest1);
//		System.out.println("=====登陆结果===========");
//		System.out.println(page.getWebResponse().getContentAsString());
//
//		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
//		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//
//		for (Cookie cookie : cookies) {
//			System.out.println(cookie.toString());
//			Cookie cookie2 = new Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
//			System.out.println(cookie2.toString());
//			webClient2.getCookieManager().addCookie(cookie2);
//		}
//
//		String url2 = "http://www.qzgjj.com/PAFundQuery/Index";
//		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);
//		HtmlPage loggedPage2 = webClient2.getPage(webRequest2);
//		System.out.println(loggedPage2.asXml());
//		HtmlElement button = (HtmlElement) loggedPage2.getFirstByXPath("//*[@id='form1']/center/input[1]");
//		// HtmlElement button =
//		// (HtmlElement)htmlPage1.getFirstByXPath("//input[@value='个人账户查询']");
//		HtmlTextInput code = (HtmlTextInput) loggedPage2.getFirstByXPath("//*[@id='Tb_MsgVCode']");
//		code.setText("111111");
//		HtmlPage htmlPage3 = button.click();
//		System.out.println(htmlPage3.asXml());
//
//		for (Cookie cookie : cookies) {
//			System.out.println(cookie.toString());
//		}
//
//		Set<Cookie> cookiesw = webClient2.getCookieManager().getCookies();
//		for (Cookie cookie : cookiesw) {
//			System.out.println(cookie.toString());
//		}

	}
	public static  ChromeDriver intiChrome() throws Exception {
//		String driverPath = "/opt/selenium/chromedriver-2.31";
		System.setProperty("webdriver.chrome.driver", "D:/zhaohui/chromedriver.exe");
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	public static String convert(String utfString) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = utfString.indexOf("\\u", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
			}
		}

		return sb.toString();
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

}
