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

public class ListTest {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String url = "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx"; 
		System.out.println(url); 
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);  
		requestSettings.setAdditionalHeader("Host", "pbsz.ebank.cmbchina.com");
		requestSettings.setAdditionalHeader("Referer", "https://pbsz.ebank.cmbchina.com/CmbBank_DebitCard_AccountManager/UI/DebitCard/AccountQuery/am_QueryHistoryTrans.aspx");
		requestSettings.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		requestSettings.setAdditionalHeader("DNT", "1");
		
		webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","WEBTRENDS_ID", "123.126.87.162-2358226944.30605561"));
		webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","WTFPC", "id=21571e0a30ffd7b98361501640904310:lv=1501663951212:ss=1501662426634"));
		webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","AuthType", "A"));
		webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","DeviceType", "A"));
		webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","cookie5", "ProVersion="));
		webClient.getCookieManager().addCookie(new Cookie("cmbchina.com","ClientStamp", "5287890619731139481"));
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("__EVENTTARGET", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("__EVENTARGUMENT", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("__LASTFOCUS", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("__VIEWSTATE", "/wEPDwULLTExNTMyNDIzNjAPZBYCAgMPZBYOAgEPDxYCHgRUZXh0BRvotKbmiLfnrqHnkIYgPiDkuqTmmJPmn6Xor6JkZAIDDxYCHgdWaXNpYmxlaGQCBQ8QZBAVARA2MjE0KioqKioqKiowOTI1FQEgemp2enNpU1ZaUlo0VTE2M0FnVzBrQlhGRnR5M0JpRnMUKwMBZxYBZmQCBw8PFgIfAWhkZAIIDw8WAh8BZ2RkAgoPFgIfAWcWCAIBDxBkEBUBISDmtLvmnJ/nu5PnrpfmiLcg5Lq65rCR5biBIDAwMDAxIBUBDzExMDE2Nzc5MzIwMDAwMRQrAwFnZGQCAw8QDxYGHg5EYXRhVmFsdWVGaWVsZAUDS2V5Hg1EYXRhVGV4dEZpZWxkBQVWYWx1ZR4LXyFEYXRhQm91bmRnZBAVDwpbIOWFqOmDqCBdDOS7o+WPkeW3pei1hAbmrL7pobkP5pys6KGMQVRN5Y+W5qy+D+mTtuiBlEFUTeWPluasvhvpk7bogZTot6jooYzkuJrliqHmiYvnu63otLkh5ZCM5Z+O5Yet6K+B5Lmm6Z2i5oyC5aSx5omL57ut6LS5DOWuouaIt+i9rOi0pgzotKbmiLfnu5Pmga8P55u05LuY6YCa5Lqk5piTEumTtuiBlOa4oOmBk+i9rOWHugzooYzlhoXmsYflhaUM5rGH5YWl5rGH5qy+D+a3seWcs+aPkOWbnuWAnxXmlK/ku5jlrp3ovazotKbmj5DnjrAVDwEtBEFHUFkEQUdTSARDV0QxBENXRDIERkVBQQRGRUFRBElDUlIESUlOVARPTk5LBFBURk8EUlhUWQRSWVRCBFNEUlAEWTNaRhQrAw9nZ2dnZ2dnZ2dnZ2dnZ2dkZAIGDxYCHgdvbmNsaWNrBRVvcGVuV2luQmVnaW5EYXRlU2VsKClkAgkPFgIfBQUTb3BlbldpbkVuZERhdGVTZWwoKWQCDA8WAh8BZxYMAgEPDxYCHwAFEDYyMTQqKioqKioqKjA5MjVkZAIFDw8WAh8ABQMxOTdkZAIHDw8WAh8ABQI2MGRkAgkPDxYCHwAFCjMyMiw4NDcuMTFkZAILDw8WAh8ABQozMjIsNzIyLjQ4ZGQCDQ8WAh8BaBYCZg9kFgICAQ9kFgICAQ9kFgxmDw8WBB4HRW5hYmxlZGgfAWhkZAICDw8WBB8GaB8BaGRkAgQPFgIeBXZhbHVlBc0BMDAwNTBZMTAwMDFIMzExMDE2Nzc5MzIwMDAwMTIwMTcwNzA4MDAwMDAwMDAxMjAxNDA4MDgyMDE3MDgwMjExMEExMDAwMTAxKysyMDE3MDgwMjIwMTcwNjIxMDAwMDAwNTAwMDAwMjUwKysrKysrKysrK0wrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrKysrK2QCBg8WAh8HBQFOZAIIDxYCHwcFA1RXT2QCCg8WAh8HZGRkCmvPTXcv4DsjFkj/hA3NZSkHi5ivliB42Rr4FB521PY="));
		requestSettings.getRequestParameters().add(new NameValuePair("__VIEWSTATEGENERATOR", "8AC6C0C9"));
		requestSettings.getRequestParameters().add(new NameValuePair("__EVENTVALIDATION", "/wEdABkwbISxdxTaD+BnFq5zZucNaEksz56re1rlLgaE+fM9Ta9X8We/Yw8PXsrJgATSBg/ST6BJFFpZBXKDkBSgl918RsdVDJL1qhvSh9ZIHdmIF4pJLtOGz5XPCAYoKbr1/5Ldue7IT85aTXpUhmnCx+V9iOgRGCou1Z0RwkQh/0bdZAvBQvuJoeDCxkmpvJCU6aYCjKBEKEXJPyJ3c1J2QWVYtE/s34lYjkL8QkRzwUIP8ZJMo85CRdWn10y8nnI7+CqzaOyj3M3zC5lvwQecsWWaPePFVMxdkfOkogNkgE5ljpvznzDocNPPEN+2r5rhaDJbrJ29Gm9VL7zXQH5ZojOANYmAucW7oNIHh1UruWPB/8a1MVaodA4e7VcBJY9CMlqnz0eLUKtsA0AR0paasFzYjwCdeFxOnItLIG9g7A6oOnZ4ZJapSJwfFfzZAdV7Qo8uUbfYZ6jGYPXY5MSu6bU/Q6glBT1OOdeXPetzpgABlBJ3PC10KaPlNzWWqIWC0a1JQ1VbaTyV6M03GP82uZs+OyoNCaSmq/7flrMtdFBW+EGMsfD4GZ6URTVcDnvXQMs="));
		requestSettings.getRequestParameters().add(new NameValuePair("ddlDebitCardList", "zjvzsiSVZRZ4U163AgW0kBXFFty3BiFs"));
		requestSettings.getRequestParameters().add(new NameValuePair("ddlSubAccountList", "110167793200001"));
		requestSettings.getRequestParameters().add(new NameValuePair("ddlTransTypeList", "-"));
		requestSettings.getRequestParameters().add(new NameValuePair("BeginDate", "20140808"));
		requestSettings.getRequestParameters().add(new NameValuePair("EndDate", "20170802"));
		requestSettings.getRequestParameters().add(new NameValuePair("BtnOK", "²é Ñ¯"));
		requestSettings.getRequestParameters().add(new NameValuePair("uc_Adv$AdvLocID", ""));
		requestSettings.getRequestParameters().add(new NameValuePair("ClientNo", "D7FFADB19E5E25C159C0C9D914739BFD364433945681189200003904"));
		requestSettings.getRequestParameters().add(new NameValuePair("PanelControl", ""));
		
		
		Page page = webClient.getPage(requestSettings);  
		String html = page.getWebResponse().getContentAsString();  
		System.out.println("html ====¡·¡·"+html);		
		
		
		
		
		
		
		
		
		
		

	}

}
