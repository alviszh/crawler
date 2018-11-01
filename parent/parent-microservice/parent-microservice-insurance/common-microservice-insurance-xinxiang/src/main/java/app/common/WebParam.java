package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangEndowment;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangHtml;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangInjury;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangMaternity;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangMedical;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangUnemployment;
import com.microservice.dao.entity.crawler.insurance.xinxiang.InsuranceXinXiangUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceXinXiangUserInfo insuranceXinXiangUserInfo;
	public InsuranceXinXiangMedical insuranceXinXiangMedical;
	public InsuranceXinXiangHtml insuranceXinXiangHtml;
	public InsuranceXinXiangEndowment insuranceXinXiangEndowment;
	public InsuranceXinXiangUnemployment insuranceXinXiangUnemployment;
	public InsuranceXinXiangInjury insuranceXinXiangInjury;
	public InsuranceXinXiangMaternity insuranceXinXiangMaternity;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceXinXiangUserInfo=" + insuranceXinXiangUserInfo + ", insuranceXinXiangMedical="
				+ insuranceXinXiangMedical + ", insuranceXinXiangHtml=" + insuranceXinXiangHtml
				+ ", insuranceXinXiangEndowment=" + insuranceXinXiangEndowment + ", insuranceXinXiangUnemployment="
				+ insuranceXinXiangUnemployment + ", insuranceXinXiangInjury=" + insuranceXinXiangInjury
				+ ", insuranceXinXiangMaternity=" + insuranceXinXiangMaternity + "]";
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
	public InsuranceXinXiangUserInfo getInsuranceXinXiangUserInfo() {
		return insuranceXinXiangUserInfo;
	}
	public void setInsuranceXinXiangUserInfo(InsuranceXinXiangUserInfo insuranceXinXiangUserInfo) {
		this.insuranceXinXiangUserInfo = insuranceXinXiangUserInfo;
	}
	public InsuranceXinXiangMedical getInsuranceXinXiangMedical() {
		return insuranceXinXiangMedical;
	}
	public void setInsuranceXinXiangMedical(InsuranceXinXiangMedical insuranceXinXiangMedical) {
		this.insuranceXinXiangMedical = insuranceXinXiangMedical;
	}
	public InsuranceXinXiangHtml getInsuranceXinXiangHtml() {
		return insuranceXinXiangHtml;
	}
	public void setInsuranceXinXiangHtml(InsuranceXinXiangHtml insuranceXinXiangHtml) {
		this.insuranceXinXiangHtml = insuranceXinXiangHtml;
	}
	public InsuranceXinXiangEndowment getInsuranceXinXiangEndowment() {
		return insuranceXinXiangEndowment;
	}
	public void setInsuranceXinXiangEndowment(InsuranceXinXiangEndowment insuranceXinXiangEndowment) {
		this.insuranceXinXiangEndowment = insuranceXinXiangEndowment;
	}
	public InsuranceXinXiangUnemployment getInsuranceXinXiangUnemployment() {
		return insuranceXinXiangUnemployment;
	}
	public void setInsuranceXinXiangUnemployment(InsuranceXinXiangUnemployment insuranceXinXiangUnemployment) {
		this.insuranceXinXiangUnemployment = insuranceXinXiangUnemployment;
	}
	public InsuranceXinXiangInjury getInsuranceXinXiangInjury() {
		return insuranceXinXiangInjury;
	}
	public void setInsuranceXinXiangInjury(InsuranceXinXiangInjury insuranceXinXiangInjury) {
		this.insuranceXinXiangInjury = insuranceXinXiangInjury;
	}
	public InsuranceXinXiangMaternity getInsuranceXinXiangMaternity() {
		return insuranceXinXiangMaternity;
	}
	public void setInsuranceXinXiangMaternity(InsuranceXinXiangMaternity insuranceXinXiangMaternity) {
		this.insuranceXinXiangMaternity = insuranceXinXiangMaternity;
	}
	
	
	
}
