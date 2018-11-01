package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingEndowment;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingHtml;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingInjury;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMaternity;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMedical;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingUnemployment;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.nanning.InsuranceNanNingRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.nanning.InsuranceNanNingRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.nanning.InsuranceNanNingRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.nanning.InsuranceNanNingRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.nanning.InsuranceNanNingRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.nanning.InsuranceNanNingRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.nanning.InsuranceNanNingRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceNanNingParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.nanning" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.nanning" })
public class InsuranceNanNingService {

	@Autowired
	private InsuranceNanNingParser insuranceNanNingParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceNanNingRepositoryMaternity insuranceNanNingRepositoryMaternity;
	@Autowired
	private InsuranceNanNingRepositoryEndowment insuranceNanNingRepositoryEndowment;
	@Autowired
	private InsuranceNanNingRepositoryHtml insuranceNanNingRepositoryHtml;
	@Autowired
	private InsuranceNanNingRepositoryInjury insuranceNanNingRepositoryInjury;
	@Autowired
	private InsuranceNanNingRepositoryUserInfo insuranceNanNingRepositoryUserInfo;
	@Autowired
	private InsuranceNanNingRepositoryMedical insuranceNanNingRepositoryMedical;
	@Autowired
	private InsuranceNanNingRepositoryUnemployment insuranceNanNingRepositoryUnemployment;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceNanNingParser.login(insuranceRequestParameters);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("个人缴费明细查询条件"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceNanNingHtml i = new InsuranceNanNingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanNingRepositoryHtml.save(i);
					taskInsurance.setTesthtml(webParam.getHtml());
					taskInsuranceRepository.save(taskInsurance);
					String cookieJson = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskInsurance.setCrawlerHost(insuranceRequestParameters.getPassword());
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
			e.printStackTrace();
		}
		
	}
	public void getMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceNanNingMedical> webParam = insuranceNanNingParser.getMedical(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
					InsuranceNanNingHtml i = new InsuranceNanNingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getMedical");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanNingRepositoryHtml.save(i);
					insuranceNanNingRepositoryMedical.saveAll(webParam.getList());
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
	public void getEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceNanNingEndowment> webParam = insuranceNanNingParser.getEndowment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getEndowment.success",taskInsurance.getTaskid());
					InsuranceNanNingHtml i = new InsuranceNanNingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getEndowment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanNingRepositoryHtml.save(i);
					insuranceNanNingRepositoryEndowment.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getEndowment.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.getEndowment.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getEndowment.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			e.printStackTrace();
		}
		
	}
	
	
	
	public void getUnemployment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceNanNingUnemployment> webParam = insuranceNanNingParser.getUnemployment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getHtml())
				{
					tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
					InsuranceNanNingHtml i = new InsuranceNanNingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUnemployment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanNingRepositoryHtml.save(i);
					insuranceNanNingRepositoryUnemployment.saveAll(webParam.getList());
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
	
	
	
	public void getInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceNanNingInjury> webParam = insuranceNanNingParser.getInjury(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getHtml())
				{
					tracer.addTag("action.getInjury.success",taskInsurance.getTaskid());
					InsuranceNanNingHtml i = new InsuranceNanNingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getInjury");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanNingRepositoryHtml.save(i);
					insuranceNanNingRepositoryInjury.saveAll(webParam.getList());
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
	public void getMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceNanNingMaternity> webParam = insuranceNanNingParser.getMaternity(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getHtml())
				{
					tracer.addTag("action.getMaternity.success",taskInsurance.getTaskid());
					InsuranceNanNingHtml i = new InsuranceNanNingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getMaternity");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanNingRepositoryHtml.save(i);
					insuranceNanNingRepositoryMaternity.saveAll(webParam.getList());
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
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		WebParam<InsuranceNanNingUserInfo> webParam = insuranceNanNingParser.getUserInfo(insuranceRequestParameters,taskInsurance);
		if(null != webParam)
		{
			if(null != webParam.getHtml())
			{
				tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
				InsuranceNanNingHtml i = new InsuranceNanNingHtml();
				i.setHtml(webParam.getHtml());
				i.setPageCount(1);
				i.setType("getUserInfo");
				i.setTaskid(taskInsurance.getTaskid());
				i.setUrl(webParam.getUrl());
				insuranceNanNingRepositoryHtml.save(i);
				insuranceNanNingRepositoryUserInfo.save(webParam.getInsuranceNanNingUserInfo());
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
	}
	

}
