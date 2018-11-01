package app.test;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

/**
 * @description:  用webdriver的方式来尝试鄂尔多斯的登录
 * @author: sln 
 * @date: 2018年1月18日 下午4:06:27 
 */
public class DriverLoginTest2 {
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static String driverPath="C:\\ChromeServer\\chromedriver.exe";
	private static Robot robot;
	public static void main(String[] args) {
			System.setProperty("webdriver.chrome.driver", driverPath);
			WebDriver driver = new ChromeDriver();
			ChromeOptions chromeOptions = new ChromeOptions();
			//通过配置参数禁止data;的出现,否则输入正确信息也无法进入首页面 
//			chromeOptions.addArguments("--user-data-dir=C:/Users/Administrator/AppData/Local/Google/Chrome/User Data/Default");
			driver = new ChromeDriver(chromeOptions);
			//设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			try{
//				String loginUrl="http://www.ordosgjj.com:8088/(S(btdn0l2xjuvime55yxdskhin))/login.aspx";
				String loginUrl="http://www.ordosgjj.com:8088/login.aspx";
				//由于进入登录页面的时候，验证码大部分情况是个空图片，故获取两次登录页面，保证验证码显示出来
				driver.get(loginUrl);
				Thread.sleep(2000);
				driver.get(loginUrl);
				Thread.sleep(2000);
				
				loginUrl = driver.getCurrentUrl();
				System.out.println("刷新页面之后的url是："+loginUrl);
				
				String apartUrl=loginUrl.substring(loginUrl.indexOf("("),loginUrl.lastIndexOf(")")+1);
				System.out.println("截取下来的部分url是："+apartUrl);
				// 输入账号
				driver.findElement(By.id("txtUsername")).sendKeys("mcx0242");
				Thread.sleep(500);
				//输入密码
				driver.findElement(By.id("txtUserpass")).sendKeys("mcx0242");
				Thread.sleep(500);
				// 验证码
				String path = WebDriverUnit.saveImg(driver, By.id("ImageCheck"));
				System.out.println("path---------------" + path);
				String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,path); 
				System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
				Gson gson = new GsonBuilder().create();
				String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
				System.out.println("code ====>>" + code);
		
				driver.findElement(By.name("yzm")).sendKeys(code);
				Thread.sleep(1000);
				// 模拟点击登录
				driver.findElement(By.id("Button1")).click();
				Thread.sleep(2000);
				robot = new Robot();// 创建Robot对象

				robot.keyPress(KeyEvent.VK_ENTER);  //该网站登陆成功后会弹出提示，那个提示alert不会自动消失，只有点击了上边的确定按钮才会将提示信息展现在html上
				Thread.sleep(1000);
				
				String currentUrl = driver.getCurrentUrl();
				
				//跳转到了登录后的页面，接下来根据此页面的显示信息判断是否登录成功
				String loginClickHtml = driver.getPageSource(); //获取点击登录按钮后的html页面
				if(currentUrl.contains("welcome.aspx")){
					if(loginClickHtml.contains("欢迎登录系统")){
				    	System.out.println("欢迎登录系统");
				 		Set<Cookie> cookies =  driver.manage().getCookies();
				 		WebClient webClient = WebCrawler.getInstance().getWebClient();
				 		//根据ip或者域名存储cookie
				 		for(Cookie cookie:cookies){
				 			System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				 			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("www.ordosgjj.com",cookie.getName(),cookie.getValue()));
				 		}
				 		
				 		//请求用户信息：
						String url="http://www.ordosgjj.com:8088/"+apartUrl+"/PersonalAccumulationMoney/Personalinformation.aspx";
						WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
						webRequest.setAdditionalHeader("Host", "www.ordosgjj.com:8088");
						webRequest.setAdditionalHeader("Referer", "http://www.ordosgjj.com:8088/"+apartUrl+"/welcome.aspx");
						webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
						Page page= webClient.getPage(webRequest);
						if(page!=null){
							String html=page.getWebResponse().getContentAsString();
							System.out.println("获取的用户信息的页面是："+html);
							
							Document doc = Jsoup.parse(html);
							String text = doc.getElementById("GridView1").text();
							System.out.println("获取的table中的信息是："+text);
							
							text = doc.getElementById("GridView1").getElementsByTag("tr").text();
							System.out.println("获取的tr中的信息是："+text);
							
//							String attr = doc.getElementById("GridView1").getElementsByTag("tr").get(1).getElementsByTag("td").get(6).getElementsByTag("a").get(0).attr("href");
//							System.out.println("获取的链接参数是："+attr);
							
						}else{
							System.out.println("不能够获取的用户信息的页面");
						}
				 		
					}else{
						if(loginClickHtml.contains("alert")){
							if(loginClickHtml.contains("验证码输入错误")){
								System.out.println("验证码输入错误");
							}else if(loginClickHtml.contains("密码错误")){
								System.out.println("密码错误");
							}else if(loginClickHtml.contains("用户名不存在，请重新输入")){
								System.out.println("用户名不存在，请重新输入");
							}else if(loginClickHtml.contains("数据库连接失败")){
								System.out.println("数据库连接失败");
							}else if(loginClickHtml.contains("登陆超时，请重新登陆！")){
								System.out.println("登陆超时，请重新登陆！");
							}else{
								System.out.println("出现了其他登录错误，模拟点击后的页面是："+loginClickHtml);
							}
						}
					}
				}else{
					System.out.println("没有进入首页面，当前链接是:"+currentUrl);
					System.out.println("没有进入首页面，当前页面内容是:"+currentUrl);
				}
			} catch (Exception e) {
				System.out.println("打印出来的异常信息是："+e.toString());
			}
	}
}
