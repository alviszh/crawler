package com.crawler.callback.json.pbccrc;

import java.io.Serializable;

/**
 * 贷乎-前置规则
 * Created by zmy on 2018/6/19.
 */
public class DaihuPrecedingRule implements Serializable{
    private String key; //业务唯一标识
    private String taskId;   //爬虫任务ID
    private String userName;    //征信认证姓名
    private String idCardNo;    //征信认证身份证号
    private String monthlyDebt; //征信月负债

    public DaihuPrecedingRule() {
    }

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

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getMonthlyDebt() {
        return monthlyDebt;
    }

    public void setMonthlyDebt(String monthlyDebt) {
        this.monthlyDebt = monthlyDebt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "DaihuPrecedingRule{" +
                "key='" + key + '\'' +
                ", taskId='" + taskId + '\'' +
                ", userName='" + userName + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", monthlyDebt='" + monthlyDebt + '\'' +
                '}';
    }
}
