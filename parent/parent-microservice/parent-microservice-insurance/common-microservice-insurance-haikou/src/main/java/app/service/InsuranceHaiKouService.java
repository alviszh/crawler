package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouEndowment;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouHtml;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouInjury;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouMaternity;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouMedical;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouUnemployment;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.haikou.InsuranceHaiKouRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.haikou.InsuranceHaiKouRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.haikou.InsuranceHaiKouRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.haikou.InsuranceHaiKouRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.haikou.InsuranceHaiKouRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.haikou.InsuranceHaiKouRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.haikou.InsuranceHaiKouRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceHaiKouParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.haikou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.haikou" })
public class InsuranceHaiKouService {

	@Autowired
	private InsuranceHaiKouParser insuranceHaiKouParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceHaiKouRepositoryEndowment insuranceHaiKouRepositoryEndowment;
	@Autowired
	private InsuranceHaiKouRepositoryHtml insuranceHaiKouRepositoryHtml;
	@Autowired
	private InsuranceHaiKouRepositoryInjury insuranceHaiKouRepositoryInjury;
	@Autowired
	private InsuranceHaiKouRepositoryMaternity insuranceHaiKouRepositoryMaternity;
	@Autowired
	private InsuranceHaiKouRepositoryMedical insuranceHaiKouRepositoryMedical;
	@Autowired
	private InsuranceHaiKouRepositoryUnemployment insuranceHaiKouRepositoryUnemployment;
	@Autowired
	private InsuranceHaiKouRepositoryUserInfo insuranceHaiKouRepositoryUserInfo;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	//登陆
	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceHaiKouParser.login(insuranceRequestParameters,taskInsurance);if(null != webParam)
			{
				if(webParam.getHtml().contains("海南省人力资源和社会保障网上大厅"))
				{
					tracer.addTag("action.login.success",taskInsurance.getTaskid());
					InsuranceHaiKouHtml i = new InsuranceHaiKouHtml();
					i.setHtml(webParam.getHtml());
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceHaiKouRepositoryHtml.save(i);
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
	@Async
	public void crawlerUser(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceHaiKouUserInfo> webParam = insuranceHaiKouParser.crawelerUser(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceHaiKouUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceHaiKouHtml i = new InsuranceHaiKouHtml();
					i.setHtml(webParam.getHtml());
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceHaiKouRepositoryHtml.save(i);
					insuranceHaiKouRepositoryUserInfo.save(webParam.getInsuranceHaiKouUserInfo());
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
	@Async
	public void crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
			try {int a=0;
				for (int i = 0; i < 10; i++) {
				WebParam<InsuranceHaiKouMedical> webParam = insuranceHaiKouParser.crawelerMedical(insuranceRequestParameters, taskInsurance,i);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.crawlerMedical.success",taskInsurance.getTaskid());
						InsuranceHaiKouHtml i1 = new InsuranceHaiKouHtml();
						i1.setHtml(webParam.getHtml());
						i1.setType("crawlerMedical");
						i1.setTaskid(taskInsurance.getTaskid());
						i1.setUrl(webParam.getUrl());
						insuranceHaiKouRepositoryHtml.save(i1);
						insuranceHaiKouRepositoryMedical.saveAll(webParam.getList());
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

	//养老
	@Async
	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			int a=0;
			for (int i = 0; i < 10; i++) {
			WebParam<InsuranceHaiKouEndowment> webParam = insuranceHaiKouParser.crawlerEndowment(insuranceRequestParameters, taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerEndowment.success",taskInsurance.getTaskid());
					InsuranceHaiKouHtml i1 = new InsuranceHaiKouHtml();
					i1.setHtml(webParam.getHtml());
					i1.setType("crawlerMedical");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHaiKouRepositoryHtml.save(i1);
					insuranceHaiKouRepositoryEndowment.saveAll(webParam.getList());
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
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawlerEndowment.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			e.printStackTrace();
		}
		
	}

	
	//工伤
	@Async
	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			int a=0;
			for (int i = 0; i < 10; i++) {
			WebParam<InsuranceHaiKouInjury> webParam = insuranceHaiKouParser.crawlerInjury(insuranceRequestParameters, taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerInjury.success",taskInsurance.getTaskid());
					InsuranceHaiKouHtml i1 = new InsuranceHaiKouHtml();
					i1.setHtml(webParam.getHtml());
					i1.setType("crawlerInjury");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHaiKouRepositoryHtml.save(i1);
					insuranceHaiKouRepositoryInjury.saveAll(webParam.getList());
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

	//失业
	@Async
	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		try {
			int a=0;
			for (int i = 0; i < 10; i++) {
			WebParam<InsuranceHaiKouUnemployment> webParam = insuranceHaiKouParser.crawlerUnemployment(insuranceRequestParameters, taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerUnemployment.success",taskInsurance.getTaskid());
					InsuranceHaiKouHtml i1 = new InsuranceHaiKouHtml();
					i1.setHtml(webParam.getHtml());
					i1.setType("crawlerMedical");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHaiKouRepositoryHtml.save(i1);
					insuranceHaiKouRepositoryUnemployment.saveAll(webParam.getList());
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

	
	//生育
	@Async
	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			int a=0;
			for (int i = 0; i < 10; i++) {
			WebParam<InsuranceHaiKouMaternity> webParam = insuranceHaiKouParser.crawlerMaternity(insuranceRequestParameters, taskInsurance,i);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerMaternity.success",taskInsurance.getTaskid());
					InsuranceHaiKouHtml i1 = new InsuranceHaiKouHtml();
					i1.setHtml(webParam.getHtml());
					i1.setType("crawlerMedical");
					i1.setTaskid(taskInsurance.getTaskid());
					i1.setUrl(webParam.getUrl());
					insuranceHaiKouRepositoryHtml.save(i1);
					insuranceHaiKouRepositoryMaternity.saveAll(webParam.getList());
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
