package app.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tianjin.HousingTianJinBase;
import com.microservice.dao.entity.crawler.housing.tianjin.HousingTianJinHtml;
import com.microservice.dao.entity.crawler.housing.tianjin.HousingTianJinPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.tianjin.HousingTianJinBaseRepository;
import com.microservice.dao.repository.crawler.housing.tianjin.HousingTianJinHtmlRepository;
import com.microservice.dao.repository.crawler.housing.tianjin.HousingTianJinPayRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tianjin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tianjin")
public class HousingFundTianJinService extends HousingBasicService implements ICrawlerLogin {
	static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
	/** 天津公积金登录的URL */
	public static final String LoginPage = "https://cx.zfgjj.cn/dzyw-grwt/index.do";
	// 登录失败之后的界面地址
	public static final String LoginPageError = "https://cx.zfgjj.cn/dzyw-grwt/index.do#";
	private static final String OCR_FILE_PATH = "/tianjin/home/img";
	public static final Logger log = LoggerFactory.getLogger(HousingFundTianJinService.class);
	@Value("${spring.application.name}")
	String appName;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingTianJinBaseRepository housingTianJinBaseRepository;
	@Autowired
	private HousingTianJinHtmlRepository housingTianJinHtmlRepository;
	@Autowired
	private HousingTianJinPayRepository housingTianJinPayRepository;
	WebDriver driver;
	@Autowired
	public HousingBasicService housingBasicService;

	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

			System.setProperty("webdriver.ie.driver", driverPath);

			WebDriver driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			driver = new InternetExplorerDriver(ieCapabilities);

			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = "https://cx.zfgjj.cn/dzyw-grwt/index.do";
			driver.get(baseUrl);

			String windowHandle = driver.getWindowHandle();
			taskHousing.setWebdriverHandle(windowHandle);
			
			driver.findElement(By.id("sfzh")).sendKeys("120225198601185269");
			// 密码
			VK.Tab();
			driver.findElement(By.id("_ocx_password")).click();
			VK.KeyPress("123456");

			Actions action = new Actions(driver);
			
////			WebElement source = driver.findElement(By.xpath("//div[@id='nc_1__bg']"));
			
			WebElement source = driver.findElement(By.xpath("//div[@id='nc_1__bg']"));
			
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
			Thread.sleep(2000);
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
			Thread.sleep(2000);
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
			Thread.sleep(2000);
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
			Thread.sleep(2000);
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
			Thread.sleep(2000);
			
			// 释放
			action.moveToElement(source).release();
			// 组织完这些一系列的步骤，然后开始真实执行操作
			Action actions = action.build();
			actions.perform();

			driver.findElement(By.id("textLogin")).click();

			Thread.sleep(10000);
			
			String currentUrl = driver.getPageSource();
			String currentUrl2 = driver.getCurrentUrl();
			System.out.println(currentUrl);

			if (currentUrl.contains("我的公积金") && !LoginPageError.equals(currentUrl2)) {
				System.out.println("登录成功！");
				// 主界面地址 http://118.112.188.109/nethall//indexAction.do
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

				WebClient webClient = WebCrawler.getInstance().getWebClient();//

				for (org.openqa.selenium.Cookie cookie : cookies) {
					System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("cx.zfgjj.cn",
							cookie.getName(), cookie.getValue()));
				}

				String cookieJson = CommonUnit.transcookieToJson(webClient);

				taskHousing.setCookies(cookieJson);
				System.out.println("执行保存！");
				taskHousingRepository.save(taskHousing);

				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				System.out.println("登录成功" + path2);

			} else {
				System.out.println("登录失败！");
				Thread.sleep(2000);
				// 验证账号
				try {
					String text = driver.findElement(By.id("sfzhError")).getText();
					System.out.println("验证账户----" + text);
					if ("错误代码：Q6010004，你尚未注册成为电子公积金用户，请先注册。".equals(text)) {
						System.out.println("账号输入不正确！");

						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());

						taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
						save(taskHousing);

					}
				} catch (Exception e) {
					System.out.println("异常错误！");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getDescription());
					save(taskHousing);
				}
				// 验证密码
				try {
					String text = driver.findElement(By.id("cxmmError")).getText();
					System.out.println("验证密码----" + text);
					if ("错误代码：Q6010005，您录入的查询密码有误，请核对后重新录入。".equals(text)
							|| "错误代码：Q6010006，您输错查询密码次数已达2次，再次输错您的登录将受到限制。".equals(text)) {
						System.out.println("密码输入不正确！");

						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());

						taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
						save(taskHousing);

					}
				} catch (Exception e) {
					System.out.println("异常错误！");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getDescription());
					save(taskHousing);
				}
				// 验证验证码
				try {
					String text = driver.findElement(By.id("yzmError")).getText();
					System.out.println("验证验证码----" + text);
					if ("验证码有误，请重新输入".equals(text)) {
						System.out.println("图片验证码输入不正确！");

						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getPhase());
						taskHousing.setPhase_status(
								HousingfundStatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getPhasestatus());
						taskHousing
								.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IMAGE_VERIFICATION_ERROR.getDescription());

						taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
						save(taskHousing);

					}
				} catch (Exception e) {
					System.out.println("图片验证码输入正确！");
				}

				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				System.out.println("登录失败，且没有登录失败错误信息" + path2);

			}

			Thread.sleep(3000);

			// 将开启的驱动关闭 为了内存考虑
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getDescription());
			save(taskHousing);
		}
		return taskHousing;
	}

	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			String url = "https://cx.zfgjj.cn/dzyw-grwt/xxcx/zhxxcx/grzhxx.do";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
			webRequest.setAdditionalHeader("Referer", "https://cx.zfgjj.cn/dzyw-grwt/xxcx/zhxxcx/grzhxx.do");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Host", "cx.zfgjj.cn");
			webRequest.setAdditionalHeader("Connection", "Keep-Alive");
			webRequest.setAdditionalHeader("Cache-Control", "no-cache");
			HtmlPage html2 = webClient.getPage(webRequest);
			String asXml2 = html2.getWebResponse().getContentAsString();
			System.out.println(asXml2);

			HousingTianJinHtml housingTianJinHtml = new HousingTianJinHtml();
			housingTianJinHtml.setHtml(asXml2);
			housingTianJinHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingTianJinHtml.setType("基本信息");
			housingTianJinHtml.setUrl(url);
			housingTianJinHtmlRepository.save(housingTianJinHtml);

			Document doc = Jsoup.parse(asXml2);
			Elements spans = doc.getElementsByTag("span");
			// 姓名
			String name = spans.get(5).text();
			// 单位名称
			String company = spans.get(6).text();
			// 开户管理部
			String management = spans.get(7).text();
			// 个人代码
			String person_code = spans.get(8).text();
			// 单位代码
			String company_code = spans.get(9).text();
			// 开户网点代码
			String kaihuwangdian_code = spans.get(10).text();
			// 身份证号
			String card = spans.get(11).text();
			// 龙卡卡号
			String longka_card = spans.get(12).text();
			// 状态
			String status = spans.get(15).text();
			// 开户时间
			String open_date = spans.get(16).text();
			// 单位比例
			String company_proportion = spans.get(17).text();
			// 个人比例
			String person_proportion = spans.get(19).text();
			// 缴费基数
			String pay_cardinal = spans.get(21).text();

			HousingTianJinBase housingTianJinBase = new HousingTianJinBase();
			housingTianJinBase.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingTianJinBase.setPay_cardinal(pay_cardinal);
			housingTianJinBase.setPerson_proportion(person_proportion);
			housingTianJinBase.setCompany_proportion(company_proportion);
			housingTianJinBase.setOpen_date(open_date);
			housingTianJinBase.setStatus(status);
			housingTianJinBase.setLongka_card(longka_card);
			housingTianJinBase.setCard(card);
			housingTianJinBase.setKaihuwangdian_code(kaihuwangdian_code);
			housingTianJinBase.setCompany_code(company_code);
			housingTianJinBase.setPerson_code(person_code);
			housingTianJinBase.setManagement(management);
			housingTianJinBase.setCompany(company);
			housingTianJinBase.setName(name);
			housingTianJinBaseRepository.save(housingTianJinBase);

			taskHousing.setCity("天津市");
			taskHousing.setDescription("基本信息数据采集成功！");
			taskHousing.setLogintype(messageLoginForHousing.getLogintype());
			taskHousing.setPhase("CRAWLER");
			taskHousing.setPhase_status("SUCCESS");
			taskHousing.setError_code(0);
			taskHousing.setUserinfoStatus(200);
			taskHousingRepository.save(taskHousing);

		} catch (Exception e) {
			e.printStackTrace();
		}

		taskHousing = crawlerPay(messageLoginForHousing);

		// 更新最后的状态
		taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());

		return taskHousing;
	}

	public TaskHousing crawlerPay(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("service.crawlerPay.taskid", messageLoginForHousing.getTask_id().trim());
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			String url = "https://cx.zfgjj.cn/dzyw-grwt/xxcx/zhxxcx/grmxz.do?zjlx=G";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
			webRequest.setAdditionalHeader("Referer", "https://cx.zfgjj.cn/dzyw-grwt/xxcx/zhxxcx/grmxz.do?zjlx=G");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Host", "cx.zfgjj.cn");
			webRequest.setAdditionalHeader("Connection", "Keep-Alive");
			webRequest.setAdditionalHeader("Cache-Control", "no-cache");
			HtmlPage html = webClient.getPage(webRequest);
			String asXml = html.asXml();

			HousingTianJinHtml housingTianJinHtml = new HousingTianJinHtml();
			housingTianJinHtml.setHtml(asXml);
			housingTianJinHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingTianJinHtml.setType("缴费信息");
			housingTianJinHtml.setUrl(url);
			housingTianJinHtmlRepository.save(housingTianJinHtml);

			Document doc = Jsoup.parse(asXml);
			Element elementById = doc.getElementById("grmxzData");
			if (null != elementById) {
				System.out.println("缴费信息获取成功！");
				Elements tr = elementById.select("tr");
				for (int i = 1; i < tr.size(); i++) {
					Elements select = tr.get(i).select("td");
					for (int j = 0; j < select.size(); j += 5) {
						// 记账日期
						String date = select.get(j).html();
						// 业务摘要
						String memo = select.get(j + 1).html();
						// 发生金额
						String happen_money = select.get(j + 2).html();
						// 账户余额
						String yue = select.get(j + 3).html();
						// 汇缴月份
						String month = select.get(j + 4).html();
						System.out.println(date);
						System.out.println(memo);
						System.out.println(happen_money);
						System.out.println(yue);
						System.out.println(month);
						HousingTianJinPay housingTianJinPay = new HousingTianJinPay();
						housingTianJinPay.setTaskid(messageLoginForHousing.getTask_id().trim());
						housingTianJinPay.setDate(date);
						housingTianJinPay.setMemo(memo);
						housingTianJinPay.setHappen_money(happen_money);
						housingTianJinPay.setYue(yue);
						housingTianJinPay.setMonth(month);
						housingTianJinPayRepository.save(housingTianJinPay);
					}

				}

				taskHousing.setCity("天津市");
				taskHousing.setDescription("缴费信息数据采集成功！");
				taskHousing.setLogintype(messageLoginForHousing.getLogintype());
				taskHousing.setPhase("CRAWLER");
				taskHousing.setPhase_status("SUCCESS");
				taskHousing.setError_code(0);
				taskHousing.setPaymentStatus(200);
				taskHousingRepository.save(taskHousing);
			} else {
				System.out.println("缴费信息获取失败！");
				taskHousing.setCity("天津市");
				taskHousing.setDescription("缴费信息数据采集失败！");
				taskHousing.setLogintype(messageLoginForHousing.getLogintype());
				taskHousing.setPhase("CRAWLER");
				taskHousing.setPhase_status("FAIL");
				taskHousing.setError_code(2);
				taskHousing.setPaymentStatus(404);
				taskHousingRepository.save(taskHousing);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskHousing;
	}

	public void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(500L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

	public void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(500L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	// 自定义起始焦点和宽、高
	public String saveImg1(WebDriver driver, int pX, int pY, int eW, int eH) throws Exception {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(screenshot);
		BufferedImage eleScreenshot = fullImg.getSubimage(pX, pY, eW, eH);
		ImageIO.write(eleScreenshot, "jpg", screenshot);
		File file = getImageLocalPath1();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	public File getImageLocalPath1() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true);
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = 2 + ".png";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true);
		codeImageFile.setWritable(true);
		return codeImageFile;

	}

	// 根据dom节点截取图片
	public String saveImg(WebDriver driver, By selector) throws Exception {
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
		File file = getImageLocalPath2();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	public File getImageLocalPath2() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true);
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = 1 + ".png";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}

	public String getImgStr(String imgFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(data));
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}