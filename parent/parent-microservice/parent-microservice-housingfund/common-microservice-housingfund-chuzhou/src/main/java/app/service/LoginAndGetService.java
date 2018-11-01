package app.service;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chuzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chuzhou")
public class LoginAndGetService extends HousingBasicService{
//	@Value("${datasource.driverPath}")
//	public String driverPath;
	public  HtmlPage loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
//		HtmlPage htmlPage = (HtmlPage) loginAndGetCommon.getHtml(url, webClient);
		
//		HtmlTextInput name1 = htmlPage.getFirstByXPath("//*[@id='TxtName']");
//		HtmlTextInput password1 = htmlPage.getFirstByXPath("//*[@id='TxtIdent']");
//		name1.setText(name);
//		password1.setText(password);
//		HtmlElement button = htmlPage.getFirstByXPath("//*[@id='BtnSearch']");
//		htmlPage = button.click();
		//System.out.println(htmlPage.asXml());
//		String html = htmlPage.getWebResponse().getContentAsString();
		
		String url1 = "http://gjjcx.chuzhou.gov.cn/default.aspx";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "gjjcx.chuzhou.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://gjjcx.chuzhou.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://gjjcx.chuzhou.gov.cn/default.aspx");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUKLTU0ODEzMzg2NA9kFgICAw9kFggCAQ8QZGQWAWZkAhAPZBYSAgEPPCsADQEADxYEHgtfIURhdGFCb3VuZGceC18hSXRlbUNvdW50ZmRkAgMPDxYCHgRUZXh0ZWRkAgUPDxYCHwJlZGQCBw8PFgIfAmVkZAIJDw8WAh8CZWRkAgsPDxYCHwJlZGQCDQ8PFgIfAmVkZAIPDw8WAh8CZWRkAhEPPCsADQEADxYEHwBnHwFmZGQCEg9kFhICAQ8PFgIfAmVkZAIDDw8WAh8CZWRkAgUPDxYCHwJlZGQCBw8PFgIfAmVkZAIJDw8WAh8CZWRkAgsPDxYCHwJlZGQCDQ8PFgIfAmVkZAIPDw8WAh8CZWRkAhEPPCsADQEADxYEHwBnHwFmZGQCFA8PFgIeB1Zpc2libGVoZGQYAwUKR3JpZFZpZXdESw88KwAKAQhmZAUJR3JpZFZpZXcxDzwrAAoBCGZkBQlHcmlkVmlldzIPPCsACgEIZmQG21jItO1XoBkALtGmpIwIqUw6DA%3D%3D&__EVENTVALIDATION=%2FwEWBwLL3fv2AQKd5I%2FlCgKNi6WLBgKSi6WLBgKkjISFCwKwms2QDQLFnfXuCuxnsuZsNwwXiy2U28y%2Bi3Qte0AV&DropDownList1=0&TxtName="+URLEncoder.encode(name, "gb2312")+"&TxtIdent="+password+"&BtnSearch=%B2%E9%D1%AF";
		webRequest.setRequestBody(requestBody);
		HtmlPage searchPage = webClient.getPage(webRequest);
//		System.out.println(searchPage.asXml());
		
		return searchPage;
	}

	public  HtmlPage loginByACCOUNT_NUM(WebClient webClient,String url, String countNumber, String num, String username) throws Exception {
		String url1 = "http://gjjcx.chuzhou.gov.cn/default.aspx";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "gjjcx.chuzhou.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://gjjcx.chuzhou.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://gjjcx.chuzhou.gov.cn/default.aspx");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUKLTU0ODEzMzg2NA9kFgICAw9kFhACAQ8QZGQWAQIBZAIDDw8WAh4EVGV4dAUP5Y2V5L2N5biQ5Y%2B377yaZGQCBg8PFgIfAAUP5Liq5Lq65biQ5Y%2B377yaZGQCCg8PFgIeB1Zpc2libGVnZGQCDA8PFgIfAWdkZAIQD2QWEgIBDzwrAA0BAA8WBB4LXyFEYXRhQm91bmRnHgtfIUl0ZW1Db3VudAICZBYCZg9kFgYCAQ9kFgpmDw8WAh8ABQcxMDAzMDc2ZGQCAQ8PFgIfAAUw5YyX5Lqs5aSW5LyB5Lq65Yqb6LWE5rqQ5pyN5Yqh5a6J5b695pyJ6ZmQ5YWs5Y%2B4ZGQCAg8PFgIfAAUFMzA1MDRkZAIDDw8WAh8ABQnmnY7ok4nok4lkZAIEDw8WAh8ABRAzMjA4MyoqKioqKioqODI4ZGQCAg9kFgpmDw8WAh8ABQcxMDAzMzgwZGQCAQ8PFgIfAAU55a6J5b695pm65piT5Lq65Yqb6LWE5rqQ6aG%2B6Zeu5pyJ6ZmQ5YWs5Y%2B45ruB5bee5YiG5YWs5Y%2B4ZGQCAg8PFgIfAAUFMDAwNDRkZAIDDw8WAh8ABQnmnY7ok4nok4lkZAIEDw8WAh8ABRAzMjA4MyoqKioqKioqODI4ZGQCAw8PFgIfAWhkZAIDDw8WAh8ABQcxMDAzMDc2ZGQCBQ8PFgIfAAUw5YyX5Lqs5aSW5LyB5Lq65Yqb6LWE5rqQ5pyN5Yqh5a6J5b695pyJ6ZmQ5YWs5Y%2B4ZGQCBw8PFgIfAAUFMzA1MDRkZAIJDw8WAh8ABQnmnY7ok4nok4lkZAILDw8WAh8ABRAzMjA4MyoqKioqKioqODI4ZGQCDQ8PFgIfAAUBMGRkAg8PDxYCHwAFATBkZAIRDzwrAA0BAA8WBB8CZx8DAgJkFgJmD2QWBgIBD2QWCmYPDxYCHwAFITMwNTA05p2O6JOJ6JOJ6L2s5YWlMTAwMzM4MC0wMDA0NGRkAgEPDxYCHwAFCjIwMTctMTItMjdkZAICDw8WAh8ABQQyMDEzZGQCAw8PFgIfAAUBMGRkAgQPDxYCHwAFATBkZAICD2QWCmYPDxYCHwAFBue7k%2BaBr2RkAgEPDxYCHwAFCjIwMTctMDYtMzBkZAICDw8WAh8ABQEwZGQCAw8PFgIfAAUCMjdkZAIEDw8WAh8ABQQyMDEzZGQCAw8PFgIfAWhkZAISDw8WAh8BaGQWEgIBDw8WAh8AZWRkAgMPDxYCHwBlZGQCBQ8PFgIfAGVkZAIHDw8WAh8AZWRkAgkPDxYCHwBlZGQCCw8PFgIfAGVkZAINDw8WAh8AZWRkAg8PDxYCHwBlZGQCEQ88KwANAQAPFgQfAmcfA2ZkZAIUDw8WAh8BZ2RkGAMFCkdyaWRWaWV3REsPPCsACgEIZmQFCUdyaWRWaWV3MQ88KwAKAQgCAWQFCUdyaWRWaWV3Mg88KwAKAQgCAWRd6M5ZRHGn3BwsiRFL54ttSqQwVw%3D%3D&__EVENTVALIDATION=%2FwEWCgKf0a%2FkDwKd5I%2FlCgKNi6WLBgKSi6WLBgKkjISFCwKwms2QDQKyhrT%2BAwLFnfXuCgLwjZmQDQLwje23CmHNilewxyVSGAgVjlXqlR3DK7n6&DropDownList1=1&TxtName="+countNumber+"&TxtIdent="+num+"&Txt3="+URLEncoder.encode(username, "gb2312")+"&BtnSearch=%B2%E9%D1%AF";
		webRequest.setRequestBody(requestBody);
		HtmlPage searchPage = webClient.getPage(webRequest);
		String html=searchPage.getWebResponse().getContentAsString();
		return searchPage;
	}
//	public  ChromeDriver intiChrome() throws Exception {
////		String driverPath = "/opt/selenium/chromedriver-2.31";
//		System.setProperty("webdriver.chrome.driver", driverPath);
//		// WebDriver driver = new ChromeDriver();
//		ChromeOptions chromeOptions = new ChromeOptions();
//		// 设置为 headless 模式 （必须）
//		// chromeOptions.addArguments("--headless");
//		// 设置浏览器窗口打开大小 （非必须）
//		// chromeOptions.addArguments("--window-size=1980,1068");
//		ChromeDriver driver = new ChromeDriver(chromeOptions);
//		return driver;
//	}
}
