package app.domain;

import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
/**
 * 中间bean，用于承接返回值，
 * @author sln
 *
 */
public class TempBean {
	private HtmlPage htmlpage;	
	private StatusCodeRec statusCodeRec;
	private String errormessage;
	private WebClient webClient;
	private String html;
	private Page page;
	public HtmlPage getHtmlpage() {
		return htmlpage;
	}
	public void setHtmlpage(HtmlPage htmlpage) {
		this.htmlpage = htmlpage;
	}
	public StatusCodeRec getStatusCodeRec() {
		return statusCodeRec;
	}
	public void setStatusCodeRec(StatusCodeRec statusCodeRec) {
		this.statusCodeRec = statusCodeRec;
	}
	public String getErrormessage() {
		return errormessage;
	}
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
}
