package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouBasicinfo;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouPersion;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public InsuranceGanZhouUserInfo  insuranceGanZhouUserInfo;	
	public InsuranceGanZhouPersion  insuranceGanZhouPersion;	
	public WebClient  webClient;
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public List<InsuranceGanZhouBasicinfo>  basicinfoList;
	
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
	public InsuranceGanZhouUserInfo getInsuranceGanZhouUserInfo() {
		return insuranceGanZhouUserInfo;
	}
	public void setInsuranceGanZhouUserInfo(InsuranceGanZhouUserInfo insuranceGanZhouUserInfo) {
		this.insuranceGanZhouUserInfo = insuranceGanZhouUserInfo;
	}
	public InsuranceGanZhouPersion getInsuranceGanZhouPersion() {
		return insuranceGanZhouPersion;
	}
	public void setInsuranceGanZhouPersion(InsuranceGanZhouPersion insuranceGanZhouPersion) {
		this.insuranceGanZhouPersion = insuranceGanZhouPersion;
	}
	public List<InsuranceGanZhouBasicinfo> getBasicinfoList() {
		return basicinfoList;
	}
	public void setBasicinfoList(List<InsuranceGanZhouBasicinfo> basicinfoList) {
		this.basicinfoList = basicinfoList;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	
}
