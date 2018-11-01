package app.bean;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingUserInfo;

public class WebParam<T> {
	
	public HtmlPage page;
	public Integer code;
	public InsuranceBeijingUserInfo InsuranceBeijingUserInfo;
	public String url;
	public String html;
	public List<T> list;
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public WebClient webClient;
	
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
	public InsuranceBeijingUserInfo getInsuranceBeijingUserInfo() {
		return InsuranceBeijingUserInfo;
	}
	public void setInsuranceBeijingUserInfo(InsuranceBeijingUserInfo insuranceBeijingUserInfo) {
		InsuranceBeijingUserInfo = insuranceBeijingUserInfo;
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

}
