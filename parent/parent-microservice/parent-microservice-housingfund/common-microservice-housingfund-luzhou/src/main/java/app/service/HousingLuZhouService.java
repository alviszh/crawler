package app.service;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

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
import com.microservice.dao.entity.crawler.housing.luzhou.HousingLuZhouPay;
import com.microservice.dao.entity.crawler.housing.luzhou.HousingLuZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.luzhou.HousingLuZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.luzhou.HousingLuZhouUserInfoRepository;

import app.crawler.htmlparse.HousingLZParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.luzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.luzhou")
public class HousingLuZhouService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingLuZhouService.class);
	@Autowired
	private HousingLuZhouUserInfoRepository housingLuZhouUserInfoRepository;
	@Autowired
	private HousingLuZhouPayRepository housingLuZhouPayRepository;
	
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "http://www.sclzgjj.com/ispobs/grQueryController/grzhxxcx.do";
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-2;
		int yue = now.get(Calendar.MONTH) +1;
		String yu = null;
		if (yue>9){
			yu = String.valueOf(yue);
		}else{
			yu = "0"+yue;
		}
		
		String endTime = String.valueOf(endNian)+ yu;
		String beginTime = beginNian +"01";
        String urlPay = "http://www.sclzgjj.com/ispobs/grQueryController/grzhmxcx.do?KSNY="+beginTime+"&JSNY="+endTime+"&orderbyname=CALINTERDATE&isasc=ASC&pagesize=1000&pageindex=1";
		
		Page page = null;
		//基本信息
		try {
			WebRequest webRequest = new WebRequest(new URL(urlInfo), HttpMethod.POST);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			//webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("个人信息html", html);
		System.out.println(html);
		HousingLuZhouUserInfo userinfo = HousingLZParse.userinfo_parse(html);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingLuZhouUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		
		//缴费信息
		try {
			WebRequest webRequest = new WebRequest(new URL(urlPay), HttpMethod.POST);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			//webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html1 = page.getWebResponse().getContentAsString();
		tracer.addTag("缴费信息html", html1);
		System.out.println(html1);
		List<HousingLuZhouPay> listresul = HousingLZParse.paydetails_parse(html1,taskHousing);
		if(listresul==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			
			housingLuZhouPayRepository.saveAll(listresul);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
			
			
		
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
}
