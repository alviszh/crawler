package app.bean;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

public class WebParamE<T> {

	public Page page;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public List<String> htmllist;

	public String errormessage;
	
	public WebClient webClient;
	
	public Integer StatusCode;
	
	public String pagnum;
	
	public WebDriver driver;
	
    public String base64img;
    
    public String indentids;
    
    
		
	public String getIndentids() {
		return indentids;
	}

	public void setIndentids(String indentids) {
		this.indentids = indentids;
	}

	public String getBase64img() {
		return base64img;
	}

	public void setBase64img(String base64img) {
		this.base64img = base64img;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

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

	public Integer getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(Integer statusCode) {
		StatusCode = statusCode;
	}

	
	public String getPagnum() {
		return pagnum;
	}

	public void setPagnum(String pagnum) {
		this.pagnum = pagnum;
	}

	
	public List<String> getHtmllist() {
		return htmllist;
	}

	public void setHtmllist(List<String> htmllist) {
		this.htmllist = htmllist;
	}

	@Override
	public String toString() {
		return "WebParamE [page=" + page + ", code=" + code + ", url=" + url + ", html=" + html + ", list=" + list
				+ ", htmllist=" + htmllist + ", errormessage=" + errormessage + ", webClient=" + webClient
				+ ", StatusCode=" + StatusCode + ", pagnum=" + pagnum + ", driver=" + driver + ", base64img="
				+ base64img + ", indentids=" + indentids + "]";
	}
	
}
