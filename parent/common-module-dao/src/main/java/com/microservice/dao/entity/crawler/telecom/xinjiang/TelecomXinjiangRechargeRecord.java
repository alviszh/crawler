package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_rechargerecord" ,indexes = {@Index(name = "index_telecom_xinjiang_rechargerecord_taskid", columnList = "taskid")})
public class TelecomXinjiangRechargeRecord extends IdEntity {

	private String serialNumber;//流水号
	private String accountTime;//入账时间
	private String accountAmount;//入账金额（元）
	private String accountWay;//缴费渠道
	private String accountType;//缴费方式
	private String accountScope;//使用范围
	private String taskid;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(String accountTime) {
		this.accountTime = accountTime;
	}

	public String getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(String accountAmount) {
		this.accountAmount = accountAmount;
	}

	public String getAccountWay() {
		return accountWay;
	}

	public void setAccountWay(String accountWay) {
		this.accountWay = accountWay;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountScope() {
		return accountScope;
	}

	public void setAccountScope(String accountScope) {
		this.accountScope = accountScope;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomXinjiangRechargeRecord [serialNumber=" + serialNumber + ", accountTime=" + accountTime
				+ ", accountAmount=" + accountAmount + ", accountWay=" + accountWay + ", accountType=" + accountType
				+ ", accountScope=" + accountScope + ", taskid=" + taskid + "]";
	}
   
}