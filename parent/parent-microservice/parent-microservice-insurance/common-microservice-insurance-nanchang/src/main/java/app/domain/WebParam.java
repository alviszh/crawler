package app.domain;


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangHtml;


import java.util.List;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	

	public String url;
	public String html;

	public List<InsuranceNanchangHtml> listhtml;



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

	public List<InsuranceNanchangHtml> getListhtml() {
		return listhtml;
	}

	public void setListhtml(List<InsuranceNanchangHtml> listhtml) {
		this.listhtml = listhtml;
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
