package com.crawler.housingfund.json;

import java.io.Serializable;
import java.util.Date;

public class TaskHousingfund implements Serializable {

    private String taskid;//uuid 前端通过uuid访问状态结果

    private String phase;//当前步骤

    private String phase_status;//步骤状态

    private String cookies;

    private String description;

    private String city;		//城市

    private Boolean finished;//爬虫任务是否全部完成

    private Integer error_code; //错误代码  StatusCodeRec 枚举类

    private String error_message; //错误信息 StatusCodeRec 枚举类

    private BasicUserHousingfund basicUserHousingfund; //用户基本表

    private Date updateTime = new Date();

    private Integer userinfoStatus; //用户信息

    private Integer paymentStatus; //缴存信息

    private String createtime;  //为了定时任务邮件，补充添加的     sln
    
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getPhase_status() {
        return phase_status;
    }

    public void setPhase_status(String phase_status) {
        this.phase_status = phase_status;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public BasicUserHousingfund getBasicUserHousingfund() {
        return basicUserHousingfund;
    }

    public void setBasicUserHousingfund(BasicUserHousingfund basicUserHousingfund) {
        this.basicUserHousingfund = basicUserHousingfund;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPaymentStatus() { return paymentStatus; }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getUserinfoStatus() {
        return userinfoStatus;
    }

    public void setUserinfoStatus(Integer userinfoStatus) {
        this.userinfoStatus = userinfoStatus;
    }

    public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Override
    public String toString() {
        return "TaskHousingfund{" +
                "taskid='" + taskid + '\'' +
                ", phase='" + phase + '\'' +
                ", phase_status='" + phase_status + '\'' +
                ", cookies='" + cookies + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", finished=" + finished +
                ", error_code=" + error_code +
                ", error_message='" + error_message + '\'' +
                ", basicUserHousingfund=" + basicUserHousingfund +
                ", updateTime=" + updateTime +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", userinfoStatus=" + userinfoStatus +
                '}';
    }
}
