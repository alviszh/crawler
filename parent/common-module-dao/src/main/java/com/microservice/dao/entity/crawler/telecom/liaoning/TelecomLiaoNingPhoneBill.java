package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_liaoning_phonebill",indexes = {@Index(name = "index_telecom_liaoning_phonebill_taskid", columnList = "taskid")})
public class TelecomLiaoNingPhoneBill extends IdEntity{

	private String feeInfoname;//服务名称

	private String feeInfovalue;//服务费用

	private String date;//服务费用

	private Integer userid;

	private String taskid;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}



	public String getFeeInfoname() {
		return feeInfoname;
	}

	public void setFeeInfoname(String feeInfoname) {
		this.feeInfoname = feeInfoname;
	}

	public String getFeeInfovalue() {
		return feeInfovalue;
	}

	public void setFeeInfovalue(String feeInfovalue) {
		this.feeInfovalue = feeInfovalue;
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
		return "TelecomLiaoNingPhoneBill [feeInfoname=" + feeInfoname + ", feeInfovalue=" + feeInfovalue + ", userid="
				+ userid + ", taskid=" + taskid + "]";
	}



}
