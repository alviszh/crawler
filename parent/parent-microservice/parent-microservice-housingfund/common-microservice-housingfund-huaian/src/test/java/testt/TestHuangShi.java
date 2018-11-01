package testt;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

public class TestHuangShi {

	public static void main(String[] args) throws Exception{
		login();
	}
	
	
	public static void login() throws Exception{
		ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
		
		String loginUrl = "http://wsfw.hs12333.gov.cn/login.ered?reqCode=queryPerson&cardno=420281198804250042&area=1";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		Page loginPage = webClient.getPage(webRequest);
		
		System.out.println("loginPage--->"+loginPage.getWebResponse().getContentAsString());
		
		String loginUrl2 = "http://wsfw.hs12333.gov.cn/login.ered?reqCode=login&cardno=420281198804250042&password=880425&persionId=13574820&area=1";
		webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.GET);
		HtmlPage loginPage2 = webClient.getPage(webRequest);
		System.out.println("loginPage2--->"+loginPage2.getWebResponse().getContentAsString());
		
		String userinfoUrl = "http://wsfw.hs12333.gov.cn/person.ered?reqCode=Init1";
		webRequest = new WebRequest(new URL(userinfoUrl), HttpMethod.GET);
		HtmlPage userinfoPage = webClient.getPage(webRequest);
		System.out.println("userinfoPage--->"+userinfoPage.asXml());
		
		String medicalUrl = "http://wsfw.hs12333.gov.cn/cost.ered?reqCode=findList&start=0&limit=500&OID=c42907ec3cf2462f931624745ce378cf&type=31";
		webRequest = new WebRequest(new URL(medicalUrl), HttpMethod.GET);
		Page medicalPage = webClient.getPage(webRequest);
		System.out.println("medicalPage--->"+medicalPage.getWebResponse().getContentAsString());
		
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject)parser.parse(medicalPage.getWebResponse().getContentAsString());
		JsonArray root = obj.get("ROOT").getAsJsonArray();
		for (JsonElement jsonElement : root) {
			JsonObject object = jsonElement.getAsJsonObject();
			System.out.println("info-->"+object.get("1").getAsString());
			System.out.println("info-->"+object.get("2").getAsString());
			System.out.println("info-->"+object.get("3").getAsString());
			System.out.println("info-->"+object.get("4").getAsString());
			System.out.println("info-->"+object.get("5").getAsString());
			System.out.println("info-->"+object.get("6").getAsString());
			System.out.println("info-->"+object.get("7").getAsString());
			System.out.println("info-->"+object.get("8").getAsString());
			System.out.println("info-->"+object.get("9").getAsString());
			System.out.println("info-->"+object.get("10").getAsString());
			System.out.println("info-->"+object.get("11").getAsString());
			System.out.println("info-->"+object.get("12").getAsString());
			System.out.println("info-->"+object.get("13").getAsString());
			System.out.println("info-->"+object.get("14").getAsString());
			System.out.println("info-->"+object.get("15").getAsString());
			System.out.println("info-->"+object.get("16").getAsString());
			System.out.println("info-->"+object.get("17").getAsString());
			System.out.println("info-->"+object.get("18").getAsString());
			System.out.println("------------*--------------");
		}
		
		/*HtmlTextInput username = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='username']");
		HtmlPasswordInput password = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@id='in_password']");
		HtmlTextInput captcha = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='captcha']");
		HtmlImage safecode = (HtmlImage)loginPage.getFirstByXPath("//img[@style='cursor: pointer;']");
		HtmlButton gr_login = (HtmlButton)loginPage.getFirstByXPath("//button[@id='gr_login']");
		
		File file = new File("E:\\Codeimg\\jingzhou.jpg");
		safecode.saveAs(file);
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		
		username.setText("420983199011037239");
		password.setText("888888");
		captcha.setText(input);
		HtmlPage loginedPage = gr_login.click();
		
		String loginUrl2 = "http://58.54.135.133/wt-web/home?logintype=1";
		webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.GET);
		HtmlPage loginedPage2 = webClient.getPage(webRequest);*/
		
//		System.out.println("登陆后的页面--》"+loginedPage2.asXml());
//		
//		String infoUrl = "http://58.54.135.133/wt-web/personal/jcmxlist";
//		webRequest = new WebRequest(new URL(infoUrl), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Referer", "http://www.hagjj.com/office/geren/login.asp");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Host", "58.54.135.133");
//		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
//		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		
//		webRequest.setRequestBody("UserId=1&beginDate=2016-01-01&endDate=2018-03-13&userId=1&pageNum=1&pageSize=100&totalcount=43&pages=1");
//		Page infoPage = webClient.getPage(webRequest);
//		System.out.println("信息的页面--》"+infoPage.getWebResponse().getContentAsString());
//		
//		String loginUrl3 = "http://www.hagjj.com/office/geren/grgjjcx.asp";
//		webRequest = new WebRequest(new URL(loginUrl3), HttpMethod.GET);
//		HtmlPage loginedPage3 = webClient.getPage(webRequest);
//		System.out.println("数据页面1--》"+loginedPage3.asXml());
//		
//		Document document = Jsoup.parse(loginedPage3.asXml());
//		String name = getNextLabelByKeyword(document, "td", "姓    名：");
//		System.out.println("姓名是--》"+name);
		
		/*String loginUrl4 = "http://www.hagjj.com/office/geren/grgjjcx0.asp";
		webRequest = new WebRequest(new URL(loginUrl4), HttpMethod.GET);
		HtmlPage loginedPage4 = webClient.getPage(webRequest);
		System.out.println("数据页面2--》"+loginedPage4.asXml());*/
		
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
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
	
}
