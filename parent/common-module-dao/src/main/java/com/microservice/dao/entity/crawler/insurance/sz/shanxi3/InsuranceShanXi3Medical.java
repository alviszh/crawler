package com.microservice.dao.entity.crawler.insurance.sz.shanxi3;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi3_medical")
public class InsuranceShanXi3Medical extends IdEntity{
	
	private String taskid;	
	private String year;					//缴费所属年月
	private String InsuranceType;			//险种类型
	private String payType;					//缴费类型
	private String paymentBase;				//缴费基数
	private String payScale;				//个人缴费比例
	private String payMoney;				//个人缴费金额
	private String asAccountScale;			//划入账户比例
	private String asAccountMoney;			//本次划入账户金额
	private String receivedDate;			//到帐日期
	
	@Override
	public String toString() {
		return "InsuranceShanXi3Medical [taskid=" + taskid + ", year=" + year + ", InsuranceType=" + InsuranceType
				+ ", payType=" + payType + ", paymentBase=" + paymentBase + ", payScale=" + payScale + ", payMoney="
				+ payMoney + ", asAccountScale=" + asAccountScale + ", asAccountMoney=" + asAccountMoney
				+ ", receivedDate=" + receivedDate + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getInsuranceType() {
		return InsuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		InsuranceType = insuranceType;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPaymentBase() {
		return paymentBase;
	}
	public void setPaymentBase(String paymentBase) {
		this.paymentBase = paymentBase;
	}
	public String getPayScale() {
		return payScale;
	}
	public void setPayScale(String payScale) {
		this.payScale = payScale;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getAsAccountScale() {
		return asAccountScale;
	}
	public void setAsAccountScale(String asAccountScale) {
		this.asAccountScale = asAccountScale;
	}
	public String getAsAccountMoney() {
		return asAccountMoney;
	}
	public void setAsAccountMoney(String asAccountMoney) {
		this.asAccountMoney = asAccountMoney;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}


}
