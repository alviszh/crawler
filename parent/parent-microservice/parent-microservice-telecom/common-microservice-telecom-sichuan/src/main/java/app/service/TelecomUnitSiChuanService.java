package app.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

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
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanBusinessMessage;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanCallThremResult;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanConsumptionPoints;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanPayMent;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanPhoneBill;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanBusinessMessageRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanCallThremRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanConsumptionPointsRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanPayMentRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanPhoneBillRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanSMSThremRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.TelecomSiChuanParser;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.sichuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.sichuan")
public class TelecomUnitSiChuanService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomSiChuanParser telecomSiChuanParser;
	@Autowired
	private TelecomSiChuanUserInfoRepository telecomSiChuanUserInfoRepository;
	@Autowired
	private TelecomSiChuanConsumptionPointsRepository telecomSiChuanConsumptionPointsRepository;
	@Autowired
	private TelecomSiChuanBusinessMessageRepository telecomSiChuanBusinessMessageRepository;
	@Autowired
	private TelecomSiChuanPayMentRepository telecomSiChuanPayMentRepository;
	@Autowired
	private TelecomSiChuanPhoneBillRepository telecomSiChuanPhoneBillRepository;
	@Autowired
	private TelecomSiChuanCallThremRepository telecomSiChuanCallThremRepository;
	@Autowired
	private TelecomSiChuanSMSThremRepository telecomSiChuanSMSThremRepository;
	//个人信息
	@Async
	public Future<String> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		tracer.addTag("中国电信抓取四川用户个人信息", messageLogin.getTask_id());
		WebClient webClient = getInitMy189homeWebClient(messageLogin, taskMobile);
		String url = "http://sc.189.cn/service/v6/wdzl?fastcode=20000547&cityCode=sc";
		String url2 = "http://sc.189.cn/service/staruser/staruserInfo.jsp";
		String url3 = "http://www.189.cn/dqmh/order/getHuaFei.do";
		String url4 = "http://sc.189.cn/service/accounthome/accounthomeAjax.jsp?screen=accountexpense";
		Page page = getHtml(url, webClient);
		Page page2 = getHtml(url2, webClient);
		Page page3 = getHtml(url3, webClient);
		Page page4 = getHtml(url4, webClient);
		String html = page.getWebResponse().getContentAsString();
		String html2 = page2.getWebResponse().getContentAsString();
		String html3 = page3.getWebResponse().getContentAsString();
		String html4 = page4.getWebResponse().getContentAsString();
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("中国电信抓取四川用户个人信息", "<xmp>"+html+"</xmp>");
		tracer.addTag("中国电信抓取四川用户个人信息", "<xmp>"+html2+"</xmp>");
		tracer.addTag("中国电信抓取四川用户个人信息", "<xmp>"+html3+"</xmp>");
		tracer.addTag("中国电信抓取四川用户个人信息", "<xmp>"+html4+"</xmp>");
		tracer.addTag("中国电信抓取四川用户个人信息", taskMobile.toString());

		if(html == null){
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode(), "数据采集中，个人信息已采集完成");
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取四川用户个人信息", messageLogin.getTask_id());
		TelecomSiChuanUserInfo telecomSiChuanUserInfo = telecomSiChuanParser.userinfo_parse(html,html2,html3,html4);
		telecomSiChuanUserInfo.setTaskid(messageLogin.getTask_id());
		tracer.addTag("中国电信抓取四川用户信息", telecomSiChuanUserInfo.toString());
		save(telecomSiChuanUserInfo);
		crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
		crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_FAMILY_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信四川抓取四川用户个人信息", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}


	//每月新增积分
	@Async
	public Future<String> getConsumptionPoints(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {


		tracer.addTag("中国电信抓取四川用户每月新增积分", messageLogin.getTask_id());

		String html = GetCommonAndData.getConsumptionPoints(webClient, messageLogin, taskMobile, i);
		tracer.addTag("中国电信抓取四川用户每月新增积分", html);
		System.out.println(html);
		if(html == null){
			tracer.addTag("中国电信抓取四川用户新增积分", "html == null");
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomSiChuanConsumptionPoints> result = telecomSiChuanParser.ConsumptionPoints_parse(html);
		if(result == null){
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取四川用户每月新增积分", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomSiChuanConsumptionPoints resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取四川用户每月新增积分", resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信四川抓取四川用户每月新增积分", messageLogin.getTask_id());
		return new AsyncResult<String>("200");

	}

	//我的业务
	@Async
	public Future<String> getBusinessMessage(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		tracer.addTag("中国电信抓取四川客户业务信息", messageLogin.getTask_id());
		WebClient webClient = getInitMy189homeWebClient(messageLogin, taskMobile);
		String url = "http://sc.189.cn/service/manage/myNewOptionalPackage.jsp";
		String url2 = "http://sc.189.cn/service/manage/mynewopenbusiness.jsp";
		String url3 = "http://sc.189.cn/service/manage/myNewBundle.jsp";
		Page page = getHtml(url, webClient);
		Page page2 = getHtml(url2, webClient);
		Page page3 = getHtml(url3, webClient);
		String html = page.getWebResponse().getContentAsString();
		String html2 = page2.getWebResponse().getContentAsString();
		String html3 = page3.getWebResponse().getContentAsString();
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("中国电信抓取四川客户业务信息", "<xmp>"+html+"</xmp>");
		tracer.addTag("中国电信抓取四川客户业务信息", taskMobile.toString());

		if(html == null){
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}

		tracer.addTag("中国电信抓取四川用户业务信息", messageLogin.getTask_id());
		List<TelecomSiChuanBusinessMessage> result = telecomSiChuanParser.BusinessMessage_parse(html,html2,html3);


		if(result == null){
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取四川用户业务信息", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomSiChuanBusinessMessage resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取四川用户业务信息", "<xmp>"+ resultset.toString() + "</xmp>");
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信四川抓取四川用户业务信息", messageLogin.getTask_id());
		return new AsyncResult<String>("200");

	}
	//通话详单
	@Async
	public Future<String> getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		tracer.addTag("中国电信抓取四川用户通话详单", messageLogin.getTask_id());
		String html = GetCommonAndData.getCallThrem(webClient, messageLogin, taskMobile, i);
		tracer.addTag("中国电信抓取四川用户每月通话详单", html );
		if(html == null){
			tracer.addTag("中国电信抓取四川用户每月通话详单", "html == null");
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 404, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}else if(html.indexOf("没有查询到相应记录！")!=-1){
			tracer.addTag("中国电信抓取四川用户每月通话详单", "html=="+html);
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 404, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomSiChuanCallThremResult> result = telecomSiChuanParser.getCallThrem_parse(html);
		if(result == null){
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取四川用户通话详单", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomSiChuanCallThremResult resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取四川用户通话详单",  resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信四川抓取四川用户通话详单", messageLogin.getTask_id());
		return new AsyncResult<String>("200");

	}


	//短信
	@Async
	public Future<String> getBillDetail(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {


		tracer.addTag("中国电信抓取四川用户短信详单", messageLogin.getTask_id());


		String html = GetCommonAndData.getBillDetail(webClient, messageLogin, taskMobile, i);


		tracer.addTag("中国电信抓取四川用户每月短信详单", html );

		if(html == null){
			tracer.addTag("中国电信抓取四川用户每月短信详单", "html == null");
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}else if(html.indexOf("没有查询到相应记录！")!=-1){
			tracer.addTag("中国电信抓取四川用户每月短信详单", "html=="+html);
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomSiChuanSMSThremResult> result = telecomSiChuanParser.getBillDetail_parse(html);
		if(result == null){
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取四川用户每月短信", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomSiChuanSMSThremResult resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取四川用户每月短信", resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信四川抓取四川用户每月短信", messageLogin.getTask_id());
		return new AsyncResult<String>("200");

	}

	//缴费信息
	@Async
	public Future<String> getPayMent(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {



		tracer.addTag("中国电信抓取四川用户缴费信息", messageLogin.getTask_id());
		String html = GetCommonAndData.getPayMent(webClient, messageLogin, taskMobile, i);
		tracer.addTag("中国电信抓取四川用户缴费信息", html);
		if(html == null){
			tracer.addTag("中国电信抓取四川用户缴费信息", "html == null");
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomSiChuanPayMent> result = telecomSiChuanParser.PayMent_parse(html);
		if(result == null){
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取四川用户每月缴费信息", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomSiChuanPayMent resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取四川用户每月缴费信息",  resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信四川抓取四川用户缴费", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}


	//月账单
	@Async
	public Future<String> getPhoneBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {

		tracer.addTag("中国电信抓取四川用户月账单信息", messageLogin.getTask_id());
		Map<String,String> map = GetCommonAndData.getPhoneBill(webClient, messageLogin, taskMobile, i);
		String html = map.get("html");
		String month = map.get("month");
		tracer.addTag("中国电信抓取四川用户月账单信息", html );
		if(html == null){
			tracer.addTag("中国电信抓取四川用户月账单信息", "html == null");
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		List<TelecomSiChuanPhoneBill> result = telecomSiChuanParser.PhoneBill_parse(html,month);

		if(result == null){
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取四川用户月账单", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomSiChuanPhoneBill resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取四川用户月账单",  resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200, StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getDescription());
		tracer.addTag("中国电信四川抓取四川月账单", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}

	private void save(TelecomSiChuanBusinessMessage result) {
		telecomSiChuanBusinessMessageRepository.save(result);
	}
	private void save(TelecomSiChuanSMSThremResult result) {
		telecomSiChuanSMSThremRepository.save(result);
	}
	private void save(TelecomSiChuanPhoneBill result) {
		telecomSiChuanPhoneBillRepository.save(result);
	}
	private void save(TelecomSiChuanCallThremResult result) {
		telecomSiChuanCallThremRepository.save(result);
	}
	private void save(TelecomSiChuanUserInfo result) {
		telecomSiChuanUserInfoRepository.save(result);
	}
	private void save(TelecomSiChuanConsumptionPoints result) {
		telecomSiChuanConsumptionPointsRepository.save(result);
	}
	private void save(TelecomSiChuanPayMent result) {
		telecomSiChuanPayMentRepository.save(result);
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
	public WebClient getInitMy189homeWebClient(MessageLogin messageLogin,TaskMobile taskMobile){
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		tracer.addTag("parser.crawler.getinfo", taskMobile.getTaskid());
		try {


			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			webClient = addcookie(webClient, taskMobile);

			String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000511";

			WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.POST);

			HtmlPage page = webClient.getPage(webRequest);

			int code = page.getWebResponse().getStatusCode();

			WebClient webClientMy189home = null ;

			if(200==code){
				webClientMy189home = page.getWebClient();
			}
			return webClientMy189home;
		} catch (Exception e) {
			// TODO: handle exception

			return null;
		}
	}

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



}
