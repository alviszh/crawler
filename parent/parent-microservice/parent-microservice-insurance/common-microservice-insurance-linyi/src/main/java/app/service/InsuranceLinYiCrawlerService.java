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
import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiChargeDetail;
import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiChargeInfo;
import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiHtml;
import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiUserInfo;
import com.microservice.dao.repository.crawler.insurance.linyi.InsuranceLinYiChargeDetailRepository;
import com.microservice.dao.repository.crawler.insurance.linyi.InsuranceLinYiChargeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.linyi.InsuranceLinYiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.linyi.InsuranceLinYiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceLinYiParser;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月7日 下午5:24:12 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.linyi"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.linyi"})
public class InsuranceLinYiCrawlerService {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceLinYiHtmlRepository insuranceLinYiHtmlRepository;
	@Autowired
	private InsuranceLinYiParser insuranceLinYiParser;
	@Autowired
	private InsuranceLinYiUserInfoRepository insuranceLinYiUserInfoRepository;
	@Autowired
	private InsuranceLinYiChargeInfoRepository insuranceLinYiChargeInfoRepository;
	@Autowired
	private InsuranceLinYiChargeDetailRepository insuranceLinYiChargeDetailRepository;
	@Autowired
	private InsuranceFiveInsurChangeStatus linYiChangeStatus;
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}")
	public String loginhost;
	@Async
	public Future<String> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		//获取用户信息
		String url="http://"+loginhost+"/person/personInfo.html";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsuranceLinYiHtml insuranceLinYiHtml = new InsuranceLinYiHtml();
			insuranceLinYiHtml.setTaskid(taskInsurance.getTaskid());
			insuranceLinYiHtml.setType("用户信息源码页");
			insuranceLinYiHtml.setPagenumber(1);
			insuranceLinYiHtml.setUrl(url);
			insuranceLinYiHtml.setHtml(html);
			insuranceLinYiHtmlRepository.save(insuranceLinYiHtml);
			tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
			InsuranceLinYiUserInfo insuranceLinYiUserInfo=insuranceLinYiParser.userInfoParser(taskInsurance,html);
			if(null!=insuranceLinYiUserInfo){
				insuranceLinYiUserInfoRepository.save(insuranceLinYiUserInfo);
				tracer.addTag("action.crawler.getUserInfo", "个人信息已入库");
				insuranceService.changeCrawlerStatusUserInfo(taskInsurance, InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getError_code());
			}
		}
		return new AsyncResult<String>("200");	
	}
	//获取个人参保信息
	@Async
	public Future<String> getChargeInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		//获取用户信息
		String url="http://"+loginhost+"/person/personCBInfo.html";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			InsuranceLinYiHtml insuranceLinYiHtml = new InsuranceLinYiHtml();
			insuranceLinYiHtml.setTaskid(taskInsurance.getTaskid());
			insuranceLinYiHtml.setType("个人参保信息源码页");
			insuranceLinYiHtml.setPagenumber(1);
			insuranceLinYiHtml.setUrl(url);
			insuranceLinYiHtml.setHtml(html);
			insuranceLinYiHtmlRepository.save(insuranceLinYiHtml);
			tracer.addTag("action.crawler.getChargeInfo.html", "个人参保信息源码页已入库");
			List<InsuranceLinYiChargeInfo> list=insuranceLinYiParser.chargeInfoParser(taskInsurance,html);
			if(null!=list && list.size()>0){
				insuranceLinYiChargeInfoRepository.saveAll(list);
				tracer.addTag("action.crawler.getChargeInfo", "个人参保信息已入库");
			}else{
				tracer.addTag("action.crawler.getChargeInfo", "个人参保信息无可采集数据");
			}
		}
		return new AsyncResult<String>("200");	
	}
	//获取个人缴费明细
	@Async
	public Future<String> getChargeDetail(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		String url="http://"+loginhost+"/person/personJFJSInfo_result.html";    //不加请求参数，直接这样请求，默认获取的是第一页的数据，通过这个默认请求，得到总页数
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			System.out.println("获取的个人缴费明细总页码数页面是："+html);
			InsuranceLinYiHtml insuranceLinYiHtml = new InsuranceLinYiHtml();
			insuranceLinYiHtml.setTaskid(taskInsurance.getTaskid());
			insuranceLinYiHtml.setType("获取个人缴费明细总页码数源码页");
			insuranceLinYiHtml.setPagenumber(1);
			insuranceLinYiHtml.setUrl(url);
			insuranceLinYiHtml.setHtml(html);
			insuranceLinYiHtmlRepository.save(insuranceLinYiHtml);
			tracer.addTag("action.crawler.getChargeDetail.html", "个人缴费明细总页码数源码页已入库");
			//获取总页码数
			Document doc = Jsoup.parse(html);
			String text = doc.getElementById("queryform1").getElementsByTag("table").get(0).getElementsByClass("page_bottom").get(0).getElementsByClass("page_num").get(0).text();
			tracer.addTag("获取到的页码总数提示信息是", text);  //每页10条记录 | 共283条记录 | 当前1/29页
			String[] split = text.split("/");
			String totalPage=split[1];
			totalPage=totalPage.substring(0, totalPage.length()-1);
			tracer.addTag("最终获取到的总页数是：", totalPage);
			int totalPageCount=Integer.parseInt(totalPage);
			if(totalPageCount>0){
				for(int i=1;i<=totalPageCount;i++){
					url="http://"+loginhost+"/person/personJFJSInfo_result.html";     //不加请求参数，直接这样请求，默认获取的是第一页的数据，通过这个默认请求，得到总页数
					String requestBody=""      
							+ "pageNo="+i+""
//							+ "&aae002bg="    //通过测试发现，这三个参数不要也可以
//							+ "&aae002ed="
//							+ "&aae140="
							+ "&datanum=10";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						insuranceLinYiHtml = new InsuranceLinYiHtml();  
						insuranceLinYiHtml.setTaskid(taskInsurance.getTaskid());
						insuranceLinYiHtml.setType("第"+i+"页个人缴费明细信息源码页");
						insuranceLinYiHtml.setPagenumber(1);
						insuranceLinYiHtml.setUrl(url);
						insuranceLinYiHtml.setHtml(html);
						insuranceLinYiHtmlRepository.save(insuranceLinYiHtml);
						tracer.addTag("action.crawler.getChargeDetail.html", "第"+i+"页个人缴费明细信息源码页已入库");
						List<InsuranceLinYiChargeDetail> list=insuranceLinYiParser.chargeDetailParser(taskInsurance,html);
						if(null!=list && list.size()>0){
							insuranceLinYiChargeDetailRepository.saveAll(list);
							tracer.addTag("action.crawler.getChargeDetail", "第"+i+"页个人缴费明细信息已入库");
						}
					}
				}
				//爬取完所有分页信息之后更新五类保险信息的状态(没有信息也要更新)
				linYiChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 200);
			}else{
				//爬取完所有分页信息之后更新五类保险信息的状态(没有信息也要更新)
				linYiChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
			}
			
		}
		return new AsyncResult<String>("200");	
	}
}
