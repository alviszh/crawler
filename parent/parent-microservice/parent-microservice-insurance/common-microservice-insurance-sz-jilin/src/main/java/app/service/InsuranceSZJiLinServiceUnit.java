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

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinPaymentBase;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinSoldout;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinStatusChanges;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinUserInfo;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinYangLaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.jilin.InsuranceSZJiLinPaymentBaseRepository;
import com.microservice.dao.repository.crawler.insurance.sz.jilin.InsuranceSZJiLinShiYeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.sz.jilin.InsuranceSZJiLinSoldoutRepository;
import com.microservice.dao.repository.crawler.insurance.sz.jilin.InsuranceSZJiLinStatusChangesRepository;
import com.microservice.dao.repository.crawler.insurance.sz.jilin.InsuranceSZJiLinUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.sz.jilin.InsuranceSZJiLinYangLaoInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZJiLinParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.sz.jilin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.sz.jilin" })
public class InsuranceSZJiLinServiceUnit {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZJiLinParser insuranceSZJiLinParser;
	@Autowired
	private InsuranceSZJiLinUserInfoRepository insuranceSZJiLinUserInfoRepository;
	@Autowired
	private InsuranceSZJiLinPaymentBaseRepository insuranceSZJiLinPaymentBaseRepository;
	@Autowired
	private InsuranceSZJiLinSoldoutRepository insuranceSZJiLinSoldoutRepository;
	@Autowired
	private InsuranceSZJiLinStatusChangesRepository insuranceSZJiLinStatusChangesRepository;
	@Autowired
	private InsuranceSZJiLinYangLaoInfoRepository insuranceSZJiLinYangLaoInfoRepository;
	@Autowired
	private InsuranceSZJiLinShiYeInfoRepository insuranceSZJiLinShiYeInfoRepository;
	/***
	 * 个人基本信息
	 * @param webClient
	 * @param taskInsurance
	 * @param parameter
	 * @return
	 */
	@Async
	public Future<String> getUserInfo(WebClient webClient, TaskInsurance taskInsurance, InsuranceRequestParameters parameter){
		try {
			String url = "https://wssb.jlsi.gov.cn:8443/grzx/grzxCbxxcx.do";
			Page page = getHtml(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			if(html!=null){
				InsuranceSZJiLinUserInfo userInfo = insuranceSZJiLinParser.getUserInfo(html,parameter);
				if(userInfo!=null){
					insuranceSZJiLinUserInfoRepository.save(userInfo);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskInsurance.setUserInfoStatus(201);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.baishan.getuserinfo", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(500);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}


	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	/**
	 * 缴费基数
	 * @param webClient
	 * @param taskInsurance
	 * @param parameter
	 */
	public Future<String> getPaymentBase(WebClient webClient, TaskInsurance taskInsurance, InsuranceRequestParameters parameter) {

		try {
			String url3 = "https://wssb.jlsi.gov.cn:8443/grzx/grzxJfjscx.do";
			Page page4 = getHtml(url3, webClient);
			String html = page4.getWebResponse().getContentAsString();
			if(html!=null){
				List<InsuranceSZJiLinPaymentBase> paymentBase = insuranceSZJiLinParser.getPaymentBase(html,parameter);
				if(paymentBase!=null){
					insuranceSZJiLinPaymentBaseRepository.saveAll(paymentBase);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("数据采集中，【缴费基数】已采集成功");
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("数据采集中，【缴费基数】已采集完成");
					taskInsurance.setError_message("缴费基数无数据");
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription("数据采集中，【缴费基数】已采集失败");
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.baishan.getPaymentBase", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription("数据采集中，【缴费基数】已采集失败");
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 个人缴费断档信息
	 * @param webClient
	 * @param taskInsurance
	 * @param parameter
	 * @return
	 */
	public Future<String> getSoldout(WebClient webClient, TaskInsurance taskInsurance, InsuranceRequestParameters parameter) {
		try {
			String url4 = "https://wssb.jlsi.gov.cn:8443/grzx/grddjf.do";
			Page page5 = getHtml(url4, webClient);
			String html = page5.getWebResponse().getContentAsString();
			if(html!=null){
				List<InsuranceSZJiLinSoldout> soldout = insuranceSZJiLinParser.getSoldout(html,parameter);
				if(soldout!=null){
					insuranceSZJiLinSoldoutRepository.saveAll(soldout);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("数据采集中，【个人缴费断档信息】已采集成功");
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("数据采集中，【个人缴费断档信息】已采集完成");
					taskInsurance.setError_message("个人缴费断档信息无数据");
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription("数据采集中，【个人缴费断档信息】已采集失败");
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.baishan.getSoldout", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription("数据采集中，【个人缴费断档信息】已采集失败");
			taskInsuranceRepository.save(taskInsurance);
		}

		return new AsyncResult<String>("200");

	}
	/**
	 * 缴费状态变更记录
	 * @param webClient
	 * @param taskInsurance
	 * @param parameter
	 */
	public Future<String> getStatusChanges(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters parameter) {
		try {
			String url2 = "https://wssb.jlsi.gov.cn:8443/dwcx/txbbgjlcxAction!querylist.do";
			Page page3 = getHtml(url2, webClient);
			String html = page3.getWebResponse().getContentAsString();
			if(html!=null){
				List<InsuranceSZJiLinStatusChanges> statuschanges = insuranceSZJiLinParser.getStatusChanges(html,parameter);
				if(statuschanges!=null){
					insuranceSZJiLinStatusChangesRepository.saveAll(statuschanges);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("数据采集中，【缴费状态变更记录】已采集成功");
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("数据采集中，【缴费状态变更记录】已采集完成");
					taskInsurance.setError_message("缴费状态变更记录无数据");
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription("数据采集中，【缴费状态变更记录】已采集失败");
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.baishan.getStatusChanges", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription("数据采集中，【缴费状态变更记录】已采集失败");
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 养老保险缴纳明细
	 * @param webClient
	 * @param taskInsurance
	 * @param parameter
	 * @return
	 */
	public Future<String> getyanglaoInfo(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters parameter) {
		try {
			String url5 = "https://wssb.jlsi.gov.cn:8443/grzx/grzxJfmxcx.do";
			Page page3 = getHtml(url5, webClient);
			String html = page3.getWebResponse().getContentAsString();
			if(html!=null){
				List<InsuranceSZJiLinYangLaoInfo> statuschanges = insuranceSZJiLinParser.getyanglaoInfo(html,parameter);
				if(statuschanges!=null){
					insuranceSZJiLinYangLaoInfoRepository.saveAll(statuschanges);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
				taskInsurance.setYanglaoStatus(500);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.baishan.getyanglaoInfo", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYanglaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
/**
 * 失业
 * @param webClient
 * @param taskInsurance
 * @param parameter
 * @return
 */
	public Future<String> getshiyeInfo(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters parameter) {
		try {
			String url2 = "https://wssb.jlsi.gov.cn:8443/grzx/grzxSybxjfmxcx.do";
			Page page3 = getHtml(url2, webClient);
			String html = page3.getWebResponse().getContentAsString();
			if(html!=null){
				List<InsuranceSZJiLinShiYeInfo> statuschanges = insuranceSZJiLinParser.getshiyeInfo(html,parameter);
				if(statuschanges!=null){
					insuranceSZJiLinShiYeInfoRepository.saveAll(statuschanges);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
					taskInsurance.setShiyeStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
					taskInsurance.setShiyeStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
				taskInsurance.setShiyeStatus(500);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShiyeStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		
		return new AsyncResult<String>("200");
	}

	public Future<String> getinsurance(TaskInsurance taskInsurance, InsuranceRequestParameters parameter) {
		taskInsurance.setGongshangStatus(201);
		taskInsurance.setShengyuStatus(201);
		taskInsurance.setYiliaoStatus(201);
		taskInsuranceRepository.save(taskInsurance);
		return new AsyncResult<String>("200");
	}
}
