package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingEndowment;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingHtml;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingInjury;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingMaternity;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingMedical;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingUnemployment;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceZhaoQingUserInfo insuranceZhaoQingUserInfo;
	public InsuranceZhaoQingMedical insuranceZhaoQingMedical;
	public InsuranceZhaoQingHtml insuranceZhaoQingHtml;
	public InsuranceZhaoQingEndowment insuranceZhaoQingEndowment;
	public InsuranceZhaoQingUnemployment insuranceZhaoQingUnemployment;
	public InsuranceZhaoQingInjury insuranceZhaoQingInjury;
	public InsuranceZhaoQingMaternity insuranceZhaoQingMaternity;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceZhaoQingUserInfo=" + insuranceZhaoQingUserInfo + ", insuranceZhaoQingMedical="
				+ insuranceZhaoQingMedical + ", insuranceZhaoQingHtml=" + insuranceZhaoQingHtml
				+ ", insuranceZhaoQingEndowment=" + insuranceZhaoQingEndowment + ", insuranceZhaoQingUnemployment="
				+ insuranceZhaoQingUnemployment + ", insuranceZhaoQingInjury=" + insuranceZhaoQingInjury
				+ ", insuranceZhaoQingMaternity=" + insuranceZhaoQingMaternity + "]";
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
	public InsuranceZhaoQingUserInfo getInsuranceZhaoQingUserInfo() {
		return insuranceZhaoQingUserInfo;
	}
	public void setInsuranceZhaoQingUserInfo(InsuranceZhaoQingUserInfo insuranceZhaoQingUserInfo) {
		this.insuranceZhaoQingUserInfo = insuranceZhaoQingUserInfo;
	}
	public InsuranceZhaoQingMedical getInsuranceZhaoQingMedical() {
		return insuranceZhaoQingMedical;
	}
	public void setInsuranceZhaoQingMedical(InsuranceZhaoQingMedical insuranceZhaoQingMedical) {
		this.insuranceZhaoQingMedical = insuranceZhaoQingMedical;
	}
	public InsuranceZhaoQingHtml getInsuranceZhaoQingHtml() {
		return insuranceZhaoQingHtml;
	}
	public void setInsuranceZhaoQingHtml(InsuranceZhaoQingHtml insuranceZhaoQingHtml) {
		this.insuranceZhaoQingHtml = insuranceZhaoQingHtml;
	}
	public InsuranceZhaoQingEndowment getInsuranceZhaoQingEndowment() {
		return insuranceZhaoQingEndowment;
	}
	public void setInsuranceZhaoQingEndowment(InsuranceZhaoQingEndowment insuranceZhaoQingEndowment) {
		this.insuranceZhaoQingEndowment = insuranceZhaoQingEndowment;
	}
	public InsuranceZhaoQingUnemployment getInsuranceZhaoQingUnemployment() {
		return insuranceZhaoQingUnemployment;
	}
	public void setInsuranceZhaoQingUnemployment(InsuranceZhaoQingUnemployment insuranceZhaoQingUnemployment) {
		this.insuranceZhaoQingUnemployment = insuranceZhaoQingUnemployment;
	}
	public InsuranceZhaoQingInjury getInsuranceZhaoQingInjury() {
		return insuranceZhaoQingInjury;
	}
	public void setInsuranceZhaoQingInjury(InsuranceZhaoQingInjury insuranceZhaoQingInjury) {
		this.insuranceZhaoQingInjury = insuranceZhaoQingInjury;
	}
	public InsuranceZhaoQingMaternity getInsuranceZhaoQingMaternity() {
		return insuranceZhaoQingMaternity;
	}
	public void setInsuranceZhaoQingMaternity(InsuranceZhaoQingMaternity insuranceZhaoQingMaternity) {
		this.insuranceZhaoQingMaternity = insuranceZhaoQingMaternity;
	}
	
	
	
}
