package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouDetail;
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouHtml;
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouUserInfo;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage1;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	public List<HtmlPage> htmlPage;
	
	public HousingBoZhouUserInfo housingBoZhouUserInfo;
	public HousingBoZhouDetail housingBoZhouDetail;
	public HousingBoZhouHtml  housingBoZhouHtml;
	
	
	public HousingBoZhouUserInfo getHousingBoZhouUserInfo() {
		return housingBoZhouUserInfo;
	}
	public void setHousingBoZhouUserInfo(HousingBoZhouUserInfo housingBoZhouUserInfo) {
		this.housingBoZhouUserInfo = housingBoZhouUserInfo;
	}
	public HousingBoZhouDetail getHousingBoZhouDetail() {
		return housingBoZhouDetail;
	}
	public void setHousingBoZhouDetail(HousingBoZhouDetail housingBoZhouDetail) {
		this.housingBoZhouDetail = housingBoZhouDetail;
	}
	public HousingBoZhouHtml getHousingBoZhouHtml() {
		return housingBoZhouHtml;
	}
	public void setHousingBoZhouHtml(HousingBoZhouHtml housingBoZhouHtml) {
		this.housingBoZhouHtml = housingBoZhouHtml;
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
	public HtmlPage getHtmlPage1() {
		return htmlPage1;
	}
	public void setHtmlPage1(HtmlPage htmlPage1) {
		this.htmlPage1 = htmlPage1;
	}
	public List<HtmlPage> getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(List<HtmlPage> htmlPage) {
		this.htmlPage = htmlPage;
	}
	
	
}
