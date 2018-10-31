package com.crawler.report.json.daihu.pbccrc;

import java.io.Serializable;

/**
 * 房贷记录明细
 * Created by zmy on 2018/6/28.
 */
public class HouseLoan implements Serializable{

    private String reportId;    //报告id
    private String queryId;     //查询编号
    private String bank;     //发卡银行
    private String releaseDate;     //发卡日期
    private String releaseCount;     //发放金额
    private String accountType;     //账户类型
    private String deadline;     //到期日期
    private String dueDate;     //截至日期
    private String isDelq;     //是否逾期
    private String isOverdue;     //购房贷款是否有逾期
    private String balance;     //余额
    private String overdueAmount;     //逾期金额
    private String delql5yAmt;     //近五年内逾期数
    private String delql5y90dayAmt;     //近五年内逾期超过90天数
    private String settleDate;     //结清日期
    private String isSettle;     //购房贷款是否已结清
    private String status;     //状态
    private String transfer;     //是否已转出
    private String transferDate;     //转出日期

    public HouseLoan() {
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
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

    public String getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
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

    public String getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(String isSettle) {
        this.isSettle = isSettle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    @Override
    public String toString() {
        return "HouseLoan{" +
                "reportId='" + reportId + '\'' +
                ", queryId='" + queryId + '\'' +
                ", bank='" + bank + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseCount='" + releaseCount + '\'' +
                ", accountType='" + accountType + '\'' +
                ", deadline='" + deadline + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", isDelq='" + isDelq + '\'' +
                ", isOverdue='" + isOverdue + '\'' +
                ", balance='" + balance + '\'' +
                ", overdueAmount='" + overdueAmount + '\'' +
                ", delql5yAmt='" + delql5yAmt + '\'' +
                ", delql5y90dayAmt='" + delql5y90dayAmt + '\'' +
                ", settleDate='" + settleDate + '\'' +
                ", isSettle='" + isSettle + '\'' +
                ", status='" + status + '\'' +
                ", transfer='" + transfer + '\'' +
                ", transferDate='" + transferDate + '\'' +
                '}';
    }
}
