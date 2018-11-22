package com.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

import javax.swing.JOptionPane;

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
import app.unit.TeleComCommonUnit;

public class hainanXLSTest {

	// private static Set<Cookie> cookies;
	private static Workbook wb;
	private static Sheet sheet;
	private static Row row;

	protected static Gson gs = new Gson();
	
	private static WebClient webClient;

	public static void main(String[] args) throws Exception {

		webClient = WebCrawler.getInstance().getNewWebClient();

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
			webClient.getCookieManager().addCookie(new Cookie("hi.189.cn", row.getCell(0) + "", row.getCell(1) + ""));
		}
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);

		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();

		for (Cookie cookie : cookies) {
			System.out.println("cookie:::::::" + cookie.getName() + "==============" + cookie.getValue());
		}
		
		String smsCode = JOptionPane.showInputDialog("请输入短信验证码：");
		
		verifySms(smsCode);


	}
	public static void verifySms(String smsCode) throws Exception {
		// String html_UserId = null;

		// try {
		// html_UserId = readyGetUserId(webClient);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// throw new ErrorException("获取关键字错误");
		// }
		webClient.getOptions().setJavaScriptEnabled(true);

		LocalDate today = LocalDate.now();

		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-0);

		String month = enddate.getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}

		String stardate = today.getYear() + month;
//		setphonecodeRead(telecomHaiNanUserIdBean, webClient);
		setphonecode(smsCode, stardate);
		// 手机验证码验证成功状态更新

	}

	public static void setphonecode(String smsCode,
			 String stardate) throws Exception {
		// System.out.println(telecomHaiNanUserIdBean.toString());

		String url = "http://hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";
		// http://hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer",
				"http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "hi.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");
	

		String canshuString = "<buffalo-call><method>queryDetailBill</method><map><type>java.util.HashMap</type><string>PRODNUM</string>"
				+ "<string>zU+rpA6loB/OyLX9h1JL7g==</string><string>CITYCODE</string><string>0898</string><string>QRYDATE</string>"
				+ "<string>201811</string><string>TYPE</string><string>8</string><string>PRODUCTID</string><string>50</string>"
				+ "<string>CODE</string><string>"
				+ smsCode
				+ "</string><string>USERID</string><string>2751219233</string>"
				+ "</map></buffalo-call>";
		webRequest.setRequestBody(canshuString);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		System.out.println(page.getWebResponse().getContentAsString());
//		System.out.println("telecomHaiNanUserIdBean====="+telecomHaiNanUserIdBean.toString());

			}


}