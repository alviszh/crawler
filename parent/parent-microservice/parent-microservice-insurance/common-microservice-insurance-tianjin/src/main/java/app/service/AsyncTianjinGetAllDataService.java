package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceTianjinParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.tianjin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.tianjin" })
public class AsyncTianjinGetAllDataService implements InsuranceLogin{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceTianjinService insuranceTianjinService;
	@Autowired
	private InsuranceTianjinParser insuranceTianjinParser;
	@Autowired
	private TracerLog tracer;

	
	

	/**
	 * @Des 登录方法
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceTianjinParser.login(insuranceRequestParameters);
			if (null == webParam) {
				tracer.addTag("parser.login.Error", "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
			} else {
				HtmlPage successPage = webParam.getPage();
				if (successPage.asText().contains("登录失败 用户名或密码错误")) {
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(
							InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),
							InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(), 103, taskInsurance);
					tracer.addTag("parser.login.name.pwd。error", taskInsurance.getTaskid());

				} else if (successPage.asText().contains("登录失败 验证码错误")) {
					taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription(),
							InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase(), 103, taskInsurance);
					tracer.addTag("parser.login.captcha。error", taskInsurance.getTaskid());
				} else {
					tracer.addTag("parser.login.webParam", webParam.toString());
					String html = webParam.getPage().getWebResponse().getContentAsString();
					tracer.addTag("parser.login.html", html);
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
				}
			}
		} catch (Exception e) {
			tracer.addTag("parser.login.TIMEOUT", taskInsurance.getTaskid());
			taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
			e.printStackTrace();
		}
		return taskInsurance;
	}

	/**
	 * @Des 更新taskInsurance
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}

	/**
	 * @Des 爬取总接口
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters){

		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			// 个人信息
			try {
				insuranceTianjinService.getUserInfo(taskInsurance);
				tracer.addTag("parser.crawler.getUserinfo", "个人信息已入库");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 医疗保险信息
			try {
				insuranceTianjinService.getMedicalInfo(taskInsurance);
				tracer.addTag("parser.crawler.getMedicalInfo", "医疗保险信息");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 养老保险信息
			try {
				insuranceTianjinService.getPensionInfo(taskInsurance);
				tracer.addTag("parser.crawler.getPensionInfo", "养老保险信息");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 工伤保险信息
			try {
				insuranceTianjinService.getInjuryInfo(taskInsurance);
				tracer.addTag("parser.crawler.getInjuryInfo", "工伤保险信息");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 生育保险信息
			try {
				insuranceTianjinService.getMaternityInfo(taskInsurance);
				tracer.addTag("parser.crawler.getMaternityInfo", "生育信息已入库");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 失业保险信息
			try {
				insuranceTianjinService.getUnemploymentInfo(taskInsurance);
				tracer.addTag("parser.crawler.getUnemploymentInfo", "失业信息已入库");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return taskInsurance;

	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
