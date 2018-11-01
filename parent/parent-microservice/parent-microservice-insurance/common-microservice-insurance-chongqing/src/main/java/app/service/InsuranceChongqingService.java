package app.service;

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
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingHtml;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingCompanyRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingFirstRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.chongqing.InsuranceChongqingUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceChongqingParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.chongqing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.chongqing"})
public class InsuranceChongqingService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceChongqingParser insuranceChongqingParser;
	@Autowired
	private InsuranceChongqingUserInfoRepository insuranceChongqingUserInfoRepository;
	@Autowired
	private InsuranceChongqingCompanyRepository insuranceChongqingCompanyRepository;
	@Autowired
	private InsuranceChongqingFirstRepository insuranceChongqingFirstRepository;
	@Autowired
	private InsuranceChongqingHtmlRepository insuranceChongqingHtmlRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());	
		try {
			WebParam webParam = insuranceChongqingParser.getUserInfo(taskInsurance,cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getUserInfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceChongqingHtml insuranceChongqingHtml = new InsuranceChongqingHtml();
				insuranceChongqingHtml.setPageCount(1);
				insuranceChongqingHtml.setType("userInfo");
				insuranceChongqingHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceChongqingHtml.setUrl(webParam.getUrl());
				insuranceChongqingHtml.setHtml(html);
				insuranceChongqingHtmlRepository.save(insuranceChongqingHtml);	
				InsuranceChongqingUserInfo  userInfo=webParam.getInsuranceChongqingUserInfo();			
				if (null!=userInfo) {
					insuranceChongqingUserInfoRepository.save(userInfo);
					tracer.addTag("getUserinfo", "社保个人信息已入库");
					tracer.addTag("爬取的用户信息", userInfo.toString());
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}else{
					tracer.addTag("getUserinfo", "重庆社保个人信息未爬取到，无数据");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
					insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}		
				insuranceChongqingCompanyRepository.saveAll(webParam.getCompanyList());
				insuranceChongqingFirstRepository.saveAll(webParam.getFirstList());					
			}	
		} catch (Exception e) {
			tracer.addTag("action.getUserInfo.Exception",e.getMessage());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance); 
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			e.printStackTrace();
		}			
	}
}
