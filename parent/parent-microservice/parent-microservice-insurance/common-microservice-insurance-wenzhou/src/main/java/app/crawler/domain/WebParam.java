package app.crawler.domain;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.wenzhou.*;

import java.util.List;

public class WebParam {

	private HtmlPage page;//返回页面
    private Page pg; //返回page类型页面
	private Integer code;
	private String alertMsg;//存储弹框信息
	private String url;//存储请求页面的url
	private String html;//存储请求页面的html代码
	private InsuranceWenzhouUserinfo userInfo;
	private List<InsuranceWenzhouInsuranceBasic> insuranceBasic;
	private InsuranceWenzhouStatus medical;
	private List<InsuranceWenzhouPensionPaywater> pensionPaywater;
	private InsuranceWenzhouHtml wenzhouHtml;
    private String userName;
    private String passWord;

    public Page getPg() {
        return pg;
    }

    public void setPg(Page pg) {
        this.pg = pg;
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

    public InsuranceWenzhouUserinfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(InsuranceWenzhouUserinfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<InsuranceWenzhouInsuranceBasic> getInsuranceBasic() {
        return insuranceBasic;
    }

    public void setInsuranceBasic(List<InsuranceWenzhouInsuranceBasic> insuranceBasic) {
        this.insuranceBasic = insuranceBasic;
    }

    public InsuranceWenzhouStatus getMedical() {
        return medical;
    }

    public void setMedical(InsuranceWenzhouStatus medical) {
        this.medical = medical;
    }

    public List<InsuranceWenzhouPensionPaywater> getPensionPaywater() {
        return pensionPaywater;
    }

    public void setPensionPaywater(List<InsuranceWenzhouPensionPaywater> pensionPaywater) {
        this.pensionPaywater = pensionPaywater;
    }

    public InsuranceWenzhouHtml getWenzhouHtml() {
        return wenzhouHtml;
    }

    public void setWenzhouHtml(InsuranceWenzhouHtml wenzhouHtml) {
        this.wenzhouHtml = wenzhouHtml;
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
                ", insuranceBasic=" + insuranceBasic +
                ", medical=" + medical +
                ", pensionPaywater=" + pensionPaywater +
                ", wenzhouHtml=" + wenzhouHtml +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
