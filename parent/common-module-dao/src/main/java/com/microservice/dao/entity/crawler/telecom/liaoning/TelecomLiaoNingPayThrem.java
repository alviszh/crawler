package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_liaoning_payrecord",indexes = {@Index(name = "index_telecom_liaoning_payrecord_taskid", columnList = "taskid")})
public class TelecomLiaoNingPayThrem extends IdEntity{

	private String service_id;//业务号码
	
	private String payWayName;//付费方式
	
	private String payKindName;//付费银行
	
	private String pay_fee;//付费金额
	
	private String payTime;//缴费时间
	
	private String taskid ;

	private Integer Userid ;

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public String getPayKindName() {
		return payKindName;
	}

	public void setPayKindName(String payKindName) {
		this.payKindName = payKindName;
	}

	public String getPay_fee() {
		return pay_fee;
	}

	public void setPay_fee(String pay_fee) {
		this.pay_fee = pay_fee;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
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
		return "TelecomLiaoNingPayThrem [service_id=" + service_id + ", payWayName=" + payWayName + ", payKindName="
				+ payKindName + ", pay_fee=" + pay_fee + ", payTime=" + payTime + ", taskid=" + taskid + ", Userid="
				+ Userid + "]";
	}
	
	
	
}
