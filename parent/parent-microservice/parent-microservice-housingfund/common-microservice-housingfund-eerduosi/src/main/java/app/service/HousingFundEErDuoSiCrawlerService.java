package app.service;

import java.net.URL;
import java.net.URLEncoder;
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

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiFlowInfo;
import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiHtml;
import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiUserInfo;
import com.microservice.dao.repository.crawler.housing.eerduosi.HousingEErDuoSiFlowInfoRepository;
import com.microservice.dao.repository.crawler.housing.eerduosi.HousingEErDuoSiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.eerduosi.HousingEErDuoSiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundEErDuoSiParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.eerduosi"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.eerduosi"})
public class HousingFundEErDuoSiCrawlerService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundEErDuoSiParser housingFundEErDuoSiParser;
	@Autowired
	private  HousingFundHelperService housingFundHelperService;
	@Autowired
	private HousingEErDuoSiHtmlRepository housingEErDuoSiHtmlRepository;
	@Autowired
	private HousingEErDuoSiUserInfoRepository housingEErDuoSiUserInfoRepository;
	@Autowired
	private HousingEErDuoSiFlowInfoRepository housingEErDuoSiFlowInfoRepository;
	
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public Future<String> getUserInfo(TaskHousing taskHousing, String path) {
		try {
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			//通过如下链接获取爬取用户信息的链接需要的请求参数
			String url="http://"+loginHost+"/"+path+"/PersonalAccumulationMoney/Personalinformation.aspx";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page= webClient.getPage(webRequest);
			if(page!=null){								
				String html = page.getWebResponse().getContentAsString(); 
				Document doc = Jsoup.parse(html);
				String apartLink = doc.getElementById("GridView1").getElementsByTag("tr").get(1).getElementsByTag("td").get(6).getElementsByTag("a").get(0).attr("href");
				
				String name=apartLink.substring(apartLink.indexOf("id1=")+"id1=".length(),apartLink.indexOf("&id2"));
				String idnum=apartLink.substring(apartLink.indexOf("id2=")+"id2=".length(),apartLink.indexOf("&id3"));
				String status=apartLink.substring(apartLink.indexOf("id3=")+"id3=".length(),apartLink.length());
				//对返回的参数进行urlencode
				name=URLEncoder.encode(name, "utf-8");
				idnum=URLEncoder.encode(idnum, "utf-8");
				status=URLEncoder.encode(status, "utf-8");
				//请求用户信息页面
				url="http://"+loginHost+"/"+path+"/PersonalAccumulationMoney/test.aspx?id1="+name+"&id2="+idnum.trim()+"&id3="+status+"";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				page= webClient.getPage(webRequest);
				if(page!=null){
					html = page.getWebResponse().getContentAsString(); 
		 			HousingEErDuoSiHtml housingEErDuoSiHtml=new HousingEErDuoSiHtml();
		 			housingEErDuoSiHtml.setHtml(html);
		 			housingEErDuoSiHtml.setPagenumber(1);
		 			housingEErDuoSiHtml.setTaskid(taskHousing.getTaskid());
		 			housingEErDuoSiHtml.setType("用户基本信息源码页");
		 			housingEErDuoSiHtml.setUrl(url);
		 			housingEErDuoSiHtmlRepository.save(housingEErDuoSiHtml);
		 			//解析用户信息
		 			HousingEErDuoSiUserInfo housingEErDuoSiUserInfo=housingFundEErDuoSiParser.userInfoParser(html,taskHousing);
		 			if(null!=housingEErDuoSiUserInfo){
		 				housingEErDuoSiUserInfoRepository.save(housingEErDuoSiUserInfo);
		 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
						tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
		 			}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");	
	}
	
	/**
	 * 调研账号的缴费信息目前只有一页，无法得知获取下一页信息的需要的参数是什么,故先这样写
	 * @param taskHousing
	 * @param path
	 * @return
	 */
	@Async
	public Future<String> getFlowInfo(TaskHousing taskHousing, String path) {
		try {
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://"+loginHost+"/"+path+"/PersonalAccumulationMoney/Personalwater.aspx";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page= webClient.getPage(webRequest);
			if(page!=null){
				String html = page.getWebResponse().getContentAsString(); 
	 			HousingEErDuoSiHtml housingEErDuoSiHtml=new HousingEErDuoSiHtml();
	 			housingEErDuoSiHtml.setHtml(html);
	 			housingEErDuoSiHtml.setPagenumber(1);
	 			housingEErDuoSiHtml.setTaskid(taskHousing.getTaskid());
	 			housingEErDuoSiHtml.setType("流水信息源码页");
	 			housingEErDuoSiHtml.setUrl(url);
	 			housingEErDuoSiHtmlRepository.save(housingEErDuoSiHtml);
	 			
	 			List<HousingEErDuoSiFlowInfo> list=housingFundEErDuoSiParser.flowInfoParser(html,taskHousing);
	 			if(null!=list && list.size()>0){
	 				housingEErDuoSiFlowInfoRepository.saveAll(list);
	 				updatePayStatusByTaskid("数据采集中，流水信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getFlowInfo", "数据采集中，流水信息已采集完成");
	 			}else{
	 				updatePayStatusByTaskid("数据采集中，流水信息已采集完成",201,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getFlowInfo", "数据采集中，流水信息无数据可供采集");
	 			}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getFlowInfo.e", e.toString());
			updatePayStatusByTaskid("数据采集中，流水信息已采集完成",201,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");	
	}
}
