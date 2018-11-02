package com.test;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanPayResult;
import com.module.htmlunit.WebCrawler;

import app.bean.TelecomHaiNanUserIdBean;
import app.bean.WebParamTelecom;
import app.crawler.telecom.htmlparse.TelecomParseHaiNan;
import app.unit.TeleComCommonUnit;

public class hainantest {
	static String cookiefile = "C:\\Users\\Administrator\\Desktop\\tel2.xls";

	public static void main(String[] args) throws Exception {
		try {

			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = loginHaiNan(webClient);
			//webClient = ready(webClient);
			// webClient.getOptions().setJavaScriptEnabled(true);
			// webClient.getOptions().setJavaScriptEnabled(false);
			/*
			 * WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			 * webClient = POIUnit.addCookie(webClient, cookiefile,
			 * "www.hi.189.cn");
			 */
			/*
			 * String url = "http://www.189.cn/dqmh/my189/initMy189home.do";
			 * List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			 * paramsList.add(new NameValuePair("fastcode", "0213")); Page page
			 * = TeleComCommonUnit.gethtmlPost(webClient2, paramsList, url);
			 */
			//readyGetUserId(webClient);

		/*	getPhoneCode(webClient);
			String valicodeStr = JOptionPane.showInputDialog("请输入短信验证码：");

			getSMSThrem(webClient, valicodeStr);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static WebClient ready(WebClient webClient) throws Exception {
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02091576";
		// webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("===" + cookie.toString());
		}
		return webClient;
	}

	public static TelecomHaiNanUserIdBean readyGetUserId(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi";
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());

		String value = doc.select("select#sel_usernum").select("option").attr("value");
		TelecomHaiNanUserIdBean telecomHaiNanUserIdBean = new TelecomHaiNanUserIdBean();
		telecomHaiNanUserIdBean.setPhonetype(value.split("\\|")[0]);
		telecomHaiNanUserIdBean.setPhone(value.split("\\|")[1]);
		telecomHaiNanUserIdBean.setWeizhiphonetype(value.split("\\|")[2]);
		telecomHaiNanUserIdBean.setWeizhi2(value.split("\\|")[3]);
		telecomHaiNanUserIdBean.setWeizhi3(value.split("\\|")[4]);
		telecomHaiNanUserIdBean.setCanshu(value.split("\\|")[5]);

		Pattern pt = Pattern.compile("userid=.*?;");
		Matcher match = pt.matcher(doc.toString());
		while (match.find()) {
			telecomHaiNanUserIdBean.setUserid(match.group().replaceAll("[^0-9]", "").trim());
			break;
		}
		System.out.println(telecomHaiNanUserIdBean.toString());
		return telecomHaiNanUserIdBean;
	}

	public static void getPhoneCode(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/BUFFALO/buffalo/CommonAjaxService";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Referer",
				"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		webRequest.setRequestBody("<buffalo-call>" + "<method>getSmsCode</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>PHONENUM</string>" + "<string>17340650319</string>"
				+ "<string>PRODUCTID</string>" + "<string>50</string>" + "<string>RTYPE</string>"
				+ "<string>QD</string>" + "</map>" + "</buffalo-call>");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

	}

	public static void getbillThrem(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/webgo/thesame/billing?objectNum=17340650319&queryMonth=201709";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

	}

	public static void getPayThrem(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/webgo/thesame/rechargeRecord?objectNum=17340650319&queryMonth=201709";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		// System.out.println("==========" +
		// page.getWebResponse().getContentAsString());

		WebParamTelecom<TelecomHaiNanPayResult> webParamTelecom = TelecomParseHaiNan
				.payResult_parse(page.getWebResponse().getContentAsString());
		List<TelecomHaiNanPayResult> list = webParamTelecom.getList();
		for (TelecomHaiNanPayResult result : list) {
			System.out.println("===" + result.toString());
		}
	}

	public static void getUserInfo(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/service/account/service_kh_xx.jsp?fastcode=0212&cityCode=hi";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.getOptions().setJavaScriptEnabled(false);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

		TelecomParseHaiNan.userinfo_parse(page.getWebResponse().getContentAsString());
	}

	public static void getBalance(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/webgo/thesame/balanceChanges?objectNum=17340650319&queryMonth=201709";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

	}

	public static void getCallThrem(WebClient webClient, String valicodeStr) throws Exception {
		String url = "http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Referer",
				"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		webRequest.setRequestBody("<buffalo-call>" + "<method>queryDetailBill</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>PRODNUM</string>"
				+ "<string>aWTUafTzjWLbzgHrGZxTLQ==</string>" + "<string>CITYCODE</string>" + "<string>0898</string>"
				+ "<string>QRYDATE</string>" + "<string>201709</string>" + "<string>TYPE</string>"
				+ "<string>8</string>" + "<string>PRODUCTID</string>" + "<string>50</string>" + "<string>CODE</string>"
				+ "<string>" + valicodeStr.trim() + "</string>" + "<string>USERID</string>"
				+ "<string>10356127</string>" + "</map>" + "</buffalo-call>");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

	}
	
	public static void getSMSThrem(WebClient webClient, String valicodeStr) throws Exception {
		String url = "http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Referer",
				"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		webRequest.setRequestBody("<buffalo-call>" + "<method>queryDetailBill</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>PRODNUM</string>"
				+ "<string>aWTUafTzjWLbzgHrGZxTLQ==</string>" + "<string>CITYCODE</string>" + "<string>0898</string>"
				+ "<string>QRYDATE</string>" + "<string>201709</string>" + "<string>TYPE</string>"
				+ "<string>6</string>" + "<string>PRODUCTID</string>" + "<string>50</string>" + "<string>CODE</string>"
				+ "<string>" + valicodeStr.trim() + "</string>" + "<string>USERID</string>"
				+ "<string>10356127</string>" + "</map>" + "</buffalo-call>");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

	}

	public static void getBussesThrem(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/ServiceOrderAjax.do?method=setEsuringIn&number=aWTUafTzjWLbzgHrGZxTLQ%3D%3D&phonetype=50";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		/*
		 * webRequest.setAdditionalHeader("Referer",
		 * "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		 * webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		 * webRequest.setAdditionalHeader("Pragma", "no-cache");
		 * webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		 * 
		 * webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		 * webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		 * webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		 * webRequest.setAdditionalHeader("Connection", "keep-alive");
		 * //webRequest.setAdditionalHeader("Content-Length", "160");
		 * webRequest.setAdditionalHeader("Content-Type",
		 * "text/xml;charset=UTF-8"); webRequest.setRequestBody("<buffalo-call>"
		 * + "<method>integralHistoryQuery</method>" + "<map>" +
		 * "<type>java.util.HashMap</type>" + "<string>MONTH</string>" +
		 * "<string>201708</string>" + "</map>" + "</buffalo-call>");
		 */
		// webRequest.setAdditionalHeader("Accept", "*/*");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("===" + cookie.toString());
		}
	}

	public static void getintegralThrem(WebClient webClient) throws Exception {
		String url = "http://www.hi.189.cn/BUFFALO/buffalo/IntegralQueryAjax";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");
		webRequest.setRequestBody(
				"<buffalo-call>" + "<method>integralHistoryQuery</method>" + "<map>" + "<type>java.util.HashMap</type>"
						+ "<string>MONTH</string>" + "<string>201708</string>" + "</map>" + "</buffalo-call>");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		System.out.println("==========" + page.getWebResponse().getContentAsString());

		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("===" + cookie.toString());
		}
	}

	public static WebClient loginHaiNan(WebClient webClient) throws Exception {
		// String url =
		// "https://uam.ct10000.com/ct10000uam/login?service=http%3A%2F%2Fwww.189.cn%3A80%2Flogin%2Fuam.do&returnURL=1&register=register2.0&UserIp=123.126.87.163";
		webClient.setJavaScriptTimeout(10000);
		webClient.getOptions().setTimeout(10000); // 15->60
		webClient.waitForBackgroundJavaScript(10000);
		 webClient.getOptions().setRedirectEnabled(true);  
		
		InetAddress ia = null;
		try {
			ia = ia.getLocalHost();

			String localname = ia.getHostName();
			String localip = ia.getHostAddress();
			System.out.println("本机名称是：" + localname);
			System.out.println("本机的ip是 ：" + localip);
			String url = "https://uam.ct10000.com/ct10000uam/login?service=http%3A%2F%2Fwww.189.cn%3A80%2Flogin%2Fuam.do&returnURL=1&register=register2.0&UserIp="
					+ localip.trim();
			HtmlPage htmlPage = (HtmlPage) TeleComCommonUnit.getHtml(url, webClient);
			System.out.println("---------------------");
			System.out.println(htmlPage.asXml());
			System.out.println("---------------------");
			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='checkImg']");
			ImageReader imageReader = valiCodeImg.getImageReader();
			BufferedImage bufferedImage = imageReader.read(0);

			JFrame f2 = new JFrame();
			JLabel l = new JLabel();
			l.setIcon(new ImageIcon(bufferedImage));
			f2.getContentPane().add(l);
			f2.setSize(100, 100);
			f2.setTitle("验证码");
			f2.setVisible(true);

			String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

			/*
			 * HtmlTextInput nameInput = (HtmlTextInput)
			 * htmlPage.getFirstByXPath("//*[@id='username']");
			 * HtmlPasswordInput passInput =
			 * htmlPage.getFirstByXPath("//*[@id='password']"); HtmlTextInput
			 * codeInput = (HtmlTextInput)
			 * htmlPage.getFirstByXPath("//*[@id='randomId']");
			 * nameInput.setText("17340650319"); passInput.setText("211314");
			 * codeInput.setText(valicodeStr.trim());
			 * 
			 * HtmlElement button = (HtmlElement) htmlPage .getFirstByXPath(
			 * "//*[@id='c2000004']/table/tbody/tr[7]/td[2]/input[3]"); htmlPage
			 * = button.click(); System.out.println("===" + htmlPage.asXml());
			 */

			Document doc = Jsoup.parse(htmlPage.asXml());
			String lt = doc.select("input[name=lt]").attr("value");
			System.out.println("lt============" + lt);

			url = "https://uam.ct10000.com/ct10000uam/login?service=http%3A%2F%2Fwww.189.cn%3A80%2Flogin%2Fuam.do&returnURL=1&register=register2.0"
					+ "&UserIp=" + localip.trim();
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("forbidpass", "null"));
			paramsList.add(new NameValuePair("forbidaccounts", "null"));
			paramsList.add(new NameValuePair("authtype", "c2000004"));
			paramsList.add(new NameValuePair("customFileld02", "22"));
			paramsList.add(new NameValuePair("areaname", "海南"));
			paramsList.add(new NameValuePair("username", "17340650319"));
			paramsList.add(new NameValuePair("customFileld01", "1"));
			paramsList.add(new NameValuePair("password", "211314"));
			paramsList.add(new NameValuePair("randomId", valicodeStr.trim()));
			paramsList.add(new NameValuePair("c2000004RmbMe", "on"));
			paramsList.add(new NameValuePair("lt", lt));
			paramsList.add(new NameValuePair("_eventId", "submit"));
			paramsList.add(new NameValuePair("open_no", "1"));
			Page page = TelecomLogin2.gethtmlPost(webClient, paramsList, url);
			System.out.println("---------------------------------------------------");
			System.out.println("==" + page.getWebResponse().getContentAsString());
			System.out.println("---------------------------------------------------");
			url = "http://www.189.cn/login/index.do";
			// webClient.getOptions().setRedirectEnabled(false);

			page = TelecomLogin2.getHtml(url, webClient);
			System.out.println("==" + page.getWebResponse().getContentAsString());
			url = "http://www.189.cn/dqmh/order/getHuaFei.do";
			page = TelecomLogin2.gethtmlPost(webClient, null, url);
			System.out.println("==" + page.getWebResponse().getContentAsString());
	

			return webClient;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}