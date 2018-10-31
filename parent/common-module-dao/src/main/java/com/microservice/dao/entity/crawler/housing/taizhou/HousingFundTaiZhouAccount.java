package com.microservice.dao.entity.crawler.housing.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_taizhou_account",indexes = {@Index(name = "index_housing_taizhou_account_taskid", columnList = "taskid")})
public class HousingFundTaiZhouAccount extends IdEntity{

	private String type;
	private String num;
	private String money;
	
	private String datea;
	private String fee;
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundTaiZhouAccount [type=" + type + ", num=" + num + ", money=" + money + ", datea=" + datea
				+ ", fee=" + fee + ", taskid=" + taskid + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
