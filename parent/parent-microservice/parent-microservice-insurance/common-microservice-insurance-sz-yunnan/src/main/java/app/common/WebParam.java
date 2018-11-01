package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanHtml;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanMedical;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;

	public InsuranceSZYunNanUserInfo insuranceSZYunNanUserInfo;
	public InsuranceSZYunNanMedical insuranceSZYunNanMedical;
	public InsuranceSZYunNanHtml insuranceSZYunNanHtml;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", insuranceSZYunNanUserInfo=" + insuranceSZYunNanUserInfo + ", insuranceSZYunNanMedical="
				+ insuranceSZYunNanMedical + ", insuranceSZYunNanHtml=" + insuranceSZYunNanHtml + "]";
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
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
	public String getWebHandle() {
		return webHandle;
	}
	public void setWebHandle(String webHandle) {
		this.webHandle = webHandle;
	}
	public InsuranceSZYunNanUserInfo getInsuranceSZYunNanUserInfo() {
		return insuranceSZYunNanUserInfo;
	}
	public void setInsuranceSZYunNanUserInfo(InsuranceSZYunNanUserInfo insuranceSZYunNanUserInfo) {
		this.insuranceSZYunNanUserInfo = insuranceSZYunNanUserInfo;
	}
	public InsuranceSZYunNanMedical getInsuranceSZYunNanMedical() {
		return insuranceSZYunNanMedical;
	}
	public void setInsuranceSZYunNanMedical(InsuranceSZYunNanMedical insuranceSZYunNanMedical) {
		this.insuranceSZYunNanMedical = insuranceSZYunNanMedical;
	}
	public InsuranceSZYunNanHtml getInsuranceSZYunNanHtml() {
		return insuranceSZYunNanHtml;
	}
	public void setInsuranceSZYunNanHtml(InsuranceSZYunNanHtml insuranceSZYunNanHtml) {
		this.insuranceSZYunNanHtml = insuranceSZYunNanHtml;
	}
	
}
