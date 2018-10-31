package com.microservice.dao.entity.crawler.housing.shenyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_shenyang_pay",indexes = {@Index(name = "index_housing_shenyang_pay_taskid", columnList = "taskid")})
public class HousingShenYangPay extends IdEntity implements Serializable{

	private String saveDate;//交易日期
	private String money;//发生额
	private String type;//摘要
	private String flag;//借贷标志
	private String fee;//余额
	private String datea;//汇缴日期
	private String taskid;
	@Override
	public String toString() {
		return "HousingShenYangPay [saveDate=" + saveDate + ", money=" + money + ", type=" + type + ", flag=" + flag
				+ ", fee=" + fee + ", datea=" + datea + ", taskid=" + taskid + "]";
	}
	public String getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(String saveDate) {
		this.saveDate = saveDate;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
	
}
