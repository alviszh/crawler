package com.microservice.dao.entity.crawler.insurance.zhoushan;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "insurance_zhoushan_maternity")
public class InsuranceZhoushanMaternity extends InsuranceZhoushanBasicBean implements Serializable {
    //    "ysym": 201803,
    private String ysym;
    //            "acym": 201803,
    private String acym;
    //            "ftym": "",
    private String ftym;
    //            "rewage": 2819,
    private String rewage;
    //            "arcpfd": 14.1,
    private String arcpfd;
    //            "cpname": "浙江中通文博服务有限公司",
    private String cpname;
    //            "sacode": "市本级"
    private String sacode;

    public String getYsym() {
        return ysym;
    }

    public void setYsym(String ysym) {
        this.ysym = ysym;
    }

    public String getAcym() {
        return acym;
    }

    public void setAcym(String acym) {
        this.acym = acym;
    }

    public String getFtym() {
        return ftym;
    }

    public void setFtym(String ftym) {
        this.ftym = ftym;
    }

    public String getRewage() {
        return rewage;
    }

    public void setRewage(String rewage) {
        this.rewage = rewage;
    }

    public String getArcpfd() {
        return arcpfd;
    }

    public void setArcpfd(String arcpfd) {
        this.arcpfd = arcpfd;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public String getSacode() {
        return sacode;
    }

    public void setSacode(String sacode) {
        this.sacode = sacode;
    }

    public String getTaskid() {
        return this.taskid;
    }
    @Override
    public String toString() {
        return "InsuranceZhoushanMaternity{" +
                "ysym='" + ysym + '\'' +
                ", acym='" + acym + '\'' +
                ", ftym='" + ftym + '\'' +
                ", rewage='" + rewage + '\'' +
                ", arcpfd='" + arcpfd + '\'' +
                ", cpname='" + cpname + '\'' +
                ", sacode='" + sacode + '\'' +
                '}';
    }
}
