package com.crawler.callback.json.pbccrc;

import java.io.Serializable;

/**
 * 借么-前置规则
 * Created by zmy on 2018/6/19.
 */
public class JiemoPrecedingRule{
    private String key; //业务唯一标识
    private String taskId;   //任务id
    private String userName;    //用户姓名
    private String idCardType;    //用户的证件类型
    private String idCardNo;    //证件号码
    private String reportTime;   //人行征信报告的生成时间
    private String type;        //业务类型（征信，运营商、社保...）

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JiemoPrecedingRule() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JiemoPrecedingRule(String key, String taskId, String userName, String idCardType, String idCardNo, String reportTime, String type) {
        this.key = key;
        this.taskId = taskId;
        this.userName = userName;
        this.idCardType = idCardType;
        this.idCardNo = idCardNo;
        this.reportTime = reportTime;
        this.type = type;
    }

    @Override
    public String toString() {
        return "JiemoPrecedingRule{" +
                "key='" + key + '\'' +
                ", taskId='" + taskId + '\'' +
                ", userName='" + userName + '\'' +
                ", idCardType='" + idCardType + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", reportTime='" + reportTime + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
