package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingAccountInfo;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingPaydetails;
import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingUserInfo;

public class WebParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public WebClient webClient;	
	public HousingChongqingAccountInfo accountInfo;
	public HousingChongqingUserInfo userInfo;
	public List<HousingChongqingPaydetails> paydetails;
	public boolean isLogin;
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public HousingChongqingAccountInfo getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(HousingChongqingAccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	public HousingChongqingUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(HousingChongqingUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<HousingChongqingPaydetails> getPaydetails() {
		return paydetails;
	}
	public void setPaydetails(List<HousingChongqingPaydetails> paydetails) {
		this.paydetails = paydetails;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
}
