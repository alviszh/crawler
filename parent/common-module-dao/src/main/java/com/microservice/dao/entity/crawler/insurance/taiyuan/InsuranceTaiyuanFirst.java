package com.microservice.dao.entity.crawler.insurance.taiyuan;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Mu on 2017/9/30.
 */
@Entity
@Table(name="insurance_taiyuan_first")
public class InsuranceTaiyuanFirst extends IdEntity{
    private String personNum ;               //个人编号
    private String companyNum;               //单位编号
    private String companyName;              //单位名称
    private String insuranceType;            //险种类型
    private String payStatus;                //人员缴费状态
    private String startTiem;                //开始日期
    private String insuranceParticipantType;//参保人类型
    private String taskId;

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getStartTiem() {
        return startTiem;
    }

    public void setStartTiem(String startTiem) {
        this.startTiem = startTiem;
    }

    public String getInsuranceParticipantType() {
        return insuranceParticipantType;
    }

    public void setInsuranceParticipantType(String insuranceParticipantType) {
        this.insuranceParticipantType = insuranceParticipantType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "InsuranceTaiyuanFirst{" +
                "\npersonNum='" + personNum + '\'' +
                "\n, companyNum='" + companyNum + '\'' +
                "\n, companyName='" + companyName + '\'' +
                "\n, insuranceType='" + insuranceType + '\'' +
                "\n, payStatus='" + payStatus + '\'' +
                "\n, startTiem='" + startTiem + '\'' +
                "\n, insuranceParticipantType='" + insuranceParticipantType + '\'' +
                "\n, taskId='" + taskId + '\'' +
                '}';
    }
}
