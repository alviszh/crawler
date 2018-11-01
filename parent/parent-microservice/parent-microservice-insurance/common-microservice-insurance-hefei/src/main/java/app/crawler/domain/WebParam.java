package app.crawler.domain;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiGeneral;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiUserInfo;

import java.util.List;

public class WebParam<T> {

    public HtmlPage page;
    public Integer code;
    public String url;   //存储请求页面的url
    public String html;  //存储请求页面的html代码
    public String alertMsg;//存储弹框信息

    private InsuranceHeFeiUserInfo insuranceHeFeiUserInfo;
    private List<InsuranceHeFeiGeneral> insuranceHeFeiGeneralList;

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

    public InsuranceHeFeiUserInfo getInsuranceHeFeiUserInfo() {
        return insuranceHeFeiUserInfo;
    }

    public void setInsuranceHeFeiUserInfo(InsuranceHeFeiUserInfo insuranceHeFeiUserInfo) {
        this.insuranceHeFeiUserInfo = insuranceHeFeiUserInfo;
    }

    public List<InsuranceHeFeiGeneral> getInsuranceHeFeiGeneralList() {
        return insuranceHeFeiGeneralList;
    }

    public void setInsuranceHeFeiGeneralList(List<InsuranceHeFeiGeneral> insuranceHeFeiGeneralList) {
        this.insuranceHeFeiGeneralList = insuranceHeFeiGeneralList;
    }

    @Override
    public String toString() {
        return "WebParam{" +
                "page=" + page +
                ", code=" + code +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                ", alertMsg='" + alertMsg + '\'' +
                '}';
    }
}
