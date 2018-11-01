package app.crawler.domain;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingPayDetails;
import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingUserInfo;

public class WebParam {
	
	public Page page;
	public Integer code;
	public String url;
	public String html;
	public String text;
	public WebClient webClient;	
	public List<HousingQujingPayDetails>  paydetails;
	public HousingQujingUserInfo userInfo;
	public boolean isLogin;
	public String alertMsg;//存储弹框信息
	public String totalpage;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public WebClient getWebClient() {
		return webClient;
	}
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public List<HousingQujingPayDetails> getPaydetails() {
		return paydetails;
	}
	public void setPaydetails(List<HousingQujingPayDetails> paydetails) {
		this.paydetails = paydetails;
	}
	public HousingQujingUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(HousingQujingUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public String getAlertMsg() {
		return alertMsg;
	}
	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}
	
	public String getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}
	
	
}
