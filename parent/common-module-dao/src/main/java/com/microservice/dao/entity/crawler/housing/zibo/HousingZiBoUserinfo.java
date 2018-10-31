package com.microservice.dao.entity.crawler.housing.zibo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_zibo_userinfo")
public class HousingZiBoUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String payDate;							//缴至年月
	private String name;							//姓名
	private String balance;							//余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "HousingZiBoUserinfo [taskid=" + taskid + ", payDate=" + payDate + ", name=" + name + ", balance="
				+ balance + "]";
	}
	

}
