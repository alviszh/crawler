package com.microservice.dao.entity.crawler.housing.huaian;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_huaian_pay")
public class HousingHuaiAnPay  extends IdEntity implements Serializable {
		
	private String taskid;
	private String date;					//日期
	private String remark;					//摘要
	private String increase;				//增加
	private String decrease;				//减少
	private String balance;					//余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getDecrease() {
		return decrease;
	}
	public void setDecrease(String decrease) {
		this.decrease = decrease;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "HousingHuaiAnPay [taskid=" + taskid + ", date=" + date + ", remark=" + remark + ", increase=" + increase
				+ ", decrease=" + decrease + ", balance=" + balance + "]";
	}
	
}
