package app.domain;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiPay;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiUserInfo;

import java.util.List;

public class WebParam<T> {

    public HtmlPage page;
    public Integer code;
    public String url;   //存储请求页面的url
    public String html;  //存储请求页面的html代码
    public String alertMsg;//存储弹框信息
    private String cookies;

    private HousingSZAnhuiUserInfo housingSZAnhuiUserInfo;
    private List<HousingSZAnhuiPay> housingSZAnhuiPayList;

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

    public String getAlertMsg() {
        return alertMsg;
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
    }

    public HousingSZAnhuiUserInfo getHousingSZAnhuiUserInfo() {
        return housingSZAnhuiUserInfo;
    }

    public void setHousingSZAnhuiUserInfo(HousingSZAnhuiUserInfo housingSZAnhuiUserInfo) {
        this.housingSZAnhuiUserInfo = housingSZAnhuiUserInfo;
    }

    public List<HousingSZAnhuiPay> getHousingSZAnhuiPayList() {
        return housingSZAnhuiPayList;
    }

    public void setHousingSZAnhuiPayList(List<HousingSZAnhuiPay> housingSZAnhuiPayList) {
        this.housingSZAnhuiPayList = housingSZAnhuiPayList;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    @Override
    public String toString() {
        return "WebParam{" +
                "page=" + page +
                ", code=" + code +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                ", alertMsg='" + alertMsg + '\'' +
                ", housingSZAnhuiUserInfo=" + housingSZAnhuiUserInfo +
                ", housingSZAnhuiPayList=" + housingSZAnhuiPayList +
                '}';
    }
}
