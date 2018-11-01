package app.unit;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chuxiong.HousingChuXiongPaydetails;
import com.microservice.dao.entity.crawler.housing.chuxiong.HousingChuXiongUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingChuXiongHtmlParser;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;

@Component
public class HousingFundChuXiongHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundChuXiongHtmlunit.class);
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingChuXiongHtmlParser  housingChuXiongHtmlParser;
	@Autowired
	private TracerLog tracer;

	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		String url = "http://www.cxzfgjj.com/gjj/gjj_cx.aspx";
		HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
		tracer.addTag("parser.crawler.login.loginPage",
				"<xmp>" + page.getWebResponse().getContentAsString() + "</xmp>");
		if (200 == page.getWebResponse().getStatusCode()) {
				HtmlImage image = page
						.getFirstByXPath("//img[(@id='imgCode')]");
				String code = chaoJiYingOcrService.getVerifycode(image, "5000");
				tracer.addTag("verifyCode ==>", code);				
				HtmlTextInput idNum = (HtmlTextInput)page.getFirstByXPath("//input[@id='TextBox1']");
				HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='TextBox2']");	
				
				HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='TextBox3']");	
				HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='TextBox4']");		
				HtmlElement button = (HtmlElement)page.getFirstByXPath("//a[@onclick='gjjcx(1);']");				
				idNum.setText(messageLoginForHousing.getNum());				
				username.setText(messageLoginForHousing.getUsername());			
				password.setText(messageLoginForHousing.getPassword());
				validateCode.setText(code);				
				HtmlPage loggedPage = button.click();
				Thread.sleep(1500);
				String html=loggedPage.asXml();
				tracer.addTag("parser.crawler.login.html", html);
				webParam.setWebClient(webClient);
				webParam.setUrl(url);
				webParam.setHtml(html);
				webParam.setPage(loggedPage);	
				webParam.setHtmlPage(loggedPage);
				if (html.contains("查询结果")) {				
					webParam.setLogin(true);
					webParam.setText("登录成功");
					HousingChuXiongUserInfo	userInfo=housingChuXiongHtmlParser.htmlUserInfoParser(html, taskHousing);
					webParam.setUserInfo(userInfo);
					List<HousingChuXiongPaydetails> paydetails=housingChuXiongHtmlParser.htmlPaydetailsParser(html, taskHousing);
					webParam.setPaydetails(paydetails);
				}else if(html.contains("身份证不能为空")){
					webParam.setText("身份证不能为空");
					webParam.setLogin(false);
				}else if(html.contains("身份证错误")){
					webParam.setText("身份证错误");
					webParam.setLogin(false);
				}else if(html.contains("验证码不能为空")){
					webParam.setText("验证码不能为空");  
					webParam.setLogin(false);
				}else if(html.contains("验证码错误")){
					webParam.setText("验证码错误");  
					webParam.setLogin(false);
				}else if(html.contains("姓名不能为空")){
					webParam.setText("姓名不能为空");  
					webParam.setLogin(false);
				}else if(html.contains("请认真检查是否姓名错误")){
					webParam.setText("请认真检查是否姓名错误");  
					webParam.setLogin(false);
				}else if(html.contains("验证码错误")){
					webParam.setText("验证码错误");
					webParam.setLogin(false);
				}else{
					webParam.setText("登陆失败");
					webParam.setLogin(false);
				}					
		}
		return webParam;
	}
	public  WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		 for(Cookie cookie : cookies){
			 webclient.getCookieManager().addCookie(cookie);
		  }
		return webclient;
	}
}
