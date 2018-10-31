package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 整存整取定期存款
 * Created by zmy on 2017/11/28.
 */
@Entity
@Table(name = "spdb_debitcard_regular")
public class SpdbDebitCardRegular extends IdEntity implements Serializable{
    private static final long serialVersionUID = -1578086407689135504L;

    private String taskid;
    private String acctNo;       //卡号
    private String savingsType; //储蓄种类
    private String sn;           //顺序号
    private String currency;    //币种
    private String currencyType;//钞汇标志
    private String balance;     //当前余额
    private String dueDate;     //到期日
    private String accountStatus;//账户状态
    private String userName;    //户名
    private String userNo;      //客户号
    private String accountType; //账户类型
    private String saveTerm;    //存期
    private String  rate;   //利率
    private String startData;//起息日期

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getSavingsType() {
        return savingsType;
    }

    public void setSavingsType(String savingsType) {
        this.savingsType = savingsType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    @Override
    public String toString() {
        return "SpdbDebitCardRegular{" +
                "taskid='" + taskid + '\'' +
                ", acctNo='" + acctNo + '\'' +
                ", savingsType='" + savingsType + '\'' +
                ", sn='" + sn + '\'' +
                ", currency='" + currency + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", balance='" + balance + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", userName='" + userName + '\'' +
                ", userNo='" + userNo + '\'' +
                ", accountType='" + accountType + '\'' +
                ", saveTerm='" + saveTerm + '\'' +
                ", rate='" + rate + '\'' +
                ", startData='" + startData + '\'' +
                '}';
    }
}
