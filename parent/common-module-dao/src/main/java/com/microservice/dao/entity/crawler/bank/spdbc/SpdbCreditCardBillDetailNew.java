package com.microservice.dao.entity.crawler.bank.spdbc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 浦发银行信用卡
 * 对账单明细
 * Created by zmy on 2017/12/5.
 */
@Entity
@Table(name="spdb_creditcard_newbilldetail")
public class SpdbCreditCardBillDetailNew extends IdEntity implements Serializable{
    private static final long serialVersionUID = -8914394761468900424L;

    private String taskid;
    private String tradeDate;   //交易日期
    private String tallyDate;   //记账日期
    private String tranSummary;    //交易摘要
    private String transSum;    //交易金额(RMB)
    private String cardNum;     //交易卡号(后四位)

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }


    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTallyDate() {
        return tallyDate;
    }

    public void setTallyDate(String tallyDate) {
        this.tallyDate = tallyDate;
    }

    public String getTranSummary() {
        return tranSummary;
    }

    public void setTranSummary(String tranSummary) {
        this.tranSummary = tranSummary;
    }

    public String getTransSum() {
        return transSum;
    }

    public void setTransSum(String transSum) {
        this.transSum = transSum;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }


    @Override
    public String toString() {
        return "SpdbCreditCardBillDetail{" +
                "taskid='" + taskid + '\'' +
                ", tradeDate='" + tradeDate + '\'' +
                ", tallyDate='" + tallyDate + '\'' +
                ", tranSummary='" + tranSummary + '\'' +
                ", transSum='" + transSum + '\'' +
                ", cardNum='" + cardNum + '\'' +
                '}';
    }
}
