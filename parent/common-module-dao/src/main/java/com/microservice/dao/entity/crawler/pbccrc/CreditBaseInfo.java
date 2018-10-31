package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 人行征信报告 - 基本信息
 * Created by zmy on 2017/12/25.
 */
@Entity
@Table(name="credit_baseinfo")
public class CreditBaseInfo extends AbstractEntity implements Serializable{
    private static final long serialVersionUID = 5973685591253914493L;

    private String mapping_id;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号
    private String user_name;   //用户姓名
    private String idCard_type; //用户的证件类型
    private String idCard_no;   //证件号码
    private String query_time;  //用户申请人行征信报告的时间
    private String report_time; //人行征信报告的生成时间
    private String marital_status;  //婚姻状态

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIdCard_type() {
        return idCard_type;
    }

    public void setIdCard_type(String idCard_type) {
        this.idCard_type = idCard_type;
    }

    public String getIdCard_no() {
        return idCard_no;
    }

    public void setIdCard_no(String idCard_no) {
        this.idCard_no = idCard_no;
    }

    public String getQuery_time() {
        return query_time;
    }

    public void setQuery_time(String query_time) {
        this.query_time = query_time;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    @Override
    public String toString() {
        return "CreditBaseInfo{" +
                "mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", user_name='" + user_name + '\'' +
                ", idCard_type='" + idCard_type + '\'' +
                ", idCard_no='" + idCard_no + '\'' +
                ", query_time='" + query_time + '\'' +
                ", report_time='" + report_time + '\'' +
                ", marital_status='" + marital_status + '\'' +
                '}';
    }
}
