package com.microservice.dao.entity.crawler.housing.xiaogan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_xiaogan_pay",indexes = {@Index(name = "index_housing_xiaogan_pay_taskid", columnList = "taskid")})
public class HousingXiaoGanPay extends IdEntity implements Serializable{
	private String taskid;
	private String payDate;						//日期
	private String type;					 	//业务类别
	private String money;					 	//金额
	private String mark;						//摘要

	@Override
	public String toString() {
		return "HousingXiaoGanPay [taskid=" + taskid + ", payDate=" + payDate + ", type=" + type
				 + ", money=" + money + ", mark=" + mark + "]";
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
}
