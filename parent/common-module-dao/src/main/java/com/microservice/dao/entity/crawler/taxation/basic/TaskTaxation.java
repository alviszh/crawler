package com.microservice.dao.entity.crawler.taxation.basic;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntityAndCookie;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "task_taxation" ,indexes = {@Index(name = "index_task_taxation_taskid", columnList = "taskid")})
public class TaskTaxation extends IdEntityAndCookie implements Serializable {

	private static final long serialVersionUID = -7601637293254927953L;

	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String phase;//当前步骤
	
	private String phase_status;//步骤状态
	
	private String description;
	
	private String owner;//数据所属人
	
	private String phonenum;//手机号
	
	private Boolean finished;//爬虫任务是否全部完成
	
	private Integer error_code; //错误代码  StatusCodeRec 枚举类
	
	private String error_message; //错误信息 StatusCodeRec 枚举类

	@JsonBackReference
	private BasicUserTaxation basicUserTaxation; //用户基本表
	
	private Date updateTime = new Date();
	 
	private String nexturl;
	
	private Integer trianNum;			//登录失败重试次数
	
	private Integer userMsgStatus; // 用户信息状态
	
	private Integer accountMsgStatus; // 账户信息状态
	
	private String testhtml; //测试
	
	private String province;//归属地省份
	
	private String city; //归属地市
	
	private Date etltime;
	
	private Date reportTime;
	
	private String reportStatus;
	private String password;
	private Date pushtime; //推送前置规则的时间



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



	public String getOwner() {
		return owner;
	}



	public void setOwner(String owner) {
		this.owner = owner;
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


	@Column(columnDefinition="text")
	public String getError_message() {
		return error_message;
	}



	public void setError_message(String error_message) {
		this.error_message = error_message;
	}


	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="basicUserTaxation_id")
	public BasicUserTaxation getBasicUserTaxation() {
		return basicUserTaxation;
	}



	public void setBasicUserTaxation(BasicUserTaxation basicUserTaxation) {
		this.basicUserTaxation = basicUserTaxation;
	}



	public Date getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	@Column(columnDefinition="text")
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


	@Column(columnDefinition="text")
	public String getTesthtml() {
		return testhtml;
	}



	public void setTesthtml(String testhtml) {
		this.testhtml = testhtml;
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



	public Date getEtltime() {
		return etltime;
	}



	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}



	public Date getReportTime() {
		return reportTime;
	}



	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}



	public String getReportStatus() {
		return reportStatus;
	}



	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Date getPushtime() {
		return pushtime;
	}



	public void setPushtime(Date pushtime) {
		this.pushtime = pushtime;
	}



	@Override
	public String toString() {
		return "TaskTaxation [taskid=" + taskid + ", phase=" + phase + ", phase_status=" + phase_status
				+ ", description=" + description + ", owner=" + owner + ", phonenum=" + phonenum + ", finished="
				+ finished + ", error_code=" + error_code + ", error_message=" + error_message + ", basicUserTaxation="
				+ basicUserTaxation + ", updateTime=" + updateTime + ", nexturl=" + nexturl + ", trianNum=" + trianNum
				+ ", userMsgStatus=" + userMsgStatus + ", accountMsgStatus=" + accountMsgStatus + ", testhtml="
				+ testhtml + ", province=" + province + ", city=" + city + ", etltime=" + etltime + ", reportTime="
				+ reportTime + ", reportStatus=" + reportStatus + ", password=" + password + ", pushtime=" + pushtime
				+ "]";
	}




	
	
	
}
