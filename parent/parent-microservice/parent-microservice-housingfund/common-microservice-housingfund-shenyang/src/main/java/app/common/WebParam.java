package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangHtml;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangPay;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangUserInfo;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingShenYangPay housingShenYangPay;
	public HousingShenYangHtml housingShenYangHtml;
	public HousingShenYangUserInfo housingShenYangUserInfo;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", housingShenYangPay=" + housingShenYangPay
				+ ", housingShenYangHtml=" + housingShenYangHtml + ", housingShenYangUserInfo="
				+ housingShenYangUserInfo + "]";
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
	public HousingShenYangPay getHousingShenYangPay() {
		return housingShenYangPay;
	}
	public void setHousingShenYangPay(HousingShenYangPay housingShenYangPay) {
		this.housingShenYangPay = housingShenYangPay;
	}
	public HousingShenYangHtml getHousingShenYangHtml() {
		return housingShenYangHtml;
	}
	public void setHousingShenYangHtml(HousingShenYangHtml housingShenYangHtml) {
		this.housingShenYangHtml = housingShenYangHtml;
	}
	public HousingShenYangUserInfo getHousingShenYangUserInfo() {
		return housingShenYangUserInfo;
	}
	public void setHousingShenYangUserInfo(HousingShenYangUserInfo housingShenYangUserInfo) {
		this.housingShenYangUserInfo = housingShenYangUserInfo;
	}
	
	
	
}
