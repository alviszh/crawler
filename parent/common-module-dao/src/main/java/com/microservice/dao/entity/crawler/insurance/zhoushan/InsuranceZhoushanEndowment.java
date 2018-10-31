package com.microservice.dao.entity.crawler.insurance.zhoushan;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "insurance_zhoushan_endowment")
public class InsuranceZhoushanEndowment extends InsuranceZhoushanBasicBean implements Serializable {
    private String ysym; //应缴年月
    private String acym; //实缴年月
    private String ftym; //到账年月
    private String rewage;//缴费基数
    private String arcpfd;//单位缴纳
    private String arpsfd;//个人缴纳
    private String reflg; //类型
    private String psseno;//
    private String sacode;//参保地
    private String cpname;//所在单位
    private String pdcode;//险种

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

    public String getArpsfd() {
        return arpsfd;
    }

    public void setArpsfd(String arpsfd) {
        this.arpsfd = arpsfd;
    }

    public String getReflg() {
        return reflg;
    }

    public void setReflg(String reflg) {
        this.reflg = reflg;
    }

    public String getPsseno() {
        return psseno;
    }

    public void setPsseno(String psseno) {
        this.psseno = psseno;
    }

    public String getSacode() {
        return sacode;
    }

    public void setSacode(String sacode) {
        this.sacode = sacode;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public String getPdcode() {
        return pdcode;
    }

    public void setPdcode(String pdcode) {
        this.pdcode = pdcode;
    }

    public String getTaskid() {
        return this.taskid;
    }
    @Override
    public String toString() {
        return "InsuranceZhoushanEndowment{" +
                "ysym='" + ysym + '\'' +
                ", acym='" + acym + '\'' +
                ", ftym='" + ftym + '\'' +
                ", rewage='" + rewage + '\'' +
                ", arcpfd='" + arcpfd + '\'' +
                ", arpsfd='" + arpsfd + '\'' +
                ", reflg='" + reflg + '\'' +
                ", psseno='" + psseno + '\'' +
                ", sacode='" + sacode + '\'' +
                ", cpname='" + cpname + '\'' +
                ", pdcode='" + pdcode + '\'' +
                '}';
    }
}
