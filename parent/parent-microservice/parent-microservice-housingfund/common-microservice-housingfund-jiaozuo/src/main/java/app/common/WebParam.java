package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoBasic;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoHtml;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoPay;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingJiaoZuoBasic housingJiaoZuoBasic;
	public HousingJiaoZuoPay housingJiaoZuoPay;
	public HousingJiaoZuoHtml housingJiaoZuoHtml;
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
	public HousingJiaoZuoBasic getHousingJiaoZuoBasic() {
		return housingJiaoZuoBasic;
	}
	public void setHousingJiaoZuoBasic(HousingJiaoZuoBasic housingJiaoZuoBasic) {
		this.housingJiaoZuoBasic = housingJiaoZuoBasic;
	}
	public HousingJiaoZuoPay getHousingJiaoZuoPay() {
		return housingJiaoZuoPay;
	}
	public void setHousingJiaoZuoPay(HousingJiaoZuoPay housingJiaoZuoPay) {
		this.housingJiaoZuoPay = housingJiaoZuoPay;
	}
	public HousingJiaoZuoHtml getHousingJiaoZuoHtml() {
		return housingJiaoZuoHtml;
	}
	public void setHousingJiaoZuoHtml(HousingJiaoZuoHtml housingJiaoZuoHtml) {
		this.housingJiaoZuoHtml = housingJiaoZuoHtml;
	}
	
}
