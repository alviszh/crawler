package com.microservice.dao.entity.crawler.insurance.wenzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 养老保险流水
 * Created by Administrator on 2017/9/22.
 */
@Entity
@Table(name="insurance_wenzhou_pension_paywater")
public class InsuranceWenzhouPensionPaywater  extends IdEntity {
    private String payMonth;	        //缴费年月
    private String payCompany;	        //缴费单位
    private String payBase;	            //缴费基数
    private String personalPayMoney;	//个人缴费金额
    private String transferStatus;	    //缴费到账情况
    private String taskId;
    private String payPeriod;            //业务期
    private String payType;              //缴费类型
    private String govSubsidy;           //政府补贴金额
    private String govPay;               //政府代缴金额
    private String insuranceType;        //保险类型

    public String getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(String payMonth) {
        this.payMonth = payMonth;
    }

    public String getPayCompany() {
        return payCompany;
    }

    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }

    public String getPayBase() {
        return payBase;
    }

    public void setPayBase(String payBase) {
        this.payBase = payBase;
    }

    public String getPersonalPayMoney() {
        return personalPayMoney;
    }

    public void setPersonalPayMoney(String personalPayMoney) {
        this.personalPayMoney = personalPayMoney;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getGovSubsidy() {
        return govSubsidy;
    }

    public void setGovSubsidy(String govSubsidy) {
        this.govSubsidy = govSubsidy;
    }

    public String getGovPay() {
        return govPay;
    }

    public void setGovPay(String govPay) {
        this.govPay = govPay;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    @Override
    public String toString() {
        return "InsuranceWenzhouPensionPaywater{" +
                "payMonth='" + payMonth + '\'' +
                ", payCompany='" + payCompany + '\'' +
                ", payBase='" + payBase + '\'' +
                ", personalPayMoney='" + personalPayMoney + '\'' +
                ", transferStatus='" + transferStatus + '\'' +
                ", taskId='" + taskId + '\'' +
                ", payPeriod='" + payPeriod + '\'' +
                ", payType='" + payType + '\'' +
                ", govSubsidy='" + govSubsidy + '\'' +
                ", govPay='" + govPay + '\'' +
                ", insuranceType='" + insuranceType + '\'' +
                '}';
    }
}
