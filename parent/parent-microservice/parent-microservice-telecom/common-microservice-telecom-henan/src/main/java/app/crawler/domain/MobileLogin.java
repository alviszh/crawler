package app.crawler.domain;

import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MobileLogin<T> {

	public String name;
	public String password;
	public Integer usrid;
	public HtmlPage htmlpage;
	public StatusCodeRec statusCodeRec;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUsrid() {
		return usrid;
	}
	public void setUsrid(Integer usrid) {
		this.usrid = usrid;
	}
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
	@Override
	public String toString() {
		return "MobileLogin [name=" + name + ", password=" + password + ", usrid=" + usrid + ", htmlpage=" + htmlpage
				+ ", statusCodeRec=" + statusCodeRec + "]";
	}

}
