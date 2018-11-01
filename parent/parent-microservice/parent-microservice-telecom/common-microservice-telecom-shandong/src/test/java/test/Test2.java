package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

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
import test.entity.Item;

public class Test2 {
	
	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("17335671020");
		passwordInput.setText("921003");
		Page click = button.click();
		
		String url1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000355";
		HtmlPage html1 = getHtml(url1, webClient);
		
		/*String url2 = "http://www.189.cn/login/sso/ecs.do?method=linkTo&platNo=10017&toStUrl=http://ha.189.cn/service/iframe/feeQuery_iframe.jsp?SERV_NO=FSE-2-2&fastcode=20000356&cityCode=ha";
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.GET);
		webRequest1.setAdditionalHeader("Host", "www.189.cn");
		webRequest1.setAdditionalHeader("Referer", "http://www.189.cn/dqmh/my189/initMy189home.do");
		webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		HtmlPage page2 = webClient.getPage(webRequest1);
		savefile("E:\\crawler\\telecomshandong\\henan.txt", page2.asXml());*/
		
		/*WebRequest webRequest = new WebRequest(new URL("http://ha.189.cn/service/bill/getRand.jsp"), HttpMethod.POST);
		
		webRequest.setAdditionalHeader("Host", "ha.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://ha.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://ha.189.cn/service/iframe/feeQuery_iframe.jsp?SERV_NO=FSE-2-2&fastcode=20000356&cityCode=ha");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("PRODTYPE", "793074235849"));
		webRequest.getRequestParameters().add(new NameValuePair("RAND_TYPE", "002"));
		webRequest.getRequestParameters().add(new NameValuePair("BureauCode", "0379"));
		webRequest.getRequestParameters().add(new NameValuePair("ACC_NBR", "17335671020"));
		webRequest.getRequestParameters().add(new NameValuePair("PROD_TYPE", "793074235849"));
//		webRequest.getRequestParameters().add(new NameValuePair("PROD_PWD", ""));
		webRequest.getRequestParameters().add(new NameValuePair("REFRESH_FLAG", "1"));
//		webRequest.getRequestParameters().add(new NameValuePair("BEGIN_DATE", ""));
//		webRequest.getRequestParameters().add(new NameValuePair("END_DATE", ""));
		webRequest.getRequestParameters().add(new NameValuePair("ACCT_DATE", "201709"));
		webRequest.getRequestParameters().add(new NameValuePair("FIND_TYPE", "2"));
//		webRequest.getRequestParameters().add(new NameValuePair("SERV_NO", ""));
		webRequest.getRequestParameters().add(new NameValuePair("QRY_FLAG", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("ValueType", "4"));
		webRequest.getRequestParameters().add(new NameValuePair("MOBILE_NAME", "17335671020"));
		webRequest.getRequestParameters().add(new NameValuePair("OPER_TYPE", "CR1"));
//		webRequest.getRequestParameters().add(new NameValuePair("PASSWORD", ""));
		
		Page page = webClient.getPage(webRequest);
		
		System.out.println(page.getWebResponse().getContentAsString());
		System.out.println("请输入短信验证码");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String verifycode = scanner.next();*/
		
		String url2 = "http://ha.189.cn/service/iframe/bill/iframe_inxxall.jsp?PRODTYPE=793074235849&RAND_TYPE=002&BureauCode=0379&ACC_NBR=17335671020&PROD_TYPE=793074235849&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&ACCT_DATE=201709&FIND_TYPE=2&SERV_NO=&QRY_FLAG=1&ValueType=4&MOBILE_NAME=17335671020&OPER_TYPE=CR1&PASSWORD=789789";
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.GET);
		
		webRequest1.setAdditionalHeader("Host", "ha.189.cn");
		webRequest1.setAdditionalHeader("Origin", "http://ha.189.cn");
		webRequest1.setAdditionalHeader("Referer", "http://ha.189.cn/service/iframe/feeQuery_iframe.jsp?SERV_NO=FSE-2-2&fastcode=20000356&cityCode=ha");
		webRequest1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		/*webRequest1.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest1.getRequestParameters().add(new NameValuePair("PRODTYPE", "793074235849"));
		webRequest1.getRequestParameters().add(new NameValuePair("RAND_TYPE", "002"));
		webRequest1.getRequestParameters().add(new NameValuePair("BureauCode", "0379"));
		webRequest1.getRequestParameters().add(new NameValuePair("ACC_NBR", "17335671020"));
		webRequest1.getRequestParameters().add(new NameValuePair("PROD_TYPE", "793074235849"));
		webRequest1.getRequestParameters().add(new NameValuePair("PROD_PWD", ""));
		webRequest1.getRequestParameters().add(new NameValuePair("REFRESH_FLAG", "1"));
		webRequest1.getRequestParameters().add(new NameValuePair("BEGIN_DATE", ""));
		webRequest1.getRequestParameters().add(new NameValuePair("END_DATE", ""));
		webRequest1.getRequestParameters().add(new NameValuePair("ACCT_DATE", "201709"));
		webRequest1.getRequestParameters().add(new NameValuePair("FIND_TYPE", "2"));
		webRequest1.getRequestParameters().add(new NameValuePair("SERV_NO", ""));
		webRequest1.getRequestParameters().add(new NameValuePair("QRY_FLAG", "1"));
		webRequest1.getRequestParameters().add(new NameValuePair("ValueType", "4"));
		webRequest1.getRequestParameters().add(new NameValuePair("MOBILE_NAME", "17335671020"));
		webRequest1.getRequestParameters().add(new NameValuePair("OPER_TYPE", "CR1"));
		webRequest1.getRequestParameters().add(new NameValuePair("PASSWORD", verifycode));*/
		
		HtmlPage page2 = webClient.getPage(webRequest1);
		System.out.println(page2.asXml());
		savefile("E:\\crawler\\telecomhenan\\henan.txt", page2.asXml());
		

	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
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
	
	//通过身份验证1和身份验证2
	public static Page getShenfen(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/bill/toDetailBillFra.parser?fastcode=00710602&cityCode=jl"), HttpMethod.GET);
		HtmlPage yanzhengPage1 = webClient.getPage(webRequest);
		Thread.sleep(3000);
		HtmlTextInput idNum = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='certCode']");
		HtmlTextInput name = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='cust_name']");
		HtmlTextInput validateInput = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='vCode2']");
		HtmlElement submitbt = (HtmlElement)yanzhengPage1.getFirstByXPath("//a[@class='btn-1']");
		HtmlImage validateImage = yanzhengPage1.getFirstByXPath("//img[@id='vImgCode2']");
		
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("E:\\Codeimg\\"+imageName);
		validateImage.saveAs(file);
		//输入图片验证码1
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		idNum.setText("220102198908196169");
		name.setText("苗金玲");
		validateInput.setText(input);
		HtmlPage yanzhengPage2 = submitbt.click();
		if(null != yanzhengPage2){
//			Thread.sleep(3000);
//			HtmlTextInput validateInput2 = (HtmlTextInput) yanzhengPage2.getFirstByXPath("//input[@id='vCode']");
//			HtmlTextInput msgCodeInput = (HtmlTextInput) yanzhengPage2.getFirstByXPath("//input[@id='sRandomCode']");
//			HtmlElement submitbt2 = (HtmlElement)yanzhengPage2.getFirstByXPath("//a[@class='btn-1']");
			HtmlImage validateImage2 = yanzhengPage2.getFirstByXPath("//img[@id='vImgCode']");
			
			//输入图片验证码2
			validateImage2.saveAs(file);
			@SuppressWarnings("resource")
			Scanner scanner2 = new Scanner(System.in);
			String input2 = scanner2.next();
//			validateInput2.setText(input2);
			
			//输入短信验证码
			@SuppressWarnings("resource")
			Scanner scanner3 = new Scanner(System.in);
			String input3 = scanner3.next();
			
//			msgCodeInput.setText(input3);
//			HtmlPage page = submitbt2.click();
			
			WebRequest webRequest2 = new WebRequest(new URL("http://jl.189.cn/service/bill/doDetailBillFra.parser"), HttpMethod.POST);
			webRequest2.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest2.getRequestParameters().add(new NameValuePair("sRandomCode", input3));
			webRequest2.getRequestParameters().add(new NameValuePair("randCode", input2));
			Page searchPage = webClient.getPage(webRequest2);
			
			return searchPage;
		}
		return null;
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
	
}
