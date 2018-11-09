package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangHtmlRepository;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.InsuranceWeiFangParser;
import app.service.aop.InsuranceLogin;

/**
 * 潍坊社保爬取Service
 * 
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.weifang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.weifang" })
public class InsuranceWeiFangService extends AbstractChaoJiYingHandler implements InsuranceLogin  {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceWeiFangHtmlRepository insuranceWeiFangHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private InsuranceWeiFangParser insuranceWeiFangParser;
	@Value("${datasource.driverPath}")
	public String driverPath;
	private WebDriver driver;
	public String sessionid;
	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		tracer.addTag("crawler.service.login.taskid",parameter.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			WebDriver driver = intiChrome();
			WebParam webParam =insuranceWeiFangParser.login(parameter, driver);
			driver = webParam.getDriver();		
			InsuranceWeiFangHtml html = new InsuranceWeiFangHtml();
			html.setUrl("https://www.sdwfhrss.gov.cn/hsp/logonDialog.jsp");
			html.setType("login");
			html.setPageCount(1);
			html.setTaskid(taskInsurance.getTaskid());
			html.setHtml(webParam.getHtml());
			insuranceWeiFangHtmlRepository.save(html);
			if(1== webParam.getCode()){
				insuranceService.changeLoginStatus("LOGIN", "SUCCESS", "登录成功！", taskInsurance);		
				tracer.addTag("crawler.service.login.success", "登陆成功");
				String cookies = webParam.getCookies();
				taskInsurance = changeLoginStatusSuccess(taskInsurance, cookies);	
				sessionid=webParam.getSessionid();
				System.out.println("登陆成功");
			}else{
				taskInsurance=insuranceService.changeLoginStatusPwdError(taskInsurance);		
				tracer.addTag("crawler.service.login.fail", "登陆失败："+webParam.getHtml());
				driver.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			insuranceService.changeLoginStatus("LOGIN", "ERROR", "连接超时！", taskInsurance);
			tracer.addTag("crawler.service.login.Exception", e.toString());
			e.printStackTrace();
			driver.close();
		}
		
		return taskInsurance;
			
	}
	public  WebDriver intiChrome() throws Exception {
	
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--window-size=1080,868");
		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
	
		return driver;
	}		
	  private TaskInsurance changeLoginStatusSuccess(TaskInsurance taskInsurance, String cookie) {
	        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
	        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
	        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

	        taskInsurance.setCookies(cookie);

	        taskInsurance = taskInsuranceRepository.save(taskInsurance);
	        return taskInsurance;
	    }
	/**
	 * 爬取指定账号的济南社保信息
	 * 
	 * @param parameter
	 * @return
	 */
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("InsuranceWeiFangService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行业务
		System.out.println("sessionid==="+sessionid);
		try {
			// 爬取解析基本信息
			crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance,sessionid);
			// 爬取解析养老保险
			for (int k = 0; k < 3; k++) {
				// 获取当前的年
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.YEAR, -k);
				String beforeMonth = df.format(c.getTime());
				System.out.println(beforeMonth);
				crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance, beforeMonth,sessionid);			
			}
			// 爬取解析医疗保险
				for (int k = 0; k < 3; k++) {
				// 获取当前的年
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.YEAR, -k);
				String beforeMonth = df.format(c.getTime());
				System.out.println(beforeMonth);
				crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance, beforeMonth,sessionid);
			}
		
			// 爬取解析失业保险()
			for (int k = 0; k < 3; k++) {
				// 获取当前的年
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.YEAR, -k);
				String beforeMonth = df.format(c.getTime());
				System.out.println(beforeMonth);
				crawlerBaseInfoService.crawlerUnemploymentInsurance(parameter, taskInsurance, beforeMonth,sessionid);
			}
		
			// 爬取解析生育保险()
			for (int k = 0; k < 3; k++) {
				// 获取当前的年
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.YEAR, -k);
				String beforeMonth = df.format(c.getTime());
				System.out.println(beforeMonth);
				crawlerBaseInfoService.crawlerShengyuInsurance(parameter, taskInsurance, beforeMonth,sessionid);
			}
			
			// 爬取解析工伤保险()
		
			for (int k = 0; k < 3; k++) {
				// 获取当前的年
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.YEAR, -k);
				String beforeMonth = df.format(c.getTime());
				System.out.println(beforeMonth);
				crawlerBaseInfoService.crawlerGongshangInsurance(parameter, taskInsurance, beforeMonth,sessionid);
			}
			
			// 更新最终的状态
			taskInsurance = insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			System.out.println("数据采集完成之后的-----" + taskInsurance.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		return taskInsurance;
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

	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public static String getImagePath(Page page) throws Exception {
		File imageFile = getImageCustomPath();
		String imgagePath = imageFile.getAbsolutePath();
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}

	// 创建验证码图片保存路径
	public static File getImageCustomPath() {
		String path = "";
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			path = System.getProperty("user.dir") + "/verifyCodeImage/";
		} else {
			path = System.getProperty("user.home") + "/verifyCodeImage/";
		}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
