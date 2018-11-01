package app.service;

import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongBusinessmessage;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongCallThremResult;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongPayMent;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongSMSThrem;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongStatusCode;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.guangdong.TelecomGuangDongBusinessmessageRepository;
import com.microservice.dao.repository.crawler.telecom.guangdong.TelecomGuangDongCallThremRepository;
import com.microservice.dao.repository.crawler.telecom.guangdong.TelecomGuangDongPayMentRepository;
import com.microservice.dao.repository.crawler.telecom.guangdong.TelecomGuangDongSMSThremRepository;
import com.microservice.dao.repository.crawler.telecom.guangdong.TelecomGuangDongStatusCodeRepository;
import com.microservice.dao.repository.crawler.telecom.guangdong.TelecomGuangDongUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;
import app.parser.TelecomGuangDongParser;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guangdong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guangdong")
public class TelecomGuangDongUnitService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomGuangDongParser telecomGuangDongParser;
	@Autowired
	private TelecomGuangDongUserInfoRepository telecomGuangDongUserInfoRepository;
	@Autowired
	private TelecomGuangDongPayMentRepository telecomGuangDongPayMentRepository;
	@Autowired
	private TelecomGuangDongBusinessmessageRepository telecomGuangDongBusinessmessageRepository;
	@Autowired
	private TelecomGuangDongStatusCodeRepository telecomGuangDongStatusCodeRepository;
	@Autowired
	private TelecomGuangDongCallThremRepository telecomGuangDongCallThremRepository;
	@Autowired
	private TelecomGuangDongSMSThremRepository telecomGuangDongSMSThremRepository;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	@Autowired
	private GetCommonAndData getCommonAndData;
	//个人信息
	@Async
	public Future<String> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		tracer.addTag("中国电信抓取广东用户个人信息", messageLogin.getTask_id());
		//基本信息
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		String url1 = "http://gd.189.cn/login_mid.html?ssid=gdds-syleft-wdxx-grxx-wdzl";
		Page page1 = getHtml(url1, webClient);
		String html1 = page1.getWebResponse().getContentAsString();

		//积分
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Host", "gd.189.cn");
		webClient.addRequestHeader("Origin", "http://gd.189.cn");
		webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/jifen.html?in_cmpid=khzy-wdsy-wdjj-jfcx");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		//webClient.getCookieManager().addCookie(new Cookie("gd.189.cn", "loginStatus", "logined"));
		String url2 = "http://gd.189.cn/J/J10032.j";
		List<NameValuePair> paramsList = new ArrayList<>();

		paramsList.add(new NameValuePair("a.c", "0"));
		paramsList.add(new NameValuePair("a.u", "user"));
		paramsList.add(new NameValuePair("a.p", "pass"));
		paramsList.add(new NameValuePair("a.s", "ECSS"));
		paramsList.add(new NameValuePair("c.n", "积分查询"));
		paramsList.add(new NameValuePair("c.t", "01"));
		paramsList.add(new NameValuePair("c.i", "01-001"));
		Page page2 = gethtmlPost(webClient, paramsList, url2);
		String html = page2.getWebResponse().getContentAsString();

		Thread.sleep(1000);

		//余额
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Host", "gd.189.cn");
		webClient.addRequestHeader("Origin", "http://gd.189.cn");
		webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xf_ye.html?in_cmpid=khzy-zcdh-fycx-wdxf-yecx");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

		LocalDate today = LocalDate.now();
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1);
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + monthint;
		String url3 = "http://gd.189.cn/J/J10061.j";
		List<NameValuePair> paramsList1 = new ArrayList<>();

		paramsList1.add(new NameValuePair("a.c", "0"));
		paramsList1.add(new NameValuePair("a.u", "user"));
		paramsList1.add(new NameValuePair("a.p", "pass"));
		paramsList1.add(new NameValuePair("a.s", "ECSS"));
		paramsList1.add(new NameValuePair("c.n", "余额查询"));
		paramsList1.add(new NameValuePair("c.t", "02"));
		paramsList1.add(new NameValuePair("c.i", "02-002"));
		paramsList1.add(new NameValuePair("d.d01", month));

		Thread.sleep(1000);

		Page page3 = gethtmlPost(webClient, paramsList1, url3);
		String html3 = page3.getWebResponse().getContentAsString();


		//星级
		String url4 = "http://gd.189.cn/J/J10133.j";
		List<NameValuePair> paramsList2 = new ArrayList<>();
		paramsList2.add(new NameValuePair("a.c", "0"));
		paramsList2.add(new NameValuePair("a.u", "user"));
		paramsList2.add(new NameValuePair("a.p", "pass"));
		paramsList2.add(new NameValuePair("a.s", "ECSS"));

		Thread.sleep(1000);

		Page page4 = gethtmlPost(webClient, paramsList2, url4);
		String html4 = page4.getWebResponse().getContentAsString();
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("中国电信抓取广东用户个人信息", "<xmp>"+html1+"</xmp>");
		tracer.addTag("中国电信抓取广东用户个人信息", taskMobile.toString());

		if(html1 == null){
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，用户信息已采集完成");
			crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，亲情号信息已采集完成");
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，账单信息已采集完成");;
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取广东用户个人信息", messageLogin.getTask_id());
		TelecomGuangDongUserInfo telecomGuangDongUserInfo=telecomGuangDongParser.userinfo_parse(html1,html,html3,html4);
		if(telecomGuangDongUserInfo!=null){
			telecomGuangDongUserInfo.setTaskid(messageLogin.getTask_id());
			save(telecomGuangDongUserInfo);
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，用户信息已采集完成");
		crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，亲情号信息已采集完成");
		crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，账单信息已采集完成");;
		tracer.addTag("中国电信广东抓取广东用户个人信息", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}

	//套餐
	@Async
	public Future<String> getBusinessmessage(MessageLogin messageLogin, TaskMobile taskMobile) {
		tracer.addTag("中国电信抓取广东用户套餐", messageLogin.getTask_id());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);

		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Host", "gd.189.cn");
		webClient.addRequestHeader("Origin", "http://gd.189.cn");
		webClient.addRequestHeader("Referer", "http://gd.189.cn/consumeInfo/myHisConsume/myHisConsume.html?in_cmpid=khzy-zcdh-fycx-wdxf-tcsycx");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		String url2 = "http://gd.189.cn/J/J10055.j";
		List<NameValuePair> paramsList = new ArrayList<>();

		paramsList.add(new NameValuePair("a.c", "0"));
		paramsList.add(new NameValuePair("a.u", "user"));
		paramsList.add(new NameValuePair("a.p", "pass"));
		paramsList.add(new NameValuePair("a.s", "ECSS"));
		paramsList.add(new NameValuePair("d.d01","1"));
		paramsList.add(new NameValuePair("d.d02", ""));

		Page page2 = gethtmlPost(webClient, paramsList, url2);
		String html = page2.getWebResponse().getContentAsString();
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Host", "gd.189.cn");
		webClient.addRequestHeader("Origin", "http://gd.189.cn");
		webClient.addRequestHeader("Referer", "http://gd.189.cn/consumeInfo/myHisConsume/myHisConsume.html?in_cmpid=khzy-zcdh-fycx-wdxf-tcsycx");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		String url3 = "http://gd.189.cn/J/J10041.j";
		List<NameValuePair> paramsList2 = new ArrayList<>();
		paramsList2.add(new NameValuePair("a.c", "0"));
		paramsList2.add(new NameValuePair("a.u", "user"));
		paramsList2.add(new NameValuePair("a.p", "pass"));
		paramsList2.add(new NameValuePair("a.s", "ECSS"));
		paramsList2.add(new NameValuePair("c.n","套餐使用查询"));
		paramsList2.add(new NameValuePair("c.t", "02"));
		paramsList2.add(new NameValuePair("c.i", "02-006"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Page page = gethtmlPost(webClient, paramsList2, url3);
		String html1 = page.getWebResponse().getContentAsString();
		tracer.addTag("中国电信抓取广东用户套餐", html1);
		if(html1 == null&&html == null){
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode(), "数据采集中，套餐信息已采集完成");
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取广东用户套餐", messageLogin.getTask_id());
		List<TelecomGuangDongBusinessmessage> result = telecomGuangDongParser.Businessmessage_parse(html,html1);
		if(result == null){
			tracer.addTag("中国电信抓取用户用户套餐", "html == null");
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode(), "数据采集中，套餐信息已采集完成");
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取用户套餐", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomGuangDongBusinessmessage resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取用户用户套餐", resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，套餐信息已采集完成");
		tracer.addTag("中国电信 抓取用户用户套餐", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}

	private void save(TelecomGuangDongCallThremResult result) {
		telecomGuangDongCallThremRepository.save(result);
	}
	private void save(TelecomGuangDongSMSThrem result) {
		telecomGuangDongSMSThremRepository.save(result);
	}
	private void save(TelecomGuangDongUserInfo result) {
		telecomGuangDongUserInfoRepository.save(result);
	}
	private void save(TelecomGuangDongBusinessmessage result) {
		telecomGuangDongBusinessmessageRepository.save(result);
	}
	private void save(TelecomGuangDongStatusCode result) {
		telecomGuangDongStatusCodeRepository.save(result);
	}
	private void save(TelecomGuangDongPayMent result) {
		telecomGuangDongPayMentRepository.save(result);
	}
	public static Page getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			// webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	//缴费信息
	@Async
	public Future<String> getPayMent(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {

		tracer.addTag("中国电信抓取广东用户缴费信息", messageLogin.getTask_id());
		String html = getCommonAndData.getPayMent(webClient, messageLogin, taskMobile, i);
		tracer.addTag("中国电信抓取广东用户缴费信息", html);
		WebParamTelecom  webParamTelecom = new WebParamTelecom();
		if(html!=null){
			webParamTelecom.setHtml(html);
			webParamTelecom.setWebClient(webClient);
			if(webParamTelecom.getHtml().indexOf("只能查询该号码开户日期之后的清单") != -1){
				crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，"+StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getMessage());
				tracer.addTag("中国电信抓取用户缴费信息----只能查询该号码开户日期之后的清单", messageLogin.getTask_id());
				return new AsyncResult<String>("200");
			}else if(html.indexOf("查询云清单为空") != -1){
				crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，"+StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getMessage());
				tracer.addTag("中国电信抓取用户缴费信息----只能查询该号码开户日期之后的清单", messageLogin.getTask_id());
				return new AsyncResult<String>("200");
			}
		} 
		if(html.equals(null)){
			tracer.addTag("中国电信抓取用户缴费信息", "html == null");
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，缴费信息已采集完成");
			return new AsyncResult<String>("200");
		}
		List<TelecomGuangDongPayMent> result = telecomGuangDongParser.getPayMent_parse(html);
		if(result == null){
			tracer.addTag("中国电信抓取用户缴费信息", "html == null");
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，缴费信息已采集完成");
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取用户缴费信息", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomGuangDongPayMent resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取用户缴费信息", resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，缴费信息已采集完成");
		tracer.addTag("中国电信抓取用户缴费信息", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}
	//通话详单
	@Async
	public Future<String> getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {

		tracer.addTag("中国电信抓取广东用户通话详单", messageLogin.getTask_id());
		String html = getCommonAndData.getCallThrem(webClient, messageLogin, taskMobile, i);
		tracer.addTag("中国电信抓取广东用户通话详单2", html);
		if(html.indexOf("查询时间验证失败，只能查询该号码开户日期之后的数据") != -1){
			int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"通话记录",nowmonth+"月",taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "没有数据：只能查询该号码开户日期之后的数据", 1);		
			mobileDataErrRecRepository.save(m);
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 201, "数据采集中，通话信息已采集完成");
			tracer.addTag("中国电信抓取用户通话详单", messageLogin.getTask_id());
			return new AsyncResult<String>("200");
		}else if(html == null||html.indexOf("没有数据")!=-1){
			tracer.addTag("中国电信抓取广东用户通话详单3", html);
			int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"通话记录",nowmonth+"月",taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "没有数据", 1);	
			mobileDataErrRecRepository.save(m);
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 201, "数据采集中，通话信息已采集完成");
			tracer.addTag("中国电信抓取广东用户通话详单4", taskMobile.toString());
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取广东用户通话详单5", messageLogin.getTask_id());
		List<TelecomGuangDongCallThremResult> result = telecomGuangDongParser.callThrem_parse(html);
		if(result == null){
			tracer.addTag("中国电信抓取用户通话详单6", "result == null");
			int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"通话记录",nowmonth+"月",taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "页面超时", 1);		
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，通话信息已采集完成");
			return new AsyncResult<String>("200");
		}
		tracer.addTag("中国电信抓取广东用户通话详单7", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomGuangDongCallThremResult resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取广东用户通话详单", resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，通话信息已采集完成");
		tracer.addTag("中国电信抓取用户通话详单8", result.toString());
		return new AsyncResult<String>("200");
	}


	//短信
	@Async
	public Future<String> getSMSThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		tracer.addTag("中国电信抓取广东用户短信", messageLogin.getTask_id());
		String html = getCommonAndData.getSMSThrem(webClient, messageLogin, taskMobile, i);
		tracer.addTag("中国电信抓取广东用户短信", html);
		if(html == null){
			int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"短信记录",nowmonth+"月",taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "页面超时", 1);		
			mobileDataErrRecRepository.save(m);

			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，短信信息已采集完成");
			return new AsyncResult<String>("200");
		}
		else if(html.indexOf("查询时间验证失败，只能查询该号码开户日期之后的数据") != -1){
			int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"短信记录",nowmonth+"月",taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "没有数据:只能查询该号码开户日期之后的数据", 1);		
			mobileDataErrRecRepository.save(m);
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "数据采集中，短信信息已采集完成");
			tracer.addTag("中国电信抓取用户短信", messageLogin.getTask_id());
			return new AsyncResult<String>("200");
		}else if(html.indexOf("用户未进行实名登记!") != -1){
			int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"短信记录",nowmonth+"月",taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "没有数据", 1);		
			mobileDataErrRecRepository.save(m);

			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "数据采集中，短信信息已采集完成");
			tracer.addTag("中国电信抓取用户短信", messageLogin.getTask_id());
			return new AsyncResult<String>("200");
		}
		else if(html.indexOf("没有数据") != -1){
			int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"短信记录",nowmonth+"月",taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "没有数据", 1);		
			mobileDataErrRecRepository.save(m);

			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "数据采集中，短信信息已采集完成");
			tracer.addTag("中国电信抓取用户短信", messageLogin.getTask_id());
			return new AsyncResult<String>("200");

		}
		tracer.addTag("中国电信抓取广东用户短信", messageLogin.getTask_id());
		List<TelecomGuangDongSMSThrem> result = telecomGuangDongParser.sMSThrem_parse(html);
		if(result==null){
			tracer.addTag("中国电信抓取用户短信", "html == null");
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "数据采集中，短信信息已采集完成");
			return new AsyncResult<String>("200");
		}

		tracer.addTag("中国电信抓取广东用户短信", messageLogin.getTask_id());
		//保存前做去重
		if(result != null){
			for (TelecomGuangDongSMSThrem resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				tracer.addTag("中国电信抓取广东用户短信", resultset.toString());
				save(resultset);
			}
		}
		crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，短信信息已采集完成");
		tracer.addTag("中国电信抓取用户短信", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}



	//积分
	@Async
	public Future<String> getStatusCode(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		//		LocalDate today = LocalDate.now();
		//		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1);
		//		String monthint = stardate.getMonthValue() + "";
		//		if(monthint.length()<2){
		//			monthint = "0" + monthint;
		//		}
		//		String month1 = stardate.getYear() + monthint;
		//		tracer.addTag("中国电信抓取广东用户积分信息", messageLogin.getTask_id());
		//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//		webClient = addcookie(webClient, taskMobile);
		//		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		//		webClient.addRequestHeader("Host", "gd.189.cn");
		//		webClient.addRequestHeader("Origin", "http://gd.189.cn");
		//		webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/jifen.html?in_cmpid=khzy-wdsy-wdjj-jfcx");
		//		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		//		String url4 = "http://gd.189.cn/J/J10033.j";
		//		List<NameValuePair> paramsList2 = new ArrayList<>();
		//		paramsList2.add(new NameValuePair("a.c", "0"));
		//		paramsList2.add(new NameValuePair("a.u", "user"));
		//		paramsList2.add(new NameValuePair("a.p", "pass"));
		//		paramsList2.add(new NameValuePair("a.s", "ECSS"));
		//		paramsList2.add(new NameValuePair("d.d01","201801"));
		//		paramsList2.add(new NameValuePair("d.d02",month1));
		//
		//		Page page4 = gethtmlPost(webClient, paramsList2, url4);
		//		String html = page4.getWebResponse().getContentAsString();
		//		//		WebParamTelecom  webParamTelecom = new WebParamTelecom();
		//		//		webParamTelecom.setHtml(html);
		//		//		webParamTelecom.setWebClient(webClient);
		//		//		if(webParamTelecom.getHtml().indexOf("只能查询该号码开户日期之后的清单") != -1){
		//		//			return null;
		//		//		}
		//		tracer.addTag("中国电信抓取广东用户积分信息", "<xmp>"+html+"</xmp>");
		//		tracer.addTag("中国电信抓取广东用户积分信息", taskMobile.toString());
		//		if(html == null){
		//			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode(), "数据采集中，积分信息已采集完成");
		//			return new AsyncResult<String>("200");
		//		}
		//		tracer.addTag("中国电信抓取广东用户积分", messageLogin.getTask_id());
		//		List<TelecomGuangDongStatusCode> result = telecomGuangDongParser.statusCode_parse(html);
		//		if(result == null){
		//			tracer.addTag("中国电信抓取用户积分信息", "html == null");
		//			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，积分信息已采集完成");
		//			return new AsyncResult<String>("200");
		//		}
		//		tracer.addTag("中国电信抓取 用户积分信息", messageLogin.getTask_id());
		//		//保存前做去重
		//		if(result != null){
		//			for (TelecomGuangDongStatusCode resultset : result) {
		//				resultset.setUserid(messageLogin.getUser_id());
		//				resultset.setTaskid(taskMobile.getTaskid());
		//				tracer.addTag("中国电信抓取 用户积分信息", resultset.toString());
		//				save(resultset);
		//			}
		//		}
		crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，积分信息已采集完成");
		tracer.addTag("中国电信 抓取 用户积分信息", messageLogin.getTask_id());
		return new AsyncResult<String>("200");
	}


	public WebClient getInitMy189homeWebClient(MessageLogin messageLogin,TaskMobile taskMobile){
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracer.addTag("parser.crawler.getinfo", taskMobile.getTaskid());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		webClient = addcookie(webClient, taskMobile);

		try {
			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

			String url = "http://gd.189.cn/J/J10008.j?a.c=0&a.u=user&a.p=pass&a.s=ECSS";


			WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);

			HtmlPage page = webClient.getPage(webRequest);

			int code = page.getWebResponse().getStatusCode();


			WebClient webClientMy189home = null ;
			if(200==code){
				webClientMy189home = page.getWebClient();
			}

			return webClientMy189home;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
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
	private void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			webRequest.setCharset(Charset.forName("gbk"));
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
