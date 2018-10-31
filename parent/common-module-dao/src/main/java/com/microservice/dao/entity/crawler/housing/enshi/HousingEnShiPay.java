package com.microservice.dao.entity.crawler.housing.enshi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_enshi_pay",indexes = {@Index(name = "index_housing_enshi_pay_taskid", columnList = "taskid")})
public class HousingEnShiPay extends IdEntity implements Serializable{
	private String years;            //日期
	private String abstra;           //摘要
	private String expenditure;      //发生额（元）
	private String balance;          //余额(元)
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingEnShiPay [years=" + years + ", abstra=" + abstra
				 + ", expenditure=" + expenditure + ", balance=" + balance
				+ ", taskid=" + taskid + "]";
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getAbstra() {
		return abstra;
	}

	public void setAbstra(String abstra) {
		this.abstra = abstra;
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
