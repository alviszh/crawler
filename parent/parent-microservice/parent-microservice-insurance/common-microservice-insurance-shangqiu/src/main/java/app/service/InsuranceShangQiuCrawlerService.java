package app.service;


import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shangqiu.InsuranceShangQiuHtml;
import com.microservice.dao.entity.crawler.insurance.shangqiu.InsuranceShangQiuUserInfo;
import com.microservice.dao.entity.crawler.insurance.shangqiu.InsuranceShangQiuYearPayInfo;
import com.microservice.dao.repository.crawler.insurance.shangqiu.InsuranceShangQiuHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.shangqiu.InsuranceShangQiuUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.shangqiu.InsuranceShangQiuYearPayInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceShangQiuParser;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shangqiu"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shangqiu"})
public class InsuranceShangQiuCrawlerService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceShangQiuCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceShangQiuHtmlRepository insuranceShangQiuHtmlRepository;
	@Autowired
	private InsuranceShangQiuUserInfoRepository insuranceShangQiuUserInfoRepository;
	@Autowired
	private InsuranceShangQiuYearPayInfoRepository insuranceShangQiuYearPayInfoRepository;
	@Autowired
	private InsuranceShangQiuParser insuranceShangQiuParser;
	@Value("${loginhost}")
	public String loginhost;
	
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance) {
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			webClient.getOptions().setJavaScriptEnabled(false);
			String html1=null;
			String html2=null;
			//个人权益维护单的信息
			String url="http://"+loginhost+"/siq/web/staff_disStaffInfo.action";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				html1=page.getWebResponse().getContentAsString();
				InsuranceShangQiuHtml insuranceShangQiuHtml = new InsuranceShangQiuHtml();
				insuranceShangQiuHtml.setTaskid(taskInsurance.getTaskid());
				insuranceShangQiuHtml.setType("个人权益维护单源码页");
				insuranceShangQiuHtml.setPagenumber(1);
				insuranceShangQiuHtml.setUrl(url);
				insuranceShangQiuHtml.setHtml(html1);
				insuranceShangQiuHtmlRepository.save(insuranceShangQiuHtml);
				tracer.addTag("action.crawler.getUserInfo.html.1", "个人权益维护单源码页已入库");
				url="http://"+loginhost+"/siq/web/staff_disStaffAddress.action";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				webClient.getOptions().setJavaScriptEnabled(false);
				page = webClient.getPage(webRequest);
				if(null!=page){
					html2=page.getWebResponse().getContentAsString();
					insuranceShangQiuHtml = new InsuranceShangQiuHtml();
					insuranceShangQiuHtml.setTaskid(taskInsurance.getTaskid());
					insuranceShangQiuHtml.setType("申报地址源码页");
					insuranceShangQiuHtml.setPagenumber(1);
					insuranceShangQiuHtml.setUrl(url);
					insuranceShangQiuHtml.setHtml(html2);
					insuranceShangQiuHtmlRepository.save(insuranceShangQiuHtml);
					tracer.addTag("action.crawler.getUserInfo.html.2", "申报地址源码页已入库");
					InsuranceShangQiuUserInfo insuranceShangQiuUserInfo=insuranceShangQiuParser.userInfoParser(taskInsurance,html1,html2);
					if(null!=insuranceShangQiuUserInfo){
						insuranceShangQiuUserInfoRepository.save(insuranceShangQiuUserInfo);
						tracer.addTag("action.crawler.getUserInfo", "全部个人权益维护单已入库");
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
								 200, taskInsurance);
					}
				}
				
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
	
		return new AsyncResult<String>("200");	
		
	}
	@Async
	public Future<String> getPension(TaskInsurance taskInsurance) {
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			webClient.getOptions().setJavaScriptEnabled(false);
			//历年缴费基数
			String url="http://218.28.196.73:33002/siq/web/staff_queryYpwilist.action";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceShangQiuHtml insuranceShangQiuHtml = new InsuranceShangQiuHtml();
				insuranceShangQiuHtml.setTaskid(taskInsurance.getTaskid());
				insuranceShangQiuHtml.setType("历年缴费基数源码页");
				insuranceShangQiuHtml.setPagenumber(1);
				insuranceShangQiuHtml.setUrl(url);
				insuranceShangQiuHtml.setHtml(html);
				insuranceShangQiuHtmlRepository.save(insuranceShangQiuHtml);
				tracer.addTag("action.crawler.getPension.html.1", "历年缴费基数源码页已入库");
				List<InsuranceShangQiuYearPayInfo> list=insuranceShangQiuParser.pensionParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceShangQiuYearPayInfoRepository.saveAll(list);
					tracer.addTag("action.crawler.getPension", "历年缴费基数已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}else{
					insuranceShangQiuYearPayInfoRepository.saveAll(list);
					tracer.addTag("action.crawler.getPension", "历年缴费基数已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
}
