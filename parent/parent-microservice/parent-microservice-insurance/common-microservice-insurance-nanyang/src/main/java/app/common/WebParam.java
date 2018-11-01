package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangEndowment;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangHtml;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangUserInfo;
  
public class WebParam<T> {
	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public InsuranceNanYangUserInfo insuranceNanYangUserInfo;
	public InsuranceNanYangHtml insuranceNanYangHtml;
	public InsuranceNanYangEndowment insuranceNanYangEndowment;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", insuranceNanYangUserInfo="
				+ insuranceNanYangUserInfo + ", insuranceNanYangHtml=" + insuranceNanYangHtml
				+ ", insuranceNanYangEndowment=" + insuranceNanYangEndowment + "]";
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
	public InsuranceNanYangUserInfo getInsuranceNanYangUserInfo() {
		return insuranceNanYangUserInfo;
	}
	public void setInsuranceNanYangUserInfo(InsuranceNanYangUserInfo insuranceNanYangUserInfo) {
		this.insuranceNanYangUserInfo = insuranceNanYangUserInfo;
	}
	public InsuranceNanYangHtml getInsuranceNanYangHtml() {
		return insuranceNanYangHtml;
	}
	public void setInsuranceNanYangHtml(InsuranceNanYangHtml insuranceNanYangHtml) {
		this.insuranceNanYangHtml = insuranceNanYangHtml;
	}
	public InsuranceNanYangEndowment getInsuranceNanYangEndowment() {
		return insuranceNanYangEndowment;
	}
	public void setInsuranceNanYangEndowment(InsuranceNanYangEndowment insuranceNanYangEndowment) {
		this.insuranceNanYangEndowment = insuranceNanYangEndowment;
	}
	
	
	
	
}
