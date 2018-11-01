package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class liuzhoulogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.lzzfgjj.com/login.jspx";
		HtmlPage page = getHtml(url, webClient);
		
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("password");
		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/captcha.svl']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlSubmitInput submit = (HtmlSubmitInput) page.getElementById("submit");
		
		username.setText("452225198306020021");//452225198306020021
		pass.setText("0781065778");//0781065778
		captcha.setText(inputValue);
		submit.click();
		String url2 = "http://www.lzzfgjj.com/grcx/grcx_grjbqk.jspx";
		Page page3 = getHtml1(url2, webClient);
		String html2 = page3.getWebResponse().getContentAsString();
		if(html2.indexOf("退出")!=-1){
			System.out.println("登录成功");
			
			//个人信息
			/*String url3 = "http://www.lzzfgjj.com/grcx/grcx_grjbqk.jspx";
			Page page4 = getHtml1(url3, webClient);
			String html4 = page4.getWebResponse().getContentAsString();
			System.out.println(html4);
			Document document = Jsoup.parse(html4);
			Elements tag = document.getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = tag.get(0).getElementsByTag("td").get(0).text();//姓名
			String text2 = tag.get(0).getElementsByTag("td").get(1).text();//个人编号
			String text3 = tag.get(0).getElementsByTag("td").get(2).text();//缴存状态
			String text4 = tag.get(1).getElementsByTag("td").get(0).text();//单位账号
			String text5 = tag.get(1).getElementsByTag("td").get(1).text();//身份证
			String text6 = tag.get(2).getElementsByTag("td").get(0).text();//单位名称
			String text7 = tag.get(3).getElementsByTag("td").get(0).text();//所属网点名称
			String text8 = tag.get(4).getElementsByTag("td").get(0).text();//月缴存基数
			String text9 = tag.get(4).getElementsByTag("td").get(1).text();//单位缴存比例(%)
			String text10 = tag.get(4).getElementsByTag("td").get(2).text();//个人缴存比例(%)
			String text11 = tag.get(5).getElementsByTag("td").get(0).text();//单位月缴存额
			String text12 = tag.get(5).getElementsByTag("td").get(1).text();//个人月缴存额
			String text13 = tag.get(5).getElementsByTag("td").get(2).text();//合计月缴存额
			String text14 = tag.get(6).getElementsByTag("td").get(0).text();//累计提取额
			String text15 = tag.get(6).getElementsByTag("td").get(1).text();//账户余额
			String text16 = tag.get(6).getElementsByTag("td").get(2).text();//当前缴至年月
			System.out.println(text16);*/
			
			//流水
			String url5 = "http://www.lzzfgjj.com/grcx/grcx_grzmmx.jspx";
			Page page2 = getHtml1(url5, webClient);
			String html3 = page2.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html3);
			System.out.println(html3);
			Elements byTag = doc.getElementsByTag("table").get(1).getElementsByTag("tr");
		    Element element = doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(0);
			for (int i = 1; i < byTag.size()-1; i++) {
				String text = byTag.get(i).getElementsByTag("td").get(0).text();//业务日期
				String text2 = byTag.get(i).getElementsByTag("td").get(1).text();//流水号
				String text3 = byTag.get(i).getElementsByTag("td").get(2).text();//业务类型
				String text4 = byTag.get(i).getElementsByTag("td").get(3).text();//缴存年月
				String text5 = byTag.get(i).getElementsByTag("td").get(4).text();//增加额
				String text6 = byTag.get(i).getElementsByTag("td").get(5).text();//减少额
				String text7 = byTag.get(i).getElementsByTag("td").get(6).text();//余额
				String text8 = byTag.get(i).getElementsByTag("td").get(7).text();//缴存单位账号
				String text9 = element.getElementsByTag("td").get(0).text();//姓名
				String text10 = element.getElementsByTag("td").get(1).text();//身份证
				System.out.println(text10);
			}
		}else{
			String alertMsg = WebCrawler.getAlertMsg();
			System.out.println("登录失败:"+alertMsg);
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
