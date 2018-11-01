package app.crawler.domain;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.sz.anhui.InsuranceSZAnHuiUserInfo;

public class WebParam {

	public HtmlPage page;
	public Integer code;	
	public InsuranceSZAnHuiUserInfo  insuranceSZAnHuiUserInfo;	
	public WebClient  webClient;
	public String url;//存储请求页面的url
	public String html;//存储请求页面的html代码
	public String msgAlert;
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
	
	public InsuranceSZAnHuiUserInfo getInsuranceSZAnHuiUserInfo() {
		return insuranceSZAnHuiUserInfo;
	}
	public void setInsuranceSZAnHuiUserInfo(InsuranceSZAnHuiUserInfo insuranceSZAnHuiUserInfo) {
		this.insuranceSZAnHuiUserInfo = insuranceSZAnHuiUserInfo;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public String getMsgAlert() {
		return msgAlert;
	}
	public void setMsgAlert(String msgAlert) {
		this.msgAlert = msgAlert;
	}
	
}
