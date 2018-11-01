package app.crawler.domain;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouLostwork;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouMedical;
import com.microservice.dao.entity.crawler.insurance.huizhou.InsuranceHuiZhouUserInfo;

public class WebParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public List<InsuranceHuiZhouMedical> medicalList;
	public List<InsuranceHuiZhouLostwork> lostworkList;
	public InsuranceHuiZhouUserInfo userInfo;
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
    
	public List<InsuranceHuiZhouMedical> getMedicalList() {
		return medicalList;
	}
	public void setMedicalList(List<InsuranceHuiZhouMedical> medicalList) {
		this.medicalList = medicalList;
	}
	public List<InsuranceHuiZhouLostwork> getLostworkList() {
		return lostworkList;
	}
	public void setLostworkList(List<InsuranceHuiZhouLostwork> lostworkList) {
		this.lostworkList = lostworkList;
	}
	public InsuranceHuiZhouUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(InsuranceHuiZhouUserInfo userInfo) {
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
