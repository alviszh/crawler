package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 浦发银行信用卡-网页
 * Created by zmy on 2017/12/5.
 */
@Entity
@Table(name="spdb_creditcard_html")
public class SpdbCreditCardHtml extends IdEntity implements Serializable {
    private static final long serialVersionUID = 5197643964582651784L;

    private String taskid;
    private String type;
    private Integer pagenumber;
    private String url;
    private String html;

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
        return "SpdbCreditCardHtml{" +
                "taskid='" + taskid + '\'' +
                ", type='" + type + '\'' +
                ", pagenumber=" + pagenumber +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                '}';
    }
}
