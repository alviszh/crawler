package app.service;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingBalance;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingCallThremResult;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPayThrem;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPhoneBill;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPhoneschemes;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPointValue;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingBalanceRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingPayThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingPhoneBillRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingPhoneschemesRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingPointValueRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingUserInfoRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingSMSThremResultRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.TelecomLiaoNIngParser;
import app.service.common.GetCommonunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.liaoning")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.liaoning")
public class TelecomUnitLiaoNingService {

	public static final Logger log = LoggerFactory.getLogger(TelecomUnitLiaoNingService.class);
	@Autowired
	private  TracerLog tracer ;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomLiaoNIngParser telecomLiaoNIngParser;
	@Autowired
	private TelecomLiaoNingUserInfoRepository telecomLiaoNingUserInfoRepository;
	@Autowired
	private TelecomLiaoNingBalanceRepository telecomLiaoNingBalanceRepository;
	@Autowired
	private TelecomLiaoNingCallThremResultRepository telecomLiaoNingCallThremResultRepository;
	@Autowired
	private TelecomLiaoNingSMSThremResultRepository telecomLiaoNingSMSThremResultRepository;
	@Autowired
	private TelecomLiaoNingPhoneschemesRepository telecomLiaoNingPhoneschemesRepository;
	@Autowired
	private TelecomLiaoNingPointValueRepository telecomLiaoNingPointValueRepository;
	@Autowired
	private TelecomLiaoNingPhoneBillRepository telecomLiaoNingPhoneBillRepository;
	@Autowired
	private TelecomLiaoNingPayThremResultRepository telecomLiaoNingPayThremResultRepository;
	//基本信息
	@Async
	public Future<String> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception  {
		tracer.addTag("中国电信抓取辽宁用户基本信息", messageLogin.getTask_id());
		//html
		WebClient webClient = getInitMy189homeWebClient(messageLogin, taskMobile);
		//webClient = ready(webClient);
		String url = "http://ln.189.cn/getSessionInfo.action";
		Page page = getHtml(url, webClient);
		String html = page.getWebResponse().getContentAsString();
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("中国电信抓取客户辽宁用户基本信息", html);
		tracer.addTag("中国电信抓取客户辽宁用户基本信息", taskMobile.toString());
		
		if(html == null || html.indexOf("页面找不到")!=-1){
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取客户辽宁用户基本信息", messageLogin.getTask_id());
		
		TelecomLiaoNingUserInfo result = telecomLiaoNIngParser.userinfo_parse(html);
		if(result!=null){
		result.setTaskid(messageLogin.getTask_id());
		save(result);
		crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信抓取客户辽宁用户基本信息", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
		}
		crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");
	}	


	// 获取辽宁用户话费余额
	@Async
	public Future<String> getChargesResult(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		
		Thread.sleep(1000);
		
		tracer.addTag("中国电信抓取获取辽宁用户话费余额", messageLogin.getTask_id());

		WebClient webClient = getInitMy189homeWebClient(messageLogin, taskMobile);
		//webClient = addcookie(webClient, taskMobile);
		//http://ln.189.cn/chargeQuery/chargeQuery_queryRealTimeCharges.parser?productType=8
		String url = "http://ln.189.cn/chargeQuery/chargeQuery_queryRealTimeCharges.action?productType=8";
		webClient.setJavaScriptTimeout(20000);
		webClient.getOptions().setTimeout(20000); // 15->60

		Page page = getHtml(url, webClient);
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("中国电信抓取获取辽宁用户话费余额",  html);

		if(html == null || html.indexOf("页面找不到")!=-1) {
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		TelecomLiaoNingBalance rootresult = telecomLiaoNIngParser.chargesResult_parse(html);
		if (rootresult == null) {
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取获取辽宁用户话费余额", messageLogin.getTask_id());
		// 保存前做去重处理
		if (rootresult != null) {
			rootresult.setUserid(messageLogin.getUser_id());
			rootresult.setTaskid(taskMobile.getTaskid());
			save(rootresult);
		}
		crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");
	}


	//获取辽宁用户通话详单
	@Async
	public Future<String> getCallThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		
		Thread.sleep(1000);
		
		tracer.addTag("中国电信抓取客户辽宁用户通话详单", messageLogin.getTask_id());

		String html = GetCommonunit.getCallThrem(webClientCookies, messageLogin, taskMobile, i);

		tracer.addTag("中国电信抓取客户辽宁用户通话详单", html);

		if(html == null || html.indexOf("页面找不到")!=-1){
			log.info("中国电信抓取客户辽宁用户通话详单html == null");
			tracer.addTag("中国电信抓取辽宁用户 通话详单", "html == null");
			
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomLiaoNingCallThremResult> rootresult= telecomLiaoNIngParser.callThrem_parse(html);

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			if (taskMobile.getCallRecordStatus() != null && taskMobile.getCallRecordStatus() != 200) {
				taskMobile.setCallRecordStatus(201);
			} else {
				taskMobile.setCallRecordStatus(201);
			}
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取客户用户 通话详单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomLiaoNingCallThremResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取客户用户  通话详单", "<xmp>" + resultset.toString() + "</xmp>");
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");
	}

	//获取辽宁用户短信详单
	@Async
	public Future<String> getSMSThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		
		Thread.sleep(1000);
		
		tracer.addTag("中国电信抓取客户辽宁用户短信详单", messageLogin.getTask_id());

		String html = GetCommonunit.getSMSThrem(webClientCookies, messageLogin, taskMobile, i);

		tracer.addTag("中国电信抓取客户辽宁用户短信详单", html);

		if(html == null || html.indexOf("页面找不到")!=-1){
			log.info("中国电信抓取客户辽宁用户短信详单html == null");
			tracer.addTag("中国电信抓取辽宁用户 短信详单", "html == null");
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomLiaoNingSMSThremResult> rootresult = telecomLiaoNIngParser.SMSThrem_parse(html);

		if (rootresult == null) {
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取客户用户短信详单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomLiaoNingSMSThremResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取客户用户  短信详单", "<xmp>" + resultset.toString() + "</xmp>");
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");
	}

	//获取用户往月话费账单
	@Async
	public Future<String> getphoneBill(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		
		Thread.sleep(1000);
		
		tracer.addTag("中国电信抓取客户辽宁往月话费账单", messageLogin.getTask_id());

		// 获取用户往月话费账单
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = getInitMy189homeWebClient(messageLogin, taskMobile);
		//更改参数直接获取到当前月的账单信息
		//String s = LoginAndGetCommon.getphoneBill(webClient, messageLogin, taskMobile, i);//+month
		//String url ="http://ln.189.cn/chargeQuery/chargeQuery_queryCustBill.action?billingCycleId="+month+"&accNbr="+messageLogin.getName();;
//		Page page = getHtml(url, webClient);
//		String html = page.getWebResponse().getContentAsString();
		
		HashMap map = GetCommonunit.getphoneBillThrem(webClient, messageLogin, taskMobile, i);
		String html = (String) map.get("html");
		String month = (String) map.get("month");
		tracer.addTag("中国电信抓取客户辽宁用户往月话费账单", "<xmp>" + html + "</xmp>");
		if(html == null || html.indexOf("页面找不到")!=-1){
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomLiaoNingPhoneBill> rootresult = telecomLiaoNIngParser.phoneBill_parse(html,month);
		if (rootresult == null) {
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取客户用户 账单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomLiaoNingPhoneBill resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取客户用户  账单", "<xmp>" + resultset.toString() + "</xmp>");
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),200,StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");

	}


	public WebClient getInitMy189homeWebClient(MessageLogin messageLogin,TaskMobile taskMobile)throws Exception{
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("parser.crawler.getinfo", taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		//根据如下链接获取爬取需要的cookie(首先根据登录的cookie获取让中间链接成功获取)
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01630716";
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.POST);
		HtmlPage page = webClient.getPage(webRequest);
		int statusCode = page.getWebResponse().getStatusCode();
		WebClient webClientMy189home = null ;
		if(200==statusCode){
			webClientMy189home = page.getWebClient();
		}
		return webClientMy189home;
	}

	//	private WebClient getWebClient(Set<Cookie> basecookies) {
	//
	//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	//		for (Cookie cookie : basecookies) {
	//			webClient.getCookieManager().addCookie(cookie);
	//		}
	//		return webClient;
	//	}


	public static WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}
	public static Page getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			// webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	//套餐
	@Async
	public Future<String> getphoneschemes(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		
		Thread.sleep(1000);
	
		tracer.addTag("中国电信抓取获取辽宁用户套餐", messageLogin.getTask_id());

		WebClient webClient = getInitMy189homeWebClient(messageLogin, taskMobile);
		//webClient = addcookie(webClient, taskMobile);
		//http://ln.189.cn/group/business/pre_queryMyBusinessList.parser
		String url = "http://ln.189.cn/group/business/pre_queryMyBusinessList.action?accNbr="+messageLogin.getName();
		webClient.setJavaScriptTimeout(20000);
		webClient.getOptions().setTimeout(20000); // 15->60

		Page page = getHtml(url, webClient);
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("中国电信抓取获取用户套餐", html );

		if(html == null|| html.indexOf("页面找不到")!=-1) {
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomLiaoNingPhoneschemes> rootresult = telecomLiaoNIngParser.phoneschemesResult_parse(html);

		if (rootresult == null) {
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取客户用户用户套餐", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomLiaoNingPhoneschemes resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取客户用户用户套餐", "<xmp>" + resultset.toString() + "</xmp>");
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");
	}


	//积分
	@Async
	public Future<String> getpointValue(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {

		
		Thread.sleep(1000);
		
		tracer.addTag("中国电信抓取获取辽宁用户积分", messageLogin.getTask_id());

		WebClient webClient = getInitMy189homeWebClient(messageLogin, taskMobile);
		//webClient = addcookie(webClient, taskMobile);

		String url = "http://ln.189.cn/group/integral/infoQuery_starIntegralInfoQuery.action";
		webClient.setJavaScriptTimeout(20000);
		webClient.getOptions().setTimeout(20000); // 15->60

		Page page = getHtml(url, webClient);
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("中国电信抓取获取用户积分", html);

		if(html == null|| html.indexOf("页面找不到")!=-1) {
			crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_FAMILY_MSG_SUCCESS.getDescription());
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		TelecomLiaoNingPointValue rootresult = telecomLiaoNIngParser.pointValue_parse(html);

		if (rootresult == null) {
			crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_FAMILY_MSG_SUCCESS.getDescription());
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取客户用户积分", messageLogin.getTask_id());
		tracer.addTag("中国电信抓取客户用户积分taskMobile=", taskMobileRepository.findByTaskid(taskMobile.getTaskid()).toString());
		rootresult.setTaskid(messageLogin.getTask_id());
		save(rootresult);
		crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_FAMILY_MSG_SUCCESS.getDescription());
		crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");

	}
	
	//缴费信息
	@Async
	public Future<String> getpayResult(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		
		Thread.sleep(1000);
		
		tracer.addTag("中国电信抓取客户辽宁用户缴费信息", messageLogin.getTask_id());

		String html = GetCommonunit.getpayThrem(webClientCookies, messageLogin, taskMobile, i);

		tracer.addTag("中国电信抓取客户辽宁用户缴费信息", html);

		if(html == null|| html.indexOf("页面找不到")!=-1){
			log.info("中国电信抓取客户辽宁用户缴费信息html == null");
			tracer.addTag("中国电信抓取辽宁用户缴费信息", "html == null");
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomLiaoNingPayThrem> rootresult = telecomLiaoNIngParser.payThrem_parse(html);

		if (rootresult == null) {
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取客户用户 缴费信息", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomLiaoNingPayThrem resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取客户用户  缴费信息", "<xmp>" + resultset.toString() + "</xmp>");
				save(resultset);
			}
		}
		crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
		return new AsyncResult<String>("200");
	}
	private void save(TelecomLiaoNingPhoneschemes result) {
		telecomLiaoNingPhoneschemesRepository.save(result);
	}
	private void save(TelecomLiaoNingPointValue result) {
		telecomLiaoNingPointValueRepository.save(result);
	}
	private void save(TelecomLiaoNingCallThremResult result) {
		telecomLiaoNingCallThremResultRepository.save(result);
	}
	private void save(TelecomLiaoNingPayThrem result) {
		telecomLiaoNingPayThremResultRepository.save(result);
	}
	private void save(TelecomLiaoNingSMSThremResult result) {
		telecomLiaoNingSMSThremResultRepository.save(result);
	}
	private void save(TelecomLiaoNingPhoneBill result) {
		telecomLiaoNingPhoneBillRepository.save(result);
	}
	private void save(TelecomLiaoNingUserInfo result) {
		telecomLiaoNingUserInfoRepository.save(result);
	}
	private void save(TelecomLiaoNingBalance result) {
		telecomLiaoNingBalanceRepository.save(result);
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


	


}
