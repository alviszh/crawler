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
			 		
			 		////////////////////////////////////////////////////////
			 		//测试获取明细信息
			 		String url="http://121.17.186.64:6675/wsyyt/command.summer?uuid=1512463329268";
			 		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
			 		String requestBody=""
//			 				+ "%24page=%2Fydpx%2F60020007%2F602007_01.ydpx"
			 				+ "&_ACCNUM=113012776892"
//			 				+ "&_UNITACCNUM=20101050449"
			 				+ "&_PAGEID=step1"
//			 				+ "&_IS=-276386"
//			 				+ "&_UNITACCNAME=%E7%9F%B3%E5%AE%B6%E5%BA%84%E8%AF%BA%E4%BA%9A%E9%80%9A%E4%BF%A1%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E8%A1%A1%E6%B0%B4%E5%88%86%E5%85%AC%E5%8F%B8"
//			 				+ "&_LOGIP=172.16.110.55"
//			 				+ "&_ACCNAME=%E9%83%9D%E9%9B%AA%E7%87%95"
//			 				+ "&isSamePer=false"
//			 				+ "&_SENDOPERID=131102199111230220"
			 				+ "&_PROCID=60020007"
//			 				+ "&_DEPUTYIDCARDNUM=131102199111230220"
//			 				+ "&_SENDTIME=2017-12-05"
//			 				+ "&_SENDDATE=2017-12-05"
//			 				+ "&_BRANCHKIND=0"
//			 				+ "&CURRENT_SYSTEM_DATE=2017-12-05"
//			 				+ "&_TYPE=init"
//			 				+ "&_ISCROP=0"
//			 				+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E8%B4%A6%E6%9F%A5%E8%AF%A2"
//			 				+ "&_WITHKEY=0"
//			 				+ "&AccName=%E9%83%9D%E9%9B%AA%E7%87%95"
			 				+ "&AccNum=113012776892"
//			 				+ "&CertiNum=131102199111230220"
//			 				+ "&OpenDate=2015-02-02"
//			 				+ "&Balance=10663.61"
//			 				+ "&UnitAccNum=20101050449"
//			 				+ "&UnitAccName=%E7%9F%B3%E5%AE%B6%E5%BA%84%E8%AF%BA%E4%BA%9A%E9%80%9A%E4%BF%A1%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E8%A1%A1%E6%B0%B4%E5%88%86%E5%85%AC%E5%8F%B8"
//			 				+ "&trancode=110015"
			 				+ "&accnum=113012776892"
//			 				+ "&unitaccnum=20101050449"
			 				+ "&year=2014";   //默认只能查当前年开始往前推三年的，哪怕此处写的是2014，返回的数据也是2015年的
//			 				+ "&instance=-276386";
			 		webRequest.setRequestBody(requestBody);
			 		Page page = webClient.getPage(webRequest);
			 		if(null!=page){
			 			String html=page.getWebResponse().getContentAsString();
			 			System.out.println("响应的页面是："+html);
			 			String returnCode = JSONObject.fromObject(html).getString("returnCode");
			 			if(returnCode.equals("0")){
			 				url="http://121.17.186.64:6675/wsyyt/dynamictable?uuid=1512463330997";
			 				webRequest=new WebRequest(new URL(url),HttpMethod.POST);
			 				requestBody=""
			 						+ "dynamicTable_id=list1"
			 						+ "&dynamicTable_currentPage=1"
			 						+ "&dynamicTable_pageSize=100"
			 						+ "&dynamicTable_nextPage=1"
			 						+ "&dynamicTable_page=%2Fydpx%2F60020007%2F602007_01.ydpx"  //必要的参数
			 						+ "&dynamicTable_paging=true"
			 						+ "&dynamicTable_configSqlCheck=0"
			 						+ "&errorFilter=1%3D1"
//			 						+ "&AccName=%E9%83%9D%E9%9B%AA%E7%87%95"
			 						+ "&AccNum=113012776892"
//			 						+ "&CertiNum=131102199111230220"
//			 						+ "&OpenDate=2015-02-02"
//			 						+ "&Balance=10663.61"
//			 						+ "&UnitAccNum=20101050449"
//			 						+ "&UnitAccName=%E7%9F%B3%E5%AE%B6%E5%BA%84%E8%AF%BA%E4%BA%9A%E9%80%9A%E4%BF%A1%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E8%A1%A1%E6%B0%B4%E5%88%86%E5%85%AC%E5%8F%B8"
			 						+ "&_APPLY=0"
			 						+ "&_CHANNEL=1"
			 						+ "&_PROCID=60020007"
			 						+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAVsaXN0MXQAsz0ic2VsZWN0IChjYXNlIHRyYW5zZGF0ZSB3aGVuICcxODk5LTEy%0ALTMxJyB0aGVuIG51bGwgZWxzZSB0cmFuc2RhdGUgZW5kKSBhcyB0cmFuc2RhdGUsIGNlcnRpbnVt%0ALCBhbXQyLCBhbXQxLCBiYXNlbnVtLCBpbnN0YW5jZSBmcm9tIGRwMDc3IHdoZXJlIGluc3RhbmNl%0AID0gIitfSVMgKyIgb3JkZXIgYnkgc2Vxbm8ieA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABZ0AAhf%0AQnJjQ29kZXB0AAdfQUNDTlVNdAAMMTEzMDEyNzc2ODkydAALX1VOSVRBQ0NOVU10AAsyMDEwMTA1%0AMDQ0OXQAB19QQUdFSUR0AAVzdGVwMXQAA19JU3NyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgAB%0ASgAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F%2FvIXnQADF9VTklU%0AQUNDTkFNRXQANuefs%2BWutuW6hOivuuS6mumAmuS%2FoeacjeWKoeaciemZkOWFrOWPuOihoeawtOWI%0AhuWFrOWPuHQABl9MT0dJUHQADTE3Mi4xNi4xMTAuNTV0AAhfQUNDTkFNRXQACemDnembqueHlXQA%0ACWlzU2FtZVBlcnQABWZhbHNldAAJX0NFTlRFUklEcHQAC19TRU5ET1BFUklEdAASMTMxMTAyMTk5%0AMTExMjMwMjIwdAAHX1BST0NJRHQACDYwMDIwMDA3dAAQX0RFUFVUWUlEQ0FSRE5VTXQAEjEzMTEw%0AMjE5OTExMTIzMDIyMHQACV9TRU5EVElNRXQACjIwMTctMTItMDV0AAlfU0VORERBVEV0AAoyMDE3%0ALTEyLTA1dAALX0JSQU5DSEtJTkR0AAEwdAATQ1VSUkVOVF9TWVNURU1fREFURXEAfgAhdAAFX1RZ%0AUEV0AARpbml0dAAHX0lTQ1JPUHEAfgAjdAAJX1BPUkNOQU1FdAAV5Liq5Lq65piO57uG6LSm5p%2Bl%0A6K%2BidAAHX1VTQktFWXB0AAhfV0lUSEtFWXEAfgAjeHQACEBTeXNEYXRldAAHQFN5c0RheXQACUBT%0AeXNNb250aHQACEBTeXNUaW1ldAAIQFN5c1dlZWt0AAhAU3lzWWVhcg%3D%3D";
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
