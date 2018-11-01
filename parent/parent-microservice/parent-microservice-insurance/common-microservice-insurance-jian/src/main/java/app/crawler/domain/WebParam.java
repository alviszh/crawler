package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnMedical;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnPersion;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public InsuranceJiAnUserInfo  insuranceJiAnUserInfo;	
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public List<InsuranceJiAnMedical>  medicalList;
	public List<InsuranceJiAnPersion>  persionList;
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
	
	public InsuranceJiAnUserInfo getInsuranceJiAnUserInfo() {
		return insuranceJiAnUserInfo;
	}
	public void setInsuranceJiAnUserInfo(InsuranceJiAnUserInfo insuranceJiAnUserInfo) {
		this.insuranceJiAnUserInfo = insuranceJiAnUserInfo;
	}
	public List<InsuranceJiAnMedical> getMedicalList() {
		return medicalList;
	}
	public void setMedicalList(List<InsuranceJiAnMedical> medicalList) {
		this.medicalList = medicalList;
	}
	public List<InsuranceJiAnPersion> getPersionList() {
		return persionList;
	}
	public void setPersionList(List<InsuranceJiAnPersion> persionList) {
		this.persionList = persionList;
	}
}
