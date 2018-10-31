package com.microservice.dao.entity.crawler.insurance.shenyang;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Mu on 2017/9/18.
 */
@Entity
@Table(name="insurance_shenyang_paymentdetails")
public class InsuranceShenyangPaymentDetailsEachyear extends IdEntity {
    //职工编号
    private String employeeId;
    //单位编号
    private String companyId;
    //姓名
    private String employeeName;
    //缴费年月
    private String payMonth;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(String payMonth) {
        this.payMonth = payMonth;
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
        return "InsuranceShenyangPaymentDetailsEachyear{" +
                "employeeId='" + employeeId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", payMonth='" + payMonth + '\'' +
                ", personalAcountmoney='" + personalAcountmoney + '\'' +
                ", taskId='" + taskId + '\'' +
                ", insuranceType='" + insuranceType + '\'' +
                '}';
    }
}
