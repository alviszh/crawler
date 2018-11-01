package app.crawler.domain;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiUserInfo;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-19 14:09
 */
public class WebParam<T> {

    public HtmlPage page;
    public Integer code;

    public InsuranceWuxiUserInfo insuranceWuxiUserInfo;

    public String url;
    public String html;


    public List<T> list;

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

    public InsuranceWuxiUserInfo getInsuranceWuxiUserInfo() {
        return insuranceWuxiUserInfo;
    }

    public void setInsuranceWuxiUserInfo(InsuranceWuxiUserInfo insuranceWuxiUserInfo) {
        this.insuranceWuxiUserInfo = insuranceWuxiUserInfo;
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

}
