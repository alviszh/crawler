package app.service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanGongShang;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanShengYu;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanShiYe;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanUserInfo;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanYanglao;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanYiLiao;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.leshan.InsuranceLeShanGongShangRepository;
import com.microservice.dao.repository.crawler.insurance.leshan.InsuranceLeShanShengYuRepository;
import com.microservice.dao.repository.crawler.insurance.leshan.InsuranceLeShanShiYeRepository;
import com.microservice.dao.repository.crawler.insurance.leshan.InsuranceLeShanUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.leshan.InsuranceLeShanYangLaoRepository;
import com.microservice.dao.repository.crawler.insurance.leshan.InsuranceLeShanYiLiaoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceLeShanParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.leshan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.leshan" })
public class insuranceLeShanServiceUnit {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceLeShanUserInfoRepository insuranceLeShanUserInfoRepository;
	@Autowired
	private InsuranceLeShanYangLaoRepository insuranceLeShanYangLaoRepository;
	@Autowired
	private InsuranceLeShanYiLiaoRepository insuranceLeShanYiLiaoRepository;
	@Autowired
	private InsuranceLeShanGongShangRepository insuranceLeShanGongShangRepository;
	@Autowired
	private InsuranceLeShanShengYuRepository insuranceLeShanShengYuRepository;
	@Autowired
	private InsuranceLeShanShiYeRepository insuranceLeShanShiYeRepository;
	@Autowired
	private InsuranceLeShanParser insuranceLeShanParser;
	public Future<String> getuserinfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			tracer.addTag("getuserinfo.service", parameter.getTaskId());
			String userurl = "http://www.scls.lss.gov.cn:8888/lswtqt/117372/Q2025.jspx";
			Page page3 = getHtml(userurl, webClient);
			String contentAsString = page3.getWebResponse().getContentAsString();
			System.out.println(contentAsString);

			if(contentAsString.indexOf("fieldData")!=-1){
				InsuranceLeShanUserInfo user = insuranceLeShanParser.getuserinfo(parameter.getTaskId(),contentAsString);
				if(null!=user){
					insuranceLeShanUserInfoRepository.save(user);
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
			tracer.addTag("getuserinfo.service.error", parameter.getTaskId());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		//		webClient.setJavaScriptTimeout(50000); 
		//		webClient.getOptions().setTimeout(50000); // 15->60 
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

	public Future<String> getyanglaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			LocalDate today = LocalDate.now();
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1);
			String monthint = stardate.getMonthValue() + "";
			if (monthint.length() < 2) {
				monthint = "0" + monthint;
			}
			String month =  stardate.getYear() + monthint;

			String ylurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=110"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e="+month
					+ "&notkeyflag=0";
			Page page4 = gethtmlPost(webClient, null, ylurl);
			String contentAsString2 = page4.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			if(contentAsString2.indexOf("lists")!=-1){
				List<InsuranceLeShanYanglao> list = insuranceLeShanParser.getyanglaoMsg(contentAsString2,parameter.getTaskId());
				if(list!=null){
					insuranceLeShanYangLaoRepository.saveAll(list);
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
			tracer.addTag("crawler.getyanglaoinfo.error", parameter.getTaskId());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYanglaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	public Future<String> getyiliaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			LocalDate today = LocalDate.now();
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1);
			String monthint = stardate.getMonthValue() + "";
			if (monthint.length() < 2) {
				monthint = "0" + monthint;
			}
			String month =  stardate.getYear() + monthint;
			tracer.addTag("insuranceLeShanService.crawler.getyiliaoMsg", taskInsurance.getTaskid());
			String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=310"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e="+month
					+ "&notkeyflag=0";
			Page page5 = gethtmlPost(webClient, null, yurl);
			String html = page5.getWebResponse().getContentAsString();
			System.out.println(html);

			if(html.indexOf("lists")!=-1){
				List<InsuranceLeShanYiLiao> list = insuranceLeShanParser.getyiliaoMsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceLeShanYiLiaoRepository.saveAll(list);
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
			tracer.addTag("insuranceLeShanService.crawler.getyiliaoinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYiliaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	public Future<String> getgongshangMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			LocalDate today = LocalDate.now();
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1);
			String monthint = stardate.getMonthValue() + "";
			if (monthint.length() < 2) {
				monthint = "0" + monthint;
			}
			String month =  stardate.getYear() + monthint;
			String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=410"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e="+month
					+ "&notkeyflag=0";
			Page page5 = gethtmlPost(webClient, null, yurl);
			String html = page5.getWebResponse().getContentAsString();
			System.out.println(html);
			if(html.indexOf("lists")!=-1){
				List<InsuranceLeShanGongShang> list = insuranceLeShanParser.getgongshangmsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceLeShanGongShangRepository.saveAll(list);
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
			tracer.addTag("insuranceLeShanService.crawler.getgongshanginfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
			taskInsurance.setGongshangStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	public Future<String> getshiyeMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			LocalDate today = LocalDate.now();
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1);
			String monthint = stardate.getMonthValue() + "";
			if (monthint.length() < 2) {
				monthint = "0" + monthint;
			}
			String month =  stardate.getYear() + monthint;
			String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=210"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e="+month
					+ "&notkeyflag=0";
			Page page5 = gethtmlPost(webClient, null, yurl);
			String contentAsString3 = page5.getWebResponse().getContentAsString();
			System.out.println(contentAsString3);
			if(contentAsString3.indexOf("lists")!=-1){
				List<InsuranceLeShanShiYe> list = insuranceLeShanParser.getshiyemsg(parameter.getTaskId(),contentAsString3);
				if(list!=null){
					insuranceLeShanShiYeRepository.saveAll(list);
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
			tracer.addTag("insuranceLeShanService.crawler.getshiyeinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShiyeStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}

	public Future<String> getshengyuMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			LocalDate today = LocalDate.now();
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1);
			String monthint = stardate.getMonthValue() + "";
			if (monthint.length() < 2) {
				monthint = "0" + monthint;
			}
			String month =  stardate.getYear() + monthint;
			String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=510"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e="+month
					+ "&notkeyflag=0";
			Page page5 = gethtmlPost(webClient, null, yurl);
			String contentAsString3 = page5.getWebResponse().getContentAsString();
			System.out.println(contentAsString3);
			if(contentAsString3.indexOf("lists")!=-1){
				List<InsuranceLeShanShengYu> list = insuranceLeShanParser.getshengyuMsg(parameter.getTaskId(),contentAsString3);
				if(list!=null){
					insuranceLeShanShengYuRepository.saveAll(list);
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
			tracer.addTag("insuranceLeShanService.crawler.getshiyeinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
			taskInsurance.setShengyuStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
}
