package test;

import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

public class AppYuyinShouye {

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
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
			username.setText("18060845645");
			passwordInput.setText("054246");
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");
				
				
				String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
				WebRequest webRequestwdzl1;
				webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
				HtmlPage wdzl1 = webclientlogin.getPage(webRequestwdzl1);
				webclientlogin = wdzl1.getWebClient();
				
				//发送短信验证码请求
				String wdzlurl4 = "http://fj.189.cn/BUFFALO/buffalo/QueryAllAjax";
				WebRequest webRequestwdzl4;
				String requestbody = "<buffalo-call><method>getCDMASmsCode</method><map><type>java.util.HashMap</type><string>PHONENUM</string><string>18060845645</string><string>PRODUCTID</string><string>50</string><string>CITYCODE</string><string>0591</string><string>I_ISLIMIT</string><string>1</string><string>QUERYTYPE</string><string>BILL</string></map></buffalo-call>";
				webRequestwdzl4 = new WebRequest(new URL(wdzlurl4), HttpMethod.POST);
				webRequestwdzl4.setAdditionalHeader("Accept", "*/*");
				webRequestwdzl4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequestwdzl4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequestwdzl4.setAdditionalHeader("Connection", "keep-alive");
				webRequestwdzl4.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");
				webRequestwdzl4.setAdditionalHeader("Host", "fj.189.cn");
				webRequestwdzl4.setAdditionalHeader("Origin", "http://fj.189.cn");
				webRequestwdzl4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
				webRequestwdzl4.setAdditionalHeader("X-Buffalo-Version", "2.0");
				webRequestwdzl4.setRequestBody(requestbody);
				Page wdzl4 = webclientlogin.getPage(webRequestwdzl4);
				String contentAsString = wdzl4.getWebResponse().getContentAsString();
				System.out.println(contentAsString);
				
				
				
				if(contentAsString.contains("短信随机密码已经发到您的手机,请查收")){
					System.out.println("验证码发送成功！");
					
					//短信验证码验证接口
					String yzm = "";
					System.out.print("输入验证码！");
					Scanner scan = new Scanner(System.in);
				    yzm = scan.next();
				    
					String wdzlurlyuyin = "http://fj.189.cn/service/bill/trans.jsp?PRODNO=18060845645&PRODTYPE=50&CITYCODE=0591&SELTYPE=1&MONTH=201709&PURID=0&email_empoent=10001&email_module=863581c5892cdfe8a67b95c7abb47ead8b102e9620994ae95637f637fa22acac173b91015574507362816b30a884632d8562bf20de621d31d745291aaec7ca6f&serPwd50=1a3a4679f43a815d540a6f62b9cb1d9dddcaeb56bfccee2796a8589d6bb9b602ef09a56a80f10779e1ec0688fa3d73b216c44080aa0ae51c1eabd12a83b09d47&randomPwd="+yzm;
					WebRequest webRequestwdzlyuyin;
					webRequestwdzlyuyin = new WebRequest(new URL(wdzlurlyuyin), HttpMethod.POST);
					HtmlPage wdzlyuyin = webclientlogin.getPage(webRequestwdzlyuyin);
					String contentAsString2 = wdzlyuyin.getWebResponse().getContentAsString();
					System.out.println(contentAsString2);
					if(contentAsString2.contains("您的短信随机码不正确或已失效")){
						System.out.println("短信验证码验证失败！");
					}else{
						System.out.println("短信验证码验证成功！");
						Document doc = Jsoup.parse(contentAsString2);
						Elements tr = doc.select("table").get(1).select("tr");
						for (int i = 1; i < tr.size(); i++) {
                    		Elements select = tr.get(i).select("td");
                    		for (int j = 0; j < select.size(); j+=7) {
                    			//通话起始时间
                    			String communicatetime2 = select.get(j+1).html();
                    			//通话时长
                    			String communicatetime = select.get(j+2).html();
                    			//呼叫类型
                    			String calltype = select.get(j+3).html();
                    			//通信地点
                    			String communicateaddr = select.get(j+4).html();
                    			//对方号码
                    			String oppositephone = select.get(j+5).html();
                    			//费用（元）
                    			String costtotal = select.get(j+6).html();
                    			System.out.println(communicatetime2);
                    			System.out.println(communicatetime);
                    			System.out.println(calltype);
                    			System.out.println(communicateaddr);
                    			System.out.println(oppositephone);
                    			System.out.println(costtotal);
                    		}
                    	}
					}
				}else{
					System.out.println("验证码发送失败！");
				}
				
			}         
		} catch (Exception e) {

		}
	}

}
