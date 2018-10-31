package com.crawler.report.json.daihu.pbccrc;

import java.io.Serializable;

/**
 * 其他贷款记录明细
 * Created by zmy on 2018/6/28.
 */
public class OtherLoan implements Serializable{

    private String reportId;    //报告id
    private String queryId;     //查询编号
    private String releaser;     //发放机构
    private String releaseDate;     //发放日期
    private String releaseCount;     //发放金额
    private String accountType;     //账户类型
    private String loanType;     //贷款类型
    private String deadline;     //到期日期
    private String dueDate;     //截至日期
    private String isDelq;     //是否逾期
    private String isOverdue;     //贷款是否有逾期
    private String balance;     //余额
    private String delql5yAmt;     //近五年内逾期数
    private String delql5y90dayAmt;     //近五年内逾期超过90天数
    private String settleDate;     //结清日期
    private String overdueAmount;     //逾期金额
    private String status;     //状态
    private String isSettle;     //贷款是否已结清
    private String badDebts;     //是否已变成呆账
    private String remainingSum;     //呆账金额

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

    public String getReleaser() {
        return releaser;
    }

    public void setReleaser(String releaser) {
        this.releaser = releaser;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(String releaseCount) {
        this.releaseCount = releaseCount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getIsDelq() {
        return isDelq;
    }

    public void setIsDelq(String isDelq) {
        this.isDelq = isDelq;
    }

    public String getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(String isOverdue) {
        this.isOverdue = isOverdue;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDelql5yAmt() {
        return delql5yAmt;
    }

    public void setDelql5yAmt(String delql5yAmt) {
        this.delql5yAmt = delql5yAmt;
    }

    public String getDelql5y90dayAmt() {
        return delql5y90dayAmt;
    }

    public void setDelql5y90dayAmt(String delql5y90dayAmt) {
        this.delql5y90dayAmt = delql5y90dayAmt;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(String isSettle) {
        this.isSettle = isSettle;
    }

    public String getBadDebts() {
        return badDebts;
    }

    public void setBadDebts(String badDebts) {
        this.badDebts = badDebts;
    }

    public String getRemainingSum() {
        return remainingSum;
    }

    public void setRemainingSum(String remainingSum) {
        this.remainingSum = remainingSum;
    }

    public OtherLoan() {
    }

    @Override
    public String toString() {
        return "OtherLoan{" +
                "reportId='" + reportId + '\'' +
                ", queryId='" + queryId + '\'' +
                ", releaser='" + releaser + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseCount='" + releaseCount + '\'' +
                ", accountType='" + accountType + '\'' +
                ", loanType='" + loanType + '\'' +
                ", deadline='" + deadline + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", isDelq='" + isDelq + '\'' +
                ", isOverdue='" + isOverdue + '\'' +
                ", balance='" + balance + '\'' +
                ", delql5yAmt='" + delql5yAmt + '\'' +
                ", delql5y90dayAmt='" + delql5y90dayAmt + '\'' +
                ", settleDate='" + settleDate + '\'' +
                ", overdueAmount='" + overdueAmount + '\'' +
                ", status='" + status + '\'' +
                ", isSettle='" + isSettle + '\'' +
                ", badDebts='" + badDebts + '\'' +
                ", remainingSum='" + remainingSum + '\'' +
                '}';
    }
}
