package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * 公共记录
 * Created by zmy on 2017/12/26.
 */
//@Entity
//@Table(name="public_information_detail")
public class PublicInformationDetail<T> implements Serializable{
    private static final long serialVersionUID = -6846682462937999401L;

    private String mapping_id;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号
    private String type;   //公共记录类型
    private List<T> content;   //公共记录明细

    public String getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(String mapping_id) {
        this.mapping_id = mapping_id;
    }

    public String getReport_no() {
        return report_no;
    }

    public void setReport_no(String report_no) {
        this.report_no = report_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PublicInformationDetail{" +
                "mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", type='" + type + '\'' +
                ", content=" + content +
                '}';
    }
}
