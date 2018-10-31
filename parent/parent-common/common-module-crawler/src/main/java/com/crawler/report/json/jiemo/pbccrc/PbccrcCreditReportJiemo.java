package com.crawler.report.json.jiemo.pbccrc;

import com.crawler.report.json.jiemo.pbccrc.*;

import java.io.Serializable;
import java.util.List;

/**
 *  征信报告JSON-借么
 * Created by zmy on 2018/6/23.
 */
public class PbccrcCreditReportJiemo implements Serializable {
    private CreditReportSimple creditReportSimple;  //基本信息
    private List<CreditRecordSummary> creditRecordSummaries;  //信贷记录信息
    private List<CreditCardInfo> creditCardInfo;  //信用卡明细
    private List<CreditLoanInfo> creditLoanInfo;  //贷款明细
    private List<CreditQueryRecord> creditQueryRecord;  //查询信息

    public CreditReportSimple getCreditReportSimple() {
        return creditReportSimple;
    }

    public void setCreditReportSimple(CreditReportSimple creditReportSimple) {
        this.creditReportSimple = creditReportSimple;
    }

    public List<CreditRecordSummary> getCreditRecordSummaries() {
        return creditRecordSummaries;
    }

    public void setCreditRecordSummaries(List<CreditRecordSummary> creditRecordSummaries) {
        this.creditRecordSummaries = creditRecordSummaries;
    }

    public List<CreditCardInfo> getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(List<CreditCardInfo> creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
    }

    public List<CreditLoanInfo> getCreditLoanInfo() {
        return creditLoanInfo;
    }

    public void setCreditLoanInfo(List<CreditLoanInfo> creditLoanInfo) {
        this.creditLoanInfo = creditLoanInfo;
    }

    public List<CreditQueryRecord> getCreditQueryRecord() {
        return creditQueryRecord;
    }

    public void setCreditQueryRecord(List<CreditQueryRecord> creditQueryRecord) {
        this.creditQueryRecord = creditQueryRecord;
    }

    @Override
    public String toString() {
        return "PbccrcCreditReportJiemo{" +
                "creditReportSimple=" + creditReportSimple +
                ", creditRecordSummaries=" + creditRecordSummaries +
                ", creditCardInfo=" + creditCardInfo +
                ", creditLoanInfo=" + creditLoanInfo +
                ", creditQueryRecord=" + creditQueryRecord +
                '}';
    }
}
