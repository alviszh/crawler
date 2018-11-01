package app.crawler.domain;

import java.util.List;
import java.util.Set;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangPaymentDetailsEachyear;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangPaymentPastYears;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangUserInfo;

public class WebParam {

	private HtmlPage page;//返回页面
	private Integer code;
	private String alertMsg;//存储弹框信息
	private String url;//存储请求页面的url
	private String html;//存储请求页面的html代码
	private InsuranceShenyangUserInfo    userInfo;
	private List<InsuranceShenyangPaymentPastYears>  paymentPastYearsList;
	private List<InsuranceShenyangPaymentDetailsEachyear>    paymentDetailsEachyearList;
    private String userName;
    private String passWord;

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

    public String getAlertMsg() {
        return alertMsg;
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
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

    public InsuranceShenyangUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(InsuranceShenyangUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<InsuranceShenyangPaymentPastYears> getPaymentPastYearsList() {
        return paymentPastYearsList;
    }

    public void setPaymentPastYearsList(List<InsuranceShenyangPaymentPastYears> paymentPastYearsList) {
        this.paymentPastYearsList = paymentPastYearsList;
    }

    public List<InsuranceShenyangPaymentDetailsEachyear> getPaymentDetailsEachyearList() {
        return paymentDetailsEachyearList;
    }

    public void setPaymentDetailsEachyearList(List<InsuranceShenyangPaymentDetailsEachyear> paymentDetailsEachyearList) {
        this.paymentDetailsEachyearList = paymentDetailsEachyearList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "WebParam{" +
                "page=" + page +
                ", code=" + code +
                ", alertMsg='" + alertMsg + '\'' +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                ", userInfo=" + userInfo +
                ", paymentPastYearsList=" + paymentPastYearsList +
                ", paymentDetailsEachyearList=" + paymentDetailsEachyearList +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
    @SuppressWarnings("all")
    public static WebClient getWebClient(Set<Cookie> cookies) {
        WebClient  webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setRefreshHandler(new ThreadedRefreshHandler());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(15000); // 15->60
        webClient.waitForBackgroundJavaScript(10000); // 5s
        try{
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        }catch(Exception e){
        }
        webClient.getOptions().setUseInsecureSSL(true); //
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理

        for(Cookie cookie:cookies){
            webClient.getCookieManager().addCookie(cookie);
        }
        return webClient;
    }

    @SuppressWarnings("all")
    public static WebClient getWebClient() {
        WebClient  webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setRefreshHandler(new ThreadedRefreshHandler());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(15000); // 15->60
        webClient.waitForBackgroundJavaScript(10000); // 5s
        try{
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        }catch(Exception e){
        }
        webClient.getOptions().setUseInsecureSSL(true); //
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理
        return webClient;
    }
}
