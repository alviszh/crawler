package test.webdriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class ListTest2 {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx"; 
		System.out.println(url); 
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);  
		requestSettings.setAdditionalHeader("Host", "pbsz.ebank.cmbchina.com");
		requestSettings.setAdditionalHeader("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx");
		requestSettings.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		requestSettings.setAdditionalHeader("DNT", "1");
		
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","WEBTRENDS_ID", "123.126.87.162-2358226944.30605561"));
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","WTFPC", "id=21571e0a30ffd7b98361501640904310:lv=1501663951212:ss=1501662426634"));
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","AuthType", "A"));
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","DeviceType", "A"));
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","cookie5", "ProVersion="));
		//webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","ClientStamp", "5287890619731139481"));
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		//requestSettings.getRequestParameters().add(new NameValuePair("__EVENTTARGET", ""));
		//requestSettings.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", ""));
		//requestSettings.getRequestParameters().add(new NameValuePair("__LASTFOCUS", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("__VIEWSTATE", "/wEPDwULLTExNTMyNDIzNjAPZBYCAgMPZBYOAgEPDxYCHgRUZXh0BRvotKbmiLfnrqHnkIYgPiDkuqTmmJPmn6Xor6JkZAIDDxYCHgdWaXNpYmxlaGQCBQ8QZBAVARA2MjE0KioqKioqKiowOTI1FQEgemp2enNpU1ZaUlo0VTE2M0FnVzBrQlhGRnR5M0JpRnMUKwMBZxYBZmQCBw8PFgIfAWhkZAIIDw8WAh8BZ2RkAgoPFgIfAWcWCAIBDxBkEBUBISDmtLvmnJ/nu5PnrpfmiLcg5Lq65rCR5biBIDAwMDAxIBUBDzExMDE2Nzc5MzIwMDAwMRQrAwFnZGQCAw8QDxYGHg5EYXRhVmFsdWVGaWVsZAUDS2V5Hg1EYXRhVGV4dEZpZWxkBQVWYWx1ZR4LXyFEYXRhQm91bmRnZBAVAQpbIOWFqOmDqCBdFQEBLRQrAwFnZGQCBg8WAh4Hb25jbGljawUVb3BlbldpbkJlZ2luRGF0ZVNlbCgpZAIJDxYCHwUFE29wZW5XaW5FbmREYXRlU2VsKClkAgwPFgIfAWgWAgIND2QWAmYPZBYCAgEPZBYCAgEPZBYKZg8PFgQeB0VuYWJsZWRoHwFoZGQCAg8PFgQfBmgfAWhkZAIEDxYCHgV2YWx1ZWRkAggPFgIfBwUDVFdPZAIKDxYCHwdkZGQGJa5ytIDkMriKL78CKhe8L/P+FeJUi/mNg8XxYjgL0g=="));
		//requestSettings.getRequestParameters().add(new NameValuePair("__VIEWSTATEGENERATOR", "8AC6C0C9"));
		requestSettings.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", "/wEdAAsIRp6c9qnAGXpwuVZlFCXkaEksz56re1rlLgaE+fM9Ta9X8We/Yw8PXsrJgATSBg/ST6BJFFpZBXKDkBSgl918RsdVDJL1qhvSh9ZIHdmIF48AnXhcTpyLSyBvYOwOqDp2eGSWqUicHxX82QHVe0KPLlG32GeoxmD12OTErum1P0OoJQU9TjnXlz3rc6YAAZQSdzwtdCmj5Tc1lqiFgtGtSUNVW2k8lejNNxj/NrmbPoiUZ86NslPawodQie+rojijAUyjAONxOLE97u3XM7P3"));
		//requestSettings.getRequestParameters().add(new NameValuePair("ddlDebitCardList", "zjvzsiSVZRZ4U163AgW0kBXFFty3BiFs"));
		//requestSettings.getRequestParameters().add(new NameValuePair("ddlSubAccountList", "110167793200001"));
		//requestSettings.getRequestParameters().add(new NameValuePair("ddlTransTypeList", "-"));
		requestSettings.getRequestParameters().add(new NameValuePair("BeginDate", "20140808"));
		requestSettings.getRequestParameters().add(new NameValuePair("EndDate", "20170802"));
		requestSettings.getRequestParameters().add(new NameValuePair("BtnOK", "²é Ñ¯"));
		//requestSettings.getRequestParameters().add(new NameValuePair("uc_Adv$AdvLocID", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("ClientNo", "DB6C29CBB7F7974F693012F3A2713ED9498490583047304700004807"));
		//requestSettings.getRequestParameters().add(new NameValuePair("PanelControl", ""));
		
		
		Page page = webClient.getPage(requestSettings);  
		String html = page.getWebResponse().getContentAsString();  
		System.out.println("html ====¡·¡·"+html);		
		
		
		
		
		
		
		
		
		
		

	}

}
