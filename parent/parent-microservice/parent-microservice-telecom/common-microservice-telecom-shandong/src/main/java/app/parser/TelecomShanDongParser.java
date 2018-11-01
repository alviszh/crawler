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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongCallDetail;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongIncrement;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongIntegral;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongMonthBill;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongPayment;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongSMSDetail;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;

@Component
public class TelecomShanDongParser {

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
		String url = "http://sd.189.cn/selfservice/service/account";
		String url2 = "http://sd.189.cn/selfservice/bill?tag=monthlyDetail";
		try {
			HtmlPage htmlPage = getHtmlPage(webClient, url);
			tracer.addTag("parser.telecom.parser.sendSms.page1",htmlPage.asXml());
			HtmlPage htmlPage2 = getHtmlPage(webClient, url2);
			tracer.addTag("parser.telecom.parser.sendSms.page2",htmlPage2.asXml());
			
			HtmlImage codeimg = htmlPage2.getFirstByXPath("//img[@id='rand_rn']");
			HtmlTextInput code = (HtmlTextInput) htmlPage2.getFirstByXPath("//input[@id='validatecode_2busi']");
			HtmlElement getbtn = (HtmlElement) htmlPage2.getFirstByXPath("//a[@id='getDynamicHref_rn']");
			if(null != codeimg){
				String verifycode = chaoJiYingOcrService.getVerifycode(codeimg, "1004");
				tracer.addTag("parser.telecom.parser.sendSms.code",verifycode);
				if(null != code && null != getbtn){
					code.setText(verifycode);
					webParam.setHtml(verifycode);
					HtmlPage click = getbtn.click();
					webParam.setHtmlPage(click);
					tracer.addTag("parser.telecom.parser.sendSms.success.page",click.asXml());
				}
			}
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
		String imgcode = taskMobile.getNexturl();//将发送短信验证码时的图片验证码获取到
		
		String smscode = messageLogin.getSms_code();
		String idnum = taskMobile.getBasicUser().getIdnum();
		String name = taskMobile.getBasicUser().getName();
		String username = java.net.URLEncoder.encode(name, "UTF-8");
		
		String url = "http://sd.189.cn/selfservice/service/realnVali";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json");
		webRequest.setAdditionalHeader("Host", "sd.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/bill?tag=hisDetail");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody("{\"username_2busi\":\""+username+"\",\"credentials_type_2busi\":\"1\",\"credentials_no_2busi\":\""+idnum+"\",\"validatecode_2busi\":\""+imgcode+"\",\"randomcode_2busi\":\""+smscode+"\",\"randomcode_flag\":\"0\",\"rid\":1,\"fid\":\"bill_monthlyDetail\"}");
		
		Page page = webClient.getPage(webRequest);
		if(200 == page.getWebResponse().getStatusCode()){
			webParam.setPage(page);
			webParam.setWebClient(webClient);
		}
		
		return webParam;
	}
	
	
	//获取每月的账单
	public WebParam<TelecomShandongMonthBill> getBillData(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getBillData.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		try {
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		WebParam<TelecomShandongMonthBill> webParam = new WebParam<TelecomShandongMonthBill>();
		
		String url = "http://sd.189.cn/selfservice/bill/queryTwoBill";
		tracer.addTag("parser.telecom.parser.getBillData.url."+i,url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webParam.setUrl(url);
		
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json");
		webRequest.setAdditionalHeader("Host", "sd.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/bill/");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody("{\"valueType\":\"1\",\"value\":\""+taskMobile.getPhonenum()+"\",\"billingCycle\":\""+getDateBefore("yyyyMM", i)+"\",\"areaCode\":\""+taskMobile.getAreacode()+"\",\"queryType\":\"5\",\"proType\":\"4\"}");
		
		Page respage = webClient.getPage(webRequest);
		tracer.addTag("parser.telecom.parser.getBillData.respage."+i,respage.getWebResponse().getContentAsString());
		webParam.setCode(respage.getWebResponse().getStatusCode());
		if(200 == respage.getWebResponse().getStatusCode()){
			webParam.setPage(respage);
			String billRec = respage.getWebResponse().getContentAsString();
			if(billRec.contains("成功")){
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(billRec); // 创建JsonObject对象
				String billDate = object.get("billingCycle").getAsString();
				String accNbr = object.get("accNbr").getAsString();
				JsonArray itemsInit = object.get("itemsInit").getAsJsonArray();
				if(itemsInit.size()>0){
					List<TelecomShandongMonthBill> bills = new ArrayList<TelecomShandongMonthBill>();
					for (JsonElement acctItem : itemsInit) {
						TelecomShandongMonthBill bill = new TelecomShandongMonthBill();
						String billType = acctItem.getAsJsonObject().get("name").getAsString();
						String fee = acctItem.getAsJsonObject().get("value").getAsString();
						
						bill.setBillDate(billDate);
						bill.setBillType(billType);
						bill.setFee(fee);
						bill.setTaskid(taskMobile.getTaskid());
						
						bills.add(bill);
					}
					webParam.setList(bills);
					tracer.addTag("parser.telecom.parser.getBillData.list."+i,bills.toString());
				}
			}
		}
		return webParam;
	}

	//获取用户信息
	public WebParam<TelecomShandongUserInfo> getUserInfo(TaskMobile taskMobile) throws Exception{
		tracer.addTag("parser.telecom.parser.getUserInfo.taskid",taskMobile.getTaskid());
		JsonParser parser = new JsonParser();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		try {
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		WebParam<TelecomShandongUserInfo> webParam = new WebParam<TelecomShandongUserInfo>();
		String url = "http://sd.189.cn/selfservice/cust/querymanage?100";
		tracer.addTag("parser.telecom.parser.getUserInfo.url",url);
		String htmljson = "";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json");
		webRequest.setAdditionalHeader("Host", "sd.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/cust/manage");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		Page userPage = webClient.getPage(webRequest);
		List<TelecomShandongUserInfo> telecomShandongUserInfos = new ArrayList<TelecomShandongUserInfo>();
		TelecomShandongUserInfo telecomShandongUserInfo = new TelecomShandongUserInfo();
		if(userPage.getWebResponse().getStatusCode() == 200){
			String strUserPage = userPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.telecom.parser.getUserInfo.userPage",strUserPage);
			htmljson += strUserPage;
			if(strUserPage.contains("prodRecords")){
				JsonObject object = (JsonObject)parser.parse(strUserPage);
				JsonObject result = object.get("result").getAsJsonObject();
				JsonObject prodRecords = result.get("prodRecords").getAsJsonObject();
				JsonObject prodRecord = prodRecords.get("prodRecord").getAsJsonObject();
				JsonObject custInfo = prodRecord.get("custInfo").getAsJsonObject();
				if(null != custInfo){
					String name = custInfo.get("name").getAsString();//客户姓名
					String indentNbrTypeName = custInfo.get("indentNbrTypeName").getAsString();//证件类型
					String indentNbr = custInfo.get("indentNbr").getAsString();//证件号码
					String addr = custInfo.get("addr").getAsString();//客户地址
					String industryClassName = custInfo.get("industryClassName").getAsString();//行业类型
					String linkMan = custInfo.get("linkMan").getAsString();//联系人
					String linkNbr = custInfo.get("linkNbr").getAsString();//联系电话
					
					telecomShandongUserInfo.setName(name);
					telecomShandongUserInfo.setIdType(indentNbrTypeName);
					telecomShandongUserInfo.setIdNum(indentNbr);
					telecomShandongUserInfo.setAddress(addr);
					telecomShandongUserInfo.setCareerType(industryClassName);
					telecomShandongUserInfo.setContacts(linkMan);
					telecomShandongUserInfo.setContactTel(linkNbr);
				}
			}
		}
		//获取当前套餐名,用户星级
		String url2 = "http://sd.189.cn/selfservice/service/account";
		tracer.addTag("parser.telecom.parser.getUserInfo.url2",url2);
		HtmlPage htmlPage2 = getHtmlPage(webClient,url2);
		
		if(htmlPage2.getWebResponse().getStatusCode() == 200){
			String page2xml = htmlPage2.asXml();
			tracer.addTag("parser.telecom.parser.getUserInfo.userPage2","<xmp>"+page2xml+"</xmp>");
			webParam.setHtmlPage(htmlPage2);
			if(page2xml.contains("套餐名称")){
				Document doc = Jsoup.parse(page2xml);
				String planName = doc.select("[style=color:#000;]").text();
				String starLevel = doc.select("#starLevel").text();
				
				telecomShandongUserInfo.setPlanName(planName);
				telecomShandongUserInfo.setStarLevel(starLevel);
			}
		}
		
		//获取当前余额
		String url3 = "http://sd.189.cn/selfservice/bill/queryBalance";
		tracer.addTag("parser.telecom.parser.getUserInfo.url3",url3);
		WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.POST);
		webRequest3.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest3.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest3.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest3.setAdditionalHeader("Connection", "keep-alive");
		webRequest3.setAdditionalHeader("Content-Type", "application/json");
		webRequest3.setAdditionalHeader("Host", "sd.189.cn");
		webRequest3.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest3.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/bill?tag=hisDetail");
		webRequest3.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest3.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		webRequest3.setRequestBody("{\"accNbr\":\""+taskMobile.getPhonenum()+"\",\"areaCode\":\""+taskMobile.getAreacode()+"\",\"accNbrType\":\"4\"}");
		Page htmlPage3 = webClient.getPage(webRequest3);
		if(htmlPage3.getWebResponse().getStatusCode() == 200){
			String page3json = htmlPage3.getWebResponse().getContentAsString();
			tracer.addTag("parser.telecom.parser.getUserInfo.userPage3",page3json);
			htmljson += page3json;
			if(page3json.contains("OK")){
				JsonObject object = (JsonObject) parser.parse(htmlPage3.getWebResponse().getContentAsString()); // 创建JsonObject对象
				String balance = object.get("balance").getAsString();
				
				telecomShandongUserInfo.setAccountBalance(balance);
			}
		}
		webParam.setHtml(htmljson);
		webParam.setUrl(url+"+,+"+url2+"+,+"+url3);
		if(null != telecomShandongUserInfo){
			telecomShandongUserInfo.setTaskid(taskMobile.getTaskid());
			telecomShandongUserInfos.add(telecomShandongUserInfo);
			webParam.setList(telecomShandongUserInfos);
			tracer.addTag("parser.telecom.parser.getUserInfo.list",telecomShandongUserInfos.toString());
		}
		
		return webParam;
	}
	
	/*
	 * @Des 获取积分生成明细
	 * 
	 */
	public WebParam<TelecomShandongIntegral> getIntegral(TaskMobile taskMobile) throws Exception{
		tracer.addTag("parser.telecom.parser.getIntegral.taskid",taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		try {
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		WebParam<TelecomShandongIntegral> webParam = new WebParam<TelecomShandongIntegral>();
		
		String url = "http://sd.189.cn/selfservice/jf/queryPointDetailInfo";
		tracer.addTag("parser.telecom.parser.getIntegral.url",url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json");
		webRequest.setAdditionalHeader("Host", "sd.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/jf/queryCredit");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody("{\"accNbr\":\""+taskMobile.getPhonenum()+"\",\"areaCode\":\""+taskMobile.getAreacode()+"\",\"accNbrType\":\"4\"}");
		
		
		webParam.setUrl(url);
		Page respage = webClient.getPage(webRequest);
		tracer.addTag("parser.telecom.parser.getIntegral.respage",respage.getWebResponse().getContentAsString());
		webParam.setCode(respage.getWebResponse().getStatusCode());
		if(200 == respage.getWebResponse().getStatusCode()){
			webParam.setPage(respage);
			String integral = respage.getWebResponse().getContentAsString();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(integral); // 创建JsonObject对象
			String resultMsg = object.get("resultMsg").getAsString();
			if(resultMsg.contains("成功")){
				JsonArray list = object.get("jsonArray").getAsJsonArray();
				if(list.size()>0){
					List<TelecomShandongIntegral> integrals = new ArrayList<TelecomShandongIntegral>();
					for (JsonElement ele : list) {
						TelecomShandongIntegral telecomShandongIntegral = new TelecomShandongIntegral();
						String ProduceDate = ele.getAsJsonObject().get("ProduceDate").getAsString();//日期
						String Bonus = ele.getAsJsonObject().get("Bonus").getAsString();			//调整积分
						String LeftBonus = ele.getAsJsonObject().get("LeftBonus").getAsString();	//积分余额
						String ConstituteType = ele.getAsJsonObject().get("ConstituteType").getAsString();	//积分来源 （1代表 新增积分）
						
						telecomShandongIntegral.setIntegralDate(ProduceDate);
						telecomShandongIntegral.setIntegralCount(Bonus);
						telecomShandongIntegral.setIntegralSum(LeftBonus);
						telecomShandongIntegral.setIntegralSource(ConstituteType);
						telecomShandongIntegral.setTaskid(taskMobile.getTaskid());
						
						integrals.add(telecomShandongIntegral);
					}
					webParam.setList(integrals);
					tracer.addTag("parser.telecom.parser.getIntegral.list",integrals.toString());
				}
			}else{
				webParam.setHtml(resultMsg);
				tracer.addTag("parser.telecom.parser.getIntegral.resultMsg",resultMsg);
			}
		}
		return webParam;
	}
	
	//获取充值缴费信息
	public WebParam<TelecomShandongPayment> getPaymentHistory(TaskMobile taskMobile) throws Exception{
		tracer.addTag("parser.telecom.parser.getPaymentHistory.taskid",taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		try {
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		WebParam<TelecomShandongPayment> webParam = new WebParam<TelecomShandongPayment>();
		
		String url = "http://sd.189.cn/selfservice/bill/queryPaymentDetail";
		tracer.addTag("parser.telecom.parser.getPaymentHistory.url",url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webParam.setUrl(url);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json");
		webRequest.setAdditionalHeader("Host", "sd.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/pay");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody("{\"accNbr\":\""+taskMobile.getPhonenum()+"\",\"areaCode\":\""+taskMobile.getAreacode()+"\",\"beginDate\":\""+getDateBefore("yyyy-MM", 6)+"-01\",\"endDate\":\""+getDateBefore("yyyy-MM-dd", 0)+"\",\"accNbrType\":\"4\"}");
		
		Page respage = webClient.getPage(webRequest);
		webParam.setCode(respage.getWebResponse().getStatusCode());
		if(200 == respage.getWebResponse().getStatusCode()){
			webParam.setPage(respage);
			String strpayment = respage.getWebResponse().getContentAsString();
			tracer.addTag("parser.telecom.parser.getPaymentHistory.page",strpayment);
			if(strpayment.contains("amount")){
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(strpayment); // 创建JsonObject对象
				JsonArray items = object.get("items").getAsJsonArray();
				if(items.size()>0){
					List<TelecomShandongPayment> payments = new ArrayList<TelecomShandongPayment>();
					for (JsonElement ele : items) {
						TelecomShandongPayment payment = new TelecomShandongPayment();
						String paymentMethod = ele.getAsJsonObject().get("paymentMethod").getAsString();	//缴费方式
						String amount = ele.getAsJsonObject().get("amount").getAsString();					//缴费金额（元）
						String charge = ele.getAsJsonObject().get("charge").getAsString();					//销账金额（元）
						String due = ele.getAsJsonObject().get("due").getAsString();						//违约金（元）
						String paymentDate = ele.getAsJsonObject().get("paymentDate").getAsString();		//付款日期
						String orgName = ele.getAsJsonObject().get("orgName").getAsString();		//付款日期
						
						payment.setPayType(paymentMethod);
						payment.setPayCount(amount);
						payment.setCost(charge);
						payment.setBreach(due);
						payment.setPayDate(paymentDate);
						payment.setOrgName(orgName);
						payment.setTaskid(taskMobile.getTaskid());
						
						payments.add(payment);
					}
					webParam.setList(payments);
					tracer.addTag("parser.telecom.parser.getPaymentHistory.list",payments.toString());
				}
			}
		}
		return webParam;
	}
	
	
	//获取增值详单
	public WebParam<TelecomShandongIncrement> getIncrement(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getIncrement.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		try {
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		WebParam<TelecomShandongIncrement> webParam = new WebParam<TelecomShandongIncrement>();
		
		String url = "http://sd.189.cn/selfservice/bill/queryBillDetail";
		tracer.addTag("parser.telecom.parser.getIncrement.url."+i,url);
		webParam.setUrl(url);
		Page detailsPage = getDetailsPage(webClient, url, taskMobile.getPhonenum(), "2", i);
		if(200 == detailsPage.getWebResponse().getStatusCode()){
			String htmljson = detailsPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.telecom.parser.getIncrement.page."+i,htmljson);
			webParam.setHtml(htmljson);
			List<TelecomShandongIncrement> telecomShandongIncrements = new ArrayList<TelecomShandongIncrement>();
			if(htmljson.contains("成功")){
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(htmljson);		// 创建JsonObject对象
				
				JsonArray items = object.get("items").getAsJsonArray();
				if(items.size() > 0){
					for (JsonElement ele : items) {
						TelecomShandongIncrement telecomShandongIncrement = new TelecomShandongIncrement();
						JsonObject item = ele.getAsJsonObject();
						String billingNbr = item.get("billingNbr").getAsString();				//业务名称
						String beginTime = item.get("beginTime").getAsString();					//申请(起始)时间
						String charge = item.get("charge").getAsString();						//费用/元
						String spName = item.get("spName").getAsString();						//sp名称
						
						telecomShandongIncrement.setServiceName(billingNbr);
						telecomShandongIncrement.setStartDate(beginTime);
						telecomShandongIncrement.setFee(charge);
						telecomShandongIncrement.setSpName(spName);
						telecomShandongIncrement.setTaskid(taskMobile.getTaskid());
						
						telecomShandongIncrements.add(telecomShandongIncrement);
					}
				}
			}
			webParam.setList(telecomShandongIncrements);
			tracer.addTag("parser.telecom.parser.getIncrement.htmljson."+i,htmljson);
			tracer.addTag("parser.telecom.parser.getIncrement.list."+i,telecomShandongIncrements.toString());
		}
		return webParam;
	}
	
	//获取通话详单
	@Retryable(value={RuntimeException.class,},maxAttempts=3,backoff = @Backoff(delay = 1000l,multiplier = 1.2))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public WebParam<TelecomShandongCallDetail> getCallDetails(TaskMobile taskMobile, int i) throws IOException{
		tracer.addTag("parser.telecom.parser.getCallDetails.taskid."+i,taskMobile.getTaskid());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			WebParam<TelecomShandongCallDetail> webParam = new WebParam<TelecomShandongCallDetail>();
			
			String url = "http://sd.189.cn/selfservice/bill/queryBillDetail";
			tracer.addTag("parser.telecom.parser.getCallDetails.url."+i,url);
			webParam.setUrl(url);
			Page detailsPage = getDetailsPage(webClient, url, taskMobile.getPhonenum(), "0", i);
			if(200 == detailsPage.getWebResponse().getStatusCode()){
				String jsonPage= detailsPage.getWebResponse().getContentAsString();
				tracer.addTag("parser.telecom.parser.getCallDetails.page."+i,jsonPage);
				webParam.setHtml(jsonPage);
				List<TelecomShandongCallDetail> telecomShandongCallDetails = new ArrayList<TelecomShandongCallDetail>();
				if(jsonPage.contains("成功")){
					JsonParser parser = new JsonParser();
					JsonObject object = (JsonObject) parser.parse(jsonPage); // 创建JsonObject对象
					
					JsonArray items = object.get("items").getAsJsonArray();
					if(items.size() > 0){
						for (JsonElement ele : items) {
							TelecomShandongCallDetail telecomShandongCallDetail = new TelecomShandongCallDetail();
							JsonObject item = ele.getAsJsonObject();
							String eventType = item.get("eventType").getAsString();			//类型
							String callingNbr = item.get("callingNbr").getAsString();		//主叫号码
							String calledNbr = item.get("calledNbr").getAsString();			//被叫号码
							String startTime = item.get("startTime").getAsString();			//起始时间
							String duration = item.get("duration").getAsString();			//时长（秒）
							String charge = item.get("charge").getAsString();				//话费（元）
							String position = item.get("position").getAsString();			//地点
							
							telecomShandongCallDetail.setType(eventType);
							telecomShandongCallDetail.setCallingNum(callingNbr);
							telecomShandongCallDetail.setCalledNum(calledNbr);
							telecomShandongCallDetail.setStartTime(startTime);
							telecomShandongCallDetail.setTimeCount(duration);
							telecomShandongCallDetail.setFee(charge);
							telecomShandongCallDetail.setArea(position);
							telecomShandongCallDetail.setTaskid(taskMobile.getTaskid());
							
							telecomShandongCallDetails.add(telecomShandongCallDetail);
						}
					}
				}
				webParam.setList(telecomShandongCallDetails);
				tracer.addTag("parser.telecom.parser.getCallDetails.htmljson."+i,jsonPage);
				tracer.addTag("parser.telecom.parser.getCallDetails.list."+i,telecomShandongCallDetails.toString());
			}
			return webParam;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.parser.getCallDetails.Exception",e.getMessage());
			tracer.addTag("parser.telecom.parser.getCallDetails.retry","重试机制触发~Exception");
			throw new RuntimeException("详单查询异常，重试机制触发！");
		}
	}
	
	//获取短信详单
	public WebParam<TelecomShandongSMSDetail> getSMSDetails(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getSMSDetails.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		if(null != webClient){
			try {
				webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			WebParam<TelecomShandongSMSDetail> webParam = new WebParam<TelecomShandongSMSDetail>();
			
			String url = "http://sd.189.cn/selfservice/bill/queryBillDetail";
			tracer.addTag("parser.telecom.parser.getSMSDetails.url."+i,url);
			webParam.setUrl(url);
			Page detailsPage = getDetailsPage(webClient, url, taskMobile.getPhonenum(), "1", i);
			if(200 == detailsPage.getWebResponse().getStatusCode()){
				String htmljson = detailsPage.getWebResponse().getContentAsString();
				webParam.setHtml(htmljson);
				tracer.addTag("parser.telecom.parser.getSMSDetails.page."+i,htmljson);
				List<TelecomShandongSMSDetail> telecomShandongSMSDetails = new ArrayList<TelecomShandongSMSDetail>();
				if(htmljson.contains("成功")){
					JsonParser parser = new JsonParser();
					JsonObject object = (JsonObject) parser.parse(htmljson); // 创建JsonObject对象
					
					JsonArray items = object.get("items").getAsJsonArray();
					if(items.size() > 0){
						for (JsonElement ele : items) {
							TelecomShandongSMSDetail telecomShandongSMSDetail = new TelecomShandongSMSDetail();
							JsonObject item = ele.getAsJsonObject();
							String callType = item.get("callType").getAsString();				//发送类型
							String callingNbr = item.get("callingNbr").getAsString();			//发送号码
							String calledNbr = item.get("calledNbr").getAsString();				//接收号码
							String startTime = item.get("startTime").getAsString();				//发送时间
							String charge = item.get("charge").getAsString();					//费用/元
							
							telecomShandongSMSDetail.setType(callType);
							telecomShandongSMSDetail.setCallingNum(callingNbr);
							telecomShandongSMSDetail.setCalledNum(calledNbr);
							telecomShandongSMSDetail.setSendTime(startTime);
							telecomShandongSMSDetail.setFee(charge);
							telecomShandongSMSDetail.setTaskid(taskMobile.getTaskid());
							
							telecomShandongSMSDetails.add(telecomShandongSMSDetail);
						}
					}
				}
				webParam.setList(telecomShandongSMSDetails);
				tracer.addTag("parser.telecom.parser.getSMSDetails.htmljson."+i,htmljson);
				tracer.addTag("parser.telecom.parser.getSMSDetails.list."+i,telecomShandongSMSDetails.toString());
			}
			
			return webParam;
		}else{
			tracer.addTag("parser.telecom.parser.getSMSDetails.error"+i,"webClient is null");
			return null;
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
	
	//获取各类详单页面信息
	public static Page getDetailsPage(WebClient webClient, String url, String telNum, String billType, int i) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/json");
		webRequest.setAdditionalHeader("Host", "sd.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/bill/");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody("{\"accNbr\":\""+telNum+"\",\"billingCycle\":\""+getDateBefore("yyyyMM", i)+"\",\"pageRecords\":\"1000\",\"pageNo\":\"1\",\"qtype\":\""+billType+"\",\"totalPage\":\"1\",\"queryType\":\"6\"}");
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i) throws Exception {
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
