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
	
	public CiticChinaDebitCardUserInfo citicChinaDebitCardUserInfo;
	public CiticChinaDebitCardAccount citicChinaDebitCardAccount;
	public CiticChinaDebitCardHtml  citicChinaDebitCardHtml;
	public CiticChinaDebitCardRegular citicChinaDebitCardRegular;
	@Override
	public String toString() {
		return "WebParam [page=" + page + ", webClient=" + webClient + ", htmlPage=" + htmlPage + ", code=" + code
				+ ", url=" + url + ", html=" + html + ", list=" + list + ", webHandle=" + webHandle
				+ ", citicChinaDebitCardUserInfo=" + citicChinaDebitCardUserInfo + ", citicChinaDebitCardAccount="
				+ citicChinaDebitCardAccount + ", citicChinaDebitCardHtml=" + citicChinaDebitCardHtml
				+ ", citicChinaDebitCardRegular=" + citicChinaDebitCardRegular + "]";
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
	public CiticChinaDebitCardUserInfo getCiticChinaDebitCardUserInfo() {
		return citicChinaDebitCardUserInfo;
	}
	public void setCiticChinaDebitCardUserInfo(CiticChinaDebitCardUserInfo citicChinaDebitCardUserInfo) {
		this.citicChinaDebitCardUserInfo = citicChinaDebitCardUserInfo;
	}
	public CiticChinaDebitCardAccount getCiticChinaDebitCardAccount() {
		return citicChinaDebitCardAccount;
	}
	public void setCiticChinaDebitCardAccount(CiticChinaDebitCardAccount citicChinaDebitCardAccount) {
		this.citicChinaDebitCardAccount = citicChinaDebitCardAccount;
	}
	public CiticChinaDebitCardHtml getCiticChinaDebitCardHtml() {
		return citicChinaDebitCardHtml;
	}
	public void setCiticChinaDebitCardHtml(CiticChinaDebitCardHtml citicChinaDebitCardHtml) {
		this.citicChinaDebitCardHtml = citicChinaDebitCardHtml;
	}
	public CiticChinaDebitCardRegular getCiticChinaDebitCardRegular() {
		return citicChinaDebitCardRegular;
	}
	public void setCiticChinaDebitCardRegular(CiticChinaDebitCardRegular citicChinaDebitCardRegular) {
		this.citicChinaDebitCardRegular = citicChinaDebitCardRegular;
	}
	
	
	
	
	
}
