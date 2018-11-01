package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingInfo;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingPaydetails;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public InsuranceAnQingUserInfo  insuranceAnQingUserInfo;	
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public List<InsuranceAnQingInfo>  insuranceAnQingInfos;
	public List<InsuranceAnQingPaydetails>  insuranceAnQingPaydetails;
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
	
	public InsuranceAnQingUserInfo getInsuranceAnQingUserInfo() {
		return insuranceAnQingUserInfo;
	}
	public void setInsuranceAnQingUserInfo(InsuranceAnQingUserInfo insuranceAnQingUserInfo) {
		this.insuranceAnQingUserInfo = insuranceAnQingUserInfo;
	}
	public List<InsuranceAnQingInfo> getInsuranceAnQingInfos() {
		return insuranceAnQingInfos;
	}
	public void setInsuranceAnQingInfos(List<InsuranceAnQingInfo> insuranceAnQingInfos) {
		this.insuranceAnQingInfos = insuranceAnQingInfos;
	}
	public List<InsuranceAnQingPaydetails> getInsuranceAnQingPaydetails() {
		return insuranceAnQingPaydetails;
	}
	public void setInsuranceAnQingPaydetails(List<InsuranceAnQingPaydetails> insuranceAnQingPaydetails) {
		this.insuranceAnQingPaydetails = insuranceAnQingPaydetails;
	}
}
