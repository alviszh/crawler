package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanEndowment;
import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanHtml;
import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanUserInfo;

public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public InsurancePingDingShanUserInfo insurancePingDingShanUserInfo;
	public InsurancePingDingShanHtml insurancePingDingShanHtml;
	public InsurancePingDingShanEndowment insurancePingDingShanEndowment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", insurancePingDingShanUserInfo="
				+ insurancePingDingShanUserInfo + ", insurancePingDingShanHtml=" + insurancePingDingShanHtml
				+ ", insurancePingDingShanEndowment=" + insurancePingDingShanEndowment + "]";
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
	public InsurancePingDingShanUserInfo getInsurancePingDingShanUserInfo() {
		return insurancePingDingShanUserInfo;
	}
	public void setInsurancePingDingShanUserInfo(InsurancePingDingShanUserInfo insurancePingDingShanUserInfo) {
		this.insurancePingDingShanUserInfo = insurancePingDingShanUserInfo;
	}
	public InsurancePingDingShanHtml getInsurancePingDingShanHtml() {
		return insurancePingDingShanHtml;
	}
	public void setInsurancePingDingShanHtml(InsurancePingDingShanHtml insurancePingDingShanHtml) {
		this.insurancePingDingShanHtml = insurancePingDingShanHtml;
	}
	public InsurancePingDingShanEndowment getInsurancePingDingShanEndowment() {
		return insurancePingDingShanEndowment;
	}
	public void setInsurancePingDingShanEndowment(InsurancePingDingShanEndowment insurancePingDingShanEndowment) {
		this.insurancePingDingShanEndowment = insurancePingDingShanEndowment;
	}
	
	
	
}
