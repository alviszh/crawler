package com.microservice.dao.entity.crawler.telecom.qinghai;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_qinghai_intergentresult")
public class TelecomQingHaiIntergentResult extends IdEntity{
	
	private String pointType;//推测为积分类型的id

	private String pointTypeName;//积分类型

	private String pointValue;//积分值

	private String validTime;//时间
	
	private Integer userid;

	private String taskid;

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public String getPointType() {
		return this.pointType;
	}

	public void setPointTypeName(String pointTypeName) {
		this.pointTypeName = pointTypeName;
	}

	public String getPointTypeName() {
		return this.pointTypeName;
	}

	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}

	public String getPointValue() {
		return this.pointValue;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getValidTime() {
		return this.validTime;
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

	@Override
	public String toString() {
		return "TelecomQingHaiIntergentResult [pointType=" + pointType + ", pointTypeName=" + pointTypeName
				+ ", pointValue=" + pointValue + ", validTime=" + validTime + ", userid=" + userid + ", taskid="
				+ taskid + "]";
	}

	
}
