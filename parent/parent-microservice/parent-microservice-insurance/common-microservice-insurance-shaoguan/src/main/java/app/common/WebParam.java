package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanHtml;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanMedical;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUnemployment;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public InsuranceShaoGuanUserInfo insuranceShaoGuanUserInfo;
	public InsuranceShaoGuanHtml insuranceShaoGuanHtml;
	public InsuranceShaoGuanEndowment insuranceShaoGuanEndowment;
	public InsuranceShaoGuanMedical insuranceShaoGuanMedical;
	public InsuranceShaoGuanUnemployment insuranceShaoGuanUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", insuranceShaoGuanUserInfo="
				+ insuranceShaoGuanUserInfo + ", insuranceShaoGuanHtml=" + insuranceShaoGuanHtml
				+ ", insuranceShaoGuanEndowment=" + insuranceShaoGuanEndowment + ", insuranceShaoGuanMedical="
				+ insuranceShaoGuanMedical + ", insuranceShaoGuanUnemployment=" + insuranceShaoGuanUnemployment + "]";
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
	public InsuranceShaoGuanUserInfo getInsuranceShaoGuanUserInfo() {
		return insuranceShaoGuanUserInfo;
	}
	public void setInsuranceShaoGuanUserInfo(InsuranceShaoGuanUserInfo insuranceShaoGuanUserInfo) {
		this.insuranceShaoGuanUserInfo = insuranceShaoGuanUserInfo;
	}
	public InsuranceShaoGuanHtml getInsuranceShaoGuanHtml() {
		return insuranceShaoGuanHtml;
	}
	public void setInsuranceShaoGuanHtml(InsuranceShaoGuanHtml insuranceShaoGuanHtml) {
		this.insuranceShaoGuanHtml = insuranceShaoGuanHtml;
	}
	public InsuranceShaoGuanEndowment getInsuranceShaoGuanEndowment() {
		return insuranceShaoGuanEndowment;
	}
	public void setInsuranceShaoGuanEndowment(InsuranceShaoGuanEndowment insuranceShaoGuanEndowment) {
		this.insuranceShaoGuanEndowment = insuranceShaoGuanEndowment;
	}
	public InsuranceShaoGuanMedical getInsuranceShaoGuanMedical() {
		return insuranceShaoGuanMedical;
	}
	public void setInsuranceShaoGuanMedical(InsuranceShaoGuanMedical insuranceShaoGuanMedical) {
		this.insuranceShaoGuanMedical = insuranceShaoGuanMedical;
	}
	public InsuranceShaoGuanUnemployment getInsuranceShaoGuanUnemployment() {
		return insuranceShaoGuanUnemployment;
	}
	public void setInsuranceShaoGuanUnemployment(InsuranceShaoGuanUnemployment insuranceShaoGuanUnemployment) {
		this.insuranceShaoGuanUnemployment = insuranceShaoGuanUnemployment;
	}
	
	
}
