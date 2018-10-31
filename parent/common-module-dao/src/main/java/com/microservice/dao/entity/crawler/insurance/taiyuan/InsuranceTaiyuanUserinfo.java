package com.microservice.dao.entity.crawler.insurance.taiyuan;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/9/30.
 */
@Entity
@Table(name="insurance_taiyuan_userinfo")
public class InsuranceTaiyuanUserinfo  extends IdEntity {
    private String gerenBianhao  ; // 个人编号
    private String name          ; //姓名
    private String gender        ; //性别
    private String idNum         ; //身份证号
    private String nation        ; //国籍
    private String birthDay      ; //生日
    private String staffStatus   ; //人员状态
    private String anmelden      ; //户口
    private String gerenShenfen  ; //个人身份
    private String address       ; //住址
    private String phoneNum      ; //电话
    private String yonggongStyle ; //用工类型
    private String peasant       ; //是否农民工
    private String taskId;

    public String getGerenBianhao() {
        return gerenBianhao;
    }

    public void setGerenBianhao(String gerenBianhao) {
        this.gerenBianhao = gerenBianhao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(String staffStatus) {
        this.staffStatus = staffStatus;
    }

    public String getAnmelden() {
        return anmelden;
    }

    public void setAnmelden(String anmelden) {
        this.anmelden = anmelden;
    }

    public String getGerenShenfen() {
        return gerenShenfen;
    }

    public void setGerenShenfen(String gerenShenfen) {
        this.gerenShenfen = gerenShenfen;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getYonggongStyle() {
        return yonggongStyle;
    }

    public void setYonggongStyle(String yonggongStyle) {
        this.yonggongStyle = yonggongStyle;
    }

    public String getPeasant() {
        return peasant;
    }

    public void setPeasant(String peasant) {
        this.peasant = peasant;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "InsuranceTaiyuanUserinfo{" +
                "gerenBianhao='" + gerenBianhao + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", idNum='" + idNum + '\'' +
                ", nation='" + nation + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", staffStatus='" + staffStatus + '\'' +
                ", anmelden='" + anmelden + '\'' +
                ", gerenShenfen='" + gerenShenfen + '\'' +
                ", address='" + address + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", yonggongStyle='" + yonggongStyle + '\'' +
                ", peasant='" + peasant + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}
