package com.microservice.dao.entity.crawler.housing.baoshan;


import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 宝山市公积金明细账信息
 * @author: zmy
 * @date:
 */
@Entity
@Table(name = "housing_baoshan_detailaccount")
public class HousingBaoShanDetailAccount extends IdEntity implements Serializable {

    private String taskid;
    //	交易日期
    private String transdate;
    //	发生额
    private String  amount;
    //	余额
    private String balance;
    //	摘要
    private String summary;
    //	开始年月
    private String begindate;
    //	截止日期
    private String enddate;

    public String getTaskid() {
        return taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    public String getTransdate() {
        return transdate;
    }
    public void setTransdate(String transdate) {
        this.transdate = transdate;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}