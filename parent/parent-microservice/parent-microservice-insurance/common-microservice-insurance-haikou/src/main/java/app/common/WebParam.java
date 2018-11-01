package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouEndowment;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouHtml;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouInjury;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouMaternity;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouMedical;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouUnemployment;
import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public InsuranceHaiKouUserInfo insuranceHaiKouUserInfo;
	public InsuranceHaiKouMedical insuranceHaiKouMedical;
	public InsuranceHaiKouHtml insuranceHaiKouHtml;
	public InsuranceHaiKouEndowment insuranceHaiKouEndowment;
	public InsuranceHaiKouInjury insuranceHaiKouInjury;
	public InsuranceHaiKouMaternity insuranceHaiKouMaternity;
	public InsuranceHaiKouUnemployment insuranceHaiKouUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", insuranceHaiKouUserInfo="
				+ insuranceHaiKouUserInfo + ", insuranceHaiKouMedical=" + insuranceHaiKouMedical
				+ ", insuranceHaiKouHtml=" + insuranceHaiKouHtml + ", insuranceHaiKouEndowment="
				+ insuranceHaiKouEndowment + ", insuranceHaiKouInjury=" + insuranceHaiKouInjury
				+ ", insuranceHaiKouMaternity=" + insuranceHaiKouMaternity + ", insuranceHaiKouUnemployment="
				+ insuranceHaiKouUnemployment + "]";
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
	public InsuranceHaiKouUserInfo getInsuranceHaiKouUserInfo() {
		return insuranceHaiKouUserInfo;
	}
	public void setInsuranceHaiKouUserInfo(InsuranceHaiKouUserInfo insuranceHaiKouUserInfo) {
		this.insuranceHaiKouUserInfo = insuranceHaiKouUserInfo;
	}
	public InsuranceHaiKouMedical getInsuranceHaiKouMedical() {
		return insuranceHaiKouMedical;
	}
	public void setInsuranceHaiKouMedical(InsuranceHaiKouMedical insuranceHaiKouMedical) {
		this.insuranceHaiKouMedical = insuranceHaiKouMedical;
	}
	public InsuranceHaiKouHtml getInsuranceHaiKouHtml() {
		return insuranceHaiKouHtml;
	}
	public void setInsuranceHaiKouHtml(InsuranceHaiKouHtml insuranceHaiKouHtml) {
		this.insuranceHaiKouHtml = insuranceHaiKouHtml;
	}
	public InsuranceHaiKouEndowment getInsuranceHaiKouEndowment() {
		return insuranceHaiKouEndowment;
	}
	public void setInsuranceHaiKouEndowment(InsuranceHaiKouEndowment insuranceHaiKouEndowment) {
		this.insuranceHaiKouEndowment = insuranceHaiKouEndowment;
	}
	public InsuranceHaiKouInjury getInsuranceHaiKouInjury() {
		return insuranceHaiKouInjury;
	}
	public void setInsuranceHaiKouInjury(InsuranceHaiKouInjury insuranceHaiKouInjury) {
		this.insuranceHaiKouInjury = insuranceHaiKouInjury;
	}
	public InsuranceHaiKouMaternity getInsuranceHaiKouMaternity() {
		return insuranceHaiKouMaternity;
	}
	public void setInsuranceHaiKouMaternity(InsuranceHaiKouMaternity insuranceHaiKouMaternity) {
		this.insuranceHaiKouMaternity = insuranceHaiKouMaternity;
	}
	public InsuranceHaiKouUnemployment getInsuranceHaiKouUnemployment() {
		return insuranceHaiKouUnemployment;
	}
	public void setInsuranceHaiKouUnemployment(InsuranceHaiKouUnemployment insuranceHaiKouUnemployment) {
		this.insuranceHaiKouUnemployment = insuranceHaiKouUnemployment;
	}

	
	
}
