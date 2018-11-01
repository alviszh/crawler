package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiEndowment;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiHtml;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiInjury;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiMaternity;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiMedical;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiUnemployment;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceEnShiUserInfo insuranceEnShiUserInfo;
	public InsuranceEnShiMedical insuranceEnShiMedical;
	public InsuranceEnShiHtml insuranceEnShiHtml;
	public InsuranceEnShiEndowment insuranceEnShiEndowment;
	public InsuranceEnShiInjury insuranceEnShiInjury;
	public InsuranceEnShiMaternity insuranceEnShiMaternity;
	public InsuranceEnShiUnemployment insuranceEnShiUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceEnShiUserInfo=" + insuranceEnShiUserInfo + ", insuranceEnShiMedical="
				+ insuranceEnShiMedical + ", insuranceEnShiHtml=" + insuranceEnShiHtml
				+ ", insuranceEnShiEndowment=" + insuranceEnShiEndowment + ", insuranceEnShiInjury="
				+ insuranceEnShiInjury + ", insuranceEnShiMaternity=" + insuranceEnShiMaternity
				+ ", insuranceEnShiUnemployment=" + insuranceEnShiUnemployment + "]";
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
	public InsuranceEnShiUserInfo getInsuranceEnShiUserInfo() {
		return insuranceEnShiUserInfo;
	}
	public void setInsuranceEnShiUserInfo(InsuranceEnShiUserInfo insuranceEnShiUserInfo) {
		this.insuranceEnShiUserInfo = insuranceEnShiUserInfo;
	}
	public InsuranceEnShiMedical getInsuranceEnShiMedical() {
		return insuranceEnShiMedical;
	}
	public void setInsuranceEnShiMedical(InsuranceEnShiMedical insuranceEnShiMedical) {
		this.insuranceEnShiMedical = insuranceEnShiMedical;
	}
	public InsuranceEnShiHtml getInsuranceEnShiHtml() {
		return insuranceEnShiHtml;
	}
	public void setInsuranceEnShiHtml(InsuranceEnShiHtml insuranceEnShiHtml) {
		this.insuranceEnShiHtml = insuranceEnShiHtml;
	}
	public InsuranceEnShiEndowment getInsuranceEnShiEndowment() {
		return insuranceEnShiEndowment;
	}
	public void setInsuranceEnShiEndowment(InsuranceEnShiEndowment insuranceEnShiEndowment) {
		this.insuranceEnShiEndowment = insuranceEnShiEndowment;
	}
	public InsuranceEnShiInjury getInsuranceEnShiInjury() {
		return insuranceEnShiInjury;
	}
	public void setInsuranceEnShiInjury(InsuranceEnShiInjury insuranceEnShiInjury) {
		this.insuranceEnShiInjury = insuranceEnShiInjury;
	}
	public InsuranceEnShiMaternity getInsuranceEnShiMaternity() {
		return insuranceEnShiMaternity;
	}
	public void setInsuranceEnShiMaternity(InsuranceEnShiMaternity insuranceEnShiMaternity) {
		this.insuranceEnShiMaternity = insuranceEnShiMaternity;
	}
	public InsuranceEnShiUnemployment getInsuranceEnShiUnemployment() {
		return insuranceEnShiUnemployment;
	}
	public void setInsuranceEnShiUnemployment(InsuranceEnShiUnemployment insuranceEnShiUnemployment) {
		this.insuranceEnShiUnemployment = insuranceEnShiUnemployment;
	}
	
}
