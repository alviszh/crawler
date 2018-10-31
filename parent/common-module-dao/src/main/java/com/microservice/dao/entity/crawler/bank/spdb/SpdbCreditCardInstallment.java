package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 账单分期记录
 * Created by zmy on 2017/12/5.
 */
@Entity
@Table(name="spdb_creditcard_installment")
public class SpdbCreditCardInstallment extends IdEntity implements Serializable{
    private static final long serialVersionUID = -39073961778943057L;

    private String taskid;
    private String orderType;   //订单类型
    private String productName;    //产品名称
    private String money;           //金额
    private String installmentNum;  //分期期数
    private String applicatTime;    //申请时间
    private String repayAmountMonth ;//每月还款金额
    private String feeTotal;        //总手续费

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getInstallmentNum() {
        return installmentNum;
    }

    public void setInstallmentNum(String installmentNum) {
        this.installmentNum = installmentNum;
    }

    public String getApplicatTime() {
        return applicatTime;
    }

    public void setApplicatTime(String applicatTime) {
        this.applicatTime = applicatTime;
    }

    public String getRepayAmountMonth() {
        return repayAmountMonth;
    }

    public void setRepayAmountMonth(String repayAmountMonth) {
        this.repayAmountMonth = repayAmountMonth;
    }

    public String getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(String feeTotal) {
        this.feeTotal = feeTotal;
    }

    @Override
    public String toString() {
        return "SpdbCreditCardInstallment{" +
                "taskid='" + taskid + '\'' +
                ", orderType='" + orderType + '\'' +
                ", productName='" + productName + '\'' +
                ", money='" + money + '\'' +
                ", installmentNum='" + installmentNum + '\'' +
                ", applicatTime='" + applicatTime + '\'' +
                ", repayAmountMonth='" + repayAmountMonth + '\'' +
                ", feeTotal='" + feeTotal + '\'' +
                '}';
    }
}
