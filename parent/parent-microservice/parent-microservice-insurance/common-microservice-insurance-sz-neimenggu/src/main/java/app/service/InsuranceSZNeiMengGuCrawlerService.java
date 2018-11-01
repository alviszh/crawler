package app.service;


import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuChargeDetail;
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuHtml;
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuInsurInfo;
import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuUserInfo;
import com.microservice.dao.repository.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuChargeDetailRepository;
import com.microservice.dao.repository.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuInsurInfoRepository;
import com.microservice.dao.repository.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZNeiMengGuParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.neimenggu"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.neimenggu"})
public class InsuranceSZNeiMengGuCrawlerService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceSZNeiMengGuCrawlerService.class);
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZNeiMengGuHtmlRepository insuranceSZNeiMengGuHtmlRepository;
	@Autowired
	private InsuranceSZNeiMengGuParser insuranceSZNeiMengGuParser;
	@Autowired
	private InsuranceSZNeiMengGuUserInfoRepository insuranceSZNeiMengGuUserInfoRepository;
	@Autowired
	private InsuranceSZNeiMengGuChargeDetailRepository insuranceSZNeiMengGuChargeDetailRepository;
	@Autowired
	private InsuranceSZNeiMengGuInsurInfoRepository insuranceSZNeiMengGuInsurInfoRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceFiveInsurChangeStatus neiMengGuChangeStatus;
	
	@Async
	public Future<String> getUserInfo(TaskInsurance taskInsurance){
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://card.12333k.cn/siweb/rpc.do?method=doQuery";
			String requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:1,recordCount:0,rowSetName:\"nmcard.ccweb_ac01\"}},parameters:{\"synCount\":\"true\"}}}";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Host", "card.12333k.cn");
			webRequest.setAdditionalHeader("Origin", "http://card.12333k.cn");
			webRequest.setAdditionalHeader("Referer", "http://card.12333k.cn/siweb/empmodify.do");
			webRequest.setAdditionalHeader("Content-Type", "application/json");   //这是必须加的请求头信息，否则无法返回数据
			webRequest.setRequestBody(requestBody);
			Page page=webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceSZNeiMengGuHtml insuranceSZNeiMengGuHtml = new InsuranceSZNeiMengGuHtml();
				insuranceSZNeiMengGuHtml.setTaskid(taskInsurance.getTaskid());
				insuranceSZNeiMengGuHtml.setType("用户信息源码页");
				insuranceSZNeiMengGuHtml.setPagenumber(1);
				insuranceSZNeiMengGuHtml.setUrl(url);
				insuranceSZNeiMengGuHtml.setHtml(html);
				insuranceSZNeiMengGuHtmlRepository.save(insuranceSZNeiMengGuHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				List<InsuranceSZNeiMengGuUserInfo> list=insuranceSZNeiMengGuParser.userInfoParser(taskInsurance,html);
				if(null!=list && list.size()>0){
					insuranceSZNeiMengGuUserInfoRepository.saveAll(list);
					tracer.addTag("action.crawler.getUserInfo", "个人信息已入库");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}else{
					insuranceSZNeiMengGuUserInfoRepository.saveAll(list);
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return new AsyncResult<String>("200");	
	}
	//属于参保信息的一部分，并不是实际缴纳记录，故爬取该类信息，但是不更新最终状态
	@Async
	public Future<String> getInsurInfo(TaskInsurance taskInsurance){
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			String url="http://app.12333s.cn/nmQuery/person/insuranceList.html";
			webClient.getOptions().setTimeout(20000);    //有时候这个页面响应的很慢
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setCharset(Charset.forName("GBK"));  //这个需要加上
			Page page=webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				InsuranceSZNeiMengGuHtml insuranceSZNeiMengGuHtml = new InsuranceSZNeiMengGuHtml();  
				insuranceSZNeiMengGuHtml.setTaskid(taskInsurance.getTaskid());
				insuranceSZNeiMengGuHtml.setType("参保信息源码页");
				insuranceSZNeiMengGuHtml.setPagenumber(1);
				insuranceSZNeiMengGuHtml.setUrl(url);
				insuranceSZNeiMengGuHtml.setHtml(html);
				insuranceSZNeiMengGuHtmlRepository.save(insuranceSZNeiMengGuHtml);
				tracer.addTag("action.crawler.getInsurInfo.html", "参保信息源码页已入库");
				List<InsuranceSZNeiMengGuInsurInfo> list=insuranceSZNeiMengGuParser.insurInfoParser(taskInsurance,html);
				if(list!=null && list.size()>0){
					insuranceSZNeiMengGuInsurInfoRepository.saveAll(list);
					tracer.addTag("action.crawler.getInsurInfo", "参保信息已入库");
//					neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 200);
				}else{
					tracer.addTag("action.crawler.getInsurInfo", "参保信息无数据可供采集");
//					neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInsurInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
//			neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
		}
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getChargeDetail(TaskInsurance taskInsurance) {
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			//获取缴费信息(参数信息必须拼接在url后边，如果用webRequest.setRequestBody(requestBody)的方式，即使改变页码也无法获取指定页码的数据，都是获取的第一页的数据)
			//必须指定citycode,不然只能获取默认城市的数据（没必要指定查询区间范围，因为默认第一页返回的信息中已经说了共有多少条数据）
			//根据第一页(默认页)获取到所有的citycode（参保地，用list集合存储）和总页码等参数(最先获取citycode，因为参保地不同，总页数也不同)
			String url="http://app.12333s.cn/nmQuery/person/paySI_query.html";    //第一次请求默认页是为了获取参保城市代号及其城市名称
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page=webClient.getPage(webRequest);
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();   //根据默认页码的返回html获取之后需要的参数
				Document doc = Jsoup.parse(html);
				Elements options = doc.getElementById("citycode").getElementsByTag("option");
				if(null!=options){
					Map<String, String> map = new HashMap<String, String>();  //将该账户下的所有的投保城市及其城市代码解析出来并存储
					for (Element element : options) {
						map.put(element.val(), element.text());  
					}
					//根据遍历map集合得到的结果来爬取剩下的数据
					for (String citycode : map.keySet()) {
						//根据citycode获取该代号对应的城市名称
						String insurCityName=map.get(citycode);
						url="http://app.12333s.cn/nmQuery/person/paySI_query.html?"   //第二次请求默认页是为了获取指定的参保城市中共有多少页缴费数据
						+ "pageNo=1"
						+ "&pageSize="
//						+ "&aae003=200110"    //不用指定范围查询，因为返回的总页数结果就是根据所有缴纳记录判定的
//						+ "&baz001=201802"
						+ "&aae140=null"    //为null代表的是任意类型的保险
						+ "&citycode="+citycode+"";
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						page=webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							doc = Jsoup.parse(html);
							String pageDetail = doc.getElementById("cont_1").getElementsByClass("tableDateBk").get(0).getElementsByTag("tr").get(0)
									.getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").get(0).getElementsByTag("td").get(2).text();
							String totalPage = pageDetail.split("共")[1].split("页")[0];
							tracer.addTag("在"+insurCityName+"缴费记录的总页数是：", totalPage);
							//接下来需要分页爬取所有的缴费信息（经过测试，无法通过改变pagesize来一次性爬取所有的数据）
							int intPageCount = Integer.parseInt(totalPage);
							for(int i=1;i<=intPageCount;i++){
								url="http://app.12333s.cn/nmQuery/person/paySI_query.html?"
										+ "pageNo="+i+""
										+ "&pageSize="
										+ "&aae140=null"
										+ "&citycode="+citycode+"";
								webRequest = new WebRequest(new URL(url), HttpMethod.POST);
								page=webClient.getPage(webRequest);
								if(null!=page){
									html=page.getWebResponse().getContentAsString();
									InsuranceSZNeiMengGuHtml insuranceSZNeiMengGuHtml = new InsuranceSZNeiMengGuHtml();  
									insuranceSZNeiMengGuHtml.setTaskid(taskInsurance.getTaskid());
									insuranceSZNeiMengGuHtml.setType("缴费信息源码页");
									insuranceSZNeiMengGuHtml.setPagenumber(i);
									insuranceSZNeiMengGuHtml.setUrl(url);
									insuranceSZNeiMengGuHtml.setHtml(html);
									insuranceSZNeiMengGuHtmlRepository.save(insuranceSZNeiMengGuHtml);
									tracer.addTag("action.crawler.getChargeDetail.html", "缴费信息源码页已入库");
									List<InsuranceSZNeiMengGuChargeDetail> list=insuranceSZNeiMengGuParser.chargeDetailParser(taskInsurance,html,citycode,insurCityName);
									if(list!=null && list.size()>0){
										insuranceSZNeiMengGuChargeDetailRepository.saveAll(list);
										tracer.addTag("action.crawler.getChargeDetail", "缴费信息已入库");
										neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 200);
									}else{
										tracer.addTag("action.crawler.getChargeDetail", "缴费信息无数据可供采集");
										neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
									}
								}
							}
						}
					}
				}else{
					neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
				}
			}
		} catch (Exception e) {
			neiMengGuChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
		}
		return new AsyncResult<String>("200");	
	}
}
