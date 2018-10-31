package com.microservice.dao.entity.crawler.insurance.jingzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 荆州社保 养老信息
 *
 * @author DougeChow
 */
@Entity
@Table(name = "insurance_jingzhou_yanglao_info")
public class InsurancejingzhouYanglaoInfo extends InsuranceJingzhouBasicBean implements Serializable {

    private static final long serialVersionUID = -7225639204374657354L;

    //	"aab001": "10374724",
    private String aab001;
    //			"yac505": "企业普通人员",
    private String yac505;
    //			"aic027": 409.64,
    private String aic027;
    //			"aic020": 2156,
    private String aic020;
    //			"aae114": "已实缴",
    private String aae114;
    //			"aic021": 172.48,
    private String aic021;
    //			"aic024": 0,
    private String aic024;
    //			"aae003": 201802,
    private String aae003;
    //			"aae037": "2018-03-22 00:00:00",
    private String aae037;
    //			"aae002": 201803,
    private String aae002;
    //			"aab004": "武汉智唯易才人力资源顾问有限公司荆州分公司",
    private String aab004;
    //			"aae143": "正常缴费",
    private String aae143;
    //			"aac001": "1027344034",
    private String aac001;
    //			"aae140": "企业养老"
    private String aae140;

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

    public String getAic027() {
        return aic027;
    }

    public void setAic027(String aic027) {
        this.aic027 = aic027;
    }

    public String getAic020() {
        return aic020;
    }

    public void setAic020(String aic020) {
        this.aic020 = aic020;
    }

    public String getAae114() {
        return aae114;
    }

    public void setAae114(String aae114) {
        this.aae114 = aae114;
    }

    public String getAic021() {
        return aic021;
    }

    public void setAic021(String aic021) {
        this.aic021 = aic021;
    }

    public String getAic024() {
        return aic024;
    }

    public void setAic024(String aic024) {
        this.aic024 = aic024;
    }

    public String getAae003() {
        return aae003;
    }

    public void setAae003(String aae003) {
        this.aae003 = aae003;
    }

    public String getAae037() {
        return aae037;
    }

    public void setAae037(String aae037) {
        this.aae037 = aae037;
    }

    public String getAae002() {
        return aae002;
    }

    public void setAae002(String aae002) {
        this.aae002 = aae002;
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

    public String getAae140() {
        return aae140;
    }

    public void setAae140(String aae140) {
        this.aae140 = aae140;
    }


    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}