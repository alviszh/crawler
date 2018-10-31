package com.crawler.report.json.jiemo.pbccrc;

import java.io.Serializable;

/**
 * 信贷记录信息-信息概要
 * Created by zmy on 2018/6/23.
 */
public class CreditRecordSummary implements Serializable{

    private String  creditType;    //账户类型
    private String  accountNum;    //账户数
    private String  unsettleUncancel;    //未结清、未销户账户数
    private String  overdueAccount;    //发生过逾期的账户数
    private String  overdueNinety;    //发生过90天以上逾期的账户数
    private String  guarantee;    //为他人担保笔数

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getUnsettleUncancel() {
        return unsettleUncancel;
    }

    public void setUnsettleUncancel(String unsettleUncancel) {
        this.unsettleUncancel = unsettleUncancel;
    }

    public String getOverdueAccount() {
        return overdueAccount;
    }

    public void setOverdueAccount(String overdueAccount) {
        this.overdueAccount = overdueAccount;
    }

    public String getOverdueNinety() {
        return overdueNinety;
    }

    public void setOverdueNinety(String overdueNinety) {
        this.overdueNinety = overdueNinety;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public CreditRecordSummary(String creditType, String accountNum, String unsettleUncancel, String overdueAccount, String overdueNinety, String guarantee) {
        this.creditType = creditType;
        this.accountNum = accountNum;
        this.unsettleUncancel = unsettleUncancel;
        this.overdueAccount = overdueAccount;
        this.overdueNinety = overdueNinety;
        this.guarantee = guarantee;
    }

    public CreditRecordSummary() {
    }

    @Override
    public String toString() {
        return "CreditRecordSummary{" +
                "creditType='" + creditType + '\'' +
                ", accountNum='" + accountNum + '\'' +
                ", unsettleUncancel='" + unsettleUncancel + '\'' +
                ", overdueAccount='" + overdueAccount + '\'' +
                ", overdueNinety='" + overdueNinety + '\'' +
                ", guarantee='" + guarantee + '\'' +
                '}';
    }
}
