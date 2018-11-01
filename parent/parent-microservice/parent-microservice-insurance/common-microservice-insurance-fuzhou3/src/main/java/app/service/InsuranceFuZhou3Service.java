package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.fuzhou3.InsuranceFuZhou3Account;
import com.microservice.dao.entity.crawler.insurance.fuzhou3.InsuranceFuZhou3Html;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.fuzhou3.InsuranceFuZhou3AccountRepository;
import com.microservice.dao.repository.crawler.insurance.fuzhou3.InsuranceFuZhou3HtmlRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceFuZhou3Parser;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.fuzhou3" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.fuzhou3" })
public class InsuranceFuZhou3Service {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceFuZhou3Parser insuranceFuZhou3Parser;
	@Autowired
	private InsuranceFuZhou3AccountRepository insuranceFuZhou3AccountRepository;
	@Autowired
	private InsuranceFuZhou3HtmlRepository InsuranceFuZhou3HtmlRepository;
	
	@Autowired
	private TracerLog tracer;

	/**
	 * 更新task表（doing 正在登录状态）
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		JSONObject jsonObject = JSONObject.fromObject(insuranceRequestParameters);
		taskInsurance.setTesthtml(jsonObject.toString());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @Des 更新task表（doing 正在采集）
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}

	
	
	public void crawler(InsuranceRequestParameters insuranceRequestParameters) throws Exception  {
		tracer.addTag("InsuranceFuZhou3Service.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
		WebParam<InsuranceFuZhou3Account> webParam = insuranceFuZhou3Parser.crawler(insuranceRequestParameters);
		if(null!=webParam){
			tracer.addTag("InsuranceFuZhou3Service.crawler 社保账户信息", "社保账户信息已入库!");

			tracer.addTag("InsuranceFuZhou3Service.crawler:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceFuZhou3Html html = new InsuranceFuZhou3Html();
			html.setHtml(webParam.getHtml());
			html.setUrl(webParam.getUrl());
			InsuranceFuZhou3HtmlRepository.save(html);
			insuranceFuZhou3AccountRepository.save(webParam.getInsuranceFuzhou3Account());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
			tracer.addTag("InsuranceFuZhou3Service.crawler  社保账户信息", " 社保账户信息源码表入库!");
		}
	}
}
