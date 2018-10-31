package com.microservice.dao.entity.crawler.housing.haikou;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_haikou_pay")
public class HousingHaiKouPay  extends IdEntity implements Serializable {
	private String taskid;
	private String payDate;								//交易日期
	private String serialNum;							//流水号
	private String idNum;								//证件号码
	private String name;								//姓名
	private String companyName;							//单位名称
	private String mark;								//摘要
	private String startDate;							//开始年月
	private String endDate;								//截止年月
	private String changeType;							//发生额方向
	private String changeMoney;							//发生额（元）
	private String balance;								//余额（元）
	private String checkUnit;							//经办机构
	private String checkStaff;							//经办柜员
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
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
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getChangeMoney() {
		return changeMoney;
	}
	public void setChangeMoney(String changeMoney) {
		this.changeMoney = changeMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCheckUnit() {
		return checkUnit;
	}
	public void setCheckUnit(String checkUnit) {
		this.checkUnit = checkUnit;
	}
	public String getCheckStaff() {
		return checkStaff;
	}
	public void setCheckStaff(String checkStaff) {
		this.checkStaff = checkStaff;
	}
	@Override
	public String toString() {
		return "HousingHaiKouPay [taskid=" + taskid + ", payDate=" + payDate + ", serialNum=" + serialNum + ", idNum="
				+ idNum + ", name=" + name + ", companyName=" + companyName + ", mark=" + mark + ", startDate="
				+ startDate + ", endDate=" + endDate + ", changeType=" + changeType + ", changeMoney=" + changeMoney
				+ ", balance=" + balance + ", checkUnit=" + checkUnit + ", checkStaff=" + checkStaff + "]";
	}
	
}
