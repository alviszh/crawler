package app.test;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;


/**
 * @description：测试哪些参数可以去掉(通过测试了发现，在获取个人明细之前的那个请求的参数中，如下没有注释的参数是有用的)
 * @author: sln 
 * @date: 
 */
public class MainTestGetUserInfo {
	public static void main(String[] args) throws Exception, IOException{ 
		String driverPath="C:\\ChromeServer\\chromedriver.exe";
		String LEN_MIN = "0";
		String TIME_ADD = "0";
		String STR_DEBUG = "a";
		
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
			
			String loginUrl="http://121.17.186.64:6675/wsyyt/";
			String firstPageUrl="http://121.17.186.64:6675/wsyyt/per.login";
			String loginIpOrHost="121.17.186.64";
			//由于进入登录页面的时候，验证码大部分情况是个空图片，故获取两次登录页面，保证验证码显示出来
			driver.get(loginUrl);
			Thread.sleep(1000);
			driver.get(loginUrl);
	//		driver.manage().window().maximize();  //页面最大化    //最大化后，在本地运行报错:disconnected: unable to connect to renderer
			Thread.sleep(2000);
			
			//选择个人用户登录方式
			driver.findElement(By.cssSelector("a[href='#tabs-1']")).click();;
			Thread.sleep(1000);
			// 输入身份证号
			driver.findElement(By.name("certinum")).sendKeys("131102199111230220");
			Thread.sleep(1000);
			//输入密码
			driver.findElement(By.name("perpwd")).sendKeys("911018");
			Thread.sleep(1000);
			// 验证码
			String path = WebDriverUnit.saveImg(driver, By.cssSelector("img[src='vericode.jsp']"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,path); 
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
	
			driver.findElement(By.name("vericode")).sendKeys(code);
			Thread.sleep(1000);
			// 模拟点击登录
			driver.findElement(By.cssSelector("button[type='submit']")).click();
			WebDriverWait wait = new WebDriverWait(driver, 15);   
			Boolean isLogon=wait.until(ExpectedConditions.urlToBe(firstPageUrl));
			if(isLogon==true){
				//跳转到了登录后的页面，接下来根据此页面的显示信息判断是否登录成功
				String loginClickHtml = driver.getPageSource(); //获取点击登录按钮后的html页面
				if(loginClickHtml.contains("欢迎您")){
			    	System.out.println("登录成功，欢迎来到首页面");
			 		Set<Cookie> cookies =  driver.manage().getCookies();
			 		WebClient webClient = WebCrawler.getInstance().getWebClient();
			 		//根据ip或者域名存储cookie
			 		for(Cookie cookie:cookies){
			 			System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
			 			webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(""+loginIpOrHost+"",cookie.getName(),cookie.getValue()));
			 		}
			 		String cookieString = CommonUnit.transcookieToJson(webClient);  //存储cookie
			 		System.out.println("存储起来的cookie是："+cookieString);
			 		
			 		/////////////////测试获取用户信息//////////////////////////
			 		webClient.getOptions().setJavaScriptEnabled(false);
					String url = "http://121.17.186.64:6675/wsyyt/init.summer?_PROCID=60020007"; 
			 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
			 		HtmlPage hPage = webClient.getPage(webRequest);  
			 		if(null!=hPage){
			 			String html = hPage.getWebResponse().getContentAsString(); 
			 			webClient=hPage.getWebClient();
			 			System.out.println("获取爬取用户信息所需要的参数源码页："+html);
			 			if(html.contains("个人姓名")){   //作为能够响应数据的一个判断
			 				Document doc=Jsoup.parse(html);
			 				//通过调研，（个人信息中）只有个人账号在后来的部分请求中起到作用
			 				//获取姓名
			 				String accName= doc.getElementById("AccName").val();
			 				//获取个人账号
			 				String accNum= doc.getElementById("AccNum").val();
			 				//获取身份证号
			 				String certiNum= doc.getElementById("CertiNum").val();
			 				//获取单位名称
			 				String unitAccName= doc.getElementById("UnitAccName").val();
			 				//获取单位账号
			 				String unitAccNum= doc.getElementById("UnitAccNum").val();
			 				url="http://121.17.186.64:6675/wsyyt/command.summer?uuid=1512540252233";
			 				webRequest= new WebRequest(new URL(url), HttpMethod.POST);  
			 				//涉及到汉字的参数需要加密，不然返回的信息中涉及到了汉字，返回的是?代替的
			 				String encodeName=URLEncoder.encode(accName.trim(), "utf-8");
			 				String encodeUnitAccName=URLEncoder.encode(unitAccName.trim(), "utf-8");
//			 				String requestBody=""
//					 				+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
//					 				+ "&_ACCNUM="+accNum+""
//					 				+ "&_PAGEID=step1"
//					 				+ "&_PROCID=60020007"
//					 				+ "&AccNum="+accNum+""
//					 				+ "&accnum="+accNum+""
//					 				+ "&year="+thisYear+""
//					 				+ "&ajaxid=query1"
//					 				+ "&accnum="+accNum+"";
			 				String requestBody=""
			 						+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
			 						+ "&_ACCNUM="+accNum+""
			 						+ "&_UNITACCNUM="+unitAccNum+""
			 						+ "&_PAGEID=step1"
			 						+ "&_IS=-278100"
			 						+ "&_UNITACCNAME="+encodeUnitAccName+""
			 						+ "&_ACCNAME="+encodeName+""
			 						+ "&isSamePer=false"
			 						+ "&_SENDOPERID="+certiNum+""
			 						+ "&_PROCID=60020007"
			 						+ "&_DEPUTYIDCARDNUM="+certiNum+""
									+ "&_BRANCHKIND=0"
									+ "&_TYPE=init"
			 						+ "&_ISCROP=0"
			 						+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E8%B4%A6%E6%9F%A5%E8%AF%A2"
			 						+ "&_WITHKEY=0"
			 						+ "&AccName="+encodeName+""
			 						+ "&AccNum="+accNum+""
			 						+ "&CertiNum="+certiNum+""
			 						+ "&OpenDate="
			 						+ "&Balance="
			 						+ "&UnitAccNum="+unitAccNum+""
			 						+ "&UnitAccName="+encodeUnitAccName+""
			 						+ "&ajaxid=query1"
			 						+ "&accnum="+accNum+"";
			 				webRequest.setRequestBody(requestBody);
			 				//如下信息必须加上，不然返回的用汉字展示的信息会是乱码
			 				webRequest.setAdditionalHeader("Host", "121.17.186.64:6675");
			 				webRequest.setAdditionalHeader("Origin", "http://121.17.186.64:6675");
			 				webRequest.setAdditionalHeader("Referer", "http://121.17.186.64:6675/wsyyt/init.summer?_PROCID=60020007");
			 				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			 				
			 				Page page = webClient.getPage(webRequest);
			 				if(page!=null){
			 					html=page.getWebResponse().getContentAsString();
			 	 				System.out.println("获取完整的用户信息html："+html);
			 				}
			 			}
			 		}
			 		////////////////////////////////////////////////////////
			 		
			 		
				}else{
					Document doc = Jsoup.parse(loginClickHtml);
					String errorMsg= doc.getElementsByClass("WTLoginError").get(0).text();  //获取页面中可能出现的错误信息
					//页面信息提示中可能存在返回这个字段，截取掉
//						String str="操作失败:进行身份校验时出错:密码错误,请检查! 返回";
					String[] split = errorMsg.split(" ");
					errorMsg=split[0];
					System.out.println("获取的错误信息是："+errorMsg);
					//通过调研，发现图片验证码输入错误，这几个公积金网站的提示都是一样的
					if(errorMsg.contains("您输入的验证码与图片不符")){
						System.out.println("验证码识别错误");
					}
					////身份证号输入有误的各网站提示
					else if(errorMsg.contains("此身份证号无对应账户信息") || errorMsg.contains("个人证件号码") || 
							errorMsg.contains("该身份证号在系统中不存在或该账号已销户") || errorMsg.contains("身份证转换错误")
							|| errorMsg.contains("身份证号不正确") || errorMsg.contains("身份证号或公积金帐号不正确")){
						 System.out.println("操作失败:进行身份校验时出错:账号输入有误！");
					//密码输入有误的各网站提示
					}else if(errorMsg.contains("您输入的密码有误") || errorMsg.contains("个人登录密码不正确") 
							|| errorMsg.contains("密码错误") || errorMsg.contains("用户密码不匹配")){
						 System.out.println("进行身份校验时出错:您输入的密码有误，请重新输入！");
					//其他错误
					}else if(errorMsg.contains("操作失败:系统错误") || errorMsg.contains("系统日终尚未结束") 
							|| errorMsg.contains("一台机器只能登陆一个用户")){
						 System.out.println("操作失败:出现了用户名或密码输入有误以外的错误"+errorMsg);
					}else{
						System.out.println("登录失败，出现了调研时没有遇到的错误："+errorMsg);
					} 
				}
			}
			driver.quit();   //关闭浏览器,释放资源
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
			driver.quit();   //关闭浏览器,释放资源
		}
	}
}
