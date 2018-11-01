package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouPaydetails;
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouUserInfo;

public class WebParam{
	
	public Page page;
	public String url;
	public String html;
	public WebClient webClient;	
	public boolean isLogin;
	public String loginMsg;
	public HousingSuiZhouUserInfo userInfo;
	public List<HousingSuiZhouPaydetails> paydetails;
	private List<String> datalist;
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
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

	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public HousingSuiZhouUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(HousingSuiZhouUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<HousingSuiZhouPaydetails> getPaydetails() {
		return paydetails;
	}
	public void setPaydetails(List<HousingSuiZhouPaydetails> paydetails) {
		this.paydetails = paydetails;
	}
	public List<String> getDatalist() {
		return datalist;
	}
	public void setDatalist(List<String> datalist) {
		this.datalist = datalist;
	}
	public String getLoginMsg() {
		return loginMsg;
	}
	public void setLoginMsg(String loginMsg) {
		this.loginMsg = loginMsg;
	}
	
}
