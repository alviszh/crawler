package app.common;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaCreditCardTransFlow;

import java.util.List;

public class WebParam<T> {
	
	public Page page;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	public List<IcbcChinaCreditCardTransFlow> creditCardTransFlows;
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
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
	public List<IcbcChinaCreditCardTransFlow> getCreditCardTransFlows() {
		return creditCardTransFlows;
	}
	public void setCreditCardTransFlows(List<IcbcChinaCreditCardTransFlow> creditCardTransFlows) {
		this.creditCardTransFlows = creditCardTransFlows;
	}
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", htmlPage=" + htmlPage + ", code=" + code + ", url=" + url + ", html="
				+ html + ", list=" + list + ", creditCardTransFlows=" + creditCardTransFlows + "]";
	}
	
	
	
}
