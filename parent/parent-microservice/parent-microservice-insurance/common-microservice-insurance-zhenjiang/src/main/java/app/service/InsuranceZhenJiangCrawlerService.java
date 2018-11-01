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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangChargeDetail;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangHtml;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangInsurInfo;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangParams;
import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangUserInfo;
import com.microservice.dao.repository.crawler.insurance.zhenjiang.InsuranceZhenJiangChargeDetailRepository;
import com.microservice.dao.repository.crawler.insurance.zhenjiang.InsuranceZhenJiangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.zhenjiang.InsuranceZhenJiangInsurInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zhenjiang.InsuranceZhenJiangParamsRepository;
import com.microservice.dao.repository.crawler.insurance.zhenjiang.InsuranceZhenJiangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceZhenJiangParser;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhenjiang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhenjiang"})
public class InsuranceZhenJiangCrawlerService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceZhenJiangCrawlerService.class);
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceZhenJiangHtmlRepository insuranceZhenJiangHtmlRepository;
	@Autowired
	private InsuranceZhenJiangParser insuranceZhenJiangParser;
	@Autowired
	private InsuranceZhenJiangUserInfoRepository insuranceZhenJiangUserInfoRepository;
	@Autowired
	private InsuranceZhenJiangChargeDetailRepository insuranceZhenJiangChargeDetailRepository;
	@Autowired
	private InsuranceZhenJiangInsurInfoRepository insuranceZhenJiangInsurInfoRepository;
	@Autowired
	private InsuranceZhenJiangParamsRepository zhenJiangParamsRepository;
	@Autowired
	private InsuranceFiveInsurChangeStatus zhenJiangChangeStatus;
	@Autowired
	private TracerLog tracer;
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance) {
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			webClient.getOptions().setJavaScriptEnabled(false);
			//获取爬取用户信息需要的身份证号：
			InsuranceZhenJiangParams zhenJiangParams = zhenJiangParamsRepository.findTopByTaskidOrderByCreatetimeDesc(taskInsurance.getTaskid());
			String idNum = null;
			if(null!=zhenJiangParams){
				idNum=zhenJiangParams.getIdcard().trim();
			}
			String url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/grsb/grsbData.action?_=1519813036284";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceZhenJiangHtml insuranceZhenJiangHtml = new InsuranceZhenJiangHtml();
				insuranceZhenJiangHtml.setTaskid(taskInsurance.getTaskid());
				insuranceZhenJiangHtml.setType("用户信息源码页");
				insuranceZhenJiangHtml.setPagenumber(1);
				insuranceZhenJiangHtml.setUrl(url);
				insuranceZhenJiangHtml.setHtml(html);
				insuranceZhenJiangHtmlRepository.save(insuranceZhenJiangHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				InsuranceZhenJiangUserInfo insuranceZhenJiangUserInfo=insuranceZhenJiangParser.userInfoParser(taskInsurance,html,idNum);
				if(null!=insuranceZhenJiangUserInfo){
					insuranceZhenJiangUserInfoRepository.save(insuranceZhenJiangUserInfo);
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
	public Future<String> getInsurInfo(TaskInsurance taskInsurance) {
		try {
			String url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/shbx/toPersonInsList.action";   
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceZhenJiangHtml insuranceZhenJiangHtml = new InsuranceZhenJiangHtml();
				insuranceZhenJiangHtml.setTaskid(taskInsurance.getTaskid());
				insuranceZhenJiangHtml.setType("参保信息源码页");
				insuranceZhenJiangHtml.setPagenumber(1);
				insuranceZhenJiangHtml.setUrl(url);
				insuranceZhenJiangHtml.setHtml(html);
				insuranceZhenJiangHtmlRepository.save(insuranceZhenJiangHtml);
				tracer.addTag("action.crawler.getInsurInfo.html", "参保信息源码页已入库");
				List<InsuranceZhenJiangInsurInfo> list=insuranceZhenJiangParser.insurInfoParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceZhenJiangInsurInfoRepository.saveAll(list);
					tracer.addTag("action.crawler.getInsurInfo", "参保信息已入库");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInsurInfo.e", e.toString());
		}
		return new AsyncResult<String>("200");	
	}
	
	@Async
	public Future<String> getChargeDetail(TaskInsurance taskInsurance)  { 
		try {
			//必须请求此前提页面
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			String url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/shbx/toPersonPayList.action";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/shbx/personPayList.action";   
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);  //用默认请求获取总共记录数
				page = webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					String total = JSONObject.fromObject(html).getString("iTotalDisplayRecords");
					int parseInt = Integer.parseInt(total);
					int remainder=parseInt%10;   //取余数
 	 				int totalPageCount=0;
 	 				if(remainder>0){  //如果余数大于0说明总记录数比每页要求显示的记录数(10)小，将如下运算的数据加1成为最终的总页数
 	 					totalPageCount=parseInt/10+1;    // 取整
 	 				}else{
 	 					totalPageCount=parseInt/10;
 	 				}
 	 				//接下来爬取该区间中每一页的数据
 	 				int currentIndex=0;  //设置初始变量
 	 				for(int i=1;i<=totalPageCount;i++){
 	 					currentIndex=(i-1)*10;
 	 					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
 	 					String requestBody=""
// 	 							+ "sEcho=2"
 	 							+ "&iColumns=9"
 	 							+ "&sColumns="
 	 							+ "&iDisplayStart="+currentIndex+""    //(当期页-1)*10       也就是起始记录数
 	 							+ "&iDisplayLength=10"    //每页显示10条数据(这个数据无法更改，经过测试，无法通过修改一次爬取完毕)
 	 							+ "&mDataProp_0=aae044"
 	 							+ "&mDataProp_1=aae003"
 	 							+ "&mDataProp_2=aae140"
 	 							+ "&mDataProp_3=aae115"
 	 							+ "&mDataProp_4=aae180"
 	 							+ "&mDataProp_5=aae020"
 	 							+ "&mDataProp_6=aae022"
 	 							+ "&mDataProp_7=aae078"
 	 							+ "&mDataProp_8=aae079"
 	 							+ "&sSearch=undefined"
 	 							+ "&bRegex=false"
 	 							+ "&sSearch_0="
 	 							+ "&bRegex_0=false"
 	 							+ "&bSearchable_0=true"
 	 							+ "&sSearch_1="
 	 							+ "&bRegex_1=false"
 	 							+ "&bSearchable_1=true"
 	 							+ "&sSearch_2="
 	 							+ "&bRegex_2=false"
 	 							+ "&bSearchable_2=true"
 	 							+ "&sSearch_3="
 	 							+ "&bRegex_3=false"
 	 							+ "&bSearchable_3=true"
 	 							+ "&sSearch_4="
 	 							+ "&bRegex_4=false"
 	 							+ "&bSearchable_4=true"
 	 							+ "&sSearch_5="
 	 							+ "&bRegex_5=false"
 	 							+ "&bSearchable_5=true"
 	 							+ "&sSearch_6="
 	 							+ "&bRegex_6=false"
 	 							+ "&bSearchable_6=true"
 	 							+ "&sSearch_7="
 	 							+ "&bRegex_7=false"
 	 							+ "&bSearchable_7=true"
 	 							+ "&sSearch_8="
 	 							+ "&bRegex_8=false"
 	 							+ "&bSearchable_8=true"
 	 							+ "&aae140="
 	 							+ "&aaa115="
 	 							+ "&aae078="
// 	 							+ "&aae041=201201"   //没有查询区间，也能分页返回数据
// 	 							+ "&aae042=201802"
 	 							;
 	 					webRequest.setRequestBody(requestBody);
 	 					page = webClient.getPage(webRequest);
 	 					if(null!=page){
 	 						html=page.getWebResponse().getContentAsString();
 	 						InsuranceZhenJiangHtml insuranceZhenJiangHtml = new InsuranceZhenJiangHtml();
 	 						insuranceZhenJiangHtml.setTaskid(taskInsurance.getTaskid());
 	 						insuranceZhenJiangHtml.setType("第"+i+"页缴费信息源码页");
 	 						insuranceZhenJiangHtml.setPagenumber(1);
 	 						insuranceZhenJiangHtml.setUrl(url);
 	 						insuranceZhenJiangHtml.setHtml(html);
 	 						insuranceZhenJiangHtmlRepository.save(insuranceZhenJiangHtml);
 	 						tracer.addTag("action.crawler.getChargeDetail.html第"+i+"页", "第"+i+"页缴费信息源码页已入库");
 	 						List<InsuranceZhenJiangChargeDetail> list=insuranceZhenJiangParser.chargeDetailParser(taskInsurance,html);
 	 						if(null!=list && list.size()>0){
 	 							insuranceZhenJiangChargeDetailRepository.saveAll(list);
 	 							tracer.addTag("action.crawler.getChargeDetail第"+i+"页", "第"+i+"页缴费信息已入库");
 	 						}
	 	 				}
					}
 	 				//在循环外更新
					zhenJiangChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 200);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeDetail.e", e.toString());
			zhenJiangChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
		}
		return new AsyncResult<String>("200");	
	}
}
