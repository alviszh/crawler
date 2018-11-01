package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoCompInfo;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoHtml;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoMedical;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoPayGeneral;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoPension;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoUnemployment;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoUserInfo;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoCompInfoRepository;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoPayGeneralRepository;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoPensionRepository;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.qingdao.InsuranceQingdaoUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceQingdaoParser;
/**
 * @author sln
 * @date 2018年8月13日下午5:48:52
 * @Description: 参保信息可以通过改变参数一次性爬取完成
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.qingdao"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.qingdao"})
public class InsuranceQingdaoCrawlerService{
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceQingdaoHtmlRepository insuranceQingdaoHtmlRepository;
	@Autowired
	private InsuranceQingdaoCompInfoRepository insuranceQingdaoCompInfoRepository;
	@Autowired
	private InsuranceQingdaoMedicalRepository insuranceQingdaoMedicalRepository;
	@Autowired
	private InsuranceQingdaoPensionRepository insuranceQingdaoPensionRepository;
	@Autowired
	private InsuranceQingdaoUnemploymentRepository insuranceQingdaoUnemploymentRepository;
	@Autowired
	private InsuranceQingdaoPayGeneralRepository insuranceQingdaoPayGeneralRepository;
	@Autowired
	private InsuranceQingdaoUserInfoRepository insuranceQingdaoUserInfoRepository;
	@Autowired
	private InsuranceQingdaoParser insuranceQingdaoParser;
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance) {
		try {
			String url = "http://12333.qingdao.gov.cn/grcx2/work/f10010101/loadUserInfo.action";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);  //是post请求，官网上
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceQingdaoHtml insuranceQingdaoHtml = new InsuranceQingdaoHtml();
				insuranceQingdaoHtml.setTaskid(taskInsurance.getTaskid());
				insuranceQingdaoHtml.setType("用户信息源码页");
				insuranceQingdaoHtml.setPageCount(1);
				insuranceQingdaoHtml.setUrl(url);
				insuranceQingdaoHtml.setHtml(html);
				insuranceQingdaoHtmlRepository.save(insuranceQingdaoHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				InsuranceQingdaoUserInfo insuranceQingdaoUserInfo = insuranceQingdaoParser.userInfoParser(html,taskInsurance);
				if(null != insuranceQingdaoUserInfo){
					insuranceQingdaoUserInfoRepository.save(insuranceQingdaoUserInfo);
					tracer.addTag("action.crawler.getUserinfo", "个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(),200, taskInsurance);
				}
			}		
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(),201, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	@Async
	public Future<String> getPayGeneral(TaskInsurance taskInsurance) {
		try {
			String url = "http://12333.qingdao.gov.cn/grcx2/work/f10010301/loadCurrentPay.action";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requstBody="pageIndex=0&pageSize=10&sortField=&sortOrder=";
			webRequest.setRequestBody(requstBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceQingdaoHtml insuranceQingdaoHtml = new InsuranceQingdaoHtml();
				insuranceQingdaoHtml.setTaskid(taskInsurance.getTaskid());
				insuranceQingdaoHtml.setType("年度缴费信息源码页");
				insuranceQingdaoHtml.setPageCount(1);
				insuranceQingdaoHtml.setUrl(url);
				insuranceQingdaoHtml.setHtml(html);
				insuranceQingdaoHtmlRepository.save(insuranceQingdaoHtml);
				tracer.addTag("action.crawler.getPayGeneral.html", "年度缴费信息源码页已入库");
				List<InsuranceQingdaoPayGeneral> list =insuranceQingdaoParser.yearPayInfoParser(html,taskInsurance);
				if(null != list && list.size()>0){
					insuranceQingdaoPayGeneralRepository.saveAll(list);
					tracer.addTag("action.crawler.getPayGeneral", "年度缴费信息已入库");
				}
			}		
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPayGeneral.e", e.toString());
		}
		return new AsyncResult<String>("200");
	}
	@Async
	public Future<String> getCompInfo(TaskInsurance taskInsurance) {
		try {
			String url = "http://12333.qingdao.gov.cn/grcx2/work/f10010102/loadInsureInfo.action";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requstBody="pageIndex=0&pageSize=20&sortField=&sortOrder=";
			webRequest.setRequestBody(requstBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceQingdaoHtml insuranceQingdaoHtml = new InsuranceQingdaoHtml();
				insuranceQingdaoHtml.setTaskid(taskInsurance.getTaskid());
				insuranceQingdaoHtml.setType("公司信息源码页");
				insuranceQingdaoHtml.setPageCount(1);
				insuranceQingdaoHtml.setUrl(url);
				insuranceQingdaoHtml.setHtml(html);			
				insuranceQingdaoHtmlRepository.save(insuranceQingdaoHtml);
				tracer.addTag("action.crawler.getCompInfo.html", "公司信息源码页已入库");
				List<InsuranceQingdaoCompInfo> list=insuranceQingdaoParser.compInfoParser(html,taskInsurance);
				if(null!=list && list.size()>0){
					insuranceQingdaoCompInfoRepository.saveAll(list);
					tracer.addTag("action.crawler.getCompInfo", "公司信息已入库");
				}
			}		
		} catch (Exception e) {
			tracer.addTag("action.crawler.getCompInfo.e", e.toString());
		}
		return new AsyncResult<String>("200");
	}
	@Async
	public Future<String> getMedical(TaskInsurance taskInsurance)  {
		try {
			String url = "http://12333.qingdao.gov.cn/grcx2/work/f10010303/loadMedicalPay.action";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requstBody="pageIndex=0&pageSize=300&sortField=&sortOrder=";
			webRequest.setRequestBody(requstBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceQingdaoHtml insuranceQingdaoHtml = new InsuranceQingdaoHtml();
				insuranceQingdaoHtml.setTaskid(taskInsurance.getTaskid());
				insuranceQingdaoHtml.setType("医疗保险信息源码页");
				insuranceQingdaoHtml.setPageCount(1);
				insuranceQingdaoHtml.setUrl(url);
				insuranceQingdaoHtml.setHtml(html);
				insuranceQingdaoHtmlRepository.save(insuranceQingdaoHtml);
				tracer.addTag("action.crawler.getMedical.html", "医疗保险信息源码页已经入库");
				List<InsuranceQingdaoMedical> list = insuranceQingdaoParser.medicalParser(html,taskInsurance);
				if(null != list && list.size()>0){
					insuranceQingdaoMedicalRepository.saveAll(list);
					tracer.addTag("action.crawler.getMedical", "医疗保险信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}
			}		
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
	
		return new AsyncResult<String>("200");
	}
	@Async
	public Future<String> getPension(TaskInsurance taskInsurance)  {
		try {
			String url = "http://12333.qingdao.gov.cn/grcx2/work/f10010302/loadAgedPay.action";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requstBody="pageIndex=0&pageSize=300&sortField=&sortOrder=";
			webRequest.setRequestBody(requstBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceQingdaoHtml insuranceQingdaoHtml = new InsuranceQingdaoHtml();
				insuranceQingdaoHtml.setTaskid(taskInsurance.getTaskid());
				insuranceQingdaoHtml.setType("养老保险信息源码页");
				insuranceQingdaoHtml.setPageCount(1);
				insuranceQingdaoHtml.setUrl(url);
				insuranceQingdaoHtml.setHtml(html);
				insuranceQingdaoHtmlRepository.save(insuranceQingdaoHtml);
				tracer.addTag("action.crawler.getPension.html", "养老保险信息源码页已经入库");
				List<InsuranceQingdaoPension> list = insuranceQingdaoParser.pensionParser(html,taskInsurance);
				if(null != list && list.size()>0){
					insuranceQingdaoPensionRepository.saveAll(list);
					tracer.addTag("action.crawler.getPension", "养老保险信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							200, taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	@Async
	public Future<String> getUnemployment(TaskInsurance taskInsurance){
		try {
			String url = "http://12333.qingdao.gov.cn/grcx2/work/f10010304/loadUnemploymentPay.action";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requstBody="pageIndex=0&pageSize=300&sortField=&sortOrder=";
			webRequest.setRequestBody(requstBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceQingdaoHtml insuranceQingdaoHtml = new InsuranceQingdaoHtml();
				insuranceQingdaoHtml.setTaskid(taskInsurance.getTaskid());
				insuranceQingdaoHtml.setType("失业保险信息源码页");
				insuranceQingdaoHtml.setPageCount(1);
				insuranceQingdaoHtml.setUrl(url);
				insuranceQingdaoHtml.setHtml(html);
				insuranceQingdaoHtmlRepository.save(insuranceQingdaoHtml);
				tracer.addTag("action.crawler.getUnemployment.html", "失业保险信息源码页已经入库");
				List<InsuranceQingdaoUnemployment> list = insuranceQingdaoParser.unemploymentParser(html,taskInsurance);
				if(null!=list && list.size()>0){
					insuranceQingdaoUnemploymentRepository.saveAll(list);
					tracer.addTag("action.crawler.getUnemployment", "失业保险信息已入库");
					insuranceService.changeCrawlerStatus( InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
							200, taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUnemployment.e", e.toString());
			insuranceService.changeCrawlerStatus( InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
}
