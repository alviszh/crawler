package com.microservice.dao.entity.crawler.insurance.taiyuan;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/30. 职工缴费信息
 */
@Entity
@Table(name="insurance_taiyuan_staffwater")
public class InsuranceTaiyuanStaffWater  extends IdEntity {
    private String personNum;            //个人编号
    private String companyName;          //公司名称
    private String insuranceType;        //险种类型
    private String payMonth;             //做账期号
    private String payType;              //缴费类型
    private String insurancePeopleType;  //参保人类型
    private String staffStatus;          //人员状态
    private String payBase;              //缴费基数
    private String personMoney;          //个人缴费划账户金额
    private String companyMoney;         //单位缴费划账户金额
    private String months;               //缴费月数
    private String taskId;

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(String payMonth) {
        this.payMonth = payMonth;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getInsurancePeopleType() {
        return insurancePeopleType;
    }

    public void setInsurancePeopleType(String insurancePeopleType) {
        this.insurancePeopleType = insurancePeopleType;
    }

    public String getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(String staffStatus) {
        this.staffStatus = staffStatus;
    }

    public String getPayBase() {
        return payBase;
    }

    public void setPayBase(String payBase) {
        this.payBase = payBase;
    }

    public String getPersonMoney() {
        return personMoney;
    }

    public void setPersonMoney(String personMoney) {
        this.personMoney = personMoney;
    }

    public String getCompanyMoney() {
        return companyMoney;
    }

    public void setCompanyMoney(String companyMoney) {
        this.companyMoney = companyMoney;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public InsuranceTaiyuanStaffWater() {
    }

    public InsuranceTaiyuanStaffWater(String personNum, String companyName, String insuranceType, String payMonth, String payType, String insurancePeopleType, String staffStatus, String payBase, String personMoney, String companyMoney, String months, String taskId) {
        this.personNum = personNum;
        this.companyName = companyName;
        this.insuranceType = insuranceType;
        this.payMonth = payMonth;
        this.payType = payType;
        this.insurancePeopleType = insurancePeopleType;
        this.staffStatus = staffStatus;
        this.payBase = payBase;
        this.personMoney = personMoney;
        this.companyMoney = companyMoney;
        this.months = months;
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "InsuranceTaiyuanStaffWaterRepository{" +
                "personNum='" + personNum + '\'' +
                ", companyName='" + companyName + '\'' +
                ", insuranceType='" + insuranceType + '\'' +
                ", payMonth='" + payMonth + '\'' +
                ", payType='" + payType + '\'' +
                ", insurancePeopleType='" + insurancePeopleType + '\'' +
                ", staffStatus='" + staffStatus + '\'' +
                ", payBase='" + payBase + '\'' +
                ", personMoney='" + personMoney + '\'' +
                ", companyMoney='" + companyMoney + '\'' +
                ", months='" + months + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
