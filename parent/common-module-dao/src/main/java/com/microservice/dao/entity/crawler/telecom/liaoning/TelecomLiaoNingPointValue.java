package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_liaoning_pointvalue",indexes = {@Index(name = "index_telecom_liaoning_pointvalue_taskid", columnList = "taskid")})
public class TelecomLiaoNingPointValue extends IdEntity{

	private String pointValue;//可用积分

	private String consumPoint;//消费积分

	private String taskid ;

	private Integer Userid ;

	public String getPointValue() {
		return pointValue;
	}

	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}

	public String getConsumPoint() {
		return consumPoint;
	}

	public void setConsumPoint(String consumPoint) {
		this.consumPoint = consumPoint;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return Userid;
	}

	public void setUserid(Integer userid) {
		Userid = userid;
	}

	@Override
	public String toString() {
		return "TelecomLiaoNingPointValue [pointValue=" + pointValue + ", consumPoint=" + consumPoint + ", taskid="
				+ taskid + ", Userid=" + Userid + "]";
	}
	

}
