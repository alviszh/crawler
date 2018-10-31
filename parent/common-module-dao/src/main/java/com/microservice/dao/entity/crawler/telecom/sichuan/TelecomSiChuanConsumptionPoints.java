package com.microservice.dao.entity.crawler.telecom.sichuan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_sichuan_consumptionpoints",indexes = {@Index(name = "index_telecom_sichuan_consumptionpoints_taskid", columnList = "taskid")})
public class TelecomSiChuanConsumptionPoints extends IdEntity{

	private String month ;//月份
	
	private String pointValue;//新增积分
	
	private String pointTypeName;//积分类型
	
	private String basicsPointValue;//消费基础分
	
	private Integer userid ;
	
	private String taskid;
	
	

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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getPointValue() {
		return pointValue;
	}

	public void setPointValue(String pointValue) {
		this.pointValue = pointValue;
	}

	public String getPointTypeName() {
		return pointTypeName;
	}

	public void setPointTypeName(String pointTypeName) {
		this.pointTypeName = pointTypeName;
	}

	public String getBasicsPointValue() {
		return basicsPointValue;
	}

	public void setBasicsPointValue(String basicsPointValue) {
		this.basicsPointValue = basicsPointValue;
	}

	@Override
	public String toString() {
		return "TelecomSiChuanConsumptionPoints [month=" + month + ", pointValue=" + pointValue + ", pointTypeName="
				+ pointTypeName + ", basicsPointValue=" + basicsPointValue + "]";
	}
	
	
}
