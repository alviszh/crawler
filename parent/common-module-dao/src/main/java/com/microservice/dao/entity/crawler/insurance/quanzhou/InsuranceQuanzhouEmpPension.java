package com.microservice.dao.entity.crawler.insurance.quanzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author zhangyongjie
 * @create 2017-09-20 19:45
 * @Desc
 */
@Entity
@Table(name = "insurance_quanzhou_emppension",indexes = {@Index(name = "index_insurance_quanzhou_emppension_taskid", columnList = "taskid")})
public class InsuranceQuanzhouEmpPension extends IdEntity {
    /**
     * taskid  uuid 前端通过uuid访问状态结果
     */
    private String taskid;

    /**
     * 建账年月
     */
    private String prepareMonthly;

    /**
     * 单位名称
     */
    private String orgName;

    /**
     * 账目类型
     */
    private String accountType;

    /**
     * 个人缴费金额
     */
    private String individualPaymentAmount;

    /**
     * 单位缴费金额
     */
    private String unitPaymentAmount;

    /**
     * 划入账户金额
     */
    private String includedAccountAmount;

    /**
     * 缴费基数
     */
    private String socialInsuranceBase;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIndividualPaymentAmount() {
        return individualPaymentAmount;
    }

    public void setIndividualPaymentAmount(String individualPaymentAmount) {
        this.individualPaymentAmount = individualPaymentAmount;
    }

    public String getUnitPaymentAmount() {
        return unitPaymentAmount;
    }

    public void setUnitPaymentAmount(String unitPaymentAmount) {
        this.unitPaymentAmount = unitPaymentAmount;
    }

    public String getIncludedAccountAmount() {
        return includedAccountAmount;
    }

    public void setIncludedAccountAmount(String includedAccountAmount) {
        this.includedAccountAmount = includedAccountAmount;
    }

    public String getSocialInsuranceBase() {
        return socialInsuranceBase;
    }

    public void setSocialInsuranceBase(String socialInsuranceBase) {
        this.socialInsuranceBase = socialInsuranceBase;
    }

    public InsuranceQuanzhouEmpPension() {
    }

    public InsuranceQuanzhouEmpPension(String taskid, String prepareMonthly, String orgName, String accountType, String individualPaymentAmount, String unitPaymentAmount, String includedAccountAmount, String socialInsuranceBase) {
        this.taskid = taskid;
        this.prepareMonthly = prepareMonthly;
        this.orgName = orgName;
        this.accountType = accountType;
        this.individualPaymentAmount = individualPaymentAmount;
        this.unitPaymentAmount = unitPaymentAmount;
        this.includedAccountAmount = includedAccountAmount;
        this.socialInsuranceBase = socialInsuranceBase;
    }
}
