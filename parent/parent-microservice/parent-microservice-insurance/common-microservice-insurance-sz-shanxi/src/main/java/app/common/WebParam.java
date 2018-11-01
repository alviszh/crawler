package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiEndowment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiHtml;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiInjury;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiMaternity;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiMedical;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiUnemployment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceSZShanXiUserInfo insuranceSZShanXiUserInfo;
	public InsuranceSZShanXiMedical insuranceSZShanXiMedical;
	public InsuranceSZShanXiHtml insuranceSZShanXiHtml;
	public InsuranceSZShanXiEndowment insuranceSZShanXiEndowment;
	public InsuranceSZShanXiUnemployment insuranceSZShanXiUnemployment;
	public InsuranceSZShanXiInjury insuranceSZShanXiInjury;
	public InsuranceSZShanXiMaternity insuranceSZShanXiMaternity;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceSZShanXiUserInfo=" + insuranceSZShanXiUserInfo + ", insuranceSZShanXiMedical="
				+ insuranceSZShanXiMedical + ", insuranceSZShanXiHtml=" + insuranceSZShanXiHtml
				+ ", insuranceSZShanXiEndowment=" + insuranceSZShanXiEndowment + ", insuranceSZShanXiUnemployment="
				+ insuranceSZShanXiUnemployment + ", insuranceSZShanXiInjury=" + insuranceSZShanXiInjury
				+ ", insuranceSZShanXiMaternity=" + insuranceSZShanXiMaternity + "]";
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
	public InsuranceSZShanXiUserInfo getInsuranceSZShanXiUserInfo() {
		return insuranceSZShanXiUserInfo;
	}
	public void setInsuranceSZShanXiUserInfo(InsuranceSZShanXiUserInfo insuranceSZShanXiUserInfo) {
		this.insuranceSZShanXiUserInfo = insuranceSZShanXiUserInfo;
	}
	public InsuranceSZShanXiMedical getInsuranceSZShanXiMedical() {
		return insuranceSZShanXiMedical;
	}
	public void setInsuranceSZShanXiMedical(InsuranceSZShanXiMedical insuranceSZShanXiMedical) {
		this.insuranceSZShanXiMedical = insuranceSZShanXiMedical;
	}
	public InsuranceSZShanXiHtml getInsuranceSZShanXiHtml() {
		return insuranceSZShanXiHtml;
	}
	public void setInsuranceSZShanXiHtml(InsuranceSZShanXiHtml insuranceSZShanXiHtml) {
		this.insuranceSZShanXiHtml = insuranceSZShanXiHtml;
	}
	public InsuranceSZShanXiEndowment getInsuranceSZShanXiEndowment() {
		return insuranceSZShanXiEndowment;
	}
	public void setInsuranceSZShanXiEndowment(InsuranceSZShanXiEndowment insuranceSZShanXiEndowment) {
		this.insuranceSZShanXiEndowment = insuranceSZShanXiEndowment;
	}
	public InsuranceSZShanXiUnemployment getInsuranceSZShanXiUnemployment() {
		return insuranceSZShanXiUnemployment;
	}
	public void setInsuranceSZShanXiUnemployment(InsuranceSZShanXiUnemployment insuranceSZShanXiUnemployment) {
		this.insuranceSZShanXiUnemployment = insuranceSZShanXiUnemployment;
	}
	public InsuranceSZShanXiInjury getInsuranceSZShanXiInjury() {
		return insuranceSZShanXiInjury;
	}
	public void setInsuranceSZShanXiInjury(InsuranceSZShanXiInjury insuranceSZShanXiInjury) {
		this.insuranceSZShanXiInjury = insuranceSZShanXiInjury;
	}
	public InsuranceSZShanXiMaternity getInsuranceSZShanXiMaternity() {
		return insuranceSZShanXiMaternity;
	}
	public void setInsuranceSZShanXiMaternity(InsuranceSZShanXiMaternity insuranceSZShanXiMaternity) {
		this.insuranceSZShanXiMaternity = insuranceSZShanXiMaternity;
	}
	
	
	
}
