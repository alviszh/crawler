package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanHtml;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanMedical;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUnemployment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shaoguan.InsuranceShaoGuanRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.shaoguan.InsuranceShaoGuanRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.shaoguan.InsuranceShaoGuanRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.shaoguan.InsuranceShaoGuanRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.shaoguan.InsuranceShaoGuanRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceShaoGuanParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shaoguan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shaoguan" })
public class InsuranceShaoGuanService {
	
	@Autowired
	private InsuranceShaoGuanParser insuranceShaoGuanParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceShaoGuanRepositoryEndowment insuranceShaoGuanRepositoryEndowment;
	@Autowired
	private InsuranceShaoGuanRepositoryHtml insuranceShaoGuanRepositoryHtml;
	@Autowired
	private InsuranceShaoGuanRepositoryMedical insuranceShaoGuanRepositoryMedical;
	@Autowired
	private InsuranceShaoGuanRepositoryUnemployment insuranceShaoGuanRepositoryUnemployment;
	@Autowired
	private InsuranceShaoGuanRepositoryUserInfo insuranceShaoGuanRepositoryUserInfo;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;

	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceShaoGuanParser.login(insuranceRequestParameters,taskInsurance);
			if(null !=webParam)
			{
				if(webParam.getHtml().contains("网上办事大厅"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceShaoGuanHtml i = new InsuranceShaoGuanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoGuanRepositoryHtml.save(i);
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
			WebParam<InsuranceShaoGuanUserInfo> webParam = insuranceShaoGuanParser.getUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceShaoGuanUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceShaoGuanHtml i = new InsuranceShaoGuanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoGuanRepositoryHtml.save(i);
					insuranceShaoGuanRepositoryUserInfo.save(webParam.getInsuranceShaoGuanUserInfo());
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
			String crawlerHost = taskInsurance.getCrawlerHost();
			int time=0;
			int degree=0;
			if(crawlerHost.equals("false"))
			{
			   time=time+1;
			}
			else
			{
				time=time+Integer.parseInt(crawlerHost);
			}
			for (int i =1; i < time+1; i++) {
				WebParam<InsuranceShaoGuanEndowment> webParam = insuranceShaoGuanParser.getEndowment(insuranceRequestParameters,taskInsurance,i);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getEndowment.success",taskInsurance.getTaskid());
						InsuranceShaoGuanHtml ii = new InsuranceShaoGuanHtml();
						ii.setHtml(webParam.getHtml());
						ii.setPageCount(1);
						ii.setType("getEndowment");
						ii.setTaskid(taskInsurance.getTaskid());
						ii.setUrl(webParam.getUrl());
						insuranceShaoGuanRepositoryHtml.save(ii);
						insuranceShaoGuanRepositoryEndowment.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
						degree++;
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
			}
			if(degree>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
			}
			else
			{
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
			String crawlerHost = taskInsurance.getCrawlerHost();
			int time=0;
			int degree=0;
			if(crawlerHost.equals("false"))
			{
			   time=time+1;
			}
			else
			{
				time=time+Integer.parseInt(crawlerHost);
			}
			for (int i =1; i < time+1; i++) {
			WebParam<InsuranceShaoGuanMedical> webParam = insuranceShaoGuanParser.getMedical(insuranceRequestParameters,taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
					InsuranceShaoGuanHtml ii = new InsuranceShaoGuanHtml();
					ii.setHtml(webParam.getHtml());
					ii.setPageCount(1);
					ii.setType("getMedical");
					ii.setTaskid(taskInsurance.getTaskid());
					ii.setUrl(webParam.getUrl());
					insuranceShaoGuanRepositoryHtml.save(ii);
					insuranceShaoGuanRepositoryMedical.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
					degree++;
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
			}
			if(degree>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
			}
			else
			{
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
			WebParam<InsuranceShaoGuanUnemployment> webParam = insuranceShaoGuanParser.getUnemployment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
					InsuranceShaoGuanHtml i = new InsuranceShaoGuanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUnemployment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceShaoGuanRepositoryHtml.save(i);
					insuranceShaoGuanRepositoryUnemployment.saveAll(webParam.getList());
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
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
		
	}

	public void getMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_NOT_FOUND);
		
	}


	

	
}
