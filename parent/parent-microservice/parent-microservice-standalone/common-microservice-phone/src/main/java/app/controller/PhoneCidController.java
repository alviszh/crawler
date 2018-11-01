package app.controller;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.PhoneInquire;
import app.service.ProMobile;

@RequestMapping("/cid") 
@RestController
@Configuration
public class PhoneCidController {
	@Autowired
    private PhoneInquire phoneInquire; 
	@Autowired 
	private TracerLog tracerLog;
	@Autowired 
	private ProMobile proMobile;
	@Scheduled(fixedDelay=60000*60*12)
	public String getCid(){
		tracerLog.addTag("PhoneCidController","-----------系统定时开始------------");
		System.out.println("-----------系统定时开始------------");
		String cid = null;
		try {
			cid = phoneInquire.getPhoneCid();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			String url1 = "https://haoma.baidu.com/user_code_info?cid="+cid+"";
			WebRequest webRequest2 = new WebRequest(new URL(url1), HttpMethod.GET);
			Page searchPage2= webClient.getPage(webRequest2);
			String html = searchPage2.getWebResponse().getContentAsString();
			System.err.println(html);
			tracerLog.addTag("cid",cid);
			tracerLog.addTag("html",html);
			System.out.println("cid:"+cid);
			if(html.contains("1")){
//				while(true){
					cid = phoneInquire.getPhoneCid();
					String url2 = "https://haoma.baidu.com/user_code_info?cid="+cid+"";
					WebRequest webRequest3 = new WebRequest(new URL(url2), HttpMethod.GET);
					Page searchPage3= webClient.getPage(webRequest3);
					String html1 = searchPage3.getWebResponse().getContentAsString();
					System.err.println(html1);
					tracerLog.addTag("cid",cid);
					tracerLog.addTag("html",html);
					System.out.println("cid:"+cid);
					
//				}
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.addTag("PhoneInquireController.Error",e.getMessage());
		}
		
		
		//把inquire_phone_item_code 的 phonenum_flag，mark_type存入pro_mobile_report_call_detail_statistics 
		proMobile.getProMobile();
		
		return null;
	}
}
