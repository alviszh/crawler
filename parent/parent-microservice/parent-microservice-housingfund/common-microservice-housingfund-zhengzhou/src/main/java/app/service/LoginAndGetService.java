package app.service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhengzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhengzhou")
public class LoginAndGetService extends HousingBasicService{
	// 根据身份证号登录
		public HtmlPage loginByIDCard(WebClient webClient,String name,String idcode, String password)
				throws Exception {
			String encode = URLEncoder.encode(name, "UTF-8");
			String url = "http://wx.zzgjj.com/pcweb/pcchaxun/chaxun?name="+encode+"&sfzh="+idcode+"&mm="+password+"";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			//Thread.sleep(1000*60);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			webClient.getOptions().setJavaScriptEnabled(false);
			
			HtmlPage searchPage = webClient.getPage(webRequest);
//			HtmlTextInput username1 = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='jcname']"); 
//			HtmlTextInput idcode1 = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='jcsfzh']");
//			HtmlPasswordInput passwords = (HtmlPasswordInput)htmlPage.getFirstByXPath("//input[@id='mm']");
//			HtmlElement button = (HtmlElement)htmlPage.getFirstByXPath("//input[@onclick='cxtest()']");
//			
//			username1.setText(name);
//			idcode1.setText(idcode);
//			passwords.setText(password);
//			
//			HtmlPage loggedPage = button.click();
			
			System.out.println(searchPage.asXml());
			return searchPage;	

		}
		
}
