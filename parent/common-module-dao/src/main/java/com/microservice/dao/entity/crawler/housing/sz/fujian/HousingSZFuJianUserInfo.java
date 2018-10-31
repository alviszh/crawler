package com.microservice.dao.entity.crawler.housing.sz.fujian;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_sz_fujian_userinfo",indexes = {@Index(name = "index_housing_sz_fujian_userinfo_taskid", columnList = "taskid")})
public class HousingSZFuJianUserInfo extends IdEntity implements Serializable{
	private String name;             //姓名
	private String birth;            //出生年月
	private String idCard;           //个人证件号
	private String phone;            //移动电话
	private String comPhone;         //单位电话
	private String creditAccumula;   //历年贷方累计
	private String fee;              //上年结转余额
	private String payDate;			 //缴至年月
	private String companyRatio;     //单位缴交率
	private String personRatio;      //个人缴交率
	private String companyAmount;    //单位应缴额
	private String personalAmount;   //职工应缴额
	private String monthpay;         //月应缴额
	private String personalCard;     //个人公积金账号
	private String date;             //开户日期
	private String state;            //公积金账户状态
	private String balance;          //公积金账户余额
	private String companyCard;      //单位公积金账号
    private String base;             //缴存基数
	private String company;          //单位名称
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingSZFuJianUserInfo [name=" + name + ", birth=" + birth + ", idCard=" + idCard
				+",phone=" + phone + ", comPhone=" + comPhone + ", creditAccumula=" + creditAccumula
				+",fee=" + fee + ", payDate=" + payDate + ", companyRatio=" + companyRatio
				+",personRatio=" + personRatio + ", companyAmount=" + companyAmount + ", personalAmount=" + personalAmount
				+",monthpay=" + monthpay + ", personalCard=" + personalCard + ", date=" + date
				+",state=" + state + ", balance=" + balance + ", companyCard=" + companyCard
				+",base=" + base +",company=" + company + ", taskid=" + taskid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getComPhone() {
		return comPhone;
	}

	public void setComPhone(String comPhone) {
		this.comPhone = comPhone;
	}

	public String getCreditAccumula() {
		return creditAccumula;
	}

	public void setCreditAccumula(String creditAccumula) {
		this.creditAccumula = creditAccumula;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getCompanyRatio() {
		return companyRatio;
	}

	public void setCompanyRatio(String companyRatio) {
		this.companyRatio = companyRatio;
	}

	public String getPersonRatio() {
		return personRatio;
	}

	public void setPersonRatio(String personRatio) {
		this.personRatio = personRatio;
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

	public String getMonthpay() {
		return monthpay;
	}

	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}

	public String getPersonalCard() {
		return personalCard;
	}

	public void setPersonalCard(String personalCard) {
		this.personalCard = personalCard;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCompanyCard() {
		return companyCard;
	}

	public void setCompanyCard(String companyCard) {
		this.companyCard = companyCard;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	

}
