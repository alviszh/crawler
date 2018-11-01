package app.service;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.spdbc.SpdbCreditCardBillDetailNew;
import com.microservice.dao.entity.crawler.bank.spdbc.SpdbCreditCardBillGeneralNew;
import com.microservice.dao.entity.crawler.bank.spdbc.SpdbCreditCardGeneralInfoNew;
import com.microservice.dao.entity.crawler.bank.spdbc.SpdbCreditCardInstallmentNew;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.spdbc.SpdbCreditCardBillDetaiNewlRepository;
import com.microservice.dao.repository.crawler.bank.spdbc.SpdbCreditCardBillGeneralNewRepository;
import com.microservice.dao.repository.crawler.bank.spdbc.SpdbCreditCardGeneralInfoNewRepository;
import com.microservice.dao.repository.crawler.bank.spdbc.SpdbCreditCardInstallmentNewRepository;
import com.module.htmlunit.WebCrawler;

import app.common.CommonUnitForSpdb;
import app.commontracerlog.TracerLog;
import app.parser.SpdbChinaCreditParser;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.spdbc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.spdbc"})
public class SpdbChinaCreditService implements ICrawlerLogin, ISms{
	@Autowired
	private SpdbChinaserviceLogin SpdbChinaserviceLogin;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CommonUnitForSpdb commonUnitForSpdb;
	@Autowired
	private SpdbCreditCardBillDetaiNewlRepository spdbCreditCardBillDetaiNewlRepository;
	@Autowired
	private SpdbCreditCardBillGeneralNewRepository spdbCreditCardBillGeneralNewRepository;
	@Autowired
	private SpdbCreditCardGeneralInfoNewRepository spdbCreditCardGeneralInfoNewRepository;
	@Autowired
	private SpdbCreditCardInstallmentNewRepository spdbCreditCardInstallmentNewRepository;
	@Autowired
	private SpdbChinaCreditParser spdbChinaCreditParser;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Override
	public TaskBank login(BankJsonBean bankJsonBean){
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(), 
				BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
				"需进行短信验证码",
				BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(),false, bankJsonBean.getTaskid());
//		String idTpyeValue =type(bankJsonBean);
//		String loginType = bankJsonBean.getLoginType();
//		tracerLog.addTag("开始选择登录类型", loginType);
//		TaskBank taskBank= null;
//		try {
//			
//			SpdbChinaserviceLogin.login(bankJsonBean,idTpyeValue);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			taskBank=taskBankStatusService.changeStatus("LOGIN", 
//					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
//					BankStatusCode.BANK_AGENT_ERROR.getDescription(),
//					BankStatusCode.BANK_AGENT_ERROR.getError_code(),false, bankJsonBean.getTaskid());
//		}
		return taskBank;
	}
	
	@Override
	public TaskBank sendSms(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		String idTpyeValue =type(bankJsonBean);
		String loginType = bankJsonBean.getLoginType();
		tracerLog.addTag("开始选择登录类型", loginType);
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			System.out.println("idTpyeValue"+idTpyeValue);
			SpdbChinaserviceLogin.login(bankJsonBean,idTpyeValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBank=taskBankStatusService.changeStatus("LOGIN", 
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(),false, bankJsonBean.getTaskid());
		}
		return taskBank;
	}
	
	@Override
	public TaskBank verifySms(BankJsonBean bankJsonBean){
		String idTpyeValue =type(bankJsonBean);
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskBank.getCookies());
			for (Cookie cookie : cookies1) {
				webClient.getCookieManager().addCookie(cookie);
			}
			String url = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/pictureCode.action"; 
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "*/*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest.setAdditionalHeader("Host", "ebill.spdbccc.com.cn");
			webRequest.setAdditionalHeader("Origin", "https://ebill.spdbccc.com.cn");
			webRequest.setAdditionalHeader("Referer", "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/toLogin.action");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			String requestBody = "messagePwd="+bankJsonBean.getVerification()+"&idNo="+bankJsonBean.getLoginName()+"&idType="+idTpyeValue+"&code="+taskBank.getParam()+"&ptcId=2";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);
			
			System.out.println(taskBank.getCookies());
			System.out.println(taskBank.getParam());
			String urlpwd = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/queryAccountListByIdNo.action";
			WebRequest webRequest1 = new WebRequest(new URL(urlpwd), HttpMethod.POST);
			webRequest1.setAdditionalHeader("Accept", "*/*");
			webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest1.setAdditionalHeader("Connection", "keep-alive");
			webRequest1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest1.setAdditionalHeader("Host", "ebill.spdbccc.com.cn");
			webRequest1.setAdditionalHeader("Origin", "https://ebill.spdbccc.com.cn");
			webRequest1.setAdditionalHeader("Referer", "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/toLogin.action");
			webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			webRequest1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			String requestBody1 = "messagePwd="+bankJsonBean.getVerification()+"&idNo="+bankJsonBean.getLoginName()+"&idType="+idTpyeValue+"&yzm="+taskBank.getParam()+"";
			webRequest1.setRequestBody(requestBody1);
			Page page1 = webClient.getPage(webRequest1);
			String html1 = page1.getWebResponse().getContentAsString();
			System.out.println("html1"+html1);
			System.out.println("bankJsonBean.getVerification():"+bankJsonBean.getVerification());
			if(html1.contains("cardNbr")){
				tracerLog.addTag("短信验证码验证成功", html1);
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(), 
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(), 
						BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(),false, bankJsonBean.getTaskid());
				System.out.println("短信验证码验证成功");
//				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
//						BankStatusCode.BANK_CRAWLER_DOING.getError_code(), false, bankJsonBean.getTaskid());
				String cookieString = CommonUnit.transcookieToJson(webClient);
				System.out.println("cookieString"+cookieString);
				taskBank.setCookies(cookieString);
				taskBank.setParam(html1);
				taskBankRepository.save(taskBank);
				
			}else if(html1.contains("3")){
				tracerLog.addTag("动态密码输入错误", html1);
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
						"动态密码输入错误", 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(),false, bankJsonBean.getTaskid());
				System.out.println("动态密码输入错误");
			}else if(html1.contains("4")){
				tracerLog.addTag("证件号码输入错误", html1);
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
						"证件号码输入错误", 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(),false, bankJsonBean.getTaskid());
				System.out.println("证件号码输入错误");
			}else{
				tracerLog.addTag("短信验证码验证失败", html1);
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription(), 
						BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(),false, bankJsonBean.getTaskid());
				System.out.println("短信验证码验证失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBank=taskBankStatusService.changeStatus("LOGIN", 
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(),false, bankJsonBean.getTaskid());
		}
		return taskBank;
	}
	
	

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskBank.getCookies());
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String html1 = taskBank.getParam();
			System.out.println(html1);
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html1); // 创建JsonObject对象
			JsonArray accountCardList = object.get("accountInfoList").getAsJsonArray();
			String type = null;
			String cardNbr = null;
			String maFlag = null;
			int date = 0;
			Calendar cal=Calendar.getInstance(); 
			int d=cal.get(Calendar.DATE);    
			if (accountCardList.size()>0){
				for (JsonElement acc : accountCardList) {
					JsonObject account = acc.getAsJsonObject();
					type = account.get("account").toString().replaceAll("\"", "");  
					cardNbr = account.get("cardNbr").toString().replaceAll("\"", "");  
					maFlag = account.get("maFlag").toString().replaceAll("\"", "");  
					System.out.println(type);
					tracerLog.addTag("个人账号", type);
				}
			}
			int a = 0;
			if(date>d){
				a=1;
			}
			String name = null;
			String credit = null;
			String creditLimit = null;
			String essayLimit = null;
			int index = 0;
			for(int i = a;i<=18;i++){
				String month=commonUnitForSpdb.getBeforeMonth(i);
				System.out.println(month);
				tracerLog.addTag("爬取账单最后的年月", month);
				String urlsss = "https://ebill.spdbccc.com.cn/cloudbank-portal/myBillController/changeAccountOrBillData.action";
				WebRequest webRequest2 = new WebRequest(new URL(urlsss), HttpMethod.POST);
				webRequest2.setAdditionalHeader("Accept", "*/*");
				webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
				webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
				webRequest2.setAdditionalHeader("Connection", "keep-alive");
				webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				webRequest2.setAdditionalHeader("Host", "ebill.spdbccc.com.cn");
				webRequest2.setAdditionalHeader("Origin", "https://ebill.spdbccc.com.cn");
				webRequest2.setAdditionalHeader("Referer", "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/loginIndex.action");
				webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
				webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				String requestBody2 = "bilM="+month+"&accountId="+type+"";
				webRequest2.setRequestBody(requestBody2);
				Page page2 = webClient.getPage(webRequest2);
				String html2 = page2.getWebResponse().getContentAsString();
				System.out.println("html2"+html2);
				
				String urlser = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/loginIndex.action?accountId="+type+"&billYM="+month+"";
				WebRequest webRequest3 = new WebRequest(new URL(urlser), HttpMethod.GET);
				HtmlPage html3 = webClient.getPage(webRequest3);
				
				String urluser = "https://ebill.spdbccc.com.cn/cloudbank-portal/myBillController/loadHomeData.action";
				WebRequest webRequest4 = new WebRequest(new URL(urluser), HttpMethod.POST);
				webRequest4.setAdditionalHeader("Accept", "*/*");
				webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
				webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
				webRequest4.setAdditionalHeader("Connection", "keep-alive");
				webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				webRequest4.setAdditionalHeader("Host", "ebill.spdbccc.com.cn");
				webRequest4.setAdditionalHeader("Origin", "https://ebill.spdbccc.com.cn");
				webRequest4.setAdditionalHeader("Referer", "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/loginIndex.action?accountId="+type+"&billYM="+month+"");
				webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
				webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				Page page4 = webClient.getPage(webRequest4);
				String htm4 = page4.getWebResponse().getContentAsString();
				System.out.println("htm4"+htm4);
				tracerLog.addTag("获取个人信息json"+month, htm4);
//				String h1 = htm4.substring(0,htm4.indexOf("htClauseText")-2);
//				String h2 = htm4.substring(htm4.indexOf("<br/></p>")+10);
//				htm4 = h1+h2;
//				System.out.println(htm4);
//				tracerLog.addTag("获取个人信息截取后json"+month, htm4);
				JsonObject object1 = (JsonObject) parser.parse(htm4); // 创建JsonObject对象
				essayLimit = object1.get("cashLimit").toString().replaceAll("\"", "");  
				credit = object1.get("creditLimit").toString().replaceAll("\"", "");  
				creditLimit = object1.get("creditLimitD").toString().replaceAll("\"", ""); 
				name = object1.get("userName").toString().replaceAll("\"", ""); 
				String lastBackDate = object1.get("lastBackDate").toString().replaceAll("\"", "");
				String accountDay = object1.get("accountDay").toString().replaceAll("\"", "");
				accountDay = month+accountDay;
				System.out.println(type);
				String urls = "https://ebill.spdbccc.com.cn/cloudbank-portal/billDetailController/PCbillDetail.action";
				WebRequest webRequest5 = new WebRequest(new URL(urls), HttpMethod.GET);
				HtmlPage html5 = webClient.getPage(webRequest5);
				
//				String urlss = "https://ebill.spdbccc.com.cn/cloudbank-portal/billDetailController/PCloadBillsDetailAll.action";
//				WebRequest webRequest6 = new WebRequest(new URL(urlss), HttpMethod.POST);
//				webRequest6.setAdditionalHeader("Accept", "*/*");
//				webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
//				webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//				webRequest6.setAdditionalHeader("Connection", "keep-alive");
//				webRequest6.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//				webRequest6.setAdditionalHeader("Host", "ebill.spdbccc.com.cn");
//				webRequest6.setAdditionalHeader("Origin", "https://ebill.spdbccc.com.cn");
//				webRequest6.setAdditionalHeader("Referer", "https://ebill.spdbccc.com.cn/cloudbank-portal/billDetailController/PCbillDetail.action");
//				webRequest6.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//				webRequest6.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//				Page page6 = webClient.getPage(webRequest6);
//				String html6 = page6.getWebResponse().getContentAsString();
//				System.out.println("html6"+html6);
				
				//下载pdf
				String pdfurl = "https://ebill.spdbccc.com.cn/cloudbank-portal/downloadPdfController/downloadPdf.action";
				WebRequest webRequest7 = new WebRequest(new URL(pdfurl), HttpMethod.GET);
//				webClient.getOptions().setJavaScriptEnabled(false);
				Page html7 = webClient.getPage(webRequest7);
				String path = commonUnitForSpdb.getPathBySystem();
				String pdfPath=commonUnitForSpdb.getPdfPath(html7,path,month);
				System.out.println("imgagePath"+pdfPath);
				String readFdf4 = commonUnitForSpdb.readFdf(pdfPath);
				File file5 = new File(readFdf4);
				String readTxt = commonUnitForSpdb.readTxtFile(file5);
//				String readTxt1 = readTxt.substring(readTxt.indexOf("Amount")+6);
//				readTxt1 = readTxt1.substring(0,readTxt1.indexOf("本期积分情况"));
//				System.out.println("读取出来的文件内容是11111："+"\r\n"+readTxt);
				tracerLog.addTag("通过PDF得到的字符串"+month, readTxt);
				if(readTxt.contains("未查出账务明细")){
					index=index+1;
					continue;
				}
				if(index>2){
					break;
				}
				
				String urlss = "https://ebill.spdbccc.com.cn/cloudbank-portal/myIntegralController/getAccountIntegral.action";
				WebRequest webRequest8 = new WebRequest(new URL(urlss), HttpMethod.GET);
				Page page8 = webClient.getPage(webRequest8);
				String html8 = page8.getWebResponse().getContentAsString();
				System.out.print("html8"+html8);
				SpdbCreditCardBillGeneralNew billGeneralNew = spdbChinaCreditParser.billGeneral(accountDay,lastBackDate,readTxt,month,creditLimit,essayLimit,html8);
				if(billGeneralNew!=null||!billGeneralNew.equals(" ")){
					spdbCreditCardBillGeneralNewRepository.save(billGeneralNew);
				}
				
				
				List<SpdbCreditCardBillDetailNew> list = spdbChinaCreditParser.billDetail(readTxt);
				for(SpdbCreditCardBillDetailNew billDetailNew:list){
					if(billDetailNew!=null||!billDetailNew.equals(" ")){
						spdbCreditCardBillDetaiNewlRepository.save(billDetailNew);
					}
					
				}
				
				List<SpdbCreditCardInstallmentNew> list1 = spdbChinaCreditParser.installment(readTxt);
				for(SpdbCreditCardInstallmentNew installmentNew:list1){
					if(installmentNew!=null||!installmentNew.equals(" ")){
						spdbCreditCardInstallmentNewRepository.save(installmentNew);
					}
					
				}
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getPhase(), 
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getPhasestatus(), 
						month+BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), 
						BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
			}
			SpdbCreditCardGeneralInfoNew info = new SpdbCreditCardGeneralInfoNew();
			info.setName(name);
			info.setAccount(type);
			info.setCardNbr(cardNbr);
			info.setMaFlag(maFlag);
			info.setCredit(credit);
			info.setCreditLimit(creditLimit);
			info.setEssayLimit(essayLimit);
			spdbCreditCardGeneralInfoNewRepository.save(info);
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_USERINFO_SUCCESS.getPhase(), 
					BankStatusCode.BANK_USERINFO_SUCCESS.getPhasestatus(), 
					BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), 
					BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
			taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid());
		 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBank=taskBankStatusService.changeStatus("LOGIN", 
					BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_AGENT_ERROR.getDescription(),
					BankStatusCode.BANK_AGENT_ERROR.getError_code(),false, bankJsonBean.getTaskid());
		}
		return taskBank;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String type(BankJsonBean bankJsonBean){
		String loginType = bankJsonBean.getLoginType();
		 String idTpyeValue = "";
			if (loginType != null) {
	           
	            if (loginType.equals("ID_NUM")) { //15/18位身份证
	            	if(bankJsonBean.getLoginName().trim().length()==18){
	            		tracerLog.addTag("loginType","18位身份证");
	            		System.out.println("18位身份证");
	            		idTpyeValue = "01";
	            	}else if(bankJsonBean.getLoginName().trim().length()==15){
	            		tracerLog.addTag("loginType","15位身份证");
	            		System.out.println("15位身份证");
	            		idTpyeValue = "02";
	            	}else{
	            		System.out.println("证件号错误");
	            		tracerLog.addTag("longError","证件号错误");
	                    taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(),
	                            BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
	                            "短信验证码发送失败,您输入的证件号有误!",
	                            BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
	            	}
	                
	            }else if (loginType.equals(StatusCodeLogin.OFFICER_CARD)) {//军官证/警官证
	            	tracerLog.addTag("loginType","军官证/警官证");
	            	System.out.println("军官证/警官证");
	                idTpyeValue = "03";
	            }else if (loginType.equals(StatusCodeLogin.PASSPORT)) { //护照
	            	tracerLog.addTag("loginType","护照");
	            	System.out.println("护照");
	                idTpyeValue = "09";
	            }else if (loginType.equals(StatusCodeLogin.CREDIT_CARD)) { //其他证件
	            	tracerLog.addTag("loginType","其他证件");
	            	System.out.println("其他证件");
	                idTpyeValue = "06";
	            }
//	            else if (loginType.equals(StatusCodeLogin.CO_BRANDED_CARD)) { //台湾居民通行证号
//	                idTpyeValue = "04";
//	            }else if (loginType.equals(StatusCodeLogin.CARD_NUM)) {//港澳居民通行证
//	                idTpyeValue = "05";
//	            }else if (loginType.equals(StatusCodeLogin.PHONE_NUM)) {//香港居民通行证
//	                idTpyeValue = "07";
//	            }else if (loginType.equals(StatusCodeLogin.CITIZEN_EMAIL)) { //澳门居民通行证
//	                idTpyeValue = "08";
//	            }else {//其他证件
//	                idTpyeValue = "06";
//	            }
	        }
		return idTpyeValue;
		
	}

	
}
