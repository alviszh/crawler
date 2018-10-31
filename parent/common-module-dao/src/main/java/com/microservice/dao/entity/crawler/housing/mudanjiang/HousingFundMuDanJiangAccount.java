package com.microservice.dao.entity.crawler.housing.mudanjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_mudanjiang_account",indexes = {@Index(name = "index_housing_mudanjiang_account_taskid", columnList = "taskid")})
public class HousingFundMuDanJiangAccount extends IdEntity implements Serializable{

	
	private String taskid;
	
	private String company;//单位名称
	
	private String companyNum;//单位账号
	
	private String payDate;//交易日期
	
	private String bussType;//业务类型
	
	private String money;//发生额

	@Override
	public String toString() {
		return "HousingFundMuDanJiangAccount [taskid=" + taskid + ", company=" + company + ", companyNum=" + companyNum
				+ ", payDate=" + payDate + ", bussType=" + bussType + ", money=" + money + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getBussType() {
		return bussType;
	}

	public void setBussType(String bussType) {
		this.bussType = bussType;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
	
}
