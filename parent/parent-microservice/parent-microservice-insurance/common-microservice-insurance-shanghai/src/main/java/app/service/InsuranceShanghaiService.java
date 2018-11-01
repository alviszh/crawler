package app.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shanghai.InsuranceShanghaiGeneralRepository;
import com.microservice.dao.repository.crawler.insurance.shanghai.InsuranceShanghaiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.shanghai.InsuranceShanghaiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.exceptiondetail.EUtils;
import app.parser.InsuranceShanghaiParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.shanghai" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.shanghai" })
public class InsuranceShanghaiService  implements InsuranceLogin{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceShanghaiParser insuranceShanghaiParser;
	@Autowired
	private InsuranceShanghaiUserInfoRepository insuranceShanghaiUserInfoRepository;
	@Autowired
	private InsuranceShanghaiGeneralRepository insuranceShanghaiGeneralRepository;
	@Autowired
	private InsuranceShanghaiHtmlRepository insuranceShanghaiHtmlRepository;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskInsurance loginRetry(InsuranceRequestParameters insuranceRequestParameters,Integer count)
			throws Exception {

		tracer.addTag("InsuranceShanghaiService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		if (null != taskInsurance) {

			insuranceService.changeLoginStatusDoing(taskInsurance);

			WebParam webParam = insuranceShanghaiParser.login(insuranceRequestParameters);

			if (null == webParam) {
				tracer.addTag("InsuranceShanghaiService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			} else {
				String html = webParam.getPage().getWebResponse().getContentAsString();
				tracer.addTag("InsuranceShanghaiService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				if (html.contains("登陆成功")) {
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					return taskInsurance;
				} else if (html.contains("请检查用户名和密码")) {
					taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					return taskInsurance;
				} else if (html.contains("验证码错误")) {
					tracer.addTag("InsuranceShanghaiService.login" + insuranceRequestParameters.getTaskId(),
							"验证码错误次数" + count);
					if (count < 4) {
						loginRetry(insuranceRequestParameters, ++count);
					} else {
						taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
						return taskInsurance;
					}
				} else {
					taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
					return taskInsurance;
				}
			}
		}
		return null;
	}

	/**
	 * @Des 获取信息
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceShanghaiService.getData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = null;
		try {
			taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			tracer.addTag("InsuranceShanghaiService.getData", "Cookie:" + taskInsurance.getCookies());
			Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
			WebParam webParam = insuranceShanghaiParser.getAllData(taskInsurance, cookies);
			if (null != webParam) {
				// 保存页面源码
				insuranceShanghaiHtmlRepository.saveAll(webParam.getInsuranceShanghaiHtml());
				tracer.addTag("InsuranceShanghaiService.getHtml", "SUCCESS:" + insuranceRequestParameters.getTaskId());
				// 保存个人信息
				insuranceShanghaiUserInfoRepository.save(webParam.getInsuranceShanghaiUserInfo());
				tracer.addTag("InsuranceShanghaiService.getUserInfo",
						"SUCCESS:" + insuranceRequestParameters.getTaskId());
				// 个人信息采集成功
				taskInsurance = insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 200);
				// 保存社保流水
				insuranceShanghaiGeneralRepository.saveAll(webParam.getInsuranceShanghaiGeneral());
				tracer.addTag("InsuranceShanghaiService.getGeneral",
						"SUCCESS:" + insuranceRequestParameters.getTaskId());
				taskInsurance.setYanglaoStatus(200);
				taskInsurance.setYiliaoStatus(200);
				taskInsurance.setShiyeStatus(200);
				taskInsurance.setShengyuStatus(201);
				taskInsurance.setGongshangStatus(201);
				taskInsuranceRepository.save(taskInsurance);
				// 采集成功
				insuranceService.changeCrawlerStatusSuccess(taskInsurance);
				tracer.addTag("InsuranceShanghaiService.getHtml", "采集完成:" + insuranceRequestParameters.getTaskId());
				return taskInsurance;
			} else {
				taskInsurance.setUserInfoStatus(500);
				taskInsurance.setYanglaoStatus(500);
				taskInsurance.setYiliaoStatus(500);
				taskInsurance.setShiyeStatus(500);
				taskInsurance.setShengyuStatus(201);
				taskInsurance.setGongshangStatus(201);
				taskInsuranceRepository.save(taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance);
				tracer.addTag("InsuranceShanghaiService.getHtml.webParam is null",
						insuranceRequestParameters.getTaskId());
				return taskInsurance;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceShanghaiService.getData---Taskid--",
					insuranceRequestParameters.getTaskId() + eutils.getEDetail(e));
			insuranceService.changeLoginStatusTimeOut(taskInsurance);
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = null;
		try {
			taskInsurance  = loginRetry(insuranceRequestParameters, 1);
		} catch (Exception e) {
			tracer.addTag("InsuranceDongguanService.login---Taskid--",
					insuranceRequestParameters.getTaskId() + eutils.getEDetail(e));
		}
		return taskInsurance;
	}

}
