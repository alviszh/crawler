package app.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yangzhou.InsuranceYangZhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.yangzhou.InsuranceYangZhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.yangzhou.InsuranceYangZhouMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.yangzhou.InsuranceYangZhouUserInfoRespository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.InsuranceYangZhouHtmlunit;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.yangzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.yangzhou"})
public class InsuranceYangZhouService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceYangZhouHtmlunit insuranceYangZhouHtmlunit;
	@Autowired
	private InsuranceYangZhouHtmlRepository insuranceYangZhouHtmlRepository;
	@Autowired
	private InsuranceYangZhouUserInfoRespository insuranceYangZhouUserInfoRespository;
	@Autowired
	private InsuranceYangZhouMedicalRepository insuranceYangZhouMedicalRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired 
	private TracerLog tracer;	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters)
			throws Exception {
		tracer.addTag("InsuranceYangZhouService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam = insuranceYangZhouHtmlunit.login(insuranceRequestParameters);
			String html = webParam.getHtml();
			tracer.addTag("InsuranceYangZhouService.login",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			String alertMsg=webParam.getAlertMsg();
			if (html.contains("个人信息查询")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "登陆成功");
			    taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
				return taskInsurance;
			}else if(alertMsg.contains("没有注册信息")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "账号不存在");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
				return taskInsurance;
			} else if (alertMsg.contains("密码错误")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "输入的密码错误");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
				return taskInsurance;
			}else {
				tracer.addTag("InsuranceWeiFangService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			}
		}
		return null;
	}
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam = insuranceYangZhouHtmlunit.getUserInfo(taskInsurance, cookies,insuranceRequestParameters.getUsername());
		if(null != webParam){
			String html=webParam.getHtml();
			tracer.addTag("getUserInfo",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			InsuranceYangZhouHtml insuranceYangZhouHtml = new InsuranceYangZhouHtml();
			insuranceYangZhouHtml.setPageCount(1);
			insuranceYangZhouHtml.setType("userinfo");
			insuranceYangZhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceYangZhouHtml.setUrl(webParam.getUrl());
			insuranceYangZhouHtml.setHtml(webParam.getHtml());
			insuranceYangZhouHtmlRepository.save(insuranceYangZhouHtml);
			if (null !=webParam.getUserInfo()) {
				insuranceYangZhouUserInfoRespository.save(webParam.getUserInfo());
				tracer.addTag("getUserInfo", "社保个人信息已入库");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
				taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			}else{
				tracer.addTag("getUserInfo", "社保个人信息未爬取到，无数据");
				insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 201);
				taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			}										
		}		
	}

	/**
	 * @Des 获取医保信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getMedicalList(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("getMedicalList", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam = insuranceYangZhouHtmlunit.getMedicalList(taskInsurance, cookies, insuranceRequestParameters.getUsername());
		if(null != webParam){
			String html=webParam.getHtml();
			tracer.addTag("getMedicalList",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			InsuranceYangZhouHtml insuranceYangZhouHtml = new InsuranceYangZhouHtml();
			insuranceYangZhouHtml.setPageCount(1);
			insuranceYangZhouHtml.setType("medical");
			insuranceYangZhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceYangZhouHtml.setUrl(webParam.getUrl());
			insuranceYangZhouHtml.setHtml(webParam.getHtml());
			insuranceYangZhouHtmlRepository.save(insuranceYangZhouHtml);
			if (null !=webParam.getMedicalList()) {
				insuranceYangZhouMedicalRepository.saveAll(webParam.getMedicalList());
				tracer.addTag("getMedicalList", "医保信息已入库");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
				taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			}else{
				tracer.addTag("getUserInfo", "医保信息未爬取到，无数据");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
				taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
				insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			}										
		}		
	}
}
