package app.service;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.suzhou2.HousingsuzhouBase;
import com.microservice.dao.entity.crawler.housing.suzhou2.HousingsuzhouHtml;
import com.microservice.dao.entity.crawler.housing.suzhou2.HousingsuzhouPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.suzhou2.HousingsuzhouBaseRepository;
import com.microservice.dao.repository.crawler.housing.suzhou2.HousingsuzhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.suzhou2.HousingsuzhouPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.suzhou2")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.suzhou2")
public class HousingFundsuzhouService extends AbstractChaoJiYingHandler implements ICrawlerLogin{
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundsuzhouService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingsuzhouHtmlRepository housingsuzhouHtmlRepository;
	@Autowired
	public HousingsuzhouBaseRepository housingsuzhouBaseRepository;
	@Autowired
	public HousingsuzhouPayRepository housingsuzhouPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	public static String driverPath = "D:\\IEDriverServer_Win32\\chromedriver.exe";
	@Autowired
	public HousingBasicService housingBasicService;
	// 登录业务层
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			System.setProperty("webdriver.chrome.driver", driverPath);
			WebDriver driver = new ChromeDriver();
			ChromeOptions chromeOptions = new ChromeOptions();
			driver = new ChromeDriver(chromeOptions);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			String baseUrl = "http://60.171.196.71:8004/";
			driver.get(baseUrl);
			// 用户名
			driver.findElement(By.id("username")).sendKeys("342201198505160231");
			// 密码
			Actions action = new Actions(driver);
			WebElement psddly = driver.findElement(By.xpath("//*[@id=\"password\"]"));
			action.moveToElement(psddly).click().sendKeys("521000mm").perform();

			WebElement source = driver.findElement(By.xpath("//*[@id=\"drag\"]/div[3]"));
			action.moveToElement(source).clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 300, 0);
			Thread.sleep(2000);
			// 释放
			action.moveToElement(source).release();
			// 组织完这些一系列的步骤，然后开始真实执行操作
			Action actions = action.build();
			actions.perform();
			// 登录按钮
			WebElement button = driver
					.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[2]/div[2]/ul/li/ul/li[6]/button"));
			action.moveToElement(button).click().perform();
			Thread.sleep(5000);
			String html = driver.getPageSource();
			System.out.println(html);
			if (html.contains("上次登录时间")) {
				System.out.println("登录成功！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());

				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

				WebClient webClient = WebCrawler.getInstance().getWebClient();//

				for (org.openqa.selenium.Cookie cookie : cookies) {
					System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
							"60.171.196.71", cookie.getName(), cookie.getValue()));
				}

				String cookies2 = CommonUnit.transcookieToJson(webClient);
				taskHousing.setCookies(cookies2);
				taskHousingRepository.save(taskHousing);

			} else {
				System.out.println("登录失败！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
				taskHousingRepository.save(taskHousing);
			}
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	// 爬取数据的业务层
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		int pagesize = 0;

		// 基本信息
		String jbxx = "http://60.171.196.71:8004/Index/Mdgjj";
		try {
			WebRequest requestSettings = new WebRequest(new URL(jbxx), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			System.out.println("基本信息----" + contentAsString);
			HousingsuzhouHtml housingsuzhouHtml = new HousingsuzhouHtml();
			housingsuzhouHtml.setHtml(contentAsString + "");
			housingsuzhouHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingsuzhouHtml.setType("基本信息");
			housingsuzhouHtml.setUrl(jbxx);
			housingsuzhouHtmlRepository.save(housingsuzhouHtml);

			HousingsuzhouHtml housingsuzhouHtml1 = new HousingsuzhouHtml();
			housingsuzhouHtml1.setHtml(contentAsString);
			housingsuzhouHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingsuzhouHtml1.setType("流水信息");
			housingsuzhouHtml1.setUrl(jbxx);
			housingsuzhouHtmlRepository.save(housingsuzhouHtml1);

			Document doc = Jsoup.parse(contentAsString);
			// 获得明细的数据个数
			Elements wdgjj_tongji = doc.getElementsByClass("wdgjj_tongji");
			String text = wdgjj_tongji.text();
			String[] split = text.split("共");
			String[] split2 = split[1].split("页");
			String trim = split2[0].trim();
			pagesize = Integer.parseInt(trim);
			System.out.println("共" + trim + "页");

			if (contentAsString.contains("class=\"zym_top-left-wenzi-bot\"")) {
				System.out.println("基本信息获取成功！");
				Elements elementsByClass = doc.getElementsByClass("zym_top-left-wenzi-bot-js");
				// 单位名称
				String dwmc = elementsByClass.get(0).getElementsByTag("span").text();
				System.out.println("单位名称:" + dwmc);
				// 个人姓名
				String grxm = elementsByClass.get(1).getElementsByTag("span").text();
				System.out.println("个人姓名:" + grxm);
				// 身份证号码
				String sfzhm = elementsByClass.get(2).getElementsByTag("span").text();
				System.out.println("身份证号码:" + sfzhm);
				// 缴存基数
				String jcjs = elementsByClass.get(3).getElementsByTag("span").text();
				System.out.println("缴存基数:" + jcjs);
				// 月缴存额
				String yjce = elementsByClass.get(4).getElementsByTag("span").text();
				System.out.println(" 月缴存额:" + yjce);
				// 公积金余额
				String gjjye = elementsByClass.get(5).getElementsByTag("span").text();
				System.out.println("公积金余额:" + gjjye);
				// 单位账号
				String dwzh = elementsByClass.get(6).getElementsByTag("span").text();
				System.out.println("单位账号:" + dwzh);
				// 个人账号
				String grzh = elementsByClass.get(7).getElementsByTag("span").text();
				System.out.println("个人账号:" + grzh);
				HousingsuzhouBase housingsuzhouBase = new HousingsuzhouBase();
				housingsuzhouBase.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingsuzhouBase.setGrzh(grzh);
				housingsuzhouBase.setDwzh(dwzh);
				housingsuzhouBase.setGjjye(gjjye);
				housingsuzhouBase.setYjce(yjce);
				housingsuzhouBase.setJcjs(jcjs);
				housingsuzhouBase.setSfzhm(sfzhm);
				housingsuzhouBase.setGrxm(grxm);
				housingsuzhouBase.setDwmc(dwmc);
				housingsuzhouBaseRepository.save(housingsuzhouBase);
				taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());
				// 部分流水信息
				Elements tables = doc.getElementsByTag("table");
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				for (int i = 0; i < trs.size(); i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					for (int j = 0; j < tds.size(); j += 5) {
						// 日期
						String rq = tds.get(j).text().trim();
						System.out.println("日期：" + rq);
						// 摘要
						String zy = tds.get(j + 1).text().trim();
						System.out.println("摘要：" + zy);
						// 发生金额
						String fsje = tds.get(j + 2).text().trim();
						System.out.println("发生金额：" + fsje);
						// 借贷标志
						String jdbz = tds.get(j + 3).text().trim();
						System.out.println("借贷标志：" + jdbz);
						// 余额
						String ye = tds.get(j + 4).text().trim();
						System.out.println("余额：" + ye);
						System.out.println("=========================");
						HousingsuzhouPay housingsuzhouPay = new HousingsuzhouPay();
						housingsuzhouPay.setTaskid(messageLoginForHousing.getTask_id().trim());
						housingsuzhouPay.setRq(rq);
						housingsuzhouPay.setZy(zy);
						housingsuzhouPay.setFsje(fsje);
						housingsuzhouPay.setJdbz(jdbz);
						housingsuzhouPay.setYe(ye);
						housingsuzhouPayRepository.save(housingsuzhouPay);
					}
				}
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());

			} else {
				System.out.println("基本信息获取失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		////////////////////////////////////// 流水信息////////////////////////////////////////

		try {
			for (int i = 2; i <= pagesize; i++) {
				// 流水请求
				String jcmx = "http://60.171.196.71:8004/Index/Mdgjj?page=" + i;
				WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.GET);
				Page page1 = webClient.getPage(requestSettings1);
				String contentAsString = page1.getWebResponse().getContentAsString();

				HousingsuzhouHtml housingsuzhouHtml1 = new HousingsuzhouHtml();
				housingsuzhouHtml1.setHtml(contentAsString);
				housingsuzhouHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingsuzhouHtml1.setType("流水信息");
				housingsuzhouHtml1.setUrl(jcmx);
				housingsuzhouHtmlRepository.save(housingsuzhouHtml1);

				Document doc = Jsoup.parse(contentAsString);

				Elements tables = doc.getElementsByTag("table");
				Element table = tables.get(0);
				Elements trs = table.getElementsByTag("tr");
				for (int k = 0; k < trs.size(); k++) {
					Elements tds = trs.get(k).getElementsByTag("td");
					for (int j = 0; j < tds.size(); j += 5) {
						// 日期
						String rq = tds.get(j).text().trim();
						System.out.println("日期：" + rq);
						// 摘要
						String zy = tds.get(j + 1).text().trim();
						System.out.println("摘要：" + zy);
						// 发生金额
						String fsje = tds.get(j + 2).text().trim();
						System.out.println("发生金额：" + fsje);
						// 借贷标志
						String jdbz = tds.get(j + 3).text().trim();
						System.out.println("借贷标志：" + jdbz);
						// 余额
						String ye = tds.get(j + 4).text().trim();
						System.out.println("余额：" + ye);
						System.out.println("=========================");
						HousingsuzhouPay housingsuzhouPay = new HousingsuzhouPay();
						housingsuzhouPay.setTaskid(messageLoginForHousing.getTask_id().trim());
						housingsuzhouPay.setRq(rq);
						housingsuzhouPay.setZy(zy);
						housingsuzhouPay.setFsje(fsje);
						housingsuzhouPay.setJdbz(jdbz);
						housingsuzhouPay.setYe(ye);
						housingsuzhouPayRepository.save(housingsuzhouPay);
					}
				}
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());

			}
			taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());


		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}