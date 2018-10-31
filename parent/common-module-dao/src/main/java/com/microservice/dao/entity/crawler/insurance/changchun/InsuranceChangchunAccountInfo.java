package com.microservice.dao.entity.crawler.insurance.changchun;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_changchun_accountinfo")
public class InsuranceChangchunAccountInfo extends IdEntity{
	
	@Column(name="task_id")
	private String taskid;	//uuid唯一标识
	private String personNumber;	//个人编号
	private String cardId;//身份证号
	private String personName;	//姓名
	private String sex;//性别
	private String nation;//民族
	private String accountTotal;//账户总额
	private String turnTotal;//其中转入总额(元)
	private String accountTotalPersonal;//账户总额个人部分
	private String turnPersonal;//其中转入个人部分(元)
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getAccountTotal() {
		return accountTotal;
	}
	public void setAccountTotal(String accountTotal) {
		this.accountTotal = accountTotal;
	}
	public String getTurnTotal() {
		return turnTotal;
	}
	public void setTurnTotal(String turnTotal) {
		this.turnTotal = turnTotal;
	}
	public String getAccountTotalPersonal() {
		return accountTotalPersonal;
	}
	public void setAccountTotalPersonal(String accountTotalPersonal) {
		this.accountTotalPersonal = accountTotalPersonal;
	}
	public String getTurnPersonal() {
		return turnPersonal;
	}
	public void setTurnPersonal(String turnPersonal) {
		this.turnPersonal = turnPersonal;
	}
	
}
