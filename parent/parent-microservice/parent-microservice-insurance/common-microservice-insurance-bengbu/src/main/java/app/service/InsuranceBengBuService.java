package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuEndowment;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuHtml;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuInjury;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMaternity;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMedical;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUnemployment;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.bengbu.InsuranceBengBuRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.bengbu.InsuranceBengBuRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.bengbu.InsuranceBengBuRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.bengbu.InsuranceBengBuRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.bengbu.InsuranceBengBuRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.bengbu.InsuranceBengBuRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.bengbu.InsuranceBengBuRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceBengBuParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.bengbu" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.bengbu" })
public class InsuranceBengBuService{

	@Autowired
	private InsuranceBengBuParser insuranceBengBuParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceBengBuRepositoryEndowment insuranceBengBuRepositoryEndowment;
	@Autowired
	private InsuranceBengBuRepositoryHtml insuranceBengBuRepositoryHtml;
	@Autowired
	private InsuranceBengBuRepositoryInjury insuranceBengBuRepositoryInjury;
	@Autowired
	private InsuranceBengBuRepositoryMaternity insuranceBengBuRepositoryMaternity;
	@Autowired
	private InsuranceBengBuRepositoryMedical insuranceBengBuRepositoryMedical;
	@Autowired
	private InsuranceBengBuRepositoryUnemployment insuranceBengBuRepositoryUnemployment;
	@Autowired
	private InsuranceBengBuRepositoryUserInfo insuranceBengBuRepositoryUserInfo;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	
	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceBengBuParser.login(insuranceRequestParameters,taskInsurance);
			if(null !=webParam)
			{
				if(webParam.getHtml().contains("姓名"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceBengBuHtml i = new InsuranceBengBuHtml();
					i.setHtml(webParam.getHtml());
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceBengBuRepositoryHtml.save(i);
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
//		return taskInsurance;
	}
	
	//个人信息
	public void crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceBengBuUserInfo> webParam = insuranceBengBuParser.crawlerUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceBengBuUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceBengBuHtml i = new InsuranceBengBuHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceBengBuRepositoryHtml.save(i);
					insuranceBengBuRepositoryUserInfo.save(webParam.getInsuranceBengBuUserInfo());
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
			WebParam<InsuranceBengBuMedical> webParam = insuranceBengBuParser.crawlerMedical(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getMedical.success",taskInsurance.getTaskid());
					InsuranceBengBuHtml i = new InsuranceBengBuHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getMedical");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceBengBuRepositoryHtml.save(i);
					insuranceBengBuRepositoryMedical.saveAll(webParam.getList());
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
	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceBengBuEndowment> webParam = insuranceBengBuParser.crawlerEndowment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getEndowment.success",taskInsurance.getTaskid());
					InsuranceBengBuHtml i = new InsuranceBengBuHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getEndowment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceBengBuRepositoryHtml.save(i);
					insuranceBengBuRepositoryEndowment.saveAll(webParam.getList());
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

	//失业
	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceBengBuUnemployment> webParam = insuranceBengBuParser.crawlerUnemployment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getUnemployment.success",taskInsurance.getTaskid());
					InsuranceBengBuHtml i = new InsuranceBengBuHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getUnemployment");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceBengBuRepositoryHtml.save(i);
					insuranceBengBuRepositoryUnemployment.saveAll(webParam.getList());
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
	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceBengBuInjury> webParam = insuranceBengBuParser.crawlerInjury(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getInjury.success",taskInsurance.getTaskid());
					InsuranceBengBuHtml i = new InsuranceBengBuHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getInjury");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceBengBuRepositoryHtml.save(i);
					insuranceBengBuRepositoryInjury.saveAll(webParam.getList());
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
	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceBengBuMaternity> webParam = insuranceBengBuParser.crawlerMaternity(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerMaternity.success",taskInsurance.getTaskid());
					InsuranceBengBuHtml i = new InsuranceBengBuHtml();
					i.setHtml(webParam.getHtml());
					i.setType("crawlerMaternity");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceBengBuRepositoryHtml.save(i);
					insuranceBengBuRepositoryMaternity.saveAll(webParam.getList());
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


//	@Override
//	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance
//			) {
//		tracer.addTag("@Async getAllData", insuranceRequestParameters.toString());
//		tracer.addTag("taskid",insuranceRequestParameters.getTaskId());	
////		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		try{		
//			tracer.addTag("InsuranceBengBu getAllData Start",insuranceRequestParameters.getTaskId());			
//			crawlerUserInfo(insuranceRequestParameters,taskInsurance);	
//			crawlerMedical(insuranceRequestParameters,taskInsurance);	
//			crawlerEndowment(insuranceRequestParameters,taskInsurance);	
//			crawlerUnemployment(insuranceRequestParameters,taskInsurance);	
//			crawlerInjury(insuranceRequestParameters,taskInsurance);	
//			crawlerMaternity(insuranceRequestParameters,taskInsurance);	
//		}catch(Exception e){
//			tracer.addTag("EurekaCmccService getUserMessage",e.getMessage());
//			insuranceService.changeLoginStatusTimeOut(taskInsurance);
//		}
//		insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());	
//		tracer.addTag("InsuranceBengBu getAllData",taskInsurance.toString());
//		return taskInsurance;
//	}
//
//	@Override
//	public TaskInsurance getAllDataDone(String taskId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
