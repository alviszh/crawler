package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 交易明细
 */
@Entity
@Table(name = "spdb_debitcard_transflow")
public class SpdbDebitCardTransFlow extends IdEntity implements Serializable{
    private static final long serialVersionUID = 4112332026704232115L;

    private String taskid;
    private String no;        //序号
    private String transTime;   //交易时间
    private String summary;     //交易摘要
    private String depositAmount;       //存入金额
    private String takeAmount;      //取出金额
    private String balance;//当时余额
    private String acctNo; //卡号

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getTakeAmount() {
        return takeAmount;
    }

    public void setTakeAmount(String takeAmount) {
        this.takeAmount = takeAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    @Override
    public String toString() {
        return "SpdbDebitCardTransFlow{" +
                "taskid='" + taskid + '\'' +
                ", no='" + no + '\'' +
                ", transTime='" + transTime + '\'' +
                ", summary='" + summary + '\'' +
                ", depositAmount='" + depositAmount + '\'' +
                ", takeAmount='" + takeAmount + '\'' +
                ", balance='" + balance + '\'' +
                ", acctNo='" + acctNo + '\'' +
                '}';
    }
}
