package com.microservice.dao.entity.crawler.insurance.wenzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/22.
 */
@Entity
@Table(name="insurance_wenzhou_userinfo")
public class InsuranceWenzhouUserinfo  extends IdEntity {
    private String name;        //姓名
    private String idnum;       //身份证号
    private String nation;      //民族
    private String gender;      //性别
    private String phoneNum;    //电话号码
    private String insuranceCity;	        //社保缴纳城市
    private String address;//居住地址
    private String email;
    private String taskId;      //

    @Column(columnDefinition="TEXT")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getInsuranceCity() {
        return insuranceCity;
    }

    public void setInsuranceCity(String insuranceCity) {
        this.insuranceCity = insuranceCity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "InsuranceWenzhouUserinfo{" +
                "name='" + name + '\'' +
                ", idnum='" + idnum + '\'' +
                ", nation='" + nation + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", insuranceCity='" + insuranceCity + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
