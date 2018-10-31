package com.microservice.dao.entity.crawler.insurance.quanzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author zhangyongjie
 * @create 2017-09-20 11:19
 * @Desc
 */
@Entity
@Table(name = "insurance_quanzhou_insuredinfo",indexes = {@Index(name = "index_insurance_quanzhou_insuredinfo_taskid", columnList = "taskid")})
public class InsuranceQuanzhouInsuredInfo extends IdEntity {
    /**
     * taskid  uuid 前端通过uuid访问状态结果
     */
    private String taskid;

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
     * 单位名称
     */
    private String orgName;

    /**
     * 地区
     */
    private String area;

    /**
     * 人员编号
     */
    private String personnelNumber;

    /**
     * 参保状态
     */
    private String insuredStatus;

    /**
     * 参保日期
     */
    private String insuredDate;

    /**
     * 年度缴费基数
     */
    private String annualPaymentBase;

    /**
     * 建立个人帐户年月
     */
    private String createAccountsTime;

    /**
     * 缴费状态
     */
    private String paymentStatus;

    /**
     * 工作状态
     */
    private String workStatus;

    /**
     * 数据更新时间
     */
    private String dataUpdateTime;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
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

    public String getAnnualPaymentBase() {
        return annualPaymentBase;
    }

    public void setAnnualPaymentBase(String annualPaymentBase) {
        this.annualPaymentBase = annualPaymentBase;
    }

    public String getCreateAccountsTime() {
        return createAccountsTime;
    }

    public void setCreateAccountsTime(String createAccountsTime) {
        this.createAccountsTime = createAccountsTime;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getDataUpdateTime() {
        return dataUpdateTime;
    }

    public void setDataUpdateTime(String dataUpdateTime) {
        this.dataUpdateTime = dataUpdateTime;
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

    public InsuranceQuanzhouInsuredInfo(String taskid, String insuranceType, String orgName, String area, String personnelNumber, String insuredStatus, String insuredDate, String createAccountsTime, String paymentStatus, String workStatus, String dataUpdateTime) {
        this.taskid = taskid;
        this.insuranceType = insuranceType;
        this.orgName = orgName;
        this.area = area;
        this.personnelNumber = personnelNumber;
        this.insuredStatus = insuredStatus;
        this.insuredDate = insuredDate;
        this.createAccountsTime = createAccountsTime;
        this.paymentStatus = paymentStatus;
        this.workStatus = workStatus;
        this.dataUpdateTime = dataUpdateTime;
    }

    public InsuranceQuanzhouInsuredInfo(String taskid, String insuranceType, String orgName, String area, String insuredDate, String annualPaymentBase, String workStatus, String dataUpdateTime) {
        this.taskid = taskid;
        this.insuranceType = insuranceType;
        this.orgName = orgName;
        this.area = area;
        this.insuredDate = insuredDate;
        this.annualPaymentBase = annualPaymentBase;
        this.workStatus = workStatus;
        this.dataUpdateTime = dataUpdateTime;
    }

    public InsuranceQuanzhouInsuredInfo() {
    }


}
