package test.webdriver.phantomjs;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class HTest {

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		HtmlPage page = webClient.getPage("http://login.189.cn/login");
		
		ScriptResult scriptResult = page.executeJavaScript("aesDecrypt('795372');");
		
		String sss = scriptResult.getJavaScriptResult().toString();
		
		System.out.println("sss---------------"+sss);

	}

}
