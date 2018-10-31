package com.crawler.report.json.daihu.pbccrc;

import java.io.Serializable;

/**
 * 信用卡记录明细
 * Created by zmy on 2018/6/28.
 */
public class CreditCard implements Serializable{

    private String reportId;    //报告id
    private String queryId;     //查询编号;
    private String bank;        //发行银行
    private String releaseDate;//发行日期
    private String cardType;    //卡类型
    private String accountType; //账户类型
    private String dueDate;     //截止日期
    private String creditAmt;   //信用额度
    private String usedAmt;     //已使用额度
    private String overDueAmount;   //逾期额度
    private String isDelq;          //是否逾期
    private String isOverdue;   //该账户是否有逾期记录
    private String delql5yAmt;  //近五年内逾期数
    private String delql5y90dayAmt; //近五年内逾期超过90天数
    private String isActive;        //是否激活
    private String status;          //状态
    private String isAccountcancel; //是否已销户
    private String balance;     //余额
    private String overdraftBalance;    //透支余额
    private String overdraftl5y60dayAmt;    //发生透支超过60天的次数
    private String badDebts;        //是否已变成呆账
    private String remainingSum;    //呆账金额

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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(String creditAmt) {
        this.creditAmt = creditAmt;
    }

    public String getUsedAmt() {
        return usedAmt;
    }

    public void setUsedAmt(String usedAmt) {
        this.usedAmt = usedAmt;
    }

    public String getOverDueAmount() {
        return overDueAmount;
    }

    public void setOverDueAmount(String overDueAmount) {
        this.overDueAmount = overDueAmount;
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsAccountcancel() {
        return isAccountcancel;
    }

    public void setIsAccountcancel(String isAccountcancel) {
        this.isAccountcancel = isAccountcancel;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOverdraftBalance() {
        return overdraftBalance;
    }

    public void setOverdraftBalance(String overdraftBalance) {
        this.overdraftBalance = overdraftBalance;
    }

    public String getOverdraftl5y60dayAmt() {
        return overdraftl5y60dayAmt;
    }

    public void setOverdraftl5y60dayAmt(String overdraftl5y60dayAmt) {
        this.overdraftl5y60dayAmt = overdraftl5y60dayAmt;
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

    public CreditCard() {
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "reportId='" + reportId + '\'' +
                ", queryId='" + queryId + '\'' +
                ", bank='" + bank + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", cardType='" + cardType + '\'' +
                ", accountType='" + accountType + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", creditAmt='" + creditAmt + '\'' +
                ", usedAmt='" + usedAmt + '\'' +
                ", overDueAmount='" + overDueAmount + '\'' +
                ", isDelq='" + isDelq + '\'' +
                ", isOverdue='" + isOverdue + '\'' +
                ", delql5yAmt='" + delql5yAmt + '\'' +
                ", delql5y90dayAmt='" + delql5y90dayAmt + '\'' +
                ", isActive='" + isActive + '\'' +
                ", status='" + status + '\'' +
                ", isAccountcancel='" + isAccountcancel + '\'' +
                ", balance='" + balance + '\'' +
                ", overdraftBalance='" + overdraftBalance + '\'' +
                ", overdraftl5y60dayAmt='" + overdraftl5y60dayAmt + '\'' +
                ", badDebts='" + badDebts + '\'' +
                ", remainingSum='" + remainingSum + '\'' +
                '}';
    }
}
