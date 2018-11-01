package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingEndowment;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingHtml;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingInjury;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMaternity;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMedical;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingUnemployment;
import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingUserInfo;
  
public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public InsuranceNanNingUserInfo insuranceNanNingUserInfo;
	public InsuranceNanNingMedical insuranceNanNingMedical;
	public InsuranceNanNingHtml insuranceNanNingHtml;
	public InsuranceNanNingEndowment insuranceNanNingEndowment;
	public InsuranceNanNingInjury insuranceNanNingInjury;
	public InsuranceNanNingMaternity insuranceNanNingMaternity;
	public InsuranceNanNingUnemployment insuranceNanNingUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", insuranceNanNingUserInfo="
				+ insuranceNanNingUserInfo + ", insuranceNanNingMedical=" + insuranceNanNingMedical
				+ ", insuranceNanNingHtml=" + insuranceNanNingHtml + ", insuranceNanNingEndowment="
				+ insuranceNanNingEndowment + ", insuranceNanNingInjury=" + insuranceNanNingInjury
				+ ", insuranceNanNingMaternity=" + insuranceNanNingMaternity
				+ ", insuranceNanNingUnemployment=" + insuranceNanNingUnemployment + "]";
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
	public InsuranceNanNingUserInfo getInsuranceNanNingUserInfo() {
		return insuranceNanNingUserInfo;
	}
	public void setInsuranceNanNingUserInfo(InsuranceNanNingUserInfo insuranceNanNingUserInfo) {
		this.insuranceNanNingUserInfo = insuranceNanNingUserInfo;
	}
	public InsuranceNanNingMedical getInsuranceNanNingMedical() {
		return insuranceNanNingMedical;
	}
	public void setInsuranceNanNingMedical(InsuranceNanNingMedical insuranceNanNingMedical) {
		this.insuranceNanNingMedical = insuranceNanNingMedical;
	}
	public InsuranceNanNingHtml getInsuranceNanNingHtml() {
		return insuranceNanNingHtml;
	}
	public void setInsuranceNanNingHtml(InsuranceNanNingHtml insuranceNanNingHtml) {
		this.insuranceNanNingHtml = insuranceNanNingHtml;
	}
	public InsuranceNanNingEndowment getInsuranceNanNingEndowment() {
		return insuranceNanNingEndowment;
	}
	public void setInsuranceNanNingEndowment(InsuranceNanNingEndowment insuranceNanNingEndowment) {
		this.insuranceNanNingEndowment = insuranceNanNingEndowment;
	}
	public InsuranceNanNingInjury getInsuranceNanNingInjury() {
		return insuranceNanNingInjury;
	}
	public void setInsuranceNanNingInjury(InsuranceNanNingInjury insuranceNanNingInjury) {
		this.insuranceNanNingInjury = insuranceNanNingInjury;
	}
	public InsuranceNanNingMaternity getInsuranceNanNingMaternity() {
		return insuranceNanNingMaternity;
	}
	public void setInsuranceNanNingMaternity(InsuranceNanNingMaternity insuranceNanNingMaternity) {
		this.insuranceNanNingMaternity = insuranceNanNingMaternity;
	}
	public InsuranceNanNingUnemployment getInsuranceNanNingUnemployment() {
		return insuranceNanNingUnemployment;
	}
	public void setInsuranceNanNingUnemployment(InsuranceNanNingUnemployment insuranceNanNingUnemployment) {
		this.insuranceNanNingUnemployment = insuranceNanNingUnemployment;
	}
	
	
	
	
}
