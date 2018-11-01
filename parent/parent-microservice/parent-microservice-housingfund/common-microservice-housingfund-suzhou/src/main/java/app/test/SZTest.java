package app.test;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;

public class SZTest {
	@Autowired
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	
	public static void main(String[] args) throws Exception {
		  login();
		
	}
	
	
	public static void login() throws Exception{
		//index界面
        String url="https://gr.szgjj.gov.cn/retail/index.jsp";
		WebParam webParam= new WebParam();
	    WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	    HtmlPage htmlPage = webClient.getPage(url);	
	    Document doc = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());
	    String val = doc.getElementsByAttributeValue("name", "sid").val();
	    //登录页面
	    String url1 = "https://gr.szgjj.gov.cn/retail/internet?sid="+val+"&service=com.jbsoft.i2hf.retail.services.UserLogon.logturnpage&turntype=0";
	    WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
	    HtmlPage page1 = webClient.getPage(webRequest);
	    HtmlImage valiCodeImg = page1.getFirstByXPath("//img[@id='validatePicture0']");
//	   
	    String verifycode = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");
	    //post请求登录
	    String url2="https://gr.szgjj.gov.cn/retail/service?service=com.jbsoft.i2hf.retail.services.UserLogon.unRegUserLogon&custacno=0097953388&paperid=230524198402142026&paperkind=A&logontype=1&validateCode="+verifycode;
		WebParam webParam1= new WebParam();
		WebClient webClient1 = WebCrawler.getInstance().getNewWebClient();
		webClient1.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.POST);		
		HtmlPage page = webClient.getPage(webRequest1);
		
		String  html = page.getWebResponse().getContentAsString();
	    System.out.println(html);

	}
}
