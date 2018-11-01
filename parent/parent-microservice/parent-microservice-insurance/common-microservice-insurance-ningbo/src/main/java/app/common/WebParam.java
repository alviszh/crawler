package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboBear;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboEndowment;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboHtml;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboHurt;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboLost;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboMedical;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboUserInfo;

public class WebParam<T> {

	public HtmlPage page;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	public WebClient webClient;
	
	
	public InsuranceNingboBear insuranceNingboBear;
	public InsuranceNingboHurt insuranceNingboHurt;
	public InsuranceNingboLost insuranceNingboLost;
	public InsuranceNingboMedical insuranceNingboMedical;
	public InsuranceNingboEndowment insuranceNingboEndowment;
	public InsuranceNingboUserInfo insuranceNingboUserInfo;
	public InsuranceNingboHtml insuranceNingboHtml;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", code=" + code + ", url=" + url + ", html=" + html + ", list=" + list
				+ ", webClient=" + webClient + ", insuranceNingboBear=" + insuranceNingboBear + ", insuranceNingboHurt="
				+ insuranceNingboHurt + ", insuranceNingboLost=" + insuranceNingboLost + ", insuranceNingboMedical="
				+ insuranceNingboMedical + ", insuranceNingboEndowment=" + insuranceNingboEndowment
				+ ", insuranceNingboUserInfo=" + insuranceNingboUserInfo + ", insuranceNingboHtml="
				+ insuranceNingboHtml + "]";
	}
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
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public InsuranceNingboBear getInsuranceNingboBear() {
		return insuranceNingboBear;
	}
	public void setInsuranceNingboBear(InsuranceNingboBear insuranceNingboBear) {
		this.insuranceNingboBear = insuranceNingboBear;
	}
	public InsuranceNingboHurt getInsuranceNingboHurt() {
		return insuranceNingboHurt;
	}
	public void setInsuranceNingboHurt(InsuranceNingboHurt insuranceNingboHurt) {
		this.insuranceNingboHurt = insuranceNingboHurt;
	}
	public InsuranceNingboLost getInsuranceNingboLost() {
		return insuranceNingboLost;
	}
	public void setInsuranceNingboLost(InsuranceNingboLost insuranceNingboLost) {
		this.insuranceNingboLost = insuranceNingboLost;
	}
	public InsuranceNingboMedical getInsuranceNingboMedical() {
		return insuranceNingboMedical;
	}
	public void setInsuranceNingboMedical(InsuranceNingboMedical insuranceNingboMedical) {
		this.insuranceNingboMedical = insuranceNingboMedical;
	}
	public InsuranceNingboEndowment getInsuranceNingboEndowment() {
		return insuranceNingboEndowment;
	}
	public void setInsuranceNingboEndowment(InsuranceNingboEndowment insuranceNingboEndowment) {
		this.insuranceNingboEndowment = insuranceNingboEndowment;
	}
	public InsuranceNingboUserInfo getInsuranceNingboUserInfo() {
		return insuranceNingboUserInfo;
	}
	public void setInsuranceNingboUserInfo(InsuranceNingboUserInfo insuranceNingboUserInfo) {
		this.insuranceNingboUserInfo = insuranceNingboUserInfo;
	}
	public InsuranceNingboHtml getInsuranceNingboHtml() {
		return insuranceNingboHtml;
	}
	public void setInsuranceNingboHtml(InsuranceNingboHtml insuranceNingboHtml) {
		this.insuranceNingboHtml = insuranceNingboHtml;
	}
	
	
}
