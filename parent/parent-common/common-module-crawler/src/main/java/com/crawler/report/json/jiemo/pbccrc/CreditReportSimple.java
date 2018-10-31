package com.crawler.report.json.jiemo.pbccrc;

import java.io.Serializable;

/**
 * 基本信息
 * Created by zmy on 2018/6/23.
 */
public class CreditReportSimple implements Serializable {

    private String queryTime;  //信息查询时间
    private String name;   //用户姓名
    private String idCard;   //证件号码
    private String marriage;  //婚姻状态
    private String generatedTime; //人行征信报告的生成时间
    private String loginIp; //用户的证件类型

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(String generatedTime) {
        this.generatedTime = generatedTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public CreditReportSimple(String queryTime, String name, String idCard, String marriage, String generatedTime, String loginIp) {
        this.queryTime = queryTime;
        this.name = name;
        this.idCard = idCard;
        this.marriage = marriage;
        this.generatedTime = generatedTime;
        this.loginIp = loginIp;
    }

    public CreditReportSimple() {
    }

    @Override
    public String toString() {
        return "CreditReportSimple{" +
                "queryTime='" + queryTime + '\'' +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", marriage='" + marriage + '\'' +
                ", generatedTime='" + generatedTime + '\'' +
                ", loginIp='" + loginIp + '\'' +
                '}';
    }
}
