package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinHtml;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
import com.microservice.dao.entity.crawler.housing.lijiang.HousingFundLiJiangAccount;
import com.microservice.dao.entity.crawler.housing.lijiang.HousingFundLiJiangHtml;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingFundLiJiangHtml housingFundLiJiangHtml;
	public HousingFundLiJiangAccount housingFundLiJiangAccount;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", housingFundLiJiangHtml="
				+ housingFundLiJiangHtml + ", housingFundLiJiangAccount=" + housingFundLiJiangAccount + "]";
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
	public HousingFundLiJiangHtml getHousingFundLiJiangHtml() {
		return housingFundLiJiangHtml;
	}
	public void setHousingFundLiJiangHtml(HousingFundLiJiangHtml housingFundLiJiangHtml) {
		this.housingFundLiJiangHtml = housingFundLiJiangHtml;
	}
	public HousingFundLiJiangAccount getHousingFundLiJiangAccount() {
		return housingFundLiJiangAccount;
	}
	public void setHousingFundLiJiangAccount(HousingFundLiJiangAccount housingFundLiJiangAccount) {
		this.housingFundLiJiangAccount = housingFundLiJiangAccount;
	}
	
	
	
}
