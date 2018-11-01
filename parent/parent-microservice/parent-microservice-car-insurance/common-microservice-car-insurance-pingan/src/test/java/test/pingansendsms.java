package test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class pingansendsms {

	public static void main(String[] args) throws Exception {
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/verifyKey.do";
//		List<NameValuePair> paramsList = new ArrayList<>();
//		paramsList.add(new NameValuePair("isPacStaff", ""));
//		paramsList.add(new NameValuePair("flag", "false"));
//		paramsList.add(new NameValuePair("policyNo", "13308013900482149897"));
//		paramsList.add(new NameValuePair("idNo", "140322197311250048"));
//		paramsList.add(new NameValuePair("verifyCode", "4518"));
//		HtmlPage page2 = (HtmlPage) gethtmlPost(webClient, paramsList, url);
//		String contentAsString = page2.getWebResponse().getContentAsString();
//		System.out.println(contentAsString);
		
	}
	
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Host", "www.pingan.com");
		webRequest.setAdditionalHeader("Origin", "http://www.pingan.com");
		webRequest.setAdditionalHeader("Referer", "http://www.pingan.com/property_insurance/pa18AutoInquiry/login.screen");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		webRequest.setAdditionalHeader("Cookie", "td_cookie=3783248030; WEBTRENDS_ID=219.141.236.211-3764843424.30688056; PA_GXH_NSS=; PA_GXH_WSS=; USER_TRACKING_COOKIE=172.28.21.70-1535946496837.837000000; everCookieFingerprint=15a2f318e999e99d15a4a737dbbbfa1269c25ed9; Hm_lvt_2f53c35010dbe120000b9a32bd028225=1537257659; _from=direct; usercookie=Ed76dDIVxvaKKeRzt17tCyqzJQ4o7GtA; _n=hb86hGlJa2Od7ZKU/+RzITk2p/zcSDnMYi+dseD9VcoYW+/TUoVXgw==; pt_738e75fa=uid=Z1VXVPE1cV2uF58tQ5ZdLg&nid=1&vid=fEavk0-Wm5c2/hqbEOVy0w&vn=1&pvn=1&sact=1539157047949&to_flag=0&pl=Tc8jGcCCfBrYKOagx09bng*pt*1539157047949; BIGipServerpa18-property_insurance_PrdPool=68950444.7288.0000; BIGipServerng_pa18-paweb_DMZ_PrdPool=3758431404.63863.0000; PA18_PROPERTY_WLS_HTTP_BRIDGE=NpCAIZEiE3HiwlMUva1NhhG5x_fSXBp6Bmb6EMDyHmEhy0f1rtrr!-436365840; BIGipServerpa18-node_pc_PrdPool=3016563116.44155.0000; paid_test=89c5212e-888e-c493-8495-29daba70e012; Hm_lvt_d06f8617511c35d7eaaa23e187cd568e=1537261313,1539747988; Hm_lpvt_d06f8617511c35d7eaaa23e187cd568e=1539747988; BIGipServerpa18cms_static_PrdPool=587472044.4983.0000; BIGipServerpa18-shop-auto_DMZ_PrdPool=738598060.1654.0000; WT-FPC=id=219.141.236.211-3764843424.30688056:lv=1539748023191:ss=1539747976980:fs=1537257478982:pv_Num=2:vt_Num=1");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control","max-age=0");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Content-Length","92");
		webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
