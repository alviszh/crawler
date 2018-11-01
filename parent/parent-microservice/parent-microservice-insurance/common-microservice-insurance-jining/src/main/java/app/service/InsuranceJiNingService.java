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
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingImageInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingImageInfoRepository;

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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.jining" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.jining" })
public class InsuranceJiNingService implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private InsuranceJiNingImageInfoRepository insuranceJiNingImageInfoRepository;

	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		List<InsuranceJiNingImageInfo> insuranceJiNingImageInfo = insuranceJiNingImageInfoRepository.findByCreatetime();
		String appservion = "";
		appservion = insuranceJiNingImageInfo.get(0).getAppservion().trim();
		String s = appservion.trim();
		String password = parameter.getPassword();
		String md5 = md5("wh");

		System.out.println("密码：" + md5);
		System.out.println("系统版本：" + appservion);
		System.out.println("验证码：" + parameter.getVerification().trim());

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
			WebRequest requestSettings = new WebRequest(new URL("http://60.211.255.251:8082/hsp/logon.do"),
					HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "doLogon"));
			requestSettings.getRequestParameters().add(new NameValuePair("_xmlString","<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s userid=\""+s2+"\"/><s usermm=\""+md5+"\"/><s authcode=\""+parameter.getVerification().trim()+"\"/><s yxzjlx=\"A\"/><s appversion=\""+hhz+"\"/><s dlfs=\"\"/><s sbkhm=\""+password+"\"/></p>"));
			requestSettings.getRequestParameters().add(new NameValuePair("_random", "0.9454396308620945"));
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
			} else if (contentAsString.contains("社保卡号与身份证号码不匹配,请检查")) {
				System.out.println("登陆失败！社保卡号与身份证号码不匹配,请检查");
				taskInsurance.setDescription(
						InsuranceStatusCode.INSURANCE_LOGIN_CARD_MISMATCHING_SOCIAL_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CARD_MISMATCHING_SOCIAL_ERROR.getPhase());
				taskInsurance.setPhase_status(
						InsuranceStatusCode.INSURANCE_LOGIN_CARD_MISMATCHING_SOCIAL_ERROR.getPhasestatus());
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

					JSONObject object = JSONObject.fromObject(contentAsString);
					String flag = object.getString("flag").trim();
					String __usersession_uuid = object.getString("__usersession_uuid").trim();
					System.out.println(flag);
					System.out.println(__usersession_uuid);

					insuranceJiNingImageInfoRepository.updateBytaskid(__usersession_uuid, parameter.getTaskId());

					if ("true".equals(flag)) {
						System.out.println("登陆成功！");
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

						String cookies = CommonUnit.transcookieToJson(loginedPage.getWebClient());
						taskInsurance.setCookies(cookies);
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
		tracer.addTag("InsuranceJiNingService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
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
		// 爬取解析生育保险()
		for (int k = 0; k < 3; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			crawlerBaseInfoService.crawlerShengyuInsurance(parameter, taskInsurance, beforeMonth);
		}
		// 爬取解析工伤保险()
		for (int k = 0; k < 3; k++) {
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
