package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXiHtml;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXiUserInfo;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXibasictypes;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXipaydetails;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.guangxi.InsuranceSZGuangXiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.sz.guangxi.InsuranceSZGuangXiUserInfoRespository;
import com.microservice.dao.repository.crawler.insurance.sz.guangxi.InsuranceSZGuangXibasictypesRepository;
import com.microservice.dao.repository.crawler.insurance.sz.guangxi.InsuranceSZGuangXipaydetailsRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceSZGuangXiParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.guangxi"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.guangxi"})
public class InsuranceGuangXiFatureService {

	@Autowired
	private InsuranceSZGuangXipaydetailsRepository  insuranceSZGuangXipaydetailsRepository;
	@Autowired
	private InsuranceSZGuangXibasictypesRepository  insuranceSZGuangXibasictypesRepository;
	@Autowired
	private InsuranceSZGuangXiUserInfoRespository  insuranceSZGuangXiUserInfoRespository;
	@Autowired
	private InsuranceSZGuangXiHtmlRepository insuranceSZGuangXiHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZGuangXiParser insuranceSZGuangXiParser;
	@Autowired
	private InsuranceService insuranceService; 
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters)  {
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		try {
			WebParam webParam = insuranceSZGuangXiParser.getUserInfoUnit(insuranceRequestParameters, cookies);
			if(null != webParam){
				String html=webParam.getHtml();
				tracer.addTag("getUserInfo",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				InsuranceSZGuangXiHtml insuranceSZGuangXiHtml = new InsuranceSZGuangXiHtml();
				insuranceSZGuangXiHtml.setPagenumber(1);
				insuranceSZGuangXiHtml.setType("userinfo");
				insuranceSZGuangXiHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceSZGuangXiHtml.setUrl(webParam.getUrl());
				insuranceSZGuangXiHtml.setHtml(webParam.getHtml());
				insuranceSZGuangXiHtmlRepository.save(insuranceSZGuangXiHtml);
				InsuranceSZGuangXiUserInfo  userInfo=webParam.getUserInfo();
				if (null !=userInfo) {
					insuranceSZGuangXiUserInfoRespository.save(userInfo);
					tracer.addTag("getUserinfo", "个人信息已入库");
					taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance); 
				    insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());	
				}else{
					tracer.addTag("getUserinfo", "个人信息未爬取到，无数据");
					taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				    insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());	
				}										
			}	
		} catch (Exception e) {
			taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
		    insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());	
			tracer.addTag("getUserInfo Exception", e.toString());
			e.printStackTrace();
		}
	}
	/**
	 * @Des 获取医疗信息
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getBasicinfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getBasicinfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		try {
			WebParam webParam = insuranceSZGuangXiParser.getBasicinfoUnit(insuranceRequestParameters, cookies);
			InsuranceSZGuangXiHtml insuranceSZGuangXiHtml = new InsuranceSZGuangXiHtml();
			insuranceSZGuangXiHtml.setPagenumber(1);
			insuranceSZGuangXiHtml.setType("basicinfo");
			insuranceSZGuangXiHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceSZGuangXiHtml.setUrl(webParam.getUrl());
			insuranceSZGuangXiHtml.setHtml(webParam.getHtml());
			insuranceSZGuangXiHtmlRepository.save(insuranceSZGuangXiHtml);
			List<InsuranceSZGuangXibasictypes> basicinfoList = webParam.getBasictypeList();
			if (null != basicinfoList && !basicinfoList.isEmpty()) {
				insuranceSZGuangXibasictypesRepository.saveAll(basicinfoList);
				tracer.addTag("getBasicinfo返回结果", insuranceRequestParameters.getTaskId() + "保险种类信息已入库");
			} else {
				tracer.addTag("getBasicinfo返回结果", insuranceRequestParameters.getTaskId() + "没有对应保险种类信息");
			}
		} catch (Exception e) {
			tracer.addTag("getBasicinfo.Exception", e.toString());
	  }		
	}	
	/**
	 * @Des 获取缴费明细
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getPaydetails(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("getPaydetails", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		try {
			int temp = 0;
			for (int i = 0; i < 60; i++) {
				String month = getDateBefore("yyyyMM", i);
				WebParam webParam = insuranceSZGuangXiParser.getPaydetailsUnit(insuranceRequestParameters, cookies, month);
				if (null != webParam) {
					InsuranceSZGuangXiHtml insuranceSZGuangXiHtml = new InsuranceSZGuangXiHtml();
					insuranceSZGuangXiHtml.setPagenumber(1);
					insuranceSZGuangXiHtml.setType("paydetail" + month);
					insuranceSZGuangXiHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceSZGuangXiHtml.setUrl(webParam.getUrl());
					insuranceSZGuangXiHtml.setHtml(webParam.getHtml());
					insuranceSZGuangXiHtmlRepository.save(insuranceSZGuangXiHtml);
					List<InsuranceSZGuangXipaydetails> paydetailsList = webParam.getPaydetailsList();
					if (null != paydetailsList && !paydetailsList.isEmpty()) {
						insuranceSZGuangXipaydetailsRepository.saveAll(paydetailsList);
						temp++;
						tracer.addTag("getPaydetails" + month, insuranceRequestParameters.getTaskId() + "保险缴费明细已经入库");
					} else {
						tracer.addTag("getPaydetails" + month,
								insuranceRequestParameters.getTaskId() + "没有当前条件所对应的缴费明细信息");
					}
				}
			}	
			if (temp > 0) {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			}else{
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			}
		} catch (Exception e) {
			tracer.addTag("getPaydetails.Exception", e.toString());
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			e.printStackTrace();		
		}
	}
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
	public static WebClient addcookie(String cookiesIn) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookiesIn);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
}
