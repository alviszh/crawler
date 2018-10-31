package com.microservice.dao.entity.crawler.insurance.shenyang;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Mu on 2017/9/18.
 */
@Entity
@Table(name="insurance_shenyang_paymentpastyears")
public class InsuranceShenyangPaymentPastYears extends IdEntity {
    //职工编号
    private String employeeId;
    //姓名
    private String employeeName;
    //账户年度
    private String accountYear;
    //单位缴费月数
    private String companyPayMonths;
    //个人缴费月数
    private String personalPayMonths;
    //缴费工资基数
    private String payBase;
    //个人缴帐户金额

    private String personalAcountmoney;

    private String taskId;

    private String insuranceType;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAccountYear() {
        return accountYear;
    }

    public void setAccountYear(String accountYear) {
        this.accountYear = accountYear;
    }

    public String getCompanyPayMonths() {
        return companyPayMonths;
    }

    public void setCompanyPayMonths(String companyPayMonths) {
        this.companyPayMonths = companyPayMonths;
    }

    public String getPersonalPayMonths() {
        return personalPayMonths;
    }

    public void setPersonalPayMonths(String personalPayMonths) {
        this.personalPayMonths = personalPayMonths;
    }

    public String getPayBase() {
        return payBase;
    }

    public void setPayBase(String payBase) {
        this.payBase = payBase;
    }

    public String getPersonalAcountmoney() {
        return personalAcountmoney;
    }

    public void setPersonalAcountmoney(String personalAcountmoney) {
        this.personalAcountmoney = personalAcountmoney;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    @Override
    public String toString() {
        return "InsuranceShenyangPaymentPastYears{" +
                "employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", accountYear='" + accountYear + '\'' +
                ", companyPayMonths='" + companyPayMonths + '\'' +
                ", personalPayMonths='" + personalPayMonths + '\'' +
                ", payBase='" + payBase + '\'' +
                ", personalAcountmoney='" + personalAcountmoney + '\'' +
                ", taskId='" + taskId + '\'' +
                ", insuranceType='" + insuranceType + '\'' +
                '}';
    }
}
