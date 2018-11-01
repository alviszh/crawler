package app.bean;

import java.util.List;

import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

public class WebParamTelecom<T> {

	public Page page;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;

	public String errormessage;
	
	public WebClient webClient;
	
	public StatusCodeEnum statusCodeEnum;
	
	public StatusCodeEnum getStatusCodeEnum() {
		return statusCodeEnum;
	}

	public void setStatusCodeEnum(StatusCodeEnum statusCodeEnum) {
		this.statusCodeEnum = statusCodeEnum;
	}

	public StatusCodeRec statusCodeRec;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
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

	public StatusCodeRec getStatusCodeRec() {
		return statusCodeRec;
	}

	public void setStatusCodeRec(StatusCodeRec statusCodeRec) {
		this.statusCodeRec = statusCodeRec;
	}

	@Override
	public String toString() {
		return "WebParamTelecom [page=" + page + ", code=" + code + ", url=" + url + ", html=" + html + ", list=" + list
				+ ", errormessage=" + errormessage + ", webClient=" + webClient + ", statusCodeRec=" + statusCodeRec
				+ "]";
	}
	
}
