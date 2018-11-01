package app.service;

import java.io.IOException;
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
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yueyang.HousingYueYangPay;
import com.microservice.dao.entity.crawler.housing.yueyang.HousingYueYangUserinfo;
import com.microservice.dao.repository.crawler.housing.yueyang.HousingYueYangPayRepository;
import com.microservice.dao.repository.crawler.housing.yueyang.HousingYueYangUserinfoRepository;

import app.crawler.htmlparse.HousingYYParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yueyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yueyang")
public class HousingYueYangService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingYueYangService.class);
	
	@Autowired
	private HousingYueYangUserinfoRepository housingYueYangUserinfoRepository;
	@Autowired
	private HousingYueYangPayRepository housingYueYangPayRepository;
	
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "http://yygjj.gov.cn/hfmis_wt/personal/ace/main/grcx_index";
		
		Page page = null;
		//基本信息
		try {
			WebRequest webRequest = new WebRequest(new URL(urlInfo), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("个人信息html", html);
		System.out.println(html);
		String spcode = html.substring(html.indexOf("spcode = "),html.indexOf("var bdsjh")).trim();
		spcode = spcode.substring(spcode.indexOf("=")+3, spcode.indexOf(";")-1);
		
		String urlInfo1 = "http://yygjj.gov.cn/hfmis_wt/common/zhfw/invoke/01A003?grzh="+spcode+"&zjhm="+messageLoginForHousing.getNum()+"&ishj=0";
		try {
			WebRequest webRequest = new WebRequest(new URL(urlInfo1), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			page = webClient.getPage(webRequest);
			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html2 = page.getWebResponse().getContentAsString();
		tracer.addTag("个人信息html2", html2);
		System.out.println(html2);
		HousingYueYangUserinfo userinfo = HousingYYParse.userinfo_parse(html2);
		if(userinfo==null){
			//System.out.println("111111");
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			//System.out.println("222222");
			userinfo.setTaskid(taskHousing.getTaskid());
			housingYueYangUserinfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		
		//个人对账明细
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int start = endNian-20;
		String urlPay = "http://yygjj.gov.cn/hfmis_wt/common/zhfw/invoke/020102";
		try {
			WebRequest webRequest = new WebRequest(new URL(urlPay), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "yygjj.gov.cn");
			webRequest.setAdditionalHeader("Origin", "http://yygjj.gov.cn");
			webRequest.setAdditionalHeader("Referer", "http://yygjj.gov.cn/hfmis_wt/personal/ace/jcmx");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			String requestBody = "filterscount=0&groupscount=0&pagenum=0&pagesize=1000&recordstartindex=0&recordendindex=1000&start="+start+"0101&end="+endNian+"1231&zjhm="+messageLoginForHousing.getNum()+"&grzh="+spcode+"";
			webRequest.setRequestBody(requestBody);
			page = webClient.getPage(webRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String html1 = page.getWebResponse().getContentAsString();
		tracer.addTag("缴费信息html", html1);
		System.out.println(html1);
		List<HousingYueYangPay> listresul = HousingYYParse.paydetails_parse(html1,taskHousing);
		if(listresul==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			
			housingYueYangPayRepository.saveAll(listresul);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
//		for(int i = 0;i<=3;i++){
//			int date = endNian-i;
//			String urlPay = "http://yygjj.gov.cn/mx_info.do?hjnf="+date+"";
//			try {
//				WebRequest webRequest = new WebRequest(new URL(urlPay), HttpMethod.GET);
//				webClient.setJavaScriptTimeout(50000); 
//				webClient.getOptions().setTimeout(50000); // 15->60 
//				page = webClient.getPage(webRequest);
//				//page = loginAndGetCommon.getHtml(urlInfo, webClient);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
				
				
			
		
		
	}
}
