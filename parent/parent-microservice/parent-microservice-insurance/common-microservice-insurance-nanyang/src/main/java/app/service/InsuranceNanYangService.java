package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangEndowment;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangHtml;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.nanyang.InsuranceNanYangRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.nanyang.InsuranceNanYangRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.nanyang.InsuranceNanYangRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceNanYangParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.nanyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.nanyang" })
public class InsuranceNanYangService {

	@Autowired
	private InsuranceNanYangParser insuranceNanYangParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceNanYangRepositoryHtml insuranceNanYangRepositoryHtml;
	@Autowired
	private InsuranceNanYangRepositoryUserInfo insuranceNanYangRepositoryUserInfo;
	@Autowired
	private InsuranceNanYangRepositoryEndowment insuranceNanYangRepositoryEndowment;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceNanYangUserInfo> webParam = insuranceNanYangParser.getUserInfo(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(null != webParam.getInsuranceNanYangUserInfo())
				{
					tracer.addTag("action.getUserInfo.success",taskInsurance.getTaskid());
					InsuranceNanYangHtml i = new InsuranceNanYangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanYangRepositoryHtml.save(i);
					insuranceNanYangRepositoryUserInfo.save(webParam.getInsuranceNanYangUserInfo());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getUserInfo.ERROR",taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
				}
			}
			else
			{
				tracer.addTag("action.getUserInfo.EXCEPTION",taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
			}
		} catch (Exception e) {
			tracer.addTag("action.getUserInfo.TIMEOUT",taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			e.printStackTrace();
		}
		
	}
	
	
	//养老
	public void getEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceNanYangEndowment> webParam = insuranceNanYangParser.getEndowment(insuranceRequestParameters,taskInsurance);
			if(null != webParam)
			{
				if(webParam.getList() != null)
				{
					tracer.addTag("action.getEndowment.success",taskInsurance.getTaskid());
					InsuranceNanYangHtml i = new InsuranceNanYangHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceNanYangRepositoryHtml.save(i);
					insuranceNanYangRepositoryEndowment.saveAll(webParam.getList());
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


	public void getMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
		
	}


	public void getUnemployment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
		
	}


	public void getMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
		
	}


	public void getInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
		
	}


	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceNanYangParser.login(insuranceRequestParameters,taskInsurance);
			if(webParam.getHtml().contains("success")){
				System.out.println("登录成功");
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				String logincookies = CommonUnit.transcookieToJson(webParam.getWebClient());
			    taskInsurance.setCookies(logincookies);
			    taskInsuranceRepository.save(taskInsurance);
//			    Thread.sleep(1000);  
			}else{
				if(webParam.getHtml().contains("用户不存在")){
					System.out.println("用户不存在");
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
							"用户不存在",taskInsurance);
				}else if(webParam.getHtml().contains("用户名或密码错误")){
					System.out.println("用户名或密码错误");
					tracer.addTag("登录信息有误：", "用户名或密码错误");
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
							InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
				}else{
					System.out.println("出现了其他登录错误："+webParam.getHtml());
					tracer.addTag("登录信息有误,详见验证登录信息之后返回的html", webParam.getHtml());
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
							"系统繁忙，请稍后再试！",taskInsurance);
				}
			}
		} catch (Exception e) {
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus(),
					InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getDescription(),taskInsurance);
			e.printStackTrace();
		}
	}

}
