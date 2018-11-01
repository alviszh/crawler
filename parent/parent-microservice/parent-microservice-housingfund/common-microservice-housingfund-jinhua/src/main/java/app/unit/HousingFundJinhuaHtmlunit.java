package app.unit;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jinhua.HousingJinHuaPaydetails;
import com.microservice.dao.entity.crawler.housing.jinhua.HousingJinHuaUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundJinhuaHtmlParser;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundJinhuaHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundJinhuaHtmlunit.class);
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingFundJinhuaHtmlParser  housingFundJinhuaHtmlParser;
	@Autowired
	private TracerLog tracer;

	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			int count) throws Exception {
		tracer.addTag("parser.crawler.login", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		String loginUrl= "https://puser.zjzwfw.gov.cn/sso/usp.do?action=ssoLogin&servicecode=jhsgjjcx";
		HtmlPage loginPage = getHtml(loginUrl,webClient);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
		//用户名
		HtmlTextInput username = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='loginname']");
		//密码
		HtmlPasswordInput password = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@id='loginpwd']");	
		
		username.setText(messageLoginForHousing.getNum());
		password.setText(messageLoginForHousing.getPassword());
		//验证码
		HtmlTextInput imageCode = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='verifycode']");	
		if (imageCode.asXml().contains("validate")) {
			HtmlImage image = loginPage.getFirstByXPath("//input[@id='captcha_img']");
			String code = chaoJiYingOcrService.getVerifycode(image, "5000");
			tracer.addTag("verifyCode ==>", code);
			imageCode.setText(code);
		}
		//登陆按钮
		HtmlElement button = (HtmlElement)loginPage.getFirstByXPath("//input[@id='submit']");
		HtmlPage loginHtmlPage = button.click();
		Thread.sleep(1500);
		String loginHtml=loginHtmlPage.asXml();
		tracer.addTag("parser.crawler.loginHtmlPage",loginHtml);
		webParam.setUrl(loginUrl);
		webParam.setPage(loginHtmlPage);
		webParam.setHtml(loginHtml);
		if (loginHtml.contains("公积金账户信息")) {
			webParam.setLogin(true);
			webParam.setText("登陆成功");
			webParam.setWebClient(webClient);
			List<HousingJinHuaUserInfo>  userInfoList=housingFundJinhuaHtmlParser.htmlUserInfoParser(loginHtml, taskHousing);
			webParam.setUserInfos(userInfoList);
			List<String>  urlList=new ArrayList<>();
			Document doc = Jsoup.parse(loginHtml, "utf-8"); 
			Elements tbody=doc.select("tbody");
			int size=tbody.size();
			for (int i = 0; i < size; i++){				
				String url=tbody.get(i).select("a").attr("href");
				urlList.add(url);
				webParam.setUrlList(urlList);
			}
		}else {
			if (loginHtml.contains("用户名、密码是否正确")) {
				webParam.setLogin(false);			
				webParam.setText("用户名、密码是否正确");
			}else if(loginHtml.contains("验证码不正确")){
				webParam.setLogin(false);			
				webParam.setText("验证码不正确");
			}else if(loginHtml.contains("密码错误")){
				webParam.setLogin(false);
				webParam.setText("密码错误");
			}
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
				Thread.sleep(1500);
				login(messageLoginForHousing, taskHousing, count);
			}	
		}
	
		return webParam;
	}
	
	public WebParam getPaydetails(String payUrl,MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		tracer.addTag("parser.crawler.getPaydetails", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient,taskHousing);		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar c = Calendar.getInstance();  
		String tbxEnd=format.format(c.getTime());
		c.add(Calendar.YEAR, -3); //年份减3  
		String tbxStart=format.format(c.getTime());
		String[] str=payUrl.split("=");
		String userAccount=str[3];		
		String url = "http://wsbs.jhgjj.gov.cn/PubWeb/GR/GRZHMX_List.aspx";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("tbxJgh", "3300701"));
		paramsList.add(new NameValuePair("tbxGrzh", userAccount));
		paramsList.add(new NameValuePair("tbxZhlx", "01"));
		paramsList.add(new NameValuePair("tbxStart", tbxStart));
		paramsList.add(new NameValuePair("tbxEnd", tbxEnd));
		paramsList.add(new NameValuePair("pageIndex", "0"));
		paramsList.add(new NameValuePair("pageSize", "20"));
		paramsList.add(new NameValuePair("btnSubmit", "%E6%9F%A5+%E8%AF%A2"));
	 	webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "wsbs.jhgjj.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://wsbs.jhgjj.gov.cn");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", html);
		webParam.setHtml(html);
		webParam.setPage(page);	
		webParam.setUrl(url);
		webParam.setHtml(html);
		webParam.setPage(page);
		List<HousingJinHuaPaydetails> paydetails=new ArrayList<HousingJinHuaPaydetails>();
	   if (html.contains("grid")) {
		    paydetails=housingFundJinhuaHtmlParser.htmlPaydetailsParser(html,userAccount, taskHousing);
			webParam.setPaydetails(paydetails);	
		}	
		return webParam;
	}
	
	public WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		 for(Cookie cookie : cookies){
			 webclient.getCookieManager().addCookie(cookie);
		  }
		return webclient;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
