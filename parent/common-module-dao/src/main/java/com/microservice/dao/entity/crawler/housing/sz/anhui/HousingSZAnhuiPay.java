package com.microservice.dao.entity.crawler.housing.sz.anhui;


import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="housing_sz_anhui_pay")
public class HousingSZAnhuiPay extends IdEntity implements Serializable {
    private static final long serialVersionUID = -1482307603170981496L;

    private String taskid;
    private String date;    //日期
    private String summary;  //摘要
    private String outAccount;  //借方金额(出账)
    private String inAccount;   //贷方金额(入账)
    private String balance;     //余额

    public HousingSZAnhuiPay() {
    }

    public HousingSZAnhuiPay(String taskid, String date, String summary, String outAccount, String inAccount, String balance) {
        this.taskid = taskid;
        this.date = date;
        this.summary = summary;
        this.outAccount = outAccount;
        this.inAccount = inAccount;
        this.balance = balance;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOutAccount() {
        return outAccount;
    }

    public void setOutAccount(String outAccount) {
        this.outAccount = outAccount;
    }

    public String getInAccount() {
        return inAccount;
    }

    public void setInAccount(String inAccount) {
        this.inAccount = inAccount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

