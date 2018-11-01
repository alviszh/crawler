package app.test;

import java.io.IOException;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import net.sf.json.JSONObject;

/**
 * @description：测试爬取个人明细信息，哪些参数可以去掉(通过测试了发现，在获取个人明细之前的那个请求的参数中，如下没有注释的参数是有用的)
 * @author: sln 
 * @date: 
 */
public class MainTest {
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
			
			String loginUrl="http://60.211.249.146:9082/jnwsyytpub/indexa.jsp";
			String firstPageUrl="http://60.211.249.146:9082/jnwsyytpub/per.login";
			String loginIpOrHost="60.211.249.146";
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
			driver.findElement(By.name("certinum")).sendKeys("370829199207064927");
			Thread.sleep(1000);
			//输入密码
			driver.findElement(By.name("perpwd")).sendKeys("120007616503");
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
			 		
			 		////////////////////////////////////////////////////////
			 		//测试获取明细信息
			 		String url="http://60.211.249.146:9082/jnwsyytpub/command.summer?uuid=1512552925459";
			 		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
			 		//通过测试，发现只需要accnum即可
			 		String requestBody=""
//			 				+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
			 				+ "&_ACCNUM=120007616503"
			 				+ "&_PAGEID=step1"
			 				+ "&_IS=-745282"
//			 				+ "&_LOGIP=20171206172922055"
//			 				+ "&_ACCNAME=%E7%8E%8B%E7%84%95"
			 				+ "&isSamePer=false"
			 				+ "&_PROCID=60020007"
//			 				+ "&_SENDOPERID=370829199207064927"
//			 				+ "&_DEPUTYIDCARDNUM=370829199207064927"
//			 				+ "&_SENDTIME=2017-12-06"
			 				+ "&_BRANCHKIND=0"
//			 				+ "&_SENDDATE=2017-12-06"
//			 				+ "&CURRENT_SYSTEM_DATE=2017-12-06"
			 				+ "&_TYPE=init"
							+ "&_ISCROP=0"
//			 				+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E8%B4%A6%E6%9F%A5%E8%AF%A2"
			 				+ "&_WITHKEY=0"
//			 				+ "&AccName=%E7%8E%8B%E7%84%95"
			 				+ "&AccNum=120007616503"
//			 				+ "&CertiNum=370829199207064927"
//			 				+ "&OpenDate=2015-08-20"
//			 				+ "&Balance=8317.09"
//			 				+ "&UnitAccNum=18010010210194"
//			 				+ "&UnitAccName=%E9%9D%92%E5%B2%9B%E4%B8%AD%E5%8A%B3%E8%81%94%E5%8A%B3%E5%8A%A1%E6%9C%8D%E5%8A%A1%E8%BF%9E%E9%94%81%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E6%B5%8E%E5%AE%81%E5%88%86%E5%85%AC%E5%8F%B8"
//			 				+ "&LastPayDate=2017-10-01"
//			 				+ "&BegDate=1998-12-16"
//			 				+ "&EndDate=2017-12-28"
			 				+ "&dynamicTable_flag=0"
			 				+ "&instancenum=-745282";
//			 				
			 		webRequest.setRequestBody(requestBody);
			 		Page page = webClient.getPage(webRequest);
			 		if(null!=page){
			 			String html=page.getWebResponse().getContentAsString();
			 			System.out.println("响应的页面是："+html);
			 			String returnCode = JSONObject.fromObject(html).getString("returnCode");
			 			if(returnCode.equals("0")){
			 				url="http://60.211.249.146:9082/jnwsyytpub/dynamictable?uuid=1512552931760";
			 				webRequest=new WebRequest(new URL(url),HttpMethod.POST);
			 				requestBody="dynamicTable_id=datalist"
			 						+ "&dynamicTable_currentPage=1"
			 						+ "&dynamicTable_pageSize=300"
			 						+ "&dynamicTable_nextPage=1"
			 						+ "&dynamicTable_page=%2Fydpx%2F60020007%2F602007_01.ydpx"
			 						+ "&dynamicTable_paging=true"
			 						+ "&dynamicTable_configSqlCheck=0"
			 						+ "&errorFilter=1%3D1"
//			 						+ "&AccName=%E7%8E%8B%E7%84%95"
			 						+ "&AccNum=120007616503"
//			 						+ "&CertiNum=370829199207064927"
//			 						+ "&OpenDate=2015-08-20"
//			 						+ "&Balance=8317.09"
//			 						+ "&UnitAccNum=18010010210194"
//			 						+ "&UnitAccName=%E9%9D%92%E5%B2%9B%E4%B8%AD%E5%8A%B3%E8%81%94%E5%8A%B3%E5%8A%A1%E6%9C%8D%E5%8A%A1%E8%BF%9E%E9%94%81%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E6%B5%8E%E5%AE%81%E5%88%86%E5%85%AC%E5%8F%B8"
//			 						+ "&LastPayDate=2017-10-01"
//			 						+ "&BegDate=1998-12-16"
//			 						+ "&EndDate=2017-12-28"
			 						+ "&_APPLY=0"
			 						+ "&_CHANNEL=1"
			 						+ "&_PROCID=60020007"
			 						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAhkYXRhbGlzdHQBBnNlbGVjdCBzdWJzdHIoKGNhc2Ugd2hlbiBvcGVyPScxMDAx%0AJyB0aGVuIHRyYW5zZGF0ZSBlbHNlIG51bGwgZW5kKSwxLDcpIGFzIHRyYW5zZGF0ZSwoY2FzZSBi%0AaXJ0aGRheSB3aGVuICcxODk5LTEyLTMxJyB0aGVuIG51bGwgZWxzZSBiaXJ0aGRheSBlbmQpIGFz%0AIGJpcnRoZGF5LCBvcGVyLCBhbXQxLCBhbXQyLCBiYXNlbnVtYmVyLCBpbnN0YW5jZW51bSBmcm9t%0AIGRwMDc3IHdoZXJlIGluc3RhbmNlbnVtID0gLTc0NTI4MiBvcmRlciBieSByZWFzb24sb3BlciB4%0A&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABR0AAdf%0AQUNDTlVNdAAMMTIwMDA3NjE2NTAzdAALX1VOSVRBQ0NOVU1wdAAHX1BBR0VJRHQABXN0ZXAxdAAD%0AX0lTc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98CAAFKAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVt%0AYmVyhqyVHQuU4IsCAAB4cP%2F%2F%2F%2F%2F%2F9KC%2BdAAMX1VOSVRBQ0NOQU1FcHQABl9MT0dJUHQAETIwMTcx%0AMjA2MTcyOTIyMDU1dAAIX0FDQ05BTUV0AAbnjovnhJV0AAlpc1NhbWVQZXJ0AAVmYWxzZXQAB19Q%0AUk9DSUR0AAg2MDAyMDAwN3QAC19TRU5ET1BFUklEdAASMzcwODI5MTk5MjA3MDY0OTI3dAAQX0RF%0AUFVUWUlEQ0FSRE5VTXQAEjM3MDgyOTE5OTIwNzA2NDkyN3QACV9TRU5EVElNRXQACjIwMTctMTIt%0AMDZ0AAtfQlJBTkNIS0lORHQAATB0AAlfU0VORERBVEV0AAoyMDE3LTEyLTA2dAATQ1VSUkVOVF9T%0AWVNURU1fREFURXEAfgAfdAAFX1RZUEV0AARpbml0dAAHX0lTQ1JPUHEAfgAddAAJX1BPUkNOQU1F%0AdAAV5Liq5Lq65piO57uG6LSm5p%2Bl6K%2BidAAHX1VTQktFWXB0AAhfV0lUSEtFWXEAfgAdeHQACEBT%0AeXNEYXRldAAHQFN5c0RheXQACUBTeXNNb250aHQACEBTeXNUaW1ldAAIQFN5c1dlZWt0AAhAU3lz%0AWWVhcg%3D%3D";
			 				webRequest.setRequestBody(requestBody);
			 				Page page2 = webClient.getPage(webRequest);
			 				if(page2!=null){
			 					html=page2.getWebResponse().getContentAsString();
			 					System.out.println("获取的个人流水信息的html是："+html);
			 				}
			 			}
			 			System.out.println("hahahaha");
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
