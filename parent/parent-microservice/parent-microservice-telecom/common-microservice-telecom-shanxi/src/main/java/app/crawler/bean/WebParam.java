package app.crawler.bean;


import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1UserInfo;

public class WebParam<T> {
	
	public Page page;
	public Integer code;
	
	public String url;
	public String html;
	
	public List<T> list;
	
	private String cookies;

	private WebClient webclient;
	
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public WebClient getWebclient() {
		return webclient;
	}
	public void setWebclient(WebClient webclient) {
		this.webclient = webclient;
	}
	public TelecomShanxi1UserInfo telecomShanxi1UserInfo;
	
	public TelecomShanxi1UserInfo getTelecomShanxi1UserInfo() {
		return telecomShanxi1UserInfo;
	}
	public void setTelecomShanxi1UserInfo(TelecomShanxi1UserInfo telecomShanxi1UserInfo) {
		this.telecomShanxi1UserInfo = telecomShanxi1UserInfo;
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
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
}
