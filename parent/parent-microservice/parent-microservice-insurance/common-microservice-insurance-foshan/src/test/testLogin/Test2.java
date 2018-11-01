package testLogin;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

/**
 * @author Administrator
 *
 */
public class Test2 {

private static Set<Cookie> cookies;
	
	public static final String URl = "http://sdzb.hrss.gov.cn:8001/logonDialog.jsp";
	
	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		HtmlPage html = getHtml(URl, webClient, 1);
		
		HtmlTextInput loginName = (HtmlTextInput)html.getFirstByXPath("//input[@id='yhmInput']");
		HtmlPasswordInput loginPassword = (HtmlPasswordInput)html.getFirstByXPath("//input[@id='mmInput']");
		HtmlImage validateImage = html.getFirstByXPath("//img[@id='validatecode1']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("E:\\Codeimg\\"+imageName);
		validateImage.saveAs(file);
		
		HtmlTextInput validateInput = (HtmlTextInput)html.getFirstByXPath("//input[@id='validatecodevalue1']");
		HtmlElement submitbt = (HtmlElement)html.getFirstByXPath("//img[@class='logonBtn']");
		
		loginName.setText("370322199012150225");
		loginPassword.setText("zb123456");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		validateInput.setText(input);
		
		HtmlPage loginedPage = submitbt.click();
		
		String url = loginedPage.getUrl().toString();
		System.out.println(url);
		
		String usersession = url.substring(url.indexOf("=")+1, url.indexOf("&",url.indexOf("=")+1));
		System.out.println("-------------");
		System.out.println(usersession);
		System.out.println("-------------");
		
		Element element = Jsoup.parse(loginedPage.asXml()).select(".dw-laneContainer-mainLane").first();
		String laneID = element.attr("id");
		System.out.println(laneID);
		
		String method = "queryLostPayHis";
		
		HtmlPage shiyePage = getHtmlByPost("http://sdzb.hrss.gov.cn:8001/siLost.do",webClient,method,usersession,laneID);
		
		String shiyePath = "E:\\crawler\\zibo\\shiyeinfo.txt";
		savefile(shiyePath,shiyePage.asXml());
		
//		HtmlPage userInfoPage = getHtml("http://sdzb.hrss.gov.cn:8001/hspUser.do", webClient, 1);
//		
//		String userinfoPath = "E:\\crawler\\zibo\\userInfo.txt";
//		savefile(userinfoPath,userInfoPage.asXml());
        
	}
	
	public static HtmlPage getHtmlByPost(String url,WebClient webClient,String method,String uuid,String laneID) throws Exception{
	
		URL urlAction = new URL(url);
		WebRequest  requestSettings = new WebRequest(urlAction, HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("method", method));
		requestSettings.getRequestParameters().add(new NameValuePair("_random", "0.07056168409558583"));
		requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", uuid));
		requestSettings.getRequestParameters().add(new NameValuePair("_laneID", laneID));
		
		HtmlPage searchPage = webClient.getPage(requestSettings);
		return searchPage;
	}
	
	//参数type来区别请求类型，1为post，其他为get
	public static HtmlPage getHtml(String url,WebClient webClient,int type) throws Exception{
//			WebClient webClient = WebCrawler.getInstance().getWebClient();
//			webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), type==1 ? HttpMethod.POST : HttpMethod.GET);
		
//		cookies = webClient.getCookieManager().getCookies();
//		for(Cookie cookie:cookies){
//			System.out.println("搜索："+cookie.getName()+":"+cookie.getValue());
//		}
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
		
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
}
