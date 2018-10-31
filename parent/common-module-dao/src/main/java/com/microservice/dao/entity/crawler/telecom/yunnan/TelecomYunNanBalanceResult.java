package com.microservice.dao.entity.crawler.telecom.yunnan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_yunnan_balance")
public class TelecomYunNanBalanceResult extends IdEntity {

	private String generalbalance;//通用余额
		
	private String specialbalance;//专用金额
		
	private String balance;//余额

	private String creditline;//信用额度
	
	private String residualcreditline;//信用额度
	
	private Integer userid;

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

	public String getGeneralbalance() {
		return generalbalance;
	}

	public void setGeneralbalance(String generalbalance) {
		this.generalbalance = generalbalance;
	}

	public String getSpecialbalance() {
		return specialbalance;
	}

	public void setSpecialbalance(String specialbalance) {
		this.specialbalance = specialbalance;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCreditline() {
		return creditline;
	}

	public void setCreditline(String creditline) {
		this.creditline = creditline;
	}

	
	public String getResidualcreditline() {
		return residualcreditline;
	}

	public void setResidualcreditline(String residualcreditline) {
		this.residualcreditline = residualcreditline;
	}

	@Override
	public String toString() {
		return "TelecomYunNanBalanceResult [generalbalance=" + generalbalance + ", specialbalance=" + specialbalance
				+ ", balance=" + balance + ", creditline=" + creditline + ", residualcreditline=" + residualcreditline
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	
	
}