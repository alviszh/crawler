package app.common;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebParam {
	public HtmlPage htmlPage1;
	
	public HtmlPage getHtmlPage1() {
		return htmlPage1;
	}
	public void setHtmlPage1(HtmlPage htmlPage1) {
		this.htmlPage1 = htmlPage1;
	}
	
	@Override
	public String toString() {
		return "WebParam [htmlPage1=" + htmlPage1+ "]";
	}
}
