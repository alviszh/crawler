package com.microservice.dao.entity.crawler.insurance.sz.shanxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi_medical",indexes = {@Index(name = "index_insurance_sz_shanxi_medical_taskid", columnList = "taskid")})
public class InsuranceSZShanXiMedical extends IdEntity{
	private String type;//缴费类型
	private String startDate;//开始年月
	private String endDate;//截止年月
	private String payMoney;//缴费金额
	private String personalPay;//其中个人缴费
	private String taskid;
	
	@Override
	public String toString() {
		return "InsuranceSZShanXiMedical [type=" + type + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", payMoney=" + payMoney + ", personalPay=" + personalPay + ", taskid=" + taskid + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getPersonalPay() {
		return personalPay;
	}
	public void setPersonalPay(String personalPay) {
		this.personalPay = personalPay;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
