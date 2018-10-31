package com.microservice.dao.entity.crawler.insurance.shenyang;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Mu on 2017/9/18.
 */

// 由于提供的账户对应人员未更改单位，没跳过槽？，不知道个人信息是否会有多个。
//养老保险个人信息
@Entity
@Table(name="insurance_shenyang_userinfo")
public class InsuranceShenyangUserInfo extends IdEntity {
    //姓名
    private String name;
    //职工编号
    private String employeeId;
    //单位编码
    private String companyId;
    //单位名称
    private String companyName;
    //出生日期
    private String birthDay;
    //参保状态
    private String insuredState;

    private String taskId;

//    private String category; // 户口性质

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getInsuredState() {
        return insuredState;
    }

    public void setInsuredState(String insuredState) {
        this.insuredState = insuredState;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    @Override
    public String toString() {
        return "InsuranceShenyangUserInfo{" +
                "name='" + name + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", insuredState='" + insuredState + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
