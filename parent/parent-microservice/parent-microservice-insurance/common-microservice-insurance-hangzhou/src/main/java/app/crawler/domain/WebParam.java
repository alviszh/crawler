package app.crawler.domain;


import java.util.List;



import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouUserInfo;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	
	public InsuranceHangzhouUserInfo InsuranceHangzhouUserInfo;
	
	public String url;
	public String html;
	public String ErrorMsg;
	/**
	 * 医疗保险账户信息:账户余额
	 */
	private String medicalBalance;
	
	public List<T> list;
	
	public String getErrorMsg() {
		return ErrorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}
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
	public InsuranceHangzhouUserInfo getInsuranceHangzhouUserInfo() {
		return InsuranceHangzhouUserInfo;
	}
	public void setInsuranceHangzhouUserInfo(
			InsuranceHangzhouUserInfo insuranceHangzhouUserInfo) {
		InsuranceHangzhouUserInfo = insuranceHangzhouUserInfo;
	}
	
}
