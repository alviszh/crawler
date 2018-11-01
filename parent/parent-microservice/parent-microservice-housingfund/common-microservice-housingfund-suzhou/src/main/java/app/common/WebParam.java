package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboHtml;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboPay;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboUserInfo;
import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouAccountBasic;
import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouAccountDetail;
import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouHtml;
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
	
	public HousingSuzhouAccountBasic housingSuzhouAccount;
	public HousingSuzhouAccountDetail housingSuzhouDetail;
	public HousingSuzhouHtml housingSuzhouHtml;
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
	public HousingSuzhouAccountBasic getHousingSuzhouAccount() {
		return housingSuzhouAccount;
	}
	public void setHousingSuzhouAccount(HousingSuzhouAccountBasic housingSuzhouAccount) {
		this.housingSuzhouAccount = housingSuzhouAccount;
	}
	public HousingSuzhouAccountDetail getHousingSuzhouDetail() {
		return housingSuzhouDetail;
	}
	public void setHousingSuzhouDetail(HousingSuzhouAccountDetail housingSuzhouDetail) {
		this.housingSuzhouDetail = housingSuzhouDetail;
	}
	public HousingSuzhouHtml getHousingSuzhouHtml() {
		return housingSuzhouHtml;
	}
	public void setHousingSuzhouHtml(HousingSuzhouHtml housingSuzhouHtml) {
		this.housingSuzhouHtml = housingSuzhouHtml;
	}
	
}
