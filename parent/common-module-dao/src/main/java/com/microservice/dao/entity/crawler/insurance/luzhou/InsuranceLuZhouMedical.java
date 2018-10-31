package com.microservice.dao.entity.crawler.insurance.luzhou;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_luzhou_medical")
public class InsuranceLuZhouMedical extends IdEntity{

	private String time;
	private String month;
	private String money;
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceLuZhouMedical [time=" + time + ", month=" + month + ", money=" + money + ", taskid=" + taskid
				+ "]";
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
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
