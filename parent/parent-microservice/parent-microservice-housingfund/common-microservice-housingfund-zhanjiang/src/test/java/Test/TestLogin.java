package Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		String url="https://www.zjzfgjj.gov.cn/Modules/GJJQuery/GJJLogin.aspx";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtzh']");
		searchpwd.setText("440803199106231129");
		
		HtmlPasswordInput searchpwd1 = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtPassword']");
		searchpwd1.setText("873569770");
		
        HtmlImage img = page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtAuthCode']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_txtAuthCodeEqual']");
		identifying.reset();
		identifying.setText(inputValue); 
		
		HtmlElement button = page.getFirstByXPath("//*[@id='MainContent_GjjLogin1_StaticBtnSubmit']");
		HtmlPage page2 = button.click();
		System.out.println(page2.getWebResponse().getContentAsString());
//		WebClient webClient2 = page2.getWebClient();
//		String contentAsString = page2.getWebResponse().getContentAsString();
//		HtmlDivision firstByXPath2 = page2.getFirstByXPath("//*[@id='__tab_MainContent_tabConainer_tabAccDetail']/div");
//		HtmlPage page4 = firstByXPath2.click();
//		System.out.println(page4.getWebResponse().getContentAsString());
		
//		WebClient webClient3 = page4.getWebClient();
		
		HtmlForm firstByXPath = page2.getFirstByXPath("//*[@id='form1']");
		System.out.println(firstByXPath);
		String[] split = firstByXPath.asXml().split("form1");
		String[] split2 = split[0].split("id");
		String substring = split2[1].substring(1, 33);
		System.out.println(substring);
		
		String url3="https://gjjcx.zjportal.net/Modules/GJJQuery/GJJDetail1.aspx?id="+substring+"&type=1";
		WebRequest webReque = new WebRequest(new URL(url3), HttpMethod.GET);
		Page page8 = webClient.getPage(webReque);
		System.out.println(page8.getWebResponse().getContentAsString());
		
//		String a ="ctl00$MainContent$ScriptManager1=ctl00$MainContent$UpdatePanel1|MainContent_tabConainer&__EVENTTARGET=MainContent_tabConainer&__EVENTARGUMENT=1&&MainContent_tabConainer_ClientState=%7B%22ActiveTabIndex%22%3A1%2C%22TabState%22%3A%5Btrue%2Ctrue%2Ctrue%5D%7D&__VIEWSTATE=";
//		
//		HtmlHiddenInput firstByXPath4 = (HtmlHiddenInput)page2.getFirstByXPath("//*[@id='__VIEWSTATE']");
//		String attribute = firstByXPath4.getAttribute("value");
//		System.out.println(firstByXPath4);
//		System.out.println(attribute);
//		String encode = URLEncoder.encode(attribute, "UTF-8");
//		
//		HtmlHiddenInput firstByXPath5 = (HtmlHiddenInput)page2.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
//		String attribute5 = firstByXPath5.getAttribute("value");
//		String encode5 = URLEncoder.encode(attribute5, "UTF-8");
//		
//		HtmlHiddenInput firstByXPath6 = (HtmlHiddenInput)page2.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
//		String attribute6 = firstByXPath6.getAttribute("value");
//		String encode6 = URLEncoder.encode(attribute6, "UTF-8");
//		
//		String b="ctl00%24MainContent%24ScriptManager1=ctl00%24MainContent%24UpdatePanel1%7CMainContent_tabConainer&__EVENTTARGET=MainContent_tabConainer&__EVENTARGUMENT=1&MainContent_tabConainer_ClientState=%7B%22ActiveTabIndex%22%3A1%2C%22TabState%22%3A%5Btrue%2Ctrue%2Ctrue%5D%7D&__VIEWSTATE="+encode+"&__VIEWSTATEGENERATOR="+encode6+"&__EVENTVALIDATION="+encode5+"&txtSKey=%E6%90%9C%E7%B4%A2%E5%85%B3%E9%94%AE%E5%AD%97&hidCurChoiceYear=&ctl00%24MainContent%24hidCurCompanyID=20150205401003171472015001201531&ctl00%24MainContent%24hidCurLoanID=&ctl00%24MainContent%24tabConainer%24tabChangeInfo%24txtNewMobile=&ctl00%24MainContent%24tabConainer%24tabChangeInfo%24txtConfirmMobile=&ctl00%24MainContent%24tabConainer%24tabChangeInfo%24txtNewPassword=&ctl00%24MainContent%24tabConainer%24tabChangeInfo%24txtConfirmPassword=&__ASYNCPOST=true&";
//		webReque.setRequestBody(b);
//		Page page4 = webClient.getPage(webReque);
		
		
		
		
		String url2="https://gjjcx.zjportal.net/Modules/GJJQuery/GJJDetail1.aspx?act=person&com="+substring+"&t="+System.currentTimeMillis();
		String url4="https://gjjcx.zjportal.net/Modules/GJJQuery/GJJDetail1.aspx?year=2017&iscom=true&com="+substring;
//		WebRequest webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
//		Page page3 = webClient.getPage(webRequest);
//		
//		WebRequest webRequest6 = new WebRequest(new URL(url2), HttpMethod.POST);
//		Page page6 = webClient.getPage(webRequest6);
//		Thread.sleep(10000);
//		System.out.println(page6.getWebResponse().getContentAsString());
		WebRequest webRequest5 = new WebRequest(new URL(url4), HttpMethod.POST);
		Page page5 = webClient.getPage(webRequest5);
		Thread.sleep(10000);
		WebRequest webRequest7 = new WebRequest(new URL(url2), HttpMethod.POST);
		Page page7 = webClient.getPage(webRequest7);
		Thread.sleep(10000);
		System.out.println(page5.getWebResponse().getContentAsString()+page7.getWebResponse().getContentAsString());
	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
}
