package app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanImageInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanImageInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.InsuranceJiNanService;

/**
 * 济南社保 Controller
 * 
 * @author qizhongbin
 *
 */
@RestController
@RequestMapping("/insurance/jinan")
public class InsuranceJiNanController {
	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceJiNanService insuranceJiNanService;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceJiNanImageInfoRepository insuranceJiNanImageInfoRepository;

	/**
	 * 登录 接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter) {
		tracer.addTag("action.login.taskid", parameter.getTaskId());
		tracer.addTag("InsuranceJiNanController.login:开始登录", parameter.toString());
		// 通过taskid查出对应表中的数据
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		taskInsurance.setCity(parameter.getCity());
		// 执行登录业务方法
		insuranceJiNanService.login(parameter);
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
		tracer.addTag("InsuranceJiNanController.crawler:检测Task", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 执行业务
		insuranceJiNanService.getAllData(parameter);
		return taskInsurance;
	}

	/**
	 * 登录验证码初始化接口
	 * 
	 * @param parameter
	 * @return
	 */
	@PostMapping(value = "/verificationCode")
	public void verificationCode(@RequestBody InsuranceRequestParameters parameter) {

		tracer.addTag("InsuranceJiNanController.verificationCode:开始获取验证码对应的图片流", null);

		// 获取系统的版本号
		String loginurl = "http://60.216.99.138/hsp/logonDialog.jsp";
		WebClient loginwebClient = WebCrawler.getInstance().getWebClient();
		HtmlPage loginsearchPage = null;
		try {
			loginsearchPage = loginwebClient.getPage(loginurl);

			String asXml = loginsearchPage.asXml();
			System.out.println(asXml);
			String[] split = asXml.split("onLogin");
			String appservion = split[1].substring(2, 8);

			InsuranceJiNanImageInfo insuranceJiNanImageInfo = new InsuranceJiNanImageInfo();
			insuranceJiNanImageInfo.setTaskid(parameter.getTaskId());
			insuranceJiNanImageInfo.setAppservion(appservion);
			insuranceJiNanImageInfoRepository.save(insuranceJiNanImageInfo);
		} catch (IOException e) {
			System.out.println("获取系统版本号保存的过程出现异常！请从新登录！");
		}

	}

}
