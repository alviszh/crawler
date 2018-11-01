package app.domain;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.List;

public class WebParam<T> {

	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;

	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	@Override
	public String toString() {
		return "WebParam [page=" + htmlPage + ", code=" + code + ", url=" + url + ", html=" + html + ", list=" + list + "]";
	}
}

