package app.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiBalance;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiBusiness;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiCurrentSituation;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiHtml;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiUserInfo;

public class WebParam<T> {
	private Page ppage;
	private HtmlPage hpage;
	private Integer code;
	private String url;   //存储请求页面的url
	private String html;  //存储请求页面的html代码
	private List<T> list;
	private TelecomJiangxiHtml telecomJiangxiHtml;
	private TelecomJiangxiUserInfo telecomJiangxiUserInfo;
	private TelecomJiangxiCurrentSituation telecomJiangxiCurrentSituation;
	private TelecomJiangxiBalance telecomJiangxiBalance;
	private TelecomJiangxiBusiness telecomJiangxiBusiness;
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
	public TelecomJiangxiHtml getTelecomJiangxiHtml() {
		return telecomJiangxiHtml;
	}
	public void setTelecomJiangxiHtml(TelecomJiangxiHtml telecomJiangxiHtml) {
		this.telecomJiangxiHtml = telecomJiangxiHtml;
	}
	public TelecomJiangxiUserInfo getTelecomJiangxiUserInfo() {
		return telecomJiangxiUserInfo;
	}
	public void setTelecomJiangxiUserInfo(TelecomJiangxiUserInfo telecomJiangxiUserInfo) {
		this.telecomJiangxiUserInfo = telecomJiangxiUserInfo;
	}
	public TelecomJiangxiCurrentSituation getTelecomJiangxiCurrentSituation() {
		return telecomJiangxiCurrentSituation;
	}
	public void setTelecomJiangxiCurrentSituation(TelecomJiangxiCurrentSituation telecomJiangxiCurrentSituation) {
		this.telecomJiangxiCurrentSituation = telecomJiangxiCurrentSituation;
	}
	public TelecomJiangxiBalance getTelecomJiangxiBalance() {
		return telecomJiangxiBalance;
	}
	public void setTelecomJiangxiBalance(TelecomJiangxiBalance telecomJiangxiBalance) {
		this.telecomJiangxiBalance = telecomJiangxiBalance;
	}
	public TelecomJiangxiBusiness getTelecomJiangxiBusiness() {
		return telecomJiangxiBusiness;
	}
	public void setTelecomJiangxiBusiness(TelecomJiangxiBusiness telecomJiangxiBusiness) {
		this.telecomJiangxiBusiness = telecomJiangxiBusiness;
	}
}
