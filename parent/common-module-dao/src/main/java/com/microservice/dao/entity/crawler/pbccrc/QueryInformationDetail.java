package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 查询记录
 * Created by zmy on 2017/12/26.
 */
@Entity
@Table(name="query_information_detail")
public class QueryInformationDetail extends AbstractEntity implements Serializable{
    private static final long serialVersionUID = -959196025440664905L;

    private String mapping_id;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号
    private String query_dateTime;   //记录查询时间
    private String query_operator;   //查询操作员
    private String query_reason;   //查询原因

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

    public String getQuery_dateTime() {
        return query_dateTime;
    }

    public void setQuery_dateTime(String query_dateTime) {
        this.query_dateTime = query_dateTime;
    }

    public String getQuery_operator() {
        return query_operator;
    }

    public void setQuery_operator(String query_operator) {
        this.query_operator = query_operator;
    }

    public String getQuery_reason() {
        return query_reason;
    }

    public void setQuery_reason(String query_reason) {
        this.query_reason = query_reason;
    }

    @Override
    public String toString() {
        return "QueryInformationDetail{" +
                "mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", query_dateTime='" + query_dateTime + '\'' +
                ", query_operator='" + query_operator + '\'' +
                ", query_reason='" + query_reason + '\'' +
                '}';
    }
}
