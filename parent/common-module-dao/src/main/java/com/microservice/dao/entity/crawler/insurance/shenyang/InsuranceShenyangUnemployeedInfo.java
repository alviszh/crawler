package com.microservice.dao.entity.crawler.insurance.shenyang;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/21.
 */
@Entity
@Table(name="insurance_shenyang_unemployeeinfo")
public class InsuranceShenyangUnemployeedInfo extends IdEntity{

    private String idCardNum;	     //职工编号
    private String insuranceCode;	 //社保编码
    private String name;             //姓名
    private String payTime;          //缴费时期
    private String personalIdentity; //个人身份
    private String payState;	     //缴费标志
    private String taskId;
    private String insuranceType;   //失业保险

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPersonalIdentity() {
        return personalIdentity;
    }

    public void setPersonalIdentity(String personalIdentity) {
        this.personalIdentity = personalIdentity;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
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
        return "InsuranceShenyangUnemployeedInfo{" +
                "idCardNum='" + idCardNum + '\'' +
                ", insuranceCode='" + insuranceCode + '\'' +
                ", name='" + name + '\'' +
                ", payTime='" + payTime + '\'' +
                ", personalIdentity='" + personalIdentity + '\'' +
                ", payState='" + payState + '\'' +
                ", taskId='" + taskId + '\'' +
                ", insuranceType='" + insuranceType + '\'' +
                '}';
    }
}
