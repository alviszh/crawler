package com.microservice.dao.entity.crawler.bank.citicchina;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="citicchina_debitcard_account",indexes = {@Index(name = "index_citicchina_debitcard_account_taskid", columnList = "taskid")}) 
public class CiticChinaDebitCardAccount extends IdEntity implements Serializable{

	private String cardNum;//交易卡号
	
	private String datea;//交易日期
	
	private String setMoney;//支出金额
	
	private String getMoney;//收入金额
	
	private String fee;//账户余额
	
	private String anotherOne;//对方
	
	private String company;//受理机构
	
	private String remark;//摘要
	
	private String status;//状态
	
	private String taskid;

	@Override
	public String toString() {
		return "CiticChinaDebitCardAccount [cardNum=" + cardNum + ", datea=" + datea + ", setMoney=" + setMoney
				+ ", getMoney=" + getMoney + ", fee=" + fee + ", anotherOne=" + anotherOne + ", company=" + company
				+ ", remark=" + remark + ", status=" + status + ", taskid=" + taskid + "]";
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getSetMoney() {
		return setMoney;
	}

	public void setSetMoney(String setMoney) {
		this.setMoney = setMoney;
	}

	public String getGetMoney() {
		return getMoney;
	}

	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	@Column(columnDefinition="text")
	public String getAnotherOne() {
		return anotherOne;
	}

	public void setAnotherOne(String anotherOne) {
		this.anotherOne = anotherOne;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
	
}
