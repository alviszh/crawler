package app.crawler.domain;


import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduUserInfo;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	
	public InsuranceChengduUserInfo InsuranceChengduUserInfo;
	
	public String url;
	public String html;
	
	/**
	 * 医疗保险账户信息:账户余额
	 */
	private String medicalBalance;
	
	public List<T> list;
	
	public String getMedicalBalance() {
		return medicalBalance;
	}
	public void setMedicalBalance(String medicalBalance) {
		this.medicalBalance = medicalBalance;
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
	public InsuranceChengduUserInfo getInsuranceChengduUserInfo() {
		return InsuranceChengduUserInfo;
	}
	public void setInsuranceChengduUserInfo(InsuranceChengduUserInfo insuranceChengduUserInfo) {
		InsuranceChengduUserInfo = insuranceChengduUserInfo;
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
	
}
