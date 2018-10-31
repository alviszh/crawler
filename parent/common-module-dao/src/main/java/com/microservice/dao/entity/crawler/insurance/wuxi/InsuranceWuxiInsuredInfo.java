package com.microservice.dao.entity.crawler.insurance.wuxi;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhangyongjie
 * @create 2017-09-26 17:18
 * @Desc
 */
@Entity
@Table(name="insurance_wuxi_insuredinfo")
public class InsuranceWuxiInsuredInfo extends IdEntity {
    /**
     * taskid  uuid 前端通过uuid访问状态结果
     */
    private String taskid;

    /**
     * 个人编码
     */
    private String personalCode;
    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 险种类型
     */
    private String insuranceType;

    /**
     * 参保状态
     */
    private String insuredStatus;

    /**
     * 参保日期
     */
    private String insuredDate;
    /**
     * 单位编号
     */
    private String orgNumber;

    /**
     * 单位类型
     */
    private String orgType;

    /**
     * 单位名称
     */
    private String orgName;

    /**
     * 连续缴费月数
     */
    private String continuityPaymentMonths;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuredStatus() {
        return insuredStatus;
    }

    public void setInsuredStatus(String insuredStatus) {
        this.insuredStatus = insuredStatus;
    }

    public String getInsuredDate() {
        return insuredDate;
    }

    public void setInsuredDate(String insuredDate) {
        this.insuredDate = insuredDate;
    }

    public String getOrgNumber() {
        return orgNumber;
    }

    public void setOrgNumber(String orgNumber) {
        this.orgNumber = orgNumber;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getContinuityPaymentMonths() {
        return continuityPaymentMonths;
    }

    public void setContinuityPaymentMonths(String continuityPaymentMonths) {
        this.continuityPaymentMonths = continuityPaymentMonths;
    }

    public InsuranceWuxiInsuredInfo() {
    }
}
