package com.microservice.dao.entity.crawler.housing.suzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_suzhou_account_detail")
public class HousingSuzhouAccountDetail extends IdEntity implements Serializable{

	private String housingNum;//个人公积金账号
	
	private String name;//姓名
	
	private String postingDate;//入账日期
	
	private String month;//所属月份
	
	private String businessType;//业务类型
	
	private String taskid;
	
	private String direction;//方向
	
	private String occurrenceAmount;//发生额
	
	private String balance;//余额
	
	private String filingOffice;//受理处
	
	private String remarks;//备注

	public String getHousingNum() {
		return housingNum;
	}

	public void setHousingNum(String housingNum) {
		this.housingNum = housingNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getOccurrenceAmount() {
		return occurrenceAmount;
	}

	public void setOccurrenceAmount(String occurrenceAmount) {
		this.occurrenceAmount = occurrenceAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getFilingOffice() {
		return filingOffice;
	}

	public void setFilingOffice(String filingOffice) {
		this.filingOffice = filingOffice;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
