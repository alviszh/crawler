package app.service;


import java.net.URL;
import java.net.URLEncoder;
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
import com.microservice.dao.entity.crawler.insurance.hengshui.InsuranceHengShuiChargeDetail;
import com.microservice.dao.entity.crawler.insurance.hengshui.InsuranceHengShuiHtml;
import com.microservice.dao.entity.crawler.insurance.hengshui.InsuranceHengShuiUserInfo;
import com.microservice.dao.repository.crawler.insurance.hengshui.InsuranceHengShuiChargeDetailRepository;
import com.microservice.dao.repository.crawler.insurance.hengshui.InsuranceHengShuiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.hengshui.InsuranceHengShuiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceHengShuiParser;



/**
 * @description:
 * @author: sln 
 * @date: 2017年12月7日 下午5:24:12 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.hengshui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.hengshui"})
public class InsuranceHengShuiCrawlerService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceHengShuiCrawlerService.class);
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceHengShuiHtmlRepository insuranceHengShuiHtmlRepository;
	@Autowired
	private InsuranceHengShuiParser insuranceHengShuiParser;
	@Autowired
	private InsuranceHengShuiUserInfoRepository insuranceHengShuiUserInfoRepository;
	@Autowired
	private InsuranceHengShuiChargeDetailRepository insuranceHengShuiChargeDetailRepository;
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}")
	public String loginhost;
	
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance, String pernum, String name, String idcard, String insurnum) throws Exception {
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		//获取用户信息(第一部分)
		String url="http://"+loginhost+"/ggfwweb/app/getYalRyjbxx?grbh="+pernum+"";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String html="";  //第一部分用户信息
		String html2="";  //第二部分用户信息
		if(null!=page){
			html=page.getWebResponse().getContentAsString();
			InsuranceHengShuiHtml insuranceHengShuiHtml = new InsuranceHengShuiHtml();
			insuranceHengShuiHtml.setTaskid(taskInsurance.getTaskid());
			insuranceHengShuiHtml.setType("用户信息源码页(第一部分)");
			insuranceHengShuiHtml.setPagenumber(1);
			insuranceHengShuiHtml.setUrl(url);
			insuranceHengShuiHtml.setHtml(html);
			insuranceHengShuiHtmlRepository.save(insuranceHengShuiHtml);
			tracer.addTag("action.crawler.getUserinfo.html.1", "第一部分个人信息源码页已入库");
		}
		//获取用户信息(第二部分)  姓名在获取的时候已经是url加密的了，貌似加密了两次
		name=URLEncoder.encode(name.trim(), "utf-8");
		name=URLEncoder.encode(name.trim(), "utf-8");
		url="http://"+loginhost+"/ggfwweb/app/getSbkinfo?"
				+ "name="+name.trim()+""
				+ "&idcard="+idcard.trim()+""
				+ "&sbkh="+insurnum.trim()+"";
		webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		page = webClient.getPage(webRequest);
		if(null!=page){
			html2=page.getWebResponse().getContentAsString();
			InsuranceHengShuiHtml insuranceHengShuiHtml = new InsuranceHengShuiHtml();
			insuranceHengShuiHtml.setTaskid(taskInsurance.getTaskid());
			insuranceHengShuiHtml.setType("用户信息源码页(第二部分)");
			insuranceHengShuiHtml.setPagenumber(1);
			insuranceHengShuiHtml.setUrl(url);
			insuranceHengShuiHtml.setHtml(html2);
			insuranceHengShuiHtmlRepository.save(insuranceHengShuiHtml);
			tracer.addTag("action.crawler.getUserinfo.html.2", "第二部分个人信息源码页已入库");
		}
		InsuranceHengShuiUserInfo insuranceHengShuiUserInfo=insuranceHengShuiParser.userInfoParser(taskInsurance,html,html2);
		if(null!=insuranceHengShuiUserInfo){
			insuranceHengShuiUserInfoRepository.save(insuranceHengShuiUserInfo);
			tracer.addTag("action.crawler.getUserInfo", "全部个人信息已入库");
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getPension(TaskInsurance taskInsurance, String pernum) throws Exception{
		String url="http://"+loginhost+"/ggfwweb/app/getYalJfxxlist?"
				+ "aac001="+pernum+""
				+ "&aae041=200001"   //从2000年开始爬起   参数只需要年和月
				+ "&aae042="+InsuranceHengShuiHelpService.getPresentDate().trim()+""
//				+ "&_search=false"
//				+ "&nd=1513673719583"
				+ "&pagesize=200"  //由于是从2000年开始爬起，故设置个比较大的数值
				+ "&pageno=1"
				+ "&sidx="
				+ "&sord=asc";
		String chineseDescription="养老保险缴费明细";
		String englishDescription="getPension";
		getDifferentInsurChargeInfo(taskInsurance,url,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getMedical(TaskInsurance taskInsurance, String pernum) throws Exception {
		String url="http://"+loginhost+"/ggfwweb/app/ylxxlist/ylpaymentdetail?"
				+ "aac001="+pernum+""
				+ "&aae041=200001"
				+ "&aae042="+InsuranceHengShuiHelpService.getPresentDate().trim()+""
				+ "&pagesize=2000"
				+ "&pageno=1"
				+ "&sidx="
				+ "&sord=asc"; 
		String chineseDescription="医疗保险缴费明细";
		String englishDescription="getMedical";
		getDifferentInsurChargeInfo(taskInsurance,url,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getInjury(TaskInsurance taskInsurance, String pernum) throws Exception{
		String url="http://"+loginhost+"/ggfwweb/app/getGsJfxxlist?"
				+ "aac001="+pernum+""
				+ "&aae041=200001"
				+ "&aae042="+InsuranceHengShuiHelpService.getPresentDate().trim()+""
				+ "&pagesize=2000"
				+ "&pageno=1"
				+ "&sidx="
				+ "&sord=asc"; 
		String chineseDescription="工伤保险缴费明细";
		String englishDescription="getInjury";
		getDifferentInsurChargeInfo(taskInsurance,url,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getUnemployment(TaskInsurance taskInsurance, String pernum) throws Exception{
		String url="http://"+loginhost+"/ggfwweb/app/getSyJfxxlist?"
				+ "aac001="+pernum+""
				+ "&aae041=200001"
				+ "&aae042="+InsuranceHengShuiHelpService.getPresentDate().trim()+""
				+ "&pagesize=2000"
				+ "&pageno=1"
				+ "&sidx="
				+ "&sord=asc"; 
		String chineseDescription="失业保险缴费明细";
		String englishDescription="getUnemployment";
		getDifferentInsurChargeInfo(taskInsurance,url,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	
	public void getDifferentInsurChargeInfo(TaskInsurance taskInsurance,String url,String chineseDescription,String englishDescription) throws Exception{
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsuranceHengShuiHtml insuranceHengShuiHtml = new InsuranceHengShuiHtml();  
			insuranceHengShuiHtml.setTaskid(taskInsurance.getTaskid());
			insuranceHengShuiHtml.setType(chineseDescription+"源码页");
			insuranceHengShuiHtml.setPagenumber(1);
			insuranceHengShuiHtml.setUrl(url);
			insuranceHengShuiHtml.setHtml(html);
			insuranceHengShuiHtmlRepository.save(insuranceHengShuiHtml);
			tracer.addTag("action.crawler."+englishDescription+".html", chineseDescription+"源码页已入库");
			
			if(englishDescription.equals("getPension")){  //生育
				List<InsuranceHengShuiChargeDetail> list=insuranceHengShuiParser.pensionParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceHengShuiChargeDetailRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+"已入库");
				}
			}else if(englishDescription.equals("getMedical")){  //医疗
				List<InsuranceHengShuiChargeDetail> list=insuranceHengShuiParser.medicalParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceHengShuiChargeDetailRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+"已入库");
				}
			}else{   //工伤和失业
				List<InsuranceHengShuiChargeDetail> list=insuranceHengShuiParser.injuryAndUnemploymentParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceHengShuiChargeDetailRepository.saveAll(list);
					tracer.addTag("action.crawler."+englishDescription+"", chineseDescription+"已入库");
				}
			}
		}
	}
}
