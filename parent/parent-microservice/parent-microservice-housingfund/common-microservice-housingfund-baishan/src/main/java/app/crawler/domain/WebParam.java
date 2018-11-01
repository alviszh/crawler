package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanPaydetails;
import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanUserInfo;

public class WebParam{
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public String cookies;	
	public String text;
	public HousingBaishanUserInfo userInfo;
	public List<HousingBaishanPaydetails> paydetails;
	private InfoParam infoParam;
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
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
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public InfoParam getInfoParam() {
		return infoParam;
	}
	public void setInfoParam(InfoParam infoParam) {
		this.infoParam = infoParam;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public HousingBaishanUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(HousingBaishanUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public List<HousingBaishanPaydetails> getPaydetails() {
		return paydetails;
	}
	public void setPaydetails(List<HousingBaishanPaydetails> paydetails) {
		this.paydetails = paydetails;
	}
	
}
