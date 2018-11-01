package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.suqian.HousingSuQianPaydetails;
import com.microservice.dao.entity.crawler.housing.suqian.HousingSuQianUserInfo;

public class WebParam {
	
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public WebClient webClient;	
	public boolean isLogin;
	public String grzh;
	public HousingSuQianUserInfo userInfo;
	public List<HousingSuQianPaydetails> paydetails;
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

	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public HousingSuQianUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(HousingSuQianUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<HousingSuQianPaydetails> getPaydetails() {
		return paydetails;
	}
	public void setPaydetails(List<HousingSuQianPaydetails> paydetails) {
		this.paydetails = paydetails;
	}
	public String getGrzh() {
		return grzh;
	}
	public void setGrzh(String grzh) {
		this.grzh = grzh;
	}
}
