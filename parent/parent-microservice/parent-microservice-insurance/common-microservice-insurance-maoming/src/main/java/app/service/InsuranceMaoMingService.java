package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingEndowment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingHtml;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingInjury;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMaternity;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMedical;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUnemployment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.maoming.InsuranceMaoMingRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.maoming.InsuranceMaoMingRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.maoming.InsuranceMaoMingRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.maoming.InsuranceMaoMingRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.maoming.InsuranceMaoMingRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.maoming.InsuranceMaoMingRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.maoming.InsuranceMaoMingRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceMaoMingParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.maoming" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.maoming" })
public class InsuranceMaoMingService {

	@Autowired
	private InsuranceMaoMingParser insuranceMaoMingParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceMaoMingRepositoryEndowment insuranceMaoMingRepositoryEndowment;
	@Autowired
	private InsuranceMaoMingRepositoryHtml insuranceMaoMingRepositoryHtml;
	@Autowired
	private InsuranceMaoMingRepositoryInjury insuranceMaoMingRepositoryInjury;
	@Autowired
	private InsuranceMaoMingRepositoryMaternity insuranceMaoMingRepositoryMaternity;
	@Autowired
	private InsuranceMaoMingRepositoryMedical insuranceMaoMingRepositoryMedical;
	@Autowired
	private InsuranceMaoMingRepositoryUnemployment insuranceMaoMingRepositoryUnemployment;
	@Autowired
	private InsuranceMaoMingRepositoryUserInfo insuranceMaoMingRepositoryUserInfo;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceMaoMingParser.login(insuranceRequestParameters,taskInsurance);
			if(null !=webParam)
			{
				if(webParam.getHtml().contains("欢迎页"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceMaoMingHtml i = new InsuranceMaoMingHtml();
					i.setHtml(webParam.getHtml());
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceMaoMingRepositoryHtml.save(i);
					taskInsuranceRepository.save(taskInsurance);
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					insuranceService.changeLoginStatusSuccessHttp(taskInsurance,cookieString);
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
	
	
	public void crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceMaoMingUserInfo> webParam = insuranceMaoMingParser.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceMaoMingUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceMaoMingHtml i = new InsuranceMaoMingHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceMaoMingRepositoryHtml.save(i);
					insuranceMaoMingRepositoryUserInfo.save(webParam.getInsuranceMaoMingUserInfo());
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


	public void crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceMaoMingMedical> webParam = insuranceMaoMingParser.crawlerMedical(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
					InsuranceMaoMingHtml i = new InsuranceMaoMingHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getMedical");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceMaoMingRepositoryHtml.save(i);
					insuranceMaoMingRepositoryMedical.saveAll(webParam.getList());
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


	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceMaoMingEndowment> webParam = insuranceMaoMingParser.crawlerEndowment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getEndowment.success",taskInsurance.getTaskid());
					InsuranceMaoMingHtml i = new InsuranceMaoMingHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getEndowment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceMaoMingRepositoryHtml.save(i);
					insuranceMaoMingRepositoryEndowment.saveAll(webParam.getList());
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


	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceMaoMingUnemployment> webParam = insuranceMaoMingParser.crawlerUnemployment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
					InsuranceMaoMingHtml i = new InsuranceMaoMingHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getUnemployment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceMaoMingRepositoryHtml.save(i);
					insuranceMaoMingRepositoryUnemployment.saveAll(webParam.getList());
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


	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceMaoMingInjury> webParam = insuranceMaoMingParser.crawlerInjury(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getInjury.success",taskInsurance.getTaskid());
					InsuranceMaoMingHtml i = new InsuranceMaoMingHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getInjury");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceMaoMingRepositoryHtml.save(i);
					insuranceMaoMingRepositoryInjury.saveAll(webParam.getList());
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


	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceMaoMingMaternity> webParam = insuranceMaoMingParser.crawlerMaternity(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerMaternity.success",taskInsurance.getTaskid());
					InsuranceMaoMingHtml i = new InsuranceMaoMingHtml();
					i.setHtml(webParam.getHtml());
					i.setType("crawlerMaternity");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceMaoMingRepositoryHtml.save(i);
					insuranceMaoMingRepositoryMaternity.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
				}
				else
				{
					tracer.addTag("action.crawlerMaternity.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.crawlerMaternity.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getMaternity.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			e.printStackTrace();
		}
		
	}

}
