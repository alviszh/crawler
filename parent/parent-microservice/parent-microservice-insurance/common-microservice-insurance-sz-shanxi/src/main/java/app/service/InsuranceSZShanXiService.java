package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiEndowment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiHtml;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiInjury;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiMaternity;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiMedical;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiUnemployment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi.InsuranceSZShanXiRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi.InsuranceSZShanXiRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi.InsuranceSZShanXiRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi.InsuranceSZShanXiRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi.InsuranceSZShanXiRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi.InsuranceSZShanXiRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi.InsuranceSZShanXiRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZShanXiParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.sz.shanxi" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.sz.shanxi" })
public class InsuranceSZShanXiService {

	@Autowired
	private InsuranceSZShanXiParser insuranceSZShanXiParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZShanXiRepositoryHtml insuranceSZShanXiRepositoryHtml;
	@Autowired
	private InsuranceSZShanXiRepositoryUserInfo insuranceSZShanXiRepositoryUserInfo;
	@Autowired
	private InsuranceSZShanXiRepositoryEndowment insuranceSZShanXiRepositoryEndowment;
	@Autowired
	private InsuranceSZShanXiRepositoryInjury insuranceSZShanXiRepositoryInjury;
	@Autowired
	private InsuranceSZShanXiRepositoryMaternity insuranceSZShanXiRepositoryMaternity;
	@Autowired
	private InsuranceSZShanXiRepositoryMedical insuranceSZShanXiRepositoryMedical;
	@Autowired
	private InsuranceSZShanXiRepositoryUnemployment insuranceSZShanXiRepositoryUnemployment;

	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceSZShanXiParser.login(insuranceRequestParameters, taskInsurance);
			if(null !=webParam)
			{
				if(webParam.getHtml().contains("userinfo"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceSZShanXiHtml i = new InsuranceSZShanXiHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZShanXiRepositoryHtml.save(i);
					taskInsurance.setTesthtml(webParam.getHtml());//个人信息
					taskInsurance.setWebdriverHandle(webParam.getWebHandle());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
					taskInsuranceRepository.save(taskInsurance);
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

	//养老
	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a=5;
//			for (int i = 0; i < 6; i++) {
				WebParam<InsuranceSZShanXiEndowment> webParam = insuranceSZShanXiParser.crawlerEndowment(insuranceRequestParameters, taskInsurance, a);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
						InsuranceSZShanXiHtml j = new InsuranceSZShanXiHtml();
						j.setHtml(webParam.getHtml());
						j.setPageCount(1);
						j.setType("getEndowment");
						j.setTaskid(taskInsurance.getTaskid());
						j.setUrl(webParam.getUrl());
						insuranceSZShanXiRepositoryHtml.save(j);
						insuranceSZShanXiRepositoryEndowment.saveAll(webParam.getList());
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
//			}
//			
//			if(a>0)
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
//			}
//			else
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
//			}
		} catch (Exception e) {
			tracer.addTag("action.getEndowment.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			e.printStackTrace();

		}
	}

	//个人信息
	public void crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		WebParam<InsuranceSZShanXiUserInfo> webParam;
		try {
			webParam = insuranceSZShanXiParser.crawlerUserInfo(insuranceRequestParameters, taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceSZShanXiUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceSZShanXiHtml i = new InsuranceSZShanXiHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZShanXiRepositoryHtml.save(i);
					insuranceSZShanXiRepositoryUserInfo.save(webParam.getInsuranceSZShanXiUserInfo());
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
		}
		
		
	}

	
	//医疗
	public void crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a=5;
//			for (int i = 0; i < 6; i++) {
				WebParam<InsuranceSZShanXiMedical> webParam = insuranceSZShanXiParser.crawlerMedical(insuranceRequestParameters, taskInsurance, a);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
						InsuranceSZShanXiHtml j = new InsuranceSZShanXiHtml();
						j.setHtml(webParam.getHtml());
						j.setPageCount(1);
						j.setType("getMedical");
						j.setTaskid(taskInsurance.getTaskid());
						j.setUrl(webParam.getUrl());
						insuranceSZShanXiRepositoryHtml.save(j);
						insuranceSZShanXiRepositoryMedical.saveAll(webParam.getList());
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
//			}
//			
//			if(a>0)
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
//			}
//			else
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
//			}
		} catch (Exception e) {
			tracer.addTag("action.getMedical.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			e.printStackTrace();

		}		
	}

	
	//生育
	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a=5;
//			for (int i = 0; i < 6; i++) {
				WebParam<InsuranceSZShanXiMaternity> webParam = insuranceSZShanXiParser.crawlerMaternity(insuranceRequestParameters, taskInsurance, a);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.getMaternity.success",taskInsurance.getTaskid());
						InsuranceSZShanXiHtml j = new InsuranceSZShanXiHtml();
						j.setHtml(webParam.getHtml());
						j.setPageCount(1);
						j.setType("getMaternity");
						j.setTaskid(taskInsurance.getTaskid());
						j.setUrl(webParam.getUrl());
						insuranceSZShanXiRepositoryHtml.save(j);
						insuranceSZShanXiRepositoryMaternity.saveAll(webParam.getList());
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
//			}
//			
//			if(a>0)
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
//			}
//			else
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
//			}
		} catch (Exception e) {
			tracer.addTag("action.getMaternity.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			e.printStackTrace();

		}		
	}

	
	//工伤
	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {int a=5;
//			for (int i = 0; i < 6; i++) {
				WebParam<InsuranceSZShanXiInjury> webParam = insuranceSZShanXiParser.crawlerInjury(insuranceRequestParameters, taskInsurance, a);
				 if(null != webParam)
					{
						if(null != webParam.getList())
						{
							tracer.addTag("action.getInjury.success",taskInsurance.getTaskid());
							InsuranceSZShanXiHtml j = new InsuranceSZShanXiHtml();
							j.setHtml(webParam.getHtml());
							j.setPageCount(1);
							j.setType("getMedical");
							j.setTaskid(taskInsurance.getTaskid());
							j.setUrl(webParam.getUrl());
							insuranceSZShanXiRepositoryHtml.save(j);
							insuranceSZShanXiRepositoryInjury.saveAll(webParam.getList());
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
//			}
//			
//			if(a>0)
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
//			}
//			else
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
//			}
		} catch (Exception e) {
			tracer.addTag("action.getInjury.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			e.printStackTrace();

		}		
	}

	
	//失业
	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {int a=5;
//			for (int i = 0; i < 6; i++) {
				WebParam<InsuranceSZShanXiUnemployment> webParam = insuranceSZShanXiParser.crawlerUnemployment(insuranceRequestParameters, taskInsurance, a);
				 if(null != webParam)
					{
						if(null != webParam.getList())
						{
							tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
							InsuranceSZShanXiHtml j = new InsuranceSZShanXiHtml();
							j.setHtml(webParam.getHtml());
							j.setPageCount(1);
							j.setType("getUnemployment");
							j.setTaskid(taskInsurance.getTaskid());
							j.setUrl(webParam.getUrl());
							insuranceSZShanXiRepositoryHtml.save(j);
							insuranceSZShanXiRepositoryUnemployment.saveAll(webParam.getList());
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
//			}
//			
//			if(a>0)
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
//			}
//			else
//			{
//				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
//			}
		} catch (Exception e) {
			tracer.addTag("action.getUnemployment.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			e.printStackTrace();

		}		
	}

}
