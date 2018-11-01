package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouImageInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouImageInfoRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * 德州社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.dezhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.dezhou" })
public class InsuranceDezhouService implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private InsuranceDeZhouImageInfoRepository insuranceDezhouImageInfoRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		List<InsuranceDeZhouImageInfo> insuranceYantaiImageInfo = insuranceDezhouImageInfoRepository.findByCreatetime();
		String appservion = "";
		appservion = insuranceYantaiImageInfo.get(0).getAppservion().trim();

		String password = parameter.getPassword();
		String md5 = md5(password);

		// 拿到登录界面cookies得到一个webclient对象
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		String cook = findByTaskid.getCookies();
		Set<Cookie> transferJsonToSet = CommonUnit.transferJsonToSet(cook);
		WebClient webClient = insuranceService.getWebClient(transferJsonToSet);
		try {
			// 获取图片验证码
			String loginurl21 = "http://222.133.0.220:9988/hso/genAuthCode2?_=0.74443901336146";
			WebRequest webRequest1 = new WebRequest(new URL(loginurl21), HttpMethod.GET);
			Page page001 = webClient.getPage(webRequest1);
			String imagePath1 = getImagePath(page001);
			String code1 = chaoJiYingOcrService.callChaoJiYingService(imagePath1, "6001");
			System.out.println("识别出来的图片验证码是---------" + code1);

			WebRequest requestSettings = new WebRequest(new URL("http://222.133.0.220:9988/hso/logon.do"),
					HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "doLogonAllowRepeat"));
			requestSettings.getRequestParameters().add(new NameValuePair("usertype", "1"));
			requestSettings.getRequestParameters().add(new NameValuePair("username", parameter.getUsername().trim()));
			requestSettings.getRequestParameters().add(new NameValuePair("password", md5));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("validatecode", code1));
			requestSettings.getRequestParameters().add(new NameValuePair("appversion", appservion));
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			// 结果五:系统版本与数据库版本两者不一致！
			System.out.println("登陆结果-------" + contentAsString);
			if (contentAsString.contains("没有找到对应的参保人信息。")) {
				System.out.println("登陆失败！没有找到对应的参保人信息。");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else if (contentAsString.contains("您输入的密码不正确！")) {
				System.out.println("登陆失败！您输入的密码不正确！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else if (contentAsString.contains("输入的验证码不正确，请检查")) {
				System.out.println("登陆失败！输入的验证码不正确，请检查");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				if (contentAsString.contains("true")) {
					System.out.println("登陆成功！");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

					String cookies = CommonUnit.transcookieToJson(loginedPage.getWebClient());
					taskInsurance.setCookies(cookies);
					saveCookie(parameter, cookies);
				} else {
					System.out.println("异常错误!请从新登陆！");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}

	/**
	 * 爬取指定账号的德州社保信息
	 * 
	 * @param parameter
	 * @return
	 */
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("InsuranceBinZhouService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 爬取解析基本信息
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance);

		// 爬取解析养老保险
		for (int k = 0; k < 20; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析医疗保险
		for (int k = 0; k < 20; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析失业保险()
		for (int k = 0; k < 20; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerUnemploymentInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析生育保险()
		for (int k = 0; k < 20; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerShengyuInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析工伤保险()
		for (int k = 0; k < 20; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerGongshangInsurance(parameter, taskInsurance, beforeMonth);
		}

		// 更新Task表为所有数据爬取成功
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);

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
