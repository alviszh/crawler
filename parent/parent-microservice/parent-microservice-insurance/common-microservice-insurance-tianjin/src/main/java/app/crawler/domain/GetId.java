package app.crawler.domain;

import com.gargoylesoftware.htmlunit.WebClient;

public class GetId {

public GetId(WebClient webClient) {
	 getId1(webClient);
}

public String getId1(WebClient webClient) {
// 	String url ="http://public.tj.hrss.gov.cn/uaa/captcha/img";
// 	try {
// 		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		
// 		requestSettings.setAdditionalHeader("Accept", "application/json, text/plain, */*");
// 		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
// 		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
// 		requestSettings.setAdditionalHeader("Connection", "keep-alive");
// 		requestSettings.setAdditionalHeader("Host", "public.tj.hrss.gov.cn");
// 		requestSettings.setAdditionalHeader("Referer", "http://public.tj.hrss.gov.cn/uaa/personlogin");
// 		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		
// 		Page page = webClient.getPage(requestSettings); 
// 		webClient.waitForBackgroundJavaScript(1000);
// 		String loggedhtml = page.getWebResponse().getContentAsString();
// 		JSONObject jsonObject = new JSONObject(loggedhtml);
// 		return jsonObject.getString("id");

// 	} catch (Exception e) {
// 		e.printStackTrace();
// 	}
	
	return null;
}
}


