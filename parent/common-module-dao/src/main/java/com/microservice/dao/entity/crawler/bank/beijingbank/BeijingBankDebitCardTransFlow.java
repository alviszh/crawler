package com.microservice.dao.entity.crawler.bank.beijingbank;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zmy on 2018/3/12.
 */

@Entity
@Table(name="beijingbank_debitcard_transflow")
public class BeijingBankDebitCardTransFlow extends IdEntity{
    private String taskid;
    private String acctNo;  //网银账户
    private String pointsAccount;   // 分账号
    private String bankName;    // 开户行
    private String num;         //序号
    private String transTime;   //交易日期
    private String summary;     //摘要
    private String transType;   //收入/支出
    private String transAmount; //交易金额
    private String balance;     //余额
    private String transPlace;   //交易地点

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

    public String getPointsAccount() {
        return pointsAccount;
    }

    public void setPointsAccount(String pointsAccount) {
        this.pointsAccount = pointsAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransPlace() {
        return transPlace;
    }

    public void setTransPlace(String transPlace) {
        this.transPlace = transPlace;
    }

    @Override
    public String toString() {
        return "BeijingBankDebitCardTransFlow{" +
                "taskid='" + taskid + '\'' +
                ", acctNo='" + acctNo + '\'' +
                ", pointsAccount='" + pointsAccount + '\'' +
                ", bankName='" + bankName + '\'' +
                ", num='" + num + '\'' +
                ", transTime='" + transTime + '\'' +
                ", summary='" + summary + '\'' +
                ", transType='" + transType + '\'' +
                ", transAmount='" + transAmount + '\'' +
                ", balance='" + balance + '\'' +
                ", transPlace='" + transPlace + '\'' +
                '}';
    }
}
