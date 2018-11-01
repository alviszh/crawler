package test;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String html = "{'code':0,'message':'','dataset':{'total':1,'rows':[{'SCHJNY':'','GRZHYE':'6319.57','DWZH':'0001314','SFDK':'否','LXDH':'','HJNY':'2018.03','SJH':'','DWMC':'长沙经济技术开发区捷特人力资源开发有限公司','ZJHM':'4306**********8343','GDDHHM':'','GRJCJS':'1500','KHRQ':'2015.01.15','XM':'陈沁怡','GRZHZTMC':'封存','JTDZ':'','GZJS':'1500','LXDZ':'','SEX':'女','GRYJCE':'75','GRZH':'048490193','DWJJL':'5','YJE':'150','ZGJJL':'5','ORGNAME':'市直'}]}}";
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject accountCard = object.get("dataset").getAsJsonObject();
		JsonArray accountCardList = accountCard.get("rows").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String jndate = account.get("GRZHYE").toString().replaceAll("\"", "");      
			System.out.println(jndate);
		}		
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url1 = "http://yygjj.gov.cn/logon.do;jsessionid=oTKoDPHt-58s5faew0mduEd31kCbPe2bc-n7B1OwTFS4b7khGu3z!1285593134";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "yygjj.gov.cn");
//		webRequest.setAdditionalHeader("Origin", "http://yygjj.gov.cn");
//		webRequest.setAdditionalHeader("Referer", "http://yygjj.gov.cn/gjjcx.jsp");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		String requestBody = "type=1&spcode=430623199105248343&sppassword=000000&Submit=+";
//		webRequest.setRequestBody(requestBody);
//		Page searchPage = webClient.getPage(webRequest);
////	    System.out.println(searchPage.getWebResponse().getContentAsString());
//		String url =  "http://yygjj.gov.cn/zg_info.do";
//		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.GET);
//		Page searchPage1 = webClient.getPage(webRequest1);
//		System.out.println(searchPage1.getWebResponse().getContentAsString());
//		
//		String url2 = "http://yygjj.gov.cn/mx_info.do?hjnf=2015";
//		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);
//		Page searchPage2 = webClient.getPage(webRequest2);
//		System.out.println(searchPage2.getWebResponse().getContentAsString());
//		
//		String url3 = "http://yygjj.gov.cn/mx_info.do?hjnf=2016";
//		WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.GET);
//		Page searchPage3 = webClient.getPage(webRequest3);
//		System.out.println(searchPage3.getWebResponse().getContentAsString());
//		
//		String url4 = "http://yygjj.gov.cn/mx_info.do?hjnf=2017";
//		WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.GET);
//		Page searchPage4 = webClient.getPage(webRequest4);
//		System.out.println(searchPage4.getWebResponse().getContentAsString());
//		
//		String url5 = "http://yygjj.gov.cn/mx_info.do?hjnf=2018";
//		WebRequest webRequest5 = new WebRequest(new URL(url5), HttpMethod.GET);
//		Page searchPage5 = webClient.getPage(webRequest5);
//		System.out.println(searchPage5.getWebResponse().getContentAsString());
	}
		

}
