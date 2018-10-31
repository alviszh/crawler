package com.crawler.report.json.jiemo.pbccrc;

import java.io.Serializable;

/**
 * 贷款明细
 * Created by zmy on 2018/6/23.
 */
public class CreditLoanInfo implements Serializable{

    private  String  queryID;   //编号
    private  String  status;   //账户状态
    private  String  loanType;   //贷款种类
    private  String  financeCorporationBankName;   //发放贷款机构名称，发放贷款银行的名称
    private  String  isDelq;   //是否发生过逾期
    private  String  isCloseout;   //贷款是否结清，购房贷款是否已结清
    private  String  transfer;   //是否已转出
    private  String  badDebts;   //是否已变成呆账
    private  String  releasedate;   //发放日期
    private  String  deadline;   //到期日期
    private  String  dueDate;   //截至年月
    private  String  releaseCount;   //贷款合同金额
    private  String  balance;   //贷款余额
    private  String  overDueAmount;   //逾期金额
    private  String  delqL5yAmt;   //最近五年逾期次数
    private  String  delqL5y90dayAmt;   //最近五年90天以上逾期次数
    private  String  settleDate;   //结清年月

    public String getQueryID() {
        return queryID;
    }

    public void setQueryID(String queryID) {
        this.queryID = queryID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getFinanceCorporationBankName() {
        return financeCorporationBankName;
    }

    public void setFinanceCorporationBankName(String financeCorporationBankName) {
        this.financeCorporationBankName = financeCorporationBankName;
    }

    public String getIsDelq() {
        return isDelq;
    }

    public void setIsDelq(String isDelq) {
        this.isDelq = isDelq;
    }

    public String getIsCloseout() {
        return isCloseout;
    }

    public void setIsCloseout(String isCloseout) {
        this.isCloseout = isCloseout;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getBadDebts() {
        return badDebts;
    }

    public void setBadDebts(String badDebts) {
        this.badDebts = badDebts;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
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

    public String getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(String releaseCount) {
        this.releaseCount = releaseCount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOverDueAmount() {
        return overDueAmount;
    }

    public void setOverDueAmount(String overDueAmount) {
        this.overDueAmount = overDueAmount;
    }

    public String getDelqL5yAmt() {
        return delqL5yAmt;
    }

    public void setDelqL5yAmt(String delqL5yAmt) {
        this.delqL5yAmt = delqL5yAmt;
    }

    public String getDelqL5y90dayAmt() {
        return delqL5y90dayAmt;
    }

    public void setDelqL5y90dayAmt(String delqL5y90dayAmt) {
        this.delqL5y90dayAmt = delqL5y90dayAmt;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    @Override
    public String toString() {
        return "CreditLoanInfo{" +
                "queryID='" + queryID + '\'' +
                ", status='" + status + '\'' +
                ", loanType='" + loanType + '\'' +
                ", financeCorporationBankName='" + financeCorporationBankName + '\'' +
                ", isDelq='" + isDelq + '\'' +
                ", isCloseout='" + isCloseout + '\'' +
                ", transfer='" + transfer + '\'' +
                ", badDebts='" + badDebts + '\'' +
                ", releasedate='" + releasedate + '\'' +
                ", deadline='" + deadline + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", releaseCount='" + releaseCount + '\'' +
                ", balance='" + balance + '\'' +
                ", overDueAmount='" + overDueAmount + '\'' +
                ", delqL5yAmt='" + delqL5yAmt + '\'' +
                ", delqL5y90dayAmt='" + delqL5y90dayAmt + '\'' +
                ", settleDate='" + settleDate + '\'' +
                '}';
    }
    public CreditLoanInfo() {
    }
}
