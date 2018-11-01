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
import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoChargeDetail;
import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoHtml;
import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoUserInfo;
import com.microservice.dao.repository.crawler.insurance.rizhao.InsuranceRiZhaoChargeDetailRepository;
import com.microservice.dao.repository.crawler.insurance.rizhao.InsuranceRiZhaoHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.rizhao.InsuranceRiZhaoUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceRiZhaoParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.rizhao"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.rizhao"})
public class InsuranceRiZhaoCrawlerService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceRiZhaoCrawlerService.class);
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceRiZhaoHtmlRepository insuranceRiZhaoHtmlRepository;
	@Autowired
	private InsuranceRiZhaoParser insuranceRiZhaoParser;
	@Autowired
	private InsuranceRiZhaoUserInfoRepository insuranceRiZhaoUserInfoRepository;
	@Autowired
	private InsuranceRiZhaoChargeDetailRepository insuranceRiZhaoChargeDetailRepository;
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}")
	public String loginhost;
	
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance) {
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://"+loginhost+"/rz_query/rpc.do?method=doQuery";
			String requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"queryStore\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"queryStore\",pageNumber:1,pageSize:1,recordCount:0,rowSetName:\"pq.vpq_gg_ac01\"}},parameters:{\"synCount\":\"true\"}}}";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			//如下请求头信息必须加上，不然无法响应出数据
			webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
			webRequest.setAdditionalHeader("Host", ""+loginhost+"");
			webRequest.setAdditionalHeader("Origin", "http://"+loginhost+"");
			webRequest.setAdditionalHeader("Referer", "http://"+loginhost+"/rz_query/personQueryAction.do?method=gg_basic");
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceRiZhaoHtml insuranceRiZhaoHtml = new InsuranceRiZhaoHtml();
				insuranceRiZhaoHtml.setTaskid(taskInsurance.getTaskid());
				insuranceRiZhaoHtml.setType("用户信息源码页");
				insuranceRiZhaoHtml.setPagenumber(1);
				insuranceRiZhaoHtml.setUrl(url);
				insuranceRiZhaoHtml.setHtml(html);
				insuranceRiZhaoHtmlRepository.save(insuranceRiZhaoHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				InsuranceRiZhaoUserInfo insuranceRiZhaoUserInfo=insuranceRiZhaoParser.userInfoParser(taskInsurance,html);
				if(null!=insuranceRiZhaoUserInfo){
					insuranceRiZhaoUserInfoRepository.save(insuranceRiZhaoUserInfo);
					tracer.addTag("action.crawler.getUserInfo", "全部个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
	
		return new AsyncResult<String>("200");	
	}
	//五类保险信息的爬取
	@Async
	public Future<String> getPension(TaskInsurance taskInsurance) {
		try {
			String rowSetName="pq.vpq_yl_sj";
			String condition="VPQ_YL_SJ_AAE003";   //要查询的保险的区间范围
			String referer="http://"+loginhost+"/rz_query/personQueryAction.do?method=change&forwardjsp=gg_grjf_ylmx";
			String chineseDescription="养老保险缴费明细";
			String englishDescription="getPension";
			getDifferentInsurChargeInfo(taskInsurance,referer,condition,chineseDescription,englishDescription,rowSetName);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getMedical(TaskInsurance taskInsurance)  {
		try {
			String rowSetName="pq.vpq_yil_sj";
			String condition="VPQ_YIL_SJ_AAE003";
			String referer="http://"+loginhost+"/rz_query/personQueryAction.do?method=change&forwardjsp=gg_grjf_yilmx";
			String chineseDescription="医疗保险缴费明细";
			String englishDescription="getMedical";
			getDifferentInsurChargeInfo(taskInsurance,referer,condition,chineseDescription,englishDescription,rowSetName);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getInjury(TaskInsurance taskInsurance) {
		try {
			String rowSetName="pq.vpq_gs_ac20";
			String referer="http://"+loginhost+"/rz_query/manager/pages/pq/gs_bx_jfmx.jsp";
			String condition="VPQ_GS_AC20_AAE003";
			String chineseDescription="工伤保险缴费明细";
			String englishDescription="getInjury";
			getDifferentInsurChargeInfo(taskInsurance,referer,condition,chineseDescription,englishDescription,rowSetName);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInjury.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getUnemployment(TaskInsurance taskInsurance) {
		try {
			String rowSetName="pq.vpq_shiy_jc01";
			String referer="http://"+loginhost+"/rz_query/manager/pages/pq/shiy_bx_jfmx.jsp";
			String condition="VPQ_SHIY_JC01_AAE003";
			String chineseDescription="失业保险缴费明细";
			String englishDescription="getUnemployment";
			getDifferentInsurChargeInfo(taskInsurance,referer,condition,chineseDescription,englishDescription,rowSetName);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUnemployment.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getBear(TaskInsurance taskInsurance) {
		try {
			String rowSetName="pq.vpq_sy_ac20";
			String referer="http://"+loginhost+"/rz_query/manager/pages/pq/sy_bx_jfmx.jsp";
			String condition="VPQ_SY_AC20_AAE003";
			String chineseDescription="生育保险缴费明细";
			String englishDescription="getBear";
			getDifferentInsurChargeInfo(taskInsurance,referer,condition,chineseDescription,englishDescription,rowSetName);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBear.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public void getDifferentInsurChargeInfo(TaskInsurance taskInsurance,String referer,String condition,String chineseDescription,String englishDescription, String rowSetName) throws Exception {
		String url="http://"+loginhost+"/rz_query/rpc.do?method=doQuery";
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
		webRequest.setAdditionalHeader("Host", ""+loginhost+"");
		webRequest.setAdditionalHeader("Origin", "http://"+loginhost+"");
		webRequest.setAdditionalHeader("Referer", ""+referer+"");
		String requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},"
				+ "body:{dataStores:{\"queryStore\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},"
				+ "name:\"queryStore\","
				+ "pageNumber:1,"
				+ "pageSize:1000,"   //将总页数改为1000
				+ "recordCount:0,"
				+ "rowSetName:\""+rowSetName+"\","   //保险类型
				+ "condition:\"["+condition+"] >= '200001' "   //从2000年开始爬取
				+ "and ["+condition+"] <= '"+InsuranceRiZhaoHelpService.getPresentDate().trim()+"'\"}},"
				+ "parameters:{\"synCount\":\"true\"}}}";
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsuranceRiZhaoHtml insuranceRiZhaoHtml = new InsuranceRiZhaoHtml();  
			insuranceRiZhaoHtml.setTaskid(taskInsurance.getTaskid());
			insuranceRiZhaoHtml.setType(chineseDescription+"源码页");
			insuranceRiZhaoHtml.setPagenumber(1);
			insuranceRiZhaoHtml.setUrl(url);
			insuranceRiZhaoHtml.setHtml(html);
			insuranceRiZhaoHtmlRepository.save(insuranceRiZhaoHtml);
			tracer.addTag("action.crawler."+englishDescription+".html", chineseDescription+"源码页已入库");
			if(englishDescription.equals("getPension")){   //养老
				List<InsuranceRiZhaoChargeDetail> list=insuranceRiZhaoParser.pensionParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceRiZhaoChargeDetailRepository.saveAll(list);
				}
			}else if(englishDescription.equals("getUnemployment")){    //失业 
				List<InsuranceRiZhaoChargeDetail> list=insuranceRiZhaoParser.unemploymentParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceRiZhaoChargeDetailRepository.saveAll(list);
				}
			}else if(englishDescription.equals("getMedical")){   //医疗
				List<InsuranceRiZhaoChargeDetail> list=insuranceRiZhaoParser.medicalParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceRiZhaoChargeDetailRepository.saveAll(list);
				}
			}else if(englishDescription.equals("getBear")){   //生育
				List<InsuranceRiZhaoChargeDetail> list=insuranceRiZhaoParser.bearParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceRiZhaoChargeDetailRepository.saveAll(list);
				}
			}else {     //工伤
				List<InsuranceRiZhaoChargeDetail> list=insuranceRiZhaoParser.injuryParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceRiZhaoChargeDetailRepository.saveAll(list);
				}
			}
		}
	}
}
