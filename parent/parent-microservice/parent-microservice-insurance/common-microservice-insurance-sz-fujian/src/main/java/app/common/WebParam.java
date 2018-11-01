package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouHtml;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouInjury;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouMedical;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceZhangZhouUserInfo insuranceZhangZhouUserInfo;
	public InsuranceZhangZhouMedical insuranceZhangZhouMedical;
	public InsuranceZhangZhouHtml insuranceZhangZhouHtml;
	public InsuranceZhangZhouEndowment insuranceZhangZhouEndowment;
	public InsuranceZhangZhouUnemployment insuranceZhangZhouUnemployment;
	public InsuranceZhangZhouInjury insuranceZhangZhouInjury;
	public InsuranceZhangZhouMaternity insuranceZhangZhouMaternity;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceZhangZhouUserInfo=" + insuranceZhangZhouUserInfo + ", insuranceZhangZhouMedical="
				+ insuranceZhangZhouMedical + ", insuranceZhangZhouHtml=" + insuranceZhangZhouHtml
				+ ", insuranceZhangZhouEndowment=" + insuranceZhangZhouEndowment + ", insuranceZhangZhouUnemployment="
				+ insuranceZhangZhouUnemployment + ", insuranceZhangZhouInjury=" + insuranceZhangZhouInjury
				+ ", insuranceZhangZhouMaternity=" + insuranceZhangZhouMaternity + "]";
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
	public InsuranceZhangZhouUserInfo getInsuranceZhangZhouUserInfo() {
		return insuranceZhangZhouUserInfo;
	}
	public void setInsuranceZhangZhouUserInfo(InsuranceZhangZhouUserInfo insuranceZhangZhouUserInfo) {
		this.insuranceZhangZhouUserInfo = insuranceZhangZhouUserInfo;
	}
	public InsuranceZhangZhouMedical getInsuranceZhangZhouMedical() {
		return insuranceZhangZhouMedical;
	}
	public void setInsuranceZhangZhouMedical(InsuranceZhangZhouMedical insuranceZhangZhouMedical) {
		this.insuranceZhangZhouMedical = insuranceZhangZhouMedical;
	}
	public InsuranceZhangZhouHtml getInsuranceZhangZhouHtml() {
		return insuranceZhangZhouHtml;
	}
	public void setInsuranceZhangZhouHtml(InsuranceZhangZhouHtml insuranceZhangZhouHtml) {
		this.insuranceZhangZhouHtml = insuranceZhangZhouHtml;
	}
	public InsuranceZhangZhouEndowment getInsuranceZhangZhouEndowment() {
		return insuranceZhangZhouEndowment;
	}
	public void setInsuranceZhangZhouEndowment(InsuranceZhangZhouEndowment insuranceZhangZhouEndowment) {
		this.insuranceZhangZhouEndowment = insuranceZhangZhouEndowment;
	}
	public InsuranceZhangZhouUnemployment getInsuranceZhangZhouUnemployment() {
		return insuranceZhangZhouUnemployment;
	}
	public void setInsuranceZhangZhouUnemployment(InsuranceZhangZhouUnemployment insuranceZhangZhouUnemployment) {
		this.insuranceZhangZhouUnemployment = insuranceZhangZhouUnemployment;
	}
	public InsuranceZhangZhouInjury getInsuranceZhangZhouInjury() {
		return insuranceZhangZhouInjury;
	}
	public void setInsuranceZhangZhouInjury(InsuranceZhangZhouInjury insuranceZhangZhouInjury) {
		this.insuranceZhangZhouInjury = insuranceZhangZhouInjury;
	}
	public InsuranceZhangZhouMaternity getInsuranceZhangZhouMaternity() {
		return insuranceZhangZhouMaternity;
	}
	public void setInsuranceZhangZhouMaternity(InsuranceZhangZhouMaternity insuranceZhangZhouMaternity) {
		this.insuranceZhangZhouMaternity = insuranceZhangZhouMaternity;
	}
	
	
	
}
