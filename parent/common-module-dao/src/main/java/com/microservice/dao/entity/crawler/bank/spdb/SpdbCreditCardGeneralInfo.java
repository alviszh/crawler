package com.microservice.dao.entity.crawler.bank.spdb;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 浦发银行信用卡
 * 账户基本信息
 * Created by zmy on 2017/12/5.
 */
@Entity
@Table(name="spdb_creditcard_generalinfo")
public class SpdbCreditCardGeneralInfo extends IdEntity implements Serializable{
    private static final long serialVersionUID = 9132819721834208833L;

    private String taskid;
    private String name;        //账户姓名
    private String today;       //今日日期
    private String account;     //账户
    private String creditLimit;  //信用额度
    private String essayLimit;        //取现额度
    private String canUseLimit;     //可用额度
    private String monthlyBillDay;    //每月账单日
    private String customerNum;         //客户号
    private String idType;    //证件类型
    private String idNum;   //证件号码

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getEssayLimit() {
        return essayLimit;
    }

    public void setEssayLimit(String essayLimit) {
        this.essayLimit = essayLimit;
    }

    public String getCanUseLimit() {
        return canUseLimit;
    }

    public void setCanUseLimit(String canUseLimit) {
        this.canUseLimit = canUseLimit;
    }

    public String getMonthlyBillDay() {
        return monthlyBillDay;
    }

    public void setMonthlyBillDay(String monthlyBillDay) {
        this.monthlyBillDay = monthlyBillDay;
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    @Override
    public String toString() {
        return "SpdbCreditCardGeneralInfo{" +
                "taskid='" + taskid + '\'' +
                ", name='" + name + '\'' +
                ", today='" + today + '\'' +
                ", account='" + account + '\'' +
                ", creditLimit='" + creditLimit + '\'' +
                ", essayLimit='" + essayLimit + '\'' +
                ", canUseLimit='" + canUseLimit + '\'' +
                ", monthlyBillDay='" + monthlyBillDay + '\'' +
                ", customerNum='" + customerNum + '\'' +
                ", idType='" + idType + '\'' +
                ", idNum='" + idNum + '\'' +
                '}';
    }
}
