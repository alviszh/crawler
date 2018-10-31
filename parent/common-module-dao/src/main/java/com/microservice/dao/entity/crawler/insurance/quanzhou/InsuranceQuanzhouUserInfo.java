package com.microservice.dao.entity.crawler.insurance.quanzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author zhangyongjie
 * @create 2017-09-19 14:13
 * @Desc 泉州社保用户信息实体
 */
@Entity
@Table(name = "insurance_quanzhou_userinfo",indexes = {@Index(name = "index_insurance_quanzhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceQuanzhouUserInfo extends IdEntity {
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
     * 通讯地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String postal;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 社保卡号
     */
    private String insuranceNumber;

    /**
     * 当前状态
     */
    private String currentStatus;


    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public InsuranceQuanzhouUserInfo(String taskid, String name, String idNumber, String address, String postal, String email, String insuranceNumber, String currentStatus) {
        this.taskid = taskid;
        this.name = name;
        this.idNumber = idNumber;
        this.address = address;
        this.postal = postal;
        this.email = email;
        this.insuranceNumber = insuranceNumber;
        this.currentStatus = currentStatus;
    }
}
