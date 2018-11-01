//package org.common.microservice.eureka.china.unicomt.test;
//
//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.springframework.util.StringUtils;
//
//import com.crawler.mobile.json.MessageLogin;
//import com.crawler.mobile.json.StatusCodeRec;
//import com.crawler.telecom.json.SMSCodejson;
//import com.gargoylesoftware.htmlunit.HttpMethod;
//import com.gargoylesoftware.htmlunit.Page;
//import com.gargoylesoftware.htmlunit.TextPage;
//import com.gargoylesoftware.htmlunit.UnexpectedPage;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebRequest;
//import com.gargoylesoftware.htmlunit.html.HtmlElement;
//import com.gargoylesoftware.htmlunit.html.HtmlImage;
//import com.gargoylesoftware.htmlunit.html.HtmlInput;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
//import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
//import com.gargoylesoftware.htmlunit.util.Cookie;
//import com.gargoylesoftware.htmlunit.util.NameValuePair;
//import com.google.gson.Gson;
//import com.module.htmlunit.WebCrawler;
//
//
///**
// * @author zhenZ
// * @createtime 2017年6月8日 上午10:02:20 北京移动通话记录爬取（测试类）
// */
//public class dianxintest {
//
//	private static Gson gs = new Gson();
//
//	public static void main(String[] args) throws Exception {
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url = "http://login.189.cn/login";
//		HtmlPage html = (HtmlPage) getHtml(url, webClient);
//		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
//		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
//		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
//		username.setText("13366777357");
//		passwordInput.setText("591414");
//
//		HtmlPage htmlpage2 = button.click();
//		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
//			System.out.println("=======失败==============");
//		}
//		
//		url = "http://www.189.cn/dqmh/my189/initMy189home.do";
//		HtmlPage html3 = (HtmlPage) getHtml(url, webClient);
//		
//		System.out.println(html3.asXml());
//		/*
//		url = "http://bj.189.cn/service/account/lastLoginTime.action";
//
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		System.out.println("====================" + (System.currentTimeMillis() / 1000));
//		paramsList.add(new NameValuePair("time", (System.currentTimeMillis() / 1000) + ""));
//		try {
//			Page page = gethtmlPost(webClient, paramsList, url);
//
//			System.out.println("===================================================================");
//
//			System.out.println(page.getWebResponse().getContentAsString());
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//
//		url = "http://bj.189.cn/iframe/feequery/billQueryIndex.action?fastcode=01390637";
//
//		try {
//			HtmlPage html4 = getHtml(url, webClient);
//			System.out.println("==================================================");
//			System.out.println(html4.asXml());
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}*/
//		//callthrembyclice(webClient);
//
//		
//		 //String code = getSMSCode(webClient); setSMSCoide(webClient, code);
//		  
//		  calltherm(webClient);
//		 /* setSMSCoide(webClient, code);
//		  calltherm2(webClient);*/
//		 
//
//		/*
//		 * url =
//		 * "http://bj.189.cn/iframe/custservice/modifyUserInfo.action?indexPage=INDEX3";
//		 * 
//		 * Page page = getHtml(url, html3.getWebClient());
//		 * 
//		 * System.out.println("====="+page.getWebResponse().getContentAsString()
//		 * );
//		 */
//
//	}
//
//	public static void integra(WebClient webClient) {
//
//		String url = "http://bj.189.cn/iframe/custquery/qryPointHistory.action?indexPage=INDEX3&fastcode=01410646&cityCode=bj";
//		try {
//
//			webClient.setJavaScriptTimeout(20000);
//
//			webClient.getOptions().setTimeout(20000); // 15->60
//
//			Page page = getHtml(url, webClient);
//			System.out.println(page.getWebResponse().getContentAsString());
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//	}
//
//	public static void pay(WebClient webClient) {
//
//		String url = "http://bj.189.cn/iframe/local/queryPaymentRecord.action";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("accNum", "13366777357"));
//		paramsList.add(new NameValuePair("requestFlag", "synchronization"));
//		paramsList.add(new NameValuePair("paymentHistoryQueryIn.startDate", "2017-03-01"));
//		paramsList.add(new NameValuePair("paymentHistoryQueryIn.endDate", "2017-03-31"));
//
//		try {
//
//			webClient.setJavaScriptTimeout(20000);
//
//			webClient.getOptions().setTimeout(20000); // 15->60
//
//			Page page = gethtmlPost(webClient, paramsList, url);
//			System.out.println(page.getWebResponse().getContentAsString());
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//	}
//
//	public static void calltherm(WebClient webClient2) {
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//
//		Map<String, String> mapcookie = new java.util.HashMap<>();
//
//		//mapcookie.put(".ybtj.189.cn", "70F8886ECE31DEA777CF3B57C702E181");
//		//mapcookie.put("Hm_lpvt_5b3beae528c7fc9af9c016650f4581e0", ((System.currentTimeMillis() / 1000) + ""));// 可能与登录过期有关
//		//mapcookie.put("Hm_lvt_5b3beae528c7fc9af9c016650f4581e0", "1503299705,1503299825,1503300283,1503300369");
//		mapcookie.put("Hm_lvt_79fae2027f43ca31186e567c6c8fe33e", "1501494752,1501494789,1501570751,1501577064");
//		mapcookie.put("Hm_lvt_99c2beeae1f4239c63480835d2892bf4", "1501581156");
//	//	mapcookie.put("JSESSIONID_bj", "HFBGZhLLrNZGTYfnwnLvQy7Rp36j1zp29nfnVp2xn3PYYMTLwgQL"); // 变化
//		mapcookie.put("SHOPID_COOKIEID", "10001");
//		mapcookie.put("UM_distinctid", "15d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557");
//		mapcookie.put("WT_FPC", "id=12c1e94d755ecde623d1501497535702");
//		mapcookie.put("WT_SS", "150286817362919eccb541");
//		//mapcookie.put("WT_si_n", "WEB_Q_MYLIST_THEPHONELIST_XDCX");
//		mapcookie.put("__utma", "218110873.589372428.1501581156.1501581156.1501581156.1");
//		mapcookie.put("__utmz", "218110873.1501581156.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
//		mapcookie.put("_gscu_1758414200", "015599983snitv16");
//		// mapcookie.put("aactgsh111220", "13366777357");
//		mapcookie.put("cityCode", "bj");
//		//cn_1260051947_dplus 参数未发生变化
//		mapcookie.put("cn_1260051947_dplus",
//				"%7B%22distinct_id%22%3A%20%2215d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557%22%2C%22sp%22%3A%20%7B%22%24recent_outside_referrer%22%3A%20%22www.baidu.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201503038497%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201503038497%7D%2C%22initial_view_time%22%3A%20%221501493821%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DC5gnI1MOIDBWo3OrGrMxnEjAAnhMBNOAHWIls2ffKuq-_zaY4GpCvvgm2_QRPn6J%26wd%3D%26eqid%3Dbf7d80a2000275f300000002597efec8%22%2C%22initial_referrer_domain%22%3A%20%22www.baidu.com%22%7D");
//		
//		/* * mapcookie.put("d_appid", "null"); mapcookie.put("d_channel", "0");//
//		 * 有变化 由空值变为0 mapcookie.put("d_cmpid", "Null");
//		 * mapcookie.put("d_openid", "null");
//		 */
//		
//		//url解码为 北京市 联通数据中心
//		mapcookie.put("dqmhIpCityInfos",
//				"%E5%8C%97%E4%BA%AC%E5%B8%82+%E8%81%94%E9%80%9A%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83");
//		mapcookie.put("fenxiId", "8c07f0a0-3c66-46ec-b4b4-1ed04013c6a9");
//		mapcookie.put("flag", "2");// 有变化 由空值变为 2
//		mapcookie.put("i_cc", "TRUE");
//		mapcookie.put("i_ppv", "99");
//		//url解码为 [[B]]
//		mapcookie.put("i_sq", "%5B%5BB%5D%5D");
//		// mapcookie.put("i_url",
//		// "http%3A%2F%2Fgd.189.cn%2Fcommon%2FnewLogin%2FnewLogin.htm%3FSSOArea%3D0000%26SSOAccount%3Dnull%26SSOProType%3Dnull%26SSORetryTimes%3Dnull%26SSOError%3Dnull%26SSOCustType%3D0%26loginOldUri%3D%2Fservice%2Fhome%2Fquery%2Fxd_index.html%26SSOOldAccount%3Dnull%26SSOProTypePre%3Dnull");
//		mapcookie.put("i_vnum", "7");
//		mapcookie.put("ijg", System.currentTimeMillis()+"");
//		mapcookie.put("insight_v", "20151104");
//		mapcookie.put("isLogin", "logined");
//		mapcookie.put("loginStatus", "logined");
//		//mapcookie.put("lvid", "bcf2b4ee745bbf3cf5523de6b4575d53");
//		mapcookie.put("nvid", "1");
//		mapcookie.put("s_cc", "TRUE");
//		//mapcookie.put("s_fid", "738EA9785110F826-3EC88B4E0CA518D6");
//		// 新加
//		mapcookie.put("s_sq",
//				"eshipeship-189-all%3D%2526pid%253D%25252Fiframe%25252Ffeequery%25252FdetailBillIndex.parser%2526pidt%253D1%2526oid%253Dfunctiononclick%252528event%252529%25257BqryDetailLstInfo%252528%252529%25257D%2526oidt%253D2%2526ot%253DSUBMIT%26eship-189-wap%3D%2526pid%253D%25252Frecharge%25252Frecharge_index.html%2526pidt%253D1%2526oid%253Dfunctiononclick%252528event%252529%25257Bhistory.back%252528%252529%25253B%25257D%2526oidt%253D2%2526ot%253DIMG"); // 新加
//																																																																																																																				// mapcookie.put("svid",
//																																																																																																																				// "74A71D3B741676F6");
//		// mapcookie.put("trkHmCity", "0");
//		// mapcookie.put("trkHmCitycode", "0");
//		//mapcookie.put("trkHmClickCoords", "208%2C502%2C1324");// 有变化
//		// mapcookie.put("trkHmCoords", "0");
//		// mapcookie.put("trkHmLinks", "0");
//		// mapcookie.put("trkHmPageName", "%2Fbj%2F");
//		//mapcookie.put("trkId", "8A61BEF4-882A-4041-9E0E-F59609E5EC5C");
//		// mapcookie.put("trkintaid", "jt-sy-jdt-07-tianyiyun");
//		mapcookie.put("userId", "201%7C138127872");
//
//		for (Map.Entry<String, String> entry : mapcookie.entrySet()) {
//			Cookie cookie = new Cookie("bj.189.cn", entry.getKey(), entry.getValue());
//			webClient.getCookieManager().addCookie(cookie);
//		}
//		
//		Set<Cookie> cookies3 = webClient2.getCookieManager().getCookies();
//		for (Cookie cookie3 : cookies3) {
//
//			if(cookie3.getName().indexOf("trkHmClickCoords")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			
//			if(cookie3.getName().indexOf("JSESSIONID_bj")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			
//			if(cookie3.getName().indexOf("Hm_lvt_5b3beae528c7fc9af9c016650f4581e0")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			
//			if(cookie3.getName().indexOf("Hm_lpvt_5b3beae528c7fc9af9c016650f4581e0")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			if(cookie3.getName().indexOf("WT_FPC")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			if(cookie3.getName().indexOf("WT_SS")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			
//			if(cookie3.getName().indexOf(".ybtj.189.cn")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			
//			if(cookie3.getName().indexOf("WT_si_n")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			
//			if(cookie3.getName().indexOf("trkId")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			if(cookie3.getName().indexOf("s_fid")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}
//			if(cookie3.getName().indexOf("lvid")!=-1){
//				System.out.println("==========="+cookie3.toString());
//				webClient.getCookieManager().addCookie(cookie3);
//			}			
//		}
//		
//		
//		
//		String url = "http://bj.189.cn/iframe/feequery/billDetailQuery.action?requestFlag=synchronization&billDetailType=1&qryMonth=2017%E5%B9%B406%E6%9C%88&startTime=1&accNum=13366777357&endTime=31";
//	/*	List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("accNum", "13366777357"));
//		paramsList.add(new NameValuePair("requestFlag", "synchronization"));
//		paramsList.add(new NameValuePair("billDetailType", "1"));
//		paramsList.add(new NameValuePair("qryMonth", "2017年07月"));
//		paramsList.add(new NameValuePair("startTime", "1"));
//		paramsList.add(new NameValuePair("endTime", "31"));*/
//		// paramsList.add(new NameValuePair("billPage", "1"));
//
//		/*
//		 * requestFlag:synchronization billDetailType:1 qryMonth:2017年08月
//		 * startTime:1 accNum:13366777357 endTime:14
//		 */
//
//		try {
//
//			webClient.setJavaScriptTimeout(20000);
//
//			webClient.getOptions().setTimeout(20000); // 15->60
//
//			//Page page = gethtmlPost(webClient, paramsList, url);
//			
//			Page page = getHtml2(url, webClient);
//			System.out.println(page.getWebResponse().getContentAsString());
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//	}
//
//	public static void callthrembyclice(WebClient webClient) {
//
//		String url = "http://www.189.cn/dqmh/my189/initMy189home.do";
//
//		try {
//
//			webClient.setJavaScriptTimeout(2000000);
//
//			webClient.getOptions().setTimeout(2000000); // 15->60
//
//			webClient.getOptions().setJavaScriptEnabled(true);
//
//			HtmlPage page = (HtmlPage) getHtml(url, webClient);
//			// style="display: none;
//
//			System.out.println(page.asXml());
//			/*HtmlElement buttonclienk = page.getFirstByXPath("//*[@id='mainBody']/div[2]/div[1]/a");
//
//			page = buttonclienk.click();
//
//			buttonclienk = page.getFirstByXPath("//*[@id='mainBody']/div[2]/div[1]/div/a[2]");
//
//			page = buttonclienk.click();
//
//			System.out.println("==================================================");
//			System.out.println(page.asXml());
//
//			String code;
//			code = getSMSCode(webClient);
//
//			HtmlElement buttonclienk2 = page.getFirstByXPath("//*[@class='ued-button']");
//			System.out.println(buttonclienk2.asXml());
//			Page page2 = buttonclienk2.click();
//
//			Thread.sleep(10000);
//			System.out.println(page2.getWebResponse().getContentAsString());
//
//			try {
//				code = getcodebySring(page2.getWebResponse().getContentAsString());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				code = getSMSCode(webClient);
//			}
//
//			HtmlInput smscodeinput = page.getFirstByXPath("//*[@id='smsRandCode']");
//			// smscodeinput.s
//			smscodeinput.setValueAttribute(code);
//			Page unexpectedPage = buttonclienk2.click();
//
//			System.out.println("===================================================================");
//			System.out.println(unexpectedPage.getWebResponse().getContentAsString());
//
//			System.out.println("code=========" + code);
//			webClient = setSMSCoide(webClient, code);
//
//			url = "http://bj.189.cn/iframe/feequery/detailBillIndex.action?tab=tab1&time=1503047054842";
//
//			try {
//
//				webClient.setJavaScriptTimeout(2000000);
//
//				webClient.getOptions().setTimeout(2000000); // 15->60
//
//				webClient.getOptions().setJavaScriptEnabled(true);
//
//				page = getHtml(url, webClient);
//
//				System.out.println(page.asXml());
//
//				Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//				for (Cookie cookie2 : cookies) {
//
//					System.out.println(cookie2.toString());
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//*/
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void calltherm2(WebClient webClient) {
//
//		Map<String, String> mapcookie = new java.util.HashMap<>();
//
//		mapcookie.put(".ybtj.189.cn", "70F8886ECE31DEA777CF3B57C702E181");
//		mapcookie.put("Hm_lpvt_5b3beae528c7fc9af9c016650f4581e0", ((System.currentTimeMillis() / 1000) + ""));// 可能与登录过期有关
//		mapcookie.put("Hm_lvt_5b3beae528c7fc9af9c016650f4581e0", "1503047531,1503047623,1503278633,1503280724");
//		mapcookie.put("Hm_lvt_79fae2027f43ca31186e567c6c8fe33e", "1501494752,1501494789,1501570751,1501577064");
//		mapcookie.put("Hm_lvt_99c2beeae1f4239c63480835d2892bf4", "1501581156");
//		mapcookie.put("JSESSIONID_bj", "HFBGZhLLrNZGTYfnwnLvQy7Rp36j1zp29nfnVp2xn3PYYMTLwgQL"); // 变化
//		mapcookie.put("SHOPID_COOKIEID", "10001");
//		mapcookie.put("UM_distinctid", "15d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557");
//		mapcookie.put("WT_FPC", "id=12c1e94d755ecde623d1501497535702");
//		mapcookie.put("WT_SS", "150286817362919eccb541");
//		mapcookie.put("WT_si_n", "WEB_Q_MYLIST_THEPHONELIST_XDCX");
//		mapcookie.put("__utma", "218110873.589372428.1501581156.1501581156.1501581156.1");
//		mapcookie.put("__utmz", "218110873.1501581156.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
//		mapcookie.put("_gscu_1758414200", "015599983snitv16");
//		mapcookie.put("aactgsh111220", "13366777357");
//		mapcookie.put("cityCode", "bj");
//		//mapcookie.put("cn_1260051947_dplus",
//			//	"%7B%22distinct_id%22%3A%20%2215d980fb0c21b8-0fd77255fd1164-474f0820-100200-15d980fb0c9557%22%2C%22sp%22%3A%20%7B%22%24recent_outside_referrer%22%3A%20%22www.baidu.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201503038497%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201503038497%7D%2C%22initial_view_time%22%3A%20%221501493821%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DC5gnI1MOIDBWo3OrGrMxnEjAAnhMBNOAHWIls2ffKuq-_zaY4GpCvvgm2_QRPn6J%26wd%3D%26eqid%3Dbf7d80a2000275f300000002597efec8%22%2C%22initial_referrer_domain%22%3A%20%22www.baidu.com%22%7D");
//		mapcookie.put("d_appid", "null");
//		mapcookie.put("d_channel", "0");// 有变化 由空值变为0 
//		mapcookie.put("d_cmpid", "Null");
//		mapcookie.put("d_openid", "null");
//		mapcookie.put("dqmhIpCityInfos",
//				"%E5%8C%97%E4%BA%AC%E5%B8%82+%E8%81%94%E9%80%9A%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83");
//		mapcookie.put("fenxiId", "8c07f0a0-3c66-46ec-b4b4-1ed04013c6a9");
//		mapcookie.put("flag", "2");// 有变化 由空值变为 2
//		mapcookie.put("i_cc", "TRUE");
//		mapcookie.put("i_ppv", "99");
//		mapcookie.put("i_sq", "%5B%5BB%5D%5D");
//		//mapcookie.put("i_url",
//			//	"http%3A%2F%2Fgd.189.cn%2Fcommon%2FnewLogin%2FnewLogin.htm%3FSSOArea%3D0000%26SSOAccount%3Dnull%26SSOProType%3Dnull%26SSORetryTimes%3Dnull%26SSOError%3Dnull%26SSOCustType%3D0%26loginOldUri%3D%2Fservice%2Fhome%2Fquery%2Fxd_index.html%26SSOOldAccount%3Dnull%26SSOProTypePre%3Dnull");
//		mapcookie.put("i_vnum", "7");
//		mapcookie.put("ijg", "1503038780187");
//		mapcookie.put("insight_v", "20151104");
//		mapcookie.put("isLogin", "logined");
//		mapcookie.put("loginStatus", "logined");
//		mapcookie.put("lvid", "bcf2b4ee745bbf3cf5523de6b4575d53");
//		mapcookie.put("nvid", "1");
//		mapcookie.put("s_cc", "TRUE");
//		mapcookie.put("s_fid", "738EA9785110F826-3EC88B4E0CA518D6"); // 新加
//		mapcookie.put("s_sq",
//				"eshipeship-189-all%3D%2526pid%253D%25252Fiframe%25252Ffeequery%25252FdetailBillIndex.parser%2526pidt%253D1%2526oid%253Dfunctiononclick%252528event%252529%25257BqryDetailLstInfo%252528%252529%25257D%2526oidt%253D2%2526ot%253DSUBMIT%26eship-189-wap%3D%2526pid%253D%25252Frecharge%25252Frecharge_index.html%2526pidt%253D1%2526oid%253Dfunctiononclick%252528event%252529%25257Bhistory.back%252528%252529%25253B%25257D%2526oidt%253D2%2526ot%253DIMG	"); // 新加
//																																																																																																																				// mapcookie.put("svid",
//																																																																																																																				// "74A71D3B741676F6");
//		mapcookie.put("trkHmCity", "0");
//		mapcookie.put("trkHmCitycode", "0");
//		mapcookie.put("trkHmClickCoords", "208%2C502%2C1324");// 有变化
//		mapcookie.put("trkHmCoords", "0");
//		mapcookie.put("trkHmLinks", "0");
//		mapcookie.put("trkHmPageName", "%2Fbj%2F");
//		mapcookie.put("trkId", "8A61BEF4-882A-4041-9E0E-F59609E5EC5C");
//		mapcookie.put("trkintaid", "jt-sy-jdt-07-tianyiyun");
//		mapcookie.put("userId", "201%7C138127872");
//
//		for (Map.Entry<String, String> entry : mapcookie.entrySet()) {
//			Cookie cookie = new Cookie("189.cn", entry.getKey(), entry.getValue());
//			webClient.getCookieManager().addCookie(cookie);
//		}
//
//		// webClient = LognAndGetBeijingUnit.ready(webClient, 0);
//		webClient = setSMSCoide(webClient, "800362");
//		String url = "http://bj.189.cn/iframe/feequery/billDetailQuery.action";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("accNum", "13366777357"));
//		paramsList.add(new NameValuePair("requestFlag", "synchronization"));
//		paramsList.add(new NameValuePair("billDetailType", "1"));
//		paramsList.add(new NameValuePair("qryMonth", "2017年06月"));
//		paramsList.add(new NameValuePair("startTime", "1"));
//		paramsList.add(new NameValuePair("endTime", "30"));
//		// paramsList.add(new NameValuePair("billPage", "1"));
//
//		/*
//		 * requestFlag:synchronization billDetailType:1 qryMonth:2017年08月
//		 * startTime:1 accNum:13366777357 endTime:14
//		 */
//
//		try {
//
//			webClient.setJavaScriptTimeout(20000);
//
//			webClient.getOptions().setTimeout(20000); // 15->60
//
//			Page page = gethtmlPost(webClient, paramsList, url);
//			System.out.println(page.getWebResponse().getContentAsString());
//
//			Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//			for (Cookie cookie2 : cookies) {
//
//				System.out.println(cookie2.toString());
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		/*
//		 * accNum:13366777357 requestFlag:synchronization billCycle:201706
//		 */
//	}
//
//	public static void calltherm3(WebClient webClient) {
//
//		String url = "http://bj.189.cn/iframe/feequery/detailBillIndex.action?tab=tab1&time=1503047054842";
//
//		try {
//
//			webClient.setJavaScriptTimeout(2000000);
//
//			webClient.getOptions().setTimeout(2000000); // 15->60
//
//			webClient.getOptions().setJavaScriptEnabled(true);
//
//			/*
//			 * String code = getSMSCode(webClient);
//			 * 
//			 * webClient = setSMSCoide(webClient, code);
//			 */
//
//			HtmlPage page = (HtmlPage) getHtml(url, webClient);
//			// style="display: none;
//			System.out.println(page.asXml());
//			HtmlElement smsele = page.getFirstByXPath("//*[@id='smsRandCodeDiv']");
//			System.out.println(smsele.asXml());
//			smsele.setAttribute("style", "display:none");
//
//			HtmlElement buttonclienk = page.getFirstByXPath("//*[@id='mainBody']/div[2]/div[1]/a");
//
//			page = buttonclienk.click();
//
//			/*
//			 * * System.out.println(
//			 * "==================================================");
//			 * System.out.println(page.asXml());
//			 */
//
//			buttonclienk = page.getFirstByXPath("//*[@id='mainBody']/div[2]/div[1]/div/a[2]");
//
//			page = buttonclienk.click();
//
//			System.out.println("==================================================");
//			System.out.println(page.asXml());
//
//			HtmlElement buttonclienk2 = page.getFirstByXPath("//*[@class='ued-button']");
//			System.out.println(buttonclienk2.asXml());
//			Page page2 = buttonclienk2.click();
//
//			System.out.println(page2.getWebResponse().getContentAsString());
//			// String code =
//			// getSMSCode(page2.getWebResponse().getContentAsString());
//			HtmlInput smscodeinput = page.getFirstByXPath("//*[@id='smsRandCode']");
//			// smscodeinput.s
//			// smscodeinput.setValueAttribute(code);
//			Page unexpectedPage = buttonclienk2.click();
//
//			System.out.println(unexpectedPage.getWebResponse().getContentAsString());
//
//			/*
//			 * HtmlInput smscode =
//			 * page.getFirstByXPath("//*[@id='smsRandCode']");
//			 * smscode.setValueAttribute(code);
//			 * 
//			 * HtmlElement buttonclienk2 =
//			 * page.getFirstByXPath("//*[@class='ued-button']");
//			 * System.out.println(buttonclienk2.asXml()); Page page2 =
//			 * buttonclienk2.click();
//			 * System.out.println(page2.getWebResponse().getContentAsString());
//			 * System.out.println(buttonclienk2.asXml()); Page unexpectedPage =
//			 * buttonclienk2.click(); Thread.sleep(5000); System.out.println(
//			 * "==================================================");
//			 * 
//			 * System.out.println(unexpectedPage.getWebResponse().
//			 * getContentAsString());
//			 */
//
//			// calltherm2(webClient);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void selemunit(WebClient webClient) {
//		/*
//		 * DesiredCapabilities dcaps = new DesiredCapabilities(); //ssl证书支持
//		 * dcaps.setCapability("acceptSslCerts", true); //截屏支持
//		 * dcaps.setCapability("takesScreenshot", true); //css搜索支持
//		 * dcaps.setCapability("cssSelectorsEnabled", true); //js支持
//		 * dcaps.setJavascriptEnabled(true); //驱动支持
//		 * dcaps.setCapability(PhantomJSDriverService.
//		 * PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//		 * "C:\\Python34\\Scripts\\phantomjs.exe"); //创建无界面浏览器对象 PhantomJSDriver
//		 * driver = new PhantomJSDriver(dcaps);
//		 */
//
//		WebDriver driver = new HtmlUnitDriver(true);
//		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//		for (Cookie cookie : cookies) {
//			org.openqa.selenium.Cookie cookiesele = new org.openqa.selenium.Cookie(cookie.getDomain(), cookie.getName(),
//					cookie.getValue());
//			driver.manage().addCookie(cookiesele);
//		}
//
//		driver.get("http://www.189.cn/dqmh/my189/initMy189home.do");
//		System.out.println(driver.getPageSource());
//	}
//
//	public static WebClient setSMSCoide(WebClient webClient, String code) {
//
//		String url = "http://bj.189.cn/iframe/feequery/detailValidCode.action";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("accNum", "13366777357"));
//		paramsList.add(new NameValuePair("requestFlag", "asynchronism"));
//		paramsList.add(new NameValuePair("randCode", code));
//
//		try {
//
//			webClient.setJavaScriptTimeout(20000);
//
//			webClient.getOptions().setTimeout(20000); // 15->60
//
//			Page page = gethtmlPost(webClient, paramsList, url);
//			System.out.println("===========setSMSCoide==============" + page.getWebResponse().getContentAsString());
//			return webClient;
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {
//
//		try {
//			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//			if (paramsList != null) {
//				webRequest.setRequestParameters(paramsList);
//			}
//			Page searchPage = webClient.getPage(webRequest);
//			return searchPage;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	public static Page getHtml(String url, WebClient webClient) throws Exception {
//
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		webClient.setJavaScriptTimeout(20000);
//
//		webClient.getOptions().setTimeout(20000); // 15->60
//		
//		Page searchPage = webClient.getPage(webRequest);
//		return searchPage;
//
//	}
//	
//	public static Page getHtml2(String url, WebClient webClient) throws Exception {
//
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		webClient.setJavaScriptTimeout(20000);
//
//		webClient.getOptions().setTimeout(20000); // 15->60
//		
//		webRequest.setAdditionalHeader("Accept", "text/html, */*; q=0.01");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,fr;q=0.6");
//		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		//requestSettings.setAdditionalHeader("Content-Length", "120");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		webRequest.setAdditionalHeader("Host", "bj.189.cn");
//		webRequest.setAdditionalHeader("Origin", "http://bj.189.cn");
//		webRequest.setAdditionalHeader("Pragma", "no-cache");  
//		webRequest.setAdditionalHeader("Referer", "http://bj.189.cn/iframe/feequery/detailBillIndex.action?fastcode=01390638&cityCode=bj");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//		
//		Page searchPage = webClient.getPage(webRequest);
//		return searchPage;
//
//	}
//
//	public static String getSMSCode(WebClient webClient) {
//		String url = "http://bj.189.cn/iframe/feequery/smsRandCodeSend.action";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("accNum", "13366777357"));
//
//		try {
//
//			webClient.setJavaScriptTimeout(20000);
//
//			webClient.getOptions().setTimeout(20000); // 15->60
//
//			Page page = gethtmlPost(webClient, paramsList, url);
//
//			SMSCodejson jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(), SMSCodejson.class);
//
//			return jsonObject.getSRandomCode();
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static String getcodebySring(String json) {
//
//		SMSCodejson jsonObject = gs.fromJson(json, SMSCodejson.class);
//
//		return jsonObject.getSRandomCode();
//	}
//
//	@SuppressWarnings("unused")
//	private static void addCookies(WebClient webClient, Set<Cookie> cookies) {
//
//		for (Cookie cookie : cookies) {
//			if (StringUtils.isEmpty(cookie.getName())) {
//				System.out.println("详情：" + cookie.getName() + ":" + cookie.getValue());
//				continue;
//			}
//			webClient.getCookieManager().addCookie(cookie);
//		}
//	}
//
//}
