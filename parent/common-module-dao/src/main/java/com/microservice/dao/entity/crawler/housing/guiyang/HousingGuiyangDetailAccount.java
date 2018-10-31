package com.microservice.dao.entity.crawler.housing.guiyang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * @description: 公积金 - 贵阳 - 账户明细
 * @author: zz 
 * 
 * 
 */
@Entity
@Table(name="housing_guiyang_detailaccount",indexes = {@Index(name = "index_housing_guiyang_detailaccount_taskid", columnList = "taskid")})
public class HousingGuiyangDetailAccount extends IdEntity {
	
	private String taskid;
	private String accountDate;						//时间
	private String processType;						//处理类型
	private String payYear;							//汇缴年月
	private String income;							//收入
	private String expend;							//支出
	private String balance;							//余额
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getPayYear() {
		return payYear;
	}
	public void setPayYear(String payYear) {
		this.payYear = payYear;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getExpend() {
		return expend;
	}
	public void setExpend(String expend) {
		this.expend = expend;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	

}
