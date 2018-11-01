package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuEndowment;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuHtml;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuInjury;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMaternity;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMedical;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUnemployment;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceBengBuUserInfo insuranceBengBuUserInfo;
	public InsuranceBengBuMedical insuranceBengBuMedical;
	public InsuranceBengBuHtml insuranceBengBuHtml;
	public InsuranceBengBuEndowment insuranceBengBuEndowment;
	public InsuranceBengBuInjury insuranceBengBuInjury;
	public InsuranceBengBuMaternity insuranceBengBuMaternity;
	public InsuranceBengBuUnemployment insuranceBengBuUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceBengBuUserInfo=" + insuranceBengBuUserInfo + ", insuranceBengBuMedical="
				+ insuranceBengBuMedical + ", insuranceBengBuHtml=" + insuranceBengBuHtml
				+ ", insuranceBengBuEndowment=" + insuranceBengBuEndowment + ", insuranceBengBuInjury="
				+ insuranceBengBuInjury + ", insuranceBengBuMaternity=" + insuranceBengBuMaternity
				+ ", insuranceBengBuUnemployment=" + insuranceBengBuUnemployment + "]";
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
	public InsuranceBengBuUserInfo getInsuranceBengBuUserInfo() {
		return insuranceBengBuUserInfo;
	}
	public void setInsuranceBengBuUserInfo(InsuranceBengBuUserInfo insuranceBengBuUserInfo) {
		this.insuranceBengBuUserInfo = insuranceBengBuUserInfo;
	}
	public InsuranceBengBuMedical getInsuranceBengBuMedical() {
		return insuranceBengBuMedical;
	}
	public void setInsuranceBengBuMedical(InsuranceBengBuMedical insuranceBengBuMedical) {
		this.insuranceBengBuMedical = insuranceBengBuMedical;
	}
	public InsuranceBengBuHtml getInsuranceBengBuHtml() {
		return insuranceBengBuHtml;
	}
	public void setInsuranceBengBuHtml(InsuranceBengBuHtml insuranceBengBuHtml) {
		this.insuranceBengBuHtml = insuranceBengBuHtml;
	}
	public InsuranceBengBuEndowment getInsuranceBengBuEndowment() {
		return insuranceBengBuEndowment;
	}
	public void setInsuranceBengBuEndowment(InsuranceBengBuEndowment insuranceBengBuEndowment) {
		this.insuranceBengBuEndowment = insuranceBengBuEndowment;
	}
	public InsuranceBengBuInjury getInsuranceBengBuInjury() {
		return insuranceBengBuInjury;
	}
	public void setInsuranceBengBuInjury(InsuranceBengBuInjury insuranceBengBuInjury) {
		this.insuranceBengBuInjury = insuranceBengBuInjury;
	}
	public InsuranceBengBuMaternity getInsuranceBengBuMaternity() {
		return insuranceBengBuMaternity;
	}
	public void setInsuranceBengBuMaternity(InsuranceBengBuMaternity insuranceBengBuMaternity) {
		this.insuranceBengBuMaternity = insuranceBengBuMaternity;
	}
	public InsuranceBengBuUnemployment getInsuranceBengBuUnemployment() {
		return insuranceBengBuUnemployment;
	}
	public void setInsuranceBengBuUnemployment(InsuranceBengBuUnemployment insuranceBengBuUnemployment) {
		this.insuranceBengBuUnemployment = insuranceBengBuUnemployment;
	}
	
}
