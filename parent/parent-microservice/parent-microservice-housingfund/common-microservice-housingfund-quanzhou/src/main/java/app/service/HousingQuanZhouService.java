package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.quanzhou.HousingQuanZhouPay;
import com.microservice.dao.entity.crawler.housing.quanzhou.HousingQuanZhouPayDetails;
import com.microservice.dao.repository.crawler.housing.quanzhou.HousingQuanZhouPayDetailsRepository;
import com.microservice.dao.repository.crawler.housing.quanzhou.HousingQuanZhouPayRepository;

import app.crawler.htmlparse.HousingQZParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.quanzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.quanzhou")
public class HousingQuanZhouService extends HousingBasicService{
	@Autowired
	private HousingQuanZhouPayRepository housingQuanZhouPayRepository;
	
	@Autowired
	private HousingQuanZhouPayDetailsRepository housingQuanZhouPayDetailsRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient)  {
		String urlPay = "http://www.qzgjj.com/PAFundQuery/PersonAccount";
		//String urlPayDetailslist = "http://www.qzgjj.com/PAFundQuery/PersonAccountDetails?c=1";
		Page page = null;
		try {
			WebRequest webRequest1 = new WebRequest(new URL(urlPay), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			page = webClient.getPage(webRequest1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(page.getWebResponse().getContentAsString());
		tracer.addTag("个人信息html",page.getWebResponse().getContentAsString());
		HousingQuanZhouPay pay =  HousingQZParse.pay_parse(page.getWebResponse().getContentAsString());
		if(pay==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
//			pay.setUserid(messageLoginForHousing.getUser_id());
			pay.setTaskid(taskHousing.getTaskid());
			housingQuanZhouPayRepository.save(pay);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		
		
		HtmlPage htmlPage = (HtmlPage) page;
		HtmlElement button = (HtmlElement)htmlPage.getFirstByXPath("//*[@id='grid_id']/tbody/tr/td[13]/a");
		System.out.println("button"+button);
		try {
			htmlPage = button.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(htmlPage.getWebResponse().getContentAsString());
		List<HousingQuanZhouPayDetails> payDetails =  HousingQZParse.paydetails_parse(htmlPage.getWebResponse().getContentAsString(),taskHousing);
		if(payDetails==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			housingQuanZhouPayDetailsRepository.saveAll(payDetails);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
			
			
		
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
		
	}
	
	

}
