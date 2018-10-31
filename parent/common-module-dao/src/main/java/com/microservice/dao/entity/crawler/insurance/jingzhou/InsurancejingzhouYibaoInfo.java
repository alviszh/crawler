package com.microservice.dao.entity.crawler.insurance.jingzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 荆州社保 医保信息
 *
 * @author DougeChow
 */
@Entity
@Table(name = "insurance_jingzhou_yibao_info")
public class InsurancejingzhouYibaoInfo extends InsuranceJingzhouBasicBean implements Serializable {

    private static final long serialVersionUID = -7225639204374657354L;

    //    "aab001": "10374724",
    private String aab001;
    //            "akc060": 71.9,
    private String akc060;
    //            "yac505": "基本医疗普通人员",
    private String yac505;
    //            "akc061": 53.93,
    private String akc061;
    //            "akc062": 233.67,
    private String akc062;
    //            "akc010": 3595,
    private String akc010;
    //            "aae114": "已实缴",
    private String aae114;
    //            "aae003": 201802,
    private String aae003;
    //            "aae002": 201803,
    private String aae002;
    //            "aae037": "2018-03-22 00:00:00",
    private String aae037;
    //            "aab004": "武汉智唯易才人力资源顾问有限公司荆州分公司",
    private String aab004;
    //            "aae143": "正常缴费",
    private String aae143;
    //            "aac001": "1027344034"
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

    public String getAkc060() {
        return akc060;
    }

    public void setAkc060(String akc060) {
        this.akc060 = akc060;
    }

    public String getYac505() {
        return yac505;
    }

    public void setYac505(String yac505) {
        this.yac505 = yac505;
    }

    public String getAkc061() {
        return akc061;
    }

    public void setAkc061(String akc061) {
        this.akc061 = akc061;
    }

    public String getAkc062() {
        return akc062;
    }

    public void setAkc062(String akc062) {
        this.akc062 = akc062;
    }

    public String getAkc010() {
        return akc010;
    }

    public void setAkc010(String akc010) {
        this.akc010 = akc010;
    }

    public String getAae114() {
        return aae114;
    }

    public void setAae114(String aae114) {
        this.aae114 = aae114;
    }

    public String getAae003() {
        return aae003;
    }

    public void setAae003(String aae003) {
        this.aae003 = aae003;
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