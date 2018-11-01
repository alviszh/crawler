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
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanHtml;
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanUserInfo;
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanYiLiaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanShiYeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanYangLaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanYiLiaoInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceJiYuanParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.jiyuan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.jiyuan" })
public class InsuranceJiYuanServiceUnit{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceJiYuanParser insuranceJiYuanParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceJiYuanUserInfoRepository insuranceJiYuanUserInfoRepository;
	@Autowired
	private InsuranceJiYuanYangLaoInfoRepository insuranceJiYuanYangLaoInfoRepository;
	@Autowired
	private InsuranceJiYuanYiLiaoInfoRepository insuranceJiYuanYiLiaoInfoRepository;
	@Autowired
	private InsuranceJiYuanGongShangInfoRepository insuranceJiYuanGongShangInfoRepository;
	@Autowired
	private InsuranceJiYuanShengYuInfoRepository insuranceJiYuanShengYuInfoRepository;
	@Autowired
	private InsuranceJiYuanShiYeInfoRepository insuranceJiYuanShiYeInfoRepository;
	@Autowired
	private InsuranceJiYuanHtmlRepository insuranceJiYuanHtmlRepository;
	/** 
	 * 个人信息
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient 
	 * @return 
	 */
	public Future<String> getuserinfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance, WebClient webClient) {

		try {
			tracer.addTag("insuranceJiYuanService.crawler.getuserinfo", taskInsurance.getTaskid());
			String url = "http://221.13.152.238:9000/grpt/zgbx/zgbxJbxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceJiYuanHtml insuranceJiYuanHtml = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml.setHtml(html);
			insuranceJiYuanHtml.setPagenumber(1);
			insuranceJiYuanHtml.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml.setType("个人信息");
			insuranceJiYuanHtml.setUrl(url);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml);
			
			if(html.indexOf("成功！")!=-1){
				InsuranceJiYuanUserInfo insuranceJiYuanUserInfo = insuranceJiYuanParser.getuserinfo(parameter.getTaskId(),html);
				if(null!=insuranceJiYuanUserInfo){
					insuranceJiYuanUserInfoRepository.save(insuranceJiYuanUserInfo);
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
			tracer.addTag("insuranceJiYuanService.crawler.getuserinfo.error", e.getMessage());
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
			tracer.addTag("insuranceJiYuanService.crawler.getyanglaoMsg", taskInsurance.getTaskid());
			String url = "http://221.13.152.238:9000/grpt/zgbx/zgbxYlbxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceJiYuanHtml insuranceJiYuanHtml = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml.setHtml(html);
			insuranceJiYuanHtml.setPagenumber(1);
			insuranceJiYuanHtml.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml.setType("养老保险个人缴费流水");
			insuranceJiYuanHtml.setUrl(url);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceJiYuanYangLaoInfo> list = insuranceJiYuanParser.getyanglaoMsg(html,parameter.getTaskId());
				if(list!=null){
					insuranceJiYuanYangLaoInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceJiYuanService.crawler.getyanglaoinfo.error", e.getMessage());
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
			tracer.addTag("insuranceJiYuanService.crawler.getyiliaoMsg", taskInsurance.getTaskid());
			String url2 = "http://221.13.152.238:9000/grpt/zgbx/zgbxMlbxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceJiYuanHtml insuranceJiYuanHtml = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml.setHtml(html);
			insuranceJiYuanHtml.setPagenumber(1);
			insuranceJiYuanHtml.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml.setType("医疗保险个人缴费流水");
			insuranceJiYuanHtml.setUrl(url2);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceJiYuanYiLiaoInfo> list = insuranceJiYuanParser.getyiliaoMsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceJiYuanYiLiaoInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceJiYuanService.crawler.getyiliaoinfo.error", e.getMessage());
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
			tracer.addTag("insuranceJiYuanService.crawler.getgongshangMsg", taskInsurance.getTaskid());
			String url2 = "http://221.13.152.238:9000/grpt/zgbx/zgbxGsbxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceJiYuanHtml insuranceJiYuanHtml = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml.setHtml(html);
			insuranceJiYuanHtml.setPagenumber(1);
			insuranceJiYuanHtml.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml.setType("工伤保险个人缴费流水");
			insuranceJiYuanHtml.setUrl(url2);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceJiYuanGongShangInfo> list = insuranceJiYuanParser.getgongshangmsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceJiYuanGongShangInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceJiYuanService.crawler.getgongshanginfo.error", e.getMessage());
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
			tracer.addTag("insuranceJiYuanService.crawler.getshiyeMsg", taskInsurance.getTaskid());
			String url2 = "http://221.13.152.238:9000/grpt/zgbx/zgbxSybxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceJiYuanHtml insuranceJiYuanHtml = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml.setHtml(html);
			insuranceJiYuanHtml.setPagenumber(1);
			insuranceJiYuanHtml.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml.setType("失业保险个人缴费流水");
			insuranceJiYuanHtml.setUrl(url2);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceJiYuanShiYeInfo> list = insuranceJiYuanParser.getshiyemsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceJiYuanShiYeInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceJiYuanService.crawler.getshiyeinfo.error", e.getMessage());
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
			tracer.addTag("insuranceJiYuanService.crawler.getshengyuMsg", taskInsurance.getTaskid());
			String url2 = "http://221.13.152.238:9000/grpt/zgbx/zgbxMybxjfxxcxAction001.action";
			Page page2 = gethtmlPost(webClient, null, url2);
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			//网页信息
			InsuranceJiYuanHtml insuranceJiYuanHtml = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml.setHtml(html);
			insuranceJiYuanHtml.setPagenumber(1);
			insuranceJiYuanHtml.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml.setType("生育保险个人缴费流水");
			insuranceJiYuanHtml.setUrl(url2);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml);
			
			if(html.indexOf("成功！")!=-1){
				List<InsuranceJiYuanShengYuInfo> list = insuranceJiYuanParser.getshengyuMsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceJiYuanShengYuInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceJiYuanService.crawler.getshiyeinfo.error", e.getMessage());
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
