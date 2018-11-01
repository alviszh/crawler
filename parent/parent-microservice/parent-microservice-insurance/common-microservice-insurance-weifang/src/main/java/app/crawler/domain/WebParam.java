package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangMedical;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangPersion;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public InsuranceWeiFangUserInfo  insuranceWeiFangUserInfo;	
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public List<InsuranceWeiFangMedical>  medicalList;
	public List<InsuranceWeiFangPersion>  persionList;
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
	public InsuranceWeiFangUserInfo getInsuranceWeiFangUserInfo() {
		return insuranceWeiFangUserInfo;
	}
	public void setInsuranceWeiFangUserInfo(InsuranceWeiFangUserInfo insuranceWeiFangUserInfo) {
		this.insuranceWeiFangUserInfo = insuranceWeiFangUserInfo;
	}
	public List<InsuranceWeiFangMedical> getMedicalList() {
		return medicalList;
	}
	public void setMedicalList(List<InsuranceWeiFangMedical> medicalList) {
		this.medicalList = medicalList;
	}
	public List<InsuranceWeiFangPersion> getPersionList() {
		return persionList;
	}
	public void setPersionList(List<InsuranceWeiFangPersion> persionList) {
		this.persionList = persionList;
	}
}
