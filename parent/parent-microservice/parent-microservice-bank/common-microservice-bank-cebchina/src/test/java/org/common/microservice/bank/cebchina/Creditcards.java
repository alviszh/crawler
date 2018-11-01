package org.common.microservice.bank.cebchina;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.jna.webdriver.WebDriverUnit;


public class Creditcards {
	static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";


	public static void main(String[] args) {
		try {

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			System.setProperty("webdriver.ie.driver", driverPath);
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			driver = new InternetExplorerDriver(ieCapabilities);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

			String url = "https://xyk.cebbank.com/mall/login?target=/mycard/home/home.htm";

			driver.get(url);
			Thread.sleep(2000L);
			driver.findElement(By.id("userName")).sendKeys("6226580034768784");//输入帐号
			String path = WebDriverUnit.saveImg(driver, By.className("grid-mock"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
			Thread.sleep(2000L);
			driver.findElement(By.id("verification-code")).sendKeys(code);//图片验证码
			Thread.sleep(5000L);
			driver.findElement(By.className("btn btn-small js-login-sendCode")).click();//发送短信
			
			String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			Thread.sleep(5000L);
			driver.findElement(By.id("yzmcode")).sendKeys(inputValue);//输入短信验证码
			
			
			driver.findElement(By.className("btn login-style-bt ")).click();//登录
			Thread.sleep(2000L);
			
			
			
			
			
			//			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			//			String url = "https://xyk.cebbank.com/mall/login?target=/mycard/home/home.htm";
			//			HtmlPage page = (HtmlPage)getHtml(url, webClient);
			//			String html = page.getWebResponse().getContentAsString();
			//			System.out.println(html);
			//
			//			HtmlTextInput cardno = (HtmlTextInput)page.getFirstByXPath("//input[@id='userName']");//身份证
			//			//yzm_get
			//			HtmlTextInput tu = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzmcode']");//图片验证码
			//
			//			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@class='grid-mock']");//图片
			//			String imageName = "111.jpg"; 
			//			File file = new File("F:\\img\\" + imageName);
			//			image.saveAs(file);

			//			HtmlTextInput login = (HtmlTextInput)page.getFirstByXPath("//input[@id='verification-code']");//短信框
			//
			//			cardno.setText("6226580034768784");
			//			String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			//			tu.setText(inputValue);


			//			String url2 = "https://xyk.cebbank.com/mall/api/usercommon/dynamic?"
			//					+ "name=6226580034768784"
			//					+ "&code="+inputValue;
			//
			//			Page page3 =gethtmlPost(webClient, null, url2);
			//			String html2 = page3.getWebResponse().getContentAsString();
			//			//{"status":200,"data":{"success":true,"result":"60"}} 成功
			//			if(html2.indexOf("status")!=-1){
			//				System.out.println("发送短信成功");
			//
			//				String duanxin = JOptionPane.showInputDialog("请输入短信验证码……");
			//				login.setText(duanxin);
			//
			//				HtmlSubmitInput submit = (HtmlSubmitInput)page.getFirstByXPath("//input[@class='btn login-style-bt ']");
			//				Page page2 = submit.click();
			//				Thread.sleep(5000L);
			//				String html3 = page2.getWebResponse().getContentAsString();
			//				System.out.println("登录成功"+html3);
			//
			//			}else{
			//				System.out.println("发送失败，发生错误："+html2);
			//			}









		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

}
