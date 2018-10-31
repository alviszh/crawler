package com.microservice.dao.entity.crawler.housing.enshi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_enshi_userinfo",indexes = {@Index(name = "index_housing_enshi_userinfo_taskid", columnList = "taskid")})
public class HousingEnShiUserInfo extends IdEntity implements Serializable{
	private String name;             //职工姓名
	private String personalAccount;  //个人帐号
	private String company;          //单位名称
	private String companyAccount;   //单位帐号
	private String fee;              //账户余额（元）
	private String companyAmount;    //单位缴存额（元）
	private String personalAmount;   //个人缴存额（元）
	private String mode;             //冻结方式
	private String guarantee;        //担保金额（元）
	private String state;            //账户状态
	private String date;             //开户年月
	private String lastDate;         //缴至年月
	
	private String taskid;

	@Override
	public String toString() {
		return "HousingEnShiUserInfo [name=" + name + ", personalAccount=" + personalAccount + ", company=" + company
				+",companyAccount=" + companyAccount + ", fee=" + fee + ", companyAmount=" + companyAmount
				+",personalAmount=" + personalAmount + ", mode=" + mode + ", guarantee=" + guarantee
				+",state=" + state + ", date=" + date + ", lastDate=" + lastDate
			    + ", taskid=" + taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyAccount() {
		return companyAccount;
	}

	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getCompanyAmount() {
		return companyAmount;
	}

	public void setCompanyAmount(String companyAmount) {
		this.companyAmount = companyAmount;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getGuarantee() {
		return guarantee;
	}

	public void setGuarantee(String guarantee) {
		this.guarantee = guarantee;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
