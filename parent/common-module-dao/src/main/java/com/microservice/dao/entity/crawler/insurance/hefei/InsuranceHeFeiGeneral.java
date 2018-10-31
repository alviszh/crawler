package com.microservice.dao.entity.crawler.insurance.hefei;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 合肥市社保信息
 * @author zmy
 */
@Entity
@Table(name = "insurance_hefei_general")
public class InsuranceHeFeiGeneral extends IdEntity{

    private String taskid;
    private String compName;
    private String payDate;
    private String insurType;
    private String perPayRatio;
    private String perPayBasenum;
    private String compPayRatio;
    private String compPayAmount;
    private String perPayAmount;
    private String payAllMoney;
    private String isThismonthInaccount;
    private String pay_type;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getInsurType() {
        return insurType;
    }

    public void setInsurType(String insurType) {
        this.insurType = insurType;
    }

    public String getPerPayRatio() {
        return perPayRatio;
    }

    public void setPerPayRatio(String perPayRatio) {
        this.perPayRatio = perPayRatio;
    }

    public String getPerPayBasenum() {
        return perPayBasenum;
    }

    public void setPerPayBasenum(String perPayBasenum) {
        this.perPayBasenum = perPayBasenum;
    }

    public String getCompPayRatio() {
        return compPayRatio;
    }

    public void setCompPayRatio(String compPayRatio) {
        this.compPayRatio = compPayRatio;
    }

    public String getCompPayAmount() {
        return compPayAmount;
    }

    public void setCompPayAmount(String compPayAmount) {
        this.compPayAmount = compPayAmount;
    }

    public String getPerPayAmount() {
        return perPayAmount;
    }

    public void setPerPayAmount(String perPayAmount) {
        this.perPayAmount = perPayAmount;
    }

    public String getPayAllMoney() {
        return payAllMoney;
    }

    public void setPayAllMoney(String payAllMoney) {
        this.payAllMoney = payAllMoney;
    }

    public String getIsThismonthInaccount() {
        return isThismonthInaccount;
    }

    public void setIsThismonthInaccount(String isThismonthInaccount) {
        this.isThismonthInaccount = isThismonthInaccount;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public InsuranceHeFeiGeneral(String taskid, String compName, String payDate, String insurType, String perPayRatio, String perPayBasenum, String compPayRatio, String compPayAmount, String perPayAmount, String payAllMoney, String isThismonthInaccount, String pay_type) {
        this.taskid = taskid;
        this.compName = compName;
        this.payDate = payDate;
        this.insurType = insurType;
        this.perPayRatio = perPayRatio;
        this.perPayBasenum = perPayBasenum;
        this.compPayRatio = compPayRatio;
        this.compPayAmount = compPayAmount;
        this.perPayAmount = perPayAmount;
        this.payAllMoney = payAllMoney;
        this.isThismonthInaccount = isThismonthInaccount;
        this.pay_type = pay_type;
    }

    public InsuranceHeFeiGeneral() {
        super();
    }
}
