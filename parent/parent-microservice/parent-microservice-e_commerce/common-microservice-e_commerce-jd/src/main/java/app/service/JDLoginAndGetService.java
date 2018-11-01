package app.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.TaskStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.zxing.Result;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.bean.MiddleErrorException;
import app.bean.WebParamE;
import app.commontracerlog.TracerLog;
import app.unit.CommonUnit;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：LoginAndGetService 类描述： 创建人：hyx
 * 创建时间：2017年12月14日 上午10:03:36
 * 
 * @version
 */
/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：LoginAndGetService 类描述：
 * 创建人：Administrator 创建时间：2017年12月19日 下午5:16:20
 * 
 * @version
 */
@Component
public class JDLoginAndGetService {

	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;

	@Autowired
	private E_CommerceTaskRepository e_commerceTaskRepository;

	@Autowired
	private AgentService agentService;

	@Autowired
	private TracerLog tracerLog;

	WebDriver driver;

	public WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// } chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	@Retryable(value = MiddleErrorException.class, maxAttempts = 3)
	public WebParamE<?> loginChrome(E_CommerceJsonBean e_CommerceJsonBean, int i) throws Exception {
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		driver = intiChrome();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://passport.jd.com/uc/login?ltype=logout";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement loginByUserButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("login-tab-r"));
			}
		});

		loginByUserButton.click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement loginname = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("loginname"));
			}
		});
		loginname.sendKeys(e_CommerceJsonBean.getUsername().trim());
		driver.findElement(By.id("nloginpwd")).click();

		driver.findElement(By.id("nloginpwd")).sendKeys(e_CommerceJsonBean.getPasswd().trim());// driver.findElement(By.className("login-btn")).click();

		driver.findElement(By.className("login-btn")).click();
		Thread.sleep(5000);

		if (driver.getPageSource().indexOf("请输入验证码") != -1) {
			System.out.println("JD_Verification1  显示");

			String code = CommonUnit.getVerfiycodeBy(By.id("JD_Verification1"), driver, this.getClass());
			driver.findElement(By.id("authcode")).sendKeys(code.trim());
			driver.findElement(By.className("login-btn")).click();
		}

		WebParamE<?> webParamE = new WebParamE<>();

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		try {
			wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					return driver.findElement(By.className("nickname"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e1) {

			}

			String html = driver.getPageSource();
			Document doc = Jsoup.parse(html);
			String errormessage = doc.select("div.msg-error").text();
			if (html.indexOf("因您的账户存在风险，需进一步校验您的信息以提升您的安全等级") != -1) {
				String phonenum = doc.select("div.phone-text").text();

				String msg = doc.select("span#code-msg").text();

				String msg_des = "接收短信手机号为" + phonenum + ";" + msg;

				webParamE.setErrormessage(msg_des);
				return webParamE;
			}
			if (errormessage.indexOf("请输入验证码") != -1) {
				System.out.println("JD_Verification1  显示");

				String code = CommonUnit.getVerfiycodeBy(By.id("JD_Verification1"), driver, this.getClass());
				driver.findElement(By.id("authcode")).sendKeys(code);
				driver.findElement(By.className("login-btn")).click();

				try {
					wait2.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {

							return driver.findElement(By.className("nickname"));
						}
					});
				} catch (Exception e1) {
					html = driver.getPageSource();
					doc = Jsoup.parse(html);

					// errormessage = doc.select("div.msg-error").text();
					//
					if (html.indexOf("因您的账户存在风险，需进一步校验您的信息以提升您的安全等级") != -1) {
						String phonenum = doc.select("div.phone-text").text();

						String msg = doc.select("span#code-msg").text();

						String msg_des = "接收短信手机号为" + phonenum + ";" + msg;

						webParamE.setErrormessage(msg_des);
						return webParamE;
					}

					errormessage = errorjuged(html, e_commerceTask);
					webParamE.setErrormessage(errormessage);
					agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);

					webParamE.setErrormessage(errormessage);

					return webParamE;
				}

			} else {
				errormessage = errorjuged(html, e_commerceTask);
			}
			webParamE.setErrormessage(errormessage);
			// driver.quit();
			// agentService.releaseInstance(e_commerceTask.getCrawlerHost(),
			// driver);
			agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);
			webParamE.setErrormessage(errormessage);

			return webParamE;
		}

		webParamE.setDriver(driver);
		return webParamE;
	}

	@Retryable(value = MiddleErrorException.class, maxAttempts = 3)
	public WebParamE<?> loginChromeByQRcode(E_CommerceJsonBean e_CommerceJsonBean) throws Exception {
		driver = intiChrome();

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://passport.jd.com/uc/login?ltype=logout";
		driver.get(baseUrl);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		// 扫码登录的table 点击后更换二维码
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.className("login-tab-l"));
			}
		});

		// loginByUserButton.click();

		wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement imgEle = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver
						.findElement(By.xpath("//*[@id='content']/div[2]/div[1]/div/div[5]/div/div[2]/div[1]/img"));
			}
		});

		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// Get entire page screenshot
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		org.openqa.selenium.Point point = imgEle.getLocation();
		// Get width and height of the element
		int eleWidth = imgEle.getSize().getWidth();
		int eleHeight = imgEle.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot

		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(eleScreenshot, "png", outputStream);
		
		Result result = QrcodeService.getQRresult(eleScreenshot);
		tracerLog.output("解析结果",result.toString());
		tracerLog.output("二维码格式类型",result.getBarcodeFormat().toString());
		tracerLog.output("二维码文本内容",result.getText());
//		
//		BinaryBitmap binaryBitmap= new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
//		//定义二维码参数
//		Map  hints= new HashMap<>();
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//		Result result = formatReader.decode(binaryBitmap, hints);
//		//输出相关的二维码信息
//		System.out.println("解析结果"+result.toString());
//		System.out.println("二维码格式类型"+result.getBarcodeFormat());
//		System.out.println("二维码文本内容"+result.getText());
//		bufferedImage.flush();


		String base64Code = Base64.getEncoder().encodeToString(outputStream.toByteArray());
		tracerLog.output("京东网二维码登录，二维码图片base64Code", base64Code);

		WebParamE<?> webParamE = new WebParamE<>();

		webParamE.setUrl(result.getText());
		webParamE.setBase64img(base64Code);
		return webParamE;
	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 验证是否已扫描登录
	 * 创建人：hyx 创建时间：2018年8月20日
	 * 
	 * @version 1 返回值 E_CommerceTask
	 */
	public WebParamE<?> checkJDQRcode(E_CommerceTask e_commerceTask) {
		String currentWebdriverHandle = driver.getWindowHandle();
		tracerLog.output(
				"checkQRcode ::::::::::::::::::::::::::::::: currentWebdriverHandle ::::::::::::::::::::::::::::::: ",
				currentWebdriverHandle);

		WebParamE<?> webParamE = new WebParamE<>();

		System.out.println("+++++++++++++++++++++++++++++++++webDriver.getCurrentUrl()++++++++++++++++++++++++++++"
				+ driver.getCurrentUrl());
		if (driver.getCurrentUrl().startsWith("https://www.jd.com/")) {
			try {

				driver.findElement(By.className("nickname"));

			} catch (Exception e1) {
				String html = driver.getPageSource();
				Document doc = Jsoup.parse(html);

				// errormessage = doc.select("div.msg-error").text();
				//
				if (html.indexOf("因您的账户存在风险，需进一步校验您的信息以提升您的安全等级") != -1) {
					String phonenum = doc.select("div.phone-text").text();

					String msg = doc.select("span#code-msg").text();

					String msg_des = "接收短信手机号为" + phonenum + ";" + msg;

					webParamE.setErrormessage(msg_des);
					//

					return webParamE;
				} else {
					webParamE.setErrormessage("二维码扫描验证中");
					return webParamE;
				}

			}
		} else {
			String html = driver.getPageSource();
			Document doc = Jsoup.parse(html);
			String err_cont = doc.select("p.err-cont").text();

			if (err_cont.indexOf("二维码已失效") != -1) {
				webParamE.setErrormessage(err_cont);
				return webParamE;
			}

			webParamE.setErrormessage("二维码扫描验证中");
			return webParamE;
		}

		webParamE.setDriver(driver);
		return webParamE;
	}

	private String errorjuged(String html, E_CommerceTask e_commerceTask) {

		// String html = driver.getPageSource();
		Document doc = Jsoup.parse(html);

		if (html.indexOf("因您的账户存在风险，需进一步校验您的信息以提升您的安全等级") != -1) {
			String phonenum = doc.select("div.phone-text").text();

			String msg = doc.select("span#code-msg").text();

			String msg_des = "接收短信手机号为" + phonenum + ";" + msg;

			// webParamE.setErrormessage(msg_des);
			return msg_des;
		}
		if (html.indexOf("您的账号存在被盗风险，为保障您的账户安全，请根据以下提示将密码重置后再登录") != -1) {

			// webParamE.setErrormessage("您的账号存在被盗风险，请到京东网站重置密码");
			agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);

			return "您的账号存在被盗风险，请到京东网站重置密码";
		}

		String errormessage = doc.select("div.msg-error").text();

		if (errormessage.indexOf("验证码错误") != -1 || errormessage.indexOf("验证码不正确") != -1) {
			driver.quit();

			throw new MiddleErrorException("验证码错误");
		}

		if (errormessage.isEmpty() || errormessage.length() <= 0) {
			driver.quit();

			throw new MiddleErrorException("验证码错误");

		}
		return errormessage;
	}

	public WebParamE<?> setSMS(E_CommerceJsonBean e_CommerceJsonBean) {

		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		driver.findElement(By.id("code")).sendKeys(e_commerceTask.getVerificationPhone().trim());
		driver.findElement(By.id("submitBtn")).click();

		WebParamE<?> webParamE = new WebParamE<>();

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		try {
			wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					return driver.findElement(By.className("nickname"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();

			try {
				String path = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				tracerLog.output("crawler.bank.login.cardnum.exception", path);
			} catch (Exception e1) {

			}

			String html = driver.getPageSource();

			Document doc = Jsoup.parse(html);

			String errormessage = doc.select("span#code-msg").text();

			if (errormessage.indexOf("短信校验码错误") != -1) {
				agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);
				webParamE.setErrormessage(errormessage);

				return webParamE;
			}
		}

		webParamE.setDriver(driver);
		return webParamE;

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取用户基本信息
	 * 创建人：hyx 创建时间：2017年12月19日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public WebDriver getUserInfo() {
		String url = "https://i.jd.com/user/info";

		driver.get(url);

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(By.linkText("基本信息"));
			}
		});

		return driver;

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取用户收货地址
	 * 创建人：hyx 创建时间：2017年12月19日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public WebDriver getReceiverAddress() throws Exception {

		String url = "https://order.jd.com/center/list.action";
		driver.get(url);

		Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		driver.findElement(By.linkText("收货地址")).click();

		wait2.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(By.id("edit-add-dialog"));
			}
		});

		return driver;
	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取白条信息
	 * 和小白信用分 创建人：hyx 创建时间：2017年12月19日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public String getBtPrivilege() throws Exception {
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("baitiao.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		String url = "https://baitiao.jd.com/v3/ious/getBtPrivilege";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "baitiao.jd.com");
		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		Page searchPage = webClient.getPage(webRequest);

		return searchPage.getWebResponse().getContentAsString();
	}

	public String getScoreInfo() throws Exception {
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("baitiao.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}

		// 获取小白信用分
		String url = "https://baitiao.jd.com/v3/ious/score_getScoreInfo";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "baitiao.jd.com");
		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");

		Page searchPage = webClient.getPage(webRequest);

		return searchPage.getWebResponse().getContentAsString();
	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取 京东绑定银行卡
	 * 创建人：hyx 创建时间：2017年12月19日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public WebDriver getQueryBindCard() throws Exception {
		String url = "https://authpay.jd.com/card/queryBindCard.action";

		driver.get(url);

		return driver;
	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 京东金条 创建人：hyx
	 * 创建时间：2017年12月19日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public String getCoffers() throws Exception {
		String url = "https://xjk.jr.jd.com/gold/account";

		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("xjk.jr.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "xjk.jr.jd.com");
		// webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://xjk.jr.jd.com/gold/page.htm");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		Page searchPage = webClient.getPage(webRequest);

		return searchPage.getWebResponse().getContentAsString();
	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取交易列表单
	 * 创建人：hyx 创建时间：2017年12月19日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public WebDriver getIndent(String url) throws Exception {
		// String url = "https://order.jd.com/center/list.action?d=2&page=2";

		driver.get(url);

		return driver;
	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取认证信息
	 * 创建人：hyx 创建时间：2017年12月19日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public WebDriver getRenZheng() throws Exception {

		String url = "https://authpay.jd.com/";
		driver.get(url);

		Thread.sleep(1000);

		url = "https://authpay.jd.com/auth/toAuthSuccessPage.action";

		driver.get(url);

		Thread.sleep(1000);

		return driver;
	}

	public String getRenZheng2() throws Exception {
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("authpay.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		String url = "https://authpay.jd.com/auth/toAuthSuccessPage.action";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.addRequestHeader("Host", "authpay.jd.com");
		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("Referer", "https://authpay.jd.com/");
		webClient.addRequestHeader("Upgrade-Insecure-Requests", "1");
		webClient.addRequestHeader("Referer", "https://authpay.jd.com/");

		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");
		// webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		Page searchPage = webClient.getPage(webRequest);

		return searchPage.getWebResponse().getContentAsString();
	}

	public  String getIndentForImgAndType(String indentids) throws Exception {
		
		
		System.out.println("=========indentids==========="+indentids);
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie("order.jd.com", cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		String url = "https://order.jd.com/lazy/getOrderProductInfo.action?orderWareIds="
				+ indentids;

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
//		webClient.addRequestHeader("Host", "baitiao.jd.com");
//		webClient.addRequestHeader("Origin", "https://baitiao.jd.com");
		webClient.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		webClient.addRequestHeader("Referer", "https://baitiao.jd.com/v3/ious/list?from=myzc-left-jrbt");

		Page searchPage = webClient.getPage(webRequest);

		return searchPage.getWebResponse().getContentAsString();
	}
	
	public E_CommerceTask quit(E_CommerceJsonBean e_CommerceJsonBean) {
		agentService.releaseInstance(e_CommerceJsonBean.getIp(), driver);
		E_CommerceTask task = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());
		task.setError_code(TaskStatusCode.SYSTEM_QUIT.getError_code());
		task.setError_message(TaskStatusCode.SYSTEM_QUIT.getDescription());

		task = e_commerceTaskRepository.save(task);
		return task;
	}

}
