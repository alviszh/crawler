package com.microservice.dao.entity.crawler.housing.weifang;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_weifang_pay")
public class HousingWeiFangPay  extends IdEntity implements Serializable {
		
	private String taskid;
	private String payDate;						//入账日期
	private String markDate;					//汇缴年月
	private String mark;						//摘要
	private String inMoney;					 	//收入（元）
	private String outMoney;					//支出（元）
	private String balance;						//余额
	private String companyName;					//单位名称
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
	public String getMarkDate() {
		return markDate;
	}
	public void setMarkDate(String markDate) {
		this.markDate = markDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getInMoney() {
		return inMoney;
	}
	public void setInMoney(String inMoney) {
		this.inMoney = inMoney;
	}
	public String getOutMoney() {
		return outMoney;
	}
	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Override
	public String toString() {
		return "HousingWeiFangPay [taskid=" + taskid + ", payDate=" + payDate + ", markDate=" + markDate + ", mark="
				+ mark + ", inMoney=" + inMoney + ", outMoney=" + outMoney + ", balance=" + balance + ", companyName="
				+ companyName + "]";
	}
	
}
