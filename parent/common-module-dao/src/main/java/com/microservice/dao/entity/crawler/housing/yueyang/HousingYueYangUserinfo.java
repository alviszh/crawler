package com.microservice.dao.entity.crawler.housing.yueyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name  ="housing_yueyang_userinfo",indexes = {@Index(name = "index_housing_yueyang_userinfo_taskid", columnList = "taskid")})
public class HousingYueYangUserinfo extends IdEntity implements Serializable{
	private String companyNum;             //单位账号
	private String companyName;			   //单位名称
	private String personalNum;            //个人账号
	private String name;				   //姓名
	private String idNum;				   //证件号码
	private String telephone;              //固定电话
	private String phone;                  //手机号码	
	private String address;                //家庭住址
	private String pay;                    //月缴存额
	private String balance;				   //个人账户余额	
	private String lastPaytime;            //缴至年月
	private String base;		           //个人缴存基数
	private String companyRatio;           //单位缴存比例
	private String personalRatio;          //个人缴存比例
	private String state;                  //个人账户状态
	private String openDate;               //开户日期
	private String loan;                   //是否贷款
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingYueYangUserinfo [companyNum=" + companyNum + ", name=" + name + ", idNum=" + idNum
				+ ", companyName=" + companyName + ", pay=" + pay + ", balance=" + balance
				+ ", personalNum=" + personalNum + ", telephone=" + telephone + ", phone=" + phone
				+ ", lastPaytime=" + lastPaytime + ", address=" + address+ ", base=" + base
				+ ", companyRatio=" + companyRatio + ", personalRatio=" + personalRatio+ ", state=" + state
				+ ", openDate=" + openDate + ", loan=" + loan+ ", taskid=" + taskid + "]";
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLastPaytime() {
		return lastPaytime;
	}

	public void setLastPaytime(String lastPaytime) {
		this.lastPaytime = lastPaytime;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPersonalNum() {
		return personalNum;
	}

	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCompanyRatio() {
		return companyRatio;
	}

	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}

	public String getPersonalRatio() {
		return personalRatio;
	}

	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getLoan() {
		return loan;
	}

	public void setLoan(String loan) {
		this.loan = loan;
	}

	
	
	
}
