/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.unicom;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_useractivity",indexes = {@Index(name = "index_unicom_useractivity_taskid", columnList = "taskid")})
public class UnicomUserActivityInfo extends IdEntity {

	private String endTime;//结束时间
	private String activityName;//业务名
	private String effectTime;
	private String activityId;
	private String effectTimeFmt;//猜测为起始时间
	private String endTimeFmt;//结束时间
	
	private Integer userid;

	private String taskid;

	@JsonBackReference
	private UnicomUserInfo userinfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userinfo_id")
	@JsonIgnore
	public UnicomUserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UnicomUserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
	}

	public String getEffectTime() {
		return effectTime;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setEffectTimeFmt(String effectTimeFmt) {
		this.effectTimeFmt = effectTimeFmt;
	}

	public String getEffectTimeFmt() {
		return effectTimeFmt;
	}

	public void setEndTimeFmt(String endTimeFmt) {
		this.endTimeFmt = endTimeFmt;
	}

	public String getEndTimeFmt() {
		return endTimeFmt;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	

}