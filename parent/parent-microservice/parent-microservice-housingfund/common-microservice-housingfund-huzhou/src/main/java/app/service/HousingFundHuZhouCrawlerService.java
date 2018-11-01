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

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huzhou.HousingHuZhouDetailAccount;
import com.microservice.dao.entity.crawler.housing.huzhou.HousingHuZhouHtml;
import com.microservice.dao.entity.crawler.housing.huzhou.HousingHuZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.huzhou.HousingHuZhouDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.huzhou.HousingHuZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.huzhou.HousingHuZhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundHuZhouParser;
import app.service.common.HousingBasicService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.huzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.huzhou"})
public class HousingFundHuZhouCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundHuZhouCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundHuZhouParser housingFundHuZhouParser;
	@Autowired
	private HousingHuZhouHtmlRepository housingHuZhouHtmlRepository;
	@Autowired
	private HousingHuZhouDetailAccountRepository housingHuZhouDetailAccountRepository;
	@Autowired
	private HousingHuZhouUserInfoRepository housingHuZhouUserInfoRepository;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public Future<String> getUserInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) {
		try {
			WebClient webClient = HousingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="https://"+loginHost+"/hzgjj-wsyyt/ajax/ejx4web.action";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody="_sid=individualAccountQueryService_loadAccountInfo"
					+ "&json={\"grzh\":\""+messageLoginForHousing.getHosingFundNumber().trim()+"\"}"
					+ "&uid=29597085-1"  //这个参数必须有
					;
			webRequest.setRequestBody(requestBody);
			Page page= webClient.getPage(webRequest); 
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				HousingHuZhouHtml housingHuZhouHtml=new HousingHuZhouHtml();
	 			housingHuZhouHtml.setHtml(html);
	 			housingHuZhouHtml.setPagenumber(1);
	 			housingHuZhouHtml.setTaskid(taskHousing.getTaskid());
	 			housingHuZhouHtml.setType("用户基本信息源码页");
	 			housingHuZhouHtml.setUrl(url);
	 			housingHuZhouHtmlRepository.save(housingHuZhouHtml);
	 			if(html.contains("交易成功")){   //可以正确查询到用户基本信息
		 			HousingHuZhouUserInfo housingHuZhouUserInfo= housingFundHuZhouParser.userInfoParser(html,taskHousing);
		 			if(null!=housingHuZhouUserInfo){
						housingHuZhouUserInfoRepository.save(housingHuZhouUserInfo);
						updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
						tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
					}else{
						updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
						tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息暂无可采集数据");
						updateTaskHousing(taskHousing.getTaskid());
					}
	 			}else{
	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息暂无可采集数据");
					updateTaskHousing(taskHousing.getTaskid());
	 			}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");	
	}
	
	@Async
	public Future<String> getFlowInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebClient webClient=HousingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="https://"+loginHost+"/hzgjj-wsyyt/ajax/ejx4web.action";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody="_sid=personDetailQueryService_infoQuery"
					+ "&json={\"page\":\"1\",\"rows\":\"300\","
					+ "\"searchCondition\":{"
					+ "\"grzh\":\""+messageLoginForHousing.getHosingFundNumber().trim()+"\","
					+ "\"jzrqBegin\":\"20000101\","
					+ "\"jzrqEnd\":\""+messageLoginForHousing.getPassword().trim()+"\"}}"
					+ "&uid=29597085-1"
					+ "&_search=false"
					+ "&nd=1519711059283"
					+ "&rows=300"
					+ "&page=1"
					+ "&sidx="
					+ "&sord=asc";
			webRequest.setRequestBody(requestBody);
			Page page=webClient.getPage(webRequest);
			if(page!=null){
				String html = page.getWebResponse().getContentAsString(); 
	 			HousingHuZhouHtml housingHuZhouHtml=new HousingHuZhouHtml();
	 			housingHuZhouHtml.setHtml(html);
	 			housingHuZhouHtml.setPagenumber(1);
	 			housingHuZhouHtml.setTaskid(taskHousing.getTaskid());
	 			housingHuZhouHtml.setType("流水信息源码页");
	 			housingHuZhouHtml.setUrl(url);
	 			housingHuZhouHtmlRepository.save(housingHuZhouHtml);
	 			
	 			List<HousingHuZhouDetailAccount> list=housingFundHuZhouParser.detailAccountParser(html,taskHousing);
	 			if(null!=list && list.size()>0){
	 				housingHuZhouDetailAccountRepository.saveAll(list);
	 				updatePayStatusByTaskid("数据采集中，流水信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，流水信息已采集完成");
	 			}else{
	 				updatePayStatusByTaskid("数据采集中，流水信息已采集完成",201,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，流水信息无数据可供采集");
	 			}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getFlowInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updatePayStatusByTaskid("数据采集中，流水信息已采集完成",201,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");	
	}
}
