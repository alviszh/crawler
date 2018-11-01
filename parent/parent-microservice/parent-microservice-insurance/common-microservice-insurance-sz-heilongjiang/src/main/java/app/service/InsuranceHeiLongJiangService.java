package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangHtml;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangInjury;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMaternity;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMedical;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUnemployment;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceHeiLongJiangParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.heilongjiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.heilongjiang" })
public class InsuranceHeiLongJiangService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHeiLongJiangParser insuranceHeiLongJiangParser;
	@Autowired
	private InsuranceSZHeiLongJiangRepositoryEndowment insuranceSZHeiLongJiangRepositoryEndowment;
	@Autowired
	private InsuranceSZHeiLongJiangRepositoryHtml insuranceSZHeiLongJiangRepositoryHtml;
	@Autowired
	private InsuranceSZHeiLongJiangRepositoryInjury insuranceSZHeiLongJiangRepositoryInjury;
	@Autowired
	private InsuranceSZHeiLongJiangRepositoryMaternity insuranceSZHeiLongJiangRepositoryMaternity;
	@Autowired
	private InsuranceSZHeiLongJiangRepositoryMedical insuranceSZHeiLongJiangRepositoryMedical;
	@Autowired
	private InsuranceSZHeiLongJiangRepositoryUnemployment insuranceSZHeiLongJiangRepositoryUnemployment;
	@Autowired
	private InsuranceSZHeiLongJiangRepositoryUserInfo insuranceSZHeiLongJiangRepositoryUserInfo;
	@Autowired
	private InsuranceService insuranceService;
	@Value("${spring.application.name}")
	String appName;
	
	
	
	//医疗
	public void getMedical(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam<InsuranceSZHeiLongJiangMedical> webParam = insuranceHeiLongJiangParser.getMedical(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
					InsuranceSZHeiLongJiangHtml i = new InsuranceSZHeiLongJiangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getMedical");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZHeiLongJiangRepositoryHtml.save(i);
					insuranceSZHeiLongJiangRepositoryMedical.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getMedical.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.getMedical.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getMedical.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			e.printStackTrace();
		}
		
	}
	
	//养老
	public void getEndowment(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
		
	}
	
	//失业
	public void getUnemployment(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam<InsuranceSZHeiLongJiangUnemployment> webParam = insuranceHeiLongJiangParser.getUnemployment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getHtml())
				{
					tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
					InsuranceSZHeiLongJiangHtml i = new InsuranceSZHeiLongJiangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUnemployment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZHeiLongJiangRepositoryHtml.save(i);
					insuranceSZHeiLongJiangRepositoryUnemployment.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getUnemployment.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.getUnemployment.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getUnemployment.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			e.printStackTrace();
		}

	}
	
	//工伤
	public void getInjury(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam<InsuranceSZHeiLongJiangInjury> webParam = insuranceHeiLongJiangParser.getInjury(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getHtml())
				{
					tracer.addTag("action.getInjury.success",taskInsurance.getTaskid());
					InsuranceSZHeiLongJiangHtml i = new InsuranceSZHeiLongJiangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getInjury");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZHeiLongJiangRepositoryHtml.save(i);
					insuranceSZHeiLongJiangRepositoryInjury.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getInjury.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.getInjury.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getInjury.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			e.printStackTrace();
		}
	}
	
	
	//个人信息
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam<InsuranceSZHeiLongJiangUserInfo> webParam = insuranceHeiLongJiangParser.getUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getHtml())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceSZHeiLongJiangHtml i = new InsuranceSZHeiLongJiangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZHeiLongJiangRepositoryHtml.save(i);
					insuranceSZHeiLongJiangRepositoryUserInfo.save(webParam.getInsuranceSZHeiLongJiangUserInfo());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getUserInfo.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.getUserInfo.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getUserInfo.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
			e.printStackTrace();
		}
		
	}
	
	//生育
	public void getMaternity(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		try {
			WebParam<InsuranceSZHeiLongJiangMaternity> webParam = insuranceHeiLongJiangParser.getMaternity(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getHtml())
				{
					tracer.addTag("action.getMaternity.success",taskInsurance.getTaskid());
					InsuranceSZHeiLongJiangHtml i = new InsuranceSZHeiLongJiangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getMaternity");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZHeiLongJiangRepositoryHtml.save(i);
					insuranceSZHeiLongJiangRepositoryMaternity.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getMaternity.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.getMaternity.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getMaternity.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			e.printStackTrace();
		}
	}
	
	//登陆
	public void login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceHeiLongJiangParser.login(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("我的首页"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceSZHeiLongJiangHtml i = new InsuranceSZHeiLongJiangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZHeiLongJiangRepositoryHtml.save(i);
					taskInsurance.setWebdriverHandle(webParam.getWebHandle());
					taskInsuranceRepository.save(taskInsurance);
					String cookieJson = CommonUnit.transcookieToJson(webParam.getWebClient());
					insuranceService.changeLoginStatusSuccessHttp(taskInsurance,cookieJson);
				}
				else
				{
					tracer.addTag("action.login.ERROR",taskInsurance.getTaskid());
					insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
				}
			}
			else
			{
				tracer.addTag("action.login.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeLoginStatusException(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.login.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeLoginStatusTimeOut(taskInsurance);
		}
		
	}
	
	
}
