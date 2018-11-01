package app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingImageInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingImageInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.InsuranceJiNingService;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/insurance/jining")
public class InsuranceJiNingController {
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceJiNingService insuranceJiNingService;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Autowired
	private InsuranceJiNingImageInfoRepository insuranceJiNingImageInfoRepository;

	/**
	 * 登录 接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceJiNingController.login:开始登录", parameter.toString());
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		taskInsurance.setCity(parameter.getCity());
		// 执行登录业务方法
		insuranceJiNingService.login(parameter);
		return taskInsurance;
	}

	/**
	 * 爬取,解析接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.crawler.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceJiNingController.crawler:检测Task", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行业务
		insuranceJiNingService.getAllData(parameter);
		return taskInsurance;
	}

	/**
	 * 登录验证码初始化接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/verificationCode")
	public List<InsuranceRequestParameters> verificationCode(@RequestBody InsuranceRequestParameters parameter) {

		tracer.addTag("InsuranceJiNingController.verificationCode:开始获取验证码对应的图片流", null);

		// 获取系统的版本号
		String loginurl = "http://60.211.255.251:8082/hsp/logonDialog_114.jsp";
		WebClient loginwebClient = WebCrawler.getInstance().getWebClient();
		HtmlPage loginsearchPage = null;
		try {
			loginsearchPage = loginwebClient.getPage(loginurl);

			HtmlImage submitButton = (HtmlImage) loginsearchPage.getFirstByXPath("//img[@class='logonBtn']");
			String attribute = submitButton + "";
			// n" onclick="onLogin('1.0.63','1
			String[] split = attribute.split("onLogin");
			String appservion = split[1].substring(2, 8);

			InsuranceJiNingImageInfo insuranceJiNingImageInfo = new InsuranceJiNingImageInfo();
			insuranceJiNingImageInfo.setTaskid(parameter.getTaskId());
			insuranceJiNingImageInfo.setAppservion(appservion);
			insuranceJiNingImageInfoRepository.save(insuranceJiNingImageInfo);
		} catch (IOException e) {
			System.out.println("获取系统版本号保存的过程出现异常！请从新登录！");
		}
		List<InsuranceRequestParameters> insuranceRequestParametersList = new ArrayList<InsuranceRequestParameters>();

		String url = "http://60.211.255.251:8082/hsp/genAuthCode?_=0.037271939301279255";
		TextPage searchPage;
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			searchPage = webClient.getPage(url);

			String image = searchPage.getWebResponse().getContentAsString();
			JSONObject jsonObj = JSONObject.fromObject(image);

			String numLeft = "data:image/jpg;base64," + jsonObj.get("numLeftBase64");
			InsuranceRequestParameters insuranceRequestParameters = new InsuranceRequestParameters();
			insuranceRequestParameters.setBase64(numLeft);
			insuranceRequestParametersList.add(insuranceRequestParameters);

			String operator = "data:image/png;base64," + jsonObj.get("operatorBase64");
			InsuranceRequestParameters insuranceRequestParameters2 = new InsuranceRequestParameters();
			insuranceRequestParameters2.setBase64(operator);
			insuranceRequestParametersList.add(insuranceRequestParameters2);

			String numRight = "data:image/jpg;base64," + jsonObj.get("numRightBase64");
			InsuranceRequestParameters insuranceRequestParameters3 = new InsuranceRequestParameters();
			insuranceRequestParameters3.setBase64(numRight);
			insuranceRequestParametersList.add(insuranceRequestParameters3);

			String equals = "data:image/png;base64," + jsonObj.get("equalsBase64");
			InsuranceRequestParameters insuranceRequestParameters4 = new InsuranceRequestParameters();
			insuranceRequestParameters4.setBase64(equals);
			insuranceRequestParametersList.add(insuranceRequestParameters4);

			String cookies = CommonUnit.transcookieToJson(webClient);
			// 通过taskid将登录界面的cookie存进数据库
			insuranceJiNingService.saveCookie(parameter, cookies);

		} catch (Exception e) {
			System.out.println("获取验证码图片返回客户端的过程出现异常！请从新登录！");
		}

		return insuranceRequestParametersList;
	}

}
