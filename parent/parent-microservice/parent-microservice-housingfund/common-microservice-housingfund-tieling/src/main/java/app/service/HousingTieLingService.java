//package app.service;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.concurrent.Future;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import com.crawler.housingfund.json.MessageLoginForHousing;
//import com.crawler.mobile.json.StatusCodeRec;
//import com.gargoylesoftware.htmlunit.HttpMethod;
//import com.gargoylesoftware.htmlunit.Page;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebRequest;
//import com.gargoylesoftware.htmlunit.html.HtmlElement;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
//import com.microservice.dao.entity.crawler.housing.tieling.HousingTieLingPay;
//import com.microservice.dao.entity.crawler.housing.tieling.HousingTielingUserInfo;
//import com.microservice.dao.repository.crawler.housing.tieling.HousingTieLingPayRepository;
//import com.microservice.dao.repository.crawler.housing.tieling.HousingTieLingUserInfoRepository;
//
//import app.crawler.htmlparse.HousingTLParse;
//import app.service.common.HousingBasicService;
//
//@Component
//@Service
//@EnableAsync
//@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tieling")
//@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tieling")
//public class HousingTieLingService  extends HousingBasicService{
//	public static final Logger log = LoggerFactory.getLogger(HousingTieLingService.class);
//	@Autowired
//	private HousingTieLingUserInfoRepository housingTieLingUserInfoRepository;
//	@Autowired
//	private HousingTieLingPayRepository housingTieLingPayRepository;
//	@Async
//	public  TaskHousing getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient,HtmlPage htmlpage) {
//		String urlPay = "http://www.tlgjj.com.cn/grmx.jhtml";
//		String urlInfo = "http://www.tlgjj.com.cn/grjc.jhtml";
//		Page page = null;
//		//基本信息
//		try {
////			WebRequest webRequest = new WebRequest(new URL(urlInfo), HttpMethod.GET);
////			webClient.setJavaScriptTimeout(50000); 
////			webClient.getOptions().setTimeout(50000); // 15->60 
////			
////			page = webClient.getPage(webRequest);
//			//page = loginAndGetCommon.getHtml(urlInfo, webClient);
//			HtmlElement button = htmlpage.getFirstByXPath("/html/body/div[5]/div[1]/div[2]/div/ul/li[2]/a");
//			page = button.click();
//			Thread.sleep(3000);
//			Document doc = Jsoup.parse(htmlpage.getWebResponse().getContentAsString());
//			Elements pages = doc.select("#CO_TotalPage");
//			Elements acc = doc.select("li.tit");
//			String accs = null;
//			if(acc.size()>0){
//				accs = acc.get(1).text().trim();
//				accs = accs.substring(accs.lastIndexOf("：")+1);
//				String accNum = accs.substring(0, accs.lastIndexOf("退"));
//				String u = "http://www.tlgjj.com.cn/sendServlet.do?TranCode=111140&AccNum=113003871446&task=send";
//				WebRequest webRequest = new WebRequest(new URL(u), HttpMethod.POST);
//				webClient.setJavaScriptTimeout(50000); 
//				webClient.getOptions().setTimeout(50000); // 15->60 
//				webClient.getOptions().setJavaScriptEnabled(false);
//				page = webClient.getPage(webRequest);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println(page.getWebResponse().getContentAsString());
//		HousingTielingUserInfo userinfo = HousingTLParse.userinfo_parse(page.getWebResponse().getContentAsString());
//		userinfo.setTaskid(taskHousing.getTaskid());
//		housingTieLingUserInfoRepository.save(userinfo);
//		taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
//		taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
//		save(taskHousing);
//		updateTaskHousing(taskHousing.getTaskid());
//		//缴费明细
//		try {
//			page = loginAndGetCommon.getHtml(urlPay, webClient);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//System.out.println(page.getWebResponse().getContentAsString());
//		List<String> urlPayDetailslist = getURLforPayDetails(page.getWebResponse().getContentAsString());
//		for (String url_result : urlPayDetailslist) {
//			try {
//				WebRequest webRequest = new WebRequest(new URL(url_result), HttpMethod.POST);
//				webClient.setJavaScriptTimeout(50000); 
//				webClient.getOptions().setTimeout(50000); // 15->60 
//				webClient.getOptions().setJavaScriptEnabled(false);
//				page = webClient.getPage(webRequest);
//				//page = loginAndGetCommon.getHtml(url_result, webClient);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			List<HousingTieLingPay> listresul = HousingTLParse.recodetails_parse(page.getWebResponse().getContentAsString());
//			for(HousingTieLingPay result : listresul){
//				result.setTaskid(taskHousing.getTaskid());
//				housingTieLingPayRepository.save(result);
//			}
//		}
//		taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
//		taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
//		save(taskHousing);
//		updateTaskHousing(taskHousing.getTaskid());
//		
//		return taskHousing;
//	}
//	
//	public  List<String> getURLforPayDetails(String html) {
//		List<String> urllist = new ArrayList<>();
//		Document doc = Jsoup.parse(html);
//		Elements pages = doc.select("#CO_TotalPage");
//		Elements acc = doc.select("li.tit");
//		String accs = null;
//		if(acc.size()>0){
//			accs = acc.get(1).text().trim();
//			accs = accs.substring(accs.lastIndexOf("：")+1);
//			String accNum = accs.substring(0, accs.lastIndexOf("退"));
//			String page = pages.text().trim();
//			System.out.println(page);
//			int pa = Integer.parseInt(page);
//			
//			Calendar now = Calendar.getInstance();
//			int endNian = now.get(Calendar.YEAR);
//			int beginNian = endNian-2;
//			int yue = now.get(Calendar.MONTH) +1;
//			int ri = now.get(Calendar.DAY_OF_MONTH);
//			String beginDate =beginNian+"-"+ yue+"-"+ri;
//			String endDate =endNian+"-"+ yue+"-"+ri;
//			for (int i = 1;i<=pa;i++) {
//				String url = "http://www.tlgjj.com.cn/sendServlet.do?task=ftp&TranCode=111141"
//						+ "&BeginDate="+beginDate+"&EndDate="+endDate+"&CI_Pagenum="+i+"&CI_Count=8&AccNum="+accNum+"";
//				System.out.println("====" + url);
//				urllist.add(url);
//			}
//		}
//		
//		
//		
//		return urllist;
//	}
//
//}
