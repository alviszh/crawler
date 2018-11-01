package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoBasic;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoHtml;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoPay;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuBasic;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuDetail;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuHtml;

public class WebParam<T> {

	public Page page;
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public HousingShangQiuBasic housingShangQiuBasic;
	public HousingShangQiuDetail housingShangQiuDetail;
	public HousingShangQiuHtml housingShangQiuHtml;
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
	public HousingShangQiuBasic getHousingShangQiuBasic() {
		return housingShangQiuBasic;
	}
	public void setHousingShangQiuBasic(HousingShangQiuBasic housingShangQiuBasic) {
		this.housingShangQiuBasic = housingShangQiuBasic;
	}
	public HousingShangQiuDetail getHousingShangQiuDetail() {
		return housingShangQiuDetail;
	}
	public void setHousingShangQiuDetail(HousingShangQiuDetail housingShangQiuDetail) {
		this.housingShangQiuDetail = housingShangQiuDetail;
	}
	public HousingShangQiuHtml getHousingShangQiuHtml() {
		return housingShangQiuHtml;
	}
	public void setHousingShangQiuHtml(HousingShangQiuHtml housingShangQiuHtml) {
		this.housingShangQiuHtml = housingShangQiuHtml;
	}
	
}
