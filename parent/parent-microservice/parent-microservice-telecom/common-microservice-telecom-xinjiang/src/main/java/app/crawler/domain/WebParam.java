package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangAddvalueItem;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPayMonths;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPointRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangProductInfo;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangRealtimeFee;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangRechargeRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangSmsRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangUserInfo;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecord;

public class WebParam {
	
	public HtmlPage page;
	public Integer code;
	public String url;
	public String html;
	public TelecomXinjiangUserInfo userInfo;
	public TelecomXinjiangProductInfo productInfo;
	public List<TelecomXinjiangRealtimeFee> realtimeFees;
	public List<TelecomXinjiangPointRecord>   points;
	public List<TelecomXinjiangAddvalueItem> addvalueItems;
	public List<TelecomXinjiangRechargeRecord>  rechargeRecords;
	public List<TelecomXinjiangVoiceRecord>  voiceRecords;
	public List<TelecomXinjiangSmsRecord> smsRecords;
	public List<TelecomXinjiangPayMonths> payMonths;
	public String maxPage;
	public WebClient webClient;
	public String smsResult;
	
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
	
	public TelecomXinjiangUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(TelecomXinjiangUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public TelecomXinjiangProductInfo getProductInfo() {
		return productInfo;
	}
	public void setProductInfo(TelecomXinjiangProductInfo productInfo) {
		this.productInfo = productInfo;
	}
	public List<TelecomXinjiangRealtimeFee> getRealtimeFees() {
		return realtimeFees;
	}
	public void setRealtimeFees(List<TelecomXinjiangRealtimeFee> realtimeFees) {
		this.realtimeFees = realtimeFees;
	}
	public List<TelecomXinjiangPointRecord> getPoints() {
		return points;
	}
	public void setPoints(List<TelecomXinjiangPointRecord> points) {
		this.points = points;
	}
	public List<TelecomXinjiangAddvalueItem> getAddvalueItems() {
		return addvalueItems;
	}
	public void setAddvalueItems(List<TelecomXinjiangAddvalueItem> addvalueItems) {
		this.addvalueItems = addvalueItems;
	}
	public List<TelecomXinjiangRechargeRecord> getRechargeRecords() {
		return rechargeRecords;
	}
	public void setRechargeRecords(List<TelecomXinjiangRechargeRecord> rechargeRecords) {
		this.rechargeRecords = rechargeRecords;
	}
	public List<TelecomXinjiangVoiceRecord> getVoiceRecords() {
		return voiceRecords;
	}
	public void setVoiceRecords(List<TelecomXinjiangVoiceRecord> voiceRecords) {
		this.voiceRecords = voiceRecords;
	}
	public List<TelecomXinjiangSmsRecord> getSmsRecords() {
		return smsRecords;
	}
	public void setSmsRecords(List<TelecomXinjiangSmsRecord> smsRecords) {
		this.smsRecords = smsRecords;
	}
	public String getMaxPage() {
		return maxPage;
	}
	public void setMaxPage(String maxPage) {
		this.maxPage = maxPage;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public String getSmsResult() {
		return smsResult;
	}
	public void setSmsResult(String smsResult) {
		this.smsResult = smsResult;
	}
	public List<TelecomXinjiangPayMonths> getPayMonths() {
		return payMonths;
	}
	public void setPayMonths(List<TelecomXinjiangPayMonths> payMonths) {
		this.payMonths = payMonths;
	}
}
