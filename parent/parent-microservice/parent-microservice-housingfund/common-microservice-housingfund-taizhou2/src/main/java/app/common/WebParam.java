package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouAccount;
import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouUserInfo;
import com.microservice.dao.entity.crawler.housing.taizhou2.HousingFundTaiZhou2Account;
import com.microservice.dao.entity.crawler.housing.taizhou2.HousingFundTaiZhou2Html;
import com.microservice.dao.entity.crawler.housing.taizhou2.HousingFundTaiZhou2UserInfo;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingFundTaiZhou2UserInfo housingFundTaiZhou2UserInfo;
	public HousingFundTaiZhou2Account housingFundTaiZhou2Account;
	public HousingFundTaiZhou2Html housingFundTaiZhou2UserHtml;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", housingFundTaiZhou2UserInfo="
				+ housingFundTaiZhou2UserInfo + ", housingFundTaiZhou2Account=" + housingFundTaiZhou2Account
				+ ", housingFundTaiZhou2UserHtml=" + housingFundTaiZhou2UserHtml + "]";
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
	public HousingFundTaiZhou2UserInfo getHousingFundTaiZhou2UserInfo() {
		return housingFundTaiZhou2UserInfo;
	}
	public void setHousingFundTaiZhou2UserInfo(HousingFundTaiZhou2UserInfo housingFundTaiZhou2UserInfo) {
		this.housingFundTaiZhou2UserInfo = housingFundTaiZhou2UserInfo;
	}
	public HousingFundTaiZhou2Account getHousingFundTaiZhou2Account() {
		return housingFundTaiZhou2Account;
	}
	public void setHousingFundTaiZhou2Account(HousingFundTaiZhou2Account housingFundTaiZhou2Account) {
		this.housingFundTaiZhou2Account = housingFundTaiZhou2Account;
	}
	public HousingFundTaiZhou2Html getHousingFundTaiZhou2UserHtml() {
		return housingFundTaiZhou2UserHtml;
	}
	public void setHousingFundTaiZhou2UserHtml(HousingFundTaiZhou2Html housingFundTaiZhou2UserHtml) {
		this.housingFundTaiZhou2UserHtml = housingFundTaiZhou2UserHtml;
	}
	
	
	
	
}
