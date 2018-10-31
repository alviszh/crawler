package com.microservice.dao.entity.crawler.telecom.yunnan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_yunnan_pay")
public class TelecomYunNanPayResult extends IdEntity {

	private String phoneid;//流水号
	
	private String bookedtime;//支付时间
	
	private String num;//支付金额
	
	private String paymethod;//支付类型
			
	private Integer userid;

	private String taskid;

	public String getPhoneid() {
		return phoneid;
	}

	public void setPhoneid(String phoneid) {
		this.phoneid = phoneid;
	}

	public String getBookedtime() {
		return bookedtime;
	}

	public void setBookedtime(String bookedtime) {
		this.bookedtime = bookedtime;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
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
		return "TelecomYunNanPayResult [phoneid=" + phoneid + ", bookedtime=" + bookedtime + ", num=" + num
				+ ", paymethod=" + paymethod + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

	
}