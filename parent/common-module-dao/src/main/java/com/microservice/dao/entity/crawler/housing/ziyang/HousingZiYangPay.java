package com.microservice.dao.entity.crawler.housing.ziyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_ziyang_pay",indexes = {@Index(name = "index_housing_ziyang_pay_taskid", columnList = "taskid")})
public class HousingZiYangPay extends IdEntity implements Serializable{
	private String years;            //日期
	private String type;             //业务类型
	private String abstra;           //摘要
	private String income;           //收入(元)
	private String expenditure;      //支出(元)
	private String balance;          //余额(元)
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingZiYangPay [years=" + years + ", type=" + type + ", abstra=" + abstra
				+ ", income=" + income + ", expenditure=" + expenditure + ", balance=" + balance
				+ ", taskid=" + taskid + "]";
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAbstra() {
		return abstra;
	}

	public void setAbstra(String abstra) {
		this.abstra = abstra;
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
