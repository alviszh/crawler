package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shiyan.InsuranceShiYanHtml;
import com.microservice.dao.entity.crawler.insurance.shiyan.InsuranceShiYanMedical;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shiyan.InsuranceShiYanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.shiyan.InsuranceShiYanMedicalRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceFuShiYanParser;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.shiyan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.shiyan" })
public class InsuranceShiYanService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceFuShiYanParser insuranceShiYanParser;
	@Autowired
	private InsuranceShiYanMedicalRepository   medicalRepository;
	@Autowired
	private InsuranceShiYanHtmlRepository shiyanHtmlRepository;
	
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
		tracer.addTag("InsuranceShiYanService.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
		WebParam<InsuranceShiYanMedical> webParam = insuranceShiYanParser.crawler(insuranceRequestParameters);
		if(null!=webParam){
			tracer.addTag("InsuranceShiYanService.crawler 医保个人账户信息", "医保个人账户信息已入库!");

			tracer.addTag("InsuranceShiYanService.crawler:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceShiYanHtml html = new InsuranceShiYanHtml();
			html.setHtml(webParam.getHtml());
			html.setUrl(webParam.getUrl());
			shiyanHtmlRepository.save(html);
			medicalRepository.save(webParam.getInsuranceShiYanMedical());
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
			tracer.addTag("InsuranceShiYanService.crawler  医保个人账户信息", " 医保个人信息源码表入库!");
		}
	}
}
