package com.microservice.dao.entity.crawler.bank.hxbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="hxbchina_debitcard_userinfo",indexes = {@Index(name = "index_hxbchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class HxbChinaDebitCardUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1427701757826087200L;
	private String taskid;
//	客户姓名
	private String custName;
//	账号
	private String accoutNo;
//	币种
	private String currency;
//	储种
	private String storeType;
//	利率
	private String rate;
//	余额时间
	private String balTime;
//	余额
	private String balance;
//	可用余额
	private String availableBal;
//	状态
	private String state;
//	账户别名
	private String accOtherName;
//	所属卡/存折/主账号
	private String belongCard;
//	开户机构
	private String openOrganization;
//	开户日期
	private String openDate;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getAccoutNo() {
		return accoutNo;
	}
	public void setAccoutNo(String accoutNo) {
		this.accoutNo = accoutNo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getBalTime() {
		return balTime;
	}
	public void setBalTime(String balTime) {
		this.balTime = balTime;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getAvailableBal() {
		return availableBal;
	}
	public void setAvailableBal(String availableBal) {
		this.availableBal = availableBal;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAccOtherName() {
		return accOtherName;
	}
	public void setAccOtherName(String accOtherName) {
		this.accOtherName = accOtherName;
	}
	public String getBelongCard() {
		return belongCard;
	}
	public void setBelongCard(String belongCard) {
		this.belongCard = belongCard;
	}
	public String getOpenOrganization() {
		return openOrganization;
	}
	public void setOpenOrganization(String openOrganization) {
		this.openOrganization = openOrganization;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public HxbChinaDebitCardUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HxbChinaDebitCardUserInfo(String taskid, String custName, String accoutNo, String currency, String storeType,
			String rate, String balTime, String balance, String availableBal, String state, String accOtherName,
			String belongCard, String openOrganization, String openDate) {
		super();
		this.taskid = taskid;
		this.custName = custName;
		this.accoutNo = accoutNo;
		this.currency = currency;
		this.storeType = storeType;
		this.rate = rate;
		this.balTime = balTime;
		this.balance = balance;
		this.availableBal = availableBal;
		this.state = state;
		this.accOtherName = accOtherName;
		this.belongCard = belongCard;
		this.openOrganization = openOrganization;
		this.openDate = openDate;
	}
	
}
