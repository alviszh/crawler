package app.service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnHtml;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnMedical;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jian.InsuranceJiAnHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.jian.InsuranceJiAnMedicalRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceJiAnMedicalParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jian"})
public class AsyncJiAnMedicalService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceJiAnMedicalParser insuranceJiAnMedicalParser;
	@Autowired
	private InsuranceJiAnMedicalRepository insuranceJiAnMedicalRepository;
	@Autowired
	private InsuranceJiAnHtmlRepository insuranceJiAnHtmlRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取医疗信息
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		tracer.addTag("getMedicalInfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Calendar calendar = Calendar.getInstance();
		int temp=0;
		for(int i=0;i<5;i++){
			String year = String.valueOf(calendar.get(Calendar.YEAR)-i);			
			WebParam webParam =insuranceJiAnMedicalParser.getMedicalInfo(taskInsurance, cookies, year);
			if (null !=webParam) {
				List<InsuranceJiAnMedical> medicalList=webParam.getMedicalList();
				if (null !=medicalList && !medicalList.isEmpty()) {
					insuranceJiAnMedicalRepository.saveAll(medicalList);	
					temp++;
					InsuranceJiAnHtml insuranceJiAnHtml = new InsuranceJiAnHtml();
					insuranceJiAnHtml.setPagenumber(1);
					insuranceJiAnHtml.setType("medicalInfo");
					insuranceJiAnHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceJiAnHtml.setUrl(webParam.getUrl());
					insuranceJiAnHtml.setHtml(webParam.getHtml());
					insuranceJiAnHtmlRepository.save(insuranceJiAnHtml);															
				}else{		
				  tracer.addTag("getMedicalInfo",insuranceRequestParameters.getTaskId()+"没有当前条件所对应的养老信息"+"查询年份"+year);	
				}					
			}
		}
		if (temp>0) {
			taskInsurance =insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);	
			taskInsurance=insuranceService.changeCrawlerStatusSuccess(taskInsurance);
		}else{
			taskInsurance =insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
			InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);		
			taskInsurance=insuranceService.changeCrawlerStatusSuccess(taskInsurance);
		}
		return taskInsurance;
	}
}
