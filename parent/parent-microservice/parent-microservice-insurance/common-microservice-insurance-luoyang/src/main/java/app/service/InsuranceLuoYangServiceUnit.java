package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangHtml;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangUserInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangYiLiaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.luoyang.InsuranceLuoYangGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.luoyang.InsuranceLuoYangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.luoyang.InsuranceLuoYangShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.luoyang.InsuranceLuoYangShiYeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.luoyang.InsuranceLuoYangUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.luoyang.InsuranceLuoYangYangLaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.luoyang.InsuranceLuoYangYiLiaoInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceLuoYangParser;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.luoyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.luoyang" })
public class InsuranceLuoYangServiceUnit{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceLuoYangParser insuranceLuoYangParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceLuoYangUserInfoRepository insuranceLuoYangUserInfoRepository;
	@Autowired
	private InsuranceLuoYangYangLaoInfoRepository insuranceLuoYangYangLaoInfoRepository;
	@Autowired
	private InsuranceLuoYangYiLiaoInfoRepository insuranceLuoYangYiLiaoInfoRepository;
	@Autowired
	private InsuranceLuoYangGongShangInfoRepository insuranceLuoYangGongShangInfoRepository;
	@Autowired
	private InsuranceLuoYangShengYuInfoRepository insuranceLuoYangShengYuInfoRepository;
	@Autowired
	private InsuranceLuoYangShiYeInfoRepository insuranceLuoYangShiYeInfoRepository;
	@Autowired
	private InsuranceLuoYangHtmlRepository insuranceLuoYangHtmlRepository;
	/** 
	 * 个人信息
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient 
	 * @return 
	 */
	public Future<String> getuserinfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance, WebClient webClient) {

		try {
			tracer.addTag("insuranceluoyangService.crawler.getuserinfo", taskInsurance.getTaskid());
			String url = "http://gr.ly12333.com/grpt/zgbx/zgbxJbxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceLuoYangHtml insuranceLuoYangHtml = new InsuranceLuoYangHtml();
			insuranceLuoYangHtml.setHtml(html);
			insuranceLuoYangHtml.setPagenumber(1);
			insuranceLuoYangHtml.setTaskid(parameter.getTaskId());
			insuranceLuoYangHtml.setType("个人信息");
			insuranceLuoYangHtml.setUrl(url);
			insuranceLuoYangHtmlRepository.save(insuranceLuoYangHtml);
			
			if(html.indexOf("成功！")!=-1){
				InsuranceLuoYangUserInfo insuranceluoyangUserInfo = insuranceLuoYangParser.getuserinfo(parameter.getTaskId(),html);
				if(null!=insuranceluoyangUserInfo){
					insuranceLuoYangUserInfoRepository.save(insuranceluoyangUserInfo);
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
				taskInsurance.setUserInfoStatus(500);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getuserinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 养老保险个人缴费流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getyanglaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			tracer.addTag("insuranceLuoYangService.crawler.getyanglaoMsg", taskInsurance.getTaskid());
			String url = "http://gr.ly12333.com/grpt/zgbx/zgbxYlbxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceLuoYangHtml insuranceLuoYangHtml = new InsuranceLuoYangHtml();
			insuranceLuoYangHtml.setHtml(html);
			insuranceLuoYangHtml.setPagenumber(1);
			insuranceLuoYangHtml.setTaskid(parameter.getTaskId());
			insuranceLuoYangHtml.setType("养老保险个人缴费流水");
			insuranceLuoYangHtml.setUrl(url);
			insuranceLuoYangHtmlRepository.save(insuranceLuoYangHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceLuoYangYangLaoInfo> list = insuranceLuoYangParser.getyanglaoMsg(html,parameter.getTaskId());
				if(list!=null){
					insuranceLuoYangYangLaoInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceLuoYangService.crawler.getyanglaoinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYanglaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 医疗保险个人缴费流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getyiliaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			tracer.addTag("insuranceLuoYangService.crawler.getyiliaoMsg", taskInsurance.getTaskid());
			String url2 = "http://gr.ly12333.com/grpt/zgbx/zgbxMlbxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceLuoYangHtml insuranceLuoYangHtml = new InsuranceLuoYangHtml();
			insuranceLuoYangHtml.setHtml(html);
			insuranceLuoYangHtml.setPagenumber(1);
			insuranceLuoYangHtml.setTaskid(parameter.getTaskId());
			insuranceLuoYangHtml.setType("医疗保险个人缴费流水");
			insuranceLuoYangHtml.setUrl(url2);
			insuranceLuoYangHtmlRepository.save(insuranceLuoYangHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceLuoYangYiLiaoInfo> list = insuranceLuoYangParser.getyiliaoMsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceLuoYangYiLiaoInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYiliaoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYiliaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
				taskInsurance.setYiliaoStatus(500);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getyiliaoinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYiliaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 工伤保险个人缴费流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getgongshangMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			tracer.addTag("insuranceLuoYangService.crawler.getgongshangMsg", taskInsurance.getTaskid());
			String url2 = "http://gr.ly12333.com/grpt/zgbx/zgbxGsbxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceLuoYangHtml insuranceLuoYangHtml = new InsuranceLuoYangHtml();
			insuranceLuoYangHtml.setHtml(html);
			insuranceLuoYangHtml.setPagenumber(1);
			insuranceLuoYangHtml.setTaskid(parameter.getTaskId());
			insuranceLuoYangHtml.setType("工伤保险个人缴费流水");
			insuranceLuoYangHtml.setUrl(url2);
			insuranceLuoYangHtmlRepository.save(insuranceLuoYangHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceLuoYangGongShangInfo> list = insuranceLuoYangParser.getgongshangmsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceLuoYangGongShangInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
				taskInsurance.setGongshangStatus(500);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getgongshanginfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
			taskInsurance.setGongshangStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 失业保险个人缴费流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getshiyeMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			tracer.addTag("insuranceLuoYangService.crawler.getshiyeMsg", taskInsurance.getTaskid());
			String url2 = "http://gr.ly12333.com/grpt/zgbx/zgbxSybxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceLuoYangHtml insuranceLuoYangHtml = new InsuranceLuoYangHtml();
			insuranceLuoYangHtml.setHtml(html);
			insuranceLuoYangHtml.setPagenumber(1);
			insuranceLuoYangHtml.setTaskid(parameter.getTaskId());
			insuranceLuoYangHtml.setType("失业保险个人缴费流水");
			insuranceLuoYangHtml.setUrl(url2);
			insuranceLuoYangHtmlRepository.save(insuranceLuoYangHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceLuoYangShiYeInfo> list = insuranceLuoYangParser.getshiyemsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceLuoYangShiYeInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceLuoYangService.crawler.getshiyeinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShiyeStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 生育保险个人缴费流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getshengyuMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			tracer.addTag("insuranceLuoYangService.crawler.getshengyuMsg", taskInsurance.getTaskid());
			String url2 = "http://gr.ly12333.com/grpt/zgbx/zgbxMybxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceLuoYangHtml insuranceLuoYangHtml = new InsuranceLuoYangHtml();
			insuranceLuoYangHtml.setHtml(html);
			insuranceLuoYangHtml.setPagenumber(1);
			insuranceLuoYangHtml.setTaskid(parameter.getTaskId());
			insuranceLuoYangHtml.setType("生育保险个人缴费流水");
			insuranceLuoYangHtml.setUrl(url2);
			insuranceLuoYangHtmlRepository.save(insuranceLuoYangHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceLuoYangShengYuInfo> list = insuranceLuoYangParser.getshengyuMsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceLuoYangShengYuInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
					taskInsurance.setShengyuStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
					taskInsurance.setShengyuStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
				taskInsurance.setShengyuStatus(500);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getshiyeinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
			taskInsurance.setShengyuStatus(404);
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
	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
