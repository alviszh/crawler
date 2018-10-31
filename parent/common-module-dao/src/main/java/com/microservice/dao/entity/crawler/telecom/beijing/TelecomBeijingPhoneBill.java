package com.microservice.dao.entity.crawler.telecom.beijing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_beijing_bill")
public class TelecomBeijingPhoneBill extends IdEntity {

	private String date;//日期
	
	private String billNumber;//号码账单
	
	private String billCustom;//账户账单

	private String other;//账户账单电子发票金额
	
	private Integer userid;

	private String taskid;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillCustom() {
		return billCustom;
	}

	public void setBillCustom(String billCustom) {
		this.billCustom = billCustom;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
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
		return "TelecomBeijingPhoneBill [date=" + date + ", billNumber=" + billNumber + ", billCustom=" + billCustom
				+ ", other=" + other + ", userid=" + userid + ", taskid=" + taskid + "]";
	}
}