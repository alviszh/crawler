package app.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
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
public class InsuranceHuzhouRecordsService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHuzhouRecordsRepository insuranceHuzhouRecordsRepository;
	@Autowired
	private InsuranceHuzhouHtmlRepository insuranceHuzhouHtmlRepository;
	@Autowired
	private InsuranceHuzhouHtmlunit insuranceHuzhouHtmlunit;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceHuzhouOtherPageService insuranceHuzhouOtherPageService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取社保信息
	 * @param insuranceRequestParameters
	 */
	public void getInsuranceRecords(InsuranceRequestParameters insuranceRequestParameters,String type){
		tracer.addTag("getPersionInfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		int temp=0;
		WebParam webParam;
		try {
			webParam = insuranceHuzhouHtmlunit.getInsuranceRecords(taskInsurance, type, cookies);
			if (null != webParam) {
				List<InsuranceHuzhouRecords> recordsList = webParam.getRecordList();
				if (null != recordsList && !recordsList.isEmpty()) {
					insuranceHuzhouRecordsRepository.saveAll(recordsList);
					tracer.addTag("getInsuranceRecords taskid=", insuranceRequestParameters.getTaskId()  
							+ recordsList.toString());
					temp++;
					InsuranceHuzhouHtml insuranceHuzhouHtml = new InsuranceHuzhouHtml();
					insuranceHuzhouHtml.setPageCount(1);
					insuranceHuzhouHtml.setType("persionInfo");
					insuranceHuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceHuzhouHtml.setUrl(webParam.getUrl());
					insuranceHuzhouHtml.setHtml(webParam.getHtml());
					insuranceHuzhouHtmlRepository.save(insuranceHuzhouHtml);
				} else {
					tracer.addTag("getInsuranceRecords", insuranceRequestParameters.getTaskId() + "没有对应的缴存信息" + "taskid"
							+ insuranceRequestParameters.getTaskId());
				}
				if (null !=webParam.getPageSum()  && Integer.parseInt(webParam.getPageSum()) > 1) {
					int pageCount= Integer.parseInt(webParam.getPageSum());
					for (int i = 2; i <= pageCount; i++) {	
					 webParam=insuranceHuzhouOtherPageService.getInsuranceOtherPageRecords(webParam,insuranceRequestParameters, type, i);
					}
				}
			}
	        if (temp>0) {      	
	        	if ("基本养老保险".endsWith(type)) {
	        		insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				}else if("失业保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				}else if("工伤保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				}else if("生育保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				}else if("基本医疗保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				}       	
	        	insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}else{
				if ("基本养老保险".endsWith(type)) {
	        		insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				}else if("失业保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				}else if("工伤保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				}else if("生育保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				}else if("基本医疗保险".endsWith(type)){
					insuranceService.changeCrawlerStatus(
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
	    					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				}      
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
		} catch (Exception e) {
			
			tracer.addTag("getInsuranceRecords Exception", e.toString());
			if ("基本养老保险".endsWith(type)) {
        		insuranceService.changeCrawlerStatus(
    					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
    					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			}else if("失业保险".endsWith(type)){
				insuranceService.changeCrawlerStatus(
    					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
    					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			}else if("工伤保险".endsWith(type)){
				insuranceService.changeCrawlerStatus(
    					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
    					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			}else if("生育保险".endsWith(type)){
				insuranceService.changeCrawlerStatus(
    					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
    					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			}else if("基本医疗保险".endsWith(type)){
				insuranceService.changeCrawlerStatus(
    					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
    					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			}  
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}
	}	
}
