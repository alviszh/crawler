package com.microservice.dao.entity.crawler.insurance.dandong;


import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "insurance_dandong_user_info")
public class InsuranceDandongUserInfo extends IdEntity {
    //    姓      名：
    private String userNama;
    //    身份证号：
    private String userIdNum;
    //    参保状态：
    private String insuranceStatus;
    //    照片状态：
    private String photoStatus;
    //    数据审验状态：
    private String dataStatus;
    //    照片信息：
    private String photoInfo;
    //    制卡状态：
    private String cardStatus;
    //    社保卡号：
    private String insuranceCardNum;
    //    卡地址：
    private String cardAddress;

    public InsuranceDandongUserInfo(String userNama, String userIdNum, String insuranceStatus, String photoStatus, String dataStatus, String photoInfo, String cardStatus, String insuranceCardNum, String cardAddress, String taskid) {
        this.userNama = userNama;
        this.userIdNum = userIdNum;
        this.insuranceStatus = insuranceStatus;
        this.photoStatus = photoStatus;
        this.dataStatus = dataStatus;
        this.photoInfo = photoInfo;
        this.cardStatus = cardStatus;
        this.insuranceCardNum = insuranceCardNum;
        this.cardAddress = cardAddress;
        this.taskid = taskid;
    }

    public InsuranceDandongUserInfo() {
    }

    private String taskid; // 任务ID

    public String getUserNama() {
        return userNama;
    }

    public void setUserNama(String userNama) {
        this.userNama = userNama;
    }

    public String getUserIdNum() {
        return userIdNum;
    }

    public void setUserIdNum(String userIdNum) {
        this.userIdNum = userIdNum;
    }

    public String getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(String insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public String getPhotoStatus() {
        return photoStatus;
    }

    public void setPhotoStatus(String photoStatus) {
        this.photoStatus = photoStatus;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(String photoInfo) {
        this.photoInfo = photoInfo;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getInsuranceCardNum() {
        return insuranceCardNum;
    }

    public void setInsuranceCardNum(String insuranceCardNum) {
        this.insuranceCardNum = insuranceCardNum;
    }

    public String getCardAddress() {
        return cardAddress;
    }

    public void setCardAddress(String cardAddress) {
        this.cardAddress = cardAddress;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Override
    public String toString() {
        return "InsuranceDandongUserInfo{" +
                "userNama='" + userNama + '\'' +
                ", userIdNum='" + userIdNum + '\'' +
                ", insuranceStatus='" + insuranceStatus + '\'' +
                ", photoStatus='" + photoStatus + '\'' +
                ", dataStatus='" + dataStatus + '\'' +
                ", photoInfo='" + photoInfo + '\'' +
                ", cardStatus='" + cardStatus + '\'' +
                ", insuranceCardNum='" + insuranceCardNum + '\'' +
                ", cardAddress='" + cardAddress + '\'' +
                ", taskid='" + taskid + '\'' +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }
}
