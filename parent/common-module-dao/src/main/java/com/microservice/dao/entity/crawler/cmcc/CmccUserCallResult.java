package com.microservice.dao.entity.crawler.cmcc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cmcc_user_call",indexes = {@Index(name = "index_cmcc_user_call_taskId", columnList = "taskId")})
public class CmccUserCallResult extends IdEntity{
	
	private String startTime;			//开始时间
	private String startYear;			//开始年份
	private String commPlac;			//接打电话所在地
	private String commMode;			//呼叫方式   主叫或被叫
	private String anotherNm;			//呼叫电话号码
	private String commTime;			//通话时长
	private String commType;			//通话方式
	private String mealFavorable;		//所使用套餐
	private String commFee;				//通话费用
	private String mobileNum;			//本机号码
	@Column(name="task_id")
	private String taskId;			

	@Override
	public String toString() {
		return "CmccUserCallResult [startTime=" + startTime + ", startYear=" + startYear + ", commPlac=" + commPlac
				+ ", commMode=" + commMode + ", anotherNm=" + anotherNm + ", commTime=" + commTime + ", commType="
				+ commType + ", mealFavorable=" + mealFavorable + ", commFee=" + commFee + ", mobileNum=" + mobileNum
				+ ", taskId=" + taskId + "]";
	}

	public String getMobileNum() {
		return mobileNum;
	}
	
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	public String getStartYear() {
		return startYear;
	}
	
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCommPlac() {
		return commPlac;
	}
	public void setCommPlac(String commPlac) {
		this.commPlac = commPlac;
	}
	public String getCommMode() {
		return commMode;
	}
	public void setCommMode(String commMode) {
		this.commMode = commMode;
	}
	public String getAnotherNm() {
		return anotherNm;
	}
	public void setAnotherNm(String anotherNm) {
		this.anotherNm = anotherNm;
	}
	public String getCommTime() {
		return commTime;
	}
	public void setCommTime(String commTime) {
		this.commTime = commTime;
	}
	public String getCommType() {
		return commType;
	}
	public void setCommType(String commType) {
		this.commType = commType;
	}
	public String getMealFavorable() {
		return mealFavorable;
	}
	public void setMealFavorable(String mealFavorable) {
		this.mealFavorable = mealFavorable;
	}
	public String getCommFee() {
		return commFee;
	}
	public void setCommFee(String commFee) {
		this.commFee = commFee;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
