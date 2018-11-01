package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouHtml;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouMedical;
import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceLuZhouUserInfo insuranceLuZhouUserInfo;
	public InsuranceLuZhouMedical insuranceLuZhouMedical;
	public InsuranceLuZhouHtml insuranceLuZhouHtml;
	public InsuranceLuZhouEndowment insuranceLuZhouEndowment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceLuZhouUserInfo=" + insuranceLuZhouUserInfo + ", insuranceLuZhouMedical="
				+ insuranceLuZhouMedical + ", insuranceLuZhouHtml=" + insuranceLuZhouHtml
				+ ", insuranceLuZhouEndowment=" + insuranceLuZhouEndowment + "]";
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
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
	public String getWebHandle() {
		return webHandle;
	}
	public void setWebHandle(String webHandle) {
		this.webHandle = webHandle;
	}
	public InsuranceLuZhouUserInfo getInsuranceLuZhouUserInfo() {
		return insuranceLuZhouUserInfo;
	}
	public void setInsuranceLuZhouUserInfo(InsuranceLuZhouUserInfo insuranceLuZhouUserInfo) {
		this.insuranceLuZhouUserInfo = insuranceLuZhouUserInfo;
	}
	public InsuranceLuZhouMedical getInsuranceLuZhouMedical() {
		return insuranceLuZhouMedical;
	}
	public void setInsuranceLuZhouMedical(InsuranceLuZhouMedical insuranceLuZhouMedical) {
		this.insuranceLuZhouMedical = insuranceLuZhouMedical;
	}
	public InsuranceLuZhouHtml getInsuranceLuZhouHtml() {
		return insuranceLuZhouHtml;
	}
	public void setInsuranceLuZhouHtml(InsuranceLuZhouHtml insuranceLuZhouHtml) {
		this.insuranceLuZhouHtml = insuranceLuZhouHtml;
	}
	public InsuranceLuZhouEndowment getInsuranceLuZhouEndowment() {
		return insuranceLuZhouEndowment;
	}
	public void setInsuranceLuZhouEndowment(InsuranceLuZhouEndowment insuranceLuZhouEndowment) {
		this.insuranceLuZhouEndowment = insuranceLuZhouEndowment;
	}
	
	
}
