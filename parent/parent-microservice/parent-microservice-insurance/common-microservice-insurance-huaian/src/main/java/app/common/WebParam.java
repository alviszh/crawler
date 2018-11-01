package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnEndowment;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnHtml;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnInjury;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnMaternity;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnMedical;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnUnemployment;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public InsuranceHuaiAnUserInfo insuranceHuaiAnUserInfo;
	public InsuranceHuaiAnMedical insuranceHuaiAnMedical;
	public InsuranceHuaiAnHtml insuranceHuaiAnHtml;
	public InsuranceHuaiAnEndowment insuranceHuaiAnEndowment;
	public InsuranceHuaiAnInjury insuranceHuaiAnInjury;
	public InsuranceHuaiAnMaternity insuranceHuaiAnMaternity;
	public InsuranceHuaiAnUnemployment insuranceHuaiAnUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", insuranceHuaiAnUserInfo="
				+ insuranceHuaiAnUserInfo + ", insuranceHuaiAnMedical=" + insuranceHuaiAnMedical
				+ ", insuranceHuaiAnHtml=" + insuranceHuaiAnHtml + ", insuranceHuaiAnEndowment="
				+ insuranceHuaiAnEndowment + ", insuranceHuaiAnInjury=" + insuranceHuaiAnInjury
				+ ", insuranceHuaiAnMaternity=" + insuranceHuaiAnMaternity + ", insuranceHuaiAnUnemployment="
				+ insuranceHuaiAnUnemployment + "]";
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
	public InsuranceHuaiAnUserInfo getInsuranceHuaiAnUserInfo() {
		return insuranceHuaiAnUserInfo;
	}
	public void setInsuranceHuaiAnUserInfo(InsuranceHuaiAnUserInfo insuranceHuaiAnUserInfo) {
		this.insuranceHuaiAnUserInfo = insuranceHuaiAnUserInfo;
	}
	public InsuranceHuaiAnMedical getInsuranceHuaiAnMedical() {
		return insuranceHuaiAnMedical;
	}
	public void setInsuranceHuaiAnMedical(InsuranceHuaiAnMedical insuranceHuaiAnMedical) {
		this.insuranceHuaiAnMedical = insuranceHuaiAnMedical;
	}
	public InsuranceHuaiAnHtml getInsuranceHuaiAnHtml() {
		return insuranceHuaiAnHtml;
	}
	public void setInsuranceHuaiAnHtml(InsuranceHuaiAnHtml insuranceHuaiAnHtml) {
		this.insuranceHuaiAnHtml = insuranceHuaiAnHtml;
	}
	public InsuranceHuaiAnEndowment getInsuranceHuaiAnEndowment() {
		return insuranceHuaiAnEndowment;
	}
	public void setInsuranceHuaiAnEndowment(InsuranceHuaiAnEndowment insuranceHuaiAnEndowment) {
		this.insuranceHuaiAnEndowment = insuranceHuaiAnEndowment;
	}
	public InsuranceHuaiAnInjury getInsuranceHuaiAnInjury() {
		return insuranceHuaiAnInjury;
	}
	public void setInsuranceHuaiAnInjury(InsuranceHuaiAnInjury insuranceHuaiAnInjury) {
		this.insuranceHuaiAnInjury = insuranceHuaiAnInjury;
	}
	public InsuranceHuaiAnMaternity getInsuranceHuaiAnMaternity() {
		return insuranceHuaiAnMaternity;
	}
	public void setInsuranceHuaiAnMaternity(InsuranceHuaiAnMaternity insuranceHuaiAnMaternity) {
		this.insuranceHuaiAnMaternity = insuranceHuaiAnMaternity;
	}
	public InsuranceHuaiAnUnemployment getInsuranceHuaiAnUnemployment() {
		return insuranceHuaiAnUnemployment;
	}
	public void setInsuranceHuaiAnUnemployment(InsuranceHuaiAnUnemployment insuranceHuaiAnUnemployment) {
		this.insuranceHuaiAnUnemployment = insuranceHuaiAnUnemployment;
	}

	
	
}
