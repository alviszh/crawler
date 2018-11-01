package test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInsertedText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class nanping {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://103.37.61.29/saving/login.aspx";
		HtmlPage pag = getHtml(url, webClient);
		HtmlButtonInput button = pag.getFirstByXPath("//input[@type='button']");
		HtmlPage page = button.click();

		HtmlTextInput user = (HtmlTextInput) page.getElementById("ctl00_cphMain_tbxUserId");
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("ctl00_cphMain_tbxPasswd");

		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='imgCheckCode']");//验证码
		String imageName = "111.jpg";
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");

		HtmlTextInput yzm = (HtmlTextInput) page.getElementById("ctl00_cphMain_tbxCheck");

		user.setText("350784199006152029");//350784199006152029
		pass.setText("152029");//152029
		yzm.setText(inputValue);

		HtmlSubmitInput sub = (HtmlSubmitInput) page.getElementById("ctl00_cphMain_Button1");
		HtmlPage page2 = sub.click();

		String html = page2.getWebResponse().getContentAsString();

		System.out.println(html);
		Thread.sleep(2000);
		String url2 = "http://103.37.61.29/saving/Main.aspx";
		HtmlPage page3 = getHtml(url2, webClient);
		String html2 = page3.getWebResponse().getContentAsString();

		System.out.println(html2);
		
		if(html2.indexOf("退出")!=-1){
			System.out.println("登录成功");
			Elements elementsByTag = Jsoup.parse(html2).getElementById("ctl00_cphMain_dgMain").getElementsByTag("td");
			String string = elementsByTag.get(1).text().trim();//帐号
			String string2 = elementsByTag.get(3).text().trim();//身份证
			String string3 = elementsByTag.get(5).text().trim();//姓名
			String string4 = elementsByTag.get(7).text().trim();//工作单位
			String string5 = elementsByTag.get(9).text().trim();//余额
			String string6 = elementsByTag.get(11).text().trim();//开户银行
			String string7 = elementsByTag.get(13).text().trim();//截至日期
			
			System.out.println(string+string2+string3+string4+string5+string6+string7);
		}else{
			String asText = page2.getElementById("ctl00_cphMain_lblMsg").asText();
			System.out.println(asText);
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
