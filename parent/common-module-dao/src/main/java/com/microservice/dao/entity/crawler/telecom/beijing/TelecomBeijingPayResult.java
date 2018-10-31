package com.microservice.dao.entity.crawler.telecom.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_beijing_payresult")
public class TelecomBeijingPayResult extends IdEntity {

	private String serialnumber;//流水号
	
	private String bookedtime;//充值时间
	
	private String phone;//电话号

	private String num;//缴费金额
	
	private String paychannels;//缴费渠道

	private String paymethod;//缴费方式
	
	private String applicable;//使用范围
		
	private Integer userid;

	private String taskid;

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getBookedtime() {
		return bookedtime;
	}

	public void setBookedtime(String bookedtime) {
		this.bookedtime = bookedtime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPaychannels() {
		return paychannels;
	}

	public void setPaychannels(String paychannels) {
		this.paychannels = paychannels;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getApplicable() {
		return applicable;
	}

	public void setApplicable(String applicable) {
		this.applicable = applicable;
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
		return "TelecomBeijingPayResult [serialnumber=" + serialnumber + ", bookedtime=" + bookedtime + ", phone="
				+ phone + ", num=" + num + ", paychannels=" + paychannels + ", paymethod=" + paymethod + ", applicable="
				+ applicable + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

	
}