package com.microservice.dao.entity.crawler.housing.huangshi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_huangshi_account")
public class HousingFundHuangShiAccount extends IdEntity{
	private String datea;//记账日期
	private String status;//归集和提取业务类型
	private String money;//发生额
	private String interest;//发生利息额
	private String fee;//个人余额
	private String reason;//提取原因
	private String type;//提取方式
	private String descr;//业务摘要
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundHuangShiAccount [datea=" + datea + ", status=" + status + ", money=" + money + ", interest="
				+ interest + ", fee=" + fee + ", reason=" + reason + ", type=" + type + ", descr=" + descr + ", taskid="
				+ taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
