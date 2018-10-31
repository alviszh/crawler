package com.microservice.dao.entity.crawler.telecom.sichuan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_sichuan_payment",indexes = {@Index(name = "index_telecom_sichuan_payment_taskid", columnList = "taskid")})
public class TelecomSiChuanPayMent extends IdEntity{

	private String amount;//充值金额
	
	private String createdate;//缴费时间
	
	private String paymentMethodName;//缴费渠道
	
	private Integer userid ;
	
	private String taskid;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
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
		return "TelecomSiChuanPayMent [amount=" + amount + ", createdate=" + createdate + ", paymentMethodName="
				+ paymentMethodName + ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	
	
}
