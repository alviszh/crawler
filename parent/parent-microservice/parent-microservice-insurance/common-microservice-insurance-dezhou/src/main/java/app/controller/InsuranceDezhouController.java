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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouImageInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouImageInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.InsuranceDezhouService;
import net.sf.json.JSONObject;

/**
 * 滨州社保 Controller
 * 
 * @author qizhongbin
 *
 */
@RestController
@RequestMapping("/insurance/dezhou")
public class InsuranceDezhouController {
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceDezhouService insuranceDezhouService;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceDeZhouImageInfoRepository insuranceDezhouImageInfoRepository;

	/**
	 * 登录 接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceDezhouController.login:开始登录", parameter.toString());
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		taskInsurance.setCity(parameter.getCity());
		// 执行登录业务方法
		insuranceDezhouService.login(parameter);
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
		tracer.addTag("InsuranceDezhouController.crawler:检测Task", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行业务
		insuranceDezhouService.getAllData(parameter);
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

		tracer.addTag("InsuranceDezhouController.verificationCode:开始获取验证码对应的图片流", null);

		// 获取系统的版本号
		String loginurl = "http://222.133.0.220:9988/hso/logon_371400.jsp";
		WebClient loginwebClient = WebCrawler.getInstance().getWebClient();
		HtmlPage loginsearchPage = null;
		try {
			loginsearchPage = loginwebClient.getPage(loginurl);

			String contentAsString = loginsearchPage.getWebResponse().getContentAsString();
			String[] split = contentAsString.split("appversion =");
			int i = split.length;
			System.out.println(i);
			if (i == 2) {
				String[] split2 = split[1].split(";");
				String appservion;
				appservion = split2[0].trim();
				String[] split3 = appservion.split("\"");
				appservion = split3[1];
				System.out.println("系统版本号-------------" + appservion);
				InsuranceDeZhouImageInfo insuranceDeZhouImageInfo = new InsuranceDeZhouImageInfo();
				insuranceDeZhouImageInfo.setTaskid(parameter.getTaskId());
				insuranceDeZhouImageInfo.setAppservion(appservion);
				insuranceDezhouImageInfoRepository.save(insuranceDeZhouImageInfo);
			} else {
				System.out.println("获取系统版本号保存的过程出现异常！请从新登录！");
			}
		} catch (IOException e) {
			System.out.println("获取系统版本号保存的过程出现异常！请从新登录！");
		}
		List<InsuranceRequestParameters> insuranceRequestParametersList = new ArrayList<InsuranceRequestParameters>();

		String url = "http://222.133.0.220:9988/hso/genAuthCode?_=0.29310054101708904";
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
			insuranceDezhouService.saveCookie(parameter, cookies);

		} catch (Exception e) {
			System.out.println("获取验证码图片返回客户端的过程出现异常！请从新登录！");
		}

		return insuranceRequestParametersList;
	}

}
