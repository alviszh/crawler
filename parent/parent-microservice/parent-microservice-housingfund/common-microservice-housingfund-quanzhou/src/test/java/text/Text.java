package text;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.service.ChaoJiYingOcrService;
import app.service.common.HousingBasicService;

public class Text{
	static String cookiefile = "C:\\Users\\Administrator\\Desktop\\tel2.xls";

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		webClient = POIUnit.addCookie(webClient, cookiefile, "personalbank.cib.com.cn");
		String urlPay = "https://personalbank.cib.com.cn/pers/main/index.do";
		//String urlPay = "http://www.qzgjj.com/PAFundQuery/PersonAccount";
		WebRequest webRequest1 = new WebRequest(new URL(urlPay), HttpMethod.GET);
		webRequest1.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest1.setAdditionalHeader("Cache-Control","max-age=0");
		webRequest1.setAdditionalHeader("Connection", "keep-alive");
		webRequest1.setAdditionalHeader("Host", "personalbank.cib.com.cn");
		//webRequest1.setAdditionalHeader("Cookie", "td_cookie=18446744072168940388; bannerGray=false; td_cookie=18446744072164730671; __RequestVerificationToken_Lw__=KylTpU5zT8GJ+W71csXyRtxp9wz67HpDxHw8c4sJFDuUvoYlZdPkb+DWa3q9lodqpUsKEcdymITVBfhCJz2fott92sJjeHxuCPS+lbdq+QAt7qncwBZt9UB8NEewEQ3lze+CAFbW9qQUtHEuYS1QN4Dp5x56fMTHcGoy7LDjSTU=; ASP.NET_SessionId=0ll3s5qbdoqm1dae2z0x31xr; bannerGray=false");
		webRequest1.setAdditionalHeader("Referer", "https://personalbank.cib.com.cn/pers/main/login!verySMS.do;jsessionid=ZHzLhWyGCCwgLwWh3t1YL4T9P7b73g2YJY1LwQ7sLq1GxhfQYTJv!-249749735");
		webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		//webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"); 
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage page = webClient.getPage(webRequest1);
//		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@value='查  询']");
//		//HtmlElement button = (HtmlElement)htmlPage1.getFirstByXPath("//input[@value='个人账户查询']");
//		HtmlTextInput code = (HtmlTextInput)page.getFirstByXPath("//input[@id='Tb_MsgVCode']");
//        code.setText("111111");
//        HtmlPage htmlPage = button.click();
		System.out.println(page.getWebResponse().getContentAsString());
	}

}
