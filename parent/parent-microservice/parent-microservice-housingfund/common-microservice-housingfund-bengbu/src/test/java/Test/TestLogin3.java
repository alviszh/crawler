package Test;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuUserInfo;
import com.module.htmlunit.WebCrawler;

public class TestLogin3 {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		String url="https://gr.szgjj.gov.cn/retail/index.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
//		String[] split = page.getWebResponse().getContentAsString().split("sid");
		HtmlInput firstByXPath2 = (HtmlInput)page.getFirstByXPath("/html/body/form/input[1]");
		System.out.println(firstByXPath2.getValueAttribute());
		HtmlElement  firstByXPath3 = (HtmlElement)page.getFirstByXPath("/html/body/div/div[3]/div[1]/div/table/tbody/tr/td[2]/input");
		HtmlPage page2 = firstByXPath3.click();
		
		
//		System.out.println(page2.getWebResponse().getContentAsString());
//		HtmlSelect  first = (HtmlSelect)page.getFirstByXPath("//*[@id='login']");
//		HtmlSelect first = (HtmlSelect) page2.getElementById("login");
//		HtmlOption optionByText = (HtmlOption)first.getOptionByText("身份证号登陆");
//		optionByText.click();
		
		HtmlTextInput firstByXPath8 = (HtmlTextInput)page2.getFirstByXPath("//*[@id='custacno']");
		firstByXPath8.setText("0097953388");
		HtmlTextInput firstByXPath7 = (HtmlTextInput)page2.getFirstByXPath("//*[@id='paperid']");
		firstByXPath7.setText("230524198402142026");
		
		
		HtmlImage img = (HtmlImage)page2.getFirstByXPath("//*[@id='validatePicture0']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput yzm = (HtmlTextInput)page2.getFirstByXPath("//input[@id='validateCode0']");
		yzm.reset();
		yzm.setText(inputValue);
		HtmlElement firstByXPath = (HtmlElement)page2.getFirstByXPath("/html/body/div/div[3]/div[1]/div[1]/form/table/tbody/tr[1]/td[3]/input");
		HtmlPage click = firstByXPath.click();
//		System.out.println(click.getWebResponse().getContentAsString());
		WebClient webClient2 = click.getWebClient();
		
		String cookieString = CommonUnit.transcookieToJson(webClient2);
		
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString); 
		for(Cookie cookie : cookies){ 
		  System.out.println(cookie);
		}
		
		String url1="https://gr.szgjj.gov.cn/retail/internet?sid="+firstByXPath2.getValueAttribute()+"&service=com.jbsoft.i2hf.retail.services.UserAccService.getBaseAccountInfo&ts="+System.currentTimeMillis();
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());
	}
}
