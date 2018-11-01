package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuBusiness;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuCall;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuHtml;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuMessage;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPay;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPhoneBill;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPhoneMonthBill;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuScore;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuUserInfo;

public class WebParam<T> {


	private HtmlPage page;
	private Integer code;
	private String url;   //存储请求页面的url
	private String html;  //存储请求页面的html代码
	private List<T> list;
	private WebClient webClient;

	private TelecomGansuPhoneBill telecomGansuPhoneBill;
	
	private TelecomGansuPay telecomGansuPay;
	
	private TelecomGansuUserInfo telecomGansuUserInfo;
	
	private TelecomGansuBusiness telecomGansuBusiness;
	
	private TelecomGansuScore telecomGansuScore;

	private TelecomGansuPhoneMonthBill telecomGansuPhoneMonthBill;
	
	private TelecomGansuMessage telecomGansuMessage;
	
	private TelecomGansuCall telecomGansuCall;
	
	private TelecomGansuHtml telecomGansuHtml;

	@Override
	public String toString() {
		return "WebParam [page=" + page + ", code=" + code + ", url=" + url + ", html=" + html + ", list=" + list
				+ ", webClient=" + webClient + ", telecomGansuPhoneBill=" + telecomGansuPhoneBill + ", telecomGansuPay="
				+ telecomGansuPay + ", telecomGansuUserInfo=" + telecomGansuUserInfo + ", telecomGansuBusiness="
				+ telecomGansuBusiness + ", telecomGansuScore=" + telecomGansuScore + ", telecomGansuPhoneMonthBill="
				+ telecomGansuPhoneMonthBill + ", telecomGansuMessage=" + telecomGansuMessage + ", telecomGansuCall="
				+ telecomGansuCall + ", telecomGansuHtml=" + telecomGansuHtml + "]";
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

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public TelecomGansuPhoneBill getTelecomGansuPhoneBill() {
		return telecomGansuPhoneBill;
	}

	public void setTelecomGansuPhoneBill(TelecomGansuPhoneBill telecomGansuPhoneBill) {
		this.telecomGansuPhoneBill = telecomGansuPhoneBill;
	}

	public TelecomGansuPay getTelecomGansuPay() {
		return telecomGansuPay;
	}

	public void setTelecomGansuPay(TelecomGansuPay telecomGansuPay) {
		this.telecomGansuPay = telecomGansuPay;
	}

	public TelecomGansuUserInfo getTelecomGansuUserInfo() {
		return telecomGansuUserInfo;
	}

	public void setTelecomGansuUserInfo(TelecomGansuUserInfo telecomGansuUserInfo) {
		this.telecomGansuUserInfo = telecomGansuUserInfo;
	}

	public TelecomGansuBusiness getTelecomGansuBusiness() {
		return telecomGansuBusiness;
	}

	public void setTelecomGansuBusiness(TelecomGansuBusiness telecomGansuBusiness) {
		this.telecomGansuBusiness = telecomGansuBusiness;
	}

	public TelecomGansuScore getTelecomGansuScore() {
		return telecomGansuScore;
	}

	public void setTelecomGansuScore(TelecomGansuScore telecomGansuScore) {
		this.telecomGansuScore = telecomGansuScore;
	}

	public TelecomGansuPhoneMonthBill getTelecomGansuPhoneMonthBill() {
		return telecomGansuPhoneMonthBill;
	}

	public void setTelecomGansuPhoneMonthBill(TelecomGansuPhoneMonthBill telecomGansuPhoneMonthBill) {
		this.telecomGansuPhoneMonthBill = telecomGansuPhoneMonthBill;
	}

	public TelecomGansuMessage getTelecomGansuMessage() {
		return telecomGansuMessage;
	}

	public void setTelecomGansuMessage(TelecomGansuMessage telecomGansuMessage) {
		this.telecomGansuMessage = telecomGansuMessage;
	}

	public TelecomGansuCall getTelecomGansuCall() {
		return telecomGansuCall;
	}

	public void setTelecomGansuCall(TelecomGansuCall telecomGansuCall) {
		this.telecomGansuCall = telecomGansuCall;
	}

	public TelecomGansuHtml getTelecomGansuHtml() {
		return telecomGansuHtml;
	}

	public void setTelecomGansuHtml(TelecomGansuHtml telecomGansuHtml) {
		this.telecomGansuHtml = telecomGansuHtml;
	}
	
	
	
}
