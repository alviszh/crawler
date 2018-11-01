package test;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

public class AppTest {
	@Autowired
	private static ChaoJiYingOcrService chaoJiYingOcrService;

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			WebRequest webRequest = new WebRequest(new URL("http://rsjapp.my.gov.cn/mycx/login.jsp"), HttpMethod.GET);
			HtmlPage loginPage = webClient.getPage(webRequest);
			HtmlTextInput username = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='j_username']");
			HtmlPasswordInput password = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='j_password']");
			HtmlTextInput validateCode = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='checkCode']");
			HtmlElement submitButton = (HtmlElement) loginPage.getFirstByXPath("//button[@id='loginBtn']");
			username.setText("510704198806124927");
			password.setText("123456");

			HtmlImage image = (HtmlImage) loginPage.getFirstByXPath("//img[@id='codeimg']");
			String code = chaoJiYingOcrService.getVerifycode(image, "4004");
			validateCode.setText(code);
			HtmlPage loginedPage = submitButton.click();
			
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
