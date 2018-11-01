package app.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;
import com.module.ocr.utils.ChaoJiYingUtils;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.zaozhuang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.zaozhuang" })
public class InsuranceZaoZhuangService extends AbstractChaoJiYingHandler implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static final String OCR_FILE_PATH = "D:\\img";
	private String uuid = UUID.randomUUID().toString();

	public static void saveFile(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);

		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);

		}
		inputStream.close();
		outputStream.close();
	}

	public String callChaoJiYingService(String imgPath, String codeType) {
		Gson gson = new GsonBuilder().create();
		long starttime = System.currentTimeMillis();
		String chaoJiYingResult = super.getVerifycodeByChaoJiYing(codeType, LEN_MIN, TIME_ADD, STR_DEBUG, imgPath);

		String errNo = ChaoJiYingUtils.getErrNo(chaoJiYingResult);
		if (!ChaoJiYingUtils.RESULT_SUCCESS.equals(errNo)) {
			tracer.addTag("errNo ======>>", errNo);
			return ChaoJiYingUtils.getErrMsg(errNo);
		}

		tracer.addTag("ChaoJiYingResult [CODETYPE={}]: {}", chaoJiYingResult);
		long endtime = System.currentTimeMillis();
		tracer.addTag("超级鹰识别耗时 callChaoJiYingService", (endtime - starttime) + "ms");
		return (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");

	}

	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 获取系统的版本号
		String loginurl = "http://218.56.155.46:8081/hso/perLogonPage.jsp";
		WebClient loginwebClient = WebCrawler.getInstance().getWebClient();
		HtmlPage loginsearchPage = null;
		try {
			loginsearchPage = loginwebClient.getPage(loginurl);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String attribute = loginsearchPage.getWebResponse().getContentAsString();
		String[] split = attribute.split("doLogonNew");
		String appservion = split[1].substring(2, 8);
		String s = appservion.trim();
		String str2 = parameter.getUsername().trim();
		s = s.replace(".", "");
		String hhz;
		hhz = "12577123" + s + "819944387";
		int len = hhz.length();
		hhz = hhz.substring(6, 10) + str2.substring(4, 7) + hhz.substring(0, 3) + str2.substring(0, 4)
				+ hhz.substring(len - 5) + str2.substring(11, 15) + hhz.substring(3, 6) + str2.substring(11)
				+ hhz.substring(11, 14);
		System.out.println("版本号-----------" + appservion);
		System.out.println("系统版本处理之后的参数：" + hhz);

		// 获取图片验证码
		String loginurl2 = "http://218.56.155.46:8081/hso/authcode3?_=0.2828684400441386";
		WebClient loginwebClient2 = WebCrawler.getInstance().getWebClient();
		Page loginsearchPage2 = null;
		try {
			loginsearchPage2 = loginwebClient2.getPage(loginurl2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream contentAsStream = null;
		String code = null;
		try {
			contentAsStream = loginsearchPage2.getWebResponse().getContentAsStream();
			saveFile(contentAsStream, OCR_FILE_PATH + "\\" + uuid + ".png");

			code = callChaoJiYingService(OCR_FILE_PATH + "\\" + uuid + ".png", "9101");
			System.out.println(code);

			String cookies = CommonUnit.transcookieToJson(loginwebClient2);
			// 通过taskid将登录界面的cookie存进数据库
			saveCookie(parameter, cookies);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		System.out.println("验证码坐标---------" + code);

		// 身份证号
		String username = parameter.getUsername().trim();
		System.out.println("身份证号-----------" + username);

		// 密码
		String password = parameter.getPassword().trim();
		String md5 = md5(password);
		System.out.println("原密码-----------" + password);
		System.out.println("加密之后的密码-----------" + md5);

		// 拿到登录界面cookies得到一个webclient对象
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		String cook = findByTaskid.getCookies();
		Set<Cookie> transferJsonToSet = CommonUnit.transferJsonToSet(cook);
		WebClient webClient = insuranceService.getWebClient(transferJsonToSet);
		try {
			String url = "http://218.56.155.46:8081/hso/logon.do?method=doLogonAllowRepeat&usertype=1&username="
					+ username + "&password=" + md5 + "&validatecode=[" + code + "]&appversion=" + hhz;
			System.out.println("登录请求路径------" + url);
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			// 结果五:系统版本与数据库版本两者不一致！
			System.out.println("登陆结果-------" + contentAsString);
			if (contentAsString.contains("您还没有社保卡，不需要填写社保卡选项")) {
				System.out.println("登陆失败！您还没有社保卡，不需要填写社保卡选项");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTEXIST.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTEXIST.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTEXIST.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else if (contentAsString.contains("您输入的密码不正确!")) {
				System.out.println("登陆失败！您输入的密码不正确!");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_PWD_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else if (contentAsString.contains("输入的验证码不正确，请检查!")) {
				System.out.println("登陆失败！输入的验证码不正确，请检查!");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				try {

					// JSONObject object =
					// JSONObject.fromObject(contentAsString);
					// String flag = object.getString("flag").trim();
					// String __usersession_uuid =
					// object.getString("__usersession_uuid").trim();
					// System.out.println(flag);
					// System.out.println(__usersession_uuid);
					//
					// insuranceZaoZhuangImageInfoRepository.updateBytaskid(__usersession_uuid,
					// parameter.getTaskId());

					if (contentAsString.contains("true")) {
						System.out.println("登陆成功！");
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
						taskInsurance = taskInsuranceRepository.save(taskInsurance);
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
				} catch (Exception e) {
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
	 * 爬取指定账号的济宁社保信息
	 * 
	 * @param parameter
	 * @return
	 */
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("InsuranceZaoZhuangService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 爬取解析基本信息
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance);

		// 爬取解析养老保险
		for (int k = 0; k < 6; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析医疗保险
		for (int k = 0; k < 6; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析失业保险()
		for (int k = 0; k < 6; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerUnemploymentInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析生育保险()
		for (int k = 0; k < 6; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerShengyuInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析工伤保险()
		for (int k = 0; k < 6; k++) {
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

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
