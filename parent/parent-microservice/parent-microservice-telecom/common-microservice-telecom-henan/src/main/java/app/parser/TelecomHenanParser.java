package app.parser;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanCallDetail;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanMonthBill;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanPayment;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanSMSDetail;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanServer;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;

@Component
public class TelecomHenanParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private LoginAndGetCommon loginAndGetCommon;
	@Autowired
	private TracerLog tracer;
	
	//发送短信验证码接口
	public WebParam sendSms(TaskMobile taskMobile){
		tracer.addTag("parser.telecom.parser.sendSms.taskid",taskMobile.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000355";
		String url2 = "http://www.189.cn/login/sso/ecs.do?method=linkTo&platNo=10017&toStUrl=http://ha.189.cn/service/iframe/feeQuery_iframe.jsp?SERV_NO=FSE-2-2&fastcode=20000356&cityCode=ha";
		try {
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.sendSms.page1",htmlPage.asXml());
			WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.GET);
			webRequest1.setAdditionalHeader("Host", "www.189.cn");
			webRequest1.setAdditionalHeader("Referer", "http://www.189.cn/dqmh/my189/initMy189home.do");
			webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			HtmlPage htmlPage2 = webClient.getPage(webRequest1);
			tracer.addTag("parser.telecom.parser.sendSms.page2",htmlPage2.asXml());
			
			String page2 = htmlPage2.asXml();
			int s = page2.indexOf("doQuery('");
			String substring = page2.substring(s,page2.indexOf(";", s));
			int i = substring.indexOf(",'");
			String prodtype = substring.substring(i+2, substring.indexOf(",'", i+1)-1);
			webParam.setHtml(prodtype);
			tracer.addTag("parser.telecom.parser.sendSms.prodtype",prodtype);
			
			WebRequest webRequest = new WebRequest(new URL("http://ha.189.cn/service/bill/getRand.jsp"), HttpMethod.POST);
			
			webRequest.setAdditionalHeader("Host", "ha.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://ha.189.cn");
			webRequest.setAdditionalHeader("Referer", "http://ha.189.cn/service/iframe/feeQuery_iframe.jsp?SERV_NO=FSE-2-2&fastcode=20000356&cityCode=ha");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("PRODTYPE", prodtype));
			webRequest.getRequestParameters().add(new NameValuePair("RAND_TYPE", "002"));
			webRequest.getRequestParameters().add(new NameValuePair("BureauCode", taskMobile.getAreacode()));
			webRequest.getRequestParameters().add(new NameValuePair("ACC_NBR", taskMobile.getPhonenum()));
			webRequest.getRequestParameters().add(new NameValuePair("PROD_TYPE", prodtype));
//			webRequest.getRequestParameters().add(new NameValuePair("PROD_PWD", ""));
			webRequest.getRequestParameters().add(new NameValuePair("REFRESH_FLAG", "1"));
//			webRequest.getRequestParameters().add(new NameValuePair("BEGIN_DATE", ""));
//			webRequest.getRequestParameters().add(new NameValuePair("END_DATE", ""));
			webRequest.getRequestParameters().add(new NameValuePair("ACCT_DATE", getDateBefore("yyyyMM", 0)));
			webRequest.getRequestParameters().add(new NameValuePair("FIND_TYPE", "2"));
//			webRequest.getRequestParameters().add(new NameValuePair("SERV_NO", ""));
			webRequest.getRequestParameters().add(new NameValuePair("QRY_FLAG", "1"));
			webRequest.getRequestParameters().add(new NameValuePair("ValueType", "4"));
			webRequest.getRequestParameters().add(new NameValuePair("MOBILE_NAME", taskMobile.getPhonenum()));
			webRequest.getRequestParameters().add(new NameValuePair("OPER_TYPE", "CR1"));
//			webRequest.getRequestParameters().add(new NameValuePair("PASSWORD", ""));
			
			Page page = webClient.getPage(webRequest);
			webParam.setPage(page);
			webParam.setWebClient(webClient);
			tracer.addTag("parser.telecom.parser.sendSms.page3",page.getWebResponse().getContentAsString());
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.sendSms.error","网络异常");
			return null;
		}
		return webParam;
	}
	
	
	//验证短信验证码
	public WebParam setSmscode(TaskMobile taskMobile, MessageLogin messageLogin) throws Exception{
		tracer.addTag("parser.telecom.parser.sendSms.taskid",taskMobile.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		
		String url = "http://ha.189.cn/service/iframe/bill/iframe_inxxall.jsp?PRODTYPE="+taskMobile.getNexturl()+"&RAND_TYPE=002&BureauCode="+taskMobile.getAreacode()+"&ACC_NBR="+taskMobile.getPhonenum()+"&PROD_TYPE="+taskMobile.getNexturl()+"&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&ACCT_DATE="+getDateBefore("yyyyMM", 0)+"&FIND_TYPE=1&SERV_NO=&QRY_FLAG=1&ValueType=4&MOBILE_NAME="+taskMobile.getPhonenum()+"&OPER_TYPE=CR1&PASSWORD="+messageLogin.getSms_code();
		HtmlPage htmlPage = getHtmlPage(webClient, url);
		if(200 == htmlPage.getWebResponse().getStatusCode()){
			webParam.setHtmlPage(htmlPage);
			tracer.addTag("parser.telecom.parser.sendSms.page","<xmp>"+htmlPage.asXml()+"</xmp>");
		}
		
		return webParam;
	}
	
	//获取用户信息
	public WebParam<TelecomHenanUserInfo> getUserInfo(TaskMobile taskMobile) {
		tracer.addTag("parser.telecom.parser.getUserInfo.taskid",taskMobile.getTaskid());
		try {
			WebParam<TelecomHenanUserInfo> webParam = new WebParam<TelecomHenanUserInfo>();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			TelecomHenanUserInfo telecomHenanUserInfo = new TelecomHenanUserInfo();
			
			String url = "http://ha.189.cn/service/iframe/manage/my_selfinfo_iframe.jsp?fastcode=20000374&cityCode=ha";
			tracer.addTag("parser.telecom.parser.getUserInfo.url",url);
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.getUserInfo.page","<xmp>"+htmlPage.asXml()+"</xmp>");
			webParam.setHtmlPage(htmlPage);
			if(200 == htmlPage.getWebResponse().getStatusCode()){
				Document document = Jsoup.parse(htmlPage.asXml());
				String name = getNextLabelByKeyword(document, "客户名称", "td");
				String type = getNextLabelByKeyword(document, "客户类型", "td");
				String idNum = getNextLabelByKeyword(document, "证件号码", "td");
				String tel = getNextLabelByKeyword(document, "联系电话", "td");
				
				telecomHenanUserInfo.setName(name);
				telecomHenanUserInfo.setType(type);
				telecomHenanUserInfo.setIdNum(idNum);
				telecomHenanUserInfo.setContactTel(tel);
			}
			
			String url1 = "http://ha.189.cn/service/iframe/bill/iframe_ye.jsp?ACC_NBR="+taskMobile.getPhonenum()+"&PROD_TYPE="+taskMobile.getNexturl();
			tracer.addTag("parser.telecom.parser.getUserInfo.url1",url1);
			HtmlPage htmlPage1 = getHtmlPage(webClient, url1);
			tracer.addTag("parser.telecom.parser.getUserInfo.page1","<xmp>"+htmlPage1.asXml()+"</xmp>");
//			webParam.setHtml(htmlPage1.asXml());
			if(200 == htmlPage1.getWebResponse().getStatusCode()){
				String asText = htmlPage1.asText();
				int i = asText.indexOf("账户余额");
				String balance = asText.substring(i, asText.indexOf(", 充", i));
				
				telecomHenanUserInfo.setAccountBalance(balance);
			}
			
			String url2 = "http://ha.189.cn/service/iframe/bill/iframe_intc.jsp?SERV_NO=FSE-2-5&FROM_FLAG=1&ACC_NBR="+taskMobile.getPhonenum()+"&ACCTNBR97="+taskMobile.getPhonenum()+"&PROD_TYPE="+taskMobile.getNexturl();
			tracer.addTag("parser.telecom.parser.getUserInfo.url2",url2);
			HtmlPage htmlPage2 = getHtmlPage(webClient, url2);
			tracer.addTag("parser.telecom.parser.getUserInfo.page2","<xmp>"+htmlPage2.asXml()+"</xmp>");
			if(200 == htmlPage2.getWebResponse().getStatusCode()){
				String string = htmlPage2.asText();
				String string2 = string.replace(" ", ",");
				int i = string2.indexOf("短信类");
				if(i != -1){
					int j = string2.indexOf(",", i);
					if(j != -1){
						String string3 = string2.substring(i+4, j-1);
						telecomHenanUserInfo.setPalnName(string3);
					}
				}
			}
			
			if(null != telecomHenanUserInfo){
				telecomHenanUserInfo.setTaskid(taskMobile.getTaskid());
				List<TelecomHenanUserInfo> telecomHenanUserInfos = new ArrayList<TelecomHenanUserInfo>();
				telecomHenanUserInfos.add(telecomHenanUserInfo);
				webParam.setList(telecomHenanUserInfos);
				tracer.addTag("parser.telecom.parser.getUserInfo.list",webParam.getList().toString());
			}
			webParam.setUrl(url+url1+url2);
			tracer.addTag("parser.telecom.parser.getUserInfo.webparam",webParam.toString());
			return webParam;
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.getUserInfo.Exception",e.toString());
			return null;
		}
		
	}
	
	
	//获取用户在用业务
	public WebParam<TelecomHenanServer> getServer(TaskMobile taskMobile) {
		try {
			tracer.addTag("parser.telecom.parser.getServer.taskid",taskMobile.getTaskid());
			WebParam<TelecomHenanServer> webParam = new WebParam<TelecomHenanServer>();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			List<TelecomHenanServer> telecomHenanServers = new ArrayList<TelecomHenanServer>();
			
			String url = "http://ha.189.cn/service/iframe/bill/iframe_zyyw.jsp?ACC_NBR="+taskMobile.getPhonenum()+"&PROD_TYPE="+taskMobile.getNexturl();
			webParam.setUrl(url);
			tracer.addTag("parser.telecom.parser.getServer.url",url);
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.getServer.page","<xmp>"+htmlPage.asXml()+"</xmp>");
			webParam.setHtmlPage(htmlPage);
			if(200 == htmlPage.getWebResponse().getStatusCode()){
				Document document = Jsoup.parse(htmlPage.asXml());
				Elements trs = document.select("tr");
				String taskid = taskMobile.getTaskid();
				for (Element tr : trs) {
					Elements tds = tr.select("td");
					if(null != tds && tds.size() > 0){
						TelecomHenanServer telecomHenanServer = new TelecomHenanServer();
						List<String> txt = new ArrayList<String>();
						for (Element td : tds) {
							String text = td.text();
							txt.add(text);
						}
						telecomHenanServer.setServerName(txt.get(0));
						telecomHenanServer.setStartDate(txt.get(1));
						telecomHenanServer.setEndDate(txt.get(2));
						telecomHenanServer.setCancel(txt.get(3));
						if(null != telecomHenanServer){
							telecomHenanServer.setTaskid(taskid);
							telecomHenanServers.add(telecomHenanServer);
						}
					}
				}
				if(null != telecomHenanServers){
					webParam.setList(telecomHenanServers);
				}
				return webParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.getServer.Exception",e.toString());
			return null;
		}
		return null;
	}
	
	//获取用户月账单
	public WebParam<TelecomHenanMonthBill> getBillData(TaskMobile taskMobile, int i) {
		try {
			tracer.addTag("parser.telecom.parser.getBillData.taskid",taskMobile.getTaskid());
			WebParam<TelecomHenanMonthBill> webParam = new WebParam<TelecomHenanMonthBill>();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			List<TelecomHenanMonthBill> telecomHenanMonthBills = new ArrayList<TelecomHenanMonthBill>();
			
			String dateBefore = getDateBefore("yyyyMM", i);
			String url = "http://ha.189.cn/service/iframe/bill/iframe_zd.jsp?ACC_NBR="+taskMobile.getPhonenum()+"&DATE="+dateBefore+"&AreaCode="+taskMobile.getAreacode()+"&usertype=1";
			webParam.setUrl(url);
			tracer.addTag("parser.telecom.parser.getBillData.url"+i,url);
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.getBillData.page"+i,"<xmp>"+htmlPage.asXml()+"</xmp>");
			webParam.setHtmlPage(htmlPage);
			if(200 == htmlPage.getWebResponse().getStatusCode()){
				Document document = Jsoup.parse(htmlPage.asXml());
				Elements tables = document.select(".table");
				if(null != tables && tables.size() > 0){
					Element table = tables.first();
					Elements trs = table.select("tr");
					if(null != trs && trs.size() > 0){
						for (int j = 0; j < trs.size()-1; j++) {
							Element tr = trs.get(j);
							TelecomHenanMonthBill telecomHenanMonthBill = new TelecomHenanMonthBill();
							Elements tds = tr.select("td");
							if(null != tds && tds.size() > 0){
								List<String> txt = new ArrayList<String>();
								for (Element td : tds) {
									String text = td.text();
									txt.add(text);
								}
								telecomHenanMonthBill.setBillType(txt.get(0));
								telecomHenanMonthBill.setBillDate(dateBefore);
								telecomHenanMonthBill.setFee(txt.get(1));
								telecomHenanMonthBill.setTaskid(taskMobile.getTaskid());
								telecomHenanMonthBills.add(telecomHenanMonthBill);
							}
						}
						webParam.setList(telecomHenanMonthBills);
					}
				}
			}
			return webParam;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.getBillData.Exception",e.toString());
			return null;
		}
	}
	
	//获取近半年的缴费记录
	public WebParam<TelecomHenanPayment> getPaymentHistory(TaskMobile taskMobile) {
		try {
			tracer.addTag("parser.telecom.parser.getPaymentHistory.taskid",taskMobile.getTaskid());
			WebParam<TelecomHenanPayment> webParam = new WebParam<TelecomHenanPayment>();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			List<TelecomHenanPayment> telecomHenanPayments = new ArrayList<TelecomHenanPayment>();
			
			String url = "http://ha.189.cn/service/pay/khxxgl/myserv_snList.jsp?REFRESH_FLAG=2&IPAGE_INDEX=1&ASK_TYPE=100&AREACODE="+taskMobile.getAreacode()+"&START_ASK_DATE="+getDateBefore("yyyy-MM-dd", 6)+"&END_ASK_DATE="+getDateBefore("yyyy-MM-dd", 0)+"&STATE=ALL";
			webParam.setUrl(url);
			tracer.addTag("parser.telecom.parser.getPaymentHistory.url",url);
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.getPaymentHistory.page","<xmp>"+htmlPage.asXml()+"</xmp>");
			webParam.setHtmlPage(htmlPage);
			if(200 == htmlPage.getWebResponse().getStatusCode()){
				Document document = Jsoup.parse(htmlPage.asXml());
				Element table = document.select("#Infotable").first();
				if(null != table){
					Element tbody = table.select("tbody").first();
					Elements trs = tbody.select("tr");
					if(null != trs && trs.size() > 0){
						for (Element tr : trs) {
							Elements tds = tr.select("td");
							if(null != tds && tds.size() > 0){
								List<String> txt = new ArrayList<>();
								TelecomHenanPayment telecomHenanPayment = new TelecomHenanPayment();
								for (Element td : tds) {
									String text = td.text();
									txt.add(text);
								}
								telecomHenanPayment.setSerialNum(txt.get(0));
								telecomHenanPayment.setPayType(txt.get(1));
								telecomHenanPayment.setFee(txt.get(2));
								telecomHenanPayment.setPayDate(txt.get(3));
								telecomHenanPayment.setTaskid(taskMobile.getTaskid());
								telecomHenanPayments.add(telecomHenanPayment);
							}
						}
						webParam.setList(telecomHenanPayments);
					}
				}
			}
			
			return webParam;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.getPaymentHistory.Exception",e.toString());
			return null;
		}
	}
	
	//获取短信详单信息
	public WebParam<TelecomHenanSMSDetail> getSMSDetails(TaskMobile taskMobile, int i) {
		try {
			tracer.addTag("parser.telecom.parser.getSMSDetails.taskid",taskMobile.getTaskid());
			WebParam<TelecomHenanSMSDetail> webParam = new WebParam<TelecomHenanSMSDetail>();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			List<TelecomHenanSMSDetail> telecomHenanSMSDetails = new ArrayList<TelecomHenanSMSDetail>();
			
			String dateBefore = getDateBefore("yyyyMM", i);
			String url = "http://ha.189.cn/service/iframe/bill/iframe_inxxall.jsp?ACC_NBR="+taskMobile.getPhonenum()+"&PROD_TYPE="+taskMobile.getNexturl()+"&BEGIN_DATE=&END_DATE=&ValueType=4&REFRESH_FLAG=1&FIND_TYPE=5&radioQryType=on&QRY_FLAG=1&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+dateBefore;
			webParam.setUrl(url);
			tracer.addTag("parser.telecom.parser.getSMSDetails.url"+i,url);
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.getSMSDetails.page"+i,"<xmp>"+htmlPage.asXml()+"</xmp>");
			webParam.setHtmlPage(htmlPage);
			if(200 == htmlPage.getWebResponse().getStatusCode()){
				Document document = Jsoup.parse(htmlPage.asXml());
				Element listQry = document.select("#listQry").first();
				Element tbody = listQry.select("tbody").first();
				Elements trs = tbody.select("tr");
				if(null != trs && trs.size() > 0){
					for (Element tr : trs) {
						Elements tds = tr.select("td");
						if(null != tds && tds.size() > 0){
							List<String> txt = new ArrayList<>();
							TelecomHenanSMSDetail telecomHenanSMSDetail = new TelecomHenanSMSDetail();
							for (Element td : tds) {
								String text = td.text();
								txt.add(text);
							}
							telecomHenanSMSDetail.setSendNum(txt.get(0));
							telecomHenanSMSDetail.setReceiveNum(txt.get(1));
							telecomHenanSMSDetail.setReceiveDate(txt.get(2));
							telecomHenanSMSDetail.setType(txt.get(3));
							telecomHenanSMSDetail.setFee(txt.get(4));
							telecomHenanSMSDetail.setExtraFee(txt.get(5));
							telecomHenanSMSDetail.setTaskid(taskMobile.getTaskid());
							telecomHenanSMSDetails.add(telecomHenanSMSDetail);
						}
					}
					webParam.setList(telecomHenanSMSDetails);
				}
			}
			return webParam;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.getSMSDetails.Exception",e.toString());
			return null;
		}
	}

	//获取通话详单
	@Retryable(value={RuntimeException.class,},maxAttempts=3,backoff = @Backoff(delay = 1000l,multiplier = 1.2))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public WebParam<TelecomHenanCallDetail> getCallDetails(TaskMobile taskMobile, int i) throws IOException{
			tracer.addTag("parser.telecom.parser.getCallDetails.taskid",taskMobile.getTaskid());
			String dateBefore = getDateBefore("yyyyMM", i);
			WebParam<TelecomHenanCallDetail> webParam = new WebParam<TelecomHenanCallDetail>();
			webParam.setHtml(dateBefore);
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			List<TelecomHenanCallDetail> telecomHenanCallDetails = new ArrayList<TelecomHenanCallDetail>();
			
			String url = "http://ha.189.cn/service/iframe/bill/iframe_inxxall.jsp?ACC_NBR="+taskMobile.getPhonenum()+"&PROD_TYPE="+taskMobile.getNexturl()+"&BEGIN_DATE=&END_DATE=&ValueType=4&REFRESH_FLAG=1&FIND_TYPE=1&radioQryType=on&QRY_FLAG=1&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+dateBefore;
			webParam.setUrl(url);
			tracer.addTag("parser.telecom.parser.getCallDetails.url"+i,url);
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.getCallDetails.page"+i,"<xmp>"+htmlPage.asXml()+"</xmp>");
			webParam.setHtmlPage(htmlPage);
			if(200 == htmlPage.getWebResponse().getStatusCode()){
				Document document = Jsoup.parse(htmlPage.asXml());
				Element listQry = document.select("#listQry").first();
				Element tbody = listQry.select("tbody").first();
				Elements trs = tbody.select("tr");
				if(null != trs && trs.size() > 0){
					for (Element tr : trs) {
						Elements tds = tr.select("td");
						if(null != tds && tds.size() > 0){
							List<String> txt = new ArrayList<>();
							TelecomHenanCallDetail telecomHenanCallDetail = new TelecomHenanCallDetail();
							for (Element td : tds) {
								String text = td.text();
								txt.add(text);
							}
							telecomHenanCallDetail.setCallNum(txt.get(0));
							telecomHenanCallDetail.setCalledNum(txt.get(1));
							telecomHenanCallDetail.setStartDate(txt.get(2));
							telecomHenanCallDetail.setEndDate(txt.get(3));
							telecomHenanCallDetail.setTimes(txt.get(4));
							telecomHenanCallDetail.setCallType(txt.get(5));
							telecomHenanCallDetail.setFee(txt.get(6));
							telecomHenanCallDetail.setExtraFee(txt.get(7));
							telecomHenanCallDetail.setTaskid(taskMobile.getTaskid());
							telecomHenanCallDetails.add(telecomHenanCallDetail);
						}
					}
					webParam.setList(telecomHenanCallDetails);
				}
			}
			return webParam;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.getCallDetails.Exception",e.getMessage());
			tracer.addTag("parser.telecom.parser.getCallDetails.retry","重试机制触发~Exception");
			throw new RuntimeException("详单查询异常，重试机制触发！");
		}
	}
	
	//根据URL获取HtmlPage
	public static HtmlPage getHtmlPage(WebClient webClient, String url) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	//根据URL获取Page
	public static Page getPage(WebClient webClient, String url) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		return page;
	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i){
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}

}
