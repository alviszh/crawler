package app.service;

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
import com.microservice.dao.entity.crawler.insurance.sz.anhui.InsuranceSZAnHuiHtml;
import com.microservice.dao.entity.crawler.insurance.sz.anhui.InsuranceSZAnHuiUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.anhui.InsuranceSZAnHuiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.sz.anhui.InsuranceSZAnHuiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceSZAnHuiHtmlunit;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.anhui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.anhui"})
public class InsuranceSZAnHuUserInfoService{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZAnHuiHtmlunit insuranceSZAnHuiHtmlunit;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZAnHuiUserInfoRepository insuranceSZAnHuiUserInfoRepository;
	@Autowired
	private InsuranceSZAnHuiHtmlRepository  insuranceSZAnHuiHtmlRepository;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam = insuranceSZAnHuiHtmlunit.getUserInfo(insuranceRequestParameters, cookies);
		String html=webParam.getHtml();
		tracer.addTag("getUserInfo",
				insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
		InsuranceSZAnHuiHtml insuranceSZAnHuiHtml = new InsuranceSZAnHuiHtml();
		insuranceSZAnHuiHtml.setPageCount(1);
		insuranceSZAnHuiHtml.setType("userInfo");
		insuranceSZAnHuiHtml.setTaskid(insuranceRequestParameters.getTaskId());
		insuranceSZAnHuiHtml.setUrl(webParam.getUrl());
		insuranceSZAnHuiHtml.setHtml(html);
		insuranceSZAnHuiHtmlRepository.save(insuranceSZAnHuiHtml);	
		InsuranceSZAnHuiUserInfo  userInfo=webParam.getInsuranceSZAnHuiUserInfo();
		if (null !=userInfo) {
			insuranceSZAnHuiUserInfoRepository.save(userInfo);
			tracer.addTag("getUserinfo", "安徽省直个人信息已入库");
			taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		}else if("请至少完成2个中级认证".contains(webParam.getMsgAlert())){
			tracer.addTag("getUserinfo", "安徽省直个人信息  用户未认证，未爬取到数据");	
			taskInsurance.setUserInfoStatus(201);
			taskInsuranceRepository.save(taskInsurance);	
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			taskInsurance.setDescription("用户未认证，未爬取到个人信息");
			taskInsuranceRepository.save(taskInsurance);	
			taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		}else{
			tracer.addTag("getUserinfo", "安徽省直个人信息失败");	
			taskInsurance.setUserInfoStatus(201);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());	
		}
	}
}
