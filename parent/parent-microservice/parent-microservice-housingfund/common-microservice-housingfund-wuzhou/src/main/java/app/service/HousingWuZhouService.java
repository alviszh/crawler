package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuzhou.HousingWuZhouPay;
import com.microservice.dao.entity.crawler.housing.wuzhou.HousingWuZhouUserinfo;
import com.microservice.dao.repository.crawler.housing.wuzhou.HousingWuZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.wuzhou.HousingWuZhouUserinfoRepository;

import app.crawler.htmlparse.HousingTZParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuzhou")
public class HousingWuZhouService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingWuZhouService.class);
	
	@Autowired
	private HousingWuZhouUserinfoRepository housingWuZhouUserinfoRepository;
	@Autowired
	private HousingWuZhouPayRepository housingWuZhouPayRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "http://www.gxwzgjj.com/dc_zg_menu.asp";
		String url = "http://www.gxwzgjj.com/dc_zg_grye.asp";
		String urlPay = "http://www.gxwzgjj.com/dc_zg_jctqmx.asp";
		
		Page page = null;
		//基本信息
		try {
			WebRequest webRequest = new WebRequest(new URL(urlInfo), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = page.getWebResponse().getContentAsString();
//		System.out.println(html);
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html1 = page.getWebResponse().getContentAsString();
		tracer.addTag("个人信息html", html);
		tracer.addTag("个人信息html", html1);
		//System.out.println(html1);
		HousingWuZhouUserinfo userinfo = HousingTZParse.userinfo_parse(html,html1);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingWuZhouUserinfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		//个人对账明细
		try {
			WebRequest webRequest = new WebRequest(new URL(urlPay), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlPay, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		List<String> urlRecoDetailslist = getURLforRecoDetails(page.getWebResponse().getContentAsString());
		for (String url_result : urlRecoDetailslist) {
			try {
				WebRequest webRequest = new WebRequest(new URL(url_result), HttpMethod.GET);
				webClient.setJavaScriptTimeout(50000); 
				webClient.getOptions().setTimeout(50000); // 15->60 
				webClient.getOptions().setJavaScriptEnabled(false);
				page = webClient.getPage(webRequest);
				//page = loginAndGetCommon.getHtml(url_result, webClient);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tracer.addTag("缴费信息html", page.getWebResponse().getContentAsString());
			List<HousingWuZhouPay> listresul = HousingTZParse.paydetails_parse(page.getWebResponse().getContentAsString(),taskHousing);
			if(listresul==null){
				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
			}else{
				
				housingWuZhouPayRepository.saveAll(listresul);
				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
			}	
				
			
		}
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
	
	public List<String> getURLforRecoDetails(String html) {
		Document doc = Jsoup.parse(html);
		List<String> urllist = new ArrayList<>();
		//System.out.println(doc);
		Elements ele = doc.select("tr > td:nth-child(2) > p > font");
		if(ele.size()>0){
			String s = ele.text().trim();
			String ss = s.substring(s.lastIndexOf("/")+1);
			int page =  Integer.parseInt(ss); 
			for(int i=0;i<page;i++){
				String url = "http://www.gxwzgjj.com/dc_zg_jctqmx.asp?type=Next&i="+i+"";
				System.out.println("====" + url);
				urllist.add(url);
			}
			return urllist;
		}
		return null;
	}
}
