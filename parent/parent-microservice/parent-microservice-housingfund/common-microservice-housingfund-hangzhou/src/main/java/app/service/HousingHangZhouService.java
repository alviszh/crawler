package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouPay;
import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouPayDetails;
import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouRecoDetails;
import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouUserinfo;
import com.microservice.dao.repository.crawler.housing.hangzhou.HousingHangZhouPayDetailsRepository;
import com.microservice.dao.repository.crawler.housing.hangzhou.HousingHangZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.hangzhou.HousingHangZhouRecoDetailsRepository;
import com.microservice.dao.repository.crawler.housing.hangzhou.HousingHangZhouUserinfoRepository;

import app.crawler.htmlparse.HousingHZParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.hangzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.hangzhou")
public class HousingHangZhouService extends HousingBasicService{
public static final Logger log = LoggerFactory.getLogger(HousingHangZhouService.class);
	
	@Autowired
	private HousingHangZhouPayRepository housingHangZhouPayRepository;
	
	@Autowired
	private HousingHangZhouUserinfoRepository housingHangZhouUserinfoRepository;
	
	@Autowired
	private HousingHangZhouPayDetailsRepository housingHangZhouPayDetailsRepository;
	
	@Autowired
	private HousingHangZhouRecoDetailsRepository housingHangZhouRecoDetailsRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String url = "http://www.hzgjj.gov.cn:8080/WebAccounts/perComInfo.do?flag=1";
		String urlPay = "http://www.hzgjj.gov.cn:8080/WebAccounts/perComInfo.do";
		String urlInfo = "http://www.hzgjj.gov.cn:8080/WebAccounts/userModify.do";
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
		HousingHangZhouUserinfo userinfo = HousingHZParse.userinfo_parse(page.getWebResponse().getContentAsString());
		userinfo.setUserid(messageLoginForHousing.getUser_id());
		userinfo.setTaskid(taskHousing.getTaskid());
		save(userinfo);
		//缴费信息
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
		HousingHangZhouPay pay =  HousingHZParse.pay_parse(page.getWebResponse().getContentAsString());
		pay.setUserid(messageLoginForHousing.getUser_id());
		pay.setTaskid(taskHousing.getTaskid());
		save(pay);
		//缴费信息明细
		String urlPayDetails = getURLforPay(page.getWebResponse().getContentAsString());
		try {
			WebRequest webRequest = new WebRequest(new URL(urlPayDetails), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlPay, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> urlPayDetailslist = getURLforPayDetails(page.getWebResponse().getContentAsString());
		for (String url_result : urlPayDetailslist) {
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
			List<HousingHangZhouPayDetails> listresult = HousingHZParse.paydetails_parse(page.getWebResponse().getContentAsString());
			for(HousingHangZhouPayDetails result : listresult){
				result.setUserid(messageLoginForHousing.getUser_id());
				result.setTaskid(taskHousing.getTaskid());
				save(result);
			}
		}
		//个人对账明细
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
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
			List<HousingHangZhouRecoDetails> listresul = HousingHZParse.recodetails_parse(page.getWebResponse().getContentAsString());
			for(HousingHangZhouRecoDetails result : listresul){
				result.setUserid(messageLoginForHousing.getUser_id());
				result.setTaskid(taskHousing.getTaskid());
				save(result);
			}
		}
		return null;
	}
	
	public String getURLforPay(String html) {
		Document doc = Jsoup.parse(html);

		System.out.println(doc);
		Elements ele = doc.select("table.BStyle_TB td a");
		String urlString = ele.get(1).attr("href").trim();

		System.out.println(urlString);
		String url = "http://www.hzgjj.gov.cn:8080"+ urlString;
		System.out.println("缴费url == " + url);
		return url;
	}
	public  List<String> getURLforPayDetails(String html) {
		List<String> urllist = new ArrayList<>();
		Document doc = Jsoup.parse(html);
		Elements urlas = doc.select("div.BStyle_PAGEdiv a");
		System.out.println(urlas);
		String str = urlas.get(0).attr("href").trim();
		String a = str.substring(0,str.lastIndexOf("&"));
		String b = a.substring(a.indexOf("&"));
		for (int i = 1;i<=3;i++) {
			String url = "http://www.hzgjj.gov.cn:8080/WebAccounts/comPerInfo.do?pagenum="+ i + b;
			System.out.println("====" + url);
			urllist.add(url);
		}
		return urllist;
	}
	public List<String> getURLforRecoDetails(String html) {
		Document doc = Jsoup.parse(html);
		List<String> urllist = new ArrayList<>();
		System.out.println(doc);
		Elements ele = doc.select("table.BStyle_TB td a");
		String urlString = ele.get(0).attr("href").trim();
		System.out.println(urlString);
		Calendar calendar = Calendar.getInstance();
		for(int i=0;i<20;i++){
			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
			String url = "http://www.hzgjj.gov.cn:8080/WebAccounts/perBillDetial.do?check_ym="+ searchYear +"&"+ urlString
					+"&flag=0&begin_date="+searchYear+"0101"+"&end_date="+searchYear+"1231";
			System.out.println("====" + url);
			urllist.add(url);
		}
		return urllist;
	}
	private void save(HousingHangZhouUserinfo result){
		housingHangZhouUserinfoRepository.save(result);
	}
	private void save(HousingHangZhouPay result){
		housingHangZhouPayRepository.save(result);
	}
	private void save(HousingHangZhouPayDetails result){
		housingHangZhouPayDetailsRepository.save(result);
	}
	private void save(HousingHangZhouRecoDetails result){
		housingHangZhouRecoDetailsRepository.save(result);
	}
}
