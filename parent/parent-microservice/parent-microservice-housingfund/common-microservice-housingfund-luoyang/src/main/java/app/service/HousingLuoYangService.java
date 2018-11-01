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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.luoyang.HousingLuoYangDetailed;
import com.microservice.dao.entity.crawler.housing.luoyang.HousingLuoYangPay;
import com.microservice.dao.entity.crawler.housing.luoyang.HousingLuoYangUserInfo;
import com.microservice.dao.repository.crawler.housing.luoyang.HousingLuoYangDetailedRepository;
import com.microservice.dao.repository.crawler.housing.luoyang.HousingLuoYangPayRepository;
import com.microservice.dao.repository.crawler.housing.luoyang.HousingLuoYangUserInfoRepository;

import app.crawler.htmlparse.HousingLYParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.louyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.louyang")
public class HousingLuoYangService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingLuoYangService.class);
	
	@Autowired
	private HousingLuoYangUserInfoRepository housingLuoYangUserInfoRepository;
	
	@Autowired
	private HousingLuoYangPayRepository housingLuoYangPayRepository;
	
	@Autowired
	private HousingLuoYangDetailedRepository housingLuoYangDetailedRepository;
	
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-10;
		int yue = now.get(Calendar.MONTH) +1;
		int ri = now.get(Calendar.DAY_OF_MONTH);
		String yu = null;
		String r = null;
		if (yue>9){
			yu = String.valueOf(yue);
		}else{
			yu = "0"+yue;
		}
		if (ri>9){
			r = String.valueOf(ri);
		}else{
			r = "0"+ri;
		}
		String beginDate =beginNian+"-"+ yu+"-"+r;
		String endDate =endNian+"-"+ yu+"-"+r;
		
		String urlInfo = "http://www.lyzfgjj.com/per/queryPerInfo.do";
		String urlDetailed = "http://www.lyzfgjj.com/per/queryPerDeposit!queryPerByYear.do?"
				+ "dto%5B%27startdate%27%5D="+beginDate+"&dto%5B%27enddate%27%5D="+endDate+""
				+ "&gridInfo%5B%27dataList_limit%27%5D=1000&gridInfo%5B%27dataList_start%27%5D=0&";
		String urlPay = "http://www.lyzfgjj.com/per/perPayRecord!getPerAccDetails.do?"
				+ "dto%5B'smnh'%5D="+beginDate+"&dto%5B'emnh'%5D="+endDate+"&&";
		
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
		tracer.addTag("个人信息html", page.getWebResponse().getContentAsString());
		HousingLuoYangUserInfo userinfo = HousingLYParse.userinfo_parse(page.getWebResponse().getContentAsString());
		userinfo.setTaskid(taskHousing.getTaskid());
		housingLuoYangUserInfoRepository.save(userinfo);
		
		//个人明细
		try {
			WebRequest webRequest = new WebRequest(new URL(urlDetailed), HttpMethod.POST);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("sssssssssssssssss"+page.getWebResponse().getContentAsString());
		tracer.addTag("个人明细html", page.getWebResponse().getContentAsString());
		List<HousingLuoYangDetailed> detailed = HousingLYParse.userinfo_detailed(page.getWebResponse().getContentAsString());
		for(HousingLuoYangDetailed result : detailed){
			result.setTaskid(taskHousing.getTaskid());
			housingLuoYangDetailedRepository.save(result);
		}
		
		//缴存明细
		try {
			WebRequest webRequest = new WebRequest(new URL(urlPay), HttpMethod.POST);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("sssssssssssssssss"+page.getWebResponse().getContentAsString());
		tracer.addTag("缴存明细html", page.getWebResponse().getContentAsString());
		List<HousingLuoYangPay> pay = HousingLYParse.paydetails_parse(page.getWebResponse().getContentAsString());
		for(HousingLuoYangPay result : pay){
			result.setTaskid(taskHousing.getTaskid());
			housingLuoYangPayRepository.save(result);
		}		
		return null;
	}
}

