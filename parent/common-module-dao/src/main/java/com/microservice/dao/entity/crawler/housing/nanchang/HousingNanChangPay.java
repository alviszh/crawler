package com.microservice.dao.entity.crawler.housing.nanchang;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_nanchang_pay")
public class HousingNanChangPay  extends IdEntity implements Serializable {
		
	private String occurDate;						//日期
	private String mark;							//摘要
	private String incomeMoney;						//收入金额
	private String payMoney;						//支出金额
	private String balance;							//余额
	private String taskid;
	public String getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getIncomeMoney() {
		return incomeMoney;
	}
	public void setIncomeMoney(String incomeMoney) {
		this.incomeMoney = incomeMoney;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingNanChangPay [occurDate=" + occurDate + ", mark=" + mark + ", incomeMoney=" + incomeMoney
				+ ", payMoney=" + payMoney + ", balance=" + balance + ", taskid=" + taskid + "]";
	}
	
	
	
}
