package com.microservice.dao.entity.crawler.telecom.heilongjiang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_heilongjiang_payresult")
public class TelecomPayMsgThemResult extends IdEntity {

	private String telenumid;//含义未知

	private String type;// 缴费类型

	private String paydate;//缴费日期

	private String paynum;//金额

	private Integer userid;

	private String taskid;

	public String getTelenumid() {
		return telenumid;
	}

	public void setTelenumid(String telenumid) {
		this.telenumid = telenumid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getPaynum() {
		return paynum;
	}

	public void setPaynum(String paynum) {
		this.paynum = paynum;
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
		return "TelecomPayMsgThemResult [telenumid=" + telenumid + ", type=" + type + ", paydate=" + paydate
				+ ", paynum=" + paynum + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

}
