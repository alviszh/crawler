package com.microservice.dao.entity.crawler.insurance.wenzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/22.
 */
@Entity
@Table(name="insurance_wenzhou_insurancebasic")
public class InsuranceWenzhouInsuranceBasic  extends IdEntity {
    private String insuranceType;	        //保险类型
    private String insuranceCompanyName;	//公司名称（参保单位名称）
    private String insuranceNum    ;        //保险编号
    private String SocialInsuranceNum;	    //社会保障号
    private String insuranceStatus;	        //社保状态（参保状态）
    private String insurancePayStatus;      //社保状态（缴费状态）
    private String insuranceCity;	        //社保缴纳城市
    private String companyNum;              //所在公司编号
    private String userName;
    private String workStatus;
    private String comment;                      //备注
    private String taskId;

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(String insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public String getSocialInsuranceNum() {
        return SocialInsuranceNum;
    }

    public void setSocialInsuranceNum(String socialInsuranceNum) {
        SocialInsuranceNum = socialInsuranceNum;
    }

    public String getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(String insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public String getInsurancePayStatus() {
        return insurancePayStatus;
    }

    public void setInsurancePayStatus(String insurancePayStatus) {
        this.insurancePayStatus = insurancePayStatus;
    }

    public String getInsuranceCity() {
        return insuranceCity;
    }

    public void setInsuranceCity(String insuranceCity) {
        this.insuranceCity = insuranceCity;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "InsuranceWenzhouInsuranceBasic{" +
                "insuranceType='" + insuranceType + '\'' +
                ", insuranceCompanyName='" + insuranceCompanyName + '\'' +
                ", insuranceNum='" + insuranceNum + '\'' +
                ", SocialInsuranceNum='" + SocialInsuranceNum + '\'' +
                ", insuranceStatus='" + insuranceStatus + '\'' +
                ", insurancePayStatus='" + insurancePayStatus + '\'' +
                ", insuranceCity='" + insuranceCity + '\'' +
                ", companyNum='" + companyNum + '\'' +
                ", userName='" + userName + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", comment='" + comment + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
