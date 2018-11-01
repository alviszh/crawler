package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouMedicalInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouUserInfo;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangZhouGeneral;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangzhouHtml;

public class WebParam {

	
	public HtmlPage page;
	public Page page2;
	public Integer code;
	public String url;
	public String html;
	public WebClient webClient;
	public GuangzhouUserInfo guangzhouUserInfo;
	public List<GuangzhouMedicalInsurance> guangzhouMedicalInsurances;
	public List<InsuranceGuangZhouGeneral> insuranceGuangZhouGenerals;
	public List<InsuranceGuangzhouHtml> insuranceGuangzhouHtmls;
	
	public Integer medicalPageCode;
	
	public HtmlPage getPage() {
		return page;
	}
	public void setPage(HtmlPage page) {
		this.page = page;
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
	public GuangzhouUserInfo getGuangzhouUserInfo() {
		return guangzhouUserInfo;
	}
	public void setGuangzhouUserInfo(GuangzhouUserInfo guangzhouUserInfo) {
		this.guangzhouUserInfo = guangzhouUserInfo;
	}
	public List<GuangzhouMedicalInsurance> getGuangzhouMedicalInsurances() {
		return guangzhouMedicalInsurances;
	}
	public void setGuangzhouMedicalInsurances(List<GuangzhouMedicalInsurance> guangzhouMedicalInsurances) {
		this.guangzhouMedicalInsurances = guangzhouMedicalInsurances;
	}
	public List<InsuranceGuangZhouGeneral> getInsuranceGuangZhouGenerals() {
		return insuranceGuangZhouGenerals;
	}
	public void setInsuranceGuangZhouGenerals(List<InsuranceGuangZhouGeneral> insuranceGuangZhouGenerals) {
		this.insuranceGuangZhouGenerals = insuranceGuangZhouGenerals;
	}
	public List<InsuranceGuangzhouHtml> getInsuranceGuangzhouHtmls() {
		return insuranceGuangzhouHtmls;
	}
	public void setInsuranceGuangzhouHtmls(List<InsuranceGuangzhouHtml> insuranceGuangzhouHtmls) {
		this.insuranceGuangzhouHtmls = insuranceGuangzhouHtmls;
	}
	public Integer getMedicalPageCode() {
		return medicalPageCode;
	}
	public void setMedicalPageCode(Integer medicalPageCode) {
		this.medicalPageCode = medicalPageCode;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public Page getPage2() {
		return page2;
	}
	public void setPage2(Page page2) {
		this.page2 = page2;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", page2=" + page2 + ", code=" + code + ", url=" + url + ", html=" + html
				+ ", webClient=" + webClient + ", guangzhouUserInfo=" + guangzhouUserInfo
				+ ", guangzhouMedicalInsurances=" + guangzhouMedicalInsurances + ", insuranceGuangZhouGenerals="
				+ insuranceGuangZhouGenerals + ", insuranceGuangzhouHtmls=" + insuranceGuangzhouHtmls
				+ ", medicalPageCode=" + medicalPageCode + "]";
	}
	
	
}
