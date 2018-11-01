package app.service;

import java.net.URL;
import java.security.MessageDigest;
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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import app.service.aop.InsuranceSms;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.kaifeng" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.kaifeng" })
public class InsurancekaifengService implements InsuranceLogin, InsuranceSms {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;

	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		String loginType = parameter.getLoginType();
		if ("短信验证码登录".equals(loginType)) {
			System.out.println("短信验证码登录!");
			try {
				String cook = taskInsurance.getCookies();
				Set<Cookie> transferJsonToSet = CommonUnit.transferJsonToSet(cook);
				WebClient webClient = insuranceService.getWebClient(transferJsonToSet);
				// 登录请求
				WebRequest requestSettings1 = new WebRequest(new URL("https://gr.kf12333.cn/loginAction.action"),
						HttpMethod.POST);
				String body1 = "from=&redirect=&username=&password=&phoneNumber=" + parameter.getUsername().trim()
						+ "&smsVerificationCode=" + parameter.getVerification().trim() + "&loginMode=1";
				requestSettings1.setRequestBody(body1);
				Page loginedPage1 = webClient.getPage(requestSettings1);
				String contentAsString1 = loginedPage1.getWebResponse().getContentAsString();
				System.out.println("登录结果：" + contentAsString1);
				JSONObject object2 = JSONObject.fromObject(contentAsString1);
				String message2 = object2.getString("message");
				if (message2.contains("登录成功")) {
					System.out.println("登录成功!");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
					String cookies = CommonUnit.transcookieToJson(webClient);
					taskInsurance.setCookies(cookies);
					saveCookie(parameter, cookies);
				} else {
					System.out.println("登录失败!请从新登录！");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				}
			} catch (Exception e) {
				System.out.println("登录失败!请从新登录！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
				e.printStackTrace();
			}

		} else {
			System.out.println("用户名密码登录！");

			try {
				WebClient webClient = WebCrawler.getInstance().getWebClient();
				webClient.getOptions().setTimeout(70000);
				WebRequest requestSettings = new WebRequest(new URL("https://gr.kf12333.cn/loginAction.action"),
						HttpMethod.POST);
				String body = "from=&redirect=&username=" + parameter.getUsername().trim() + "&password="
						+ parameter.getPassword().trim() + "&phoneNumber=&smsVerificationCode=&loginMode=0";
				requestSettings.setRequestBody(body);
				Page loginedPage = webClient.getPage(requestSettings);
				String contentAsString = loginedPage.getWebResponse().getContentAsString();
				System.out.println("登录结果：" + contentAsString);
				JSONObject object2 = JSONObject.fromObject(contentAsString);
				String message2 = object2.getString("message");
				if (message2.contains("登录成功")) {
					System.out.println("登录成功!");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
					String cookies = CommonUnit.transcookieToJson(webClient);
					taskInsurance.setCookies(cookies);
					saveCookie(parameter, cookies);
				} else {
					System.out.println("登录失败!请从新登录！");
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				}
			} catch (Exception e) {
				System.out.println("登录失败!请从新登录！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
				e.printStackTrace();
			}
		}

		return taskInsurance;
	}

	// 发送手机验证码
	@Async
	public TaskInsurance sendSms(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			webClient.getOptions().setTimeout(70000);
			// 发送验证码请求
			WebRequest requestSettings = new WebRequest(new URL("https://gr.kf12333.cn/loginSMSAction.action"),
					HttpMethod.POST);
			String body = "phoneNumber=18738990573&sjc=Mon May 07 2018 11:08:05 GMT+0800 (中国标准时间)";
			requestSettings.setRequestBody(body);
			Page loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			System.out.println("发送那个验证码请求的结果是：" + contentAsString);
			JSONObject object = JSONObject.fromObject(contentAsString);
			String message = object.getString("message");
			if (message.contains("发送成功")) {
				System.out.println("验证码发送成功！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getPhasestatus());
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setCookies(cookies);
				saveCookie(parameter, cookies);
			} else {
				System.out.println("验证码发送失败！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			System.out.println("异常错误！验证码发送失败！");
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			e.printStackTrace();
		}
		return taskInsurance;
	}

	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("InsurancekaifengService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 爬取解析基本信息
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance);
		// 爬取解析养老保险
		crawlerBaseInfoService.crawlerAgedInsurance(parameter, taskInsurance);
		// 爬取解析医疗保险
		crawlerBaseInfoService.crawlerMedicalInsurance(parameter, taskInsurance);
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

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskInsurance verifySms(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
