package app.service;

import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouImageInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.binzhou.InsuranceBinZhouImageInfoRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

/**
 * 滨州社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.binzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.binzhou" })
public class InsuranceBinzhouService implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private InsuranceBinZhouImageInfoRepository insuranceBinZhouImageInfoRepository;

	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		List<InsuranceBinZhouImageInfo> insuranceYantaiImageInfo = insuranceBinZhouImageInfoRepository
				.findByCreatetime();
		String appservion = "";
		appservion = insuranceYantaiImageInfo.get(0).getAppservion();
		String trim = appservion.trim();
		String password = parameter.getPassword();
//		String password = parameter.getPassword();
		String md5 = md5(password);
		String s = trim;

		String s2 = parameter.getUsername();
//		String s2 = parameter.getUsername().trim();
		s = s.replace(".", "");
		String hhz;
		hhz = "9853398" + s + "7291166723";
		int len = hhz.length();
		hhz = hhz.substring(6, 9) + s2.substring(4, 7) + hhz.substring(0, 4) + s2.substring(0, 4)
				+ hhz.substring(len - 5) + s2.substring(11, 15) + hhz.substring(3, 7) + s2.substring(11)
				+ hhz.substring(10, 14);
		System.out.println("系统版本处理之后的参数：" + hhz);

		// 拿到登录界面cookies得到一个webclient对象
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		String cook = findByTaskid.getCookies();
		Set<Cookie> transferJsonToSet = CommonUnit.transferJsonToSet(cook);
		WebClient webClient = insuranceService.getWebClient(transferJsonToSet);
		try {
			WebRequest requestSettings = new WebRequest(new URL("http://222.134.45.172:8002/hsp/logon.do"),
					HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "doLogon"));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("_xmlString",
							"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s userid=\""+s2+"\"/><s usermm=\""+md5+"\"/><s authcode=\""+parameter.getVerification()+"\"/><s yxzjlx=\"A\"/><s dlfs=\"1\"/><s appversion=\""+hhz+"\"/></p>"));
			requestSettings.getRequestParameters().add(new NameValuePair("_random", "0.6226803416811124"));

			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			// {"flag":true,"sjhm_zx":"","__usersession_uuid":"USERSESSION_3bd5eac7_dd7d_4b46_91b0_3c2ad343e5cd","sjhm_user":"","sjhm_zxjm":""}
			// 账号错误：查不到用户信息，请先注册。
			// 密码错误：您输入的密码不正确!
			// 验证码错误：输入的验证码不正确，请检查!
			System.out.println("登陆结果-------" + contentAsString);
			if (contentAsString.contains("查不到用户信息，请先注册。")) {
				System.out.println("登陆失败！查不到用户信息，请先注册。");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhasestatus());
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
				JSONObject object = JSONObject.fromObject(contentAsString);
				String flag = object.getString("flag").trim();
				String __usersession_uuid = object.getString("__usersession_uuid").trim();
				System.out.println(flag);
				System.out.println(__usersession_uuid);

				insuranceBinZhouImageInfoRepository.updateBytaskid(__usersession_uuid, parameter.getTaskId());

				if ("true".equals(flag)) {
					System.out.println("登陆成功！");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

					String cookies = CommonUnit.transcookieToJson(loginedPage.getWebClient());
					taskInsurance.setCookies(cookies);
					saveCookie(parameter, cookies);
					taskInsuranceRepository.save(taskInsurance);
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
	 * 爬取指定账号的滨州社保信息
	 * 
	 * @param parameter
	 * @return
	 */
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("InsuranceBinZhouService.crawler:开始执行爬取", parameter.toString());

		// 爬取解析基本信息
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance);

		// 爬取解析养老保险
		for (int k = 0; k < 3; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance, beforeMonth);
		}

		// 爬取解析医疗保险
		for (int k = 0; k < 3; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析失业保险()
		for (int k = 0; k < 3; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerUnemploymentInsurance(parameter, taskInsurance, beforeMonth);
		}

		// 更新最终的状态
		insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId().trim());

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
