package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.yangzhou.InsuranceYangZhouMedical;
import com.microservice.dao.entity.crawler.insurance.yangzhou.InsuranceYangZhouUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public InsuranceYangZhouUserInfo  userInfo;	
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public String alertMsg;
	public List<InsuranceYangZhouMedical>  medicalList;
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
	
	public InsuranceYangZhouUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(InsuranceYangZhouUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<InsuranceYangZhouMedical> getMedicalList() {
		return medicalList;
	}
	public void setMedicalList(List<InsuranceYangZhouMedical> medicalList) {
		this.medicalList = medicalList;
	}
	public String getAlertMsg() {
		return alertMsg;
	}
	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}
}
