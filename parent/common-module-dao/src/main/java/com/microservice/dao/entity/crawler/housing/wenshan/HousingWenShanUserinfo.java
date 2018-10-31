package com.microservice.dao.entity.crawler.housing.wenshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_wenshan_userinfo",indexes = {@Index(name = "index_housing_wenshan_userinfo_taskid", columnList = "taskid")})
public class HousingWenShanUserinfo extends IdEntity implements Serializable{
	private String taskid;
	private String staffName;						//姓名
	private String idNum;							//证件号码
	private String staffNum;						//个人账号
	private String companyName;						//单位名称
	private String base;                            //个人缴存基数
	private String personalAmount;                  //个人月缴存额
	private String companyAmount;                   //单位月缴存额
	private String monthPay;						//月缴存额
	private String balance;						    //个人账户余额
	private String date;					        //缴至年月
	private String openDate;                        //开户日期
	private String state;							//个人账户状态
	private String companyPercent;					//单位比例
	private String personalPercent;                 //个人比例
	private String phone;                           //手机号码

	@Override
	public String toString() {
		return "HousingWenShanUserinfo [taskid=" + taskid + ", staffName=" + staffName + ", staffNum=" + staffNum
				+ ", companyName=" + companyName + ", state=" + state + ", idNum=" + idNum
				+ ", base=" + base + ", openDate=" + openDate + ", monthPay=" +monthPay + ", phone=" + phone
				+ ", personalAmount=" + personalAmount + ", companyAmount=" + companyAmount + ", balance=" + balance
				+ ", personalPercent=" + personalPercent + ", date=" + date + ",  companyPercent=" + companyPercent+ "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getCompanyAmount() {
		return companyAmount;
	}

	public void setCompanyAmount(String companyAmount) {
		this.companyAmount = companyAmount;
	}

	public String getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(String monthPay) {
		this.monthPay = monthPay;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCompanyPercent() {
		return companyPercent;
	}

	public void setCompanyPercent(String companyPercent) {
		this.companyPercent = companyPercent;
	}

	public String getPersonalPercent() {
		return personalPercent;
	}

	public void setPersonalPercent(String personalPercent) {
		this.personalPercent = personalPercent;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
	
}
