package app.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tieling.HousingTieLingPay;
import com.microservice.dao.entity.crawler.housing.tieling.HousingTielingUserInfo;
import com.microservice.dao.repository.crawler.housing.tieling.HousingTieLingPayRepository;
import com.microservice.dao.repository.crawler.housing.tieling.HousingTieLingUserInfoRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.crawler.htmlparse.HousingTLParse;
import app.service.common.HousingBasicService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tieling")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tieling")
public class LoginAndGetService extends HousingBasicService{
	@Value("${datasource.driverPath}")
	public String driverPath;
	@Value("${imagePath}")
	public String OCR_FILE_PATH;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	@Autowired
	private HousingTieLingUserInfoRepository housingTieLingUserInfoRepository;
	@Autowired
	private HousingTieLingPayRepository housingTieLingPayRepository;
	private WebDriver driver;
	// 联名卡号
	public  HtmlPage loginByCO_BRANDED_CARD(WebClient webClient,String url, MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) throws Exception {
		try {
			WebDriver driver = intiChrome();
			driver.get(url);
			driver.findElement(By.id("CI_Act")).click();
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("#CI_Act > option:nth-child(1)")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("CertiNum")).sendKeys(messageLoginForHousing.getNum().trim());
			Thread.sleep(1000);
			driver.findElement(By.id("CI_UserNum")).sendKeys(messageLoginForHousing.getPassword().trim());
			Thread.sleep(1000);
			String path1 = WebDriverUnit.saveImg(driver, By.id("image"));
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1004", LEN_MIN, TIME_ADD, STR_DEBUG, path1); // 1005
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//			String code = getVerfiycode(By.id("image"), driver);
			driver.findElement(By.id("captcha")).sendKeys(code);
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");
			Thread.sleep(3000);
			driver.findElement(By.id("btn_submit")).click();
			Thread.sleep(1000 * 5);
			
			 try{  
				 Alert alt = driver.switchTo().alert();
			     alt.accept();  
		           
		     }catch (NoAlertPresentException Ex)  {  
		            
		     }     
		     
			String htmlpage = driver.getPageSource();
			//System.out.println(htmlpage);
			if(htmlpage.contains("欢迎您")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
			
				driver.findElement(By.cssSelector("div.listMenu > ul > li:nth-child(2) > a")).click();
				Thread.sleep(2000);
				String htmlsource2 = driver.getPageSource();
				//System.out.println(htmlsource2);
				login(htmlsource2, messageLoginForHousing, taskHousing);
				driver.findElement(By.cssSelector("div.listMenu > ul > li:nth-child(3) > a")).click();
				Thread.sleep(2000);
				String htmlsource3 = driver.getPageSource();
				//System.out.println(htmlsource3);
				login(htmlsource3, messageLoginForHousing, taskHousing);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
				save(taskHousing);
				driver.quit();
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
				save(taskHousing);
				driver.quit();
				//return null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getDescription());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getDescription());
			save(taskHousing);
			driver.quit();
		}
		return null;
	}	
		
	// 根据个人登记号登录,用户名
	public  HtmlPage loginByACCOUNT_NUM(WebClient webClient,String url,MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing){
		try {
			WebDriver driver = intiChrome();
			driver.get(url);
			driver.findElement(By.id("CI_Act")).click();
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("#CI_Act > option:nth-child(2)")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("CertiNum")).sendKeys(messageLoginForHousing.getNum().trim());
			Thread.sleep(1000);
			driver.findElement(By.id("CI_UserNum")).sendKeys(messageLoginForHousing.getPassword().trim());
			Thread.sleep(1000);
			String code = getVerfiycode(By.id("image"), driver);
			driver.findElement(By.id("captcha")).sendKeys(code);
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");
			Thread.sleep(3000);
			driver.findElement(By.id("btn_submit")).click();
			Thread.sleep(1000 * 5);
			
			 try{  
				 Alert alt = driver.switchTo().alert();
			     alt.accept();  
		           
		     }catch (NoAlertPresentException Ex)  {  
		            
		     }     
//			 Alert alt = driver.switchTo().alert();
//		     alt.accept();
		     
			String htmlpage = driver.getPageSource();
			//String htmlpage = "";
			//System.out.println("htmlpage:"+htmlpage);
			if(htmlpage.contains("欢迎您")){
				System.out.println("222222");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
				
				driver.findElement(By.cssSelector("div.listMenu > ul > li:nth-child(2) > a")).click();
				Thread.sleep(2000);
				String htmlsource2 = driver.getPageSource();
				//System.out.println(htmlsource2);
				login(htmlsource2, messageLoginForHousing, taskHousing);
				driver.findElement(By.cssSelector("div.listMenu > ul > li:nth-child(3) > a")).click();
				Thread.sleep(2000);
				String htmlsource3 = driver.getPageSource();
				//System.out.println(htmlsource3);
				login(htmlsource3, messageLoginForHousing, taskHousing);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
				save(taskHousing);
				driver.quit();
			}else{
				
				System.out.println("111111111");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
				save(taskHousing);
				driver.quit();
				//return null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getDescription());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getDescription());
			save(taskHousing);
			driver.quit();
		}
		
		return null;
	}	
	
	// 根据身份证号登录
	public  HtmlPage loginByIDNUM(WebClient webClient,String url, MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing){
		try {
			WebDriver driver = intiChrome();
			driver.get(url);
			//		driver.findElement(By.id("CI_Act")).click();
			//		Thread.sleep(1000);
			//		driver.findElement(By.cssSelector("#CI_Act > option:nth-child(1)")).click();
			//		Thread.sleep(1000);
			driver.findElement(By.id("CertiNum")).sendKeys(messageLoginForHousing.getNum().trim());
			Thread.sleep(1000);
			driver.findElement(By.id("CI_UserNum")).sendKeys(messageLoginForHousing.getPassword().trim());
			Thread.sleep(1000);
			String code = getVerfiycode(By.id("image"), driver);
			driver.findElement(By.id("captcha")).sendKeys(code);
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");
			Thread.sleep(3000);
			driver.findElement(By.id("btn_submit")).click();
			Thread.sleep(1000 * 5);
			
			 try{  
				 Alert alt = driver.switchTo().alert();
			     alt.accept();  
		           
		     }catch (NoAlertPresentException Ex)  {  
		            
		     }     
		     
			String htmlpage = driver.getPageSource();
			if(htmlpage.contains("欢迎您")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
				
				driver.findElement(By.cssSelector("div.listMenu > ul > li:nth-child(2) > a")).click();
				Thread.sleep(2000);
				String htmlsource2 = driver.getPageSource();
				//System.out.println(htmlsource2);
				login(htmlsource2, messageLoginForHousing, taskHousing);
				driver.findElement(By.cssSelector("div.listMenu > ul > li:nth-child(3) > a")).click();
				Thread.sleep(2000);
				String htmlsource3 = driver.getPageSource();
				//System.out.println(htmlsource3);
				login(htmlsource3, messageLoginForHousing, taskHousing);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
				save(taskHousing);
				driver.quit();
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
				save(taskHousing);
				driver.quit();
				//return null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getDescription());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getDescription());
			save(taskHousing);
			driver.quit();
		}
		return null;
		
	}	
	
	
	
	public  WebDriver intiChrome() throws Exception {
//		String driverPath = "/opt/selenium/chromedriver-2.31";
		System.setProperty("webdriver.chrome.driver", driverPath);
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu"); 

		driver = new ChromeDriver(chromeOptions);
		 
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		return driver;
	}
	
	public String getVerfiycode(By by, WebDriver driver) throws Exception {
		String path =WebDriverUnit.saveImg(driver, by);
		System.out.println("path---------------" + path);
		String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1004", LEN_MIN, TIME_ADD, STR_DEBUG, path);
		System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		return code;
	}
	
	public  String saveImg(WebDriver driver, By selector) throws Exception{
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(screenshot);
		// Get the location of element on the page 
		WebElement ele = driver.findElement(selector); 
		Point point = ele.getLocation(); 
		// Get width and height of the element
		int eleWidth = ele.getSize().getWidth();
		int eleHeight = ele.getSize().getHeight(); 
		// Crop the entire page screenshot to get only element screenshot
		System.out.println("point.getX()-------"+point.getX());
		System.out.println("point.getY()-------"+point.getY());
		System.out.println("eleWidth-------"+eleWidth);
		System.out.println("eleHeight-------"+eleHeight);
		BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot); 
		File file = getImageCustomPath();		
		FileUtils.copyFile(screenshot, file); 
		return file.getAbsolutePath();
	}
	
	public  File getImageCustomPath() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;

	}
	
	public String login(String htmlpage,MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing){
		
			
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
			save(taskHousing);
			if (htmlpage.contains("个人公积金账号")){
				HousingTielingUserInfo userinfo = HousingTLParse.userinfo_parse(htmlpage);
				userinfo.setTaskid(taskHousing.getTaskid());
				housingTieLingUserInfoRepository.save(userinfo);
				taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
				save(taskHousing);
				updateTaskHousing(taskHousing.getTaskid());
			}
			if(htmlpage.contains("开始时间")){
				
				List<String> urlPayDetailslist = null;;
				try {
					urlPayDetailslist = getURLforPayDetails(htmlpage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				for (String url_result : urlPayDetailslist) {
					Page page= null;			
					//System.out.println(">>>>>>>>>>>"+url_result);
					List<HousingTieLingPay> listresul = HousingTLParse.recodetails_parse(url_result);
					for(HousingTieLingPay result : listresul){
						result.setTaskid(taskHousing.getTaskid());
						housingTieLingPayRepository.save(result);
					}
					taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
					taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
					save(taskHousing);
					updateTaskHousing(taskHousing.getTaskid());
				}
			}
			return null;
		
		
	}
	public  List<String> getURLforPayDetails(String html) throws Exception {
		List<String> urllist = new ArrayList<>();
		Document doc = Jsoup.parse(html);
		Elements acc = doc.select("li.tit");
		String accs = null;
		if(acc.size()>0){
			accs = acc.get(1).text().trim();
			accs = accs.substring(accs.lastIndexOf("：")+1);
			String accNum = accs.substring(0, accs.lastIndexOf("退"));
			Calendar now = Calendar.getInstance();
			int endNian = now.get(Calendar.YEAR);
			int beginNian = endNian-2;
			int yue = now.get(Calendar.MONTH) +1;
			int ri = now.get(Calendar.DAY_OF_MONTH);
			String yu = null;
			String r = null;
			if (yue>9){
				yu = String.valueOf(yue);
			}else{
				yu = "0"+yue;
			}
			if (ri>9){
				r = String.valueOf(ri);
			}else{
				r = "0"+ri;
			}
			String beginDate =beginNian+"-"+ yu+"-"+r;
			String endDate =endNian+"-"+ yu+"-"+r;
			driver.findElement(By.id("beginDate")).sendKeys(beginDate);
			Thread.sleep(1000);
			driver.findElement(By.id("endDate")).sendKeys(endDate);
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("div.bsdtFormItem > div > button")).click();
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");   
			Thread.sleep(10000);
			String htmla = driver.getPageSource();
			//System.out.println(htmla);
			Document doc1 = Jsoup.parse(htmla);
			Elements pages = doc1.select("#CO_TotalPage");
			String page = pages.text().trim();
			System.out.println(page);
			int pa = Integer.parseInt(page);
			for (int i = 1;i<=pa;i++) {
//				String url = "http://www.tlgjj.com.cn/sendServlet.do?task=ftp&TranCode=111141"
//						+ "&BeginDate="+beginDate+"&EndDate="+endDate+"&CI_Pagenum="+i+"&CI_Count=8&AccNum="+accNum+"";
//				System.out.println("====" + url);
				if (i==1){
					urllist.add(htmla);
					((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000)");
				}else{
					driver.findElement(By.id("next_page")).click();
					((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");   
					Thread.sleep(10000);
					((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000)");
					String htmlb = driver.getPageSource();
					urllist.add(htmlb);	
				}
				
			}
		}
		return urllist;
	}

}
