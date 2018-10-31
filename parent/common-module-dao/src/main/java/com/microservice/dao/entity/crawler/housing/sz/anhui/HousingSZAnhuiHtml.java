package com.microservice.dao.entity.crawler.housing.sz.anhui;


import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="housing_sz_anhui_html")
public class HousingSZAnhuiHtml  extends IdEntity implements Serializable {

    private static final long serialVersionUID = 7874933455038037278L;
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

}


