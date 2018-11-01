package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiAccount;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPaymonths;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPointrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiRecharges;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiServices;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiSmsrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiUserinfo;

public class WebParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public WebClient webClient;	
	public String smsResult;
	public TelecomHubeiUserinfo userinfo;
	public TelecomHubeiAccount  accountinfo;	
	public List<TelecomHubeiCallrecords> callrecords;
	public List<TelecomHubeiPaymonths> paymonths;
	public List<TelecomHubeiPointrecords> pointrecords;
	public List<TelecomHubeiRecharges> recharges;
	public List<TelecomHubeiSmsrecords> smsrecords;
	public List<TelecomHubeiServices> services;
	public String number;
	public String areaCode;
	public String isLogin;
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
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
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public TelecomHubeiUserinfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(TelecomHubeiUserinfo userinfo) {
		this.userinfo = userinfo;
	}
	public String getSmsResult() {
		return smsResult;
	}
	public void setSmsResult(String smsResult) {
		this.smsResult = smsResult;
	}
	public TelecomHubeiAccount getAccountinfo() {
		return accountinfo;
	}
	public void setAccountinfo(TelecomHubeiAccount accountinfo) {
		this.accountinfo = accountinfo;
	}
	public List<TelecomHubeiCallrecords> getCallrecords() {
		return callrecords;
	}
	public void setCallrecords(List<TelecomHubeiCallrecords> callrecords) {
		this.callrecords = callrecords;
	}
	public List<TelecomHubeiPaymonths> getPaymonths() {
		return paymonths;
	}
	public void setPaymonths(List<TelecomHubeiPaymonths> paymonths) {
		this.paymonths = paymonths;
	}
	public List<TelecomHubeiPointrecords> getPointrecords() {
		return pointrecords;
	}
	public void setPointrecords(List<TelecomHubeiPointrecords> pointrecords) {
		this.pointrecords = pointrecords;
	}
	public List<TelecomHubeiRecharges> getRecharges() {
		return recharges;
	}
	public void setRecharges(List<TelecomHubeiRecharges> recharges) {
		this.recharges = recharges;
	}
	public List<TelecomHubeiSmsrecords> getSmsrecords() {
		return smsrecords;
	}
	public void setSmsrecords(List<TelecomHubeiSmsrecords> smsrecords) {
		this.smsrecords = smsrecords;
	}
	public List<TelecomHubeiServices> getServices() {
		return services;
	}
	public void setServices(List<TelecomHubeiServices> services) {
		this.services = services;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
}
