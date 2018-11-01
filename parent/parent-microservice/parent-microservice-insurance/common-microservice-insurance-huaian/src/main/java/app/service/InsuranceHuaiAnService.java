package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnEndowment;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnHtml;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnInjury;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnMaternity;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnMedical;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnUnemployment;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.huaian.InsuranceHuaiAnRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.huaian.InsuranceHuaiAnRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.huaian.InsuranceHuaiAnRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.huaian.InsuranceHuaiAnRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.huaian.InsuranceHuaiAnRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.huaian.InsuranceHuaiAnRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.huaian.InsuranceHuaiAnRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceHuaiAnParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huaian" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huaian" })
public class InsuranceHuaiAnService {

	@Autowired
	private InsuranceHuaiAnParser insuranceHuaiAnParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceHuaiAnRepositoryEndowment insuranceHuaiAnRepositoryEndowment;
	@Autowired
	private InsuranceHuaiAnRepositoryHtml insuranceHuaiAnRepositoryHtml;
	@Autowired
	private InsuranceHuaiAnRepositoryInjury insuranceHuaiAnRepositoryInjury;
	@Autowired
	private InsuranceHuaiAnRepositoryMaternity insuranceHuaiAnRepositoryMaternity;
	@Autowired
	private InsuranceHuaiAnRepositoryMedical insuranceHuaiAnRepositoryMedical;
	@Autowired
	private InsuranceHuaiAnRepositoryUnemployment insuranceHuaiAnRepositoryUnemployment;
	@Autowired
	private InsuranceHuaiAnRepositoryUserInfo insuranceHuaiAnRepositoryUserInfo;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceHuaiAnParser.login(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("个人编号"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceHuaiAnHtml i = new InsuranceHuaiAnHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceHuaiAnRepositoryHtml.save(i);
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
			WebParam<InsuranceHuaiAnUserInfo> webParam = insuranceHuaiAnParser.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceHuaiAnUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceHuaiAnHtml i = new InsuranceHuaiAnHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceHuaiAnRepositoryHtml.save(i);
					insuranceHuaiAnRepositoryUserInfo.save(webParam.getInsuranceHuaiAnUserInfo());
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
	
	//医疗
	public void crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			int a=0;
			for (int i = 0; i < 3; i++) {
				WebParam<InsuranceHuaiAnMedical> webParam = insuranceHuaiAnParser.crawlerMedical(insuranceRequestParameters,taskInsurance,i);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.crawlerMedical.success",taskInsurance.getTaskid());
						InsuranceHuaiAnHtml i1 = new InsuranceHuaiAnHtml();
						i1.setHtml(webParam.getHtml());
						i1.setPageCount(1);
						i1.setType("crawlerMedical");
						i1.setTaskid(taskInsurance.getTaskid());
						i1.setUrl(webParam.getUrl());
						insuranceHuaiAnRepositoryHtml.save(i1);
						insuranceHuaiAnRepositoryMedical.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
						a++;
					}
					else
					{
						tracer.addTag("action.crawlerMedical.ERROR",taskInsurance.getTaskid());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
					}
				}
				else
				{
					tracer.addTag("action.crawlerMedical.EXCEPTION",taskInsurance.getTaskid());
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
			tracer.addTag("action.crawlerMedical.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			e.printStackTrace();
		}
		
	}
	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		
		try {
			int a=0;
			for (int i = 0; i < 3; i++) {
			WebParam<InsuranceHuaiAnEndowment> webParam = insuranceHuaiAnParser.crawlerEndowment(insuranceRequestParameters,taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerEndowment.success",taskInsurance.getTaskid());
					InsuranceHuaiAnHtml i1 = new InsuranceHuaiAnHtml();
					i1.setHtml(webParam.getHtml());
					i1.setPageCount(1);
					i1.setType("crawlerMedical");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHuaiAnRepositoryHtml.save(i1);
					insuranceHuaiAnRepositoryEndowment.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
					a++;
				}
				else
				{
					tracer.addTag("action.crawlerEndowment.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.crawlerEndowment.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			}
			}
			
			if(a>0)
			{
				tracer.addTag("action.crawlerEndowment.success2",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
			}
			else
			{
				tracer.addTag("action.crawlerEndowment.EXCEPTION2",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			}
			} catch (Exception e) {
				tracer.addTag("action.crawlerEndowment.TIMEOUT",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
				e.printStackTrace();
		}
		
		
	}
	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			int a=0;
			for (int i = 0; i < 3; i++) {
			WebParam<InsuranceHuaiAnInjury> webParam = insuranceHuaiAnParser.crawlerInjury(insuranceRequestParameters,taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerInjury.success",taskInsurance.getTaskid());
					InsuranceHuaiAnHtml i1 = new InsuranceHuaiAnHtml();
					i1.setHtml(webParam.getHtml());
					i1.setPageCount(1);
					i1.setType("crawlerInjury");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHuaiAnRepositoryHtml.save(i1);
					insuranceHuaiAnRepositoryInjury.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
					a++;
				}
				else
				{
					tracer.addTag("action.crawlerInjury.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.crawlerInjury.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			}
			}
			
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			}
			} catch (Exception e) {
				tracer.addTag("action.crawlerInjury.TIMEOUT",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
				e.printStackTrace();
		}
		
	}
	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {
			int a =0;
			for (int i = 0; i < 3; i++) {
			WebParam<InsuranceHuaiAnUnemployment> webParam = insuranceHuaiAnParser.crawlerUnemployment(insuranceRequestParameters,taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerUnemployment.success",taskInsurance.getTaskid());
					InsuranceHuaiAnHtml i1 = new InsuranceHuaiAnHtml();
					i1.setHtml(webParam.getHtml());
					i1.setPageCount(1);
					i1.setType("crawlerMedical");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHuaiAnRepositoryHtml.save(i1);
					insuranceHuaiAnRepositoryUnemployment.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
					a++;
				}
				else
				{
					tracer.addTag("action.crawlerUnemployment.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
				}
			}
			else
			{
				tracer.addTag("action.crawlerUnemployment.EXCEPTION",taskInsurance.getTaskid());
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
				tracer.addTag("action.crawlerUnemployment.TIMEOUT",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
				e.printStackTrace();
		}
		
	}
	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			int a =0;
			for (int i = 0; i < 3; i++) {
			WebParam<InsuranceHuaiAnMaternity> webParam = insuranceHuaiAnParser.crawlerMaternity(insuranceRequestParameters,taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerMaternity.success",taskInsurance.getTaskid());
					InsuranceHuaiAnHtml i1 = new InsuranceHuaiAnHtml();
					i1.setHtml(webParam.getHtml());
					i1.setPageCount(1);
					i1.setType("crawlerMedical");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHuaiAnRepositoryHtml.save(i1);
					insuranceHuaiAnRepositoryMaternity.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
					a++;
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
				tracer.addTag("action.crawlerMaternity.TIMEOUT",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
				e.printStackTrace();
		}
		
	}

}
