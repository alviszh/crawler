package com.microservice.dao.entity.crawler.bank.beijingbank;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zmy on 2018/3/9.
 */

@Entity
@Table(name="beijingbank_debitcard_html")
public class BeijingBankDebitCardHtml extends IdEntity{
    private String taskid;
    private String type;
    private Integer pagenumber;
    private String url;
    private String html;
    private String body;//请求正文

    @Column(columnDefinition="text")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(Integer pagenumber) {
        this.pagenumber = pagenumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(columnDefinition="text")
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "BeijingBankDebitCardHtml{" +
                "taskid='" + taskid + '\'' +
                ", type='" + type + '\'' +
                ", pagenumber=" + pagenumber +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
