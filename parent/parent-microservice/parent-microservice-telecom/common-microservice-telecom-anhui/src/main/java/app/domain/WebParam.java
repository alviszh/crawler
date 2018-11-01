package app.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBill;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBusiness;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiCall;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiHtml;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiMessage;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiPay;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiScore;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiUserInfo;

public class WebParam<T> {

	private WebClient webClient;
	private HtmlPage page;
	private Integer code;
	private String url;   //存储请求页面的url
	private String html;  //存储请求页面的html代码
	private List<T> list;
	
	private TelecomAnhuiCall telecomAnhuiCall;
	
	private TelecomAnhuiMessage telecomAnhuiMessage;
	
	private TelecomAnhuiPay telecomAnhuiPay;
	
	private TelecomAnhuiScore telecomAnhuiScore;
	
	private TelecomAnhuiUserInfo telecomAnhuiUserInfo;
	
	private TelecomAnhuiHtml telecomAnhuiHtml;
	
	private TelecomAnhuiBill telecomAnhuiBill;
	
	private TelecomAnhuiBusiness telecomAnhuiBusiness;

	@Override
	public String toString() {
		return "WebParam [webClient=" + webClient + ", page=" + page + ", code=" + code + ", url=" + url + ", html="
				+ html + ", list=" + list + ", telecomAnhuiCall=" + telecomAnhuiCall + ", telecomAnhuiMessage="
				+ telecomAnhuiMessage + ", telecomAnhuiPay=" + telecomAnhuiPay + ", telecomAnhuiScore="
				+ telecomAnhuiScore + ", telecomAnhuiUserInfo=" + telecomAnhuiUserInfo + ", telecomAnhuiHtml="
				+ telecomAnhuiHtml + ", telecomAnhuiBill=" + telecomAnhuiBill + ", telecomAnhuiBusiness="
				+ telecomAnhuiBusiness + "]";
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
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

	public TelecomAnhuiCall getTelecomAnhuiCall() {
		return telecomAnhuiCall;
	}

	public void setTelecomAnhuiCall(TelecomAnhuiCall telecomAnhuiCall) {
		this.telecomAnhuiCall = telecomAnhuiCall;
	}

	public TelecomAnhuiMessage getTelecomAnhuiMessage() {
		return telecomAnhuiMessage;
	}

	public void setTelecomAnhuiMessage(TelecomAnhuiMessage telecomAnhuiMessage) {
		this.telecomAnhuiMessage = telecomAnhuiMessage;
	}

	public TelecomAnhuiPay getTelecomAnhuiPay() {
		return telecomAnhuiPay;
	}

	public void setTelecomAnhuiPay(TelecomAnhuiPay telecomAnhuiPay) {
		this.telecomAnhuiPay = telecomAnhuiPay;
	}

	public TelecomAnhuiScore getTelecomAnhuiScore() {
		return telecomAnhuiScore;
	}

	public void setTelecomAnhuiScore(TelecomAnhuiScore telecomAnhuiScore) {
		this.telecomAnhuiScore = telecomAnhuiScore;
	}

	public TelecomAnhuiUserInfo getTelecomAnhuiUserInfo() {
		return telecomAnhuiUserInfo;
	}

	public void setTelecomAnhuiUserInfo(TelecomAnhuiUserInfo telecomAnhuiUserInfo) {
		this.telecomAnhuiUserInfo = telecomAnhuiUserInfo;
	}

	public TelecomAnhuiHtml getTelecomAnhuiHtml() {
		return telecomAnhuiHtml;
	}

	public void setTelecomAnhuiHtml(TelecomAnhuiHtml telecomAnhuiHtml) {
		this.telecomAnhuiHtml = telecomAnhuiHtml;
	}

	public TelecomAnhuiBill getTelecomAnhuiBill() {
		return telecomAnhuiBill;
	}

	public void setTelecomAnhuiBill(TelecomAnhuiBill telecomAnhuiBill) {
		this.telecomAnhuiBill = telecomAnhuiBill;
	}

	public TelecomAnhuiBusiness getTelecomAnhuiBusiness() {
		return telecomAnhuiBusiness;
	}

	public void setTelecomAnhuiBusiness(TelecomAnhuiBusiness telecomAnhuiBusiness) {
		this.telecomAnhuiBusiness = telecomAnhuiBusiness;
	}
	
	
}
