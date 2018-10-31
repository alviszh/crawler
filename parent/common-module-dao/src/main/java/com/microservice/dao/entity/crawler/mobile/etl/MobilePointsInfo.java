package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="mobile_points_info",indexes = {@Index(name = "index_mobile_points_info_taskid", columnList = "taskId")})

/*
 * 积分信息
 */

public class MobilePointsInfo extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String pointsTime;	//获得积分时间
	private String points;	//可硬积分
	private String telephoneNumber; //用户手机号
	private String months;
	@JsonBackReference
	private String resource; //溯源字段
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPointsTime() {
		return pointsTime;
	}
	public void setPointsTime(String pointsTime) {
		this.pointsTime = pointsTime;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		return "MobilePointsInfo [taskId=" + taskId + ", pointsTime=" + pointsTime + ", points=" + points
				+ ", telephoneNumber=" + telephoneNumber + ", months=" + months + ", resource=" + resource + "]";
	}
	
	
		
}
