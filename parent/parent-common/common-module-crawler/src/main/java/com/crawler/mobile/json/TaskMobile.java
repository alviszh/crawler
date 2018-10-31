package com.crawler.mobile.json;

import java.io.Serializable;
import java.util.Date;

public class TaskMobile implements Serializable{

    private String taskid;//uuid 前端通过uuid访问状态结果

    private String phase;//当前步骤

    private String phase_status;//步骤状态

    private String description;

    private String carrier;//运营商

    private String owner;//数据所属人

    private String phonenum;//手机号

//	private Integer progress;//爬虫进度

    private Boolean finished;//爬虫任务是否全部完成

    private Integer error_code; //错误代码  StatusCodeRec 枚举类

    private String error_message; //错误信息 StatusCodeRec 枚举类

    private Date updateTime = new Date();

    private String nexturl;

    private Integer trianNum;			//登录失败重试次数

    private Integer userMsgStatus; // 用户信息状态

    private Integer accountMsgStatus; // 账户信息状态

    private Integer callRecordStatus; // 通话详单状态

    private Integer smsRecordStatus; // 短信记录状态

    private Integer businessMsgStatus; // 业务信息状态

    private Integer payMsgStatus; // 缴费信息状态

    private Integer integralMsgStatus; // 积分信息状态

    private Integer familyMsgStatus; // 亲情号信息状态

    private BasicUser basicUser;

    private String province;//归属地（省份）
    
    private String city;  //4月8日添加 （为etl邮件添加）
    
    private String createtime;  //4月8日添加 （为etl邮件添加）

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNexturl() {
        return nexturl;
    }

    public void setNexturl(String nexturl) {
        this.nexturl = nexturl;
    }

    public Integer getTrianNum() {
        return trianNum;
    }

    public void setTrianNum(Integer trianNum) {
        this.trianNum = trianNum;
    }

    public Integer getUserMsgStatus() {
        return userMsgStatus;
    }

    public void setUserMsgStatus(Integer userMsgStatus) {
        this.userMsgStatus = userMsgStatus;
    }

    public Integer getAccountMsgStatus() {
        return accountMsgStatus;
    }

    public void setAccountMsgStatus(Integer accountMsgStatus) {
        this.accountMsgStatus = accountMsgStatus;
    }

    public Integer getCallRecordStatus() {
        return callRecordStatus;
    }

    public void setCallRecordStatus(Integer callRecordStatus) {
        this.callRecordStatus = callRecordStatus;
    }


    public Integer getSmsRecordStatus() {
        return smsRecordStatus;
    }

    public void setSmsRecordStatus(Integer smsRecordStatus) {
        this.smsRecordStatus = smsRecordStatus;
    }

    public Integer getBusinessMsgStatus() {
        return businessMsgStatus;
    }

    public void setBusinessMsgStatus(Integer businessMsgStatus) {
        this.businessMsgStatus = businessMsgStatus;
    }

    public Integer getPayMsgStatus() {
        return payMsgStatus;
    }

    public void setPayMsgStatus(Integer payMsgStatus) {
        this.payMsgStatus = payMsgStatus;
    }

    public Integer getIntegralMsgStatus() {
        return integralMsgStatus;
    }

    public void setIntegralMsgStatus(Integer integralMsgStatus) {
        this.integralMsgStatus = integralMsgStatus;
    }

    public Integer getFamilyMsgStatus() {
        return familyMsgStatus;
    }

    public void setFamilyMsgStatus(Integer familyMsgStatus) {
        this.familyMsgStatus = familyMsgStatus;
    }

    public BasicUser getBasicUser() {
        return basicUser;
    }

    public void setBasicUser(BasicUser basicUser) {
        this.basicUser = basicUser;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Override
    public String toString() {
        return "TaskMobile{" +
                "taskid='" + taskid + '\'' +
                ", phase='" + phase + '\'' +
                ", phase_status='" + phase_status + '\'' +
                ", description='" + description + '\'' +
                ", carrier='" + carrier + '\'' +
                ", owner='" + owner + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", finished=" + finished +
                ", error_code=" + error_code +
                ", error_message='" + error_message + '\'' +
                ", updateTime=" + updateTime +
                ", nexturl='" + nexturl + '\'' +
                ", trianNum=" + trianNum +
                ", userMsgStatus=" + userMsgStatus +
                ", accountMsgStatus=" + accountMsgStatus +
                ", callRecordStatus=" + callRecordStatus +
                ", smsRecordStatus=" + smsRecordStatus +
                ", businessMsgStatus=" + businessMsgStatus +
                ", payMsgStatus=" + payMsgStatus +
                ", integralMsgStatus=" + integralMsgStatus +
                ", familyMsgStatus=" + familyMsgStatus +
                ", basicUser=" + basicUser +
                ", province='" + province + '\'' +
                '}';
    }
}
