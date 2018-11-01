package app.test;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

public class TestGetBill {
	
	public static void main(String[] args) throws Exception {
		String url = "http://accounts.ccb.com/tran/WCCMainB1L1?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainB1L1";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String domain = "accounts.ccb.com";
		String requestBody = "UniEncodeStr=lTALt5YMiyFbTAH65jwwysRXkfb0d0SAlW2T8rugWJwfCuUmZGR0u6qRnbEj9ivOlwHOYXDjBWUsVR%252BcayArKwsshr0C40fOXrIhzVs%252B8ltxaW5Sc8pypq1mNYmUiZyicE1MpQ5zu2Q%253D&TXCODE=E3CX11&EdDt=&UNEdDt=IpLrr2etojWm6RRQMHhkmczmDwU5HNByFla769ULlZzsKmbWjFa9SFMxQ%25252Bo%25252BHLQ%25252FcEPjgVkGyIrEC9QGh3keDr6j%25252BuLAtqjTkrVutWz0e0JMdVDRa8cSbI8dbWyq%25252BpC2C%25252BR0JnxL1LYs9sC2sFY0x8JV46153cVeWT5AbFUNTPtxDhnX6BwOjmPF1f3UiHuIo41TN%25252FUiu2WzoTQoHZxoNaWzcFpQuzCB09uq9TGShwvfu7q0jTISJhf24QrIC6HMSa2LPL4VqUe7hmEGc7HUzmzU4sqaWOHigB6fiyYzw3tFrOPXKoGCr9MnjsLe%25252FbKTyLhb4QLtEqzIrYHqHcvBl%25252B7A9UDw6c6VkdbFhxQ8xTcqu7nh%25252BnHps9FbobV7lDdsaozwct8OubJ52AmaZnvhL7Xbhks0U15iGkuWYzEiwYn1Z27Nry7N7ltRw7oB7fA7u6VvFpNONcOMHqO0jXV2oU9JLyteOGch4DUAvd8pzxilpBCUkQ3DGOjsqf4QZYLAQ%25252BYKqa7pNr3NCSty587jHP9dOr1TZ%25252BsnoKVQbx4BdEsQoPBJP%25252BF5I9TRiXPomlqN&clientFileName=";
		String cookie = "UniEncodeStr=lTALt5YMiyFbTAH65jwwysRXkfb0d0SAlW2T8rugWJwfCuUmZGR0u6qRnbEj9ivOlwHOYXDjBWUsVR%252BcayArKwsshr0C40fOXrIhzVs%252B8ltxaW5Sc8pypq1mNYmUiZyicE1MpQ5zu2Q%253D&TXCODE=E3CX11&EdDt=&UNEdDt=IpLrr2etojWm6RRQMHhkmczmDwU5HNByFla769ULlZzsKmbWjFa9SFMxQ%25252Bo%25252BHLQ%25252FcEPjgVkGyIrEC9QGh3keDr6j%25252BuLAtqjTkrVutWz0e0JMdVDRa8cSbI8dbWyq%25252BpC2C%25252BR0JnxL1LYs9sC2sFY0x8JV46153cVeWT5AbFUNTPtxDhnX6BwOjmPF1f3UiHuIo41TN%25252FUiu2WzoTQoHZxoNaWzcFpQuzCB09uq9TGShwvfu7q0jTISJhf24QrIC6HMSa2LPL4VqUe7hmEGc7HUzmzU4sqaWOHigB6fiyYzw3tFrOPXKoGCr9MnjsLe%25252FbKTyLhb4QLtEqzIrYHqHcvBl%25252B7A9UDw6c6VkdbFhxQ8xTcqu7nh%25252BnHps9FbobV7lDdsaozwct8OubJ52AmaZnvhL7Xbhks0U15iGkuWYzEiwYn1Z27Nry7N7ltRw7oB7fA7u6VvFpNONcOMHqO0jXV2oU9JLyteOGch4DUAvd8pzxilpBCUkQ3DGOjsqf4QZYLAQ%25252BYKqa7pNr3NCSty587jHP9dOr1TZ%25252BsnoKVQbx4BdEsQoPBJP%25252BF5I9TRiXPomlqN&clientFileName=";
		WebRequest requestSettings = new WebRequest(new URL(url),HttpMethod.POST);
		
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
	    requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
	    requestSettings.setAdditionalHeader("Origin", "http://accounts.ccb.com");
	    requestSettings.setAdditionalHeader("Host", "accounts.ccb.com");
	    requestSettings.setAdditionalHeader("Referer", "http://accounts.ccb.com/tran/WCCMainPlatV5?CCB_IBSVersion=V5&SERVLET_NAME=WCCMainPlatV5");
	    requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest"); 
	    
	    requestSettings.setAdditionalHeader("Cookie",cookie );
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
//	    webClient.getCookieManager().addCookie(new Cookie(domain,"tranCCBIBS1", "pEUNOI"));
	    
//	    requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
//		requestSettings.getRequestParameters().add(new NameValuePair("CCB_IBSVersion", "V5"));
//		requestSettings.getRequestParameters().add(new NameValuePair("SERVLET_NAME", "WCCMainB1L1"));
		requestSettings.setRequestBody(requestBody);
		HtmlPage page = webClient.getPage(requestSettings);			
		System.out.println("*****************************************************    该账户流水");
		System.out.println(page.getWebResponse().getContentAsString());
	}

}
