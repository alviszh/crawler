package com.microservice.dao.entity.crawler.housing.fuzhou2;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_fuzhou2_account")
public class HousingFuzhou2Account extends IdEntity implements Serializable{

	
	private String taskid;
	private String flowNumber;//流水号
	private String accountType;//账户类型
	private String personNum;//个人账号
	private String dateOfOccurrence;//发生日期
	private String summaryNote;//摘要备注
	private String amountOfOccurrence;//发生金额;
	private String balance;//余额
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getFlowNumber() {
		return flowNumber;
	}
	public void setFlowNumber(String flowNumber) {
		this.flowNumber = flowNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getPersonNum() {
		return personNum;
	}
	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}
	public String getDateOfOccurrence() {
		return dateOfOccurrence;
	}
	public void setDateOfOccurrence(String dateOfOccurrence) {
		this.dateOfOccurrence = dateOfOccurrence;
	}
	public String getSummaryNote() {
		return summaryNote;
	}
	public void setSummaryNote(String summaryNote) {
		this.summaryNote = summaryNote;
	}
	public String getAmountOfOccurrence() {
		return amountOfOccurrence;
	}
	public void setAmountOfOccurrence(String amountOfOccurrence) {
		this.amountOfOccurrence = amountOfOccurrence;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	

	
}
