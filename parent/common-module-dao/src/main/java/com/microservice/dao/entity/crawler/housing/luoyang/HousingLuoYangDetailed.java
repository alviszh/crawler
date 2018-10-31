package com.microservice.dao.entity.crawler.housing.luoyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_luoyang_detailed",indexes = {@Index(name = "index_housing_luoyang_detailed_taskid", columnList = "taskid")})
public class HousingLuoYangDetailed extends IdEntity implements Serializable{
	private String years;            //缴存年月
	private String time;             //入账时间
    private String companyName;      //单位名称
    private String income;           //收入(元)
	private String expenditure;      //支出(元)
	private String balance;          //当前余额(元)
    private String type;             //业务类型
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingLuoYangDetailed [years=" + years + ", time=" + time + ", companyName=" + companyName
				+ ", income=" + income + ", expenditure=" + expenditure + ", balance=" + balance
				+ ", type=" + type + ", taskid=" + taskid + "]";
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
