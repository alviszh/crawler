package app.domain.crawler;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBill;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBusiness;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiCall;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiHtml;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiMessage;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiPay;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiScore;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiUserInfo;

public class WebParam<T> {

	private WebClient webClient;
	private HtmlPage page;
	private Integer code;
	private String url;   //存储请求页面的url
	private String html;  //存储请求页面的html代码
	private List<T> list;
	private String prodType;
	
	private TelecomGuangxiCall telecomGuangxiCall;
	
	private TelecomGuangxiBill telecomGuangxiBill;
	
	private TelecomGuangxiMessage telecomGuangxiMessage;
	
	private TelecomGuangxiScore telecomGuangxiScore;
	
	private TelecomGuangxiPay telecomGuangxiPay;
	
	private TelecomGuangxiUserInfo telecomGuangxiUserInfo;
	 
	private TelecomGuangxiHtml telecomGuangxiHtml;
	
	private TelecomGuangxiBusiness telecomGuangxiBusiness;

	@Override
	public String toString() {
		return "WebParam [webClient=" + webClient + ", page=" + page + ", code=" + code + ", url=" + url + ", html="
				+ html + ", list=" + list + ", telecomGuangxiCall=" + telecomGuangxiCall + ", telecomGuangxiBill="
				+ telecomGuangxiBill + ", telecomGuangxiMessage=" + telecomGuangxiMessage + ", telecomGuangxiScore="
				+ telecomGuangxiScore + ", telecomGuangxiPay=" + telecomGuangxiPay + ", telecomGuangxiUserInfo="
				+ telecomGuangxiUserInfo + ", telecomGuangxiHtml=" + telecomGuangxiHtml + ", telecomGuangxiBusiness="
				+ telecomGuangxiBusiness + "]";
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

	public TelecomGuangxiCall getTelecomGuangxiCall() {
		return telecomGuangxiCall;
	}

	public void setTelecomGuangxiCall(TelecomGuangxiCall telecomGuangxiCall) {
		this.telecomGuangxiCall = telecomGuangxiCall;
	}

	public TelecomGuangxiBill getTelecomGuangxiBill() {
		return telecomGuangxiBill;
	}

	public void setTelecomGuangxiBill(TelecomGuangxiBill telecomGuangxiBill) {
		this.telecomGuangxiBill = telecomGuangxiBill;
	}

	public TelecomGuangxiMessage getTelecomGuangxiMessage() {
		return telecomGuangxiMessage;
	}

	public void setTelecomGuangxiMessage(TelecomGuangxiMessage telecomGuangxiMessage) {
		this.telecomGuangxiMessage = telecomGuangxiMessage;
	}

	public TelecomGuangxiScore getTelecomGuangxiScore() {
		return telecomGuangxiScore;
	}

	public void setTelecomGuangxiScore(TelecomGuangxiScore telecomGuangxiScore) {
		this.telecomGuangxiScore = telecomGuangxiScore;
	}

	public TelecomGuangxiPay getTelecomGuangxiPay() {
		return telecomGuangxiPay;
	}

	public void setTelecomGuangxiPay(TelecomGuangxiPay telecomGuangxiPay) {
		this.telecomGuangxiPay = telecomGuangxiPay;
	}

	public TelecomGuangxiUserInfo getTelecomGuangxiUserInfo() {
		return telecomGuangxiUserInfo;
	}

	public void setTelecomGuangxiUserInfo(TelecomGuangxiUserInfo telecomGuangxiUserInfo) {
		this.telecomGuangxiUserInfo = telecomGuangxiUserInfo;
	}

	public TelecomGuangxiHtml getTelecomGuangxiHtml() {
		return telecomGuangxiHtml;
	}

	public void setTelecomGuangxiHtml(TelecomGuangxiHtml telecomGuangxiHtml) {
		this.telecomGuangxiHtml = telecomGuangxiHtml;
	}

	public TelecomGuangxiBusiness getTelecomGuangxiBusiness() {
		return telecomGuangxiBusiness;
	}

	public void setTelecomGuangxiBusiness(TelecomGuangxiBusiness telecomGuangxiBusiness) {
		this.telecomGuangxiBusiness = telecomGuangxiBusiness;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	

	

	
	
	
}
