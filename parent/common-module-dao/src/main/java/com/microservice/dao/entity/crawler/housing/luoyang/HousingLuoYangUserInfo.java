package com.microservice.dao.entity.crawler.housing.luoyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_luoyang_userinfo",indexes = {@Index(name = "index_housing_luoyang_userinfo_taskid", columnList = "taskid")})
public class HousingLuoYangUserInfo extends IdEntity implements Serializable{

	private String name;             //姓名
	private String idCard;           //身份证号
	private String personalAccount;  //个人帐号
	private String phone;            //电话号码
	private String company;          //单位名称
	private String unitAccount;      //单位帐号
	private String administration;   //缴存管理部
	private String depositBank;      //缴存银行
	private String proportion;       //缴存比例
	private String base;             //工资基数
	private String amountPaid;       //月汇缴额	
	private String fee;              //缴存余额
	private String state;            //账户状态
	private String date;             //开户日期
	private String years;            //缴至年月
	private String bank;             //绑定银行
	private String cardNumber;       //绑定银行卡号
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingLuoYangUserInfo [name=" + name + ", idCard=" + idCard + ", personalAccount=" + personalAccount
				+",phone=" + phone + ", company=" + company + ", unitAccount=" + unitAccount
				+",administration=" + administration + ", depositBank=" + depositBank + ", proportion=" + proportion
				+",base=" + base + ", amountPaid=" + amountPaid + ", fee=" + fee
				+",state=" + state + ", date=" + date + ", years=" + years
				+ ", bank=" + bank + ", cardNumber=" + cardNumber + ", taskid=" + taskid + "]";
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

	public String getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getUnitAccount() {
		return unitAccount;
	}

	public void setUnitAccount(String unitAccount) {
		this.unitAccount = unitAccount;
	}

	public String getAdministration() {
		return administration;
	}

	public void setAdministration(String administration) {
		this.administration = administration;
	}

	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	public String getProportion() {
		return proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
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

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
