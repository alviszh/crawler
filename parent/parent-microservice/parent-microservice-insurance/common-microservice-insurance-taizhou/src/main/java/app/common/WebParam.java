package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouHtml;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouInjury;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouMedical;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceTaiZhouUserInfo insuranceTaiZhouUserInfo;
	public InsuranceTaiZhouMedical insuranceTaiZhouMedical;
	public InsuranceTaiZhouHtml insuranceTaiZhouHtml;
	public InsuranceTaiZhouEndowment insuranceTaiZhouEndowment;
	public InsuranceTaiZhouUnemployment insuranceTaiZhouUnemployment;
	public InsuranceTaiZhouInjury insuranceTaiZhouInjury;
	public InsuranceTaiZhouMaternity insuranceTaiZhouMaternity;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceTaiZhouUserInfo=" + insuranceTaiZhouUserInfo + ", insuranceTaiZhouMedical="
				+ insuranceTaiZhouMedical + ", insuranceTaiZhouHtml=" + insuranceTaiZhouHtml
				+ ", insuranceTaiZhouEndowment=" + insuranceTaiZhouEndowment + ", insuranceTaiZhouUnemployment="
				+ insuranceTaiZhouUnemployment + ", insuranceTaiZhouInjury=" + insuranceTaiZhouInjury
				+ ", insuranceTaiZhouMaternity=" + insuranceTaiZhouMaternity + "]";
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
	public InsuranceTaiZhouUserInfo getInsuranceTaiZhouUserInfo() {
		return insuranceTaiZhouUserInfo;
	}
	public void setInsuranceTaiZhouUserInfo(InsuranceTaiZhouUserInfo insuranceTaiZhouUserInfo) {
		this.insuranceTaiZhouUserInfo = insuranceTaiZhouUserInfo;
	}
	public InsuranceTaiZhouMedical getInsuranceTaiZhouMedical() {
		return insuranceTaiZhouMedical;
	}
	public void setInsuranceTaiZhouMedical(InsuranceTaiZhouMedical insuranceTaiZhouMedical) {
		this.insuranceTaiZhouMedical = insuranceTaiZhouMedical;
	}
	public InsuranceTaiZhouHtml getInsuranceTaiZhouHtml() {
		return insuranceTaiZhouHtml;
	}
	public void setInsuranceTaiZhouHtml(InsuranceTaiZhouHtml insuranceTaiZhouHtml) {
		this.insuranceTaiZhouHtml = insuranceTaiZhouHtml;
	}
	public InsuranceTaiZhouEndowment getInsuranceTaiZhouEndowment() {
		return insuranceTaiZhouEndowment;
	}
	public void setInsuranceTaiZhouEndowment(InsuranceTaiZhouEndowment insuranceTaiZhouEndowment) {
		this.insuranceTaiZhouEndowment = insuranceTaiZhouEndowment;
	}
	public InsuranceTaiZhouUnemployment getInsuranceTaiZhouUnemployment() {
		return insuranceTaiZhouUnemployment;
	}
	public void setInsuranceTaiZhouUnemployment(InsuranceTaiZhouUnemployment insuranceTaiZhouUnemployment) {
		this.insuranceTaiZhouUnemployment = insuranceTaiZhouUnemployment;
	}
	public InsuranceTaiZhouInjury getInsuranceTaiZhouInjury() {
		return insuranceTaiZhouInjury;
	}
	public void setInsuranceTaiZhouInjury(InsuranceTaiZhouInjury insuranceTaiZhouInjury) {
		this.insuranceTaiZhouInjury = insuranceTaiZhouInjury;
	}
	public InsuranceTaiZhouMaternity getInsuranceTaiZhouMaternity() {
		return insuranceTaiZhouMaternity;
	}
	public void setInsuranceTaiZhouMaternity(InsuranceTaiZhouMaternity insuranceTaiZhouMaternity) {
		this.insuranceTaiZhouMaternity = insuranceTaiZhouMaternity;
	}
	
	
	
}
