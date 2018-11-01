package org.common.microservice.eureka.china.telecom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class dianxintest {

	public static void main(String[] args) {
		loginapp();
	}

	public static void loginapp() {
		// String url =
		// "http://ct-android.omniture.cn:9000/b/ss/insight_andorid/0/JAVA-3.2.6-AN/s92497441?AQB=1&ndh=1&v50=20160330&ce=UTF-8&v15=login&v39=dianxin&v41=864678036411047&v40=20160000000021945873&v10=18003658894&c.&a.&OSVersion=Android%207.0&DeviceName=MHA-AL00&CarrierName=%E4%B8%AD%E5%9B%BD%E8%81%94%E9%80%9A&Resolution=1080x1812&.a&.c&v4=channel27&pageName=%E7%94%B5%E4%BF%A1%E8%90%A5%E4%B8%9A%E5%8E%85%2F6.0.3&vid=ccb8daa1973e46418d2bf5d65d6e109c&v1=6.0.3&t=1%2F7%2F2017%2015%3A13%3A42%202%20-480&AQE=1";

		String url = "http://cservice.client.189.cn:8004/map/clientXML?encrypted=false";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		try {
			webClient.getOptions().setTimeout(5000000);
			Page page = getHtml(url, webClient);

			System.out.println("================" + page.getWebResponse().getContentAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		url = "http://cupdate.client.189.cn:8006/ClientUpdate/services/clientInfo/version?reqParam=%7B%22mobile%22%3A%2218003658894%22%2C%22company%22%3A%22F%22%2C%22device%22%3A%22HUAWEI+MHA-AL00%22%2C%22currentVersion%22%3A%226.0.3%22%2C%22reqTime%22%3A%2220170802134713947%22%7D";

		WebRequest webRequest;
		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);

			Page page = webClient.getPage(webRequest);
			System.out.println("================2===========" + page.getWebResponse().getContentAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		url = "http://bigdata.client.189.cn:8081/bdcdcp/go/client/upload";
		/*POST http://bigdata.client.189.cn:8081/bdcdcp/go/client/upload HTTP/1.1
			EsTag: ABCD9K8Q89KKHLJ8000HLICUFKUQLJHU
			Content-Length: 487
			Content-Type: application/x-www-form-urlencoded
			Host: bigdata.client.189.cn:8081
			Connection: Keep-Alive
			User-Agent: Apache-HttpClient/UNAVAILABLE (java 1.4)


			HTTP/1.1 200 OK
			Server: Apache-Coyote/1.1
			Transfer-Encoding: chunked
			Date: Wed, 02 Aug 2017 06:09:50 GMT*/



		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);

			Page page = webClient.getPage(webRequest);
			System.out.println("================3===========" + page.getWebResponse().getContentAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	/*	for(int i= 0;i<6;i++){
			url = "http://cservice.client.189.cn:8004/map/clientXML?encrypted=false";
			WebRequest webRequest;
			try {
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setAdditionalHeader("User-Agent", "HUAWEI MHA-AL00/6.0.3");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN-#Hans");
				webRequest.setAdditionalHeader("Host", "cservice.client.189.cn:8004");
				webRequest.setAdditionalHeader("Connection", "Keep-Alive");
				webRequest.setAdditionalHeader("Content-Type", "text/xml");

				Page page = webClient.getPage(webRequest);
				System.out.println("================2===========" + page.getWebResponse().getContentAsString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		//http://ct-android.omniture.cn:9000/b/ss/insight_andorid/0/JAVA-3.2.6-AN/s82653645?AQB=1&ndh=1&pageName=%E7%94%B5%E4%BF%A1%E8%90%A5%E4%B8%9A%E5%8E%85%2F6.0.3&vid=328ba03aa057493b97cc390c8381b0e6&v41=864678036411047&v50=20160330&t=2%2F7%2F2017%209%3A48%3A0%203%20-480&v35=%E8%AE%BE%E7%BD%AE&v10=18003658894&v4=channel27&ce=UTF-8&c.&a.&OSVersion=Android%207.0&DeviceName=MHA-AL00&CarrierName=%E4%B8%AD%E5%9B%BD%E8%81%94%E9%80%9A&Resolution=1080x1812&.a&.c&v1=6.0.3&AQE=1

		url = "http://content.kefu.189.cn/tykfh5/services/dispatch.jsp?&dispatchUrl=ClientUni%2Fclientuni%2Fservices%2Ffee%2FperiodPoint%3FreqParam%3D%7B%22token%22%3A%220090cec27bdf89234058c87da92de8c5%22%2C%22fromDate%22%3A%22201703%22%2C%22toDate%22%3A%22201708%22%2C%22reqTime%22%3A%2220170801160328853%22%2C%22sign%22%3A%22005A20A1DCE38849ABD67C160DCF3F89%22%7D";

		try {
			 webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			/*
			 * webRequest.setAdditionalHeader("User-Agent",
			 * "HUAWEI MHA-AL00/6.0.3");
			 * webRequest.setAdditionalHeader("Accept-Language", "zh-CN-#Hans");
			 * webRequest.setAdditionalHeader("Host",
			 * "cservice.client.189.cn:8004");
			 * webRequest.setAdditionalHeader("Connection", "Keep-Alive");
			 * webRequest.setAdditionalHeader("Content-Type", "text/xml");
			 */

			Page page = webClient.getPage(webRequest);
			System.out.println("================3===========" + page.getWebResponse().getContentAsString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Content-Length: 912 Content-Type: text/xml Host: Connection:
		 * Keep-Alive User-Agent:
		 * 
		 * 
		 * HTTP/1.1 200 OK Server: Apache-Coyote/1.1 Cache-Control: no-cache
		 * Content-Type: application/xml;charset=utf-8 Content-Length: 576 Date:
		 * Tue, 01 Aug 2017 07:30:55 GMT
		 */

	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

		/*
		 * Content-Length: 912 Content-Type: text/xml Host: Connection:
		 * Keep-Alive User-Agent:
		 * 
		 * 
		 * HTTP/1.1 200 OK Server: Apache-Coyote/1.1 Cache-Control: no-cache
		 * Content-Type: application/xml;charset=utf-8 Content-Length: 576 Date:
		 * Tue, 01 Aug 2017 07:30:55 GMT
		 */

		webRequest.setAdditionalHeader("User-Agent", "HUAWEI MHA-AL00/6.0.3");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN-#Hans");
		webRequest.setAdditionalHeader("Host", "cservice.client.189.cn:8004");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setAdditionalHeader("Content-Type", "text/xml");

		/*
		 * webRequest.setAdditionalHeader("User-Agent",
		 * "Mozilla/5.0 (Linux; U; Android 7.0; zh-CN-#Hans; MHA-AL00 Build/HUAWEIMHA-AL00) 电信营业厅/6.0.3"
		 * ); webRequest.setAdditionalHeader("Accept-Language", "zh-CN-#Hans");
		 * webRequest.setAdditionalHeader("Host",
		 * "ct-android.omniture.cn:9000");
		 * webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		 */
		// webRequest.setAdditionalHeader("Accept-Encoding", "gzip");
		/*
		 * GET
		 * http://ct-android.omniture.cn:9000/b/ss/insight_andorid/0/JAVA-3.2.6-
		 * AN/s92497441?AQB=1&ndh=1&v50=20160330&ce=UTF-8&v15=login&v39=dianxin&
		 * v41=864678036411047&v40=20160000000021945873&v10=18003658894&c.&a.&
		 * OSVersion=Android%207.0&DeviceName=MHA-AL00&CarrierName=%E4%B8%AD%E5%
		 * 9B%BD%E8%81%94%E9%80%9A&Resolution=1080x1812&.a&.c&v4=channel27&
		 * pageName=%E7%94%B5%E4%BF%A1%E8%90%A5%E4%B8%9A%E5%8E%85%2F6.0.3&vid=
		 * ccb8daa1973e46418d2bf5d65d6e109c&v1=6.0.3&t=1%2F7%2F2017%2015%3A13%
		 * 3A42%202%20-480&AQE=1 HTTP/1.1 User-Agent: Mozilla/5.0 (Linux; U;
		 * Android 7.0; zh-CN-#Hans; MHA-AL00 Build/HUAWEIMHA-AL00) 电信营业厅/6.0.3
		 * Accept-Language: zh-CN-#Hans Host: ct-android.omniture.cn:9000
		 * Connection: Keep-Alive Accept-Encoding: gzip
		 * 
		 * 
		 * HTTP/1.1 504 Gateway Timeout Connection: close
		 */

		Page page = webClient.getPage(webRequest);
		return page;
	}
}
