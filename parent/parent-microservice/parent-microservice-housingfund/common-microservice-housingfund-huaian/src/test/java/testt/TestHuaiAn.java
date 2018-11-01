package testt;

import java.io.File;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

public class TestHuaiAn {

	public static void main(String[] args) throws Exception{
		login();
	}
	
	
	public static void login() throws Exception{
		ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
		
		String loginUrl = "http://www.hagjj.com/office/geren/login.asp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		
		HtmlImage safecode1 = (HtmlImage)loginPage.getFirstByXPath("//img[@id='safecode']");
		loginPage = (HtmlPage) safecode1.click();
		
		HtmlElement blank = loginPage.getFirstByXPath("//div[@id='passwordInfo']");
		HtmlTextInput cardid = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='cardid']");
		HtmlPasswordInput password = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@id='password']");
		HtmlTextInput verifycode = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='verifycode']");
		HtmlImage safecode = (HtmlImage)loginPage.getFirstByXPath("//img[@id='safecode']");
//		HtmlSubmitInput regsub = (HtmlSubmitInput)loginPage.getFirstByXPath("//input[@id='regsub']");
		
		File file = new File("E:\\Codeimg\\huaian.jpg");
		safecode.saveAs(file);
		
		String code = chaoJiYingOcrService.getVerifycode(safecode, "1004");
		System.out.println("验证码是==》"+code);
		
		String loginUrl2 = "http://www.hagjj.com/office/geren/slogin.asp";
		webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
		webRequest.setAdditionalHeader("Referer", "http://www.hagjj.com/office/geren/login.asp");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Host", "www.hagjj.com");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		
		webRequest.setRequestBody("pcardid=320821199004180792&userpwd=123456&verifycode="+code);
		HtmlPage loginedPage = webClient.getPage(webRequest);
		System.out.println("登陆后的页面--》"+loginedPage.asXml());
		
		String loginUrl3 = "http://www.hagjj.com/office/geren/grgjjcx.asp";
		webRequest = new WebRequest(new URL(loginUrl3), HttpMethod.GET);
		HtmlPage loginedPage3 = webClient.getPage(webRequest);
		System.out.println("数据页面1--》"+loginedPage3.asXml());
		
		Document document = Jsoup.parse(loginedPage3.asXml());
		String name = getNextLabelByKeyword(document, "td", "姓    名：");
		System.out.println("姓名是--》"+name);
		
		/*String loginUrl4 = "http://www.hagjj.com/office/geren/grgjjcx0.asp";
		webRequest = new WebRequest(new URL(loginUrl4), HttpMethod.GET);
		HtmlPage loginedPage4 = webClient.getPage(webRequest);
		System.out.println("数据页面2--》"+loginedPage4.asXml());*/
		
		/*cardid.setText("320821199004180792");
		password.setText("123456");
		verifycode.setText(code);
		HtmlPage click = blank.click();
		System.out.println("登陆后的页面1--》"+click.asXml());
		HtmlSubmitInput regsub = (HtmlSubmitInput)click.getFirstByXPath("//input[@id='regsub']");
		HtmlPage loginedPage = regsub.click();
		System.out.println("登陆后的页面--》"+loginedPage.asXml());*/
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
}
