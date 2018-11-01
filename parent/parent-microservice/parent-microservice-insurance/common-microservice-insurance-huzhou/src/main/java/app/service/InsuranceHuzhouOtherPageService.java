package app.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouHtml;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouRecords;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.huzhou.InsuranceHuzhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.huzhou.InsuranceHuzhouRecordsRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.InsuranceHuzhouHtmlunit;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huzhou"})
public class InsuranceHuzhouOtherPageService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHuzhouRecordsRepository insuranceHuzhouRecordsRepository;
	@Autowired
	private InsuranceHuzhouHtmlRepository insuranceHuzhouHtmlRepository;
	@Autowired
	private InsuranceHuzhouHtmlunit insuranceHuzhouHtmlunit;
	@Autowired
	private TracerLog tracer;	
	public WebParam getInsuranceOtherPageRecords(WebParam webParam,InsuranceRequestParameters insuranceRequestParameters,String type,int pageCount) throws Exception{		
		tracer.addTag("getInsuranceOtherPageRecords",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		webParam =insuranceHuzhouHtmlunit.getInsuranceRecordsOtherPage(webParam,taskInsurance, type, pageCount, cookies);
		List<InsuranceHuzhouRecords> recordsList = webParam.getRecordList();
		if (null != recordsList && !recordsList.isEmpty()) {
			insuranceHuzhouRecordsRepository.saveAll(recordsList);
			tracer.addTag("getInsuranceOtherPageRecords taskid="+insuranceRequestParameters.getTaskId()+" pageNum"+pageCount,recordsList.toString());
			InsuranceHuzhouHtml insuranceHuzhouHtml = new InsuranceHuzhouHtml();
			insuranceHuzhouHtml.setPageCount(pageCount);
			insuranceHuzhouHtml.setType("otherPageRecords");
			insuranceHuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceHuzhouHtml.setUrl(webParam.getUrl());
			insuranceHuzhouHtml.setHtml(webParam.getHtml());
			insuranceHuzhouHtmlRepository.save(insuranceHuzhouHtml);
		} else {
			tracer.addTag("getInsuranceOtherPageRecords",
					insuranceRequestParameters.getTaskId() + "没有当前条件所对应的养老信息" + "taskid" + insuranceRequestParameters.getTaskId());
		}
		return webParam;
	}	
}
