package app.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiImageInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiImageInfoRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

/**
 * 济宁社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.yantai" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.yantai" })
public class InsuranceYanTaiService implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private InsuranceYantaiImageInfoRepository insuranceYantaiImageInfoRepository;
	public static String driverPath = "D:\\IEDriverServer_Win32\\8.15\\chromedriver.exe";
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		System.setProperty("webdriver.chrome.driver", driverPath);
		try {
			ChromeOptions chromeOptions = new ChromeOptions();
			ChromeDriver driver = new ChromeDriver(chromeOptions);

			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = "http://ytrsj.gov.cn:8081/hsp/logonDialog_withF.jsp";
			driver.get(baseUrl);

			Thread.sleep(1000);

			// 个人公积金账户
			driver.findElement(By.id("yhmInput")).sendKeys("37068619910213653X");
			Thread.sleep(1000);
			// 身份证号
			driver.findElement(By.id("mmInput")).sendKeys("wang6200");
			Thread.sleep(1000);
			// 验证码
			String path = saveImg(driver, By.xpath(("//*[@id=\"logoninfo\"]/div/table/tbody/tr[5]/td[2]")));

			System.out.println("path---------------" + path);

			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("5201", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 505
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("authcode_result")).sendKeys(code);
			Thread.sleep(1000);
			// 模拟登录
			driver.findElement(By.className("logonBtn")).click();

			Thread.sleep(3000);

			String pageSource = driver.getPageSource();
			System.out.println("源代码" + pageSource);

			String currentUrl = driver.getCurrentUrl();
			// http://ytrsj.gov.cn:8081/hsp/mainFrame.jsp?&__usersession_uuid=USERSESSION_94a53302_367c_4ea1_b5f5_0983e87ac942&_width=1034&_height=708
			String[] split = currentUrl.split("__usersession_uuid=");
			String[] split2 = split[1].split("&");
			System.out.println("首页地址-----" + currentUrl);
			System.out.println("入参__usersession_uuid-----" + split2[0]);

			//将信息的入参存进数据库
			InsuranceYantaiImageInfo  insuranceYantaiImageInfo = new InsuranceYantaiImageInfo();
			insuranceYantaiImageInfo.setTaskid(parameter.getTaskId());
			insuranceYantaiImageInfo.setRequestParameter(split2[0]);
			insuranceYantaiImageInfoRepository.save(insuranceYantaiImageInfo);
			
			
			Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

			WebClient webClient = WebCrawler.getInstance().getWebClient();//

			for (org.openqa.selenium.Cookie cookie : cookies) {
				System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
				webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ytrsj.gov.cn",
						cookie.getName(), cookie.getValue()));
			}

			if (pageSource.contains("重新登录") && pageSource.contains("退出")) {
				System.out.println("登陆成功！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

				String cookies1 = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setCookies(cookies1);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("异常错误!请从新登陆！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			System.out.println("异常错误!请从新登陆！");
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;

	}

	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("InsuranceYanTaiService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		
		// 爬取解析基本信息
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance);

		// 爬取解析养老保险
		for (int k = 0; k < 4; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析医疗保险
		for (int k = 0; k < 4; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析失业保险()
		for (int k = 0; k < 4; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerUnemploymentInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析生育保险()
		for (int k = 0; k < 4; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerShengyuInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析工伤保险()
		for (int k = 0; k < 4; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerGongshangInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 更新最终的状态
		taskInsurance = insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId());
		System.out.println("数据采集完成之后的-----" + taskInsurance.toString());

		return taskInsurance;
	}

	// 通过taskid将登录界面的cookie存进数据库
	public void saveCookie(InsuranceRequestParameters parameter, String cookies) {
		taskInsuranceRepository.updateCookiesByTaskid(cookies, parameter.getTaskId());
	}

	/**
	 * 获取TaskInsurance
	 * 
	 * @param parameter
	 * @return
	 */
	public TaskInsurance getTaskInsurance(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		return taskInsurance;
	}

	// 将字符串md5加密，返回加密后的字符串
	public String md5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static String saveImg(WebDriver driver, By selector) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page
		WebElement ele = driver.findElement(selector);
		Point point = ele.getLocation();
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight();
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------" + point.getX());
		System.out.println("point.getY()-------" + point.getY());
		System.out.println("eleWidth-------" + eleWidth);
		System.out.println("eleHeight-------" + eleHeight);
		BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	public static File getImageLocalPath() {
		File parentDirFile = new File("D:\\img");
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File("D:\\img" + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
