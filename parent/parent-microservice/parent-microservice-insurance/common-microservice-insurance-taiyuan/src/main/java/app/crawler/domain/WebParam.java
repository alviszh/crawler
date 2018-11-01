package app.crawler.domain;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.taiyuan.*;
import com.microservice.dao.entity.crawler.insurance.wenzhou.*;

import java.util.List;

public class WebParam {

	private HtmlPage page;//返回页面
    private Page pg; //返回page类型页面
	private Integer code;
	private String alertMsg;//存储弹框信息
	private String url;//存储请求页面的url
	private String html;//存储请求页面的html代码
	private List<InsuranceTaiyuanUserinfo> userinfos;
	private List<InsuranceTaiyuanFirst> firsts;
	private InsuranceTaiyuanHtml taiyuanHtml;
	private List<InsuranceTaiyuanResidentWater>  residentWaters;
	private List<InsuranceTaiyuanStaffWater> staffWaters;
    private String userName;
    private String passWord;

    public HtmlPage getPage() {
        return page;
    }

    public void setPage(HtmlPage page) {
        this.page = page;
    }

    public Page getPg() {
        return pg;
    }

    public void setPg(Page pg) {
        this.pg = pg;
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

    public List<InsuranceTaiyuanUserinfo> getUserinfos() {
        return userinfos;
    }

    public void setUserinfos(List<InsuranceTaiyuanUserinfo> userinfos) {
        this.userinfos = userinfos;
    }

    public List<InsuranceTaiyuanFirst> getFirsts() {
        return firsts;
    }

    public void setFirsts(List<InsuranceTaiyuanFirst> firsts) {
        this.firsts = firsts;
    }

    public InsuranceTaiyuanHtml getTaiyuanHtml() {
        return taiyuanHtml;
    }

    public void setTaiyuanHtml(InsuranceTaiyuanHtml taiyuanHtml) {
        this.taiyuanHtml = taiyuanHtml;
    }

    public List<InsuranceTaiyuanResidentWater> getResidentWaters() {
        return residentWaters;
    }

    public void setResidentWaters(List<InsuranceTaiyuanResidentWater> residentWaters) {
        this.residentWaters = residentWaters;
    }

    public List<InsuranceTaiyuanStaffWater> getStaffWaters() {
        return staffWaters;
    }

    public void setStaffWaters(List<InsuranceTaiyuanStaffWater> staffWaters) {
        this.staffWaters = staffWaters;
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
                ", pg=" + pg +
                ", code=" + code +
                ", alertMsg='" + alertMsg + '\'' +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                ", userinfos=" + userinfos +
                ", firsts=" + firsts +
                ", taiyuanHtml=" + taiyuanHtml +
                ", residentWaters=" + residentWaters +
                ", staffWaters=" + staffWaters +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
