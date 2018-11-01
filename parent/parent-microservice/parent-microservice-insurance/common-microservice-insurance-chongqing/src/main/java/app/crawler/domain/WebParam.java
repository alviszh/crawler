package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingBirth;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingCompany;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingFirst;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingInjury;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingLostwork;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingMedical;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingPersion;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public String alertMsg;//存储弹框信息
	public InsuranceChongqingUserInfo  insuranceChongqingUserInfo;	
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public List<InsuranceChongqingFirst>    firstList;
	public List<InsuranceChongqingCompany>  companyList;
	public List<InsuranceChongqingBirth>    birthList;
	public List<InsuranceChongqingInjury>   injuryList;
	public List<InsuranceChongqingLostwork> lostworkList;
	public List<InsuranceChongqingMedical>  medicalList;
	public List<InsuranceChongqingPersion>  persionList;
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
	public String getAlertMsg() {
		return alertMsg;
	}
	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}
	public InsuranceChongqingUserInfo getInsuranceChongqingUserInfo() {
		return insuranceChongqingUserInfo;
	}
	public void setInsuranceChongqingUserInfo(InsuranceChongqingUserInfo insuranceChongqingUserInfo) {
		this.insuranceChongqingUserInfo = insuranceChongqingUserInfo;
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
	public List<InsuranceChongqingFirst> getFirstList() {
		return firstList;
	}
	public void setFirstList(List<InsuranceChongqingFirst> firstList) {
		this.firstList = firstList;
	}
	public List<InsuranceChongqingCompany> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<InsuranceChongqingCompany> companyList) {
		this.companyList = companyList;
	}
	public List<InsuranceChongqingBirth> getBirthList() {
		return birthList;
	}
	public void setBirthList(List<InsuranceChongqingBirth> birthList) {
		this.birthList = birthList;
	}
	public List<InsuranceChongqingInjury> getInjuryList() {
		return injuryList;
	}
	public void setInjuryList(List<InsuranceChongqingInjury> injuryList) {
		this.injuryList = injuryList;
	}
	public List<InsuranceChongqingLostwork> getLostworkList() {
		return lostworkList;
	}
	public void setLostworkList(List<InsuranceChongqingLostwork> lostworkList) {
		this.lostworkList = lostworkList;
	}
	public List<InsuranceChongqingMedical> getMedicalList() {
		return medicalList;
	}
	public void setMedicalList(List<InsuranceChongqingMedical> medicalList) {
		this.medicalList = medicalList;
	}
	public List<InsuranceChongqingPersion> getPersionList() {
		return persionList;
	}
	public void setPersionList(List<InsuranceChongqingPersion> persionList) {
		this.persionList = persionList;
	}
    
}
