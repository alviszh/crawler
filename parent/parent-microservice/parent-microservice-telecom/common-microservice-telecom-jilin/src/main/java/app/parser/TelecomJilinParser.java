package app.parser;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.mobile.BasicUser;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinCallDetails;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinIncrement;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinIntegral;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinMonthBill;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinPayment;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinSMSDetails;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinUserInfo;
import com.microservice.dao.repository.crawler.mobile.BasicUserRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;

@Component
public class TelecomJilinParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private BasicUserRepository basicUserRepository;
	@Autowired
	private LoginAndGetCommon loginAndGetCommon;
	@Autowired
	private TracerLog tracer;
	/*
	 * 身份验证1--验证身份证号，姓名以及验证码，验证是否为机主身份
	 */
	public HtmlPage getCheckOneHtml(MessageLogin messageLogin, TaskMobile taskMobile) {
		tracer.addTag("parser.telecom.parser.check1.taskid",taskMobile.getTaskid());
		BasicUser basicUser = basicUserRepository.findById(messageLogin.getUser_id());
		tracer.addTag("parser.telecom.parser.check1.basicUser",basicUser.toString());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		try {
			HtmlPage htmlPage = getHtmlPage(webClient, "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00710602");
			tracer.addTag("parser.telecom.parser.check1.initMy189home","<xmp>"+htmlPage.asXml()+"</xmp>");
			if(200 == htmlPage.getWebResponse().getStatusCode()){
				WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/bill/toDetailBillFra.action?fastcode=00710602&cityCode=jl"), HttpMethod.GET);
				HtmlPage yanzhengPage1 = webClient.getPage(webRequest);
				tracer.addTag("parser.telecom.parser.check1.check1page","<xmp>"+yanzhengPage1.asXml()+"</xmp>");
				Thread.sleep(3000);
				HtmlTextInput idNum = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='certCode']");
				HtmlTextInput name = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='cust_name']");
				HtmlTextInput validateInput = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='vCode2']");
				HtmlElement submitbt = (HtmlElement)yanzhengPage1.getFirstByXPath("//a[@class='btn-1']");
				HtmlImage validateImage = yanzhengPage1.getFirstByXPath("//img[@id='vImgCode2']");
				
				idNum.setText(basicUser.getIdnum());
				name.setText(basicUser.getName());
				long start=System.currentTimeMillis(); //获取开始时间  
				validateInput.setText(chaoJiYingOcrService.getVerifycode(validateImage, "1006"));
				long end=System.currentTimeMillis(); //获取结束时间  
				tracer.addTag("身份验证1超级鹰用时：",(end-start)+"ms");
				HtmlPage yanzhengPage2 = submitbt.click();
				Thread.sleep(3000);
				tracer.addTag("parser.telecom.parser.check1.check2page","<xmp>"+yanzhengPage2.asXml()+"</xmp>");
				if(null != yanzhengPage2 && 200 == yanzhengPage2.getWebResponse().getStatusCode()){
					String contentAsString = yanzhengPage2.getWebResponse().getContentAsString();
					tracer.addTag("parser.telecom.parser.check1.strcheck2","<xmp>"+contentAsString+"</xmp>");
					if(contentAsString.contains("请用本机发送CXXD至10001获取查询详单的验证码")){
						tracer.addTag("parser.telecom.parser.check1","checked");
						return yanzhengPage2;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	//身份验证2--验证码以及短信随机码验证
	public WebParam getCheckTwoHtml(MessageLogin messageLogin, HtmlPage checked1) throws Exception{
		tracer.addTag("parser.telecom.parser.check2.taskid",messageLogin.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		WebParam webParam = new WebParam();
		Set<Cookie> cookies = checked1.getWebClient().getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		
		if(null != checked1){
			HtmlImage validateImage2 = checked1.getFirstByXPath("//img[@id='vImgCode']");
			
			WebRequest webRequest2 = new WebRequest(new URL("http://jl.189.cn/service/bill/doDetailBillFra.action"), HttpMethod.POST);
			webRequest2.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest2.getRequestParameters().add(new NameValuePair("sRandomCode", messageLogin.getSms_code()));
			long start=System.currentTimeMillis(); //获取开始时间  
			webRequest2.getRequestParameters().add(new NameValuePair("randCode", chaoJiYingOcrService.getVerifycode(validateImage2, "1006")));
			long end=System.currentTimeMillis(); //获取结束时间  
			tracer.addTag("身份验证2超级鹰用时：",(end-start)+"ms");
			Page searchPage = webClient.getPage(webRequest2);
			String strpage = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.telecom.parser.check2.rspPage",strpage);
			if(strpage.contains("true")){
				tracer.addTag("parser.telecom.parser.check2.status","验证通过");
				webParam.setPage(searchPage);
				webParam.setWebClient(webClient);
			}else if(strpage.contains("你输入的短信随机码码错误")){
				tracer.addTag("parser.telecom.parser.check2.status","你输入的短信随机码码错误");
				webParam.setHtml("你输入的短信随机码码错误");
			}else if(strpage.contains("您输入的验证码有误")){
				tracer.addTag("parser.telecom.parser.check2.status","您输入的验证码有误");
				webParam.setHtml("您输入的验证码有误");
			}
			
		}
		
		
		return webParam;
	}
	
	//获取每月的账单
	public WebParam<TelecomJilinMonthBill> getBillData(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getBillData.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		WebParam<TelecomJilinMonthBill> webParam = new WebParam<TelecomJilinMonthBill>();
		
		String url = "http://jl.189.cn/service/bill/queryBillInfoFra.action?billingCycle="+getDateBefore("yyyyMM", i);
		tracer.addTag("parser.telecom.parser.getBillData.url."+i,url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webParam.setUrl(url);
		Page respage = webClient.getPage(webRequest);
		tracer.addTag("parser.telecom.parser.getBillData.respage."+i,respage.getWebResponse().getContentAsString());
		webParam.setCode(respage.getWebResponse().getStatusCode());
		if(200 == respage.getWebResponse().getStatusCode()){
			webParam.setPage(respage);
			String billRec = respage.getWebResponse().getContentAsString();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(billRec); // 创建JsonObject对象
			String billDate = object.get("billingCycle").getAsString();
			JsonArray billItemList = object.get("billItemList").getAsJsonArray();
			if(billItemList.size()>0){
				JsonElement billItem = billItemList.get(0);
				String phoneNum = billItem.getAsJsonObject().get("accNbr").getAsString();
				JsonArray acctItems = billItem.getAsJsonObject().get("acctItems").getAsJsonArray();
				if(acctItems.size()>0){
					List<TelecomJilinMonthBill> bills = new ArrayList<TelecomJilinMonthBill>();
					for (JsonElement acctItem : acctItems) {
						TelecomJilinMonthBill bill = new TelecomJilinMonthBill();
						String billType = acctItem.getAsJsonObject().get("acctItemName").getAsString();
						String fee = acctItem.getAsJsonObject().get("acctItemFee").getAsString();
						
						bill.setPhoneNum(phoneNum);
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

	//获取客户资料中的用户信息
	public WebParam<TelecomJilinUserInfo> getUserInfo(TaskMobile taskMobile) throws Exception{
		tracer.addTag("parser.telecom.parser.getUserInfo.taskid",taskMobile.getTaskid());
		JsonParser parser = new JsonParser();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		WebParam<TelecomJilinUserInfo> webParam = new WebParam<TelecomJilinUserInfo>();
		String url = "http://jl.189.cn/service/manage/modifyUserInfoFra.action?fastcode=00700588&cityCode=jl";
		tracer.addTag("parser.telecom.parser.getUserInfo.url",url);
		webParam.setUrl(url);
		HtmlPage userPage = getHtmlPage(webClient,url);
		List<TelecomJilinUserInfo> telecomJilinUserInfos = new ArrayList<TelecomJilinUserInfo>();
		TelecomJilinUserInfo telecomJilinUserInfo = new TelecomJilinUserInfo();
		if(userPage.getWebResponse().getStatusCode() == 200){
			String strUserPage = userPage.asXml();
			tracer.addTag("parser.telecom.parser.getUserInfo.userPage","<xmp>"+userPage.asXml()+"</xmp>");
			if(strUserPage.contains("证件号码")){
				webParam.setHtmlPage(userPage);
				
				Document doc = Jsoup.parse(strUserPage);
				String name = getNextLabelByKeyword(doc,"客户名称","th");
				String type = getNextLabelByKeyword(doc,"客户类型","th");
				String idType = getNextLabelByKeyword(doc,"证件类型","th");
				String idNum = getNextLabelByKeyword(doc,"证件号码","th");
				String address = doc.select("#custAddress_new").val();
				String contactTel = doc.select("#custPhone_new").val();
				
				telecomJilinUserInfo.setName(name);
				telecomJilinUserInfo.setType(type);
				telecomJilinUserInfo.setIdType(idType);
				telecomJilinUserInfo.setIdNum(idNum);
				telecomJilinUserInfo.setAddress(address);
				telecomJilinUserInfo.setContactTel(contactTel);
			}
		}
		//获取当前套餐名
		String url2 = "http://jl.189.cn/service/bill/cumulationInfoQueryFra.action?requestFlag=asynchronism&shijian=";
		tracer.addTag("parser.telecom.parser.getUserInfo.url2",url2);
		Page htmlPage2 = getPage(webClient,url2);
		
		if(htmlPage2.getWebResponse().getStatusCode() == 200){
			JsonObject object = (JsonObject) parser.parse(htmlPage2.getWebResponse().getContentAsString()); // 创建JsonObject对象
			JsonObject pawUse = object.get("pawUse").getAsJsonObject();
			String planName = pawUse.get("planName").getAsString();
			if(null != planName){
				telecomJilinUserInfo.setPlanName(planName);
			}
		}
		
		//获取当前余额
		String url3 = "http://jl.189.cn/service/bill/balanceQueryFra.action?requestFlag=asynchronism&shijian=";
		tracer.addTag("parser.telecom.parser.getUserInfo.url3",url3);
		Page htmlPage3 = getPage(webClient,url3);
		if(htmlPage3.getWebResponse().getStatusCode() == 200){
			JsonObject object = (JsonObject) parser.parse(htmlPage3.getWebResponse().getContentAsString()); // 创建JsonObject对象
			JsonObject basicAccountBalance = object.get("basicAccountBalance").getAsJsonObject();
			String accountBalance = basicAccountBalance.get("accountBalance").getAsString();
			if(null != accountBalance){
				telecomJilinUserInfo.setAccountBalance(accountBalance);
			}
		}
		
		if(null != telecomJilinUserInfo){
			telecomJilinUserInfo.setTaskid(taskMobile.getTaskid());
			telecomJilinUserInfos.add(telecomJilinUserInfo);
			webParam.setList(telecomJilinUserInfos);
			tracer.addTag("parser.telecom.parser.getUserInfo.list",telecomJilinUserInfos.toString());
		}
		
		return webParam;
	}
	
	/*
	 * @Des 获取积分生成明细
	 * 
	 */
	public WebParam<TelecomJilinIntegral> getIntegral(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getIntegral.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		WebParam<TelecomJilinIntegral> webParam = new WebParam<TelecomJilinIntegral>();
		
		String url = "http://jl.189.cn/service/jf/integralHistorySearchFra.action";
		tracer.addTag("parser.telecom.parser.getIntegral.url."+i,url);
		String strDate = getDateBefore("yyyyMMdd", i);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("pointHistoryIn.year", strDate.substring(0, 4)));
		webRequest.getRequestParameters().add(new NameValuePair("pointHistoryIn.month", strDate.substring(4, 6)));
		webRequest.getRequestParameters().add(new NameValuePair("requestFlag", "asynchronism"));
		
		webParam.setUrl(url);
		Page respage = webClient.getPage(webRequest);
		tracer.addTag("parser.telecom.parser.getIntegral.respage."+strDate,respage.getWebResponse().getContentAsString());
		webParam.setCode(respage.getWebResponse().getStatusCode());
		if(200 == respage.getWebResponse().getStatusCode()){
			webParam.setPage(respage);
			String integral = respage.getWebResponse().getContentAsString();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(integral); // 创建JsonObject对象
			String tip = object.get("tip").getAsString();
			if(tip.contains("成功")){
				JsonObject retInfo = object.get("retInfo").getAsJsonObject();
				JsonArray list = retInfo.get("list").getAsJsonArray();
				if(list.size()>0){
					List<TelecomJilinIntegral> integrals = new ArrayList<TelecomJilinIntegral>();
					for (JsonElement ele : list) {
						TelecomJilinIntegral telecomJilinIntegral = new TelecomJilinIntegral();
						String period = ele.getAsJsonObject().get("period").getAsString();									//积分账期
						String constituteTypeName = ele.getAsJsonObject().get("constituteTypeName").getAsString();			//积分类型
						String addBonus = ele.getAsJsonObject().get("addBonus").getAsString();								//积分值
						
						telecomJilinIntegral.setIntegralDate(period);
						telecomJilinIntegral.setIntegralType(constituteTypeName);
						telecomJilinIntegral.setIntegralCount(addBonus);
						telecomJilinIntegral.setTaskid(taskMobile.getTaskid());
						
						integrals.add(telecomJilinIntegral);
					}
					webParam.setList(integrals);
					tracer.addTag("parser.telecom.parser.getIntegral.list."+strDate,integrals.toString());
				}
			}else{
				webParam.setHtml(tip);
				tracer.addTag("parser.telecom.parser.getIntegral.tip."+strDate,tip);
			}
		}
		return webParam;
	}
	
	//获取充值缴费信息
	public WebParam<TelecomJilinPayment> getPaymentHistory(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getPaymentHistory.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		WebParam<TelecomJilinPayment> webParam = new WebParam<TelecomJilinPayment>();
		
		String url = "http://jl.189.cn/service/bill/queryPaymentRecordFra.action?billingCycle="+getDateBefore("yyyyMM", i)+"&queryWebCard.page=1&time=0.03145141637853177";
		tracer.addTag("parser.telecom.parser.getPaymentHistory.url."+i,url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webParam.setUrl(url);
		Page respage = webClient.getPage(webRequest);
		webParam.setCode(respage.getWebResponse().getStatusCode());
		if(200 == respage.getWebResponse().getStatusCode()){
			webParam.setPage(respage);
			String strpayment = respage.getWebResponse().getContentAsString();
			tracer.addTag("parser.telecom.parser.getPaymentHistory.page."+getDateBefore("yyyyMM", i),strpayment);
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(strpayment); // 创建JsonObject对象
			JsonArray listPaymentHistory = object.get("listPaymentHistory").getAsJsonArray();
			if(listPaymentHistory.size()>0){
				List<TelecomJilinPayment> payments = new ArrayList<TelecomJilinPayment>();
				for (JsonElement ele : listPaymentHistory) {
					TelecomJilinPayment payment = new TelecomJilinPayment();
					String billingID = ele.getAsJsonObject().get("billingID").getAsString();									//流水号
					String paymentDate = ele.getAsJsonObject().get("paymentDate").getAsString();								//缴费时间
					String fee = ele.getAsJsonObject().get("fee").getAsString();												//金额
					String paymentType = ele.getAsJsonObject().get("paymentType").getAsString();								//缴费类型
					
					payment.setSerialNum(billingID);
					payment.setPayDate(paymentDate);
					payment.setPayCount(fee);
					payment.setPayType(paymentType);
					payment.setTaskid(taskMobile.getTaskid());
					
					payments.add(payment);
					
				}
				webParam.setList(payments);
				tracer.addTag("parser.telecom.parser.getPaymentHistory.list."+getDateBefore("yyyyMM", i),payments.toString());
			}
		}
		return webParam;
	}
	
	
	//获取增值详单
	public WebParam<TelecomJilinIncrement> getIncrement(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getIncrement.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		WebParam<TelecomJilinIncrement> webParam = new WebParam<TelecomJilinIncrement>();
		
		String url = "http://jl.189.cn/service/bill/billDetailQueryFra.action";
		tracer.addTag("parser.telecom.parser.getIncrement.url."+i,url);
		webParam.setUrl(url);
		Page detailsPage1 = getDetailsPage(webClient, url, "0", "6", i);
		if(200 == detailsPage1.getWebResponse().getStatusCode()){
			if(detailsPage1.getWebResponse().getContentAsString().indexOf("The page you are looking for is temporarily unavailable") != -1){
				return webParam;
			}
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(detailsPage1.getWebResponse().getContentAsString());		// 创建JsonObject对象
			JsonObject pagingInfo = object.get("pagingInfo").getAsJsonObject();
			int totalPage = pagingInfo.get("totalPage").getAsInt();
			String htmljson = "";
			htmljson += detailsPage1.getWebResponse().getContentAsString();
			List<TelecomJilinIncrement> telecomJilinIncrements = new ArrayList<TelecomJilinIncrement>();
			
			JsonArray items1 = object.get("items").getAsJsonArray();
			if(items1.size() > 0){
				for (JsonElement ele : items1) {
					TelecomJilinIncrement telecomJilinIncrement = new TelecomJilinIncrement();
					JsonObject item = ele.getAsJsonObject();
					String serviceNum = item.get("accNbr").getAsString();					//业务号码
					String beginTime = item.get("beginTime").getAsString();					//起始时间
					String endTime = item.get("endTime").getAsString();						//结束时间
					String type = item.get("type").getAsString();							//业务名称
					String fee = item.get("fee").getAsString();								//费用
					
					telecomJilinIncrement.setServiceNum(serviceNum);
					telecomJilinIncrement.setStartDate(beginTime);
					telecomJilinIncrement.setEndDate(endTime);
					telecomJilinIncrement.setServiceName(type);
					telecomJilinIncrement.setFee(fee);
					telecomJilinIncrement.setTaskid(taskMobile.getTaskid());
					
					telecomJilinIncrements.add(telecomJilinIncrement);
				}
			}
			
			if(totalPage > 1){
				for (int j = 1; j < totalPage; j++) {
					Page detailsPage = getDetailsPage(webClient, url, j+"", "6", i);
					htmljson += "|"+detailsPage.getWebResponse().getContentAsString();
					JsonObject obj = (JsonObject) parser.parse(detailsPage.getWebResponse().getContentAsString()); // 创建JsonObject对象
					JsonArray items = obj.get("items").getAsJsonArray();
					if(items.size() > 1){
						for (JsonElement ele : items) {
							TelecomJilinIncrement telecomJilinIncrement = new TelecomJilinIncrement();
							JsonObject item = ele.getAsJsonObject();
							String serviceNum = item.get("accNbr").getAsString();					//业务号码
							String beginTime = item.get("beginTime").getAsString();					//起始时间
							String endTime = item.get("endTime").getAsString();						//结束时间
							String type = item.get("type").getAsString();							//业务名称
							String fee = item.get("fee").getAsString();								//费用
							
							telecomJilinIncrement.setServiceNum(serviceNum);
							telecomJilinIncrement.setStartDate(beginTime);
							telecomJilinIncrement.setEndDate(endTime);
							telecomJilinIncrement.setServiceName(type);
							telecomJilinIncrement.setFee(fee);
							telecomJilinIncrement.setTaskid(taskMobile.getTaskid());
							
							telecomJilinIncrements.add(telecomJilinIncrement);
						}
					}
				}
			}
			webParam.setHtml(htmljson);
			webParam.setList(telecomJilinIncrements);
			tracer.addTag("parser.telecom.parser.getIncrement.htmljson."+i,htmljson);
			tracer.addTag("parser.telecom.parser.getIncrement.list."+i,telecomJilinIncrements.toString());
		}
		return webParam;
	}
	
	//获取通话详单
	//获取通话详单
	@Retryable(value={RuntimeException.class,},maxAttempts=3,backoff = @Backoff(delay = 1000l,multiplier = 1.2))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public WebParam<TelecomJilinCallDetails> getCallDetails(TaskMobile taskMobile, int i) throws IOException{
		tracer.addTag("parser.telecom.parser.getCallDetails.taskid."+i,taskMobile.getTaskid());
		WebParam<TelecomJilinCallDetails> webParam;
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
			webParam = new WebParam<TelecomJilinCallDetails>();
			
			String url = "http://jl.189.cn/service/bill/billDetailQueryFra.action";
			tracer.addTag("parser.telecom.parser.getCallDetails.url."+i,url);
			webParam.setUrl(url);
			Page detailsPage1 = getDetailsPage(webClient, url, "0", "2", i);
			if(200 == detailsPage1.getWebResponse().getStatusCode()){
				if(detailsPage1.getWebResponse().getContentAsString().indexOf("The page you are looking for is temporarily unavailable") != -1){
					return webParam;
				}
				String jsonPage= detailsPage1.getWebResponse().getContentAsString();
				
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(jsonPage); // 创建JsonObject对象
				JsonObject pagingInfo = object.get("pagingInfo").getAsJsonObject();
				int totalPage = pagingInfo.get("totalPage").getAsInt();
				String htmljson = "";
				htmljson += detailsPage1.getWebResponse().getContentAsString();
				List<TelecomJilinCallDetails> telecomJilinCallDetails = new ArrayList<TelecomJilinCallDetails>();
				
				JsonArray items1 = object.get("items").getAsJsonArray();
				if(items1.size() > 0){
					for (JsonElement ele : items1) {
						TelecomJilinCallDetails telecomJilinCallDetail = new TelecomJilinCallDetails();
						JsonObject item = ele.getAsJsonObject();
						String accNbr = item.get("accNbr").getAsString();				//主叫号码
						String calledAccNbr = item.get("calledAccNbr").getAsString();	//被叫号码
						String callType = item.get("callType").getAsString();			//呼叫类型
						String beginTime = item.get("beginTime").getAsString();			//起始时间
						String duration = item.get("duration").getAsString();			//时长（秒）
						String fee = item.get("fee").getAsString();						//费用（元）
						String netType = item.get("netType").getAsString();				//通话类型
						String visitArea = item.get("visitArea").getAsString();			//通话地
						String billArea = item.get("billArea").getAsString();			//归属地
						String callingArea = item.get("callingArea").getAsString();			//对端归属地
						
						
						telecomJilinCallDetail.setCallingNum(accNbr);
						telecomJilinCallDetail.setCalledNum(calledAccNbr);
						telecomJilinCallDetail.setCallType(callType);
						telecomJilinCallDetail.setStartDate(beginTime);
						telecomJilinCallDetail.setCallTime(duration);
						telecomJilinCallDetail.setCallFee(fee);
						telecomJilinCallDetail.setType(netType);
						telecomJilinCallDetail.setCallPlace(visitArea);
						telecomJilinCallDetail.setAttribution(billArea);
						telecomJilinCallDetail.setOtherAttribution(callingArea);
						telecomJilinCallDetail.setTaskid(taskMobile.getTaskid());
						
						telecomJilinCallDetails.add(telecomJilinCallDetail);
					}
				}
				
				if(totalPage > 1){
					for (int j = 1; j < totalPage; j++) {
						Page detailsPage = getDetailsPage(webClient, url, j+"", "2", i);
						htmljson += "|"+detailsPage.getWebResponse().getContentAsString();
						JsonObject obj = (JsonObject) parser.parse(detailsPage.getWebResponse().getContentAsString()); // 创建JsonObject对象
						JsonArray items = obj.get("items").getAsJsonArray();
						if(items.size() > 1){
							for (JsonElement ele : items) {
								TelecomJilinCallDetails telecomJilinCallDetail = new TelecomJilinCallDetails();
								JsonObject item = ele.getAsJsonObject();
								String accNbr = item.get("accNbr").getAsString();				//本机号码
								String calledAccNbr = item.get("calledAccNbr").getAsString();	//对方号码
								String callType = item.get("callType").getAsString();			//呼叫类型
								String beginTime = item.get("beginTime").getAsString();			//起始时间
								String duration = item.get("duration").getAsString();			//时长（秒）
								String fee = item.get("fee").getAsString();						//费用（元）
								String netType = item.get("netType").getAsString();				//通话类型
								String visitArea = item.get("visitArea").getAsString();			//通话地
								String billArea = item.get("billArea").getAsString();			//归属地
								String callingArea = item.get("callingArea").getAsString();			//对端归属地
								
								
								telecomJilinCallDetail.setCallingNum(accNbr);
								telecomJilinCallDetail.setCalledNum(calledAccNbr);
								telecomJilinCallDetail.setCallType(callType);
								telecomJilinCallDetail.setStartDate(beginTime);
								telecomJilinCallDetail.setCallTime(duration);
								telecomJilinCallDetail.setCallFee(fee);
								telecomJilinCallDetail.setType(netType);
								telecomJilinCallDetail.setCallPlace(visitArea);
								telecomJilinCallDetail.setAttribution(billArea);
								telecomJilinCallDetail.setOtherAttribution(callingArea);
								telecomJilinCallDetail.setTaskid(taskMobile.getTaskid());
								
								telecomJilinCallDetails.add(telecomJilinCallDetail);
							}
						}
					}
				}
				webParam.setHtml(htmljson);
				webParam.setList(telecomJilinCallDetails);
				tracer.addTag("parser.telecom.parser.getCallDetails.htmljson."+i,htmljson);
				tracer.addTag("parser.telecom.parser.getCallDetails.list."+i,telecomJilinCallDetails.toString());
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
	public WebParam<TelecomJilinSMSDetails> getSMSDetails(TaskMobile taskMobile, int i) throws Exception{
		tracer.addTag("parser.telecom.parser.getSMSDetails.taskid."+i,taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);
		WebParam<TelecomJilinSMSDetails> webParam = new WebParam<TelecomJilinSMSDetails>();
		
		String url = "http://jl.189.cn/service/bill/billDetailQueryFra.action";
		tracer.addTag("parser.telecom.parser.getSMSDetails.url."+i,url);
		webParam.setUrl(url);
		Page detailsPage1 = getDetailsPage(webClient, url, "0", "5", i);
		if(200 == detailsPage1.getWebResponse().getStatusCode()){
			if(detailsPage1.getWebResponse().getContentAsString().indexOf("The page you are looking for is temporarily unavailable") != -1){
				return webParam;
			}
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(detailsPage1.getWebResponse().getContentAsString()); // 创建JsonObject对象
			JsonObject pagingInfo = object.get("pagingInfo").getAsJsonObject();
			int totalPage = pagingInfo.get("totalPage").getAsInt();
			String htmljson = "";
			htmljson += detailsPage1.getWebResponse().getContentAsString();
			List<TelecomJilinSMSDetails> telecomJilinSMSDetails = new ArrayList<TelecomJilinSMSDetails>();
			
			JsonArray items1 = object.get("items").getAsJsonArray();
			if(items1.size() > 0){
				for (JsonElement ele : items1) {
					TelecomJilinSMSDetails telecomJilinSMSDetail = new TelecomJilinSMSDetails();
					JsonObject item = ele.getAsJsonObject();
					String accnum = item.get("accNbr").getAsString();				//发送号码
					String calledAccNbr = item.get("calledAccNbr").getAsString();	//接受号码
					String beginTime = item.get("beginTime").getAsString();			//发送时间
					String fee = item.get("fee").getAsString();						//费用
					
					telecomJilinSMSDetail.setSendNum(accnum);
					telecomJilinSMSDetail.setReceiveNum(calledAccNbr);
					telecomJilinSMSDetail.setSendDate(beginTime);
					telecomJilinSMSDetail.setFee(fee);
					telecomJilinSMSDetail.setTaskid(taskMobile.getTaskid());
					
					telecomJilinSMSDetails.add(telecomJilinSMSDetail);
				}
			}
			
			if(totalPage > 1){
				for (int j = 1; j < totalPage; j++) {
					Page detailsPage = getDetailsPage(webClient, url, j+"", "5", i);
					htmljson += "|"+detailsPage.getWebResponse().getContentAsString();
					JsonObject obj = (JsonObject) parser.parse(detailsPage.getWebResponse().getContentAsString()); // 创建JsonObject对象
					JsonArray items = obj.get("items").getAsJsonArray();
					if(items.size() > 1){
						for (JsonElement ele : items) {
							TelecomJilinSMSDetails telecomJilinSMSDetail = new TelecomJilinSMSDetails();
							JsonObject item = ele.getAsJsonObject();
							String accnum = item.get("accNbr").getAsString();				//发送号码
							String calledAccNbr = item.get("calledAccNbr").getAsString();	//接受号码
							String beginTime = item.get("beginTime").getAsString();			//发送时间
							String fee = item.get("fee").getAsString();						//费用
							
							telecomJilinSMSDetail.setSendNum(accnum);
							telecomJilinSMSDetail.setReceiveNum(calledAccNbr);
							telecomJilinSMSDetail.setSendDate(beginTime);
							telecomJilinSMSDetail.setFee(fee);
							telecomJilinSMSDetail.setTaskid(taskMobile.getTaskid());
							
							telecomJilinSMSDetails.add(telecomJilinSMSDetail);
						}
					}
				}
			}
			webParam.setHtml(htmljson);
			webParam.setList(telecomJilinSMSDetails);
			tracer.addTag("parser.telecom.parser.getSMSDetails.htmljson."+i,htmljson);
			tracer.addTag("parser.telecom.parser.getSMSDetails.list."+i,telecomJilinSMSDetails.toString());
		}
		
		return webParam;
	}
	
	
	//根据URL获取HtmlPage
	public static HtmlPage getHtmlPage(WebClient webClient, String url) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	//根据URL获取HtmlPage
	public static Page getPage(WebClient webClient, String url) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		return page;
	}
	
	//获取各类详单页面信息
	public static Page getDetailsPage(WebClient webClient, String url, String pagenum, String billType, int i) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Host", "jl.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://jl.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://jl.189.cn/service/bill/toDetailBillFra.action?cityCode=jl&fastcode=00710602");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("billDetailValidate", "true"));
		webRequest.getRequestParameters().add(new NameValuePair("billDetailType", billType));
		webRequest.getRequestParameters().add(new NameValuePair("startTime", getDateBefore("yyyy-MM", i)+"-01"));
		webRequest.getRequestParameters().add(new NameValuePair("endTime", getDateBefore("yyyy-MM-dd", i)));
		webRequest.getRequestParameters().add(new NameValuePair("pagingInfo.currentPage", pagenum));
		webRequest.getRequestParameters().add(new NameValuePair("contactID", ""));
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
        if(fmt.contains("yyyy-MM-dd")){
        	c.add(Calendar.MONTH, -i+1);
        	c.set(Calendar.DAY_OF_MONTH, 0);
        }else{
        	c.add(Calendar.MONTH, -i);
        }
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
