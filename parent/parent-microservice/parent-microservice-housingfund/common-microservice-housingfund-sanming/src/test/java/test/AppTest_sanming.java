package test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class AppTest_sanming {
	public static void main(String[] args) {
		
		try {
			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			//获取登录请求的入参
			String loginUrl0 = "http://www.smgjj.com/MemberLogin.aspx";
			WebRequest webRequest0 = new WebRequest(new URL(loginUrl0), HttpMethod.GET);
			Page page0 = webClient.getPage(webRequest0);
			String contentAsString0 = page0.getWebResponse().getContentAsString();
            Document doc = Jsoup.parse(contentAsString0);
            System.out.println(contentAsString0);
            String form1 = doc.getElementsByAttributeValue("name", "form1").val();
            System.out.println("form1:"+form1);
            String __EVENTTARGET = doc.getElementById("__EVENTTARGET").val();
            System.out.println("__EVENTTARGET:"+__EVENTTARGET);
            String __EVENTARGUMENT = doc.getElementById("__EVENTARGUMENT").val();
            System.out.println("__EVENTARGUMENT:"+__EVENTARGUMENT);
            String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
            System.out.println("__VIEWSTATE:"+__VIEWSTATE);
            String __VIEWSTATEENCRYPTED = doc.getElementById("__VIEWSTATEENCRYPTED").val();
            System.out.println("__VIEWSTATEENCRYPTED:"+__VIEWSTATEENCRYPTED);
            
            // 图片请求
            String loginurl3 = "http://www.smgjj.com/CheckCode.aspx";
            WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.GET);
            HtmlPage html = webClient.getPage(requestSettings1);
            // 图片
            HtmlImage randImage = (HtmlImage) html.getFirstByXPath("//*[@id=\"form1\"]/img");
            String imageName = "111.jpg";
            File file = new File("D:\\img\\" + imageName);
            randImage.saveAs(file);
            // 验证登录信息的链接：
            String code = JOptionPane.showInputDialog("请输入验证码……");
            
			// 登录请求
			String loginUrl = "http://www.smgjj.com/MemberLogin.aspx";
			WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("form1", form1));
			webRequest.getRequestParameters().add(new NameValuePair("__EVENTTARGET", __EVENTTARGET));
			webRequest.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT));
			webRequest.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
			webRequest.getRequestParameters().add(new NameValuePair("__VIEWSTATEENCRYPTED", __VIEWSTATEENCRYPTED));
			webRequest.getRequestParameters().add(new NameValuePair("txtUUKey", "362323199404066512"));
			webRequest.getRequestParameters().add(new NameValuePair("txtMMKey", "666666"));
			webRequest.getRequestParameters().add(new NameValuePair("keypwd", ""));
			webRequest.getRequestParameters().add(new NameValuePair("txtValidate", code));
			webRequest.getRequestParameters().add(new NameValuePair("btnSummit", "登 录"));
			webRequest.getRequestParameters().add(new NameValuePair("Serial_ID", ""));
			webRequest.getRequestParameters().add(new NameValuePair("Digest", ""));
			webRequest.getRequestParameters().add(new NameValuePair("random1", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlsthzdw", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlstzfbm", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlstqtgjj", ""));
			webRequest.getRequestParameters().add(new NameValuePair("HomePageBottomInfo1$dlstqtwz", ""));
			Page page = webClient.getPage(webRequest);
			String contentAsString = page.getWebResponse().getContentAsString();
            System.out.println(contentAsString);
            if (contentAsString.contains("三明市住房公积金后台管理系统")) {
				System.out.println("登陆成功！");
				
				// 流水入参的请求
				String jcmx = "http://www.smgjj.com/MemberFunction/PersonFunction/PersonAcfSerach.aspx?PageType=Add&SiteTicket=";
				WebRequest requestSettings12 = new WebRequest(new URL(jcmx), HttpMethod.GET);
				Page page1 = webClient.getPage(requestSettings12);
				String contentAsString1 = page1.getWebResponse().getContentAsString();
				
				Document doc1 = Jsoup.parse(contentAsString1);
				String form11 = doc1.getElementsByAttributeValue("name", "form1").get(0).val().trim();
//				String __EVENTTARGET = doc1.getElementById("__EVENTTARGET").val();
				String __EVENTARGUMENT1 = doc1.getElementById("__EVENTARGUMENT").val();
				String __VIEWSTATE1 = doc1.getElementById("__VIEWSTATE").val();
				String __VIEWSTATEENCRYPTED1 = doc1.getElementById("__VIEWSTATEENCRYPTED").val();
				String __EVENTVALIDATION = doc1.getElementById("__EVENTVALIDATION").val();
				
				System.out.println("form11:"+form11);
				System.out.println("__EVENTARGUMENT1:"+__EVENTARGUMENT1);
				System.out.println("__VIEWSTATE1:"+__VIEWSTATE1);
				System.out.println("__VIEWSTATEENCRYPTED1:"+__VIEWSTATEENCRYPTED1);
				System.out.println("__EVENTVALIDATION:"+__EVENTVALIDATION);
				
				// 流水1入参的请求
				String jcmx1 = "http://www.smgjj.com/MemberFunction/PersonFunction/PersonAcfSerach.aspx?PageType=Add&SiteTicket=";
				WebRequest requestSettings11 = new WebRequest(new URL(jcmx1), HttpMethod.POST);
				requestSettings11.setRequestParameters(new ArrayList<NameValuePair>());
				requestSettings11.getRequestParameters().add(new NameValuePair("form1", form11));
				requestSettings11.getRequestParameters().add(new NameValuePair("__EVENTTARGET", "dlZhangHao$ctl01$lblmore"));
				requestSettings11.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT1));
				requestSettings11.getRequestParameters().add(new NameValuePair("__VIEWSTATE", __VIEWSTATE1));
				requestSettings11.getRequestParameters().add(new NameValuePair("__VIEWSTATEENCRYPTED", __VIEWSTATEENCRYPTED1));
				requestSettings11.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
				Page page11 = webClient.getPage(requestSettings11);
				String contentAsString12 = page11.getWebResponse().getContentAsString();
	            System.out.println(contentAsString12);
				
			} else {
				System.out.println("登陆失败！异常错误！");
			}
		} catch (Exception e) {
		}
	}
}
