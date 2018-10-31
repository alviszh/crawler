package com.microservice.dao.entity.crawler.insurance.luzhou;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_luzhou_endowment")
public class InsuranceLuZhouEndowment extends IdEntity{

	private String time;
	private String month;
	private String money;
	private String fee;
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceLuZhouEndowment [time=" + time + ", month=" + month + ", money=" + money + ", fee=" + fee
				+ ", taskid=" + taskid + "]";
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
}
