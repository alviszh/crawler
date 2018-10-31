package com.microservice.dao.entity.crawler.insurance.jingzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 荆州社保 失业信息
 *
 * @author DougeChow
 */
@Entity
@Table(name = "insurance_jingzhou_shiye_info")
public class InsurancejingzhouShiyeInfo extends InsuranceJingzhouBasicBean implements Serializable {

    private static final long serialVersionUID = -7225639204374657354L;

    //"aab001": "10374724",
    private String aab001;
    //		"yac505": "0201",
    private String yac505;
    //		"ajc031": 15.09,
    private String ajc031;
    //		"ajc020": 2156,
    private String ajc020;
    //		"aae114": "已实缴",
    private String aae114;
    //		"aae038": "2018-02-14 00:00:00",
    private String aae038;
    //		"ajc030": 6.47,
    private String ajc030;
    //		"aae003": 201802,
    private String aae003;
    //		"aae115": "已实缴",
    private String aae115;
    //		"aae002": 201801,
    private String aae002;
    //		"aae037": "2018-02-14 00:00:00",
    private String aae037;
    //		"aae067": "100000019064627",
    private String aae067;
    //		"aab004": "武汉智唯易才人力资源顾问有限公司荆州分公司",
    private String aab004;
    //		"aae143": "正常缴费",
    private String aae143;
    //		"aac001": "1027344034"
    private String aac001;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAab001() {
        return aab001;
    }

    public void setAab001(String aab001) {
        this.aab001 = aab001;
    }

    public String getYac505() {
        return yac505;
    }

    public void setYac505(String yac505) {
        this.yac505 = yac505;
    }

    public String getAjc031() {
        return ajc031;
    }

    public void setAjc031(String ajc031) {
        this.ajc031 = ajc031;
    }

    public String getAjc020() {
        return ajc020;
    }

    public void setAjc020(String ajc020) {
        this.ajc020 = ajc020;
    }

    public String getAae114() {
        return aae114;
    }

    public void setAae114(String aae114) {
        this.aae114 = aae114;
    }

    public String getAae038() {
        return aae038;
    }

    public void setAae038(String aae038) {
        this.aae038 = aae038;
    }

    public String getAjc030() {
        return ajc030;
    }

    public void setAjc030(String ajc030) {
        this.ajc030 = ajc030;
    }

    public String getAae003() {
        return aae003;
    }

    public void setAae003(String aae003) {
        this.aae003 = aae003;
    }

    public String getAae115() {
        return aae115;
    }

    public void setAae115(String aae115) {
        this.aae115 = aae115;
    }

    public String getAae002() {
        return aae002;
    }

    public void setAae002(String aae002) {
        this.aae002 = aae002;
    }

    public String getAae037() {
        return aae037;
    }

    public void setAae037(String aae037) {
        this.aae037 = aae037;
    }

    public String getAae067() {
        return aae067;
    }

    public void setAae067(String aae067) {
        this.aae067 = aae067;
    }

    public String getAab004() {
        return aab004;
    }

    public void setAab004(String aab004) {
        this.aab004 = aab004;
    }

    public String getAae143() {
        return aae143;
    }

    public void setAae143(String aae143) {
        this.aae143 = aae143;
    }

    public String getAac001() {
        return aac001;
    }

    public void setAac001(String aac001) {
        this.aac001 = aac001;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}