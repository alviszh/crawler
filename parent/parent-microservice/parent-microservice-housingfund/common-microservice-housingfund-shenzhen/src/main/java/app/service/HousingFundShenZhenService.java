package app.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shenzhen.HousingShenZhenBase;
import com.microservice.dao.entity.crawler.housing.shenzhen.HousingShenZhenHtml;
import com.microservice.dao.repository.crawler.housing.shenzhen.HousingShenZhenBaseRepository;
import com.microservice.dao.repository.crawler.housing.shenzhen.HousingShenZhenHtmlRepository;
import com.module.jna.webdriver.WebDriverUnit;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shenzhen")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shenzhen")
public class HousingFundShenZhenService extends HousingBasicService implements ICrawler {
	@Value("${datasource.driverPath}")
	public String driverPath;

	@Value("${datasource.driverPath.yzm}")
	public String OCR_FILE_PATH;

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

	@Autowired
	private HousingShenZhenHtmlRepository housingShenZhenHtmlRepository;
	@Autowired
	public HousingBasicService housingBasicService;
	@Autowired
	private HousingShenZhenBaseRepository housingShenZhenBaseRepository;

	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {

			System.setProperty("webdriver.chrome.driver", driverPath);

			ChromeOptions chromeOptions = new ChromeOptions();
			ChromeDriver driver = new ChromeDriver(chromeOptions);

			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = "http://app.szzfgjj.com:7001/pages/wy_yecx.jsp";
			driver.get(baseUrl);
			driver.get(baseUrl);

			Thread.sleep(1000);

			// 个人公积金账户
			driver.findElement(By.id("accnum2")).sendKeys(messageLoginForHousing.getHosingFundNumber().trim());
			Thread.sleep(1000);
			// 身份证号
			driver.findElement(By.id("certinum2")).sendKeys(messageLoginForHousing.getNum().trim());
			Thread.sleep(1000);
			// 验证码
			String path = saveImg(driver, By.id("imgyzm"));

			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("4004", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 505
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("verify")).sendKeys(code);
			Thread.sleep(1000);
			// 模拟登录
			driver.findElement(By.id("but_2")).click();

			Thread.sleep(3000);

			try {

				Alert alt = driver.switchTo().alert();
				String text = alt.getText();
				System.out.println(text);
				if (text.contains("验证码错误")) {
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getError_code());
					save(taskHousing);
				}
				if (text.contains("无此记录")) {
					taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getError_code());
					save(taskHousing);
				}
				if (text.contains("身份证输入不合法")) {

					taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getError_code());

					save(taskHousing);
				}
			} catch (Exception e) {
				System.out.println("网页中不存在alert元素");
			}

			WebElement newaccnum = driver.findElement(By.name("newaccnum"));
			// 个人公积金账户
			String person_number = newaccnum.getAttribute("value");
			System.out.println("个人公积金账户==============" + person_number);
			WebElement peraccstate = driver.findElement(By.name("peraccstate"));
			// 状态
			String status = peraccstate.getAttribute("value");
			System.out.println("状态==============" + status);

			WebElement balance = driver.findElement(By.name("balance"));
			// 账户余额
			String yue = balance.getAttribute("value");
			System.out.println("账户余额==============" + yue);

			WebElement sbbalance = driver.findElement(By.name("sbbalance"));
			// 社保移交金额
			String move_money = sbbalance.getAttribute("value");
			System.out.println("社保移交金额==============" + move_money);
			if (!"".equals(person_number) && !"".equals(status) && !"".equals(yue) && !"".equals(move_money)) {
				System.out.println("数据解析成功！");
				HousingShenZhenHtml housingShenZhenHtml = new HousingShenZhenHtml();
				housingShenZhenHtml.setHtml(baseUrl);
				housingShenZhenHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingShenZhenHtml.setType("基本信息");
				housingShenZhenHtml.setUrl(baseUrl);
				housingShenZhenHtmlRepository.save(housingShenZhenHtml);

				HousingShenZhenBase housingShenZhenBase = new HousingShenZhenBase();
				housingShenZhenBase.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingShenZhenBase.setPerson_number(person_number);
				housingShenZhenBase.setStatus(status);
				housingShenZhenBase.setYue(yue);
				housingShenZhenBase.setMove_money(move_money);
				housingShenZhenBaseRepository.save(housingShenZhenBase);
				taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());
				taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());
			} else {
				taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
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
		File file = getImageLocalPath();
		FileUtils.copyFile(screenshot, file);
		return file.getAbsolutePath();
	}

	public File getImageLocalPath() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}