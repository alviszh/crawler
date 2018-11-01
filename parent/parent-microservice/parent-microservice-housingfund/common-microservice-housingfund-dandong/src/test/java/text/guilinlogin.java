package text;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class guilinlogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.glzfgjj.cn:8136/";
		HtmlPage page = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("ctl00_ContentPlaceHolder1_TextBox1");
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("ctl00_ContentPlaceHolder1_TextBox2");
		HtmlSubmitInput login = (HtmlSubmitInput) page.getElementById("ctl00_ContentPlaceHolder1_btnSearch");
		
		username.setText("450324199303132249");//450324199303132249
		pass.setText("132249");//132249
		Page page2 = login.click();
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		Document doc = Jsoup.parse(html);
		String text = doc.getElementById("ctl00_ContentPlaceHolder1_Panel3").text();
		String[] split = text.split(" ");
		String[] split2 = split[0].split("：");
		String name = split2[1].trim();//姓名
		System.out.println(name);
		String[] split3 = split[1].split("：");
		String idcard = split3[1].trim();//身份证号码
		System.out.println(idcard);
		Elements elementsByTag = doc.getElementById("ctl00_ContentPlaceHolder1_GridView1").getElementsByTag("tr");
		for (int i = 1; i < elementsByTag.size(); i++) {
			String text2 = elementsByTag.get(i).getElementsByTag("td").get(0).text();//记账日期
			String text3 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//摘要
			String text4 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//减少金额
			String text5 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//增加金额
			String text6 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//累计金额
			System.out.println("身份证："+idcard+"\r姓名："+name+"\r记账日期:"+text2+"\r摘要:"+text3+"\r减少金额："+text4+"\r增加金额:"+text5+"\r累计金额:"+text6+"\r");
		}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page getHtml1(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
