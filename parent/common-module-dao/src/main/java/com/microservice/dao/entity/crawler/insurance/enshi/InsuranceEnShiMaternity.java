package com.microservice.dao.entity.crawler.insurance.enshi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_enshi_maternity",indexes = {@Index(name = "index_insurance_enshi_maternity_taskid", columnList = "taskid")})
public class InsuranceEnShiMaternity extends IdEntity{
	private String personalNum;//个人编号
	private String inDate;//费款所属期
	private String moneyType;//款项
	private String payType;//缴费类型
	private String base;//缴费基数
	private String payNow;//本期应缴
	private String personalMoney;//划入个人账户全额
	private String getFlag;//到账标志
	private String getDate;//到账日期
	private String sendFlag;//划账标志
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceEnShiMaternity [personalNum=" + personalNum + ", inDate=" + inDate + ", moneyType=" + moneyType
				+ ", payType=" + payType + ", base=" + base + ", payNow=" + payNow + ", personalMoney=" + personalMoney
				+ ", getFlag=" + getFlag + ", getDate=" + getDate + ", sendFlag=" + sendFlag + ", taskid=" + taskid
				+ "]";
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getPayNow() {
		return payNow;
	}
	public void setPayNow(String payNow) {
		this.payNow = payNow;
	}
	public String getPersonalMoney() {
		return personalMoney;
	}
	public void setPersonalMoney(String personalMoney) {
		this.personalMoney = personalMoney;
	}
	public String getGetFlag() {
		return getFlag;
	}
	public void setGetFlag(String getFlag) {
		this.getFlag = getFlag;
	}
	public String getGetDate() {
		return getDate;
	}
	public void setGetDate(String getDate) {
		this.getDate = getDate;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
