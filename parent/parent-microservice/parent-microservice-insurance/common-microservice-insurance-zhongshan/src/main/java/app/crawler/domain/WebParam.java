package app.crawler.domain;


import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduUserInfo;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanHtml;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanMedicalCare;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionDetail;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionAccount;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	
	public InsuranceZhongShanMedicalCare insuranceZhongShanMedicalCare;
	public InsuranceZhongShanPensionDetail insuranceZhongShanPensionDetail;
	public InsuranceZhongShanPensionAccount insuranceZhongShanPensionAccount;
	public InsuranceZhongShanHtml zhongshanHtml;
	
	public String url;
	public String html;
	
	
	
	public InsuranceZhongShanMedicalCare getInsuranceZhongShanMedicalCare() {
		return insuranceZhongShanMedicalCare;
	}
	public void setInsuranceZhongShanMedicalCare(InsuranceZhongShanMedicalCare insuranceZhongShanMedicalCare) {
		this.insuranceZhongShanMedicalCare = insuranceZhongShanMedicalCare;
	}
	public InsuranceZhongShanPensionDetail getInsuranceZhongShanPensionDetail() {
		return insuranceZhongShanPensionDetail;
	}
	public void setInsuranceZhongShanPensionDetail(InsuranceZhongShanPensionDetail insuranceZhongShanPensionDetail) {
		this.insuranceZhongShanPensionDetail = insuranceZhongShanPensionDetail;
	}
	public InsuranceZhongShanPensionAccount getInsuranceZhongShanPensionAccount() {
		return insuranceZhongShanPensionAccount;
	}
	public void setInsuranceZhongShanPensionAccount(InsuranceZhongShanPensionAccount insuranceZhongShanPensionAccount) {
		this.insuranceZhongShanPensionAccount = insuranceZhongShanPensionAccount;
	}
	public InsuranceZhongShanHtml getZhongshanHtml() {
		return zhongshanHtml;
	}
	public void setZhongshanHtml(InsuranceZhongShanHtml zhongshanHtml) {
		this.zhongshanHtml = zhongshanHtml;
	}
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
