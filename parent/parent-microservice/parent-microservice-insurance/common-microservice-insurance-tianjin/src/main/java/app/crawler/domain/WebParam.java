package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingHtml;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.BasicUserShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamAgedShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamGeneralShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamLostWorkShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamMedicalShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinHtml;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinInjury;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMaternity;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMedical;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinPension;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUnemployment;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUserInfo;

public class WebParam {

	public HtmlPage page;    
	public Integer code;
	public String url;
	public Page page1;
	
	
	public Page getPage1() {
		return page1;
	}
	public void setPage1(Page page1) {
		this.page1 = page1;
	}
	public InsuranceTianjinUserInfo insuranceTianjinUserInfo;
	public List<InsuranceTianjinInjury> insuranceTianjinInjury;
	public List<InsuranceTianjinMaternity> insuranceTianjinMaternity;
	public List<InsuranceTianjinMedical> insuranceTianjinMedical;
	public List<InsuranceTianjinPension> insuranceTianjinPension;
	public List<InsuranceTianjinUnemployment> insuranceTianjinUnemployment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", code=" + code + ", url=" + url + ", insuranceTianjinUserInfo="
				+ insuranceTianjinUserInfo + ", insuranceTianjinInjury=" + insuranceTianjinInjury
				+ ", insuranceTianjinMaternity=" + insuranceTianjinMaternity + ", insuranceTianjinMedical="
				+ insuranceTianjinMedical + ", insuranceTianjinPension=" + insuranceTianjinPension
				+ ", insuranceTianjinUnemployment=" + insuranceTianjinUnemployment + "]";
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
	public InsuranceTianjinUserInfo getInsuranceTianjinUserInfo() {
		return insuranceTianjinUserInfo;
	}
	public void setInsuranceTianjinUserInfo(InsuranceTianjinUserInfo insuranceTianjinUserInfo) {
		this.insuranceTianjinUserInfo = insuranceTianjinUserInfo;
	}
	public List<InsuranceTianjinInjury> getInsuranceTianjinInjury() {
		return insuranceTianjinInjury;
	}
	public void setInsuranceTianjinInjury(List<InsuranceTianjinInjury> insuranceTianjinInjury) {
		this.insuranceTianjinInjury = insuranceTianjinInjury;
	}
	public List<InsuranceTianjinMaternity> getInsuranceTianjinMaternity() {
		return insuranceTianjinMaternity;
	}
	public void setInsuranceTianjinMaternity(List<InsuranceTianjinMaternity> insuranceTianjinMaternity) {
		this.insuranceTianjinMaternity = insuranceTianjinMaternity;
	}
	public List<InsuranceTianjinMedical> getInsuranceTianjinMedical() {
		return insuranceTianjinMedical;
	}
	public void setInsuranceTianjinMedical(List<InsuranceTianjinMedical> insuranceTianjinMedical) {
		this.insuranceTianjinMedical = insuranceTianjinMedical;
	}
	public List<InsuranceTianjinPension> getInsuranceTianjinPension() {
		return insuranceTianjinPension;
	}
	public void setInsuranceTianjinPension(List<InsuranceTianjinPension> insuranceTianjinPension) {
		this.insuranceTianjinPension = insuranceTianjinPension;
	}
	public List<InsuranceTianjinUnemployment> getInsuranceTianjinUnemployment() {
		return insuranceTianjinUnemployment;
	}
	public void setInsuranceTianjinUnemployment(List<InsuranceTianjinUnemployment> insuranceTianjinUnemployment) {
		this.insuranceTianjinUnemployment = insuranceTianjinUnemployment;
	}
	
	
	
	
}
