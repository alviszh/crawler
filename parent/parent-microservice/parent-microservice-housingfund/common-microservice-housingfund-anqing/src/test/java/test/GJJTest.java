package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.crawler.htmlparse.HousingAnQingParse;

public class GJJTest {

	static String url = "http://106.15.197.102:8888/Default.aspx";

	private static WebClient webClient = null;

	public static void main(String[] args) throws Exception {
		webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage htmlPage = (HtmlPage) getHtml(url, webClient);
		System.out.println(htmlPage.asXml());

		HtmlForm form = htmlPage.getFirstByXPath("/html/body/table[3]/tbody/tr[1]/td[2]/form");

		HtmlInput nameInput = form.getFirstByXPath("//*[@id='loginName']");
		HtmlInput passInput = form.getFirstByXPath("//*[@id='loginPwd']");

		nameInput.setValueAttribute("king3715");
		passInput.setValueAttribute("king3715");

		HtmlLabel valiCodeImg = htmlPage.getFirstByXPath("//*[@id='autoRandom']");
	
		String valicodeStr = valiCodeImg.asText();
		System.out.println("===================="+valicodeStr);

		HtmlInput verfiyInput = form.getFirstByXPath("//*[@id='marketCaptchaCode']");

		verfiyInput.setValueAttribute(valicodeStr);

		HtmlElement loginButton = (HtmlElement) form.getFirstByXPath("//*[@id='button']");
		htmlPage = loginButton.click();
		System.out.println("=================================================");
		System.out.println(htmlPage.asXml());
		//HousingAnQingParse.userinfo_parse(htmlPage.asXml());

		url = "http://139.224.222.226:8888/details.aspx?page=1";
		 htmlPage = (HtmlPage) getHtml(url, webClient);
		System.out.println(htmlPage.asXml());
		HousingAnQingParse.pay_parse(htmlPage.asXml());
	}

	public static List<String> getURLforUserInfo(String html) {
		List<String> urllist = new ArrayList<>();
		Document doc = Jsoup.parse(html);
		Elements urlas = doc.select("td.style21").select("a");
		System.out.println(urlas);
		for (Element urla : urlas) {
			String urlString = urla.attr("onclick");

			String url = "http://www.bjgjj.gov.cn/wsyw/wscx/" + urlString
					.substring(urlString.indexOf("(") + 1, urlString.indexOf(",")).replaceAll("\"", "").trim();
			System.out.println("====" + url);
			urllist.add(url);
		}
		return urllist;
	}

	public static String getURLforPay(String html) {
		Document doc = Jsoup.parse(html);

		System.out.println(doc);
		String urlString = doc.select("a:contains(查看历史明细账信息)").attr("onclick");

		System.out.println(urlString);
		String url = "http://www.bjgjj.gov.cn/wsyw/wscx/"
				+ urlString.substring(urlString.indexOf("(") + 1, urlString.indexOf(",")).replaceAll("\'", "").trim();
		System.out.println("缴费url == " + url);

		return url;
	}

	public static void getResult(String url, WebClient webClient) throws Exception {
		Page page = getHtml(url, webClient);
	
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

}
