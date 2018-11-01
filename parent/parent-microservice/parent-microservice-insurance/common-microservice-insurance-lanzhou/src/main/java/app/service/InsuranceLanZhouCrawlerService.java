package app.service;


import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouHtml;
import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouMedicalInfo;
import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouPensionInfo;
import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.lanzhou.InsuranceLanZhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.lanzhou.InsuranceLanZhouMedicalInfoRepository;
import com.microservice.dao.repository.crawler.insurance.lanzhou.InsuranceLanZhouPensionInfoRepository;
import com.microservice.dao.repository.crawler.insurance.lanzhou.InsuranceLanZhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceLanZhouParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.lanzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.lanzhou"})
public class InsuranceLanZhouCrawlerService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceLanZhouCrawlerService.class);
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceLanZhouHtmlRepository insuranceLanZhouHtmlRepository;
	@Autowired
	private InsuranceLanZhouParser insuranceLanZhouParser;
	@Autowired
	private InsuranceLanZhouUserInfoRepository insuranceLanZhouUserInfoRepository;
	@Autowired
	private InsuranceLanZhouPensionInfoRepository insuranceLanZhouPensionInfoRepository;
	@Autowired
	private InsuranceLanZhouMedicalInfoRepository insuranceLanZhouMedicalInfoRepository;
	@Autowired
	private TracerLog tracer;
	private static String pernum;  //个人编号
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance) {
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			String url="http://wssb.lzmohrss.org.cn/siweb/rpc.do?method=doQuery";
			String requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:10,recordCount:0,rowSetName:\"lz_query.v_gg_info\"}},parameters:{\"synCount\":\"true\"}}}";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			//如下请求头信息必须加上，不然无法响应出数据
			webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
			webRequest.setAdditionalHeader("Host", "wssb.lzmohrss.org.cn");
			webRequest.setAdditionalHeader("Origin", "http://wssb.lzmohrss.org.cn");
			webRequest.setAdditionalHeader("Referer", "http://wssb.lzmohrss.org.cn/siweb/gg_emp_info.do?method=begin");
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceLanZhouHtml insuranceLanZhouHtml = new InsuranceLanZhouHtml();
				insuranceLanZhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceLanZhouHtml.setType("用户信息源码页");
				insuranceLanZhouHtml.setPagenumber(1);
				insuranceLanZhouHtml.setUrl(url);
				insuranceLanZhouHtml.setHtml(html);
				insuranceLanZhouHtmlRepository.save(insuranceLanZhouHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				InsuranceLanZhouUserInfo insuranceLanZhouUserInfo=insuranceLanZhouParser.userInfoParser(taskInsurance,html);
				if(null!=insuranceLanZhouUserInfo){
					insuranceLanZhouUserInfoRepository.save(insuranceLanZhouUserInfo);
					tracer.addTag("action.crawler.getUserInfo", "全部个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
	
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getPension(TaskInsurance taskInsurance) {
		try {
			String rowSetName="lz_query.v_gg_yzh";
			String condition="V_GG_YZH_AAE002";   //要查询的保险的区间范围
			String referer="http://wssb.lzmohrss.org.cn/siweb/gg_emp_yzh.do?method=begin";
			String chineseDescription="养老保险缴费明细";
			String englishDescription="getPension";
			getDifferentInsurChargeInfo(taskInsurance,referer,condition,chineseDescription,englishDescription,rowSetName);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getMedical(TaskInsurance taskInsurance)  {  //爬取医疗信息和养老信息相比，多了个人编号参数
		try {
			String url="http://wssb.lzmohrss.org.cn/siweb/gg_emp_yilsz.do?method=begin";   
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Host", "wssb.lzmohrss.org.cn");
			webRequest.setAdditionalHeader("Referer", "http://wssb.lzmohrss.org.cn/siweb/si/childmenu.do");
			HtmlPage page = webClient.getPage(webRequest);
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();
				html=html.substring(html.indexOf("aab001"), html.indexOf("aae041"));
				pernum=html.substring(html.indexOf("\"")+1, html.lastIndexOf("\""));
			}
			String rowSetName="lz_query.v_gg_yilsz";
			String condition="V_GG_YILSZ_AAE003";
			String referer="http://wssb.lzmohrss.org.cn/siweb/gg_emp_yilsz.do?method=begin";
			String chineseDescription="医疗保险缴费明细";
			String englishDescription="getMedical";
			getDifferentInsurChargeInfo(taskInsurance,referer,condition,chineseDescription,englishDescription,rowSetName);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	public void getDifferentInsurChargeInfo(TaskInsurance taskInsurance,String referer,String condition,String chineseDescription,String englishDescription, String rowSetName) throws Exception {
		String url="http://wssb.lzmohrss.org.cn/siweb/rpc.do?method=doQuery";
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
		webRequest.setAdditionalHeader("Host", "wssb.lzmohrss.org.cn");
		webRequest.setAdditionalHeader("Origin", "http://wssb.lzmohrss.org.cn");
		webRequest.setAdditionalHeader("Referer", ""+referer+"");
		String requestBody=null;
		if(englishDescription.equals("getPension")){
			requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},"
					+ "body:{dataStores:{\"queryStore\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},"
					+ "name:\"queryStore\","
					+ "pageNumber:1,"
					+ "pageSize:1000,"   //将总页数改为1000
					+ "recordCount:0,"
					+ "rowSetName:\""+rowSetName+"\","   //保险类型
					+ "condition:\"["+condition+"] >= '200001' "   //从2000年开始爬取
					+ "and ["+condition+"] <= '"+InsuranceLanZhouHelpService.getPresentDate().trim()+"'\"}},"
					+ "parameters:{\"synCount\":\"true\"}}}";
		}else{
			requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},"
					+ "body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},"
					+ "name:\"\","
					+ "pageNumber:1,"
					+ "pageSize:10,"
					+ "recordCount:0,"
					+ "rowSetName:\""+rowSetName+"\","   //保险类型
					+ "condition:\"["+condition+"] >= '200001' "   //从2000年开始爬取
					+ "and ["+condition+"] <= '"+InsuranceLanZhouHelpService.getPresentDate().trim()+"'"
					+ "and [V_GG_YILSZ_AAC001] = '"+pernum+"'\"}},"
					+ "parameters:{\"synCount\":\"true\"}}}";
		}
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsuranceLanZhouHtml insuranceLanZhouHtml = new InsuranceLanZhouHtml();  
			insuranceLanZhouHtml.setTaskid(taskInsurance.getTaskid());
			insuranceLanZhouHtml.setType(chineseDescription+"源码页");
			insuranceLanZhouHtml.setPagenumber(1);
			insuranceLanZhouHtml.setUrl(url);
			insuranceLanZhouHtml.setHtml(html);
			insuranceLanZhouHtmlRepository.save(insuranceLanZhouHtml);
			tracer.addTag("action.crawler."+englishDescription+".html", chineseDescription+"源码页已入库");
			if(englishDescription.equals("getPension")){   //养老
				List<InsuranceLanZhouPensionInfo> list=insuranceLanZhouParser.pensionParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceLanZhouPensionInfoRepository.saveAll(list);
				}
			}else {     //医疗
				List<InsuranceLanZhouMedicalInfo> list=insuranceLanZhouParser.medicalParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceLanZhouMedicalInfoRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+""+"<xmp>"+html+"</xmp>"+"已入库");
				}
			}
		}
	}
}
