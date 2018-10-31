package com.microservice.dao.entity.crawler.insurance.suzhou;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_suzhou_user")
public class InsuranceSuzhouUser extends IdEntity {
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String personalNumber;//个人编号
	private String name;//姓名
	private String cardNum;//身份证号
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonalNumber() {
		return personalNumber;
	}
	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	
	
	
	
}
