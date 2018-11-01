package app.service;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.CookieJson;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiAccount;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiCallRec;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiPackage;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiPayfee;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.hebei.TelecomHebeiAccountRepository;
import com.microservice.dao.repository.crawler.telecom.hebei.TelecomHebeiCallRecRepository;
import com.microservice.dao.repository.crawler.telecom.hebei.TelecomHebeiPackageRepository;
import com.microservice.dao.repository.crawler.telecom.hebei.TelecomHebeiPayfeeRepository;
import com.microservice.dao.repository.crawler.telecom.hebei.TelecomHebeiUserInfoRepository;
import app.commontracerlog.TracerLog;
import app.parser.TelecomHebeiParser;
import app.service.aop.ISmsTwice;
import app.unit.TeleComCommonUnit;

@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hebei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hebei")
public class TelecomHebeiService implements ISmsTwice{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHebeiUserInfoRepository telecomHebeiUserInfoRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomHebeiParser telecomHebeiParser;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomHebeiPackageRepository telecomHebeiPackageRepository;
	@Autowired
	private TelecomHebeiPayfeeRepository telecomHebeiPayfeeRepository;
	@Autowired
	private TelecomHebeiAccountRepository telecomHebeiAccountRepository;
	@Autowired
	private TelecomHebeiCallRecRepository telecomHebeiCallRecRepository;
	
	SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd"); 


	/**
	 * @Des 获取用户信息
	 * @param taskMobile
	 * @param messageLogin
	 * @throws Exception
	 */
	@Async
	public void getUserInfo(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.hebei.getUserInfo.start", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String userUrl ="http://he.189.cn/service/manage/index_iframe.jsp?FLAG=1&fastcode=00420429&cityCode=he";
		Page page = TeleComCommonUnit.getHtml(userUrl, webClient);
		tracer.addTag("crawler.telecom.hebei.getUserInfo.html", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		TelecomHebeiUserInfo telecomHebeiUserInfo = telecomHebeiParser.parserUserinfo(doc,taskMobile.getTaskid());		
		telecomHebeiUserInfoRepository.save(telecomHebeiUserInfo);

		taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "【用户信息】采集中！");	
		taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "无亲情套餐！");
		taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	/**
	 * @Des 获取套餐详情
	 * @param taskMobile
	 * @param messageLogin
	 * @throws Exception
	 */
	@Async
	public void getPackage(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.hebei.getPackage.start", messageLogin.getTask_id());	
		String packageUrl = "http://he.189.cn/service/manage/prod_baseinfo_iframe.jsp";
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = getClient(taskMobile.getCookies());
		Page page = TeleComCommonUnit.getHtml(packageUrl, webClient);
		tracer.addTag("crawler.telecom.hebei.getPackage.html", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		TelecomHebeiPackage telecomHebeiPackage = telecomHebeiParser.parserPackage(doc,messageLogin.getTask_id(),messageLogin.getName());		
		if(null != telecomHebeiPackage){
			telecomHebeiPackageRepository.save(telecomHebeiPackage);		
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 200, "【业务信息】采集完成！");		
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
		}else{
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201, "【业务信息】采集完成！");
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	
		}
	}

	
	/**
	 * @Des 获取缴费信息
	 * @param taskMobile
	 * @param messageLogin
	 */
	@Async
	public void getPayfee(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.hebei.getPayfee.start", messageLogin.getTask_id());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = getClient(taskMobile.getCookies());
		
		//获取当前时间半年前
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
	    c.add(Calendar.MONTH, -6);
	    Date m6 = c.getTime();
	    String mon6 = sdf.format(m6);
		
		String payfeeUrl = "http://he.189.cn/service/pay/userPayfeeHisList_iframe.jsp?START_ASK_DATE="+mon6+"&END_ASK_DATE="+sdf.format(new Date());
		Page page = TeleComCommonUnit.getHtml(payfeeUrl, webClient);
		tracer.addTag("crawler.telecom.hebei.getPayfee.html", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		List<TelecomHebeiPayfee> telecomHebeiPayfees = telecomHebeiParser.parserPayfee(doc,messageLogin.getTask_id());
		
		if(null != telecomHebeiPayfees && telecomHebeiPayfees.size()>0){
			telecomHebeiPayfeeRepository.saveAll(telecomHebeiPayfees);
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 200, "【缴费信息】采集中！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());			
		}else{
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201, "【缴费信息】采集完成！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	
		}
	}

	/**
	 * @Des 获取账单信息
	 * @param taskMobile
	 * @param messageLogin
	 */
	@Async
	public void getAccount(MessageLogin messageLogin, String mon) throws Exception {
		
		tracer.addTag("crawler.telecom.hebei.getAccount.start", messageLogin.getTask_id());
		tracer.addTag("crawler.telecom.hebei.getAccount.month", mon);
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = getClient(taskMobile.getCookies());
		String accountUrl = "http://he.189.cn/service/bill/action/bill_month_list_detail_iframe.jsp?ACC_NBR="+messageLogin.getName()+"&SERVICE_KIND=8&feeDate="+mon+"&retCode=0000";
		Page page = TeleComCommonUnit.getHtml(accountUrl, webClient);
		tracer.addTag("crawler.telecom.hebei.getAccount.html", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
	
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		TelecomHebeiAccount telecomHebeiAccount = telecomHebeiParser.parserAccount(doc,messageLogin.getTask_id(),mon);
		if(null != telecomHebeiAccount){
			telecomHebeiAccountRepository.save(telecomHebeiAccount);
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "【账单信息】第"+mon+"月采集完成！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());		
		}else{
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "【账单信息】第"+mon+"月采集完成！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());		
		}
		
	}
	

	/**
	 * @Des 获取通话记录
	 * @param taskMobile
	 * @param messageLogin
	 * @param mon
	 * @param startYear
	 * @param endYear
	 * @param mon2 
	 */
	@Async
	public void getCallRec(MessageLogin messageLogin) throws Exception{
		
		tracer.addTag("crawler.telecom.hebei.getCallRec.start", messageLogin.getTask_id());
//		tracer.addTag("crawler.telecom.hebei.getCallRec.month", mon);
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = getClient(taskMobile.getCookies());
		
		String callUrl = "http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp";
		tracer.addTag("getcalllrec.url", callUrl);
		Page page = getPage(callUrl, webClient,taskMobile,messageLogin);
		tracer.addTag("crawler.telecom.hebei.getCallRec.html.", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(page.getWebResponse().getContentAsString().contains("电信账号登录")){
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else if(page.getWebResponse().getContentAsString().contains("随机码不正确,请重新输入")){
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "随机码不正确，获取通话记录超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			List<TelecomHebeiCallRec> telecomHebeiCallRecs = telecomHebeiParser.parserCallRec(doc,messageLogin.getTask_id());
			
			if(null != telecomHebeiCallRecs && telecomHebeiCallRecs.size()>0){
				telecomHebeiCallRecRepository.saveAll(telecomHebeiCallRecs);
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "【通讯信息】采集中！");			
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());		
			}else{
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】月采集超时！");			
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
			
		}
	
		
	}
	
	/**
	 * @Des 获取短信信息
	 * @param messageLogin
	 * @param mon
	 * @param startYear
	 * @param endYear
	 */
	@Async
	public void getMsgRec(MessageLogin messageLogin, String startYear,
			String endYear) throws Exception{
		
		tracer.addTag("crawler.telecom.hebei.getMsgRec.start", messageLogin.getTask_id());
//		tracer.addTag("crawler.telecom.hebei.getMsgRec.month", mon);
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
//		String msgUrl = "http://he.189.cn/service/bill/parser/ifr_bill_detailslist_em_new_iframe.jsp?ACC_NBR="+messageLogin.getName()+"&CITY_CODE="+taskMobile.getNexturl()+"&BEGIN_DATE="+startYear+" 00:00:00&END_DATE="+endYear+" 23:59:59&FEE_DATE="+mon+"&SERVICE_KIND=8&retCode=0000&QUERY_FLAG=1&QUERY_TYPE_NAME=移动短信详单&QUERY_TYPE=2&radioQryType=on&QRY_FLAG=1";
//	
//		Page page = TeleComCommonUnit.getHtml(msgUrl, webClient);
//		tracer.addTag("crawler.telecom.hebei.getCallRec.html."+mon, "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
//		if(page.getWebResponse().getContentAsString().contains("您所查询的条件内没有相应的记录")){
//			tracer.addTag("crawler.telecom.hebei.getCallRec.nodata", "尊敬的客户，您所查询的条件内没有相应的记录！");
//			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
//			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "采集中！");
//		}else{
//			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
//			List<TelecomHebeiMsg> telecomHebeiMsgRecs = telecomHebeiParser.parserMsgRec(doc,messageLogin.getTask_id(),mon);
//			
//			if(null != telecomHebeiMsgRecs && telecomHebeiMsgRecs.size()>0){
//				telecomHebeiMsgRepository.saveAll(telecomHebeiMsgRecs);
				taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "【短信信息】采集成功！");			
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());		
//			}
//		}
	
	}
	
	
	/**
	 * @Des 获取当前时间前半年
	 * @return
	 */
	public String getCurrentYearStartTime() {  

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.MONTH, -6);
        Date m6 = c.getTime();
        String mon6 = longSdf.format(m6);  
        
		return mon6;  
	} 
	
	/**
	 * @Des 获取当前时间后半年
	 * @return
	 */
	public String getCurrentYearEndTime() {  
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.MONTH, +6);
        Date m6 = c.getTime();
        String mon6 = longSdf.format(m6);  
        
		return mon6;  
	}

	/**
	 * @Des 更新cookie
	 * @param taskMobile
	 * @param messageLogin
	 * @throws Exception
	 */
	public TaskMobile updateCookies(TaskMobile taskMobile, MessageLogin messageLogin) throws Exception {
		
		String index = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00380407";		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		webClient.getOptions().setJavaScriptEnabled(false);
//		HtmlPage page1 = (HtmlPage) TeleComCommonUnit.getHtml(index, webClient);
//		System.out.println(page1.asXml());
		
		String url = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10006&toStUrl=http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he";
		TeleComCommonUnit.getHtml(url, webClient);		
		webClient.getOptions().setJavaScriptEnabled(true);
		String cityCodeUrl = "http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he";
		Page page = TeleComCommonUnit.getHtml(cityCodeUrl, webClient);
		String cityCode = telecomHebeiParser.parserCityCode(page.getWebResponse().getContentAsString());
		tracer.addTag("updateCookies.cityCode", cityCode);
		String cookies = CommonUnit.transcookieToJson(webClient);
		if(null == cityCode){
			taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_THREE.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_THREE.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.MESSAGE_LOGIN_ERROR_THREE.getDescription());
			taskMobileRepository.save(taskMobile);
			return taskMobile;
		}else{		
			taskMobile.setCookies(cookies);
			taskMobile.setNexturl(cityCode);
			return taskMobile;
		}
		
	}
	

	/**
	 * 发送短信
	 * @param taskMobile
	 * @param messageLogin
	 * @throws Exception 
	 */ 
	@Async
	public TaskMobile sendSmsTwice(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		SimpleDateFormat longSdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss",Locale.UK);
		String reDo = URLEncoder.encode(longSdf.format(new Date()))+"+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
		String smsUrl = "http://he.189.cn/service/transaction/postValidCode.jsp?"
				+ "reDo="+reDo+ "&OPER_TYPE=CR1&RAND_TYPE=006&PRODTYPE=2020966&MOBILE_NAME="+messageLogin.getName()+
				"&MOBILE_NAME_PWD=&NUM="+messageLogin.getName()+"&AREA_CODE="+taskMobile.getNexturl()+"&LOGIN_TYPE=21";
		System.out.println("发送短信的URL："+smsUrl);
		tracer.addTag("sendsms.url", smsUrl);
		try {
			Page page = TeleComCommonUnit.getHtml(smsUrl, webClient);
			tracer.addTag("sendsms.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
			String result = parserSMS(page.getWebResponse().getContentAsString());
			if("0".equals(result)){
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getError_code());
				taskMobileRepository.save(taskMobile);
			}else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getError_code());
				taskMobileRepository.save(taskMobile);
			}
			
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription("因电信网站网络波动 ，短信发送超时，请重新发送。");
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getError_code());
			taskMobileRepository.save(taskMobile);
		}
		return taskMobile;
	}
	
	/**
	 * 解析短信验证码报文
	 * @param html
	 * @return
	 */
	public String parserSMS(String html){
		Document doc = Jsoup.parse(html);
		return doc.select("actionFlag").first().text();
	}
	
	
	public WebClient getClient(String cookieStr){
		if(StringUtils.isBlank(cookieStr)){
			throw new RuntimeException("cookie is null !");
		}else{
			Set<Cookie> cookies = transferJsonToSet(cookieStr);
			WebClient webClient = null;
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.setRefreshHandler(new ThreadedRefreshHandler());
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(50000); // 5s
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setUseInsecureSSL(true); //
			
			for(Cookie cookie:cookies){
				webClient.getCookieManager().addCookie(cookie);
			}
			return webClient;
		}
		
	}
	
	/**
	 * @Des 将json转为Set<Cookie>
	 * @param json
	 * @return
	 */
	public Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie("he.189.cn", cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}
		
		return set;

	}
	
	
	public Page getPage(String url, WebClient webClient,TaskMobile taskMobile,MessageLogin messageLogin) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();  
		calendar.add(Calendar.MONTH, -1);  
		calendar.set(Calendar.DAY_OF_MONTH, 1);		
		String BEGIN_DATE = sdf.format(calendar.getTime());
		
		calendar.add(Calendar.MONTH, 1);  
		calendar.set(Calendar.DAY_OF_MONTH, 0);  
		System.out.println(sdf.format(calendar.getTime()));
		String END_DATE = sdf.format(calendar.getTime());
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		c1.add(Calendar.MONTH, -1);
		Date date1 = c1.getTime();
		String currentmon = sdf2.format(date1);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");

		webRequest.setAdditionalHeader("Host", "he.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://he.189.cn/service/bill/feeQuery_iframe.jsp?SERV_NO=SHQD1&fastcode=00380407&cityCode=he");
		webRequest.setAdditionalHeader("Origin", "http://he.189.cn");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		
		tracer.addTag("ACC_NBR", messageLogin.getName());
		tracer.addTag("CITY_CODE", taskMobile.getNexturl());
		tracer.addTag("BEGIN_DATE", BEGIN_DATE);
		tracer.addTag("END_DATE", END_DATE);
		tracer.addTag("FEE_DATE", currentmon);
		tracer.addTag("ACCT_DATE", currentmon);
		tracer.addTag("ACCT_DATE_1", sdf2.format(new Date()));
		
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("ACC_NBR", messageLogin.getName()));
		webRequest.getRequestParameters().add(new NameValuePair("CITY_CODE", taskMobile.getNexturl()));
		webRequest.getRequestParameters().add(new NameValuePair("BEGIN_DATE", BEGIN_DATE+" 00:00:00"));
		webRequest.getRequestParameters().add(new NameValuePair("END_DATE", END_DATE+" 23:59:59"));
		webRequest.getRequestParameters().add(new NameValuePair("FEE_DATE", currentmon));
		webRequest.getRequestParameters().add(new NameValuePair("SERVICE_KIND", "8"));
		webRequest.getRequestParameters().add(new NameValuePair("retCode", "0000"));
		webRequest.getRequestParameters().add(new NameValuePair("QUERY_FLAG", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("QUERY_TYPE_NAME", "移动语音详单"));
		webRequest.getRequestParameters().add(new NameValuePair("QUERY_TYPE", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("radioQryType", "on"));
		webRequest.getRequestParameters().add(new NameValuePair("QRY_FLAG", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("ACCT_DATE", currentmon));
		webRequest.getRequestParameters().add(new NameValuePair("ACCT_DATE_1", sdf2.format(new Date())));
		webRequest.getRequestParameters().add(new NameValuePair("sjmput", messageLogin.getSms_code()));
		
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

}
