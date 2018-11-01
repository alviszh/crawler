package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Html;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3HtmlRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.common.AsyncShanXi3GetAllDataCommonService;
import app.service.common.CalendarParamService;
import app.service.common.LoginAndGetCommonService;
import app.service.common.RSA;
/**
 * @Description
 * @author sln
 * @date 2007年8月24日 下午6:16:55
 * 
 * 
 * 改版爬取方法记录：
 * 改版后，爬取通话和短信，需要经过中间请求，获取该手机号码是是否是预付费手机号，如果是，isPrepay 就是2，对应的通话和短信的pOffrTypeID就是11和12，否则就是1和2
 */

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shanxi3")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shanxi3")
public class AsyncShanXi3GetAllDataService implements ICrawlerLogin {
	@Autowired
	private AsyncShanXi3GetAllDataCommonService asyncShanXi3GetAllDataCommonService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private  LoginAndGetCommonService loginAndGetCommonService;
	@Autowired
	private RSA rsa;
	@Autowired
	private TelecomShanxi3HtmlRepository telecomShanxi3HtmlRepository;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	public String encryptedPhone;
	public String callPOffrTypeID=null;
	public String smsPOffrTypeID=null;

	/* (non-Javadoc)
	 * @see app.service.aop.ICrawler#getAllData(com.crawler.mobile.json.MessageLogin)
	 */
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			//将手机号加密：
			encryptedPhone = rsa.encryptedPhone(messageLogin.getName());
			tracer.addTag("RSA.encryptedPhone.encryptedPhone", "加密后的手机号是："+encryptedPhone);
		} catch (Exception e) {
			tracer.addTag("RSA.encryptedPhone.encryptedPhone.e",e.getMessage());
		}
		///////////////////////////////////////////////////////////////////////////
		//此处请求，用于跳过短信验证码，获取该手机号的付费方式，并将cookie存储到表中
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setTimeout(60000);
			webClient.getOptions().setJavaScriptEnabled(false);
			//根据如下链接获取爬取各种信息需要用到的cookie（首先根据登录的cookie获取让中间链接成功获取）
			String url="http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000196&cityCode=sn";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			if(200==statusCode){
				url="http://sn.189.cn/service/bill/initQueryTicket.action";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				page = webClient.getPage(webRequest);
				if(page!=null){
					statusCode = page.getWebResponse().getStatusCode();
					if(200==statusCode){
						String html = page.getWebResponse().getContentAsString();
						//保存页面信息
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("所需参数响应源码页_通话详单和短信详单");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomShanxi3HtmlRepository.save(htmlStore);
						if(html.contains("mobilesId")){
							Document doc = Jsoup.parse(html);
							String isPrepay = doc.getElementById("mobilesId").getElementsByTag("option").get(0).attr("title");
							isPrepay=isPrepay.split("#")[0];
							if(isPrepay.equals("2")){  //预付费
								callPOffrTypeID="11";
								smsPOffrTypeID="12";
							}else{  
								callPOffrTypeID="1";
								smsPOffrTypeID="2";
							}
							//中间请求通过，获取其cookie
							String cookies = CommonUnit.transcookieToJson(webClient);
							taskMobile.setCookies(cookies);
							taskMobileRepository.save(taskMobile);   //将中间请求的cookie更新到taskmobile表中
							tracer.addTag("action.crawler.getInitMy189homeWebClient.cookies",cookies+"======中间请求，跳过验证码的cookie已经更新到表中");
						}else{
							tracer.addTag("获取通话详单和短信详单所需参数响应的页面中未提供相关数据，源码是：","<xmp>"+html+"</xmp>");
						}
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("获取通话详单和短信详单所需参数过程中出现异常，异常内容是：", e.getMessage());
		}
		///////////////////////////////////////////////////////////////////////////		
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成

		//用户信息
		try {
			Future<String> future=asyncShanXi3GetAllDataCommonService.getUserInfo(messageLogin, taskMobile);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getUserInfo", e.toString());
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),404, "数据采集中，用户信息已采集完成");

		}
		//爬取业务信息
		try {
			Future<String> future =asyncShanXi3GetAllDataCommonService.getBusiness(messageLogin, taskMobile);
			listfuture.put("getBusiness", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getBusiness", e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),404, "数据采集中，业务信息已采集完成");

		}
		
		//爬取积分信息
		try{
			List<String> monthList = CalendarParamService.getMonth();
			for(int i=0;i<6;i++){
				String yearmonth = monthList.get(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getIntegra(messageLogin,taskMobile,yearmonth);
				listfuture.put(yearmonth+"getIntegra", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getIntegra", e.toString());
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，积分记录已采集完成");

		}
		//爬取亲情号通话信息
		try{
			for(int i=0;i<6;i++){
				String firstMonthdate = CalendarParamService.getFirstMonthdate(i);
				String lastMonthdate = CalendarParamService.getLastMonthdate(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getFamilyCall(messageLogin,taskMobile,firstMonthdate,lastMonthdate,i);
				listfuture.put(firstMonthdate+"至"+lastMonthdate+"getFamilyCall", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getFamilyCall", e.toString());
			crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，亲情号通话详单已采集完成");

		}
		//爬取通话详单
		try {
			for(int i=0;i<6;i++){
				String firstMonthdate = CalendarParamService.getFirstMonthdate(i);
				String lastMonthdate = CalendarParamService.getLastMonthdate(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getCallRecord(messageLogin,taskMobile,firstMonthdate,lastMonthdate,callPOffrTypeID);
				listfuture.put(firstMonthdate+"至"+lastMonthdate+"getCallRecord", future);
			}
		}catch (Exception e) {
			tracer.addTag("action.crawler.auth.getCallRecord", e.toString());
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),404, "数据采集中，通话详单已采集完成");

		}
			
		//爬取短信记录
		try {
			for(int i=0;i<6;i++){
				String firstMonthdate = CalendarParamService.getFirstMonthdate(i);
				String lastMonthdate = CalendarParamService.getLastMonthdate(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getSMSRecord(messageLogin, taskMobile,firstMonthdate,lastMonthdate,smsPOffrTypeID);
				listfuture.put(firstMonthdate+"至"+lastMonthdate+"getSMSRecord", future);
			}	
		}catch (Exception e) {
			tracer.addTag("action.crawler.auth.getSMSRecord", e.toString());
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),404, "数据采集中，短信详单已采集完成");

		}
		//爬取缴费信息
		//获取包括本月的6个月
		try {
			List<String> monthList= CalendarParamService.getMonthIncludeThis();
			for(int i=0;i<=5;i++){
				String month=monthList.get(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getChargeInfo(messageLogin, taskMobile,encryptedPhone,month);
				listfuture.put(month+"getChargeInfo", future);
			} 
		}catch (Exception e) {
			tracer.addTag("action.crawler.auth.getChargeInfo", e.toString());
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),404, "数据采集中，充值记录已采集完成");

		}
		//爬取月账单
		try {
			List<String> monthList = CalendarParamService.getMonth();
			for(int i=0;i<6;i++){
				String yearmonth = monthList.get(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getMonthBill(messageLogin,taskMobile,yearmonth,i);
				listfuture.put(yearmonth+"getMonthBill", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getMonthBill", e.toString());

		}
		//可用余额汇总信息
		//获取包括本月的6个月
		try {
			List<String> monthList= CalendarParamService.getMonthIncludeThis();
			for(int i=0;i<=5;i++){
				String month=monthList.get(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getBalanceSum(messageLogin, taskMobile,encryptedPhone,month);
				listfuture.put(month+"getBalanceSum", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getBalanceSum", e.toString());

		}
		//可用余额明细信息
		//获取包括本月的6个月
		try {
			List<String> monthList= CalendarParamService.getMonthIncludeThis();
			for(int i=0;i<=5;i++){
				String month=monthList.get(i);
				Future<String> future =asyncShanXi3GetAllDataCommonService.getBalanceDetail(messageLogin, taskMobile,encryptedPhone,month);
				listfuture.put(month+"getBalanceDetail", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getBalanceDetail", e.toString());
		}
		//爬取我的现状信息
		try {
			Future<String> future =asyncShanXi3GetAllDataCommonService.getCurrentSituation(messageLogin, taskMobile);
			listfuture.put("getCurrentSituation", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getCurrentSituation", e.toString());
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，账户信息已采集完成");
		}
		//爬取余额
		try {
			Future<String> future =asyncShanXi3GetAllDataCommonService.getBalance(messageLogin, taskMobile,encryptedPhone);
			listfuture.put("getBalance", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getBalance",e.toString());
		}
//		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracer.addTag("AsyncShanXi3GetAllDataService-listfuture--ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		return taskMobile;
	}

	/* (non-Javadoc)
	 * @see app.service.aop.ICrawler#getAllDataDone(java.lang.String)
	 */
	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see app.service.aop.ICrawlerLogin#login(com.crawler.mobile.json.MessageLogin)
	 */
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}
}

