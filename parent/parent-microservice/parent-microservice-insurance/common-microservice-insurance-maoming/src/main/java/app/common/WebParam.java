package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingEndowment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingHtml;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingInjury;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMaternity;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMedical;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUnemployment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceMaoMingUserInfo insuranceMaoMingUserInfo;
	public InsuranceMaoMingMedical insuranceMaoMingMedical;
	public InsuranceMaoMingHtml insuranceMaoMingHtml;
	public InsuranceMaoMingEndowment insuranceMaoMingEndowment;
	public InsuranceMaoMingInjury insuranceMaoMingInjury;
	public InsuranceMaoMingMaternity insuranceMaoMingMaternity;
	public InsuranceMaoMingUnemployment insuranceMaoMingUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceMaoMingUserInfo=" + insuranceMaoMingUserInfo + ", insuranceMaoMingMedical="
				+ insuranceMaoMingMedical + ", insuranceMaoMingHtml=" + insuranceMaoMingHtml
				+ ", insuranceMaoMingEndowment=" + insuranceMaoMingEndowment + ", insuranceMaoMingInjury="
				+ insuranceMaoMingInjury + ", insuranceMaoMingMaternity=" + insuranceMaoMingMaternity
				+ ", insuranceMaoMingUnemployment=" + insuranceMaoMingUnemployment + "]";
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
	public InsuranceMaoMingUserInfo getInsuranceMaoMingUserInfo() {
		return insuranceMaoMingUserInfo;
	}
	public void setInsuranceMaoMingUserInfo(InsuranceMaoMingUserInfo insuranceMaoMingUserInfo) {
		this.insuranceMaoMingUserInfo = insuranceMaoMingUserInfo;
	}
	public InsuranceMaoMingMedical getInsuranceMaoMingMedical() {
		return insuranceMaoMingMedical;
	}
	public void setInsuranceMaoMingMedical(InsuranceMaoMingMedical insuranceMaoMingMedical) {
		this.insuranceMaoMingMedical = insuranceMaoMingMedical;
	}
	public InsuranceMaoMingHtml getInsuranceMaoMingHtml() {
		return insuranceMaoMingHtml;
	}
	public void setInsuranceMaoMingHtml(InsuranceMaoMingHtml insuranceMaoMingHtml) {
		this.insuranceMaoMingHtml = insuranceMaoMingHtml;
	}
	public InsuranceMaoMingEndowment getInsuranceMaoMingEndowment() {
		return insuranceMaoMingEndowment;
	}
	public void setInsuranceMaoMingEndowment(InsuranceMaoMingEndowment insuranceMaoMingEndowment) {
		this.insuranceMaoMingEndowment = insuranceMaoMingEndowment;
	}
	public InsuranceMaoMingInjury getInsuranceMaoMingInjury() {
		return insuranceMaoMingInjury;
	}
	public void setInsuranceMaoMingInjury(InsuranceMaoMingInjury insuranceMaoMingInjury) {
		this.insuranceMaoMingInjury = insuranceMaoMingInjury;
	}
	public InsuranceMaoMingMaternity getInsuranceMaoMingMaternity() {
		return insuranceMaoMingMaternity;
	}
	public void setInsuranceMaoMingMaternity(InsuranceMaoMingMaternity insuranceMaoMingMaternity) {
		this.insuranceMaoMingMaternity = insuranceMaoMingMaternity;
	}
	public InsuranceMaoMingUnemployment getInsuranceMaoMingUnemployment() {
		return insuranceMaoMingUnemployment;
	}
	public void setInsuranceMaoMingUnemployment(InsuranceMaoMingUnemployment insuranceMaoMingUnemployment) {
		this.insuranceMaoMingUnemployment = insuranceMaoMingUnemployment;
	}
	
}
