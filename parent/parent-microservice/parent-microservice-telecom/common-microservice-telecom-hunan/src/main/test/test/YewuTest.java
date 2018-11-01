package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

public class YewuTest {

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

				
				String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/businessquery/business-query!userBusinessList.parser?_z=1&cityCode=hn&fastcode=10000279";
				WebRequest webRequestwdzl;
				webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
				webclientlogin.getPage(webRequestwdzl);
				HtmlPage wdzl = webclientlogin.getPage(webRequestwdzl);
				String asXml2 = wdzl.asXml();
				System.out.println(asXml2);
				Document doc = Jsoup.parse(asXml2);
			    Elements trs = doc.select("table");
			        //套餐业务
			        String taocantable =  trs.get(0)+"";
                    if(taocantable.contains("无任何数据")){
                    }else{
                    	Elements element0 = trs.get(0).select("tr");
                    	for (int i = 1; i < element0.size(); i++) {
                    		Elements select = element0.get(i).select("td");
                    		for (int j = 0; j < select.size(); j+=4) {
                    			//套餐名称
                    			String businessname = select.get(j).html();
                    			//说明
                    			String explain = select.get(j+1).html();
                    			//生效日期
                    			String effecttime = select.get(j+2).html();
                    			//失效日期
                    			String losetime = select.get(j+3).html();
                    			System.out.println(businessname);
                    			System.out.println(explain);
                    			System.out.println(effecttime);
                    			System.out.println(losetime);
                    		}
                    	}
                    }
					
			    //增值业务
			    Elements element1 = trs.get(1).select("tr");
			    for (int i = 1; i < element1.size(); i++) {
					Elements select = element1.get(i).select("td");
					for (int j = 0; j < select.size(); j+=6) {
						//业务名称
						String businessname = select.get(j).html();
						//SP名称
						String businessSPname = select.get(j+1).html();
						//业务费用
						String businessfee = select.get(j+2).html();
						//生效日期
						String effecttime = select.get(j+3).html();
						//失效日期
						String losetime = select.get(j+4).html();
						System.out.println(businessname);
						System.out.println(businessSPname);
						System.out.println(businessfee);
						System.out.println(effecttime);
						System.out.println(losetime);
					}
				}
			    //程控业务
			    Elements element2 = trs.get(2).select("tr");
			    for (int i = 1; i < element2.size(); i++) {
					Elements select = element2.get(i).select("td");
					for (int j = 0; j < select.size(); j+=4) {
						//套餐名称
						String businessname = select.get(j).html();
						//说明
						String businessintroduce = select.get(j+1).html();
						//生效日期
						String effecttime = select.get(j+2).html();
						System.out.println(businessname);
						System.out.println(businessintroduce);
						System.out.println(effecttime);
					}
				}
			    System.out.println("1111111111");
			}         
		} catch (Exception e) {

		}
	}

}
