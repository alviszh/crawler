package Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		String url="http://218.90.206.76:8080/jeesite/a/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		id_card.reset();
		id_card.setText("321202199009030928");
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		searchpwd.reset();
		searchpwd.setText("030928");
		
		
        HtmlImage img = page.getFirstByXPath("//*[@id='loginForm']/div[1]/img");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@name='validateCode']");
		identifying.reset();
		identifying.setText(inputValue);
		
//		HtmlElement button = page.getFirstByXPath("//*[@id='loginForm']/input[3]");
//		HtmlPage page2 = button.click();
		
		String url1="http://218.90.206.76:8080/jeesite/servlet/validateCodeServlet?validateCode="+inputValue;
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		Page page1 = webClient.getPage(webRequest);
		String url2="http://218.90.206.76:8080/jeesite/a/login?username=321202199009030928&password=030928&validateCode="+inputValue;
		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
		Page page2 = webClient.getPage(webRequest2);
		String url3="http://218.90.206.76:8080/jeesite/a";
		WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest3);
		String url4="http://218.90.206.76:8080/jeesite/a/sys/user/index";
		WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.GET);
		Page page4 = webClient.getPage(webRequest4);
		System.out.println(page4.getWebResponse().getContentAsString());
	}
}
