package test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class jiujianglogin {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.jjzfgjj.com/caxun.aspx";
		HtmlPage pag = getHtml(url, webClient);
		HtmlImage imag = (HtmlImage) pag.getFirstByXPath("//img[@id='captchaimg']");
		HtmlPage page = (HtmlPage) imag.click();
		
		HtmlTextInput username = (HtmlTextInput) page.getElementById("UserCard");
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("Password");
		
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='captchaimg']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		HtmlTextInput yzm = (HtmlTextInput) page.getElementByName("CaptchaText");
		
		username.setText("360402199308031522");
		pass.setText("111111");//111111
		yzm.setText(inputValue);
		
		HtmlSubmitInput button1 = (HtmlSubmitInput) page.getElementById("Button1");
		Page page2 = button1.click();
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		String error = WebCrawler.getAlertMsg();
		if(html.indexOf("单位名称：")!=-1){
			System.out.println("登录成功");
			Document document = Jsoup.parse(html);
			String string = document.getElementsByTag("td").get(1).text().trim();
			String string2 = document.getElementsByTag("td").get(3).text().trim();
			String string3 = document.getElementsByTag("td").get(5).text().trim();
			String string4 = document.getElementsByTag("td").get(7).text().trim();
			String string5 = document.getElementsByTag("td").get(9).text().trim();
			String string6 = document.getElementsByTag("td").get(11).text().trim();
			String string7 = document.getElementsByTag("td").get(13).text().trim();
			System.out.println(string+string2+string3+string4+string5+string6+string7);
		}else{
			System.out.println("登录失败："+error);
		}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
