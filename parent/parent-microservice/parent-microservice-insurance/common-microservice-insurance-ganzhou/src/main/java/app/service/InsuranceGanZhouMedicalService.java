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
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouBasicinfo;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouHtml;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.ganzhou.InsuranceGanZhouBasicinfoRepository;
import com.microservice.dao.repository.crawler.insurance.ganzhou.InsuranceGanZhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.ganzhou.InsuranceGanZhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGanZhouParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jian"})
public class InsuranceGanZhouMedicalService{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGanZhouUserInfoRepository insuranceGanZhouUserInfoRepository;
	@Autowired
	private InsuranceGanZhouHtmlRepository insuranceGanZhouHtmlRepository;
	@Autowired
	private InsuranceGanZhouParser insuranceGanZhouParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGanZhouBasicinfoRepository insuranceGanZhouBasicinfoRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters)  {
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		try {
			WebParam  webParam = insuranceGanZhouParser.getUserInfo(insuranceRequestParameters, cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getUserInfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceGanZhouHtml insuranceGanZhouHtml = new InsuranceGanZhouHtml();
				insuranceGanZhouHtml.setPageCount(1);
				insuranceGanZhouHtml.setType("userInfo");
				insuranceGanZhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceGanZhouHtml.setUrl(webParam.getUrl());
				insuranceGanZhouHtml.setHtml(html);
				insuranceGanZhouHtmlRepository.save(insuranceGanZhouHtml);	
				InsuranceGanZhouUserInfo  userInfo=webParam.getInsuranceGanZhouUserInfo();
				if (null !=userInfo) {
					insuranceGanZhouUserInfoRepository.save(userInfo);
					tracer.addTag("getUserinfo", "赣州医保个人信息已入库");
					taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}else{
					tracer.addTag("getUserinfo", "赣州医保个人信息未爬取到，无数据");
					taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}										
			}	
		} catch (Exception e) {
			tracer.addTag("getUserinfo.Exception", e.toString());
			taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}	
	}
	/**
	 * @Des 获取医疗信息
	 * @param insuranceRequestParameters
	 */
	public void getBasicinfo(InsuranceRequestParameters insuranceRequestParameters,String useraccount) {
		tracer.addTag("getBasicinfo",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());		
		try {
			WebParam webParam = insuranceGanZhouParser.getBasicinfo(insuranceRequestParameters, cookies, useraccount);
			if (null != webParam) {
				InsuranceGanZhouHtml insuranceGanZhouHtml = new InsuranceGanZhouHtml();
				insuranceGanZhouHtml.setPageCount(1);
				insuranceGanZhouHtml.setType("getBasicinfo");
				insuranceGanZhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceGanZhouHtml.setUrl(webParam.getUrl());
				insuranceGanZhouHtml.setHtml(webParam.getHtml());
				insuranceGanZhouHtmlRepository.save(insuranceGanZhouHtml);
				List<InsuranceGanZhouBasicinfo> basicinfoList = webParam.getBasicinfoList();
				if (null != basicinfoList && !basicinfoList.isEmpty()) {
					insuranceGanZhouBasicinfoRepository.saveAll(basicinfoList);
					tracer.addTag("getBasicinfo", insuranceRequestParameters.getTaskId() + "赣州医保保险信息已入库");
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				} else {
					tracer.addTag("getBasicinfo", insuranceRequestParameters.getTaskId() + "赣州医保没有当前条件所对应的基本保险信息");
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
			}
		} catch (Exception e) {
			tracer.addTag("getBasicinfo.Exception", e.toString());
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}
	 }


}
