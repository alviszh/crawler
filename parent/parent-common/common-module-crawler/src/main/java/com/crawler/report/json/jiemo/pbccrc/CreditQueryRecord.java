package com.crawler.report.json.jiemo.pbccrc;

import java.io.Serializable;

/**
 * 查询信息
 * Created by zmy on 2018/6/23.
 */
public class CreditQueryRecord implements Serializable{
    private String  queryId;   //编号
    private String  queryTime;   //查询日期
    private String  queryReason;   //查询原因

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getQueryReason() {
        return queryReason;
    }

    public void setQueryReason(String queryReason) {
        this.queryReason = queryReason;
    }

    public CreditQueryRecord() {
    }

    @Override
    public String toString() {
        return "CreditQueryRecord{" +
                "queryId='" + queryId + '\'' +
                ", queryTime='" + queryTime + '\'' +
                ", queryReason='" + queryReason + '\'' +
                '}';
    }
}
