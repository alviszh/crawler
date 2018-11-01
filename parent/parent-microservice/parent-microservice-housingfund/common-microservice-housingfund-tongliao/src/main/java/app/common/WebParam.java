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
import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoAccount;
import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoHtml;
import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoUserInfo;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingFundTongLiaoUserInfo housingFundTongLiaoUserInfo;
	public HousingFundTongLiaoAccount housingFundTongLiaoAccount;
	public HousingFundTongLiaoHtml housingFundTongLiaoHtml;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", housingFundTongLiaoUserInfo="
				+ housingFundTongLiaoUserInfo + ", housingFundTongLiaoAccount=" + housingFundTongLiaoAccount
				+ ", housingFundTongLiaoHtml=" + housingFundTongLiaoHtml + "]";
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
	public HousingFundTongLiaoUserInfo getHousingFundTongLiaoUserInfo() {
		return housingFundTongLiaoUserInfo;
	}
	public void setHousingFundTongLiaoUserInfo(HousingFundTongLiaoUserInfo housingFundTongLiaoUserInfo) {
		this.housingFundTongLiaoUserInfo = housingFundTongLiaoUserInfo;
	}
	public HousingFundTongLiaoAccount getHousingFundTongLiaoAccount() {
		return housingFundTongLiaoAccount;
	}
	public void setHousingFundTongLiaoAccount(HousingFundTongLiaoAccount housingFundTongLiaoAccount) {
		this.housingFundTongLiaoAccount = housingFundTongLiaoAccount;
	}
	public HousingFundTongLiaoHtml getHousingFundTongLiaoHtml() {
		return housingFundTongLiaoHtml;
	}
	public void setHousingFundTongLiaoHtml(HousingFundTongLiaoHtml housingFundTongLiaoHtml) {
		this.housingFundTongLiaoHtml = housingFundTongLiaoHtml;
	}
	
	
	
}
