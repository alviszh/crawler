package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingEndowment;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingHtml;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingInjury;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingMaternity;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingMedical;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUnemployment;
import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public InsuranceShaoXingUserInfo insuranceShaoXingUserInfo;
	public InsuranceShaoXingHtml insuranceShaoXingHtml;
	public InsuranceShaoXingEndowment insuranceShaoXingEndowment;
	public InsuranceShaoXingMedical insuranceShaoXingMedical;
	public InsuranceShaoXingUnemployment insuranceShaoXingUnemployment;
	public InsuranceShaoXingInjury insuranceShaoXingInjury;
	public InsuranceShaoXingMaternity insuranceShaoXingMaternity;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", insuranceShaoXingUserInfo="
				+ insuranceShaoXingUserInfo + ", insuranceShaoXingHtml=" + insuranceShaoXingHtml
				+ ", insuranceShaoXingEndowment=" + insuranceShaoXingEndowment + ", insuranceShaoXingMedical="
				+ insuranceShaoXingMedical + ", insuranceShaoXingUnemployment=" + insuranceShaoXingUnemployment
				+ ", insuranceShaoXingInjury=" + insuranceShaoXingInjury + ", insuranceShaoXingMaternity="
				+ insuranceShaoXingMaternity + "]";
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
	public InsuranceShaoXingUserInfo getInsuranceShaoXingUserInfo() {
		return insuranceShaoXingUserInfo;
	}
	public void setInsuranceShaoXingUserInfo(InsuranceShaoXingUserInfo insuranceShaoXingUserInfo) {
		this.insuranceShaoXingUserInfo = insuranceShaoXingUserInfo;
	}
	public InsuranceShaoXingHtml getInsuranceShaoXingHtml() {
		return insuranceShaoXingHtml;
	}
	public void setInsuranceShaoXingHtml(InsuranceShaoXingHtml insuranceShaoXingHtml) {
		this.insuranceShaoXingHtml = insuranceShaoXingHtml;
	}
	public InsuranceShaoXingEndowment getInsuranceShaoXingEndowment() {
		return insuranceShaoXingEndowment;
	}
	public void setInsuranceShaoXingEndowment(InsuranceShaoXingEndowment insuranceShaoXingEndowment) {
		this.insuranceShaoXingEndowment = insuranceShaoXingEndowment;
	}
	public InsuranceShaoXingMedical getInsuranceShaoXingMedical() {
		return insuranceShaoXingMedical;
	}
	public void setInsuranceShaoXingMedical(InsuranceShaoXingMedical insuranceShaoXingMedical) {
		this.insuranceShaoXingMedical = insuranceShaoXingMedical;
	}
	public InsuranceShaoXingUnemployment getInsuranceShaoXingUnemployment() {
		return insuranceShaoXingUnemployment;
	}
	public void setInsuranceShaoXingUnemployment(InsuranceShaoXingUnemployment insuranceShaoXingUnemployment) {
		this.insuranceShaoXingUnemployment = insuranceShaoXingUnemployment;
	}
	public InsuranceShaoXingInjury getInsuranceShaoXingInjury() {
		return insuranceShaoXingInjury;
	}
	public void setInsuranceShaoXingInjury(InsuranceShaoXingInjury insuranceShaoXingInjury) {
		this.insuranceShaoXingInjury = insuranceShaoXingInjury;
	}
	public InsuranceShaoXingMaternity getInsuranceShaoXingMaternity() {
		return insuranceShaoXingMaternity;
	}
	public void setInsuranceShaoXingMaternity(InsuranceShaoXingMaternity insuranceShaoXingMaternity) {
		this.insuranceShaoXingMaternity = insuranceShaoXingMaternity;
	}
	
	
}
