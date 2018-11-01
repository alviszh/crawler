package test;

import java.net.URL;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class JiaofeiTest {

	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String loginurl = "http://login.189.cn/login";
			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);

			// 获取对应的输入框
			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin
					.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
			username.setText("17388977090");
			passwordInput.setText("216832");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("------------" + asXml);
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");
				
				
				String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
				WebRequest webRequestwdzl1;
				webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
				HtmlPage wdzl1 = webclientlogin.getPage(webRequestwdzl1);
				webclientlogin = wdzl1.getWebClient();

				
				String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/queryPaymentRecord.parser?queryNumType=80000045&accNbrType=80000045&queryMonth=201707";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
				webclientlogin.getPage(webRequestwdzl);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				String asXml2 = wdzl.asXml();
				System.out.println(asXml2);
				if(asXml2.contains("未查询到相关数据")){
					System.out.println("未查询到相关数据");
				}else{
					Document doc = Jsoup.parse(asXml2);
					Elements trs = doc.getElementsByClass("taoc_table");
					//缴费信息
					Elements element0 = trs.get(0).select("tr");
					for (int i = 1; i < element0.size(); i++) {
						Elements select = element0.get(i).select("td");
						if(select.size()==5){
							for (int j = 0; j < select.size(); j+=5) {
								//入账时间
								String paydate = select.get(j).html();
								//入账金额
								String paymoney = select.get(j+1).html();
								//交费渠道
								String payditch = select.get(j+2).html();
								//交费方式
								String payway = select.get(j+3).html();
								//使用范围
								String scope = select.get(j+4).html();
								System.out.println(paydate);
								System.out.println(paymoney);
								System.out.println(payditch);
								System.out.println(payway);
								System.out.println(scope);
							}
						}
					}
				}
					
			    
			}         
		} catch (Exception e) {

		}
	}


}
