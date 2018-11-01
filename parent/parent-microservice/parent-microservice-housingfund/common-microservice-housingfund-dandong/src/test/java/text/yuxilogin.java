package text;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class yuxilogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.yxgjj.com/";
		HtmlPage page = getHtml(url, webClient);
		String contentAsString = page.getWebResponse().getContentAsString();
		System.out.println(contentAsString);
		HtmlCheckBoxInput sle = (HtmlCheckBoxInput)page.getElementById("#");
		sle.click();
		
		HtmlTextInput xm = (HtmlTextInput)page.getElementByName("xm");
		HtmlTextInput id_card_input = (HtmlTextInput)page.getElementByName("id_card_input");
		HtmlSubmitInput submit = (HtmlSubmitInput) page.getFirstByXPath("//input[@class='login-submit']");
		xm.setText("");//张露婷
		id_card_input.setText("");//530424199604270042
		
		Page page2 = submit.click();
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		if(html.indexOf("开始年份：")!=-1){
			System.out.println("登录成功");
//			String encodeName=URLEncoder.encode("张露婷", "utf-8");
			//个人信息
			Document doc = Jsoup.parse(html);
			Element elementsByTag = doc.getElementsByTag("table").get(6);
			String text = elementsByTag.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text();
			System.out.println(text);
			String[] split = text.split(" ");
			
			String[] split1 = split[0].split(":");
			String string = split1[1];//姓名
			
			String[] split2 = split[1].split(":");
			String string2 = split2[1];//单位
			
			String[] split3= split[2].split(":");
			String string3 = split3[1];//月缴交额
			
			String[] split4= split[3].split(":");
			String string4 = split4[1];//帐户余额
			
			String[] split5= split[4].split(":");
			String string5 = split5[1];//账号
			
			System.out.println("姓名:"+string+"\r单位:"+string2
					+"\r月缴:"+string3+"\r账户余额:"+string4+"\r帐号："+string5);
			
			//流水
			Elements tag = doc.getElementsByClass("9size").get(0).getElementsByTag("tr");
			for (int i = 2; i < tag.size(); i++) {
				String tex = tag.get(i).getElementsByTag("td").get(0).text();//日期
				String text1 = tag.get(i).getElementsByTag("td").get(1).text();//摘要
				String text2 = tag.get(i).getElementsByTag("td").get(2).text();//收入
				String text3 = tag.get(i).getElementsByTag("td").get(3).text();//支出
				String text4 = tag.get(i).getElementsByTag("td").get(4).text();//余额
				System.out.println("日期："+tex+"\r摘要:"+text1
						+"\r收入："+text2+"\r支出："+text3+"\r余额:"+text4+"\r");
			}
			
		}else{
			Document doc = Jsoup.parse(html);
			String error = "网络繁忙";
			Elements elementsByTag = doc.getElementsByTag("td");
			if(elementsByTag.size()>2){
				error = elementsByTag.get(2).text();
			}
			if(elementsByTag.size()<3){
				error = elementsByTag.get(1).text();
			}
			System.out.println("登录失败:"+error);
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
		webRequest.setCharset(Charset.forName("gbk"));
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
