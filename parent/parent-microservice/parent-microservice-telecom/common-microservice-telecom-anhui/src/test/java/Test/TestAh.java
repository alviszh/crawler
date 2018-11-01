package Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.module.htmlunit.WebCrawler;

public class TestAh {
	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		// 正常页面登陆
		String url = "http://login.189.cn/web/login";
		String url1="http://www.189.cn/dqmh/userCenter/userInfo.do?method= editUserInfo_new&fastcode=&cityCode=ah";
		String url2="http://ah.189.cn/service/bill/fee.parser?type=bill";
		String url3="http://ah.189.cn/service/manage/showCustInfo.parser";
		String url4="http://ah.189.cn/service/manage/getHandelProdList.parser";
		String url5="http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Fbill%2Ffee.parser%3Ftype";
		String url6="http://ah.189.cn/sso/login?returnUrl=%2Fservice";
		HtmlPage html = getHtml(url6, webClient);
//		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");// logon_name
//		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");// logon_passwd
//		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
//		username.setText("17718194181");
//		passwordInput.setText("119110");
//		HtmlPage htmlpage2 = button.click();
//		WebClient client = htmlpage2.getWebClient();
//		System.out.println("---------------+++++++++++++++"+html.getWebResponse().getContentAsString()+"==========+++++++++++++");
		
		HtmlDivision firstByXPath = html.getFirstByXPath("//div[@class='login-con']");
		
		HtmlTextInput username1 =(HtmlTextInput) firstByXPath.getFirstByXPath("//input[@class='login-text']");
		username1.reset();
		username1.setText("17718194181");
		
		HtmlImage image = firstByXPath.getFirstByXPath("//img[@id='vImg']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		image.saveAs(file);
		HtmlTextInput inputuserjym = firstByXPath.getFirstByXPath("//input[@id='validCode']");
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		inputuserjym.setText(inputValue);	
		String codeurl="http://ah.189.cn/sso/ValidateRandom?validCode="+inputValue;
		Page page = webClient.getPage(codeurl);
		System.out.println(page.getWebResponse().getContentAsString());
		
		String mobilNbr = "17718194181";
		String _key = mobilNbr.substring(1,2)+mobilNbr.substring(3,4)+mobilNbr.substring(6,7)+mobilNbr.substring(8,10);
		String params = "validCode="+inputValue+"&phone=" + mobilNbr+"&key="+_key;
		//HtmlAnchor abutton = html.getFirstByXPath("//a[@id='send_code_a']");
		
		TestJs  test = new TestJs();
		String str = test.encryptedPhone(params);
		
		String urlurl="http://ah.189.cn/sso/SendSms?_v="+str;
		
        WebRequest requestSettings = new WebRequest(new URL(urlurl), HttpMethod.POST);
		
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("Origin", "http://ah.189.cn");
		requestSettings.setAdditionalHeader("Host", "ah.189.cn");
		requestSettings.setAdditionalHeader("Referer", "http://ah.189.cn/sso/login?returnUrl=%2Fservice%3Ftype");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page pagedl = webClient.getPage(requestSettings);
		System.out.println(pagedl);
		
		HtmlTextInput firstByXPath2 = firstByXPath.getFirstByXPath("//input[@class='login-text-yzm']");
		firstByXPath2.reset();
		//firstByXPath2.setText(text);
		
		HtmlAnchor abutton = html.getFirstByXPath("//a[@class='loginBtn']");
		HtmlPage page2 = abutton.click();
		System.out.println(page2.getWebResponse().getContentAsString());
		
		//Page page3 = webClient.getPage(urlurl);
		//System.out.println(page3.getWebResponse().getContentAsString());
		
		
		// if(html.asXml().contains("imgCaptcha"))
		// {
		// HtmlImage imgCaptcha =
		// (HtmlImage)html.getFirstByXPath("//img[@id='imgCaptcha']");//verify_key
		// HtmlTextInput txtCaptcha =
		// (HtmlTextInput)html.getFirstByXPath("//input[@id='txtCaptcha']");//logon_valid
		// String code = chaoJiYingOcrService.getVerifycode(imgCaptcha,"1004");
		// txtCaptcha.setText(code);
		// }
	
		//HtmlForm firstByXPath = page.getFirstByXPath("//form[@id='personalInfo']");
		//Document doc = Jsoup.parse(firstByXPath.asXml());
		//Element elements = doc.getElementsByTag("div").get(2).getElementsByTag("span").get(1);
		//System.out.println(elements.text()+"--");
		//System.out.println("==============++++++++++++++++++"+page.getWebResponse().getContentAsString());
	}
	public String encryptedPhone(String phonenum) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("telecom.js", Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",phonenum);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
