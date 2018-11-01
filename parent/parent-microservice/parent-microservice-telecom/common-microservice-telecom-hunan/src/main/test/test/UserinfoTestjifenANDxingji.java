package test;

import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class UserinfoTestjifenANDxingji {

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

				
				String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/integralservice/integral-service!loadMyIntegral.parser?_z=1&cityCode=hn&fastcode=10000290";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				webclientlogin.getPage(webRequestwdzl);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				String asXml2 = wdzl.asXml();
				System.out.println(asXml2);
				Document doc = Jsoup.parse(asXml2);
				
				//即将到期积分  
			    Element word = doc.getElementsByClass("word").get(1);
			    String expireintegral = word.html();
			    //当前可用积分  
			    Element mone = doc.getElementsByClass("word").get(0);
			    String useintegral = mone.html();
			   
			    //倍增积分  
			    Elements mtwolist = doc.getElementsByClass("mtwo");
			    if(mtwolist.size()==0){
			    }else{
			    	Element mtwo = doc.getElementsByClass("mtwo").get(0);
			    	String doubleintegral = mtwo.html();
			    }
			    //促销积分   
			    Elements mthreelist = doc.getElementsByClass("mthree");
			    if(mthreelist.size()==0){
			    }else{
			    Element mthree = doc.getElementsByClass("mthree").get(0);
			    String salesintegral = mthree.html();
			    }
			    // 历史积分
				Elements mfourlist = doc.getElementsByClass("mfour");
				if(mfourlist.size() == 0){
				}else{
					Element mfour = doc.getElementsByClass("mfour").get(0);
					String historyintegral = mfour.html();
				}
			    System.out.println("1111111111111");
			}         
		} catch (Exception e) {

		}
	}

}
