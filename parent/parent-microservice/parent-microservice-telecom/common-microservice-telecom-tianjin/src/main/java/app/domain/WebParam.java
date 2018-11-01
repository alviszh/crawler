package app.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinAccountInfo;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinBalance;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinBusiness;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinCurrentSituation;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinHtml;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinUserInfo;

public class WebParam<T> {
	private Page ppage;
	private HtmlPage hpage;
	private Integer code;
	private String url;   //存储请求页面的url
	private String html;  //存储请求页面的html代码
	private List<T> list;
	private TelecomTianjinHtml telecomTianjinHtml;
	private TelecomTianjinUserInfo telecomTianjinUserInfo;
	private TelecomTianjinCurrentSituation telecomTianjinCurrentSituation;
	private TelecomTianjinAccountInfo telecomTianjinAccountInfo;
	private TelecomTianjinBalance telecomTianjinBalance;
	private TelecomTianjinBusiness telecomTianjinBusiness;
	public Page getPpage() {
		return ppage;
	}
	public void setPpage(Page ppage) {
		this.ppage = ppage;
	}
	public HtmlPage getHpage() {
		return hpage;
	}
	public void setHpage(HtmlPage hpage) {
		this.hpage = hpage;
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
	public TelecomTianjinHtml getTelecomTianjinHtml() {
		return telecomTianjinHtml;
	}
	public void setTelecomTianjinHtml(TelecomTianjinHtml telecomTianjinHtml) {
		this.telecomTianjinHtml = telecomTianjinHtml;
	}
	public TelecomTianjinUserInfo getTelecomTianjinUserInfo() {
		return telecomTianjinUserInfo;
	}
	public void setTelecomTianjinUserInfo(TelecomTianjinUserInfo telecomTianjinUserInfo) {
		this.telecomTianjinUserInfo = telecomTianjinUserInfo;
	}
	public TelecomTianjinCurrentSituation getTelecomTianjinCurrentSituation() {
		return telecomTianjinCurrentSituation;
	}
	public void setTelecomTianjinCurrentSituation(TelecomTianjinCurrentSituation telecomTianjinCurrentSituation) {
		this.telecomTianjinCurrentSituation = telecomTianjinCurrentSituation;
	}
	public TelecomTianjinAccountInfo getTelecomTianjinAccountInfo() {
		return telecomTianjinAccountInfo;
	}
	public void setTelecomTianjinAccountInfo(TelecomTianjinAccountInfo telecomTianjinAccountInfo) {
		this.telecomTianjinAccountInfo = telecomTianjinAccountInfo;
	}
	public TelecomTianjinBalance getTelecomTianjinBalance() {
		return telecomTianjinBalance;
	}
	public void setTelecomTianjinBalance(TelecomTianjinBalance telecomTianjinBalance) {
		this.telecomTianjinBalance = telecomTianjinBalance;
	}
	public TelecomTianjinBusiness getTelecomTianjinBusiness() {
		return telecomTianjinBusiness;
	}
	public void setTelecomTianjinBusiness(TelecomTianjinBusiness telecomTianjinBusiness) {
		this.telecomTianjinBusiness = telecomTianjinBusiness;
	}
	
	
}
