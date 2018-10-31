package com.microservice.dao.entity.crawler.housing.quanzhou;

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
@Table(name  ="housing_quanzhou_paydetails",indexes = {@Index(name = "index_housing_quanzhou_paydetails_taskid", columnList = "taskid")})
public class HousingQuanZhouPayDetails extends IdEntity implements Serializable{
	private String perAccount;        //个人账号
	private String date;              //日期
	private String abstrac;           //摘要
	private String withdrSum;         //支取金额
	private String depoBalance;       //缴存余额	
	private String balance;           //余额
	private Integer userid;

	private String taskid;
	@Override
	public String toString() {
		return "HousingQuanZhouPayDetails [perAccount=" + perAccount + ",date=" + date
				+ ", abstrac=" + abstrac + ", withdrSum=" + withdrSum + ", depoBalance="
				+ depoBalance+ ", balance=" + balance 
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	public String getPerAccount() {
		return perAccount;
	}
	public void setPerAccount(String perAccount) {
		this.perAccount = perAccount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAbstrac() {
		return abstrac;
	}
	public void setAbstrac(String abstrac) {
		this.abstrac = abstrac;
	}
	public String getWithdrSum() {
		return withdrSum;
	}
	public void setWithdrSum(String withdrSum) {
		this.withdrSum = withdrSum;
	}
	public String getDepoBalance() {
		return depoBalance;
	}
	public void setDepoBalance(String depoBalance) {
		this.depoBalance = depoBalance;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
