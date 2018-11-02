package com.microservice.dao.entity.crawler.telecom.guangdong;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guangdong_businessmessage",indexes = {@Index(name = "index_telecom_guangdong_businessmessage_taskid", columnList = "taskid")})
public class TelecomGuangDongBusinessmessage extends IdEntity{

	private String taskid;
	
	private Integer userid;
	
	private String ztaoname;//主套餐名称
	
	private String tcontent;//套餐内容
	
	private String gross;//总量
	
	private String residue;//剩余
	
	private String unit;//单位
	
	private String describe;//描述

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getZtaoname() {
		return ztaoname;
	}

	public void setZtaoname(String ztaoname) {
		this.ztaoname = ztaoname;
	}

	public String getTcontent() {
		return tcontent;
	}

	public void setTcontent(String tcontent) {
		this.tcontent = tcontent;
	}

	public String getGross() {
		return gross;
	}

	public void setGross(String gross) {
		this.gross = gross;
	}

	public String getResidue() {
		return residue;
	}

	public void setResidue(String residue) {
		this.residue = residue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Column(name = "describeinfo")
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String toString() {
		return "TelecomGuangDongBusinessmessage [taskid=" + taskid + ", userid=" + userid + ", ztaoname=" + ztaoname
				+ ", tcontent=" + tcontent + ", gross=" + gross + ", residue=" + residue + ", unit=" + unit
				+ ", describe=" + describe + "]";
	}
	
	
}
