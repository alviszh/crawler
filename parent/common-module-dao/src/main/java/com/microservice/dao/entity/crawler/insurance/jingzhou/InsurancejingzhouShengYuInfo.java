package com.microservice.dao.entity.crawler.insurance.jingzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 荆州社保 生育信息
 *
 * @author DougeChow
 */
@Entity
@Table(name = "insurance_jingzhou_shengyu_info")
public class InsurancejingzhouShengYuInfo extends InsuranceJingzhouBasicBean implements Serializable {

    private static final long serialVersionUID = -7225639204374657354L;

    //	"bmc001": 2156,
    private String bmc001;
    //			"aae003": 201801,
    private String aae003;
    //			"aab001": "10374724",
    private String aab001;
    //			"aae115": "已实缴",
    private String aae115;
    //			"yac505": "0501",
    private String yac505;
    //			"aae037": "2018-02-14 00:00:00",
    private String aae037;
    //			"bmc002": 8.62,
    private String bmc002;
    //			"aae002": 201802,
    private String aae002;
    //			"aae143": "正常缴费",
    private String aae143;
    //			"aab004": "武汉智唯易才人力资源顾问有限公司荆州分公司",
    private String aab004;
    //			"aac001": "1027344034"
    private String aac001;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBmc001() {
        return bmc001;
    }

    public void setBmc001(String bmc001) {
        this.bmc001 = bmc001;
    }

    public String getAae003() {
        return aae003;
    }

    public void setAae003(String aae003) {
        this.aae003 = aae003;
    }

    public String getAab001() {
        return aab001;
    }

    public void setAab001(String aab001) {
        this.aab001 = aab001;
    }

    public String getAae115() {
        return aae115;
    }

    public void setAae115(String aae115) {
        this.aae115 = aae115;
    }

    public String getYac505() {
        return yac505;
    }

    public void setYac505(String yac505) {
        this.yac505 = yac505;
    }

    public String getAae037() {
        return aae037;
    }

    public void setAae037(String aae037) {
        this.aae037 = aae037;
    }

    public String getBmc002() {
        return bmc002;
    }

    public void setBmc002(String bmc002) {
        this.bmc002 = bmc002;
    }

    public String getAae002() {
        return aae002;
    }

    public void setAae002(String aae002) {
        this.aae002 = aae002;
    }

    public String getAae143() {
        return aae143;
    }

    public void setAae143(String aae143) {
        this.aae143 = aae143;
    }

    public String getAab004() {
        return aab004;
    }

    public void setAab004(String aab004) {
        this.aab004 = aab004;
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