package test;

import java.net.MalformedURLException;
import java.net.URL;

import com.crawler.ningbo.domain.json.WebParam;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestNB {

	
	
	public static void main(String[] args) throws Exception {
        String url = "https://rzxt.nbhrss.gov.cn/nbsbk-rzxt/web/pages/index.jsp";
		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		HtmlPage page = webClient.getPage(webRequest);
		
		int status = page.getWebResponse().getStatusCode();
			
			HtmlImage image = page.getFirstByXPath("//img[@id='yzmJsp']");
			
		//	String code = chaoJiYingOcrService.getVerifycode(image, "4004");
			
			HtmlTextInput cardno = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginid']"); 
			
			HtmlPasswordInput perpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='pwd']");
			
			HtmlTextInput verify = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzm']");
			
			HtmlElement sub = (HtmlElement)page.getFirstByXPath("//input[@type='btnLogin']");
			
			cardno.setText("330222197703136914");
			
			perpwd.setText("63207764");
			
			verify.setText("4955");
			
			HtmlElement usnameError = (HtmlElement) page.getElementById("advice-i_username");
			String style = usnameError.getAttribute("style");
			if(!style.contains("display:none")){
				webParam.setCode(103);
			//	return webParam;

			}
			
			HtmlPage loggedPage = sub.click();
			System.out.println(loggedPage);
			webParam.setCode(loggedPage.getWebResponse().getStatusCode());
			webParam.setPage(loggedPage);
			System.out.println(webParam);
	}
}
//		
//		
//	}
//	
//	
//	//参数type来区别请求类型，1为post，其他为get
//	public static HtmlPage getHtml(String url,WebClient webClient,int type) throws Exception{
////		WebClient webClient = WebCrawler.getInstance().getWebClient();
////		webClient.getOptions().setJavaScriptEnabled(false);
//		WebRequest webRequest = new WebRequest(new URL(url), type==1 ? HttpMethod.POST : HttpMethod.GET);
////		webRequest.setAdditionalHeader("Host", "gzlss.hrssgz.gov.cn");
////		
////		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0");
////		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
////		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
////		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
////		webRequest.setAdditionalHeader("Connection", "keep-alive");
////		webRequest.setAdditionalHeader("Cookie", "JSESSIONID=k4P8Z1LB110ntrhJwlPnnJhvDyd2mfF1DnRBphpZkdxQk5JwZ118!1548516745"); 
//		
//		cookies = webClient.getCookieManager().getCookies();
//		for(Cookie cookie:cookies){
//			System.out.println("搜索："+cookie.getName()+":"+cookie.getValue());
//		}
//		HtmlPage searchPage = webClient.getPage(webRequest);
//		return searchPage;
//		
//	}
//	
	

