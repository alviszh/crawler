package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.panjin.InsurancePanJinChargeDetail;
import com.microservice.dao.entity.crawler.insurance.panjin.InsurancePanJinHtml;
import com.microservice.dao.entity.crawler.insurance.panjin.InsurancePanJinUserInfo;
import com.microservice.dao.repository.crawler.insurance.panjin.InsurancePanJinChargeDetailRepository;
import com.microservice.dao.repository.crawler.insurance.panjin.InsurancePanJinHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.panjin.InsurancePanJinUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsurancePanJinParser;

/**
 * @description:
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.panjin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.panjin"})
public class InsurancePanJinCrawlerService {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsurancePanJinHtmlRepository insurancePanJinHtmlRepository;
	@Autowired
	private InsurancePanJinParser insurancePanJinParser;
	@Autowired
	private InsurancePanJinUserInfoRepository insurancePanJinUserInfoRepository;
	@Autowired
	private InsurancePanJinChargeDetailRepository insurancePanJinChargeDetailRepository;
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}")
	public String loginhost;
	@Async
	public Future<String> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		//获取用户信息
		String url="http://"+loginhost+"/person/personRYJBXX.html";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsurancePanJinHtml insurancePanJinHtml = new InsurancePanJinHtml();
			insurancePanJinHtml.setTaskid(taskInsurance.getTaskid());
			insurancePanJinHtml.setType("用户信息源码页");
			insurancePanJinHtml.setPagenumber(1);
			insurancePanJinHtml.setUrl(url);
			insurancePanJinHtml.setHtml(html);
			insurancePanJinHtmlRepository.save(insurancePanJinHtml);
			tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
			InsurancePanJinUserInfo insurancePanJinUserInfo=insurancePanJinParser.userInfoParser(taskInsurance,html);
			if(null!=insurancePanJinUserInfo){
				insurancePanJinUserInfoRepository.save(insurancePanJinUserInfo);
				tracer.addTag("action.crawler.getUserInfo", "个人信息已入库");
				insuranceService.changeCrawlerStatusUserInfo(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getError_code());
			}
		}
		return new AsyncResult<String>("200");	
	}
	//爬取"+chineseDescription+"
	@Async
	public Future<String> getPension(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://"+loginhost+"/person/web_personYLCBJFXXCX_query.html"; //不加请求参数requestBody，默认获取的是第一页的数据，从默认响应的数据中获取所有记录的总页数
		String tabId="2";
		String chineseDescription="养老保险缴费明细";
		String englishDescription="getPension";
		getDifferentInsurChargeInfo(taskInsurance,url,tabId,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(), 
				InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getMedical(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception {
		String url="http://"+loginhost+"/person/web_personYILCBJFXX_query.html"; 
		String tabId="3";
		String chineseDescription="医疗保险缴费明细";
		String englishDescription="getMedical";
		getDifferentInsurChargeInfo(taskInsurance,url,tabId,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getInjury(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception{
		String url="http://"+loginhost+"/person/web_personGSCBJFXX_query.html"; 
		String tabId="3";
		String chineseDescription="工伤保险缴费明细";
		String englishDescription="getInjury";
		getDifferentInsurChargeInfo(taskInsurance,url,tabId,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getBear(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		String url="http://"+loginhost+"/person/web_personSHYCBJFXX_query.html"; 
		String tabId="3";
		String chineseDescription="生育保险缴费明细";
		String englishDescription="getBear";
		getDifferentInsurChargeInfo(taskInsurance,url,tabId,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	@Async
	public Future<String> getUnemployment(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance) throws Exception{
		String url="http://"+loginhost+"/person/web_personSYCBJFXX_query.html"; 
		String tabId="3";
		String chineseDescription="失业保险缴费明细";
		String englishDescription="getUnemployment";
		getDifferentInsurChargeInfo(taskInsurance,url,tabId,chineseDescription,englishDescription);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
				 200, taskInsurance);
		return new AsyncResult<String>("200");	
	}
	
	//除了养老保险tab位于第二个位置，其余险种个人缴费明细信息都在第三个位置
	public void getDifferentInsurChargeInfo(TaskInsurance taskInsurance,String url,String tabId,String chineseDescription,String englishDescription) throws Exception{
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsurancePanJinHtml insurancePanJinHtml = new InsurancePanJinHtml();
			insurancePanJinHtml.setTaskid(taskInsurance.getTaskid());
			insurancePanJinHtml.setType("获取"+chineseDescription+"总页码数源码页");
			insurancePanJinHtml.setPagenumber(1);
			insurancePanJinHtml.setUrl(url);
			insurancePanJinHtml.setHtml(html);
			insurancePanJinHtmlRepository.save(insurancePanJinHtml);
			tracer.addTag("action.crawler."+englishDescription+".html", ""+chineseDescription+"总页码数源码页已入库");
			//获取总页码数
			Document doc = Jsoup.parse(html);
			//==================================================================
			String text = doc.getElementById("tab"+tabId).getElementsByClass("page_bottom").get(0).getElementsByClass("page_num").get(0).text();
			tracer.addTag("获取到的"+chineseDescription+"页码总数提示信息是", text);  //每页10条记录 | 共283条记录 | 当前1/29页
			String[] split = text.split("/");
			String totalPage=split[1];
			totalPage=totalPage.substring(0, totalPage.length()-1);
			tracer.addTag("最终获取到的"+chineseDescription+"总页数是：", totalPage);
			int totalPageCount=Integer.parseInt(totalPage);
			//===================================================================
			String requestBody="";
			for(int i=1;i<=totalPageCount;i++){
				//当前页码处于requestBody中的参数pageNo1还是pageNo2，取决于tabId是几
				if(tabId.equals("2")){
					requestBody=""
							+ "pageNo=1"
							+ "&pageNo1="+i+""   //当前页码
							+ "&pageNo2=1"
							+ "&pageNo3=1"
							+ "&tzid="+tabId+"";   //养老保险所处的tab的位置(第二个)
				}else{
					requestBody=""
							+ "pageNo=1"
							+ "&pageNo1=1"   
							+ "&pageNo2="+i+""   //当前页码
							+ "&pageNo3=1"
							+ "&tzid="+tabId+"";   //其他类型的保险所处的tab的位置(第三个)
				}
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setRequestBody(requestBody);
				page = webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					insurancePanJinHtml = new InsurancePanJinHtml();  
					insurancePanJinHtml.setTaskid(taskInsurance.getTaskid());
					insurancePanJinHtml.setType("第"+i+"页"+chineseDescription+"源码页");
					insurancePanJinHtml.setPagenumber(i);
					insurancePanJinHtml.setUrl(url);
					insurancePanJinHtml.setHtml(html);
					insurancePanJinHtmlRepository.save(insurancePanJinHtml);
					tracer.addTag("action.crawler."+englishDescription+".html", "第"+i+"页"+chineseDescription+"源码页已入库");
					List<InsurancePanJinChargeDetail> list=insurancePanJinParser.chargeDetailParser(taskInsurance,html,tabId);
					if(null!=list && list.size()>0){
						insurancePanJinChargeDetailRepository.saveAll(list);
						tracer.addTag("action.crawler."+englishDescription+"", "第"+i+"页"+chineseDescription+"已入库");
					}
				}
			}
		}
	}
}
