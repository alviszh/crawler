package org.common.microservice.housingfund.yulin;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import app.service.ChaoJiYingOcrService;

public class AppTest {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
    public static void main(String[] args) throws Exception {
//    	String sqye = "1213";
//    	String bqye = "2000";
//    	int a = Integer.parseInt(sqye);
//		int b = Integer.parseInt(bqye);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String sb = "/Date(1511971200000)/";
//        sb.substring(6, 19);
//        sdf.format(Long.parseLong(sb.substring(6, 19)));
//        System.out.println(a+b);
//    	String jsonMessage = "[{'num':'成绩', '外语':88, '历史':65, '地理':99, 'object':{'aaa':'1111','bbb':'2222','cccc':'3333'}}," +
//    	          "{'num':'兴趣', '外语':28, '历史':45, '地理':19, 'object':{'aaa':'11a11','bbb':'2222','cccc':'3333'}}," +
//    	          "{'num':'爱好', '外语':48, '历史':62, '地理':39, 'object':{'aaa':'11c11','bbb':'2222','cccc':'3333'}}]";
//    	 JSONArray myJsonArray = JSONArray.fromObject(jsonMessage);
//    	 JSONObject obj = JSONObject.fromObject(myJsonArray.get(2));
////    	 System.out.println(obj.getString("num"));
//    	
//    	String url="http://www.jzgjj.gov.cn:7002/wt-web/login"; 
//    	WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
//    	HtmlPage page = webClient.getPage(url); 
//    	
//    	Document doc = Jsoup.parse(page.getWebResponse().getContentAsString()); 
//    	Element e1 = doc.getElementById("modulus"); 
//    	String val1 = e1.val(); 
//
//    	Element e2 = doc.getElementById("exponent"); 
//    	String val2 = e2.val(); 
//    	System.out.println(val1 + "--" + val2);
    	
    	final Base64 base64 = new Base64();
    	final String text = "450922199009210488"+" "+"LHF15296519439";
    	final byte[] textByte = text.getBytes("UTF-8");
    	//编码
    	final String encodedText = base64.encodeToString(textByte);
    	System.out.println(encodedText);

    }
}