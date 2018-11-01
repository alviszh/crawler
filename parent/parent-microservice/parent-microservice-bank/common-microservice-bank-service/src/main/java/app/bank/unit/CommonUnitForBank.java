package app.bank.unit;


import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class CommonUnitForBank {

	public static final Logger log = LoggerFactory.getLogger(CommonUnitForBank.class);
	
	
	public static String transcookieToJsonBySelenium(WebDriver driver){
		
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		
		Set<CookieJson> cookiesSet= new HashSet<>();
		
		for(org.openqa.selenium.Cookie cookie:cookiesDriver){ 
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson); 
		} 
		
		return new Gson().toJson(cookiesSet);
	}
	
	
	/**
	 * @Des 将cookie转为string
	 * @param webClient
	 * @return
	 */
	public static String transcookieToJson(WebClient webClient){
		Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 

		Set<CookieJson> cookiesSet= new HashSet<>();
		
		for(Cookie cookie:cookies){ 
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson); 
		} 

		String cookieJson = new Gson().toJson(cookiesSet);
		return cookieJson;

	}
	
	public static String transcookieToJson2(Set<Cookie> cookies){
		//Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 

		Set<CookieJson> cookiesSet= new HashSet<>();
		
		for(Cookie cookie:cookies){ 
			CookieJson cookiejson = new CookieJson();
			cookiejson.setDomain(cookie.getDomain());
			cookiejson.setKey(cookie.getName());
			cookiejson.setValue(cookie.getValue());
			cookiesSet.add(cookiejson); 
		} 

		String cookieJson = new Gson().toJson(cookiesSet);
		return cookieJson;

	}
	
	/**
	 * @Des 将json转为Set<Cookie>
	 * @param json
	 * @return
	 */
	public static Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie(cookieJson.getDomain(), cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}
		
		return set;

	}
		
	/**
	 * MD5加码。32位
	 * 
	 * @param inStr
	 * @return
	 */
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
}
