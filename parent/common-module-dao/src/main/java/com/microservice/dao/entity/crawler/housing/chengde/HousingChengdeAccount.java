package com.microservice.dao.entity.crawler.housing.chengde;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_chengde_account")
public class HousingChengdeAccount extends IdEntity implements Serializable{
	
	private String num;//账号
	
	private String name;//姓名
	
	private String companyPay;//单位缴存金额
	
	private String personPay;//个人缴存金额

	private String collectionPay;//缴存合计
	
	private String recentWithdrawal;//最近支取金额
	
	private String balance;//公积金余额
	
	private String taskid;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getPersonPay() {
		return personPay;
	}

	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}

	public String getCollectionPay() {
		return collectionPay;
	}

	public void setCollectionPay(String collectionPay) {
		this.collectionPay = collectionPay;
	}

	public String getRecentWithdrawal() {
		return recentWithdrawal;
	}

	public void setRecentWithdrawal(String recentWithdrawal) {
		this.recentWithdrawal = recentWithdrawal;
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


	
}
