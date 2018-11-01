package app.common;

import java.util.List;

import javax.persistence.Column;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinHtml;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangAccount;
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangUserInfo;
import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaAccount;
import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaUserInfo;
import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianAccount;
import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianUserInfo;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingFundZhuMaDianUserInfo housingFundZhuMaDianUserInfo;
	public HousingFundZhuMaDianAccount housingFundZhuMaDianAccount;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", housingFundZhuMaDianUserInfo="
				+ housingFundZhuMaDianUserInfo + ", housingFundZhuMaDianAccount=" + housingFundZhuMaDianAccount + "]";
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
	public HousingFundZhuMaDianUserInfo getHousingFundZhuMaDianUserInfo() {
		return housingFundZhuMaDianUserInfo;
	}
	public void setHousingFundZhuMaDianUserInfo(HousingFundZhuMaDianUserInfo housingFundZhuMaDianUserInfo) {
		this.housingFundZhuMaDianUserInfo = housingFundZhuMaDianUserInfo;
	}
	public HousingFundZhuMaDianAccount getHousingFundZhuMaDianAccount() {
		return housingFundZhuMaDianAccount;
	}
	public void setHousingFundZhuMaDianAccount(HousingFundZhuMaDianAccount housingFundZhuMaDianAccount) {
		this.housingFundZhuMaDianAccount = housingFundZhuMaDianAccount;
	}
	
	
	
	
	
}
