package app.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangUserInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangYiLiaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guigang.InsuranceGuiGangGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guigang.InsuranceGuiGangShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guigang.InsuranceGuiGangShiYeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guigang.InsuranceGuiGangUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guigang.InsuranceGuiGangYangLaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guigang.InsuranceGuiGangYiLiaoInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceGuiGangParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.guigang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.guigang" })
public class InsuranceGuiGangServiceUnit {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceGuiGangParser insuranceGuiGangParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGuiGangUserInfoRepository insuranceGuiGangUserInfoRepository;
	@Autowired
	private InsuranceGuiGangGongShangInfoRepository insuranceGuiGangGongShangInfoRepository;
	@Autowired
	private InsuranceGuiGangShengYuInfoRepository insuranceGuiGangShengYuInfoRepository;
	@Autowired
	private InsuranceGuiGangYangLaoInfoRepository insuranceGuiGangYangLaoInfoRepository;
	@Autowired
	private InsuranceGuiGangYiLiaoInfoRepository insuranceGuiGangYiLiaoInfoRepository;
	@Autowired
	private InsuranceGuiGangShiYeInfoRepository insuranceGuiGangShiYeInfoRepository;
	public Future<String> getuserinfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		
		try {
			Cookie cookie = webClient.getCookieManager().getCookie("userid");
			String userid = cookie.getValue();
			System.out.println("userid------------>"+userid);
			String url6 = "http://www.gxggsi.gov.cn:7005/privateHomeAction!query.do?userid="+userid;
			Page page = getHtml(url6, webClient);
			String html4 = page.getWebResponse().getContentAsString();
			System.out.println(html4);
			String user2 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryInsuranceInfoAction!toSearchPersonInfo.do";
			Page page5 = getHtml(user2, webClient);
			String html5 = page5.getWebResponse().getContentAsString();
			System.out.println(html5);
			if(html4!=null||html5!=null){
				InsuranceGuiGangUserInfo userinfo = insuranceGuiGangParser.getuser(html4,html5,parameter.getTaskId());
				if(userinfo!=null){
					insuranceGuiGangUserInfoRepository.save(userinfo);
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
			tracer.addTag("insuranceGuiGangService.crawler.getuserinfo.error", e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	

	public Future<String> getyanglaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient, int year) {
		try {
		String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do"
				+ "?dto%5B%27aae002%27%5D="+year
				+ "&&dto%5B%27aae140%27%5D=110&&";//110-->养老
		Page page6 = getHtml(url4, webClient);
		String html6 = page6.getWebResponse().getContentAsString();
		System.out.println(html6);
		if(html6.indexOf("msg")==-1){
			System.out.println("有数据："+html6);
			List<InsuranceGuiGangYangLaoInfo> list = insuranceGuiGangParser.getyanglaomsg(parameter.getTaskId(),html6);
			if(list!=null){
				insuranceGuiGangYangLaoInfoRepository.saveAll(list);
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
		tracer.addTag("insuranceGuiGangService.crawler.getyanglaoinfo.error", e.getMessage());
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
	 * @param year 
	 * @return
	 */
	public Future<String> getyiliaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient, int year) {
		try {
			tracer.addTag("insuranceGuiGangService.crawler.getyiliaoMsg", taskInsurance.getTaskid());
			String url6 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do"
					+ "?dto%5B'aae002'%5D="+year
					+ "&&dto%5B'aae140'%5D=310&&";//310-->基本医疗
			Page page6 = getHtml(url6, webClient);
			String html = page6.getWebResponse().getContentAsString();
			System.out.println(html);
			if(html.indexOf("msg")==-1){
				System.out.println("有数据：");
				List<InsuranceGuiGangYiLiaoInfo> list = insuranceGuiGangParser.getyiliaoMsg(parameter.getTaskId(),html);
				if(list!=null){
					insuranceGuiGangYiLiaoInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceGuiGangService.crawler.getyiliaoinfo.error", e.getMessage());
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
	 * @param year 
	 * @return
	 */
	public Future<String> getgongshangMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient, int year) {
		try {
			tracer.addTag("insuranceGuiGangService.crawler.getgongshangMsg", taskInsurance.getTaskid());
			String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do?"
					+ "dto%5B'aae002'%5D="+year
					+ "&&dto%5B'aae140'%5D=410&&";//410-->工伤
			Page page6 = getHtml(url4, webClient);
			String html6 = page6.getWebResponse().getContentAsString();
			System.out.println(html6);
			if(html6.indexOf("msg")==-1){
				System.out.println("有数据：");
				List<InsuranceGuiGangGongShangInfo> list = insuranceGuiGangParser.getgongshangmsg(parameter.getTaskId(),html6);
				if(list!=null){
					insuranceGuiGangGongShangInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceGuiGangService.crawler.getgongshanginfo.error", e.getMessage());
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
	 * @param year 
	 * @return
	 */
	public Future<String> getshiyeMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient, int year) {
		try {
			tracer.addTag("insuranceGuiGangService.crawler.getshiyeMsg", taskInsurance.getTaskid());
			String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do?"
					+ "dto%5B'aae002'%5D="+year
					+ "&&dto%5B'aae140'%5D=210&&";//210-->失业
			Page page6 = getHtml(url4, webClient);
			String html6 = page6.getWebResponse().getContentAsString();
			System.out.println(html6);
			if(html6.indexOf("msg")==-1){
				List<InsuranceGuiGangShiYeInfo> list = insuranceGuiGangParser.getshiyemsg(parameter.getTaskId(),html6);
				if(list!=null){
					insuranceGuiGangShiYeInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceGuiGangService.crawler.getshiyeinfo.error", e.getMessage());
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
	 * @param year 
	 * @return
	 */
	public Future<String> getshengyuMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient, int year) {
		try {
			tracer.addTag("insuranceGuiGangService.crawler.getshengyuMsg", taskInsurance.getTaskid());
			String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do?"
					+ "dto%5B'aae002'%5D="+year
					+ "&&dto%5B'aae140'%5D=510&&";//510-->生育
			Page page6 = getHtml(url4, webClient);
			String html6 = page6.getWebResponse().getContentAsString();
			System.out.println(html6);
			if(html6.indexOf("msg")==-1){
				System.out.println("有数据：");
				List<InsuranceGuiGangShengYuInfo> list = insuranceGuiGangParser.getshengyuMsg(parameter.getTaskId(),html6);
				if(list!=null){
					insuranceGuiGangShengYuInfoRepository.saveAll(list);
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
			tracer.addTag("insuranceGuiGangService.crawler.getshiyeinfo.error", e.getMessage());
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
		webRequest.setCharset(Charset.forName("UTF-8"));
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
