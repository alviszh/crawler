package com.microservice.dao.entity.crawler.housing.foshan;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_foshan_pay")
public class HousingFoShanPay  extends IdEntity implements Serializable {
		
	private String occurDate;						//发生日期
	private String occurMoney;						//发生金额
	private String balance;							//余额
	private String mark;							//摘要
	private String taskid;
	public String getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}
	public String getOccurMoney() {
		return occurMoney;
	}
	public void setOccurMoney(String occurMoney) {
		this.occurMoney = occurMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingFoShanPay [occurDate=" + occurDate + ", occurMoney=" + occurMoney + ", balance=" + balance
				+ ", mark=" + mark + ", taskid=" + taskid + "]";
	}
	
	
	
}
