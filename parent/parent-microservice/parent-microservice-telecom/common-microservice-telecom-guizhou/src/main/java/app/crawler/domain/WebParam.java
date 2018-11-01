package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouAccount;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouCallrecord;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouPaymonth;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouPoint;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouRecharges;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouSmsrecord;

public class WebParam {
	
	public HtmlPage htmlPage;
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public WebClient webClient;	
	public TelecomGuizhouAccount account;
	public TelecomGuizhouPoint point;
	public List<TelecomGuizhouCallrecord> callrecords;
	public List<TelecomGuizhouPaymonth> paymonths;
	public List<TelecomGuizhouRecharges> Recharges;
	public List<TelecomGuizhouSmsrecord> smsrecords;
	public String smsResult;
	
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
	public TelecomGuizhouAccount getAccount() {
		return account;
	}
	public void setAccount(TelecomGuizhouAccount account) {
		this.account = account;
	}
	public List<TelecomGuizhouCallrecord> getCallrecords() {
		return callrecords;
	}
	public void setCallrecords(List<TelecomGuizhouCallrecord> callrecords) {
		this.callrecords = callrecords;
	}
	public List<TelecomGuizhouPaymonth> getPaymonths() {
		return paymonths;
	}
	public void setPaymonths(List<TelecomGuizhouPaymonth> paymonths) {
		this.paymonths = paymonths;
	}
	public List<TelecomGuizhouRecharges> getRecharges() {
		return Recharges;
	}
	public void setRecharges(List<TelecomGuizhouRecharges> recharges) {
		Recharges = recharges;
	}
	public List<TelecomGuizhouSmsrecord> getSmsrecords() {
		return smsrecords;
	}
	public void setSmsrecords(List<TelecomGuizhouSmsrecord> smsrecords) {
		this.smsrecords = smsrecords;
	}
	public String getSmsResult() {
		return smsResult;
	}
	public void setSmsResult(String smsResult) {
		this.smsResult = smsResult;
	}
	public TelecomGuizhouPoint getPoint() {
		return point;
	}
	public void setPoint(TelecomGuizhouPoint point) {
		this.point = point;
	}
}
