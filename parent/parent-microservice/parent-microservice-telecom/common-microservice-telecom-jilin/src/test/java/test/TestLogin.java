package test;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

import test.entity.Item;

public class TestLogin {

	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("18004413282");
		passwordInput.setText("122333");

		HtmlPage htmlpage = button.click();
//		System.out.println(htmlpage.asXml());
		HtmlPage htmlPage2 = getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00710602", webClient);
		Thread.sleep(2000);
//		System.out.println(htmlPage2.asXml());
		HtmlPage userInfoPage = getUserInfo(webClient);
//		System.out.println(userInfoPage.asXml());
//		Page shenfen = getShenfen(webClient);
//		String shenfen1 = "E:\\crawler\\telecomJilin\\shenfenInfo.txt";
//		savefile(shenfen1,shenfen.getWebResponse().getContentAsString());
//		if(null != shenfen){
//			HtmlPage shihuaInfo = getShihuaInfo(webClient);
//			String shihua = "E:\\crawler\\telecomJilin\\shihuaInfo.txt";
//			savefile(shihua,shihuaInfo.asXml());
//		}
//		String url1 = "http://jl.189.cn/service/bill/queryBillInfoFra.action?billingCycle=201704";
//		String url2 = "http://jl.189.cn/service/pay/queryDirectChargeVCFra.action?queryWebCard.startDate=2017-03-01&queryWebCard.endDate=2017-08-29&queryWebCard.bankFlag=1&queryWebCard.page=1&time=0.8519757522446632";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
////		webRequest.setAdditionalHeader("Cookie", "lvid=1bd4b26e3b67808b15e15918882e1399; nvid=1; _gscu_1708861450=03296508k374eo64; JSESSIONID_jl=khzZZl7hbDh8W2qlPz4hbJknQcyThTzT1hvQ2Q2C04hhvQhHm3dg!52509184; aactgsh111220=18004413282; userId=201%7C20160000000021506155; isLogin=logined; .ybtj.189.cn=E302EC249511839911A7AF718368AAA2; loginStatus=logined; trkHmCity=0; trkHmLinks=0; trkHmCitycode=0; trkHmCoords=0; trkHmPageName=0; trkHmClickCoords=0; s_sq=%5B%5BB%5D%5D; cityCode=jl; SHOPID_COOKIEID=10030; s_fid=5A8972EF27951F0F-10DBB57352887DC7; trkId=DA81A784-B624-4777-A6BB-DD9D4D29B851; s_cc=true");
//		webRequest.setAdditionalHeader("Host", "jl.189.cn");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		
//		Page chongzhiRecPage = webClient.getPage(webRequest);
//		HtmlPage chongzhiRecPage = getHtml("", webClient);
//		String chongzhiRec = chongzhiRecPage.getWebResponse().getContentAsString();
//		Gson gson = new Gson();
//		
//		JsonParser parser = new JsonParser();
//		JsonObject object = (JsonObject) parser.parse(chongzhiRec); // 创建JsonObject对象
//		JsonObject pagination = object.get("pagination").getAsJsonObject();
//		JsonArray items = pagination.get("items").getAsJsonArray();
//		for (JsonElement jsonElement : items) {
//			Item fromJson = gson.fromJson(jsonElement, Item.class);
//			
//			JsonObject item = jsonElement.getAsJsonObject();
//			String bonusInfo = item.get("bonusInfo").getAsString();
//			String cardNo = item.get("cardNo").getAsString();
//			String rechargeAmount = item.get("rechargeAmount").getAsString();
//			String rechargeFlag = item.get("rechargeFlag").getAsString();
//			String rechargeFlowAmount = item.get("rechargeFlowAmount").getAsString();
//			String rechargeTime = item.get("rechargeTime").getAsString();
//			String rechargeUnit = item.get("rechargeUnit").getAsString();
//			String rechargeUser = item.get("rechargeUser").getAsString();
//			String rechargeUserType = item.get("rechargeUserType").getAsString();
//			String result = item.get("result").getAsString();
//			System.out.println(item.toString());
//		}
		
		Document doc = Jsoup.parse(userInfoPage.asXml());
		String kehuname = getNextLabelByKeyword(doc,"客户名称","th");
		String kehutype = getNextLabelByKeyword(doc,"客户类型","th");
		String zhengjiantype = getNextLabelByKeyword(doc,"证件类型","th");
		String zhengjiannum = getNextLabelByKeyword(doc,"证件号码","th");
		String tongxindizhi = doc.select("#custAddress_new").val();
		String dianhua = doc.select("#custPhone_new").val();
		
		System.out.println(tongxindizhi+dianhua);
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
