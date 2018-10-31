package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 账单信息概要
 * Created by zmy on 2017/12/5.
 */
@Entity
@Table(name="spdb_creditcard_billgeneral")
public class SpdbCreditCardBillGeneral extends IdEntity implements Serializable{
    private static final long serialVersionUID = -7023133529303996560L;

    private String taskid;
    private String billMonth;   //账单月份
    private String payAmount;   //本期应还款余额
    private String minPayAmount;   //本期最低还款额
    private String  dueDate;        //到期还款日
    private String cardNum;         //交易卡号(后四位)

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getMinPayAmount() {
        return minPayAmount;
    }

    public void setMinPayAmount(String minPayAmount) {
        this.minPayAmount = minPayAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    @Override
    public String toString() {
        return "SpdbCreditCardBillGeneral{" +
                "taskid='" + taskid + '\'' +
                ", billMonth='" + billMonth + '\'' +
                ", payAmount='" + payAmount + '\'' +
                ", minPayAmount='" + minPayAmount + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", cardNum='" + cardNum + '\'' +
                '}';
    }
}
