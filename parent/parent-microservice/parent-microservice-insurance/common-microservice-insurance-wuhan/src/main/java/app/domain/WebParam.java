package app.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.wuhan.InsuranceWuhanAllInfo;
import com.microservice.dao.entity.crawler.insurance.wuhan.InsuranceWuhanPersonalInfo;
import com.microservice.dao.entity.crawler.insurance.wuhan.InsuranceWuhanUserInfo;


public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	public List<T> list;
	public List<HtmlPage> htmlPage;
	public InsuranceWuhanAllInfo wuhanAllInfo;
	public InsuranceWuhanPersonalInfo wuhanPersonalInfo;
	public InsuranceWuhanUserInfo wuhanUserInfo;
	public String url;
	public String html;
	public String cookie;
	
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
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
	public InsuranceWuhanAllInfo getWuhanAllInfo() {
		return wuhanAllInfo;
	}
	public void setWuhanAllInfo(InsuranceWuhanAllInfo wuhanAllInfo) {
		this.wuhanAllInfo = wuhanAllInfo;
	}
	public InsuranceWuhanPersonalInfo getWuhanPersonalInfo() {
		return wuhanPersonalInfo;
	}
	public void setWuhanPersonalInfo(InsuranceWuhanPersonalInfo wuhanPersonalInfo) {
		this.wuhanPersonalInfo = wuhanPersonalInfo;
	}
	public InsuranceWuhanUserInfo getWuhanUserInfo() {
		return wuhanUserInfo;
	}
	public void setWuhanUserInfo(InsuranceWuhanUserInfo wuhanUserInfo) {
		this.wuhanUserInfo = wuhanUserInfo;
	}
	public HtmlPage getPage() {
		return page;
	}
	public void setPage(HtmlPage page) {
		this.page = page;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public List<HtmlPage> getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(List<HtmlPage> htmlPage) {
		this.htmlPage = htmlPage;
	}
	
	
}
