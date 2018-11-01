package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouHtml;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouInjury;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouMedical;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zhangzhou.InsuranceZhangZhouRepositoryEndowment;
import com.microservice.dao.repository.crawler.insurance.zhangzhou.InsuranceZhangZhouRepositoryHtml;
import com.microservice.dao.repository.crawler.insurance.zhangzhou.InsuranceZhangZhouRepositoryInjury;
import com.microservice.dao.repository.crawler.insurance.zhangzhou.InsuranceZhangZhouRepositoryMaternity;
import com.microservice.dao.repository.crawler.insurance.zhangzhou.InsuranceZhangZhouRepositoryMedical;
import com.microservice.dao.repository.crawler.insurance.zhangzhou.InsuranceZhangZhouRepositoryUnemployment;
import com.microservice.dao.repository.crawler.insurance.zhangzhou.InsuranceZhangZhouRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceZhangZhouParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.zhangzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.zhangzhou" })
public class InsuranceZhangZhouService {

	@Autowired
	private InsuranceZhangZhouParser insuranceZhangZhouParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceZhangZhouRepositoryEndowment insuranceZhangZhouRepositoryEndowment;
	@Autowired
	private InsuranceZhangZhouRepositoryHtml insuranceZhangZhouRepositoryHtml;
	@Autowired
	private InsuranceZhangZhouRepositoryInjury insuranceZhangZhouRepositoryInjury;
	@Autowired
	private InsuranceZhangZhouRepositoryMaternity insuranceZhangZhouRepositoryMaternity;
	@Autowired
	private InsuranceZhangZhouRepositoryMedical insuranceZhangZhouRepositoryMedical;
	@Autowired
	private InsuranceZhangZhouRepositoryUnemployment insuranceZhangZhouRepositoryUnemployment;
	@Autowired
	private InsuranceZhangZhouRepositoryUserInfo insuranceZhangZhouRepositoryUserInfo;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;

	public void login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam webParam = insuranceZhangZhouParser.login(insuranceRequestParameters, taskInsurance);
			if (null != webParam) {
				if (webParam.getHtml().contains("姓名")) {
					tracer.addTag("action.login.success", taskInsurance.getTaskid());
					InsuranceZhangZhouHtml i = new InsuranceZhangZhouHtml();
					i.setHtml(webParam.getHtml());
					i.setType("login");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceZhangZhouRepositoryHtml.save(i);
					taskInsuranceRepository.save(taskInsurance);
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					insuranceService.changeLoginStatusSuccessHttp(taskInsurance, cookieString);
				} else {
					tracer.addTag("action.login.ERROR", taskInsurance.getTaskid());
					insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
				}
			} else {
				tracer.addTag("action.login.EXCEPTION", taskInsurance.getTaskid());
				insuranceService.changeLoginStatusException(taskInsurance);
			}

		} catch (Exception e) {
			tracer.addTag("action.login.TIMEOUT", taskInsurance.getTaskid());
			insuranceService.changeLoginStatusTimeOut(taskInsurance);
			e.printStackTrace();
		}
	}

	// 个人信息
	public void crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			WebParam<InsuranceZhangZhouUserInfo> webParam = insuranceZhangZhouParser
					.crawlerUserInfo(insuranceRequestParameters, taskInsurance);
			if (null != webParam) {
				if (null != webParam.getInsuranceZhangZhouUserInfo()) {
					tracer.addTag("action.getUserInfo.success", taskInsurance.getTaskid());
					InsuranceZhangZhouHtml i = new InsuranceZhangZhouHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskInsurance.getTaskid());
					i.setUrl(webParam.getUrl());
					insuranceZhangZhouRepositoryHtml.save(i);
					insuranceZhangZhouRepositoryUserInfo.save(webParam.getInsuranceZhangZhouUserInfo());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
							InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
				} else {
					tracer.addTag("action.getUserInfo.ERROR", taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
							InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
				}
			} else {
				tracer.addTag("action.getUserInfo.EXCEPTION", taskInsurance.getTaskid());
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getUserInfo.TIMEOUT", taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
			e.printStackTrace();
		}

	}

	// 养老
	public void crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {

		try {int a=0;
			for (int i = 0; i < 5; i++) {
				WebParam<InsuranceZhangZhouEndowment> webParam = insuranceZhangZhouParser
						.crawlerEndowment(insuranceRequestParameters, taskInsurance, i);
				if (null != webParam) {
					if (null != webParam.getList()) {
						tracer.addTag("action.getUserInfo.success", taskInsurance.getTaskid());
						InsuranceZhangZhouHtml in = new InsuranceZhangZhouHtml();
						in.setHtml(webParam.getHtml());
						in.setPageCount(1);
						in.setType("getEndowment");
						in.setTaskid(taskInsurance.getTaskid());
						in.setUrl(webParam.getUrl());
						insuranceZhangZhouRepositoryHtml.save(in);
						insuranceZhangZhouRepositoryEndowment.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
						a++;
					} else {
						tracer.addTag("action.getEndowment.ERROR", taskInsurance.getTaskid());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
					}
				} else {
					tracer.addTag("action.getEndowment.EXCEPTION", taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
							InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
				}
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getEndowment.TIMEOUT", taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			e.printStackTrace();
		}

	}

	// 医疗
	public void crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {

		try {int a=0;
			for (int i = 0; i < 5; i++) {
				WebParam<InsuranceZhangZhouMedical> webParam = insuranceZhangZhouParser
						.crawlerMedical(insuranceRequestParameters, taskInsurance, i);
				if (null != webParam) {
					if (null != webParam.getList()) {
						tracer.addTag("action.getMedical.success", taskInsurance.getTaskid());
						InsuranceZhangZhouHtml in = new InsuranceZhangZhouHtml();
						in.setHtml(webParam.getHtml());
						in.setPageCount(1);
						in.setType("getMedical");
						in.setTaskid(taskInsurance.getTaskid());
						in.setUrl(webParam.getUrl());
						insuranceZhangZhouRepositoryHtml.save(in);
						insuranceZhangZhouRepositoryMedical.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
						a++;
					} else {
						tracer.addTag("action.getMedical.ERROR", taskInsurance.getTaskid());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
					}
				} else {
					tracer.addTag("action.getMedical.EXCEPTION", taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
							InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
				}
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getMedical.TIMEOUT", taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			e.printStackTrace();
		}

	}

	// 生育
	public void crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {

		try {int a=0;
			for (int i = 0; i < 5; i++) {
				WebParam<InsuranceZhangZhouMaternity> webParam = insuranceZhangZhouParser
						.crawlerMaternity(insuranceRequestParameters, taskInsurance, i);
				if (null != webParam) {
					if (null != webParam.getList()) {
						tracer.addTag("action.getMaternity.success", taskInsurance.getTaskid());
						InsuranceZhangZhouHtml in = new InsuranceZhangZhouHtml();
						in.setHtml(webParam.getHtml());
						in.setPageCount(1);
						in.setType("getMaternity");
						in.setTaskid(taskInsurance.getTaskid());
						in.setUrl(webParam.getUrl());
						insuranceZhangZhouRepositoryHtml.save(in);
						insuranceZhangZhouRepositoryMaternity.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
						a++;
					} else {
						tracer.addTag("action.getMaternity.ERROR", taskInsurance.getTaskid());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
					}
				} else {
					tracer.addTag("action.getMaternity.EXCEPTION", taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
							InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
				}
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			}
		} catch (Exception e) {
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
		}
	}

	// 工伤
	public void crawlerInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {

		try {int a=0;
			for (int i = 0; i < 5; i++) {
				WebParam<InsuranceZhangZhouInjury> webParam = insuranceZhangZhouParser
						.crawlerInjury(insuranceRequestParameters, taskInsurance, i);
				if (null != webParam) {
					if (null != webParam.getList()) {
						tracer.addTag("action.getInjury.success", taskInsurance.getTaskid());
						InsuranceZhangZhouHtml in = new InsuranceZhangZhouHtml();
						in.setHtml(webParam.getHtml());
						in.setPageCount(1);
						in.setType("getMedical");
						in.setTaskid(taskInsurance.getTaskid());
						in.setUrl(webParam.getUrl());
						insuranceZhangZhouRepositoryHtml.save(in);
						insuranceZhangZhouRepositoryInjury.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
						a++;
					} else {
						tracer.addTag("action.getInjury.ERROR", taskInsurance.getTaskid());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
					}
				} else {
					tracer.addTag("action.getInjury.EXCEPTION", taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
							InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
				}
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			}
		} catch (Exception e) {
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
		}
	}

	// 失业
	public void crawlerUnemployment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {

		try {int a=0;
			for (int i = 0; i < 5; i++) {
				WebParam<InsuranceZhangZhouUnemployment> webParam = insuranceZhangZhouParser
						.crawlerUnemployment(insuranceRequestParameters, taskInsurance, i);
				if (null != webParam) {
					if (null != webParam.getList()) {
						tracer.addTag("action.getUnemployment.success", taskInsurance.getTaskid());
						InsuranceZhangZhouHtml in = new InsuranceZhangZhouHtml();
						in.setHtml(webParam.getHtml());
						in.setPageCount(1);
						in.setType("getUnemployment");
						in.setTaskid(taskInsurance.getTaskid());
						in.setUrl(webParam.getUrl());
						insuranceZhangZhouRepositoryHtml.save(in);
						insuranceZhangZhouRepositoryUnemployment.saveAll(webParam.getList());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
						a++;
					} else {
						tracer.addTag("action.getUnemployment.ERROR", taskInsurance.getTaskid());
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
								InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
					}
				} else {
					tracer.addTag("action.getUnemployment.EXCEPTION", taskInsurance.getTaskid());
					insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
							InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
				}
			}
			if(a>0)
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
			}
			else
			{
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			}
		} catch (Exception e) {
			tracer.addTag("action.getUnemployment.TIMEOUT", taskInsurance.getTaskid());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			e.printStackTrace();
		}

	}
}
