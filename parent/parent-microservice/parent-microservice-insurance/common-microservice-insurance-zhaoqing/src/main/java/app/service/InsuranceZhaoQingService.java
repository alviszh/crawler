package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingEndowment;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingHtml;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingInjury;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingMaternity;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingMedical;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingUnemployment;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zhaoqing.InsuranceZhaoQingRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.zhaoqing.InsuranceZhaoQingRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.zhaoqing.InsuranceZhaoQingRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.zhaoqing.InsuranceZhaoQingRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.zhaoqing.InsuranceZhaoQingRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.zhaoqing.InsuranceZhaoQingRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.zhaoqing.InsuranceZhaoQingRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceZhaoQingParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhaoqing" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhaoqing" })
public class InsuranceZhaoQingService {

	@Autowired
	private InsuranceZhaoQingParser insuranceZhaoQingParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceZhaoQingRepositoryHtml insuranceZhaoQingRepositoryHtml;
	@Autowired
	private InsuranceZhaoQingRepositoryUserInfo insuranceZhaoQingRepositoryUserInfo;
	@Autowired
	private InsuranceZhaoQingRepositoryEndowment insuranceZhaoQingRepositoryEndowment;
	@Autowired
	private InsuranceZhaoQingRepositoryInjury insuranceZhaoQingRepositoryInjury;
	@Autowired
	private InsuranceZhaoQingRepositoryMaternity insuranceZhaoQingRepositoryMaternity;
	@Autowired
	private InsuranceZhaoQingRepositoryMedical insuranceZhaoQingRepositoryMedical;
	@Autowired
	private InsuranceZhaoQingRepositoryUnemployment insuranceZhaoQingRepositoryUnemployment;
	
	//登陆
	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceZhaoQingParser.login(insuranceRequestParameters,taskInsurance);
			if(null !=webParam)
			{
				if(webParam.getHtml().contains("个人信息"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceZhaoQingHtml i = new InsuranceZhaoQingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceZhaoQingRepositoryHtml.save(i);
					taskInsurance.setTesthtml(webParam.getHtml());//个人信息
					taskInsurance.setWebdriverHandle(webParam.getWebHandle());//社保号
					taskInsurance.setCrawlerHost(webParam.getUrl());//url参数号
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
	public void crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceZhaoQingUserInfo> webParam = insuranceZhaoQingParser.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceZhaoQingUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceZhaoQingHtml i = new InsuranceZhaoQingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceZhaoQingRepositoryHtml.save(i);
					insuranceZhaoQingRepositoryUserInfo.save(webParam.getInsuranceZhaoQingUserInfo());
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
	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a =0;
			for (int j = 0; j < 6; j++) {
				WebParam<InsuranceZhaoQingEndowment> webParam = insuranceZhaoQingParser.crawlerEndowment(insuranceRequestParameters,taskInsurance,j);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
						InsuranceZhaoQingHtml i = new InsuranceZhaoQingHtml();
						i.setHtml(webParam.getHtml());
						i.setPageCount(1);
						i.setType("getEndowment");
						i.setTaskid(taskInsurance.getTaskid());
						i.setUrl(webParam.getUrl());
						insuranceZhaoQingRepositoryHtml.save(i);
						insuranceZhaoQingRepositoryEndowment.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
						a++;
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
			if(a>0)
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
	public void crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a=0;
			for (int j = 0; j < 6; j++) {
			   WebParam<InsuranceZhaoQingMedical> webParam = insuranceZhaoQingParser.crawlerMedical(insuranceRequestParameters,taskInsurance,j);
			   if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
						InsuranceZhaoQingHtml i = new InsuranceZhaoQingHtml();
						i.setHtml(webParam.getHtml());
						i.setPageCount(1);
						i.setType("getMedical");
						i.setTaskid(taskInsurance.getTaskid());
						i.setUrl(webParam.getUrl());
						insuranceZhaoQingRepositoryHtml.save(i);
						insuranceZhaoQingRepositoryMedical.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
						a++;
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
			if(a>0)
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

	//生育
	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a=0;
			for (int j = 0; j <6; j++) {
			   WebParam<InsuranceZhaoQingMaternity> webParam = insuranceZhaoQingParser.crawlerMaternity(insuranceRequestParameters,taskInsurance,j);
			   if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getMaternity.success",taskInsurance.getTaskid());
						InsuranceZhaoQingHtml i = new InsuranceZhaoQingHtml();
						i.setHtml(webParam.getHtml());
						i.setPageCount(1);
						i.setType("getMaternity");
						i.setTaskid(taskInsurance.getTaskid());
						i.setUrl(webParam.getUrl());
						insuranceZhaoQingRepositoryHtml.save(i);
						insuranceZhaoQingRepositoryMaternity.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
						a++;
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
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getMaternity.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			e.printStackTrace();
		}			
	}

	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a=0;
			for (int j = 0; j < 6; j++) {
			   WebParam<InsuranceZhaoQingInjury> webParam = insuranceZhaoQingParser.crawlerInjury(insuranceRequestParameters,taskInsurance,j);
			   if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getInjury.success",taskInsurance.getTaskid());
						InsuranceZhaoQingHtml i = new InsuranceZhaoQingHtml();
						i.setHtml(webParam.getHtml());
						i.setPageCount(1);
						i.setType("getMedical");
						i.setTaskid(taskInsurance.getTaskid());
						i.setUrl(webParam.getUrl());
						insuranceZhaoQingRepositoryHtml.save(i);
						insuranceZhaoQingRepositoryInjury.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
						a++;
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
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
			}
			else{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);

			}
		} catch (Exception e) {
			tracer.addTag("action.getInjury.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			e.printStackTrace();
		}			
	}

	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {int a=0;
			for (int j = 0; j < 6; j++) {
			   WebParam<InsuranceZhaoQingUnemployment> webParam = insuranceZhaoQingParser.crawlerUnemployment(insuranceRequestParameters,taskInsurance,j);
			   if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
						InsuranceZhaoQingHtml i = new InsuranceZhaoQingHtml();
						i.setHtml(webParam.getHtml());
						i.setPageCount(1);
						i.setType("getUnemployment");
						i.setTaskid(taskInsurance.getTaskid());
						i.setUrl(webParam.getUrl());
						insuranceZhaoQingRepositoryHtml.save(i);
						insuranceZhaoQingRepositoryUnemployment.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
						a++;
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
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getUnemployment.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			e.printStackTrace();
		}			
	}

}
