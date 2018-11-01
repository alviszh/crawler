package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.xian.HousingXianBasic;
import com.microservice.dao.entity.crawler.housing.xian.HousingXianDetail;
import com.microservice.dao.entity.crawler.housing.xian.HousingXianHtml;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage1;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	public List<HtmlPage> htmlPage;
	
	public HousingXianBasic housingXianBasic;
	public HousingXianDetail housingXianDetail;
	public HousingXianHtml housingXianHtml;
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
	public HtmlPage getHtmlPage1() {
		return htmlPage1;
	}
	public void setHtmlPage1(HtmlPage htmlPage1) {
		this.htmlPage1 = htmlPage1;
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
	public List<HtmlPage> getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(List<HtmlPage> htmlPage) {
		this.htmlPage = htmlPage;
	}
	public HousingXianBasic getHousingXianBasic() {
		return housingXianBasic;
	}
	public void setHousingXianBasic(HousingXianBasic housingXianBasic) {
		this.housingXianBasic = housingXianBasic;
	}
	public HousingXianDetail getHousingXianDetail() {
		return housingXianDetail;
	}
	public void setHousingXianDetail(HousingXianDetail housingXianDetail) {
		this.housingXianDetail = housingXianDetail;
	}
	public HousingXianHtml getHousingXianHtml() {
		return housingXianHtml;
	}
	public void setHousingXianHtml(HousingXianHtml housingXianHtml) {
		this.housingXianHtml = housingXianHtml;
	}
	
}
