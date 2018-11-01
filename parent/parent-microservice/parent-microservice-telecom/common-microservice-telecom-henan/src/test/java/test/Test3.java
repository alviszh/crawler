package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

public class Test3 {
	
	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		/*String url = "http://www.cs12333.com/revision/";
		HtmlPage html = getHtml(url, webClient);
		savefile("E:\\crawler\\changsha\\login.txt", html.asXml());
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='uid1']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='upwd1']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//input[@id='loginBtn1']");
		username.setText("430181197607179146");
		passwordInput.setText("xxh9146");
		HtmlPage click = button.click();
		System.out.println(click.getBaseURI());
		savefile("E:\\crawler\\changsha\\logined.txt", click.asXml());*/
		
		String url1 = "http://www.cs12333.com/Online/onlineServiceActionPersonNormal.do?normalPersonUserName=430181197607179146&normalPersonPassWord=xxh9146&normalPersonUserType=4";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Host", "www.cs12333.com");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		webRequest.setAdditionalHeader("Referer", "http://www.cs12333.com/revision/");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		HtmlPage searchPage = webClient.getPage(webRequest);
//		System.out.println(searchPage.getBaseURI());
//		savefile("E:\\crawler\\changsha\\logined.txt", searchPage.asXml());
		
		String url2 = "http://www.cs12333.com/Online/modules/business/socialCard/view/module/SocialCardPersonModule.swf";
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.GET);
		webRequest1.setAdditionalHeader("Host", "www.cs12333.com");
		webRequest1.setAdditionalHeader("Accept", "*/*");
		webRequest1.setAdditionalHeader("Referer", "http://www.cs12333.com/Online/OnlineServiceAppliction.swf/[[DYNAMIC]]/2");
		webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		Page page = webClient.getPage(webRequest1);
		savefileByInputStream("E:\\crawler\\changsha\\sCard.swf", page.getWebResponse().getContentAsStream());
		
	}
	
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		webRequest.setAdditionalHeader("Host", "www.cs12333.com");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
//		webRequest.setAdditionalHeader("Referer", "http://www.cs12333.com/revision/");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	//获取市话详单页面信息
	public static HtmlPage getShihuaInfo(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/bill/billDetailQueryFra.parser"), HttpMethod.POST);
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("billDetailValidate", "true"));
		webRequest.getRequestParameters().add(new NameValuePair("billDetailType", "2"));
		webRequest.getRequestParameters().add(new NameValuePair("startTime", "2017-08-01"));
		webRequest.getRequestParameters().add(new NameValuePair("endTime", "2017-08-24"));
		webRequest.getRequestParameters().add(new NameValuePair("pagingInfo.currentPage", "0"));
		webRequest.getRequestParameters().add(new NameValuePair("contactID", "201708240948267398068"));
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	//获取客户资料页面信息
	public static HtmlPage getUserInfo(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/manage/modifyUserInfoFra.parser?fastcode=00700588&cityCode=jl"), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
	
	/**
	 * @Des 通过关键字获取具体的标签内容
	 * @param html
	 * @param keyword
	 * @return
	 */
	public String getMsgByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			return element.text();
		}
		return null;
	}
	
	//将String保存到本地
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
	
	//将String保存到本地
	public static void savefileByInputStream(String filePath, InputStream inputStream) throws Exception{
		
		FileOutputStream fos = new FileOutputStream(filePath);

		byte[] b = new byte[1024];

		while((inputStream.read(b)) != -1){

			fos.write(b);

		}

		inputStream.close();

		fos.close();
	}
	
}
