package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapPaymonths;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapPointrecord;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapRecharges;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapUserinfo;

public class WapParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public WebClient webClient;	
	public TelecomHubeiWapUserinfo userinfo;
	public List<TelecomHubeiWapCallrecords> callrecords;
	public List<TelecomHubeiWapPaymonths> paymonths;
	public TelecomHubeiWapPointrecord pointrecord;
	public List<TelecomHubeiWapRecharges> recharges;
	public String smsResult;
	public boolean state;
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
	public TelecomHubeiWapUserinfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(TelecomHubeiWapUserinfo userinfo) {
		this.userinfo = userinfo;
	}
	public List<TelecomHubeiWapCallrecords> getCallrecords() {
		return callrecords;
	}
	public void setCallrecords(List<TelecomHubeiWapCallrecords> callrecords) {
		this.callrecords = callrecords;
	}
	public List<TelecomHubeiWapPaymonths> getPaymonths() {
		return paymonths;
	}
	public void setPaymonths(List<TelecomHubeiWapPaymonths> paymonths) {
		this.paymonths = paymonths;
	}
	
	public TelecomHubeiWapPointrecord getPointrecord() {
		return pointrecord;
	}
	public void setPointrecord(TelecomHubeiWapPointrecord pointrecord) {
		this.pointrecord = pointrecord;
	}
	public List<TelecomHubeiWapRecharges> getRecharges() {
		return recharges;
	}
	public void setRecharges(List<TelecomHubeiWapRecharges> recharges) {
		this.recharges = recharges;
	}
	public String getSmsResult() {
		return smsResult;
	}
	public void setSmsResult(String smsResult) {
		this.smsResult = smsResult;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
}
