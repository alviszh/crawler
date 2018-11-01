package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangEndowment;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangHtml;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangInjury;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMaternity;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMedical;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUnemployment;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceSZHeiLongJiangUserInfo insuranceSZHeiLongJiangUserInfo;
	public InsuranceSZHeiLongJiangMedical insuranceSZHeiLongJiangMedical;
	public InsuranceSZHeiLongJiangHtml insuranceSZHeiLongJiangHtml;
	public InsuranceSZHeiLongJiangEndowment insuranceSZHeiLongJiangEndowment;
	public InsuranceSZHeiLongJiangInjury insuranceSZHeiLongJiangInjury;
	public InsuranceSZHeiLongJiangMaternity insuranceSZHeiLongJiangMaternity;
	public InsuranceSZHeiLongJiangUnemployment insuranceSZHeiLongJiangUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceSZHeiLongJiangUserInfo=" + insuranceSZHeiLongJiangUserInfo
				+ ", insuranceSZHeiLongJiangMedical=" + insuranceSZHeiLongJiangMedical
				+ ", insuranceSZHeiLongJiangHtml=" + insuranceSZHeiLongJiangHtml + ", insuranceSZHeiLongJiangEndowment="
				+ insuranceSZHeiLongJiangEndowment + ", insuranceSZHeiLongJiangInjury=" + insuranceSZHeiLongJiangInjury
				+ ", insuranceSZHeiLongJiangMaternity=" + insuranceSZHeiLongJiangMaternity
				+ ", insuranceSZHeiLongJiangUnemployment=" + insuranceSZHeiLongJiangUnemployment + "]";
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
	public InsuranceSZHeiLongJiangUserInfo getInsuranceSZHeiLongJiangUserInfo() {
		return insuranceSZHeiLongJiangUserInfo;
	}
	public void setInsuranceSZHeiLongJiangUserInfo(InsuranceSZHeiLongJiangUserInfo insuranceSZHeiLongJiangUserInfo) {
		this.insuranceSZHeiLongJiangUserInfo = insuranceSZHeiLongJiangUserInfo;
	}
	public InsuranceSZHeiLongJiangMedical getInsuranceSZHeiLongJiangMedical() {
		return insuranceSZHeiLongJiangMedical;
	}
	public void setInsuranceSZHeiLongJiangMedical(InsuranceSZHeiLongJiangMedical insuranceSZHeiLongJiangMedical) {
		this.insuranceSZHeiLongJiangMedical = insuranceSZHeiLongJiangMedical;
	}
	public InsuranceSZHeiLongJiangHtml getInsuranceSZHeiLongJiangHtml() {
		return insuranceSZHeiLongJiangHtml;
	}
	public void setInsuranceSZHeiLongJiangHtml(InsuranceSZHeiLongJiangHtml insuranceSZHeiLongJiangHtml) {
		this.insuranceSZHeiLongJiangHtml = insuranceSZHeiLongJiangHtml;
	}
	public InsuranceSZHeiLongJiangEndowment getInsuranceSZHeiLongJiangEndowment() {
		return insuranceSZHeiLongJiangEndowment;
	}
	public void setInsuranceSZHeiLongJiangEndowment(InsuranceSZHeiLongJiangEndowment insuranceSZHeiLongJiangEndowment) {
		this.insuranceSZHeiLongJiangEndowment = insuranceSZHeiLongJiangEndowment;
	}
	public InsuranceSZHeiLongJiangInjury getInsuranceSZHeiLongJiangInjury() {
		return insuranceSZHeiLongJiangInjury;
	}
	public void setInsuranceSZHeiLongJiangInjury(InsuranceSZHeiLongJiangInjury insuranceSZHeiLongJiangInjury) {
		this.insuranceSZHeiLongJiangInjury = insuranceSZHeiLongJiangInjury;
	}
	public InsuranceSZHeiLongJiangMaternity getInsuranceSZHeiLongJiangMaternity() {
		return insuranceSZHeiLongJiangMaternity;
	}
	public void setInsuranceSZHeiLongJiangMaternity(InsuranceSZHeiLongJiangMaternity insuranceSZHeiLongJiangMaternity) {
		this.insuranceSZHeiLongJiangMaternity = insuranceSZHeiLongJiangMaternity;
	}
	public InsuranceSZHeiLongJiangUnemployment getInsuranceSZHeiLongJiangUnemployment() {
		return insuranceSZHeiLongJiangUnemployment;
	}
	public void setInsuranceSZHeiLongJiangUnemployment(
			InsuranceSZHeiLongJiangUnemployment insuranceSZHeiLongJiangUnemployment) {
		this.insuranceSZHeiLongJiangUnemployment = insuranceSZHeiLongJiangUnemployment;
	}
	
	
	
	
	
}
