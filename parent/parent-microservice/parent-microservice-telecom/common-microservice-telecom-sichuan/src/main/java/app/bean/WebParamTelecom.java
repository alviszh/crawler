package app.bean;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebParamTelecom<T> {

	public HtmlPage page;
	
	public Integer code;
	
	public String url;
	
	public String html;
	
	public List<T> list;
	
	public WebClient webClient;

	public HtmlPage getPage() {
		return page;
	}

	public void setPage(HtmlPage page) {
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

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public String toString() {
		return "WebParamTelecom [page=" + page + ", code=" + code + ", url=" + url + ", html=" + html + ", list=" + list
				+ ", webClient=" + webClient + "]";
	}
	
	
}
