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
import com.microservice.dao.entity.crawler.housing.wenshan.HousingWenShanPay;
import com.microservice.dao.entity.crawler.housing.wenshan.HousingWenShanUserinfo;
import com.microservice.dao.repository.crawler.housing.wenshan.HousingWenShanPayRepository;
import com.microservice.dao.repository.crawler.housing.wenshan.HousingWenShanUserinfoRepository;

import app.crawler.htmlparse.HousingWSParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wenshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wenshan")
public class HousingWenShanService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingWenShanService.class);
	
	@Autowired
	private HousingWenShanUserinfoRepository housingWenShanUserinfoRepository;
	@Autowired
	private HousingWenShanPayRepository housingWenShanPayRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "https://www.wsgjj.com/WSYYT/qryPersonInfo.do";
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-20;
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
		String endTime = String.valueOf(endNian)+ yu + r;
		String beginTime = beginNian +"0101";
		String urlPay = "https://www.wsgjj.com/WSYYT/qryPersonAccDetails.do?begDate="+beginTime+"&currentPage=1&endDate="+endTime+"&limits=1000";
		
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
		//System.out.println(html);
		HousingWenShanUserinfo userinfo = HousingWSParse.userinfo_parse(html);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingWenShanUserinfoRepository.save(userinfo);
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
		//System.out.println(html1);		
		List<HousingWenShanPay> listresul = HousingWSParse.paydetails_parse(html1,taskHousing);
		if(listresul==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			
			housingWenShanPayRepository.saveAll(listresul);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
			
			
		
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
}
