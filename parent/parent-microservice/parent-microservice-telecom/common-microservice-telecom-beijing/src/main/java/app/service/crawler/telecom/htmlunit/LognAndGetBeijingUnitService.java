package app.service.crawler.telecom.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;

import app.service.SMSCodejson;

@Component
public class LognAndGetBeijingUnitService {

	public  final Logger log = LoggerFactory.getLogger(LognAndGetBeijingUnitService.class);

	private  Gson gs = new Gson();

	public  String getSMSCode(WebClient webClient, MessageLogin messageLogin) throws Exception {
		String url = "http://bj.189.cn/iframe/feequery/smsRandCodeSend.action?accNum="
				+  messageLogin.getName().trim();

			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "bj.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");

			Page page = webClient.getPage(webRequest);

			SMSCodejson jsonObject = gs.fromJson(page.getWebResponse().getContentAsString(), SMSCodejson.class);

			return jsonObject.getSRandomCode();

	}
	
	
	public  Page verifySms(WebClient webClient, MessageLogin messageLogin,String  smsCode) throws Exception{
		String url = "http://bj.189.cn/iframe/feequery/detailValidCode.action?requestFlag=asynchronism"
				+ "&accNum="+messageLogin.getName().trim()
				+ "&randCode="+smsCode;

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60
			
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);

			log.info("======" + page.getWebResponse().getContentAsString());


			return page;

	}

	public  Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public  Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) {

		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public  Page getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			return page;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public  WebClient ready(WebClient webClient, int k) {
		String url = "http://bj.189.cn/service/account/lastLoginTime.parser";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("time", (System.currentTimeMillis() / 1000) + ""));
		try {
			gethtmlPost(webClient, paramsList, url);
		} catch (Exception e) {
			e.printStackTrace();
			if (k < 3) {
				return ready(webClient, k);
			} else {
				return null;
			}

		}
		url = "http://bj.189.cn/iframe/feequery/billQueryIndex.parser?fastcode=01390637";

		try {
			getHtml(url, webClient);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		return webClient;
	}

	public  WebClient readyForCallThrem(WebClient webClient, int k,MessageLogin messageLogin) {
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01390638";

		try {
			Page page = getHtml(url, webClient);

			url = "http://bj.189.cn/iframe/feequery/detailBillIndex.action?tab=tab1&time="+System.currentTimeMillis();

			page = getHtml(url, webClient);
			webClient =  page.getEnclosingWindow().getWebClient();
			
			url = "http://bj.189.cn/service/account/customerHome.action?PlatNO=90000&Ticket=900002052446f49cdd73af35403b9ece9f1b1c03f9b1&TxID=100362018091414205235ada3d0b5e9a445e7832c251fd3d619&SSOURL=http%3A%2F%2Fbj.189.cn%2Fiframe%2Ffeequery%2FdetailBillIndex.action%3Ffastcode%3D01390638%26cityCode%3Dbj";
			
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "bj.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");

			page = webClient.getPage(webRequest);
			
//			webClient = page.getEnclosingWindow().getWebClient();
//			
//			String smsCode = getSMSCode(webClient, messageLogin);
//			
//			 page = verifySms(webClient, messageLogin, smsCode);
			return page.getEnclosingWindow().getWebClient();
		} catch (Exception e) {
			if (k < 3) {
				return ready(webClient, k);
			} else {
				return null;
			}
		}

	}
	
	public  String getSms(WebClient webClient, int k,MessageLogin messageLogin){

		try {
			
						
			String smsCode = getSMSCode(webClient, messageLogin);
			
			
			return smsCode;
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public  WebClient smsForCall(WebClient webClient, int k,MessageLogin messageLogin,String smsCode){

		try {
						
			Page page = verifySms(webClient, messageLogin, smsCode);
			return page.getEnclosingWindow().getWebClient();
		} catch (Exception e) {
			if (k < 3) {
				return ready(webClient, k);
			} else {
				return null;
			}
		}
	}

	
//	public void postForCall(WebClient webClient, String smsCode){
//		http://bj.189.cn/iframe/feequery/detailValidCode.action?requestFlag=asynchronism&accNum=13366777357&randCode=870651
//	
//	
//	}
	/*
	 * public  void main(String[] args) throws Exception { HtmlPage
	 * htmlpage = login("18003658894", "211314"); htmlpage =
	 * getphonecode(htmlpage); MessageResult messageResult = new
	 * MessageResult(); JFrame f2 = new JFrame(); f2.setSize(100, 100);
	 * f2.setTitle("短信验证码"); f2.setVisible(true); String valicodeStr =
	 * JOptionPane.showInputDialog("请输入短信验证码："); f2.setVisible(false);
	 * messageResult.setCode(valicodeStr); htmlpage =
	 * setphonecode(messageResult); String html = getCallThemHtml(messageResult,
	 * "18003658894", "20175","2"); System.out.println(html); }
	 */


}
