package text;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.service.common.LoginAndGetCommon;

public class login {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.czgjj.net/";
		HtmlPage page = (HtmlPage)getHtml(url, webClient);
		String html = page.getWebResponse().getContentAsString();
		HtmlTextInput xm = (HtmlTextInput) page.getElementById("xm");//真实姓名
		HtmlTextInput sfzh = (HtmlTextInput) page.getElementById("sfzh");//身份证
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@alt='点击更换验证码']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		HtmlTextInput yzm = (HtmlTextInput) page.getElementByName("CheckCodex");
		
		xm.setText("贺君霞");
		sfzh.setText("130202197603210627");
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		yzm.setText(inputValue);
		
		HtmlSubmitInput submit = (HtmlSubmitInput)page.getElementByName("button");
		Page page2 = submit.click();
		Thread.sleep(2000);
		String html2 = page2.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(html2);
		Element element = doc.getElementsByClass("box").get(0);
		String text = element.getElementsByClass("ul_search").get(0).getElementsByTag("li").get(0).text();//姓名
		String substring = text.substring(5, text.length()).trim();
		String text2 = element.getElementsByClass("ul_search").get(0).getElementsByTag("li").get(1).text();//身份证
		String trim = text2.substring(5, text2.length()).trim();
		String text3 = element.getElementsByClass("ul_search").get(1).getElementsByTag("li").get(0).text();//每月交缴额
		String trim2 = text3.substring(6, text3.length()).trim();
		String text4 = element.getElementsByClass("ul_search").get(1).getElementsByTag("li").get(1).text();//余额
		String trim3 = text4.substring(3,text4.length()).trim();
		String text5 = element.getElementsByClass("ul_search").get(1).getElementsByTag("li").get(2).text();//日期
		String trim4 = text5.substring(3,text5.length()).trim();
	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
