package com.microservice.dao.entity.crawler.insurance.quanzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author zhangyongjie
 * @create 2017-09-21 14:22
 * @Desc
 */
@Entity
@Table(name = "insurance_quanzhou_injury",indexes = {@Index(name = "index_insurance_quanzhou_injury_taskid", columnList = "taskid")})
public class InsuranceQuanzhouInjury extends IdEntity {

    /**
     * taskid  uuid 前端通过uuid访问状态结果
     */
    private String taskid;

    /**
     * 建账年月
     */
    private String prepareMonthly;
    /**
     * 单位缴费金额
     */
    private String unitPaymentAmount;
    /**
     * 缴费基数
     */
    private String socialInsuranceBase;
    /**
     * 单位缴费比例
     */
    private String orgPayRatio;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPrepareMonthly() {
        return prepareMonthly;
    }

    public void setPrepareMonthly(String prepareMonthly) {
        this.prepareMonthly = prepareMonthly;
    }

    public String getUnitPaymentAmount() {
        return unitPaymentAmount;
    }

    public void setUnitPaymentAmount(String unitPaymentAmount) {
        this.unitPaymentAmount = unitPaymentAmount;
    }

    public String getSocialInsuranceBase() {
        return socialInsuranceBase;
    }

    public void setSocialInsuranceBase(String socialInsuranceBase) {
        this.socialInsuranceBase = socialInsuranceBase;
    }

    public String getOrgPayRatio() {
        return orgPayRatio;
    }

    public void setOrgPayRatio(String orgPayRatio) {
        this.orgPayRatio = orgPayRatio;
    }

    public InsuranceQuanzhouInjury() {
    }

    public InsuranceQuanzhouInjury(String taskid, String prepareMonthly, String unitPaymentAmount, String socialInsuranceBase, String orgPayRatio) {
        this.taskid = taskid;
        this.prepareMonthly = prepareMonthly;
        this.unitPaymentAmount = unitPaymentAmount;
        this.socialInsuranceBase = socialInsuranceBase;
        this.orgPayRatio = orgPayRatio;
    }
}
