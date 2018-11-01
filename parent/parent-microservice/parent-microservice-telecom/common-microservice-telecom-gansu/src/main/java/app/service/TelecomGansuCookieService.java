package app.service;

import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.gansu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.gansu")
public class TelecomGansuCookieService implements ICrawlerLogin{

//	@Autowired
//	private TelecomCommonGansuService telecomCommonGansuService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomGansuService telecomGansuService;
//	//获取全能cookie
//	public void getCookie(MessageLogin messageLogin, TaskMobile taskMobile) {
//		taskMobile = telecomCommonGansuService.findtaskMobile(messageLogin.getTask_id());
//		tracer.addTag("parser.telecom.crawler.getCookie", taskMobile.getTaskid());
//				try {
//					//缴费获取cookie
//					HtmlPage myPage2 = getMyPage2(taskMobile,messageLogin);
//					if(null != myPage2)
//					{
//						WebClient client = myPage2.getWebClient();
//						try {    
//							telecomCommonGansuService.getAllData(messageLogin, taskMobile,client);
//						} catch (Exception e) {
//							tracer.addTag("parser.crawler.getAllData.ERROR", messageLogin.getTask_id());
//						}
//						//账单
//						try {
//							telecomGansuService.getPhoneBillInfo(messageLogin,taskMobile,client);
//						} catch (Exception e) {
//							tracer.addTag("parser.crawler.getMessage.ERROR", messageLogin.getTask_id());
//						}
//						
//						//缴费信息
//						try {
//							telecomGansuService.getPayMent(messageLogin,taskMobile,client);
//						} catch (Exception e) {
//							tracer.addTag("parser.crawler.getPayMent.ERROR", messageLogin.getTask_id());
//						}
//						
//						//通话记录
//						telecomGansuService.getCall(messageLogin,taskMobile,client);
//						
//						//短信记录
//						telecomGansuService.getMessage(messageLogin,taskMobile,client);
//					}
//					else
//					{
//						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
//						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
//						taskMobile.setDescription("网站出现异常，请您重新尝试(新入网用户)");
//						taskMobileRepository.save(taskMobile);
//					}
//				} catch (Exception e1) {
//						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
//						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
//						taskMobile.setDescription("网站出现异常，请您重新尝试！");
//						taskMobileRepository.save(taskMobile);
//						e1.printStackTrace();
//				}
//	}
	

	//添加cookie
	public WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}
	
	//根据URL获得Htmlpage
	public HtmlPage getHtml(String url, WebClient webClient)  {
		webClient.getCache().setMaxSize(0);
		
		HtmlPage searchPage=null;
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			 searchPage = webClient.getPage(webRequest);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.ERROR", "ERROR");
		}
		
		return searchPage;
	}

	public  HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type) throws Exception { 
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET); 
		HtmlPage searchPage = webClient.getPage(webRequest); 
		int statusCode = searchPage.getWebResponse().getStatusCode(); 
		if (200 == statusCode) { 
			return searchPage; 
		} 
		return null; 
	}
	
	public HtmlPage getMyPage2(TaskMobile taskMobile,MessageLogin messageLogin){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebClient client = addcookie(webClient, taskMobile);
		try {    
			String url ="http://gs.189.cn/web/pay2/dealCheckV7.action?numberType=4%3A"+messageLogin.getName()+"&beginTime="+getPayTime(1)+"&endTime="+getPayTime(1)+"&productInfo=4%3A"+messageLogin.getName()+"&validatecode=&busitype=3&mobilenum=&rand=Name";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			HtmlPage page1 = client.getPage(webRequest);
//			HtmlPage page1 = telecomRetryGanSuService.getRetry(taskMobile,messageLogin,url,client);
			if(null !=page1)
			{
				int code2 = page1.getWebResponse().getStatusCode();
				if(code2==200)
				{
					System.out.println("————————————————"+page1.getWebResponse().getContentAsString());
					return page1;
				}
			}
		} catch (Exception e) {
			tracer.addTag("parser.getMyPage2.getcookie.Exception", taskMobile.getTaskid());
		}
		return null;
	}
	
	//交费时间
	public String getPayTime(int time) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -time);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}


	public void crawler(MessageLogin messageLogin, TaskMobile taskMobile) {
		//缴费获取cookie
		HtmlPage page1 = getMyPage2(taskMobile,messageLogin);
		WebClient webClient2 = page1.getWebClient();
		//个人信息
		try{
			telecomGansuService.getUserInfo(messageLogin, taskMobile,webClient2);
		}
		catch(Exception e){
			tracer.addTag("parser.crawler.getUserInfo.ERROR", messageLogin.getTask_id());
		}
		//业务
		try{
			telecomGansuService.getBusiness(messageLogin,taskMobile,webClient2);
		}catch (Exception e) {
		    tracer.addTag("parser.crawler.getBusiness.ERROR", messageLogin.getTask_id());
		}
		
		//月账单
		try {
			telecomGansuService.getPhoneMonthBillInfo(messageLogin,taskMobile,webClient2);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.getPhoneMonthBillInfo.ERROR",messageLogin.getTask_id());
		}
		
		telecomGansuService.getPhoneBillInfo(messageLogin,taskMobile,webClient2);
		
		telecomGansuService.getPayMent(messageLogin,taskMobile,webClient2);
		
		String cookieString = CommonUnit.transcookieToJson(webClient2);
		taskMobile.setNexturl(cookieString);
		taskMobileRepository.save(taskMobile);
	}
	
	
	public void crawlerAll(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebClient webClient = addcookieNet(taskMobile);
		//亲情号码
		try{
			telecomGansuService.getFamilyMsg(messageLogin, taskMobile,webClient);
		}
		catch(Exception e){
			tracer.addTag("parser.crawler.getFamilyMsg.ERROR", messageLogin.getTask_id());
		}
		
		//积分
		try{
			telecomGansuService.getJiFen(messageLogin,taskMobile,webClient);
		}catch(Exception e){
			tracer.addTag("parser.crawler.getJiFen.ERROR", messageLogin.getTask_id());
		}
		
		
		//通话记录
		telecomGansuService.getCall(messageLogin,taskMobile,webClient);
		
		//短信记录
		telecomGansuService.getMessage(messageLogin,taskMobile,webClient);
	}
	
	
	public WebClient addcookieNet(TaskMobile taskMobile) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getNexturl());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		crawler(messageLogin, taskMobile);
		crawlerAll(messageLogin, taskMobile);
		return taskMobile;
	}


	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

}
