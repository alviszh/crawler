package com.microservice.dao.entity.crawler.insurance.wuxi;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhangyongjie
 * @create 2017-09-26 19:30
 * @Desc
 */
@Entity
@Table(name = "insurance_wuxi_medical")
public class InsuranceWuxiMedical extends IdEntity {
    /**
     * taskid  uuid 前端通过uuid访问状态结果
     */
    private String taskid;

    /**
     * 年度
     */
    private String year;

    /**
     * 上年结转金额
     */
    private String amountFromLastYear;

    /**
     * 本年单位划拨
     */
    private String unitAllocationTheYear;

    /**
     * 本年帐户利息
     */
    private String interestTheYear;

    /**
     * 基本帐户余额
     */
    private String baseAccountBalance;

    /**
     * 企补账户余额
     */
    private String supplementaryAccountBalance;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAmountFromLastYear() {
        return amountFromLastYear;
    }

    public void setAmountFromLastYear(String amountFromLastYear) {
        this.amountFromLastYear = amountFromLastYear;
    }

    public String getUnitAllocationTheYear() {
        return unitAllocationTheYear;
    }

    public void setUnitAllocationTheYear(String unitAllocationTheYear) {
        this.unitAllocationTheYear = unitAllocationTheYear;
    }

    public String getInterestTheYear() {
        return interestTheYear;
    }

    public void setInterestTheYear(String interestTheYear) {
        this.interestTheYear = interestTheYear;
    }

    public String getBaseAccountBalance() {
        return baseAccountBalance;
    }

    public void setBaseAccountBalance(String baseAccountBalance) {
        this.baseAccountBalance = baseAccountBalance;
    }

    public String getSupplementaryAccountBalance() {
        return supplementaryAccountBalance;
    }

    public void setSupplementaryAccountBalance(String supplementaryAccountBalance) {
        this.supplementaryAccountBalance = supplementaryAccountBalance;
    }

    public InsuranceWuxiMedical() {
    }
}
