package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.heihe.HousingHeihePaydetails;
import com.microservice.dao.entity.crawler.housing.heihe.HousingHeiheUserInfo;

public class WebParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public WebClient webClient;	
	public int pageSize;
	public boolean isLogin;
	public HousingHeiheUserInfo userInfo;
	public List<HousingHeihePaydetails> paydetails;
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

	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public HousingHeiheUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(HousingHeiheUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<HousingHeihePaydetails> getPaydetails() {
		return paydetails;
	}
	public void setPaydetails(List<HousingHeihePaydetails> paydetails) {
		this.paydetails = paydetails;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
