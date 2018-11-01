package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.qinzhou.HousingFundQinZhouAccount;
import com.microservice.dao.entity.crawler.housing.qinzhou.HousingFundQinZhouHtml;
import com.microservice.dao.entity.crawler.housing.qinzhou.HousingFundQinZhouUserInfo;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingFundQinZhouUserInfo housingFundQinZhouUserInfo;
	public HousingFundQinZhouAccount housingFundQinZhouAccount;
	public HousingFundQinZhouHtml housingFundQinZhouHtml;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", housingFundQinZhouUserInfo="
				+ housingFundQinZhouUserInfo + ", housingFundQinZhouAccount=" + housingFundQinZhouAccount
				+ ", housingFundQinZhouHtml=" + housingFundQinZhouHtml + "]";
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
	public HousingFundQinZhouUserInfo getHousingFundQinZhouUserInfo() {
		return housingFundQinZhouUserInfo;
	}
	public void setHousingFundQinZhouUserInfo(HousingFundQinZhouUserInfo housingFundQinZhouUserInfo) {
		this.housingFundQinZhouUserInfo = housingFundQinZhouUserInfo;
	}
	public HousingFundQinZhouAccount getHousingFundQinZhouAccount() {
		return housingFundQinZhouAccount;
	}
	public void setHousingFundQinZhouAccount(HousingFundQinZhouAccount housingFundQinZhouAccount) {
		this.housingFundQinZhouAccount = housingFundQinZhouAccount;
	}
	public HousingFundQinZhouHtml getHousingFundQinZhouHtml() {
		return housingFundQinZhouHtml;
	}
	public void setHousingFundQinZhouHtml(HousingFundQinZhouHtml housingFundQinZhouHtml) {
		this.housingFundQinZhouHtml = housingFundQinZhouHtml;
	}
	
	
}
