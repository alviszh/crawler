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

import app.utils.CommonUtils;

/**
 * @author Administrator
 *
 */
public class Test2 {

private static Set<Cookie> cookies;
	
	public static final String URl = "http://sdzb.hrss.gov.cn:8001/logonDialog.jsp";
	
	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		HtmlPage html = getHtml(URl, webClient, 1);
		
//		HtmlTextInput loginName = (HtmlTextInput)html.getFirstByXPath("//input[@id='yhmInput']");
//		HtmlPasswordInput loginPassword = (HtmlPasswordInput)html.getFirstByXPath("//input[@id='mmInput']");
		HtmlElement submitbt = (HtmlElement)html.getFirstByXPath("//img[@class='logonBtn']");
		HtmlImage validateImage = html.getFirstByXPath("//img[@id='validatecode1']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("E:\\Codeimg\\"+imageName);
		validateImage.saveAs(file);
		
//		HtmlTextInput validateInput = (HtmlTextInput)html.getFirstByXPath("//input[@id='validatecodevalue1']");
//		HtmlElement submitbt = (HtmlElement)html.getFirstByXPath("//img[@class='logonBtn']");
		
//		loginName.setText("370322199012150225");
//		loginPassword.setText("zb123456");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
//		validateInput.setText(input);
		CommonUtils commonUtils = new CommonUtils();
		String md5Pwd = commonUtils.md5("zb123456");
		String attribute = submitbt.getAttribute("onclick");
		String appversion = attribute.substring(attribute.indexOf("(")+2, attribute.indexOf(",")-1);
		
		WebRequest  requestSettings = new WebRequest(new URL("http://sdzb.hrss.gov.cn:8001/logon.do"), HttpMethod.GET);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("method", "doLogon"));
		requestSettings.getRequestParameters().add(new NameValuePair("_xmlString", "<?xml version='1.0' encoding='UTF-8'?><p><s userid='370322199012150225'/><s usermm='"+md5Pwd+"'/><s authcode='"+input+"'/><s yxzjlx='A'/><s appversion='"+appversion+"'/></p>"));
		requestSettings.getRequestParameters().add(new NameValuePair("_random", "0.9454396308620945"));
		
		HtmlPage page = webClient.getPage(requestSettings);
		String contentAsString = page.getWebResponse().getContentAsString();
		System.out.println(contentAsString);
		
		System.out.println("-------------------");
		System.out.println(contentAsString);
		System.out.println("-------------------");
		
		HtmlPage html2 = getHtml(URl, webClient, 1);
		
		HtmlTextInput loginName2 = (HtmlTextInput)html2.getFirstByXPath("//input[@id='yhmInput']");
		HtmlPasswordInput loginPassword2 = (HtmlPasswordInput)html2.getFirstByXPath("//input[@id='mmInput']");
		HtmlImage validateImage2 = html2.getFirstByXPath("//img[@id='validatecode1']");
		String imageName2 = UUID.randomUUID() + ".jpg";
		File file2 = new File("E:\\Codeimg\\"+imageName2);
		validateImage2.saveAs(file2);
		
		HtmlTextInput validateInput2 = (HtmlTextInput)html2.getFirstByXPath("//input[@id='validatecodevalue1']");
		HtmlElement submitbt2 = (HtmlElement)html2.getFirstByXPath("//img[@class='logonBtn']");
		
		loginName2.setText("370322199012150225");
		loginPassword2.setText("zb123456");
		@SuppressWarnings("resource")
		Scanner scanner2 = new Scanner(System.in);
		String input2 = scanner2.next();
		validateInput2.setText(input2);
		
		HtmlPage loginedPage = submitbt2.click();
		
		String localPath = "E:\\crawler\\zibo\\loginErro1.txt";
		savefile(localPath,loginedPage.asXml());
		
		String url = loginedPage.getUrl().toString();
		System.out.println(url);
		
		String usersession = url.substring(url.indexOf("=")+1, url.indexOf("&",url.indexOf("=")+1));
		System.out.println("-------------");
		System.out.println(usersession);
		System.out.println("-------------");
		
		Element element = Jsoup.parse(loginedPage.asXml()).select(".dw-laneContainer-mainLane").first();
		String laneID = element.attr("id");
		System.out.println(laneID);
		
		String method = "fwdQueryPerInfo";
		
		HtmlPage userinfoPage = getHtmlByPost("http://sdzb.hrss.gov.cn:8001/hspUser.do",webClient,method,usersession,laneID);
		
		String userinfoPath = "E:\\crawler\\zibo\\userinfo.txt";
		savefile(userinfoPath,userinfoPage.asXml());
		
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
