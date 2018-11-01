package app.service.common;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;

public class LoginAndGetSiChuan {

	public static WebParamTelecom getphonecode(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TelecomCommonUnit.addcookie(webClient, taskMobile);
		String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000326";
		String url1 = "http://sc.189.cn/service/billDetail/qryProdItvInfoAjax.jsp";
		TelecomCommonUnit.gethtmlPost(webClient, null, url2);
		TelecomCommonUnit.gethtmlPost(webClient, null, url1);
		try {
			
			
			LocalDate today = LocalDate.now();
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1);
			LocalDate enddate = today;
			String url = "http://sc.189.cn/service/billDetail/sendSMSAjax.jsp"
					+ "?dateTime1="+stardate.toString()
					+ "&dateTime2="+enddate.toString();
			
			taskMobile.setTrianNum(0);
//			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//			paramsList = new ArrayList<NameValuePair>();
//			paramsList.add(new NameValuePair("dateTime1", stardate.toString()));
//			paramsList.add(new NameValuePair("dateTime2", enddate.toString()));
			
			Page page = TelecomCommonUnit.getHtml(url, webClient);
			WebParamTelecom  webParamTelecom = new WebParamTelecom();
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			System.out.println(page.getWebResponse().getContentAsString());
			return webParamTelecom;
			} catch (Exception e) {

				taskMobile.setTrianNum(taskMobile.getTrianNum() + 1);
				if (taskMobile.getTrianNum() > 2) {
					return null;
				}
				return getphonecode(messageLogin, taskMobile);
			}
		
	}
	

	public static WebParamTelecom setphonecode(MessageLogin messageLogin, TaskMobile taskMobile){
		String str = messageLogin.getSms_code();
		byte[] encodeBase64;
		try {
			encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));
		
		LocalDate today = LocalDate.now();
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1);
		LocalDate enddate = today;
		String url = "http://sc.189.cn/service/billDetail/detailQuery.jsp";
//				+ "startTime="+stardate.toString()
//				+ "&endTime="+enddate.toString()
//				+ "&qryType=21"
//				+ "&randomCode="+new String(encodeBase64);
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TelecomCommonUnit.addcookie(webClient, taskMobile);
		System.out.println(new String(encodeBase64));
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("startTime", stardate.toString()));
		paramsList.add(new NameValuePair("endTime", enddate.toString()));
		paramsList.add(new NameValuePair("qryType", "21"));
		paramsList.add(new NameValuePair("randomCode", new String(encodeBase64)));
		url = "http://sc.189.cn/service/billDetail/detailQuery.jsp";
		
		try {
			Page page = TelecomCommonUnit.gethtmlPost(webClient, paramsList, url);
			WebParamTelecom  webParamTelecom = new WebParamTelecom();
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			String html = webParamTelecom.getHtml();
			System.out.println(html);
			return webParamTelecom;
		} catch (Exception e) {
			taskMobile.setTrianNum(taskMobile.getTrianNum() + 1);
			if (taskMobile.getTrianNum() > 2) {
				return null;
			}
			return setphonecode(messageLogin, taskMobile);
		}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
	}
}
