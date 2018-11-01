package app.common;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Account;
import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Basic;
import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Html;


public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	public WebDriver driver;
	
	public WebDriver getDriver() {
		return driver;
	}
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	public HousingFuzhou2Account fuzhou2Account;
	public HousingFuzhou2Basic fuzhou2Basic;
	public HousingFuzhou2Html fuzhou2Html;
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
	public HousingFuzhou2Account getFuzhou2Account() {
		return fuzhou2Account;
	}
	public void setFuzhou2Account(HousingFuzhou2Account fuzhou2Account) {
		this.fuzhou2Account = fuzhou2Account;
	}
	public HousingFuzhou2Basic getFuzhou2Basic() {
		return fuzhou2Basic;
	}
	public void setFuzhou2Basic(HousingFuzhou2Basic fuzhou2Basic) {
		this.fuzhou2Basic = fuzhou2Basic;
	}
	public HousingFuzhou2Html getFuzhou2Html() {
		return fuzhou2Html;
	}
	public void setFuzhou2Html(HousingFuzhou2Html fuzhou2Html) {
		this.fuzhou2Html = fuzhou2Html;
	}
	
	
}
