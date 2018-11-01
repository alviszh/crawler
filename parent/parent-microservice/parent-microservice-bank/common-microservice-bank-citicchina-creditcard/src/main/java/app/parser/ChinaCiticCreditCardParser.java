package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardAccount;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardBill;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONObject;
@Component
public class ChinaCiticCreditCardParser {
	@Autowired
	TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	    

		//去掉引号
		public String outString(String string)throws Exception{
			String string2 = string.replaceAll("\"\"", "");
			String string3 = string2.replaceAll("\"", "");
			return string3;
		}
		
		
		
		/**
		 * @Des 获取目标标签的下一个兄弟标签的内容2
		 * @param document
		 * @param keyword
		 * @return
		 */
		public String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
			Elements es = element.select(tag + ":contains(" + keyword + ")");
			if (null != es && es.size() > 0) {
				Element element1 = es.first();
				Element nextElement = element1.nextElementSibling();
				if (null != nextElement) {
					return nextElement.text();
				}
			}
			return null;
		}

		// 获取当前时间
		public String getFirstDay(String fmt, int i) throws Exception {
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -i);
			Date m = c.getTime();
			String mon = format.format(m);
			return mon;
		}

		public WebClient addcookie(WebClient webclient, TaskBank taskBank) {
			Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
			}.getType();
			Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskBank.getCookies());
			Iterator<Cookie> i = cookies.iterator();
			while (i.hasNext()) {
				webclient.getCookieManager().addCookie(i.next());
			}
			return webclient;
		}




		//登陆信用卡htmlunit
		public WebParam loginCreditCardHtmlunit(BankJsonBean bankJsonBean, TaskBank taskBank,String fileSavePath) throws Exception {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// 图片请求
			String loginurl3 = "https://creditcard.ecitic.com/citiccard/ucweb/newvalicode.do";
			WebRequest webRequest = new WebRequest(new URL(loginurl3), HttpMethod.GET);
			Page html = webClient.getPage(webRequest);
			
			String imgagePath=getImagePath(html,fileSavePath);
			String verifycode = null;
			
			verifycode = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1004"); 
			
			String regEx="^[a-zA-Z0-9]+$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(verifycode);
			boolean rs = matcher.find();
			String a=null;
			if(rs==true){
				a=verifycode;
			}
			else
			{
				verifycode = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1006"); 
				a=verifycode;
			}
//			getImagePath(html, "D:\\img");
//			// 验证登录信息的链接：
//			String code = JOptionPane.showInputDialog("请输入验证码……");
			String loginUrl0 = "https://creditcard.ecitic.com/citiccard/ucweb/login.do";
			
			//密码加密
			String str=DigestUtils.md5Hex(bankJsonBean.getPassword()); 
			
			String body = "{\"loginType\":\"01\"," + "\"memCode\":\""+str+"\","
					+ "\"isBord\":\"false\",\"phone\":\""+bankJsonBean.getLoginName()+"\","
					+ "\"source\":\"PC\",\"page\":\"new\",\"valiCode\":\"" +a.trim()+ "\"}";
			webRequest = new WebRequest(new URL(loginUrl0), HttpMethod.POST);
			webRequest.setAdditionalHeader("Content-Type", "application/json");
			webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
			webRequest.setAdditionalHeader("Origin", "https://creditcard.ecitic.com");
			webRequest.setAdditionalHeader("Referer", "https://creditcard.ecitic.com/citiccard/ucweb/entry.do");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36");
			webRequest.setRequestBody(body);
			Page page0 = webClient.getPage(webRequest);
			String contentAsString0 = page0.getWebResponse().getContentAsString();
			System.out.println("验证登陆是否成功的响应：" + contentAsString0);
			WebParam webParam = new WebParam();
			webParam.setHtml(contentAsString0);
			if (contentAsString0.contains("验证成功")) {
				String url = "https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do";
				webClient.getOptions().setJavaScriptEnabled(false);
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				html = webClient.getPage(webRequest);
				String contentAsString = html.getWebResponse().getContentAsString();
				System.out.println("发送验证码前提页面：" + contentAsString);

				if (contentAsString.contains("免费获取")) {
					url = "https://creditcard.ecitic.com/citiccard/ucweb/sendSms.do";
					webClient.getOptions().setJavaScriptEnabled(false);
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
					webRequest.setAdditionalHeader("Origin", "https://creditcard.ecitic.com");
					webRequest.setAdditionalHeader("Referer",
							"https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do");
					webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
					webRequest.setAdditionalHeader("Content-Type", "multipart/form-data");
					html = webClient.getPage(webRequest);
					contentAsString = html.getWebResponse().getContentAsString();
					System.out.println("发送验证码：" + contentAsString);
					JSONObject fromObject = JSONObject.fromObject(contentAsString);
					String string = fromObject.getString("phone");
					webParam.setHtml(contentAsString);
					webParam.setWebClient(webClient);
					webParam.setWebHandle(string);
				}
			}
			return webParam;
		}




		//输入验证码htmlunit
		public WebParam creditcardSaveCodeHtmlunit(TaskBank taskBank, BankJsonBean bankJsonBean) throws Exception {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebParam webParam = new WebParam();
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskBank.getCookies());
			for (Cookie cookie : cookies1) {
				webClient.getCookieManager().addCookie(cookie);
			}
			String url = "https://creditcard.ecitic.com/citiccard/ucweb/checkSms.do?date=1527502348409";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
			webRequest.setAdditionalHeader("Origin", "https://creditcard.ecitic.com");
			webRequest.setAdditionalHeader("Referer",
					"https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setAdditionalHeader("Content-Type", "application/json");
			String requestBody = "{\"smsCode\":\"" + bankJsonBean.getVerification() + "\"}";
			webRequest.setRequestBody(requestBody);
			Page html = webClient.getPage(webRequest);
			webParam.setHtml(html.getWebResponse().getContentAsString());
			String contentAsString = html.getWebResponse().getContentAsString();
			System.out.println("验证验证码：" + contentAsString);
			
			url="https://creditcard.ecitic.com/citiccard/newonline/myaccount.do?func=mainpage";
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Host", "creditcard.ecitic.com");
			webRequest.setAdditionalHeader("Referer",
					"https://creditcard.ecitic.com/citiccard/ucweb/sendSmsInit.do");
			
			Page page = webClient.getPage(webRequest);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			System.out.println(page.getWebResponse().getContentAsString());
			if(page.getWebResponse().getContentAsString().contains("首页-我的账户")){
				webParam.setUrl(url);
				webParam.setWebClient(webClient);
			}
			return webParam;
		}




		//个人信息htmlunit
		public WebParam<CiticChinaCreditCardUserInfo> creditCardUserInfoCawlerHtmlunit(TaskBank taskBank, BankJsonBean bankJsonBean) throws Exception {
			String url="https://creditcard.ecitic.com/citiccard/newonline/settingManage.do?func=queryUserInfo&";
			String url1="https://creditcard.ecitic.com/citiccard/newonline/installments.do?func=checkDreamGoldRight";
			String url2="https://creditcard.ecitic.com/citiccard/newonline/common.do?func=querySignCards";
			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskBank.getCookies());
			for (Cookie cookie : cookies1) {
				webClient.getCookieManager().addCookie(cookie);
			}
			
			Page page2 = webClient.getPage(url);
			System.out.println(page2.getWebResponse().getContentAsString());
			Page page1 = webClient.getPage(url1);
			Page page = webClient.getPage(url2);
			String contentAsString = page.getWebResponse().getContentAsString();
			WebParam<CiticChinaCreditCardUserInfo> webParam = new WebParam<CiticChinaCreditCardUserInfo>();
			if(page2.getWebResponse().getContentAsString().contains("用户个人信息查询 成功")&&contentAsString.contains("card_nbr"))
			{
				System.out.println("44444444444444444444444444");
				int credit_avail_1Num = contentAsString.indexOf("credit_avail_1");
				int credit_avail_2Num = contentAsString.indexOf("credit_avail_2");
				String credit_avail_1 = contentAsString.substring(credit_avail_1Num, credit_avail_2Num-1).substring(16);
				int card_nbr_noNum = contentAsString.indexOf("card_nbr_no");
				int card_typeNum = contentAsString.indexOf("card_type");
				String card_nbr_no = contentAsString.substring(card_nbr_noNum, card_typeNum-1).substring(25,29);

				Document doc = Jsoup.parse(page2.getWebResponse().getContentAsString());
					Element element = doc.getElementsByTag("resinfo").get(0);
					String string = element.toString();
					//System.out.println(string);
					int address = string.indexOf("address");
					int city = string.indexOf("city");
					int companyphone = string.indexOf("companyphone");
					int email = string.indexOf("email");
					int emergentphone = string.indexOf("emergentphone");
					int province = string.indexOf("province");
					int zipcode = string.indexOf("zipcode");
					String address1 = string.substring(address, city).substring(8);
//					String city1 = string.substring(city,companyphone).substring(5);
					String companyphone1 = string.substring(companyphone,email).substring(13);
					String email1 = string.substring(email,emergentphone).substring(6);
					String emergentphone1 = string.substring(emergentphone,province).substring(14);
//					String province1 = string.substring(province,zipcode).substring(9);
					String zipcode1 = string.substring(zipcode).substring(8).replace("/>", "");
//					System.out.println(address1);
//					System.out.println(city1);
//					System.out.println(companyphone1);
//					System.out.println(email1);
//					System.out.println(emergentphone1);
//					System.out.println(province1);
//					System.out.println(zipcode1);
					CiticChinaCreditCardUserInfo c = new CiticChinaCreditCardUserInfo();
					c.setAvailable(credit_avail_1.replace("\"", ""));
					c.setName(taskBank.getParam());
					c.setAddr(address1.replace("\"", ""));
					c.setCode(zipcode1.replace("\"", ""));
					c.setPhone(companyphone1.replace("\"", ""));
					c.setPhoneNum(emergentphone1.replace("\"", ""));
					c.setTaskid(taskBank.getTaskid());
					c.setEmail(email1.replace("\"", ""));
					c.setLastNumber(card_nbr_no.replace("\"", ""));
					webParam.setHtml(page2.getWebResponse().getContentAsString());
					webParam.setUrl(url);
					webParam.setWebClient(webClient);
					webParam.setCiticChinaCreditCardUserInfo(c);
			}
			return webParam;
		}




		//流水htmlunit
		public WebParam<CiticChinaCreditCardAccount> creditCardAccountCrawlerHtmlunit(TaskBank taskBank, BankJsonBean bankJsonBean,int i) throws Exception {
			String url1="https://creditcard.ecitic.com/citiccard/newonline/installments.do?func=checkDreamGoldRight";
			String url2="https://creditcard.ecitic.com/citiccard/newonline/common.do?func=querySignCards";
			WebParam<CiticChinaCreditCardAccount> webParam = new WebParam<CiticChinaCreditCardAccount>();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskBank.getCookies());
			for (Cookie cookie : cookies1) {
				webClient.getCookieManager().addCookie(cookie);
			}
			Page page1 = webClient.getPage(url1);
			webParam.setHtml(page1.getWebResponse().getContentAsString());
			Page page2 = webClient.getPage(url2);
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			String contentAsString = page2.getWebResponse().getContentAsString();
			//System.out.println(contentAsString);
			if(contentAsString.contains("card_nbr"))
			{
				int indexOf1 = contentAsString.indexOf("stmt_dte_1");
				int indexOf22 = contentAsString.indexOf("stmt_dte_2");
				String stmt_date = contentAsString.substring(indexOf1, indexOf22);
				String substring2 = stmt_date.substring(12);
				String replace = substring2.replace("\"", "").replace("-", "");
//				System.out.println(replace);
				
				int indexOf = contentAsString.indexOf("card_nbr");
				String substring = contentAsString.substring(indexOf+10,indexOf+42);
				
				int indexOf2 = contentAsString.indexOf("card_nbr_no");
				String string2 = contentAsString.substring(indexOf2+13).replace("\"", "");
//				System.out.println(string2);
				
				
				String url3="https://creditcard.ecitic.com/citiccard/newonline/installments.do?func=queryInstallmentsByCardNoBZ&cardno="+substring+"&crytype=156";
//				String url4="https://creditcard.ecitic.com/citiccard/newonline/billQuery.do?func=queryBillInfo&cardNo="+substring+"&stmt_date="+getFirstDay("yyyyMMdd",i)+"&crytype=156&start_pos=1&count=610&rowsPage=10";
				String url4="https://creditcard.ecitic.com/citiccard/newonline/billQuery.do?func=queryBillInfo&cardNo="+substring+"&stmt_date="+getFirstDay("yyyyMM"+replace.substring(6).trim(), i)+"&crytype=156&start_pos=1&count=12&rowsPage=10&startpos=1";

				Page page3 = webClient.getPage(url3);
				Thread.sleep(1000);
				if(null != page3)
				{
					webParam.setHtml(page3.getWebResponse().getContentAsString());
					Page page4 = webClient.getPage(url4); 
					Thread.sleep(1000);
					tracer.addTag("crawler.account.creditcard.first"+i, "<xmp>"+page3.getWebResponse().getContentAsString()+"</xmp>"+url3);
					if(null != page4 && page4.getWebResponse().getContentAsString().contains("acct"))
					{                                                      
						tracer.addTag("crawler.account.creditcard.second"+i, "<xmp>"+page4.getWebResponse().getContentAsString()+"</xmp>"+url4);
						webParam.setHtml(page4.getWebResponse().getContentAsString());
//						System.out.println(page4.getWebResponse().getContentAsString());
						String contentAsString2 = page4.getWebResponse().getContentAsString();
						int indexOf3 = contentAsString2.indexOf("acct");
						int indexOf4 = contentAsString2.indexOf("acct_pdt");
						int indexOf5 = contentAsString2.indexOf("bank_nbr");
						String subs = contentAsString2.substring(indexOf3, indexOf4);
						String subs2 = contentAsString2.substring(indexOf4, indexOf5);
						String acct_pdt = subs2.substring(10).replace("\"", "");
						String acct = subs.substring(6).replace("\"", "");
//						System.out.println(acct.trim());
						String url5="https://creditcard.ecitic.com/citiccard/newonline/billQuery.do?func=queryBillDetail&cardNo="+substring+"&stmt_date="+getFirstDay("yyyyMM"+replace.substring(6).trim(),i)+"&crytype=156&start_pos=1&count=12&rowsPage=1000&startpos=1&acct="+acct.trim()+"&acct_pdt="+acct_pdt;
						Page page5 = webClient.getPage(url5); 
						Thread.sleep(1000);
//						System.out.println(url5);
						System.out.println(page5.getWebResponse().getContentAsString());
						if(page5.getWebResponse().getContentAsString().contains("billDetailList"))
						{
							webParam.setHtml(page5.getWebResponse().getContentAsString());
							tracer.addTag("crawler.account.creditcard.success"+i, page5.getWebResponse().getContentAsString()+url5);
							Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
							Document doc1 = Jsoup.parse(page5.getWebResponse().getContentAsString());
							System.out.println(page4.getWebResponse().getContentAsString());
//							if(page5.getWebResponse().getContentAsString().contains("获取账单信息成功"))
//							{
//								Element element1 = doc.getElementsByTag("billprofile").get(0);
//								//System.out.println(element1);
//								String string1 = element1.toString();
//								
//								int cur_amtNum = string1.indexOf("cur_amt");
//								int min_payNum = string1.indexOf("min_pay");			
//								int cashNum = string1.indexOf("cash");
//								int stmt_date_textNum = string1.indexOf("stmt_date_text");
//								int dte_pymt_dueNum = string1.indexOf("dte_pymt_due");
//								int purchaseNum = string1.indexOf("purchase");
//								int credit_avail_1Num = contentAsString.indexOf("credit_avail_1"); 
//								int credit_avail_2Num = contentAsString.indexOf("credit_avail_2");
//								
//								int curr_codeNum = string1.indexOf("curr_code");
//								int pre_hpayNum = string1.indexOf("pre_hpay");
//								int stmt_dateNum = string1.indexOf("stmt_date");
//								String cur_amt = string1.substring(cur_amtNum, curr_codeNum).substring(8);
//								System.out.println(cur_amt);
//								String min_pay = string1.substring(min_payNum, pre_hpayNum).substring(8);
//								System.out.println(min_pay);
//								String cash = string1.substring(cashNum, cur_amtNum).substring(5);
//								System.out.println(cash);
//								String stmt_date_text = string1.substring(stmt_date_textNum).substring(15,29);
//								System.out.println(stmt_date_text);
//								String dte_pymt_due = string1.substring(dte_pymt_dueNum, min_payNum).substring(13);
//								System.out.println(dte_pymt_due);
//								String purchase = string1.substring(purchaseNum, stmt_dateNum).substring(9);
//								System.out.println(purchase);
//								String credit_avail_1 = contentAsString.substring(credit_avail_1Num, credit_avail_2Num-1).substring(16);
//								System.out.println(credit_avail_1);
//								CiticChinaCreditCardBill citicChinaCreditCardBill =new  CiticChinaCreditCardBill();
//								List<CiticChinaCreditCardBill> list1 = new ArrayList<CiticChinaCreditCardBill>();
//								citicChinaCreditCardBill.setCardNum(outString(string2));
//								citicChinaCreditCardBill.setRepay(outString(cur_amt));
//								citicChinaCreditCardBill.setLowstRepay(outString(min_pay));
//								citicChinaCreditCardBill.setAlreadyMoney(outString(cash));
//								citicChinaCreditCardBill.setNotRepay(outString(purchase));
//								citicChinaCreditCardBill.setThisDatea(outString(stmt_date_text));
//								citicChinaCreditCardBill.setRepayDatea(outString(dte_pymt_due));
//								citicChinaCreditCardBill.setCredit_avail_1(outString(credit_avail_1));
//								citicChinaCreditCardBill.setTaskid(taskBank.getTaskid());
//								list1.add(citicChinaCreditCardBill);
								Elements elementsByTag = doc1.getElementsByTag("billdetaillist");
			//							System.out.println(elementsByTag);
								CiticChinaCreditCardAccount c = null;
								List<CiticChinaCreditCardAccount> list = new ArrayList<CiticChinaCreditCardAccount>();
								
								for (Element element : elementsByTag) {
									String string = element.toString();
									c = new CiticChinaCreditCardAccount();
									int post_amtNum = string.indexOf("post_amt");
									int post_currNum = string.indexOf("post_curr");
									int post_dateNum = string.indexOf("post_date");
									int tram_codeNum = string.indexOf("tram_code");
									int tran_currNum = string.indexOf("tran_curr");
									int tran_dateNum = string.indexOf("tran_date");
									int tran_descNum = string.indexOf("tran_desc");
									int tran_amtNum = string.indexOf("tran_amt");
									String post_amt = string.substring(post_amtNum, post_currNum).substring(9);
									String post_curr = string.substring(post_currNum, post_dateNum).substring(10);
									String post_date = string.substring(post_dateNum, tram_codeNum).substring(10);
									//String tram_code = string.substring(tram_codeNum, tran_amtNum).substring(10);
									String tran_curr = string.substring(tran_currNum, tran_dateNum).substring(10);
									String tran_date = string.substring(tran_dateNum, tran_descNum).substring(10);
									String tran_amt = string.substring(tran_amtNum,tran_currNum).substring(9);
									String tran_desc = string.substring(tran_descNum).substring(10).replace("/>", "");
									//System.out.println(post_amt+"/"+post_curr);
									//System.out.println(post_date);
									//System.out.println(tram_code);
									//System.out.println(tran_date);
									//System.out.println(tran_desc);
									//System.out.println(tran_amt+"/"+tran_curr);
									
									c.setMoneyStatus(outString(post_amt));
									c.setDescription(outString(tran_desc));
									c.setMoneyStatus(outString(post_amt+"/"+post_curr));
									c.setDatea(outString(post_date));
									c.setSum(outString(tran_amt+"/"+tran_curr));
									c.setGetDatea(outString(tran_date));
									c.setIdCard(string2.substring(16));
									c.setTaskid(taskBank.getTaskid());
									c.setAccountType("已出账单");
									list.add(c);
								}
//								System.out.println(list);
								webParam.setUrl(url1);
								webParam.setList(list);
//								webParam.setList1(list1);
							}
						}
					}
					else
					{
						String url6="https://creditcard.ecitic.com/citiccard/newonline/billQuery.do?func=queryUnSettleBill&cardNo="+substring+"&stmt_date="+getFirstDay("yyyyMM"+replace.substring(6).trim(),i)+"&crytype=156&rowsPage=10&startpos=1";
//						             https://creditcard.ecitic.com/citiccard/newonline/billQuery.do?func=queryUnSettleBill&cardNo=ec667ec427ef64e969191fe3b3c0ff24&stmt_date=&crytype=156&rowsPage=10&startpos=1
						Page page6 = webClient.getPage(url6); 
						Thread.sleep(1000);
						if(null != page6 && page6.getWebResponse().getContentAsString().contains("billDetailList"))
						{
							webParam.setHtml(page6.getWebResponse().getContentAsString());
							CiticChinaCreditCardAccount c = null;
							Document doc = Jsoup.parse(page6.getWebResponse().getContentAsString());
							Elements elementsByTag = doc.getElementsByTag("billDetailList");
//							System.out.println(elementsByTag);
							List<CiticChinaCreditCardAccount> list = new ArrayList<CiticChinaCreditCardAccount>();
							for (int j = 0; j < elementsByTag.size(); j++) {
								c = new CiticChinaCreditCardAccount();
								Element element = elementsByTag.get(j);
								String string = element.toString();
								int tran_desc = string.indexOf("tran_desc");
//								System.out.println(tran_desc);
								int tran_date = string.indexOf("tran_date");
//								System.out.println(tran_date);
								int tran_curr = string.indexOf("tran_curr");
//								System.out.println(tran_curr);
								int tran_amt = string.indexOf("tran_amt");
//								System.out.println(tran_amt);
								int post_date = string.indexOf("post_date");
//								System.out.println(post_date);
								int post_curr = string.indexOf("post_curr");
//								System.out.println(post_curr);
								int rs_card_nbr = string.indexOf("rs_card_nbr");
//								System.out.println(rs_card_nbr);
								int post_amt = string.indexOf("post_amt");
//								System.out.println(post_amt);
								int tran_code = string.indexOf("tran_code");
//								System.out.println(tran_code);
								
								
								String tran_desc1 = string.substring(tran_desc, tran_date);
								String tran_date1 = string.substring(tran_date, tran_curr);
								String tran_curr1 = string.substring(tran_curr, tran_code);
								String tran_amt1 = string.substring(tran_amt, rs_card_nbr);
								String post_date1 = string.substring(post_date, post_curr);
								String post_curr1 = string.substring(post_curr, post_amt);
								System.out.println(tran_desc1.substring(11, tran_desc1.length()-2));
								System.out.println(tran_date1.substring(11, tran_date1.length()-2));
								System.out.println(tran_curr1.substring(11, tran_curr1.length()-2));
								System.out.println(tran_amt1.substring(10, tran_amt1.length()-2));
								System.out.println(post_date1.substring(11, post_date1.length()-2));
								System.out.println(post_curr1.substring(11, post_curr1.length()-2));
								
								c.setDescription(tran_desc1.substring(11, tran_desc1.length()-2));
								c.setDatea(tran_date1.substring(11, tran_date1.length()-2));
								c.setGetDatea(post_date1.substring(11, post_date1.length()-2));
								c.setIdCard(substring);
								c.setAccountType("未出账单");
								c.setMoneyStatus(tran_curr1.substring(11, tran_curr1.length()-2));
								c.setSum(tran_amt1.substring(10, tran_amt1.length()-2));
								c.setTaskid(taskBank.getTaskid());
								list.add(c);
							}
							System.out.println(list);
							webParam.setUrl(url6);
							webParam.setList(list);
						}
					}
				}
//			}
			return webParam;
		}
		
		/**
		 * 指定图片验证码保存的路径和随机生成的名称，拼接在一起	
		 * 利用IO流保存验证码成功后，将完整路径信息一并返回
		 * 
		 * @param page
		 * @param imagePath
		 * @return
		 * @throws Exception
		 */
		public  String getImagePath(Page page,String imagePath) throws Exception{
			File parentDirFile = new File(imagePath);
			parentDirFile.setReadable(true);
			parentDirFile.setWritable(true); 
			if (!parentDirFile.exists()) {
				System.out.println("==========创建文件夹==========");
				parentDirFile.mkdirs();
			}
			String imageName = UUID.randomUUID().toString() + ".jpg";
			File codeImageFile = new File(imagePath + "/" + imageName);
			codeImageFile.setReadable(true); 
			codeImageFile.setWritable(true, false);
			////////////////////////////////////////
			
			String imgagePath = codeImageFile.getAbsolutePath(); 
			InputStream inputStream = page.getWebResponse().getContentAsStream();
			FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
			if (inputStream != null && outputStream != null) {  
		        int temp = 0;  
		        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
		        	outputStream.write(temp);   // 边读边写  
		        }  
		        outputStream.close();  
		        inputStream.close();   // 关闭输入输出流  
		    }
			return imgagePath;
		}



		public WebParam<CiticChinaCreditCardBill> creditCardBillCrawlerHtmlunit(TaskBank taskBank,
				BankJsonBean bankJsonBean, int j) throws Exception{

			String url1="https://creditcard.ecitic.com/citiccard/newonline/installments.do?func=checkDreamGoldRight";
			String url2="https://creditcard.ecitic.com/citiccard/newonline/common.do?func=querySignCards";
			WebParam<CiticChinaCreditCardBill> webParam = new WebParam<CiticChinaCreditCardBill>();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskBank.getCookies());
			for (Cookie cookie : cookies1) {
				webClient.getCookieManager().addCookie(cookie);
			}
			Page page1 = webClient.getPage(url1);
			webParam.setHtml(page1.getWebResponse().getContentAsString());
			Page page2 = webClient.getPage(url2);
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			String contentAsString = page2.getWebResponse().getContentAsString();
//			System.out.println(contentAsString);
			if(contentAsString.contains("card_nbr"))
			{
				int indexOf1 = contentAsString.indexOf("stmt_dte_1");
				int indexOf22 = contentAsString.indexOf("stmt_dte_2");
				String stmt_date = contentAsString.substring(indexOf1, indexOf22);
				String substring2 = stmt_date.substring(12);
				String replace = substring2.replace("\"", "").replace("-", "");
//				System.out.println(replace);
				
				int indexOf = contentAsString.indexOf("card_nbr");
				String substring = contentAsString.substring(indexOf+10,indexOf+42);
				
				int indexOf2 = contentAsString.indexOf("card_nbr_no");
				String string2 = contentAsString.substring(indexOf2+13,indexOf2+29).replace("\"", "");
//				System.out.println(string2);
				
				
				String url3="https://creditcard.ecitic.com/citiccard/newonline/installments.do?func=queryInstallmentsByCardNoBZ&cardno="+substring+"&crytype=156";
//				String url4="https://creditcard.ecitic.com/citiccard/newonline/billQuery.do?func=queryBillInfo&cardNo="+substring+"&stmt_date="+getFirstDay("yyyyMMdd",j)+"&crytype=156&start_pos=1&count=1000&rowsPage=10&startpos=1";
				String url6="https://creditcard.ecitic.com/citiccard/newonline/billQuery.do?func=queryBillInfo&cardNo="+substring+"&stmt_date="+getFirstDay("yyyyMM"+replace.substring(6).trim(), j)+"&crytype=156&start_pos=1&count=12&rowsPage=10&startpos=1";
				Page page3 = webClient.getPage(url3);
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				Page page4 = webClient.getPage(url6); 
				webParam.setHtml(page4.getWebResponse().getContentAsString());
				System.out.println(url6);

//				System.out.println(page4.getWebResponse().getContentAsString());
				List<CiticChinaCreditCardBill> list = new ArrayList<CiticChinaCreditCardBill>();
				if(page4.getWebResponse().getContentAsString().contains("网络繁忙"))
				{
					return null;
				}
				else{
					String contentAsString2 = page4.getWebResponse().getContentAsString();
					int indexOf3 = contentAsString2.indexOf("acct");
					int indexOf4 = contentAsString2.indexOf("acct_pdt");
					int indexOf5 = contentAsString2.indexOf("bank_nbr");
					String subs = contentAsString2.substring(indexOf3, indexOf4);
					String subs2 = contentAsString2.substring(indexOf4, indexOf5);
					String acct_pdt = subs2.substring(10).replace("\"", "");
					String acct = subs.substring(6).replace("\"", "");
					Document doc = Jsoup.parse(contentAsString2);
					if(doc.toString().contains("获取账单信息成功"))
					{
						Element element1 = doc.getElementsByTag("billprofile").get(0);
						//System.out.println(element1);
						String string1 = element1.toString();
						int cur_amtNum = string1.indexOf("cur_amt");
						int min_payNum = string1.indexOf("min_pay");			
						int cashNum = string1.indexOf("cash");
						int stmt_date_textNum = string1.indexOf("stmt_date_text");
						int dte_pymt_dueNum = string1.indexOf("dte_pymt_due");
						int purchaseNum = string1.indexOf("purchase");
						int credit_avail_1Num = contentAsString.indexOf("credit_avail_1"); 
						int credit_avail_2Num = contentAsString.indexOf("credit_avail_2");
						
						int curr_codeNum = string1.indexOf("curr_code");
						int pre_hpayNum = string1.indexOf("pre_hpay");
						int stmt_dateNum = string1.indexOf("stmt_date");
						String cur_amt = string1.substring(cur_amtNum, curr_codeNum).substring(8);
						//System.out.println(cur_amt);
						String min_pay = string1.substring(min_payNum, pre_hpayNum).substring(8);
						//System.out.println(min_pay);
						String cash = string1.substring(cashNum, cur_amtNum).substring(5);
						//System.out.println(cash);
						String stmt_date_text = string1.substring(stmt_date_textNum).substring(15,29);
						//System.out.println(stmt_date_text);
						String dte_pymt_due = string1.substring(dte_pymt_dueNum, min_payNum).substring(13);
						//System.out.println(dte_pymt_due);
						String purchase = string1.substring(purchaseNum, stmt_dateNum).substring(9);
						//System.out.println(purchase);
						String credit_avail_1 = contentAsString.substring(credit_avail_1Num, credit_avail_2Num-1).substring(16);
						//System.out.println(credit_avail_1);
						CiticChinaCreditCardBill citicChinaCreditCardBill =new  CiticChinaCreditCardBill();
						citicChinaCreditCardBill.setCardNum(outString(string2));
						citicChinaCreditCardBill.setRepay(outString(cur_amt));
						citicChinaCreditCardBill.setLowstRepay(outString(min_pay));
						citicChinaCreditCardBill.setAlreadyMoney(outString(cash));
						citicChinaCreditCardBill.setNotRepay(outString(purchase));
						citicChinaCreditCardBill.setThisDatea(outString(stmt_date_text));//replace
						citicChinaCreditCardBill.setRepayDatea(outString(dte_pymt_due));
						citicChinaCreditCardBill.setCredit_avail_1(outString(credit_avail_1));
						citicChinaCreditCardBill.setTaskid(taskBank.getTaskid());
						list.add(citicChinaCreditCardBill);
//							System.out.println(list);
						webParam.setUrl(url1);
						webParam.setList(list);
						
					}
				
				  }
				}
			return webParam;
		}
}
