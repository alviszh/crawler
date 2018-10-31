package com.microservice.dao.entity.crawler.housing.wuhai;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_wuhai_userinfo")
public class HousingWuHaiUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String name;							//姓   名
	private String idNum;							//身份证号
	private String company;							//工作单位
	private String lastYearBalance;					//上年结转
	private String payDate;							//缴至年月
	private String nowPay;							//本年缴存
	private String nowOut;							//本年提取
	private String nowInterest;						//本年结息
	private String balance;							//余额
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
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLastYearBalance() {
		return lastYearBalance;
	}
	public void setLastYearBalance(String lastYearBalance) {
		this.lastYearBalance = lastYearBalance;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getNowPay() {
		return nowPay;
	}
	public void setNowPay(String nowPay) {
		this.nowPay = nowPay;
	}
	public String getNowOut() {
		return nowOut;
	}
	public void setNowOut(String nowOut) {
		this.nowOut = nowOut;
	}
	public String getNowInterest() {
		return nowInterest;
	}
	public void setNowInterest(String nowInterest) {
		this.nowInterest = nowInterest;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "HousingWuHaiUserinfo [taskid=" + taskid + ", name=" + name + ", idNum=" + idNum + ", company=" + company
				+ ", lastYearBalance=" + lastYearBalance + ", payDate=" + payDate + ", nowPay=" + nowPay + ", nowOut="
				+ nowOut + ", nowInterest=" + nowInterest + ", balance=" + balance + "]";
	}
	
}
