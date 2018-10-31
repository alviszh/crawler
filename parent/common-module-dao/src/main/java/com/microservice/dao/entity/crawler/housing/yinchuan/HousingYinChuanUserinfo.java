package com.microservice.dao.entity.crawler.housing.yinchuan;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_yinchuan_userinfo")
public class HousingYinChuanUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String name;							//姓名
	private String companyName;						//单位名称
	private String openDate;						//开户日期
	private String idNum;							//身份证号
	private String payDate;							//缴至年月
	private String personMonthPay;					//个人月缴存额
	private String monthPay;						//月汇缴额
	private String balance;							//公积金余额
	private String state;							//缴存状态
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPersonMonthPay() {
		return personMonthPay;
	}
	public void setPersonMonthPay(String personMonthPay) {
		this.personMonthPay = personMonthPay;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "HousingYinChuanUserinfo [taskid=" + taskid + ", name=" + name + ", companyName=" + companyName
				+ ", openDate=" + openDate + ", idNum=" + idNum + ", payDate=" + payDate + ", personMonthPay="
				+ personMonthPay + ", monthPay=" + monthPay + ", balance=" + balance + ", state=" + state + "]";
	}
	

}
