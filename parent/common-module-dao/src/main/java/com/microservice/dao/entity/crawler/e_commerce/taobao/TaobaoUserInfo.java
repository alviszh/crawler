package com.microservice.dao.entity.crawler.e_commerce.taobao;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_taobao_user_info")
public class TaobaoUserInfo extends IdEntity implements Serializable {

    private String taskid;
    private String loginName;
    private String nickName;
    private String realname;
    private String gerden;
    private String birthday;
    private String domicileProvince;//居住地
    private String domicileCity;//居住地
    private String domicileArea;//居住地
    private String constellation;//星座
    private String hometownArea;
    private String hometownCity;
    private String hometownProvince;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getGerden() {
        return gerden;
    }

    public void setGerden(String gerden) {
        this.gerden = gerden;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getDomicileProvince() {
        return domicileProvince;
    }

    public void setDomicileProvince(String domicileProvince) {
        this.domicileProvince = domicileProvince;
    }

    public String getDomicileCity() {
        return domicileCity;
    }

    public void setDomicileCity(String domicileCity) {
        this.domicileCity = domicileCity;
    }

    public String getDomicileArea() {
        return domicileArea;
    }

    public void setDomicileArea(String domicileArea) {
        this.domicileArea = domicileArea;
    }

    public String getHometownProvince() {
        return hometownProvince;
    }

    public String getHometownCity() {
        return hometownCity;
    }

    public String getHometownArea() {
        return hometownArea;
    }


    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Override
    public String toString() {
        return "TaobaoUserInfo{" +
                "loginName='" + loginName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realname + '\'' +
                ", gerden='" + gerden + '\'' +
                ", birthday='" + birthday + '\'' +
                ", constellation='" + constellation + '\'' +
                ", domicileProvince='" + domicileProvince + '\'' +
                ", domicileCity='" + domicileCity + '\'' +
                ", domicileArea='" + domicileArea + '\'' +
                ", hometownProvince='" + hometownProvince + '\'' +
                ", hometownCity='" + hometownCity + '\'' +
                ", hometownArea='" + hometownArea + '\'' +
                '}';
    }

    public void setHometownProvince(String hometownProvince) {
        this.hometownProvince = hometownProvince;
    }

    public void setHometownCity(String hometownCity) {
        this.hometownCity = hometownCity;
    }

    public void setHometownArea(String hometownArea) {
        this.hometownArea = hometownArea;
    }
}
