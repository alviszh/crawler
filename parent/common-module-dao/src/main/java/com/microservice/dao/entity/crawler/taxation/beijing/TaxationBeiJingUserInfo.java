package com.microservice.dao.entity.crawler.taxation.beijing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="taxation_beijing_userinfo",indexes = {@Index(name = "index_taxation_beijing_userinfo_taskid", columnList = "taskid")}) 
public class TaxationBeiJingUserInfo extends IdEntity{
	
	private String taskid;
	
	private String name;
	
	private String nation;
	
	private String cardType;
	
	private String cardNum;

	@Override
	public String toString() {
		return "TaxationBeiJingUserInfo [taskid=" + taskid + ", name=" + name + ", nation=" + nation + ", cardType="
				+ cardType + ", cardNum=" + cardNum + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	
	
}
