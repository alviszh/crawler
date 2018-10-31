package com.microservice.dao.entity.crawler.housing.hangzhou;

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
@Table(name  ="housing_hangzhou_recodetails",indexes = {@Index(name = "index_housing_hangzhou_recodetails_taskid", columnList = "taskid")})
public class HousingHangZhouRecoDetails extends IdEntity implements Serializable{
	private String date;        //记账日期
	private String abstrac;     //摘要
	private String increase;    //增加
	private String reduce;      //减少
	private String balance;     //余额
	private Integer userid;

	private String taskid;
	@Override
	public String toString() {
		return "HousingHangZhouRecoDetails [date=" + date + ",abstrac=" + abstrac
				+ ", increase=" + increase + ", reduce=" + reduce + ", balance=" + balance 
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
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
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getReduce() {
		return reduce;
	}
	public void setReduce(String reduce) {
		this.reduce = reduce;
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
