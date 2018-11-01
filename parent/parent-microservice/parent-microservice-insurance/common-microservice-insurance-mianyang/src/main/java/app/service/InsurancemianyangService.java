package app.service;

import java.net.URL;
import java.security.MessageDigest;

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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * 济宁社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.mianyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.mianyang" })
public class InsurancemianyangService implements InsuranceLogin {

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
			WebRequest webRequest = new WebRequest(new URL("http://rsjapp.my.gov.cn/mycx/login.jsp"), HttpMethod.GET);
			HtmlPage loginPage = webClient.getPage(webRequest);
			HtmlTextInput username = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='j_username']");
			HtmlPasswordInput password = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='j_password']");
			HtmlTextInput validateCode = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='checkCode']");
			HtmlElement submitButton = (HtmlElement) loginPage.getFirstByXPath("//button[@id='loginBtn']");

			username.setText(parameter.getUsername().trim());
			password.setText(parameter.getPassword().trim());

			HtmlImage image = (HtmlImage) loginPage.getFirstByXPath("//img[@id='codeimg']");
			String code = chaoJiYingOcrService.getVerifycode(image, "3007");
			System.out.println("验证码：" + code);
			validateCode.setText(code);
			System.out.println(username + "");
			System.out.println(password + "");
			System.out.println(validateCode + "");
			HtmlPage loginedPage = submitButton.click();
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			// 结果五:系统版本与数据库版本两者不一致！
			if (contentAsString.contains("退出系统")) {
				System.out.println("登陆成功！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

				String cookies = CommonUnit.transcookieToJson(loginedPage.getWebClient());
				taskInsurance.setCookies(cookies);
				saveCookie(parameter, cookies);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("异常错误!请从新登陆！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
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
		tracer.addTag("InsurancemianyangService.crawler:开始执行爬取", parameter.toString());
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
}
