package com.microservice.dao.entity.crawler.housing.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="housing_taizhou_userinfo",indexes = {@Index(name = "index_housing_taizhou_userinfo_taskid", columnList = "taskid")})
public class HousingFundTaiZhouUserInfo extends IdEntity{

	private String taskid;
	private String num;
	
	private String name;
	
	private String idCard;
	private String fee;
	private String monthPay;
	private String status;
	private String payDate;
	
	private String bank;
	private String company;
	@Override
	public String toString() {
		return "HousingFundTaiZhouUserInfo [taskid=" + taskid + ", num=" + num + ", name=" + name + ", idCard=" + idCard
				+ ", fee=" + fee + ", monthPay=" + monthPay + ", status=" + status + ", payDate=" + payDate + ", bank="
				+ bank + ", company=" + company + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getMonthPay() {
		return monthPay;
	}
	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	
}
