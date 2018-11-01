package app.service;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
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
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoHtml;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoUserInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoYiLiaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoShiYeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoYangLaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoYiLiaoInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceJiaoZuoParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.jiaozuo" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.jiaozuo" })
public class InsuranceJiaoZuoServiceUnit {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceJiaoZuoParser insuranceJiaoZuoParser;
	@Autowired
	private InsuranceJiaoZuoUserInfoRepository insuranceJiaoZuoUserInfoRepository;
	@Autowired
	private InsuranceJiaoZuoYiLiaoInfoRepository insuranceJiaoZuoYiLiaoInfoRepository;
	@Autowired
	private InsuranceJiaoZuoYangLaoInfoRepository insuranceJiaoZuoYangLaoInfoRepository;
	@Autowired
	private InsuranceJiaoZuoShengYuInfoRepository insuranceJiaoZuoShengYuInfoRepository;
	@Autowired
	private InsuranceJiaoZuoGongShangInfoRepository insuranceJiaoZuoGongShangInfoRepository;
	@Autowired
	private InsuranceJiaoZuoShiYeInfoRepository insuranceJiaoZuoShiYeInfoRepository;
	@Autowired
	private InsuranceJiaoZuoHtmlRepository insuranceJiaoZuoHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	/**
	 * 个人信息
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getuserinfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonInsuredInfo?"
					+ "cardNumber="+parameter.getUserIDNum()
					+ "&name="+parameter.getName()
					+ "&insuranceCategory=%E5%85%BB%E8%80%81";//养老
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getObjData.client?"
					+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
//			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml = new InsuranceJiaoZuoHtml();
//			insuranceJiaoZuoHtml.setHtml(html2);
//			insuranceJiaoZuoHtml.setPagenumber(1);
//			insuranceJiaoZuoHtml.setTaskid(parameter.getTaskId());
//			insuranceJiaoZuoHtml.setType("个人信息");
//			insuranceJiaoZuoHtml.setUrl(url2);
//			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml);
			System.out.println(html2);
			if(html2!=null){
				InsuranceJiaoZuoUserInfo userinfo = insuranceJiaoZuoParser.getuserinfo(html2,taskInsurance.getTaskid());
				if(userinfo!=null){
					insuranceJiaoZuoUserInfoRepository.save(userinfo);
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
	 * 养老保险
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getyanglaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+parameter.getUserIDNum()
					+ "&name="+parameter.getName()
					+ "&insuranceCategory=%E5%85%BB%E8%80%81";//养老
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
					+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
//			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml = new InsuranceJiaoZuoHtml();
//			insuranceJiaoZuoHtml.setHtml(html2);
//			insuranceJiaoZuoHtml.setPagenumber(1);
//			insuranceJiaoZuoHtml.setTaskid(parameter.getTaskId());
//			insuranceJiaoZuoHtml.setType("养老保险缴费页面");
//			insuranceJiaoZuoHtml.setUrl(url2);
//			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml);
			if(html2!=null){
				List<InsuranceJiaoZuoYangLaoInfo> yanglao = insuranceJiaoZuoParser.getyanglaoMsg(html2,taskInsurance.getTaskid());
				if(yanglao!=null){
					insuranceJiaoZuoYangLaoInfoRepository.saveAll(yanglao);
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
			tracer.addTag("insurancejiaozuoService.crawler.getyanglaoinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYanglaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 医疗
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getyiliaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+parameter.getUserIDNum()
					+ "&name="+parameter.getName()
					+ "&insuranceCategory=%E5%8C%BB%E7%96%97";//医疗
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
					+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
//			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml = new InsuranceJiaoZuoHtml();
//			insuranceJiaoZuoHtml.setHtml(html2);
//			insuranceJiaoZuoHtml.setPagenumber(1);
//			insuranceJiaoZuoHtml.setTaskid(parameter.getTaskId());
//			insuranceJiaoZuoHtml.setType("医疗保险缴费页面");
//			insuranceJiaoZuoHtml.setUrl(url2);
//			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml);
			if(html2!=null){
				List<InsuranceJiaoZuoYiLiaoInfo> list = insuranceJiaoZuoParser.getyiliaomsg(html2,taskInsurance.getTaskid());
				if(list!=null){
					insuranceJiaoZuoYiLiaoInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceJiaoZuoService.crawler.getyiliaoinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYiliaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}

		return new AsyncResult<String>("200");
	}
	/***
	 * 工伤
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getgongshangMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+parameter.getUserIDNum()
					+ "&name="+parameter.getName()
					+ "&insuranceCategory=%e5%b7%a5%e4%bc%a4";//工伤
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
					+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
//			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml = new InsuranceJiaoZuoHtml();
//			insuranceJiaoZuoHtml.setHtml(html2);
//			insuranceJiaoZuoHtml.setPagenumber(1);
//			insuranceJiaoZuoHtml.setTaskid(parameter.getTaskId());
//			insuranceJiaoZuoHtml.setType("工伤保险缴费页面");
//			insuranceJiaoZuoHtml.setUrl(url2);
//			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml);
			if(html2!=null){
				List<InsuranceJiaoZuoGongShangInfo> list = insuranceJiaoZuoParser.getgongshangmsg(html2,taskInsurance.getTaskid());
				if(list!=null){
					insuranceJiaoZuoGongShangInfoRepository.saveAll(list);
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
	 * 失业
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getshiyeMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+parameter.getUserIDNum()
					+ "&name="+parameter.getName()
					+ "&insuranceCategory=%e5%a4%b1%e4%b8%9a";//生育
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
					+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
//			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml = new InsuranceJiaoZuoHtml();
//			insuranceJiaoZuoHtml.setHtml(html2);
//			insuranceJiaoZuoHtml.setPagenumber(1);
//			insuranceJiaoZuoHtml.setTaskid(parameter.getTaskId());
//			insuranceJiaoZuoHtml.setType("失业保险缴费页面");
//			insuranceJiaoZuoHtml.setUrl(url2);
//			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml);
			if(html2!=null){
				List<InsuranceJiaoZuoShiYeInfo> shiye = insuranceJiaoZuoParser.getshiyemsg(html2,taskInsurance.getTaskid());
				if(shiye!=null){
					insuranceJiaoZuoShiYeInfoRepository.saveAll(shiye);
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
			tracer.addTag("insuranceJiaoZuoService.crawler.getshiyeinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShiyeStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 生育
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getshengyuMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+parameter.getUserIDNum()
					+ "&name="+parameter.getName()
					+ "&insuranceCategory=%e7%94%9f%e8%82%b2";//生育
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
					+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
//			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml = new InsuranceJiaoZuoHtml();
//			insuranceJiaoZuoHtml.setHtml(html2);
//			insuranceJiaoZuoHtml.setPagenumber(1);
//			insuranceJiaoZuoHtml.setTaskid(parameter.getTaskId());
//			insuranceJiaoZuoHtml.setType("生育保险缴费页面");
//			insuranceJiaoZuoHtml.setUrl(url2);
//			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml);
			if(html2!=null){
				List<InsuranceJiaoZuoShengYuInfo> list = insuranceJiaoZuoParser.getshengyumsg(html2,taskInsurance.getTaskid());
				if(list!=null){
					insuranceJiaoZuoShengYuInfoRepository.saveAll(list);
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
