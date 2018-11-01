package test;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class Test4 extends AbstractChaoJiYingHandler{
	
	static String driverPath = "D:\\ChromeServer\\chromedriver.exe";

	static Boolean headless = false;
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	public static void main(String[] args) throws Exception{
		/*String url = "http://219.132.4.6:6012/web/ggfw/app/index.html#/ggfw/home";
		String loginImgUrl = "http://219.132.4.6:6012/web/ImageCheck.jpg";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Page page = webClient.getPage(url);
		WebRequest webRequest = new WebRequest(new URL(loginImgUrl), HttpMethod.GET);
		Page loginPage = webClient.getPage(webRequest);
//		File file = new File("E:\\Codeimg\\zhanjiang.jpg");
		InputStream inputStream = loginPage.getWebResponse().getContentAsStream();
		
		FileOutputStream fileOut = new FileOutputStream(new File("E:\\Codeimg\\zhanjiang.jpg"));  
        byte[] buf = new byte[1024 * 8];  
        while (true) {  
            int read = 0;  
            if (inputStream != null) {  
                read = inputStream.read(buf);  
            }  
            if (read == -1) {  
                break;  
            }  
            fileOut.write(buf, 0, read);  
        } 
        
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		
		String loginUrl = "http://219.132.4.6:6012/web/ajaxlogin.do";
		webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "219.132.4.6:6012");
		webRequest.setAdditionalHeader("Origin", "http://219.132.4.6:6012");
		webRequest.setAdditionalHeader("Referer", "http://219.132.4.6:6012/web/ggfw/app/index.html");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody("LOGINID=440803199106231129&&PASSWORD=32313a3230373a3233323a3135373a3230333a3231333a3234333a3138323a3133323a3138373a3232313a3134333a38363a33333a3139303a3139393a3131353a3133323a35323a31393a3130373a3232393a38393a31363a32313a3230373a3134373a36363a35373a3232363a3138333a3235&&IMAGCHECK="+input+"&&OPERTYPE2=3");
		Page loginedPage = webClient.getPage(webRequest);
		System.out.println("loginedPage===>"+loginedPage.getWebResponse().getContentAsString());*/
		loginBySelenium();
	}
	
	public static void loginBySelenium() throws Exception{
		WebDriver driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		String baseUrl = "http://218.95.230.61/wt-web/grlogin";
		driver.get(baseUrl);
		
		WebDriverWait wait=new WebDriverWait(driver, 10);
//		WebElement notice= wait.until(new ExpectedCondition<WebElement>() {  
//		            public WebElement apply(WebDriver driver) {  
//		                return driver.findElement(By.xpath("//a[@title='cancel']"));  
//		            } 
//		        });
//		notice.click();
//		
//		WebElement loginFrame= wait.until(new ExpectedCondition<WebElement>() {  
//            public WebElement apply(WebDriver driver) {  
//                return driver.findElement(By.xpath("//a[@ng-click='login()']"));  
//            } 
//        });
//		loginFrame.click();

//		WebElement LOGINID= wait.until(new ExpectedCondition<WebElement>() {  
//            public WebElement apply(WebDriver driver) {  
//                return driver.findElement(By.name("LOGINID"));  
//            } 
//        });
		
		//WebElement username = driver.findElement(By.name("LOGINID"));
		WebElement username = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("in_password"));
		WebElement imagecheck = driver.findElement(By.id("captcha"));
		String path = WebDriverUnit.saveImg(driver, By.id("captcha_img"), new File("D:\\img\\zhanjiang.jpg")); 
		System.out.println("path---------------"+path); 
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path); 
		System.out.println("chaoJiYingResult---------------"+chaoJiYingResult); 
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str"); 
		System.out.println("code ====>>"+code); 

		WebElement button = driver.findElement(By.xpath("//input[@id='gr_login']"));
		
		username.sendKeys("630103199102270822");
		password.sendKeys("8200549wyp");
		imagecheck.sendKeys(code);
		button.click();
		
		WebElement imagecheck2= wait.until(new ExpectedCondition<WebElement>() {  
          public WebElement apply(WebDriver driver) {  
              return driver.findElement(By.id("force_captcha"));  
          } 
       });
			
		String path2 = WebDriverUnit.saveImg(driver, By.xpath("//img[@src='/wt-web/captcha']"), new File("D:\\img\\zhanjiang2.jpg")); 
		System.out.println("path---------------"+path2); 
		String chaoJiYingResult2 = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, path2); 
		System.out.println("chaoJiYingResult2---------------"+chaoJiYingResult2); 
		Gson gson2 = new GsonBuilder().create();
		String code2 = (String) gson2.fromJson(chaoJiYingResult2, Map.class).get("pic_str"); 
		imagecheck2.sendKeys(code2);
		WebElement button2 = driver.findElement(By.id("qzdl"));
		button2.click();
		Alert alert = null;
		try {
			alert = wait.until(new Function<WebDriver, Alert>() {  
	            public Alert apply(WebDriver driver) {  
	                return driver.switchTo().alert(); 
	            }
	        });
		} catch (Exception e) {
			System.out.println("没有alert提示框");
		}
		
		if(null != alert){
			String alertText = alert.getText();
			System.out.println("弹框提示=="+alertText);
			alert.accept();
		}
		
		Thread.sleep(1000);
		System.out.println("page--->"+driver.getPageSource());
		
		//验证码错误    <div class="tooltip-inner" id="yzm_tip">验证码错误</div>
		//	<span id="force_captcha_err"><i>验证码输入错误</i></span>
		//<div class="tooltip-inner" id="username_tip">用户名无效</div>
		//<div class="tooltip-inner" id="username_tip">用户名格式错误</div>
		//<div class="tooltip-inner" id="pwd_tip">个人密码错误</div>
		
//		Document doc = Jsoup.parse(driver.getPageSource());
//		String username = doc.body().select(".ng-binding").text();
//		System.out.println("登陆后的名字==》"+username);
//		String msg = username.substring(4);
//		if(msg.contains("登录失败")){
//			System.out.println("login Fail"+msg);
//		}else{
//			System.out.println("login success"+msg);
//		}
		

		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
		for (org.openqa.selenium.Cookie cookie : cookies) {
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson);
		}
		String cookieJson = new Gson().toJson(cookiesSet);
		
		WebClient webClient= addcookie(cookieJson);
		String url="http://218.95.230.61/wt-web/jcr/jcrkhxxcx_mh.service";
		

		WebRequest webRequest=  new WebRequest(new URL(url), HttpMethod.POST);
		String body="ffbm=01&ywfl=01&ywlb=99&cxlx=01";
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "218.95.230.61");
		webRequest.setAdditionalHeader("Origin", "http://218.95.230.61");
		webRequest.setAdditionalHeader("Referer", "http://218.95.230.61/wt-web/home?logintype=1");	
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	//Token 1620726984
		webRequest.setRequestBody(body);
		Page page=webClient.getPage(webRequest);
		System.out.println("page="+page.getWebResponse().getContentAsString());
		//{"success":true,"msg":null,"totalcount":1,"results":[{"totalcount":0,"success":null,"dwbh":"000094000228","dwmc":"青海易才人力资源顾问公司百分之十","grbh":"01000000094041018882","grzh":"01000940000410018119","xingming":"刘莉","xmqp":"liuli","xingbie":"女性","gddhhm":"","sjhm":"13086275847","zjlx":"身份证","zjhm":"630103199102270822","csny":"1991-02-27","hyzk":"未说明的婚姻状况","zhiye":"其他","zhichen":"其他","zhiwu":"其他","xueli":"其他","yzbm":"","jtzz":"","jtysr":"0.0","xmjp":"ll","cjgzrq":"2016-04-25 00:00:00.0","szbm":"","txdz":"","poxm":"","pozjlx":"","pozjhm":"","zfqk":"","sfyzfdk":"无贷款","cdgx":"","sbzh":"","grysr":0.0,"xydj":"","jgbm":"0101","djrq":"2016-04-25","djsj":"2016-04-25 16:30:06.0","djczyid":2826,"djczy":"","cym":"","ywm":"","csd":"","minzu":"","shengao":"0","tizhong":"0","zysl":0.0,"yysl":0.0,"jtdh":"","hjszd":"","daszd":"","zzmm":"","zjxy":"","zhuanye":"","byyx":"","rzrq":"2016-04-25 00:00:00.0","xzz":"","xzzdh":"","jzhm":"","yuzhong":"","wysp":"","xuexing":"","aihao":"","techang":"","mqgzzt":"","dzxx":"","dlzt":"","grlbbm":"","grzhye":"311.23","gryjce":"150.0","dwyjce":"150.0","grjcjs":"1,500.00","grckzhhm":"","grckzhkhyhmc":"","grckzhkhyhdm":"","grckzhhmssyhbm":"","zhsfdj":"0","grzhzt":"正常","jzny":"201804","dwzh":"99051069","yjce":"300.00","grjcl":"0.1","dwjcl":"0.1","jcbl":"单位10.0%,个人10.0%","dkqk":null,"dkcs":null,"htdkje":null,"dkye":null,"yhrq":null,"dkffrq":null,"tqyy":null,"tqjehj":null,"tqrq":null,"tqfs":null}],"erros":null,"vdMapList":null,"data":{"totalcount":0,"success":null,"dwbh":"000094000228","dwmc":"青海易才人力资源顾问公司百分之十","grbh":"01000000094041018882","grzh":"01000940000410018119","xingming":"刘莉","xmqp":"liuli","xingbie":"女性","gddhhm":"","sjhm":"13086275847","zjlx":"身份证","zjhm":"630103199102270822","csny":"1991-02-27","hyzk":"未说明的婚姻状况","zhiye":"其他","zhichen":"其他","zhiwu":"其他","xueli":"其他","yzbm":"","jtzz":"","jtysr":"0.0","xmjp":"ll","cjgzrq":"2016-04-25 00:00:00.0","szbm":"","txdz":"","poxm":"","pozjlx":"","pozjhm":"","zfqk":"","sfyzfdk":"无贷款","cdgx":"","sbzh":"","grysr":0.0,"xydj":"","jgbm":"0101","djrq":"2016-04-25","djsj":"2016-04-25 16:30:06.0","djczyid":2826,"djczy":"","cym":"","ywm":"","csd":"","minzu":"","shengao":"0","tizhong":"0","zysl":0.0,"yysl":0.0,"jtdh":"","hjszd":"","daszd":"","zzmm":"","zjxy":"","zhuanye":"","byyx":"","rzrq":"2016-04-25 00:00:00.0","xzz":"","xzzdh":"","jzhm":"","yuzhong":"","wysp":"","xuexing":"","aihao":"","techang":"","mqgzzt":"","dzxx":"","dlzt":"","grlbbm":"","grzhye":"311.23","gryjce":"150.0","dwyjce":"150.0","grjcjs":"1,500.00","grckzhhm":"","grckzhkhyhmc":"","grckzhkhyhdm":"","grckzhhmssyhbm":"","zhsfdj":"0","grzhzt":"正常","jzny":"201804","dwzh":"99051069","yjce":"300.00","grjcl":"0.1","dwjcl":"0.1","jcbl":"单位10.0%,个人10.0%","dkqk":null,"dkcs":null,"htdkje":null,"dkye":null,"yhrq":null,"dkffrq":null,"tqyy":null,"tqjehj":null,"tqrq":null,"tqfs":null}}

		String url22="http://218.95.230.61/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2013-01-02&jsrq=2018-04-26&ndxz=%E5%85%A8%E9%83%A8&fontSize=13px&pageNum=1&pageSize=10";
	    HtmlPage page22=getHtml(url22,webClient);
		System.out.println("page22=="+page22.getWebResponse().getContentAsString());
		
		
		String url2="http://218.95.230.61/wt-web/jcr/jcrxxcxzhmxcx.service";
		String body2="ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2013-01-02&jsrq=2018-04-26&ndxz=%E5%85%A8%E9%83%A8&fontSize=13px&pageNum=1&pageSize=500";
		WebRequest webRequest2=  new WebRequest(new URL(url2), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest2.setAdditionalHeader("Host", "218.95.230.61");
		webRequest2.setAdditionalHeader("Origin", "http://218.95.230.61");
		webRequest2.setAdditionalHeader("Referer", "http://218.95.230.61/wt-web/home?logintype=1");	
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");	//Token 1620726984
		webRequest2.setRequestBody(body2);
		Page page2=webClient.getPage(webRequest2);
		System.out.println("page2="+page2.getWebResponse().getContentAsString());
		
		
		//{"success":true,"msg":null,"totalcount":42720,"results":[{"totalcount":0,"msg":null,"grzh":"01000220000110000001","jzrq":"2013-01-07","gjhtqywlx":"汇缴","fse":"672.00","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301072201_00001070","czbz":"非冲账","id":"1000124275762","tsnr":"汇缴:672.00元"},{"totalcount":0,"msg":null,"grzh":"01000220000110000003","jzrq":"2013-01-07","gjhtqywlx":"汇缴","fse":"1,080.00","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301072201_00001070","czbz":"非冲账","id":"1000124275789","tsnr":"汇缴:1,080.00元"},{"totalcount":0,"msg":null,"grzh":"01000220000110000007","jzrq":"2013-01-07","gjhtqywlx":"汇缴","fse":"552.00","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301072201_00001070","czbz":"非冲账","id":"1000124275822","tsnr":"汇缴:552.00元"},{"totalcount":0,"msg":null,"grzh":"01000220000110000008","jzrq":"2013-01-07","gjhtqywlx":"汇缴","fse":"552.00","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301072201_00001070","czbz":"非冲账","id":"1000124275843","tsnr":"汇缴:552.00元"},{"totalcount":0,"msg":null,"grzh":"01000220000110000009","jzrq":"2013-01-07","gjhtqywlx":"汇缴","fse":"600.00","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301072201_00001070","czbz":"非冲账","id":"1000124275850","tsnr":"汇缴:600.00元"},{"totalcount":0,"msg":null,"grzh":"01000220000110000005","jzrq":"2013-01-10","gjhtqywlx":"销户提取","fse":"4,200.79","dngjfse":2400.0,"snjzfse":1800.79,"fslxe":"27.78","tqyy":"","tqfs":"","ywlsh":"11301102201_00006105","czbz":"非冲账","id":"1000124275811","tsnr":"销户提取:4,200.79元"},{"totalcount":0,"msg":null,"grzh":"01000250000450000006","jzrq":"2013-01-14","gjhtqywlx":"汇缴","fse":"391.00","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301142501_00011566","czbz":"非冲账","id":"1000125097097","tsnr":"汇缴:391.00元"},{"totalcount":0,"msg":null,"grzh":"01000110001160000059","jzrq":"2013-01-15","gjhtqywlx":"内部转移","fse":"-25,017.35","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301151101_00014945","czbz":"非冲账","id":"1000118350625","tsnr":"内部转移:-25,017.35元"},{"totalcount":0,"msg":null,"grzh":"01000280000540000038","jzrq":"2013-01-23","gjhtqywlx":"内部转移","fse":"-2,039.85","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301232801_00028738","czbz":"非冲账","id":"1000126070421","tsnr":"内部转移:-2,039.85元"},{"totalcount":0,"msg":null,"grzh":"01000140000110000279","jzrq":"2013-01-24","gjhtqywlx":"汇缴","fse":"0.00","dngjfse":0.0,"snjzfse":0.0,"fslxe":"0.00","tqyy":"","tqfs":"","ywlsh":"11301241401_00031638","czbz":"非冲账","id":"1000121331665","tsnr":"汇缴:0.00元"}],"erros":null,"vdMapList":null,"data":null}
//		
//		String infoUrl = "http://219.132.4.6:6012/web/ajax.do?r=0.7635891010868991&_isModel=true&params={'oper':'JbgrxxcxAction.query','params':{'MenuId':'104014'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129'}}}}";
//		driver.get(infoUrl);
//		System.out.println("infopage"+driver.getPageSource());
//		Document document = Jsoup.parse(driver.getPageSource());
//		String html = document.body().text();
//		System.out.println("要解析的html"+html);
//		JsonParser parser = new JsonParser();
//		JsonObject obj = (JsonObject)parser.parse(html);
//		JsonObject datas = obj.get("datas").getAsJsonObject();
//		JsonObject personInfo = datas.get("ncm_gt_个人基本资料").getAsJsonObject();
//		JsonObject params = personInfo.get("params").getAsJsonObject();
//		String idNum = params.get("证件号码").getAsString();
//		System.out.println(idNum);
//		Thread.sleep(500);
		
//		for (int i = 1; i < 6; i++) {
//			String Url = "http://219.132.4.6:6012/web/ajax.do?_isModel=true&params={'oper':'ZbgrjfqkcxAction.query','params':{'MenuId':'104020'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129','险种类型':'"+i+"0'}},'ncm_glt_个人已缴历史明细':{'heads':[],'params':{'pageSize':500,'curPageNum':1,'rowsCount':0,'Total_showMsg':null,'Total_showMsgCell':null,'Total_Cols':[]},'heads_change':[],'dataset':[]}}}";
//			driver.get(Url);
//			System.out.println(i+"----"+driver.getPageSource());
//			Thread.sleep(500);
//		}
//		
		
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
//		for(org.openqa.selenium.Cookie cookie : cookiesDriver){
//			Cookie cookieWebClient = new Cookie("219.132.4.6:6012", cookie.getName(), cookie.getValue());
//			webClient.getCookieManager().addCookie(cookieWebClient);
//		}
//		
//		WebRequest webRequest = new WebRequest(new URL(infoUrl), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		webRequest.setAdditionalHeader("Host", "219.132.4.6:6012");
//		webRequest.setAdditionalHeader("Origin", "http://219.132.4.6:6012");
//		webRequest.setAdditionalHeader("Referer", "http://219.132.4.6:6012/web/ggfw/app/index.html");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		webRequest.setRequestBody("_isModel=true&params={'oper':'JbgrxxcxAction.query','params':{'MenuId':'104014'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'440803199106231129'}}}}");
//		Page infoPage = webClient.getPage(webRequest);
//		System.out.println("loginedPage===>"+infoPage.getWebResponse().getContentAsString());
	}
	
	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	
	public static WebClient addcookie(String cookiesIn) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookiesIn);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
	public static String js(String e) throws Exception{
		
		String g = "";
		String j = "";
		String[] k = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
		for (int f = 0; f < e.length(); f++){
			if(f == 0){
				g = Integer.toHexString((int)e.charAt(f));
			}else{
				g += Integer.toHexString((int)e.charAt(f));
			}
			System.out.println("g==="+g);
			for (f = 0; f < g.length(); f++){
				j += k[g.charAt(f) >> 4] + k[g.charAt(f) & 15];
			}
		}
		
		return j;
	}
}
