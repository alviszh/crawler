package test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class MsgMain {
	public static void main(String[] args) {
		try{
			String loginurl = "http://login.189.cn/login";
			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
		    WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl),HttpMethod.GET);
			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
			
			
			//获取对应的输入框
			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
			username.setText("18995154123");
			passwordInput.setText("795372");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("------------"+asXml);
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			}else{
				System.out.println("登录成功！");
				String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000776";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				webclientlogin = wdzl.getWebClient();
				int statusCode = wdzl.getWebResponse().getStatusCode();
				if(statusCode==200){
					
						String packurl7 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
						String requestPayloadSend7="<buffalo-call><method>getCustOfBillByCustomerCode</method><string>2951121277270000__giveup</string></buffalo-call>";
						WebRequest webRequestpack7= new WebRequest(new URL(packurl7),HttpMethod.POST);
						webRequestpack7.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
						webRequestpack7.setAdditionalHeader("Host", "nx.189.cn");
						webRequestpack7.setAdditionalHeader("Origin", "http://nx.189.cn");
						webRequestpack7.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
						webRequestpack7.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
						webRequestpack7.setRequestBody(requestPayloadSend7);
						webclientlogin.getPage(webRequestpack7);
						
						String packurl8 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
						String requestPayloadSend8="<buffalo-call><method>getFeeNumByHT</method><string>10732369</string><string>201708</string></buffalo-call>";
						WebRequest webRequestpack8= new WebRequest(new URL(packurl8),HttpMethod.POST);
						webRequestpack8.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
						webRequestpack8.setAdditionalHeader("Host", "nx.189.cn");
						webRequestpack8.setAdditionalHeader("Origin", "http://nx.189.cn");
						webRequestpack8.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
						webRequestpack8.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
						webRequestpack8.setRequestBody(requestPayloadSend8);
						webclientlogin.getPage(webRequestpack8);
						
						String packurl9 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
						String requestPayloadSend9="<buffalo-call><method>getSelectedFeeProdNum</method><string>undefined</string><string>18995154123</string><string>2</string></buffalo-call>";
						WebRequest webRequestpack9= new WebRequest(new URL(packurl9),HttpMethod.POST);
						webRequestpack9.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
						webRequestpack9.setAdditionalHeader("Host", "nx.189.cn");
						webRequestpack9.setAdditionalHeader("Origin", "http://nx.189.cn");
						webRequestpack9.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
						webRequestpack9.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
						webRequestpack9.setRequestBody(requestPayloadSend9);
						webclientlogin.getPage(webRequestpack9);
						
//						//发送验证码接口
//					    String sendurl = "http://nx.189.cn/bfapp/buffalo/CtSubmitService";
//					    String requestPayloadSend="<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
//					    WebRequest webRequestSend = new WebRequest(new URL(sendurl), HttpMethod.POST);
//					    webRequestSend.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
//					    webRequestSend.setAdditionalHeader("Host", "nx.189.cn");
//					    webRequestSend.setAdditionalHeader("Origin", "http://nx.189.cn");
//					    webRequestSend.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
//					    webRequestSend.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
//					    webRequestSend.setRequestBody(requestPayloadSend);
//					    Page pageSend = webclientlogin.getPage(webRequestSend);
//					    
//					    
//					    String cookieString = CommonUnit.transcookieToJson(webclientlogin);
//					    Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
//					    
//						String send = pageSend.getWebResponse().getContentAsString();
//						System.out.println("发送验证码接口"+send);
//						if(send.contains("base.Success")){
							//短信验证码验证接口
							String checkurl = "http://nx.189.cn/bfapp/buffalo/CtQryService";
							String phone = "18995154123";
//							String yzm;
//							System.out.print("输入验证码！");
//							Scanner scan = new Scanner(System.in);
//						    yzm = scan.next();
							
						    String requestPayloadCheck="<buffalo-call><method>validBillSMS</method><string>"+phone+"</string><string>123456</string></buffalo-call>";
						    WebRequest webRequestCheck = new WebRequest(new URL(checkurl), HttpMethod.POST);
						    webRequestCheck.setAdditionalHeader("Accept", "*/*");
						    webRequestCheck.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
						    webRequestCheck.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
						    webRequestCheck.setAdditionalHeader("Connection", "keep-alive");
						    webRequestCheck.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
						    webRequestCheck.setAdditionalHeader("Host", "nx.189.cn");
							webRequestCheck.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
							webRequestCheck.setRequestBody(requestPayloadCheck);
						    Page pageCheck = webclientlogin.getPage(webRequestCheck);
							String Check = pageCheck.getWebResponse().getContentAsString();
							System.out.println("验证验证码的接口"+Check);
							if(Check.contains("base.Success")){
								//爬取和解析    通话记录
								//获得数据的接口
								String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
								String requestPayload="<buffalo-call><method>qry_sj_yuyinfeiqingdan</method><string>20170801</string><string>20170831</string></buffalo-call>";
								WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
								webRequest.setAdditionalHeader("Accept", "*/*");
								webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
								webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
								webRequest.setAdditionalHeader("Connection", "keep-alive");
								webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
								webRequest.setAdditionalHeader("Host", "nx.189.cn");
								webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
								webRequest.setRequestBody(requestPayload);
								Page page = webclientlogin.getPage(webRequest);
								String contentAsString = page.getWebResponse().getContentAsString();
								System.out.println(contentAsString);
							}
						}
				}
						
//				}
				
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
	}

}
