package com.microservice.dao.entity.crawler.insurance.zhoushan;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 医保
 */

@Entity
@Table(name = "insurance_zhoushan_medical")
public class InsuranceZhoushanMedical extends InsuranceZhoushanBasicBean implements Serializable {
    // 应缴年月
    private String ysym;
    // 实缴年月
    private String acym;
    // 到账年月
    private String ftym;
    // 缴费基数: 4420,
    private String rewage;
    //单位金额: 243.1,
    private String arcpfd;
    //  个人金额: 0,
    private String arpsfd;
    // 医疗补助: 0,
    private String arfd05;
    // 医疗救助: 5,
    private String arfd07;
    // 单位划入: 0,
    private String arcpinfd;
    //   个人划入: 0,
    private String arpsinfd;
    //  补助金划入: 0,
    private String arinfd05;
    //  类型": "正常应收",
    private String reflg;
    //                    "pdcode": "02",
    private String pdcode;
    //                    "psseno": 1567969,
    private String psseno;
    //  所在单位: "浙江中通文博服务有限公司",
    private String cpname;
    //  参保地: "市本级
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

    public String getArpsfd() {
        return arpsfd;
    }

    public void setArpsfd(String arpsfd) {
        this.arpsfd = arpsfd;
    }

    public String getArfd05() {
        return arfd05;
    }

    public void setArfd05(String arfd05) {
        this.arfd05 = arfd05;
    }

    public String getArfd07() {
        return arfd07;
    }

    public void setArfd07(String arfd07) {
        this.arfd07 = arfd07;
    }

    public String getArcpinfd() {
        return arcpinfd;
    }

    public void setArcpinfd(String arcpinfd) {
        this.arcpinfd = arcpinfd;
    }

    public String getArpsinfd() {
        return arpsinfd;
    }

    public void setArpsinfd(String arpsinfd) {
        this.arpsinfd = arpsinfd;
    }

    public String getArinfd05() {
        return arinfd05;
    }

    public void setArinfd05(String arinfd05) {
        this.arinfd05 = arinfd05;
    }

    public String getReflg() {
        return reflg;
    }

    public void setReflg(String reflg) {
        this.reflg = reflg;
    }

    public String getPdcode() {
        return pdcode;
    }

    public void setPdcode(String pdcode) {
        this.pdcode = pdcode;
    }

    public String getPsseno() {
        return psseno;
    }

    public void setPsseno(String psseno) {
        this.psseno = psseno;
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
        return "InsuranceZhoushanMedical{" +
                "ysym='" + ysym + '\'' +
                ", acym='" + acym + '\'' +
                ", ftym='" + ftym + '\'' +
                ", rewage='" + rewage + '\'' +
                ", arcpfd='" + arcpfd + '\'' +
                ", arpsfd='" + arpsfd + '\'' +
                ", arfd05='" + arfd05 + '\'' +
                ", arfd07='" + arfd07 + '\'' +
                ", arcpinfd='" + arcpinfd + '\'' +
                ", arpsinfd='" + arpsinfd + '\'' +
                ", arinfd05='" + arinfd05 + '\'' +
                ", reflg='" + reflg + '\'' +
                ", pdcode='" + pdcode + '\'' +
                ", psseno='" + psseno + '\'' +
                ", cpname='" + cpname + '\'' +
                ", sacode='" + sacode + '\'' +
                '}';
    }
}
