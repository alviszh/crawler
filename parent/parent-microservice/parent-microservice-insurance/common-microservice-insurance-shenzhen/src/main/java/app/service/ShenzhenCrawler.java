package app.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

/**
 * 深圳社保爬取
 * @author rongshengxu
 *
 */
@Component
public class ShenzhenCrawler {
	
	public static final Logger log = LoggerFactory.getLogger(ShenzhenCrawler.class);

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	/** 深圳社保登录URL */
	public static final String LOGIN_URL = "https://e.szsi.gov.cn/siservice/login.jsp";
	/** 深圳社保主页URL */
	public static final String HOME_URL = "https://e.szsi.gov.cn/siservice/pIndex.jsp";
	
	/**
	 * 登录 爬取
	 * @param parameter
	 * @throws Exception 
	 */
	public WebParam<HtmlPage> loginCrawler(InsuranceRequestParameters parameter){
		WebParam<HtmlPage> webParam = new WebParam<HtmlPage>();
		HtmlPage loginedPage = null;
		try {
			loginedPage = crawlerLogin(parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		webParam.setData(loginedPage);
		if(loginedPage != null){
			webParam.setPid(getPid(loginedPage));
		}
		//webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
		return webParam;
	}
	
	/**
	 * 获取登录后的pid参数
	 * @param loginedPage
	 * @return
	 */
	private String getPid(HtmlPage loginedPage){
		HtmlHiddenInput hid = (HtmlHiddenInput)loginedPage.getFirstByXPath("//input[@name='pid']");
		return hid.getAttribute("value");
	}
	
	/**
	 * 深圳社保登录
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	private HtmlPage crawlerLogin(InsuranceRequestParameters parameter) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest webRequest  = new WebRequest(new URL(LOGIN_URL), HttpMethod.GET);
		HtmlPage loginPage  = webClient.getPage(webRequest);
		HtmlTextInput username = (HtmlTextInput)loginPage.getFirstByXPath("//input[@name='AAC002']"); 
		HtmlPasswordInput password = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@type='password']");
		HtmlTextInput validateCode = (HtmlTextInput)loginPage.getFirstByXPath("//input[@name='PSINPUT']");
		HtmlElement submitButton = (HtmlElement)loginPage.getFirstByXPath("//input[@class='login-button']");
		
		username.setText(parameter.getUsername());
		password.setText(parameter.getPassword());
		
		List<HtmlImage> listOfImage = loginPage.getByXPath("//img");
		HtmlImage image = listOfImage.get(1);
		String code = chaoJiYingOcrService.getVerifycode(image, "4004");
		validateCode.setText(code);
		
		HtmlPage loginedPage = submitButton.click();
		return loginedPage;
	}
	
	/**
	 * 进入主页
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 * 
	 */
	public HtmlPage inHomePage(InsuranceRequestParameters parameter,Set<Cookie> cookies,String pid){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for(Cookie cookie : cookies){
			webClient.getCookieManager().addCookie(cookie);
		}
		HtmlPage inPage = null;
		try {
			WebRequest webRequest = new WebRequest(new URL(HOME_URL), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "e.szsi.gov.cn");
			webRequest.setAdditionalHeader("Origin", "https://e.szsi.gov.cn");
			webRequest.setAdditionalHeader("Referer", "https://e.szsi.gov.cn/siservice/LoginAction.do");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new NameValuePair("pid",pid));
			webRequest.setRequestParameters(params);
			
			inPage = webClient.getPage(webRequest);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inPage;
	}
	
	/**
	 * 爬取深圳社保-基本信息
	 * @param parameter
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws IOException
	 */
	public WebParam<HtmlPage> crawlerBaseInfo(InsuranceRequestParameters parameter,HtmlPage inHomePage){
		WebParam<HtmlPage> webParam = new WebParam<HtmlPage>();
		HtmlPage baseInfoPage = null;
		try {
			HtmlElement baseInfoElement = (HtmlElement)inHomePage.getByXPath("//a").get(13);
			baseInfoPage = baseInfoElement.click();
			
			webParam.setData(baseInfoPage);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			webParam.setData(baseInfoPage);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch (IOException e) {
			e.printStackTrace();
			webParam.setData(baseInfoPage);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch(Exception e){
			if("java.lang.ClassCastException".equals(e.getClass().getName()) && 
					"com.gargoylesoftware.htmlunit.TextPage cannot be cast to com.gargoylesoftware.htmlunit.html.HtmlPage".equals(e.getMessage())){
				webParam.setData(baseInfoPage);
				webParam.setCode(InsuranceShenzhenCrawlerResult.TIMEOUT.getCode());
			}else{
				webParam.setData(baseInfoPage);
				webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
			}
			e.printStackTrace();
		}
		return webParam;
	}
	
	/**
	 * 爬取深圳社保-养老保险
	 * @param parameter
	 * @return
	 */
	public WebParam<HtmlPage> crawlerAgedInsurance(InsuranceRequestParameters parameter,HtmlPage inHomePage){
		WebParam<HtmlPage> webParam = new WebParam<HtmlPage>();
		HtmlPage page = null;
		try {
			HtmlElement element = (HtmlElement)inHomePage.getFirstByXPath("//ul[@id='menu110000_110003']/li[1]/a");
			page = element.click();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch (IOException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch(Exception e){
			if("java.lang.ClassCastException".equals(e.getClass().getName()) && 
					"com.gargoylesoftware.htmlunit.TextPage cannot be cast to com.gargoylesoftware.htmlunit.html.HtmlPage".equals(e.getMessage())){
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.TIMEOUT.getCode());
			}else{
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
			}
			e.printStackTrace();
		}
		return webParam; 
	}
	
	/**
	 * 爬取深圳社保-医疗保险
	 * @param parameter
	 * @return
	 */
	public WebParam<HtmlPage> crawlerMedicalInsurance(InsuranceRequestParameters parameter,HtmlPage inHomePage){
		WebParam<HtmlPage> webParam = new WebParam<HtmlPage>();
		HtmlPage page = null;
		try {
			HtmlElement element = (HtmlElement)inHomePage.getFirstByXPath("//ul[@id='menu110000_110004']/li[1]/a");
			page = element.click();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch (IOException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch(Exception e){
			if("java.lang.ClassCastException".equals(e.getClass().getName()) && 
					"com.gargoylesoftware.htmlunit.TextPage cannot be cast to com.gargoylesoftware.htmlunit.html.HtmlPage".equals(e.getMessage())){
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.TIMEOUT.getCode());
			}else{
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
			}
			e.printStackTrace();
		}
		return webParam; 
	}
	
	/**
	 * 爬取深圳社保-工伤保险
	 * @param parameter
	 * @return
	 */
	public WebParam<HtmlPage> crawlerInjuryInsurance(InsuranceRequestParameters parameter,HtmlPage inHomePage){
		WebParam<HtmlPage> webParam = new WebParam<HtmlPage>();
		HtmlPage page = null;
		try {
			HtmlElement element = (HtmlElement)inHomePage.getFirstByXPath("//ul[@id='menu110000_110005']/li[1]/a");
			page = element.click();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch (IOException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch(Exception e){
			if("java.lang.ClassCastException".equals(e.getClass().getName()) && 
					"com.gargoylesoftware.htmlunit.TextPage cannot be cast to com.gargoylesoftware.htmlunit.html.HtmlPage".equals(e.getMessage())){
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.TIMEOUT.getCode());
			}else{
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
			}
			e.printStackTrace();
		}
		return webParam; 
	}
	
	/**
	 * 爬取深圳社保-失业保险
	 * @param parameter
	 * @return
	 */
	public WebParam<HtmlPage> crawlerUnemploymentInsurance(InsuranceRequestParameters parameter,HtmlPage inHomePage){
		WebParam<HtmlPage> webParam = new WebParam<HtmlPage>();
		HtmlPage page = null;
		try {
			HtmlElement element = (HtmlElement)inHomePage.getFirstByXPath("//ul[@id='menu110000_110006']/li[1]/a");
			page = element.click();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch (IOException e) {
			e.printStackTrace();
			webParam.setData(page);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		} catch(Exception e){
			if("java.lang.ClassCastException".equals(e.getClass().getName()) && 
					"com.gargoylesoftware.htmlunit.TextPage cannot be cast to com.gargoylesoftware.htmlunit.html.HtmlPage".equals(e.getMessage())){
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.TIMEOUT.getCode());
			}else{
				webParam.setData(page);
				webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
			}
			e.printStackTrace();
		}
		return webParam; 
	}
	
}
