package com.crawler.report.json.daihu.pbccrc;

import java.io.Serializable;

/**
 * 征信数据信息
 * Created by zmy on 2018/6/28.
 */
public class CompulsoryExecution implements Serializable{

    private String reportId;    //报告id
    private String queryId;     //查询编号;
    private String reportResult;    //报告结果
    private String reportUrl;       //报告地址

    public CompulsoryExecution() {
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getReportResult() {
        return reportResult;
    }

    public void setReportResult(String reportResult) {
        this.reportResult = reportResult;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    @Override
    public String toString() {
        return "CompulsoryExecution{" +
                "reportId='" + reportId + '\'' +
                ", queryId='" + queryId + '\'' +
                ", reportResult='" + reportResult + '\'' +
                ", reportUrl='" + reportUrl + '\'' +
                '}';
    }
}
