package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.ziyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.ziyang" })
public class InsuranceziyangService implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceService insuranceService;
	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			// getRSAKey
			String loginurl2 = "http://online.zysrsj.gov.cn/getRSAKey.jspx";
			WebRequest webRequest = new WebRequest(new URL(loginurl2), HttpMethod.POST);
			Page page00 = webClient.getPage(webRequest);
			String page000 = page00.getWebResponse().getContentAsString();

			JSONObject json = JSONObject.fromObject(page000);
			String success = json.getString("success").trim();

			String publicKeyModulus = "";
			String publicKeyExponent = "";

			if ("true".equals(success)) {
				System.out.println("获取加密参数成功！");
				String fieldData = json.getString("fieldData").trim();
				JSONObject jsonfieldData = JSONObject.fromObject(fieldData);
				// 加密需要的数据
				publicKeyModulus = jsonfieldData.getString("publicKeyModulus").trim();
				System.out.println("publicKeyModulus-----------------" + publicKeyModulus);
				publicKeyExponent = jsonfieldData.getString("publicKeyExponent").trim();
				System.out.println("publicKeyExponent-----------------" + publicKeyExponent);

				// 获取图片验证码
				String loginurl21 = "http://online.zysrsj.gov.cn/CaptchaImg";
				WebRequest webRequest1 = new WebRequest(new URL(loginurl21), HttpMethod.GET);
				Page page001 = webClient.getPage(webRequest1);
				String imagePath1 = getImagePath(page001);
				String code1 = chaoJiYingOcrService.callChaoJiYingService(imagePath1, "3004");
				System.out.println("识别出来的图片验证码是---------" + code1);

				String username = parameter.getUsername().trim();
				String password = parameter.getPassword().trim();

				String reverseusername = reverse(username);
				String reversepassword = reverse(password);

				String encrypted1 = encrypted(reverseusername, publicKeyExponent, publicKeyModulus);
				String encrypted2 = encrypted(reversepassword, publicKeyExponent, publicKeyModulus);
				String encrypted3 = encrypted("m4wss3672emhnc49", publicKeyExponent, publicKeyModulus);
				System.out.println("加密经过反转的用户名，结果是：" + encrypted1);
				System.out.println("加密经过反转的密码，结果是：" + encrypted2);
				System.out.println("加密经过反转的登录页面隐藏域中的netysku，结果是：" + encrypted3);

				WebRequest requestSettings = new WebRequest(new URL("http://online.zysrsj.gov.cn/member/login.jspx"),
						HttpMethod.POST);
				String body = "username="+encrypted1+"&password="+encrypted2+"&netysku="+encrypted3+"&loginid_type=idcard&aac002="+username+"&phonecode="+code1+"&aae005=&usertype=aac001&checkCode="+code1+"";
				requestSettings.setRequestBody(body);
				Page loginedPage = webClient.getPage(requestSettings);
				String contentAsString = loginedPage.getWebResponse().getContentAsString();

				System.out.println("验证的登录结果页面是：" + contentAsString);

				if (contentAsString.contains("用户名或者密码不正确")) {
					System.out.println("登陆失败！用户名或者密码不正确");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				} else if (contentAsString.contains("验证码错误！")) {
					System.out.println("登陆失败！验证码错误！");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				} else {
					try {
						if (contentAsString.contains("<title>资阳市社会保障网上办事大厅</title>")) {
							System.out.println("登陆成功！");
							taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
							taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
							taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

							String cookies = CommonUnit.transcookieToJson(webClient);
							taskInsurance.setCookies(cookies);
							saveCookie(parameter, cookies);
						} else {
							System.out.println("异常错误!请从新登陆！");
							taskInsurance
									.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
							taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
							taskInsurance
									.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
							taskInsurance = taskInsuranceRepository.save(taskInsurance);
						}
					} catch (Exception e) {
						System.out.println("异常错误!请从新登陆！");
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
						taskInsurance = taskInsuranceRepository.save(taskInsurance);
					}
				}

			} else {
				System.out.println("获取加密参数失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常错误!请从新登陆！");
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}

	/**
	 * 爬取指定账号的济宁社保信息
	 * 
	 * @param parameter
	 * @return
	 */

	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("InsuranceziyangService.crawler:开始执行爬取", parameter.toString());
		// 爬取解析基本信息
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance);
		// 爬取解析养老保险
		crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance);
		// 爬取解析医疗保险
		crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance);
		// 爬取解析失业保险()
		crawlerBaseInfoService.crawlerUnemploymentInsurance(parameter, taskInsurance);
		// 爬取解析生育保险()
		crawlerBaseInfoService.crawlerShengyuInsurance(parameter, taskInsurance);
		// 爬取解析工伤保险()
		crawlerBaseInfoService.crawlerGongshangInsurance(parameter, taskInsurance);

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

	public static String encrypted(String num, String param1, String param2) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("ziyang.js", Charsets.UTF_8);
		engine.eval(path);

		Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", num, param1, param2);
		return data.toString();
	}

	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	public static String reverse(String s) {
		char[] array = s.toCharArray();
		String resever = "";
		for (int i = array.length - 1; i >= 0; i--) {
			resever += array[i];
		}
		return resever;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
