package app.domain;

import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public String cookies;
	public WebDriver driver;
	public String sessionid;
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

	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public WebDriver getDriver() {
		return driver;
	}
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

}
