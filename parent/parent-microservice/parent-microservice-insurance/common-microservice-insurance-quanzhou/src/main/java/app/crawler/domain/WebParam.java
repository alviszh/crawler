package app.crawler.domain;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouInsuredInfo;
import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouUserInfo;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyongjie
 * @create 2017-09-19 14:09
 */
public class WebParam<T> {

    public HtmlPage page;
    public Integer code;

    public InsuranceQuanzhouUserInfo insuranceQuanzhouUserInfo;

    public InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo;

    public String url;
    public String html;

    //获取页面传递参数
    public Map<String,Object> params;

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

    public InsuranceQuanzhouUserInfo getInsuranceQuanzhouUserInfo() {
        return insuranceQuanzhouUserInfo;
    }

    public void setInsuranceQuanzhouUserInfo(InsuranceQuanzhouUserInfo insuranceQuanzhouUserInfo) {
        this.insuranceQuanzhouUserInfo = insuranceQuanzhouUserInfo;
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

    public InsuranceQuanzhouInsuredInfo getInsuranceQuanzhouInsuredInfo() {
        return insuranceQuanzhouInsuredInfo;
    }

    public void setInsuranceQuanzhouInsuredInfo(InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo) {
        this.insuranceQuanzhouInsuredInfo = insuranceQuanzhouInsuredInfo;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
