package app.common;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardAccount;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardBill;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardHtml;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardUserInfo;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardAccount;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardHtml;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardRegular;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardUserInfo;

/**
 * yl
 * @author Administrator
 *2017/10/26
 */
public class WebParam<T> {

	public Page page;  
	public WebClient webClient;
	public HtmlPage htmlPage;
	public Integer code;
	public String url;
	public String html;
	public List<T> list;
	
	public String webHandle;
	
	public CiticChinaCreditCardAccount citicChinaCreditCardAccount;
	public CiticChinaCreditCardUserInfo citicChinaCreditCardUserInfo;
	public CiticChinaCreditCardHtml citicChinaCreditCardHtml;
	public CiticChinaCreditCardBill citicChinaCreditCardBill;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", citicChinaCreditCardAccount=" + citicChinaCreditCardAccount + ", citicChinaCreditCardUserInfo="
				+ citicChinaCreditCardUserInfo + ", citicChinaCreditCardHtml=" + citicChinaCreditCardHtml
				+ ", citicChinaCreditCardBill=" + citicChinaCreditCardBill + "]";
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public HtmlPage getHtmlPage() {
		return htmlPage;
	}
	public void setHtmlPage(HtmlPage htmlPage) {
		this.htmlPage = htmlPage;
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
	public String getWebHandle() {
		return webHandle;
	}
	public void setWebHandle(String webHandle) {
		this.webHandle = webHandle;
	}
	public CiticChinaCreditCardAccount getCiticChinaCreditCardAccount() {
		return citicChinaCreditCardAccount;
	}
	public void setCiticChinaCreditCardAccount(CiticChinaCreditCardAccount citicChinaCreditCardAccount) {
		this.citicChinaCreditCardAccount = citicChinaCreditCardAccount;
	}
	public CiticChinaCreditCardUserInfo getCiticChinaCreditCardUserInfo() {
		return citicChinaCreditCardUserInfo;
	}
	public void setCiticChinaCreditCardUserInfo(CiticChinaCreditCardUserInfo citicChinaCreditCardUserInfo) {
		this.citicChinaCreditCardUserInfo = citicChinaCreditCardUserInfo;
	}
	public CiticChinaCreditCardHtml getCiticChinaCreditCardHtml() {
		return citicChinaCreditCardHtml;
	}
	public void setCiticChinaCreditCardHtml(CiticChinaCreditCardHtml citicChinaCreditCardHtml) {
		this.citicChinaCreditCardHtml = citicChinaCreditCardHtml;
	}
	public CiticChinaCreditCardBill getCiticChinaCreditCardBill() {
		return citicChinaCreditCardBill;
	}
	public void setCiticChinaCreditCardBill(CiticChinaCreditCardBill citicChinaCreditCardBill) {
		this.citicChinaCreditCardBill = citicChinaCreditCardBill;
	}
	
	
	
	
	
}
