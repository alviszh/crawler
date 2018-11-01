package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.changchun.InsuranceChangchunAccountInfo;
import com.microservice.dao.entity.crawler.insurance.changchun.InsuranceChangchunUserInfo;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	public InsuranceChangchunUserInfo InsuranceChangchunUserInfo;
	public InsuranceChangchunAccountInfo insuranceChangchunAccountInfo;
	public List<T> list;
	public List<HtmlPage> htmlPage;
	
	public List<HtmlPage> getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(List<HtmlPage> htmlPage) {
		this.htmlPage = htmlPage;
	}
	public InsuranceChangchunAccountInfo getInsuranceChangchunAccountInfo() {
		return insuranceChangchunAccountInfo;
	}
	public void setInsuranceChangchunAccountInfo(InsuranceChangchunAccountInfo insuranceChangchunAccountInfo) {
		this.insuranceChangchunAccountInfo = insuranceChangchunAccountInfo;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public String url;
	public String html;
	
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
	public InsuranceChangchunUserInfo getInsuranceChangchunUserInfo() {
		return InsuranceChangchunUserInfo;
	}
	public void setInsuranceChangchunUserInfo(InsuranceChangchunUserInfo insuranceChangchunUserInfo) {
		InsuranceChangchunUserInfo = insuranceChangchunUserInfo;
	}
	
	
}
