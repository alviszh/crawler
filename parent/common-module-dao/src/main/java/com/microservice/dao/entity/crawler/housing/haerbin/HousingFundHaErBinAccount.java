package com.microservice.dao.entity.crawler.housing.haerbin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_haerbin_account",indexes = {@Index(name = "index_housing_haerbin_account_taskid", columnList = "taskid")})
public class HousingFundHaErBinAccount extends IdEntity{
	private String Datea;//记账日期
	private String descr;//摘要
	private String money;//发生额
	private String fee;//个人账户余额
	private String status;//交易类型
	private String startDate;//开始年月
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundHaErBinAccount [Datea=" + Datea + ", descr=" + descr + ", money=" + money + ", fee=" + fee
				+ ", status=" + status + ", startDate=" + startDate + ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return Datea;
	}
	public void setDatea(String datea) {
		Datea = datea;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
