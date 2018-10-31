package com.microservice.dao.entity.crawler.housing.jiamusi;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_jiamusi_pay")
public class HousingJiaMuSiPay  extends IdEntity implements Serializable {
		
	private String taskid;
	private String date;						//日期
	private String getMoney;					//提取金额
	private String setMoney;					//存入金额
	private String balance;						//余额
	private String mark;						//摘要
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
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}
	public String getSetMoney() {
		return setMoney;
	}
	public void setSetMoney(String setMoney) {
		this.setMoney = setMoney;
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
	@Override
	public String toString() {
		return "HousingJiaMuSiPay [taskid=" + taskid + ", date=" + date + ", getMoney=" + getMoney + ", setMoney="
				+ setMoney + ", balance=" + balance + ", mark=" + mark + "]";
	}
	
}
