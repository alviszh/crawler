package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanEndowment;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanHtml;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanMedical;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.yunnan.InsuranceSZYunNanRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.sz.yunnan.InsuranceSZYunNanRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.sz.yunnan.InsuranceSZYunNanRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.sz.yunnan.InsuranceSZYunNanRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZYunNanParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.yunnan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.yunnan" })
public class InsuranceSZYunNanService {

	@Autowired
	private InsuranceSZYunNanParser insuranceSZYunNanParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceSZYunNanRepositoryMedical insuranceSZYunNanRepositoryMedical;
	@Autowired
	private InsuranceSZYunNanRepositoryUserInfo insuranceSZYunNanRepositoryUserInfo;
	@Autowired
	private InsuranceSZYunNanRepositoryHtml insuranceSZYunNanRepositoryHtml;
	@Autowired
	private InsuranceSZYunNanRepositoryEndowment insuranceSZYunNanRepositoryEndowment;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	public void MedicalLogin(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceSZYunNanParser.login(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("医保个人账户权益信息"))
				{
					tracer.addTag("action.MedicalLogin.success",taskInsurance.getTaskid());
					InsuranceSZYunNanHtml i = new InsuranceSZYunNanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("MedicalLogin");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZYunNanRepositoryHtml.save(i);
					taskInsuranceRepository.save(taskInsurance);
					insuranceService.changeLoginStatusSuccessHttp(taskInsurance,webParam.getHtml());
				}
				else
				{
					tracer.addTag("action.MedicalLogin.ERROR",taskInsurance.getTaskid());
					insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
				}
			}
			else
			{
				tracer.addTag("action.MedicalLogin.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeLoginStatusException(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.MedicalLogin.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeLoginStatusTimeOut(taskInsurance);
			e.printStackTrace();
		}
		
	}
	

	//养老登陆
	public void EndowmentLogin(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceSZYunNanParser.EndowmentLogin(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("养老保险个人账户权益信息"))
				{
					tracer.addTag("action.EndowmentLogin.success",taskInsurance.getTaskid());
					InsuranceSZYunNanHtml i = new InsuranceSZYunNanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("EndowmentLogin");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZYunNanRepositoryHtml.save(i);
					taskInsurance.setTesthtml(webParam.getHtml());
					taskInsuranceRepository.save(taskInsurance);
					insuranceService.changeCrawlerStatus("养老登陆成功", InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase(), 200, taskInsurance);
				}
				else
				{
					tracer.addTag("action.EndowmentLogin.ERROR",taskInsurance.getTaskid());
					insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
				}
			}
			else
			{
				tracer.addTag("action.EndowmentLogin.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeLoginStatusException(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.EndowmentLogin.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeLoginStatusTimeOut(taskInsurance);
			e.printStackTrace();
		}
		
	}
	
	
	//医疗
	public void crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceSZYunNanMedical> webParam = insuranceSZYunNanParser.crawlerMedical(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
					InsuranceSZYunNanHtml i = new InsuranceSZYunNanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getMedical");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZYunNanRepositoryHtml.save(i);
					insuranceSZYunNanRepositoryMedical.saveAll(webParam.getList());
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
	
	//个人信息
	public void crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceSZYunNanUserInfo> webParam = insuranceSZYunNanParser.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceSZYunNanUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceSZYunNanHtml i = new InsuranceSZYunNanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZYunNanRepositoryHtml.save(i);
					insuranceSZYunNanRepositoryUserInfo.save(webParam.getInsuranceSZYunNanUserInfo());
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
	
	
	//工伤
	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		taskInsurance.setGongshangStatus(201);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS);

		
	}
	
	//养老
	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceSZYunNanEndowment> webParam = insuranceSZYunNanParser.crawlerEndowment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerEndowment.success",taskInsurance.getTaskid());
					InsuranceSZYunNanHtml i = new InsuranceSZYunNanHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("crawlerEndowment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceSZYunNanRepositoryHtml.save(i);
					insuranceSZYunNanRepositoryEndowment.saveAll(webParam.getList());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
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
		} catch (Exception e) {
			tracer.addTag("action.crawlerEndowment.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			e.printStackTrace();
		}

		
	}
	
	//生育
	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		taskInsurance.setShengyuStatus(201);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS);

		
	}
	
	//失业
	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		taskInsurance.setShiyeStatus(201);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS);

		
	}


}
