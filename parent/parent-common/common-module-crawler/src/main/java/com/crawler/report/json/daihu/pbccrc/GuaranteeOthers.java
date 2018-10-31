package com.crawler.report.json.daihu.pbccrc;

import java.io.Serializable;

/**
 * 为他人担保记录明细
 * Created by zmy on 2018/6/28.
 */
public class GuaranteeOthers implements Serializable{

    private String reportId;    //报告id
    private String guaranteeNumber;    //为他人担保笔数

    public GuaranteeOthers() {
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getGuaranteeNumber() {
        return guaranteeNumber;
    }

    public void setGuaranteeNumber(String guaranteeNumber) {
        this.guaranteeNumber = guaranteeNumber;
    }

    @Override
    public String toString() {
        return "GuaranteeOthers{" +
                "reportId='" + reportId + '\'' +
                ", guaranteeNumber='" + guaranteeNumber + '\'' +
                '}';
    }
}
