package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingHtml;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingInjury;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingMaternity;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingMedical;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUnemployment;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shaoxing.InsuranceShaoXingRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.shaoxing.InsuranceShaoXingRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.shaoxing.InsuranceShaoXingRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.shaoxing.InsuranceShaoXingRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.shaoxing.InsuranceShaoXingRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.shaoxing.InsuranceShaoXingRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.shaoxing.InsuranceShaoXingRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceShaoXingParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shaoxing" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shaoxing" })
public class InsuranceShaoXingService {

	@Autowired
	private InsuranceShaoXingParser insuranceShaoXingParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceShaoXingRepositoryEndowment insuranceShaoXingRepositoryEndowment;
	@Autowired
	private InsuranceShaoXingRepositoryHtml insuranceShaoXingRepositoryHtml;
	@Autowired
	private InsuranceShaoXingRepositoryInjury insuranceShaoXingRepositoryInjury;
	@Autowired
	private InsuranceShaoXingRepositoryMaternity insuranceShaoXingRepositoryMaternity;
	@Autowired
	private InsuranceShaoXingRepositoryMedical insuranceShaoXingRepositoryMedical;
	@Autowired
	private InsuranceShaoXingRepositoryUnemployment insuranceShaoXingRepositoryUnemployment;
	@Autowired
	private InsuranceShaoXingRepositoryUserInfo insuranceShaoXingRepositoryUserInfo;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceShaoXingParser.login(insuranceRequestParameters,taskInsurance);
			if(null !=webParam)
			{
				if(webParam.getHtml().contains("欢迎登入"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceShaoXingHtml i = new InsuranceShaoXingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoXingRepositoryHtml.save(i);
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

	//个人信息
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceShaoXingUserInfo> webParam = insuranceShaoXingParser.getUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceShaoXingUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceShaoXingHtml i = new InsuranceShaoXingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoXingRepositoryHtml.save(i);
					insuranceShaoXingRepositoryUserInfo.save(webParam.getInsuranceShaoXingUserInfo());
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

	//养老
	public void getEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceShaoXingEndowment> webParam = insuranceShaoXingParser.getEndowment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getEndowment.success",taskInsurance.getTaskid());
					InsuranceShaoXingHtml i = new InsuranceShaoXingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getEndowment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoXingRepositoryHtml.save(i);
					insuranceShaoXingRepositoryEndowment.saveAll(webParam.getList());
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

	//医疗
	public void getMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceShaoXingMedical> webParam = insuranceShaoXingParser.getMedical(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
					InsuranceShaoXingHtml i = new InsuranceShaoXingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getMedical");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoXingRepositoryHtml.save(i);
					insuranceShaoXingRepositoryMedical.saveAll(webParam.getList());
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

	//失业
	public void getUnemployment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceShaoXingUnemployment> webParam = insuranceShaoXingParser.getUnemployment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
					InsuranceShaoXingHtml i = new InsuranceShaoXingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUnemployment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoXingRepositoryHtml.save(i);
					insuranceShaoXingRepositoryUnemployment.saveAll(webParam.getList());
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
	public void getInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceShaoXingInjury> webParam = insuranceShaoXingParser.getInjury(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
					InsuranceShaoXingHtml i = new InsuranceShaoXingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getInjury");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoXingRepositoryHtml.save(i);
					insuranceShaoXingRepositoryInjury.saveAll(webParam.getList());
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

	//生育
	public void getMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceShaoXingMaternity> webParam = insuranceShaoXingParser.getMaternity(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMaternity.success",taskInsurance.getTaskid());
					InsuranceShaoXingHtml i = new InsuranceShaoXingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getMaternity");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoXingRepositoryHtml.save(i);
					insuranceShaoXingRepositoryMaternity.saveAll(webParam.getList());
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

}
