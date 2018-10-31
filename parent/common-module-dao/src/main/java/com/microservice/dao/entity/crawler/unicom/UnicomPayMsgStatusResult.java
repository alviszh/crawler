package com.microservice.dao.entity.crawler.unicom;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_paymsgstatus",indexes = {@Index(name = "index_unicom_paymsgstatus_taskid", columnList = "taskid")})
public class UnicomPayMsgStatusResult extends IdEntity {

	private String payfee;// 缴费金额
	private String paydate;// 缴费日期
	private String paychannel;// 缴费类型 账务后台等

	private String paymentid;// 未知含义 在哔哩哔哩卡出现

	private String payment;// 未知含义 在哔哩哔哩卡出现

	private Integer userid;

	private String taskid;

	public String getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
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

	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}

	public String getPayfee() {
		return payfee;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
	}

	public String getPaychannel() {
		return paychannel;
	}

	@Override
	public String toString() {
		return "UnicomPayMsgStatusResult [payfee=" + payfee + ", paydate=" + paydate + ", paychannel=" + paychannel
				+ ", paymentid=" + paymentid + ", payment=" + payment + ", userid=" + userid + ", taskid=" + taskid
				+ "]";
	}

	
}