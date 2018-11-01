package app.service;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiBalance;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiBusiness;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiCallRecord;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiChargeInfo;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiCurrentSituation;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiHtml;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiIntegra;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiMonthBill;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiSMSRecord;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiBalanceRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiBusinessRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiChargeInfoRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiCurrentSituationRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiIntegraRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiMonthBillRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiSMSRecordRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.TelecomJiangxiParser;
import app.service.common.CalendarParamService;
import app.service.common.LoginAndGetCommonService;


/**
 * 
 * @Description: 所有信息爬取
 * @author sln
 * @date 2017年9月16日
 */
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.jiangxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.jiangxi")
public class AsyncJiangxiGetAllDataService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private LoginAndGetCommonService loginAndGetCommonService;
	@Autowired
	private TelecomJiangxiParser telecomJiangxiParser;
	@Autowired
	private TelecomJiangxiHtmlRepository telecomJiangxiHtmlRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomJiangxiUserInfoRepository telecomJiangxiUserInfoRepository;
	@Autowired
	private TelecomJiangxiIntegraRepository telecomJiangxiIntegraRepository;
	@Autowired
	private TelecomJiangxiBusinessRepository telecomJiangxiBusinessRepository;
	@Autowired
	private TelecomJiangxiCurrentSituationRepository telecomJiangxiCurrentSituationRepository;
	@Autowired
	private TelecomJiangxiMonthBillRepository telecomJiangxiMonthBillRepository;
	@Autowired
	private TelecomJiangxiChargeInfoRepository telecomJiangxiChargeInfoRepository;
	@Autowired
	private TelecomJiangxiBalanceRepository telecomJiangxiBalanceRepository;
	@Autowired
	private TelecomJiangxiCallRecordRepository telecomJiangxiCallRecordRepository;
	@Autowired
	private TelecomJiangxiSMSRecordRepository telecomJiangxiSMSRecordRepository;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	/**
	 * 爬取用戶信息
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getUserInfo(TaskMobile taskMobile)  {
		try {
			WebClient webClient = loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setJavaScriptEnabled(false);   //禁止js运行
			String url="http://jx.189.cn/service/account/seeInfo.jsp";
			WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			if(null!=page){
				int statusCode = page.getWebResponse().getStatusCode();
				String html = page.getWebResponse().getContentAsString();
				if(200==statusCode){
					WebParam<TelecomJiangxiUserInfo> webParam= telecomJiangxiParser.userInfoParser(taskMobile,html);
					//保存页面信息
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("用户信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getUserInfo.html", "用户信息源码页已经入库");
					if(null!=webParam.getTelecomJiangxiUserInfo()){
						telecomJiangxiUserInfoRepository.save(webParam.getTelecomJiangxiUserInfo());
						tracer.addTag("action.crawler.getUserInfo", "用户信息已入库");
						crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode(), "数据采集中，用户信息已采集完成");
					}else{
						crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),201, "数据采集中，用户信息已采集完成");
					}
				}else{			
					//无查询记录或者是出现其他情况
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("用户信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getUserInfo.html"+System.currentTimeMillis(), "用户信息源码页已采集完成");
					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),201, "数据采集中，用户信息已采集完成");				
				}
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),404, "数据采集中，用户信息已采集完成");
			tracer.addTag("action.crawler.getUserInfo.e",taskMobile.getTaskid()+"  "+e.toString());
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取积分信息
	 * @param messageLogin 
	 * @param taskMobile
	 * @param webClient 
	 * @param qryMonth
	 * @return 
	 */
	@Async
	public Future<String> getIntegra(MessageLogin messageLogin, TaskMobile taskMobile, String qryMonth) {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url = "http://jx.189.cn/jf/getUserBonus.jsp?LAN_CODE="+taskMobile.getAreacode()+"&ACC_MONTH="+qryMonth+"&ACCOUNT_TYPE=80000045&ACCOUNT_NO="+messageLogin.getName()+"";
			Page tPage = loginAndGetCommonService.getPage(url, webClient);
			if(tPage!=null){	
				int integraStatusCode=tPage.getWebResponse().getStatusCode();
				String html = tPage.getWebResponse().getContentAsString();
				if(200 == integraStatusCode){
					List<TelecomJiangxiIntegra> list =telecomJiangxiParser.integraParser(html,taskMobile,qryMonth);
					if(null != tPage){
						TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(qryMonth+"积分信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomJiangxiHtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getIntegra.html"+qryMonth,"积分信息源码页已经入库");
					}
					if(null != list){
						telecomJiangxiIntegraRepository.saveAll(list);
						tracer.addTag("action.crawler.getIntegra"+qryMonth,"积分信息已入库");
						crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode(), "数据采集中，"+qryMonth+"积分信息已采集完成");
					}else{
						//更新task_mobile表的相关信息	
						crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),201,"数据采集中，"+qryMonth+"无积分记录或此时处于月初或月末，无法查询指定月积分");
					}
				}else{
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(qryMonth+"积分信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getIntegra.html"+qryMonth, "积分信息源码页无积分记录或此时处于月初或月末，无法查询指定月积分");
					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，"+qryMonth+"无积分记录或此时处于月初或月末，无法查询指定月积分");
				}
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，无积分记录或此时处于月初或月末，无法查询指定月积分");
			tracer.addTag("action.crawler.getIntegra.e",taskMobile.getTaskid()+"  "+e.toString());
		}
		
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取业务信息
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getBusiness(TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://jx.189.cn/service/account/modules/m_122.jsp";
			Page page = loginAndGetCommonService.getHtml(url, webClient);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				String html =page.getWebResponse().getContentAsString();
				if(200==statusCode){
					List<TelecomJiangxiBusiness> list= telecomJiangxiParser.businessParser(taskMobile,html);
					if(null !=page){
						TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("业务信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomJiangxiHtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBusiness.html", "业务信息源码页已经入库");
					}
					if(null != list){
						telecomJiangxiBusinessRepository.saveAll(list);
						tracer.addTag("action.crawler.getBusiness", "业务信息已入库");
						crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode(), "数据采集中，业务信息已采集完成");
						
					}else{
						crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),201, "数据采集中，业务信息已采集完成");
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBusiness.e",taskMobile.getTaskid()+"  "+e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),404, "数据采集中，业务信息已采集完成");
		}
		
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取首页我的现状信息
	 * @param taskMobile
	 * @return 
	 */
	@Async
	public Future<String> getCurrentSituation(TaskMobile taskMobile) {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String urlHuaFei="http://www.189.cn/dqmh/order/getHuaFei.do";
			String urlTaoCan="http://www.189.cn/dqmh/order/getTaoCan.do";
			Page pageHuaFei = loginAndGetCommonService.getPage(urlHuaFei, webClient);
			Page pageTaoCan = loginAndGetCommonService.getPage(urlTaoCan, webClient);
			int statusCodeHuaFei = pageHuaFei.getWebResponse().getStatusCode();
			int statusCodeTaoCan=pageTaoCan.getWebResponse().getStatusCode(); 
			if(200==statusCodeHuaFei && 200==statusCodeTaoCan){
				String htmlHuaFei=pageHuaFei.getWebResponse().getContentAsString();
				String htmlTaoCan=pageTaoCan.getWebResponse().getContentAsString();
				WebParam<TelecomJiangxiCurrentSituation> webParam= telecomJiangxiParser.currentSituationParser(taskMobile,htmlHuaFei,htmlTaoCan);
				//保存页面信息
				TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
				htmlStore.setTaskid(taskMobile.getTaskid());
				htmlStore.setType("我的现状信息源码页");
				htmlStore.setPageCount(1);
				htmlStore.setUrl("话费的url "+urlHuaFei+" 华丽丽的分界线 "+" 套餐的url "+urlTaoCan);
				htmlStore.setHtml("话费的html "+htmlHuaFei+" 信息分界线 "+" 套餐的html "+htmlTaoCan);
				List<String> monthList= CalendarParamService.getMonthIncludeThis();
				htmlStore.setYearmonth(monthList.get(0));
				telecomJiangxiHtmlRepository.save(htmlStore);
				tracer.addTag("action.crawler.getCurrentSituation.html", "我的现状信息源码页已经入库");
				if(null!=webParam.getTelecomJiangxiCurrentSituation()){
					telecomJiangxiCurrentSituationRepository.save(webParam.getTelecomJiangxiCurrentSituation());
					tracer.addTag("action.crawler.getCurrentSituation", "我的现状信息已入库");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getCurrentSituation.e",taskMobile.getTaskid()+"  "+e.toString());
		}
		return new AsyncResult<String>("200");
		
	}
	/**
	 * 爬取月账单信息
	 * @param messageLogin 
	 * @param taskMobile
	 * @param yearmonth
	 * @return 
	 */
	@Async
	public Future<String> getMonthBill(MessageLogin messageLogin, TaskMobile taskMobile, String yearmonth) {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://jx.189.cn/service/bill/e_billing/?month="+yearmonth+"";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			String html =page.getWebResponse().getContentAsString();		
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					if(!html.contains("对不起，账单查询失败！")){ //若是爬取没有数据的月份，也会这样提示
						List<TelecomJiangxiMonthBill> list= telecomJiangxiParser.getMonthBill(messageLogin,taskMobile,html,yearmonth);
						if(null !=page){
							TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
							htmlStore.setTaskid(taskMobile.getTaskid());
							htmlStore.setType(yearmonth+"月账单信息源码页");
							htmlStore.setPageCount(1);
							htmlStore.setUrl(url);
							htmlStore.setHtml(html);
							htmlStore.setYearmonth(yearmonth);
							telecomJiangxiHtmlRepository.save(htmlStore);
							tracer.addTag("action.crawler.getMonthBill.html"+yearmonth, "月账单信息源码页已经入库");
						}
						if(null != list){
							telecomJiangxiMonthBillRepository.saveAll(list);
							tracer.addTag("action.crawler.getMonthBill"+yearmonth, "月账单信息已入库");
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(yearmonth+"月账单信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomJiangxiHtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getMonthBill.html"+yearmonth, "月账单信息源码页已采集完成");
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMonthBill.e",taskMobile.getTaskid()+"  "+e.toString());
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取充值记录
	 * @param messageLogin
	 * @param taskMobile
	 * @param webClient
	 * @param qryMonth
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getChargeInfo(MessageLogin messageLogin, TaskMobile taskMobile, String qryMonth)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			   params.add(new NameValuePair("callCount", "1"));  
			   params.add(new NameValuePair("page", "/2017/fee.jsp"));
			   params.add(new NameValuePair("httpSessionId", ""));
			   params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
			   params.add(new NameValuePair("c0-scriptName", "Service"));
			   params.add(new NameValuePair("c0-methodName", "excute"));
			   params.add(new NameValuePair("c0-id", "0"));   //参数1
			   params.add(new NameValuePair("c0-param0", "string:FEE_QRY_SERVICE"));   //参数2
			   params.add(new NameValuePair("c0-param1", "boolean:false"));
			   params.add(new NameValuePair("c0-e1", "string:"+qryMonth+""));
			   params.add(new NameValuePair("c0-e2", "string:QRY_PAYMENT_IN_MONTH_BY_LOGIN_NBR"));
			   params.add(new NameValuePair("c0-param2", "Object_Object:{month:reference:c0-e1, method:reference:c0-e2}"));
			   params.add(new NameValuePair("batchId", "7"));
			webRequest.setRequestParameters(params);
//			webRequest.setRequestBody(payload);
			webRequest.setAdditionalHeader("Accept", "*/*");    
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
			webRequest.setAdditionalHeader("Connection", "keep-alive");      
			webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
			webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
			webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/2017/fee.jsp"); 
			
			Page page = loginAndGetCommonService.gethtmlPostByWebRequest(webClient, webRequest);
			String html = page.getWebResponse().getContentAsString();
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					List<TelecomJiangxiChargeInfo> list= telecomJiangxiParser.chargeInfoParser(taskMobile,html,qryMonth);
					//保存页面信息
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(qryMonth+"充值记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					htmlStore.setYearmonth(qryMonth);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getChargeInfo.html"+qryMonth, "充值记录源码页已经入库");
					if(null!=list && list.size()>0){
						telecomJiangxiChargeInfoRepository.saveAll(list);
						tracer.addTag("action.crawler.getChargeInfo"+qryMonth, "充值记录已入库");
						crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode(), "数据采集中，"+qryMonth+"充值记录已采集完成");
					}else{
						crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),200, "数据采集中，"+qryMonth+"充值记录已采集完成");
					}
				}else{				
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(qryMonth+"充值记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getChargeInfo.html"+qryMonth, "充值记录源码页已采集完成");
					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),201, "数据采集中,"+qryMonth+"充值记录已采集完成");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeInfo.e",taskMobile.getTaskid()+"  "+e.toString());
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),404, "数据采集中,充值记录已采集完成");
		}
		
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取余额
	 * @param messageLogin
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getBalance(MessageLogin messageLogin, TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			   params.add(new NameValuePair("callCount", "1"));  
			   params.add(new NameValuePair("page", "/newpay/netbank/"));
			   params.add(new NameValuePair("httpSessionId", ""));
			   params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
			   params.add(new NameValuePair("c0-scriptName", "Service"));
			   params.add(new NameValuePair("c0-methodName", "excute"));
			   params.add(new NameValuePair("c0-id", "0"));   //参数1
			   params.add(new NameValuePair("c0-param0", "string:NEW_ACCOUNT_QUERY"));   //参数2
			   params.add(new NameValuePair("c0-param1", "boolean:false"));
			   params.add(new NameValuePair("c0-e1", "string:"+taskMobile.getAreacode()+""));
			   params.add(new NameValuePair("c0-e2", "number:80000045"));
			   params.add(new NameValuePair("c0-e3", "string:undefined"));
			   params.add(new NameValuePair("c0-e4", "string:"+messageLogin.getName()+""));
			   params.add(new NameValuePair("c0-param2", "Object_Object:{areaCode:reference:c0-e1, productId:reference:c0-e2, accountId:reference:c0-e3, phone:reference:c0-e4}"));
			   params.add(new NameValuePair("batchId", "7"));
		    webRequest.setRequestParameters(params);
		    webRequest.setAdditionalHeader("Accept", "*/*");    
		    webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
		    webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
		    webRequest.setAdditionalHeader("Connection", "keep-alive");      
		    webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
		    webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
	        webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
	        webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/newpay/netbank/"); 
	        webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
		    Page page = loginAndGetCommonService.gethtmlPostByWebRequest(webClient, webRequest);
		    String html = page.getWebResponse().getContentAsString();
			tracer.addTag("action.crawler.getBalance.page", "请求链接返回的页面是：taskid是：");
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					WebParam<TelecomJiangxiBalance> webParam= telecomJiangxiParser.BalanceParser(taskMobile,html);
					//保存页面信息
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("余额信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getBalance.html", "余额信息源码页已经入库");
					if(null!=webParam.getTelecomJiangxiBalance()){
						telecomJiangxiBalanceRepository.save(webParam.getTelecomJiangxiBalance());
						tracer.addTag("action.crawler.getBalance", "余额信息已入库");
						crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，余额信息已采集完成");
						
					}else{
						crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),201, "数据采集中，余额信息已采集完成");
						
					}
				}else{
					//无查询记录或者是出现其他情况
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("余额信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getBalance.html", "余额信息源码页已采集完成");
					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),201, "数据采集中，余额信息已采集完成");
					
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBalance.e",taskMobile.getTaskid()+"  "+e.toString());
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),404, "数据采集中，余额信息已采集完成");
		}
		
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取通话详单
	 * @param messageLogin
	 * @param taskMobile
	 * @param qryMonth
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getCallRecord(MessageLogin messageLogin, TaskMobile taskMobile, String qryMonth)  {
		try {
			String url="http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new NameValuePair("callCount", "1"));  
			params.add(new NameValuePair("page", "/2017/details.jsp"));
			params.add(new NameValuePair("httpSessionId", ""));
			params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
			params.add(new NameValuePair("c0-scriptName", "Service"));
			params.add(new NameValuePair("c0-methodName", "excute"));
			params.add(new NameValuePair("c0-id", "0"));   //参数1
			params.add(new NameValuePair("c0-param0", "string:DETAILS_SERVICE"));   //参数2
			params.add(new NameValuePair("c0-param1", "boolean:false"));
			params.add(new NameValuePair("c0-e1", "string:"+qryMonth+""));
			params.add(new NameValuePair("c0-e2", "string:7"));
			params.add(new NameValuePair("c0-e3", "string:"+messageLogin.getSms_code()+""));
			params.add(new NameValuePair("c0-e4", "string:QRY_DETAILS_BY_LOGIN_NBR"));
			params.add(new NameValuePair("c0-param2", "Object_Object:{month:reference:c0-e1, query_type:reference:c0-e2, valid_code:reference:c0-e3, method:reference:c0-e4}"));
			params.add(new NameValuePair("batchId", "3"));
			webRequest.setRequestParameters(params);
			webRequest.setAdditionalHeader("Accept", "*/*");    
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
			webRequest.setAdditionalHeader("Connection", "keep-alive");      
			webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
			webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
			webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/2017/details.jsp"); 
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
			HtmlPage hPage = webClient.getPage(webRequest);
			if(hPage!=null){	
				String html=hPage.getWebResponse().getContentAsString();
				int integraStatusCode=hPage.getWebResponse().getStatusCode();
				if(200 == integraStatusCode){
					List<TelecomJiangxiCallRecord> list =telecomJiangxiParser.callRecordParser(html,taskMobile,qryMonth);
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(qryMonth+"通话记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getCallRecord.html"+qryMonth,"通话记录源码页已经入库");
					if(null!=list && list.size()>0 ){
						telecomJiangxiCallRecordRepository.saveAll(list);
						tracer.addTag("action.crawler.getCallRecord"+qryMonth,"通话记录已入库");
						crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_CallRecordStatus_SUCESS.getCode(), "数据采集中，"+qryMonth+"通话记录已采集完成");
					}else{
						//更新task_mobile表的相关信息	
						crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),201, "数据采集中，"+qryMonth+"通话记录已采集完成");
					}
				}else{
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(qryMonth+"通话记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getCallRecord.html"+qryMonth, "通话记录源码页已采集完成");
					crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 201,"数据采集中，"+qryMonth+"通话记录已采集完成");
				}
			}
		} catch (Exception e) {
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
							"通话记录",qryMonth,taskMobile.getCarrier(),taskMobile.getCity(),
							"timeout", "连接超时", 1);							
			mobileDataErrRecRepository.save(m);
			tracer.addTag("action.crawler.getCallRecord.e",taskMobile.getTaskid()+"  "+e.toString());
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 404, "数据采集中,通话记录已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取短信信息
	 * @param messageLogin
	 * @param taskMobile
	 * @param webClient
	 * @param qryMonth
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getSMSRecord(MessageLogin messageLogin, TaskMobile taskMobile,String qryMonth) throws  Exception {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new NameValuePair("callCount", "1"));  
			params.add(new NameValuePair("page", "/2017/details.jsp"));
			params.add(new NameValuePair("httpSessionId", ""));
			params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
			params.add(new NameValuePair("c0-scriptName", "Service"));
			params.add(new NameValuePair("c0-methodName", "excute"));
			params.add(new NameValuePair("c0-id", "0"));   //参数1
			params.add(new NameValuePair("c0-param0", "string:DETAILS_SERVICE"));   //参数2
			params.add(new NameValuePair("c0-param1", "boolean:false"));
			params.add(new NameValuePair("c0-e1", "string:"+qryMonth+""));
			params.add(new NameValuePair("c0-e2", "string:8"));
			params.add(new NameValuePair("c0-e3", "string:"+messageLogin.getSms_code()+""));
			params.add(new NameValuePair("c0-e4", "string:QRY_DETAILS_BY_LOGIN_NBR"));
			params.add(new NameValuePair("c0-param2", "Object_Object:{month:reference:c0-e1, query_type:reference:c0-e2, valid_code:reference:c0-e3, method:reference:c0-e4}"));
			params.add(new NameValuePair("batchId", "2"));
			webRequest.setRequestParameters(params);
			webRequest.setAdditionalHeader("Accept", "*/*");    
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
			webRequest.setAdditionalHeader("Connection", "keep-alive");      
			webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
			webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
			webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/2017/details.jsp"); 
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
			HtmlPage hPage = webClient.getPage(webRequest);
			if(hPage!=null){	
				String html=hPage.getWebResponse().getContentAsString();
				int integraStatusCode=hPage.getWebResponse().getStatusCode();
				if(200 == integraStatusCode){
					List<TelecomJiangxiSMSRecord> list =telecomJiangxiParser.smsRecordParser(html,taskMobile,qryMonth);
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(qryMonth+"短信记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getSMSRecord.html"+qryMonth,"短信记录源码页已经入库");
					if(null != list && list.size()>0){
						telecomJiangxiSMSRecordRepository.saveAll(list);
						tracer.addTag("action.crawler.getSMSRecord"+qryMonth,"短信记录已入库");
						crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_SMSRecordStatus_SUCESS.getCode(), "数据采集中,"+qryMonth+"短信记录已采集完成");
					}else{
						//更新task_mobile表的相关信息	
						crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),201, "数据采集中,"+qryMonth+"短信记录已采集完成");
					}
				}else{
					TelecomJiangxiHtml htmlStore = new TelecomJiangxiHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(qryMonth+"短信记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomJiangxiHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getSMSRecord.html"+qryMonth, "短信记录源码页已采集完成");
					crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "数据采集中,"+qryMonth+"短信记录已采集完成");
				}
			}
		} catch (Exception e) {
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"短信记录",qryMonth,taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "连接超时", 1);		
			mobileDataErrRecRepository.save(m);
			tracer.addTag("action.crawler.getSMSRecord.e",taskMobile.getTaskid()+"  "+e.toString());
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "数据采集中,短信记录已采集完成");
		}
		return new AsyncResult<String>("200");
	}
}
	
