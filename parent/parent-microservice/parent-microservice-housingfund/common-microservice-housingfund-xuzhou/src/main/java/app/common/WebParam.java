package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboHtml;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboPay;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboUserInfo;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouAccount;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouDetail;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouHtml;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingXuzhouAccount housingXuzhouAccount;
	public HousingXuzhouDetail housingXuzhouDetail;
	public HousingXuzhouHtml housingXuzhouHtml;
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
	public HousingXuzhouAccount getHousingXuzhouAccount() {
		return housingXuzhouAccount;
	}
	public void setHousingXuzhouAccount(HousingXuzhouAccount housingXuzhouAccount) {
		this.housingXuzhouAccount = housingXuzhouAccount;
	}
	public HousingXuzhouDetail getHousingXuzhouDetail() {
		return housingXuzhouDetail;
	}
	public void setHousingXuzhouDetail(HousingXuzhouDetail housingXuzhouDetail) {
		this.housingXuzhouDetail = housingXuzhouDetail;
	}
	public HousingXuzhouHtml getHousingXuzhouHtml() {
		return housingXuzhouHtml;
	}
	public void setHousingXuzhouHtml(HousingXuzhouHtml housingXuzhouHtml) {
		this.housingXuzhouHtml = housingXuzhouHtml;
	}
	
	
}
