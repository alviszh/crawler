package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	public String url;
	public String html;
	public String usersessionuuid;
	public String laneid;
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
	public String getUsersessionuuid() {
		return usersessionuuid;
	}
	public void setUsersessionuuid(String usersessionuuid) {
		this.usersessionuuid = usersessionuuid;
	}
	public String getLaneid() {
		return laneid;
	}
	public void setLaneid(String laneid) {
		this.laneid = laneid;
	}
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", code=" + code + ", url=" + url + ", html=" + html + ", usersessionuuid="
				+ usersessionuuid + ", laneid=" + laneid + ", list=" + list + "]";
	}
	

}
