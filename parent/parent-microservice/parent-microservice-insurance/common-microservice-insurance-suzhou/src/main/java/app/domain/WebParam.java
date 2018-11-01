package app.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouBirth;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouEnterprisePension;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouTownsWorkers;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouUser;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouWorkInjury;


public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	public List<T> list;
	public List<HtmlPage> htmlPage;
	public InsuranceSuzhouBirth insuranceSuzhouBirth;
	public InsuranceSuzhouEnterprisePension   insuranceSuzhouEnterprisePension ;
	public InsuranceSuzhouTownsWorkers  insuranceSuzhouTownsWorkers;
	public InsuranceSuzhouUnemployment insuranceSuzhouUnemployment;
	public InsuranceSuzhouWorkInjury   insuranceSuzhouWorkInjury;
	public InsuranceSuzhouUser  insuranceSuzhouUser;
	public Page page2;
	
	
	public Page getPage2() {
		return page2;
	}
	public void setPage2(Page page2) {
		this.page2 = page2;
	}
	public InsuranceSuzhouUser getInsuranceSuzhouUser() {
		return insuranceSuzhouUser;
	}
	public void setInsuranceSuzhouUser(InsuranceSuzhouUser insuranceSuzhouUser) {
		this.insuranceSuzhouUser = insuranceSuzhouUser;
	}
	public InsuranceSuzhouBirth getInsuranceSuzhouBirth() {
		return insuranceSuzhouBirth;
	}
	public void setInsuranceSuzhouBirth(InsuranceSuzhouBirth insuranceSuzhouBirth) {
		this.insuranceSuzhouBirth = insuranceSuzhouBirth;
	}
	public InsuranceSuzhouEnterprisePension getInsuranceSuzhouEnterprisePension() {
		return insuranceSuzhouEnterprisePension;
	}
	public void setInsuranceSuzhouEnterprisePension(InsuranceSuzhouEnterprisePension insuranceSuzhouEnterprisePension) {
		this.insuranceSuzhouEnterprisePension = insuranceSuzhouEnterprisePension;
	}
	public InsuranceSuzhouTownsWorkers getInsuranceSuzhouTownsWorkers() {
		return insuranceSuzhouTownsWorkers;
	}
	public void setInsuranceSuzhouTownsWorkers(InsuranceSuzhouTownsWorkers insuranceSuzhouTownsWorkers) {
		this.insuranceSuzhouTownsWorkers = insuranceSuzhouTownsWorkers;
	}
	public InsuranceSuzhouUnemployment getInsuranceSuzhouUnemployment() {
		return insuranceSuzhouUnemployment;
	}
	public void setInsuranceSuzhouUnemployment(InsuranceSuzhouUnemployment insuranceSuzhouUnemployment) {
		this.insuranceSuzhouUnemployment = insuranceSuzhouUnemployment;
	}
	public InsuranceSuzhouWorkInjury getInsuranceSuzhouWorkInjury() {
		return insuranceSuzhouWorkInjury;
	}
	public void setInsuranceSuzhouWorkInjury(InsuranceSuzhouWorkInjury insuranceSuzhouWorkInjury) {
		this.insuranceSuzhouWorkInjury = insuranceSuzhouWorkInjury;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public String url;
	public HtmlPage getPage() {
		return page;
	}
	public void setPage(HtmlPage page) {
		this.page = page;
	}
	public List<HtmlPage> getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(List<HtmlPage> htmlPage) {
		this.htmlPage = htmlPage;
	}
	public String html;
	
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
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
}
