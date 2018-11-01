package app.crawler.domain;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXiUserInfo;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXibasictypes;
import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXipaydetails;

public class WebParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public List<InsuranceSZGuangXibasictypes> basictypeList;
	public List<InsuranceSZGuangXipaydetails> paydetailsList;
	public InsuranceSZGuangXiUserInfo userInfo;
	public String cookies;
	public WebDriver driver;
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
	public List<InsuranceSZGuangXibasictypes> getBasictypeList() {
		return basictypeList;
	}
	public void setBasictypeList(List<InsuranceSZGuangXibasictypes> basictypeList) {
		this.basictypeList = basictypeList;
	}
	public List<InsuranceSZGuangXipaydetails> getPaydetailsList() {
		return paydetailsList;
	}
	public void setPaydetailsList(List<InsuranceSZGuangXipaydetails> paydetailsList) {
		this.paydetailsList = paydetailsList;
	}
	public InsuranceSZGuangXiUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(InsuranceSZGuangXiUserInfo userInfo) {
		this.userInfo = userInfo;
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
	
}
