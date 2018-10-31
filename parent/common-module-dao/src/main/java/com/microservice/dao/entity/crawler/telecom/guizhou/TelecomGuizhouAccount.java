package com.microservice.dao.entity.crawler.telecom.guizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guizhou_account" ,indexes = {@Index(name = "index_telecom_guizhou_account_taskid", columnList = "taskid")})
public class TelecomGuizhouAccount extends IdEntity {

	private String balanceAmount;// 通用余额
	private String specAmount;// 专用余额
	private String commonAmount;// 合计余额
	private String realOweAmount;// 本月实时话费
	private String taskid;
	
	public String getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public String getSpecAmount() {
		return specAmount;
	}
	public void setSpecAmount(String specAmount) {
		this.specAmount = specAmount;
	}
	public String getCommonAmount() {
		return commonAmount;
	}
	public void setCommonAmount(String commonAmount) {
		this.commonAmount = commonAmount;
	}
	public String getRealOweAmount() {
		return realOweAmount;
	}
	public void setRealOweAmount(String realOweAmount) {
		this.realOweAmount = realOweAmount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	@Override
	public String toString() {
		return "TelecomGuizhouAccount [balanceAmount=" + balanceAmount + ", specAmount=" + specAmount
				+ ", commonAmount=" + commonAmount + ", realOweAmount=" + realOweAmount + ", taskid=" + taskid + "]";
	}
	public TelecomGuizhouAccount(String balanceAmount, String specAmount, String commonAmount, String realOweAmount,
			String taskid) {
		super();
		this.balanceAmount = balanceAmount;
		this.specAmount = specAmount;
		this.commonAmount = commonAmount;
		this.realOweAmount = realOweAmount;
		this.taskid = taskid;
	}
	public TelecomGuizhouAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
}