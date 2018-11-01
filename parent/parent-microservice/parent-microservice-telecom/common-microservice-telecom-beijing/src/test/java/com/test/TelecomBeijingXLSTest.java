package com.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

import app.bean.error.ErrorException;
import app.service.SMSCodejson;

public class TelecomBeijingXLSTest {

	// private static Set<Cookie> cookies;
	private static Workbook wb;
	private static Sheet sheet;
	private static Row row;

	protected static Gson gs = new Gson();

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		InputStream is = new FileInputStream("C:/Users/Administrator/Desktop/tel.xls");
		wb = new HSSFWorkbook(is);
		System.out.println("list中的数据打印出来");
		sheet = wb.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("rowNum" + rowNum);
		System.out.println("colNum" + colNum);

		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 0; i <= rowNum; i++) {
			row = sheet.getRow(i);

			System.out.println(i + ":" + "" + row.getCell(0) + ":::::::" + row.getCell(1));
			webClient.getCookieManager().addCookie(new Cookie("bj.189.cn", row.getCell(0) + "", row.getCell(1) + ""));
		}
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		
		
		String url = "http://bj.189.cn/service/account/customerHome.action?PlatNO=90000&Ticket=900002052446f49cdd73af35403b9ece9f1b1c03f9b1&TxID=100362018091414205235ada3d0b5e9a445e7832c251fd3d619&SSOURL=http%3A%2F%2Fbj.189.cn%2Fiframe%2Ffeequery%2FdetailBillIndex.action%3Ffastcode%3D01390638%26cityCode%3Dbj";

		
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "bj.189.cn");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");

		Page searchPage = webClient.getPage(webRequest);
		
		webClient = searchPage.getEnclosingWindow().getWebClient();
		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		
		for(Cookie cookie   :  cookies){
			System.out.println("cookie:::::::"+cookie.getName()+"=============="+cookie.getValue());
		}
		
		String smsCode = getSMSCode(webClient);
		
		verifySms(webClient, smsCode);
		
		getCallThrem(webClient);
		
		System.out.println("smsCod==========="+smsCode);

	}

	public static String getCallThrem(WebClient webClient) {

		String url = "http://bj.189.cn/iframe/feequery/billDetailQuery.action?"
				+ "requestFlag=synchronization&billDetailType=1"
				+ "&qryMonth=2018%E5%B9%B409%E6%9C%88"
				+ "&startTime=1"
				+ "&accNum=13366777357"
				+ "&endTime=14";

		System.out.println("请求的url  :" + url);

		// log.info("请求的url :" + url);

		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = getHtmlForCookie(url, webClient);

			String html = page.getWebResponse().getContentAsString();
			
			System.out.println("====================================================================");
			
			System.out.println(html);

			
			System.out.println("====================================================================");


			if (html.indexOf("系统正忙，请稍后再试") != -1) {
				// if (i == 0) {
				// i++;
				// html = getCallThrem(webClient, messageLogin, taskMobile,
				// stardate, enddate, month, pagnum, i);
				// }
				throw new ErrorException("系统正忙，请稍后再试");

			}
			return html;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}
	
	public static String getSMSCode(WebClient webClient) {
		String url = "http://bj.189.cn/iframe/feequery/smsRandCodeSend.action?accNum="
				+ "13366777357";

		try {

			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "bj.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");

			Page page = webClient.getPage(webRequest);

//			log.info("======" + page.getWebResponse().getContentAsString());

			SMSCodejson jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(), SMSCodejson.class);

			return jsonObject.getSRandomCode();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}
	
	public static Page verifySms(WebClient webClient,String  smsCode){
		String url = "http://bj.189.cn/iframe/feequery/detailValidCode.action?requestFlag=asynchronism"
				+ "&accNum="+"13366777357"
				+ "&randCode="+smsCode;

		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60
			
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);

			System.out.println("'''''''''"+page.getWebResponse().getContentAsString());
			
			return page;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public static Page getHtmlForCookie(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			webRequest = new WebRequest(new URL(url), HttpMethod.GET);

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "bj.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");

			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}