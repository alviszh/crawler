package com.microservice.dao.entity.crawler.housing.jian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_jian_pay",indexes = {@Index(name = "index_housing_jian_pay_taskid", columnList = "taskid")})
public class HousingJiAnPay extends IdEntity implements Serializable{

	private String jndate;             //业务日期
	private String abstracts;          //摘要
	private String income;             //收入金额
	private String expenditure;        //支出金额
	
    private String taskid;
	
	@Override
	public String toString() {
		return "HousingJiAnPay [jndate=" + jndate + ", abstracts=" + abstracts
				+ ", income=" + income + ", expenditure=" + expenditure
				+ ", taskid=" + taskid + "]";
	}

	public String getJndate() {
		return jndate;
	}

	public void setJndate(String jndate) {
		this.jndate = jndate;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(String expenditure) {
		this.expenditure = expenditure;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
