package com.microservice.dao.entity.crawler.insurance.taiyuan;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/30.
 */
@Entity
@Table(name="insurance_taiyuan_html")
public class InsuranceTaiyuanHtml  extends IdEntity {
    //任务ID
    private String taskId;//uuid 前端通过uuid访问状态结果
    //
    private String type;
    //
    private Integer pageCount;
    //访问URL
    private String url;
    //爬取页面
    private String html;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
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
        return "InsuranceWenzhouHtml{" +
                "taskId='" + taskId + '\'' +
                ", type='" + type + '\'' +
                ", pageCount=" + pageCount +
                ", url='" + url + '\'' +
                ", html='" + html + '\'' +
                '}';
    }
}
