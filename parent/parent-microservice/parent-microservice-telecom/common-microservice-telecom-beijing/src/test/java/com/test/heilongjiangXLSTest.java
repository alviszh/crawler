package com.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.bean.error.ErrorException;
import app.service.SMSCodejson;

public class heilongjiangXLSTest {

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
			webClient.getCookieManager().addCookie(new Cookie("hl.189.cn", row.getCell(0) + "", row.getCell(1) + ""));
		}
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);

		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();

		for (Cookie cookie : cookies) {
			System.out.println("cookie:::::::" + cookie.getName() + "==============" + cookie.getValue());
		}

//		WebParamTelecom webParamTelecom = getphonecode(webClient);
//
//		System.out.println("发送短信验证码" + webParamTelecom.toString());
//
//		String sms_code = JOptionPane.showInputDialog("请输入短信验证码：");
//
//		MessageLogin messageLogin = new MessageLogin();
//
//		messageLogin.setSms_code(sms_code.trim());
//
//		setphonecode(messageLogin, webClient);
//
//		getCallThemHtml(webClient);
	}

	public static WebParamTelecom getphonecode(WebClient webClient) {

		WebParamTelecom webParamTelecom = new WebParamTelecom<>();
		try {

			// String errorurl =
			// "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000846";
			// HtmlPage htmlpage_error = getHtml(errorurl, webClient);
			//
			// if
			// (htmlpage_error.getWebResponse().getContentAsString().indexOf("对不起，系统忙，请稍后再试")
			// != -1) {
			// webParamTelecom.setHtml(htmlpage_error.getWebResponse().getContentAsString());
			// webParamTelecom.setErrormessage("电信黑龙江网站出现问题，请稍后重试");
			// return webParamTelecom;
			// }
			// webClient = htmlpage_error.getWebClient();
			// //
			// String url =
			// "http://hl.189.cn/service/cqd/detailQueryCondition.do";
			// HtmlPage htmlpage = getHtml(url, webClient);
			String url = "http://hl.189.cn/service/userCheck.do?method=sendMsg";

			List<NameValuePair> paramsList = new ArrayList<>();
			paramsList.add(new NameValuePair("method", "sendMsg"));

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");

			webRequest.setAdditionalHeader("Host", "hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Origin", "http://hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Referer",
					"http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl");// 设置请求报文头里的refer字段

			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");// 设置请求报文头里的refer字段

			webClient.getOptions().setJavaScriptEnabled(false);

			Page page = webClient.getPage(webRequest);
			System.out.println(page.getWebResponse().getContentAsString());
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);

			System.out.println(page.getWebResponse().getContentAsString());
			System.out.println("==========================");
			if (webParamTelecom.getHtml().indexOf("对不起，系统忙，请稍后再试") != -1) {
				webParamTelecom.setErrormessage("电信黑龙江网站出现问题，请稍后重试");
			}
			return webParamTelecom;
		} catch (Exception e) {

			return webParamTelecom;
		}

	}

	public static WebParamTelecom setphonecode(MessageLogin messageLogin, WebClient webClient) {
		WebParamTelecom webParamTelecom = new WebParamTelecom();

		String url = "http://hl.189.cn/service/zzfw.do";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("method", "checkDX"));
		paramsList.add(new NameValuePair("yzm", messageLogin.getSms_code().trim()));

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");

			webRequest.setAdditionalHeader("Host", "hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Origin", "http://hl.189.cn");// 设置请求报文头里的refer字段
			webRequest.setAdditionalHeader("Referer",
					"http://hl.189.cn/service/zzfw.do?method=ywsl&id=10&fastcode=20000846&cityCode=hl");// 设置请求报文头里的refer字段

			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");// 设置请求报文头里的refer字段

			Page page = webClient.getPage(webRequest);
			System.out.println(page.getWebResponse().getContentAsString());
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			return webParamTelecom;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			webParamTelecom.setHtml(e.getMessage());
			webParamTelecom.setErrormessage(e.getMessage());
			return webParamTelecom;
		}

	}

	
	public static String getCallThemHtml(WebClient webClient) {
		String url = "http://hl.189.cn/service/cqd/queryDetailList.do?isMobile=0&seledType=9&queryType=&pageSize=10&pageNo=1&flag=&pflag=&accountNum=13351102145%3A2000004&callType=3&selectType=1&detailType=9&selectedDate=20188&method=queryCQDMain";

		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			System.out.println("==========================================");
			System.out.println(page.getWebResponse().getContentAsString());
			System.out.println("==========================================");

			return page.getWebResponse().getContentAsString();
		} catch (Exception e) {

			return null;
		}

	}
}