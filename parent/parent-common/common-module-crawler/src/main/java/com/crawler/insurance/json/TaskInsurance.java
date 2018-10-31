package com.crawler.insurance.json;

import java.io.Serializable;
import java.util.Date;

public class TaskInsurance implements Serializable {

    private String taskid;//uuid 前端通过uuid访问状态结果

    private String phase;//当前步骤

    private String phase_status;//步骤状态

    private String cookies;

    private String description;

    private String city;		//城市

    private Boolean finished;//爬虫任务是否全部完成

    private Integer error_code; //错误代码  StatusCodeRec 枚举类

    private String error_message; //错误信息 StatusCodeRec 枚举类

    private BasicUserInsurance basicUserInsurance; //用户基本表

    private Date updateTime = new Date();

    private Integer yanglaoStatus; //养老保险

    private Integer shiyeStatus; //失业保险

    private Integer gongshangStatus; //工伤保险

    private Integer shengyuStatus; //生育保险

    private Integer yiliaoStatus; //医疗保险

    /** 深圳社保爬取必要参数 */
    private String pid;

    private Integer userInfoStatus; //用户信息

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

    public BasicUserInsurance getBasicUserInsurance() {
        return basicUserInsurance;
    }

    public void setBasicUserInsurance(BasicUserInsurance basicUserInsurance) {
        this.basicUserInsurance = basicUserInsurance;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYanglaoStatus() {
        return yanglaoStatus;
    }

    public void setYanglaoStatus(Integer yanglaoStatus) {
        this.yanglaoStatus = yanglaoStatus;
    }

    public Integer getShiyeStatus() {
        return shiyeStatus;
    }

    public void setShiyeStatus(Integer shiyeStatus) {
        this.shiyeStatus = shiyeStatus;
    }

    public Integer getGongshangStatus() {
        return gongshangStatus;
    }

    public void setGongshangStatus(Integer gongshangStatus) {
        this.gongshangStatus = gongshangStatus;
    }

    public Integer getShengyuStatus() {
        return shengyuStatus;
    }

    public void setShengyuStatus(Integer shengyuStatus) {
        this.shengyuStatus = shengyuStatus;
    }

    public Integer getYiliaoStatus() {
        return yiliaoStatus;
    }

    public void setYiliaoStatus(Integer yiliaoStatus) {
        this.yiliaoStatus = yiliaoStatus;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getUserInfoStatus() {
        return userInfoStatus;
    }

    public void setUserInfoStatus(Integer userInfoStatus) {
        this.userInfoStatus = userInfoStatus;
    }

    public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Override
    public String toString() {
        return "TaskInsurance{" +
                "taskid='" + taskid + '\'' +
                ", phase='" + phase + '\'' +
                ", phase_status='" + phase_status + '\'' +
                ", cookies='" + cookies + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", finished=" + finished +
                ", error_code=" + error_code +
                ", error_message='" + error_message + '\'' +
                ", basicUserInsurance=" + basicUserInsurance +
                ", updateTime=" + updateTime +
                ", yanglaoStatus=" + yanglaoStatus +
                ", shiyeStatus=" + shiyeStatus +
                ", gongshangStatus=" + gongshangStatus +
                ", shengyuStatus=" + shengyuStatus +
                ", yiliaoStatus=" + yiliaoStatus +
                ", pid='" + pid + '\'' +
                ", userInfoStatus=" + userInfoStatus +
                '}';
    }
}
