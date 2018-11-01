package app.crawler.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

@Component
public class LoginAndGetLiaoNing {

	public static WebClient addcoolieByLiaoNing(WebClient webClient2){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Map<String,String> mapcookie = new HashMap<>();
		
		mapcookie.put(".ybtj.189.cn", "F39CA881DB96BBD0152758ED852574A5");
		mapcookie.put("Hm_lvt_9c25be731676bc425f242983796b341c", "1503037371");
		mapcookie.put("JSESSIONID", "1A60C4BB07E71E7359121DBEA927CE3D");//
		mapcookie.put("aactgsh111220", "18940105113");//
		mapcookie.put("SHOPID_COOKIEID", "10005");
		mapcookie.put("cityCode", "ln");
		mapcookie.put("loginStatus", "non-logined");
		mapcookie.put("lvid", "6eee86aff9c05e448dd98ff6c449b69e");
		mapcookie.put("nvid", "1");
		mapcookie.put("s_cc", "true");
		mapcookie.put("s_fid", "14F22B44457CFDE2-1462311F58F57166");
		mapcookie.put("s_sq", "%5B%5BB%5D%5D");
		mapcookie.put("td_cookie", "18446744071030574792");
		mapcookie.put("trkHmCity", "0");
		mapcookie.put("trkHmCitycode", "0");
		mapcookie.put("trkHmClickCoords", "90%2C471%2C1821");
		mapcookie.put("trkHmCoords", "0");
		mapcookie.put("trkHmLinks", "0");
		mapcookie.put("trkHmPageName", "%2Fln%2F");//0
		mapcookie.put("trkId", "FB1E9EBE-E9D9-44BB-8B47-DCD0DB7EDFFF");
		mapcookie.put("userId", "201%7C20170100000001992715");
		
		for (Map.Entry<String, String> entry : mapcookie.entrySet()) {
			webClient.getCookieManager().addCookie(new Cookie("ln.189.cn", entry.getKey(), entry.getValue()));
		}
		Set<Cookie> cookies = webClient2.getCookieManager().getCookies();
		for (Cookie cookie : cookies) {
			if(cookie.getName().indexOf("trkHmClickCoords")!=-1){
				webClient.getCookieManager().addCookie(cookie);
			}
			
			if(cookie.getName().indexOf("JSESSIONID") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			
			if(cookie.getName().indexOf("lvid") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("nvid") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("s_cc") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("s_fid") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("s_sq") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf(".ybtj.189.cn") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("Hm_lvt_9c25be731676bc425f242983796b341c") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("SHOPID_COOKIEID") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("cityCode") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("loginStatus") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("trkHmCity") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("trkHmCitycode") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("trkHmCoords") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("trkHmPageName") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if(cookie.getName().indexOf("trkHmLinks") != -1){
				webClient.getCookieManager().addCookie(cookie);
			}
			if (cookie.getName().indexOf("trkId") != -1) {
				webClient.getCookieManager().addCookie(cookie);
			}
			if (cookie.getName().indexOf("userId") != -1) {
				webClient.getCookieManager().addCookie(cookie);
			}
		}
		return webClient;
	}

}
