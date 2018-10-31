package com.microservice.dao.entity.crawler.housing.heihe;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 黑河公积金用户信息
 */
@Entity
@Table(name="housing_heihe_userinfo")
public class HousingHeiheUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String username;// 姓名
	private String accountNum;// 	职工账号
	private String companyAccount;// 单位账号
	private String companyName;// 单位名称
	private String idnum;// 身份证号码
	private String openTime;// 账户开户时间
	private String accountState;// 账户状态
	private String crrountBalance;// 	账户余额
	private String lastPaytime;// 缴至年月
	private String monthpay;//	月缴存额
	private String companyPay;// 	单位缴额
	private String personPay;// 	职工缴额
	private String hasCard;//	是否持有联名卡
	private String drawAmount;// 本年提取金额
	private String taskid;
	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMonthpay() {
		return monthpay;
	}

	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}

	public String getCompanyAccount() {
		return companyAccount;
	}

	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getCrrountBalance() {
		return crrountBalance;
	}

	public void setCrrountBalance(String crrountBalance) {
		this.crrountBalance = crrountBalance;
	}
	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getLastPaytime() {
		return lastPaytime;
	}

	public void setLastPaytime(String lastPaytime) {
		this.lastPaytime = lastPaytime;
	}

	public String getHasCard() {
		return hasCard;
	}

	public void setHasCard(String hasCard) {
		this.hasCard = hasCard;
	}

	public String getCompanyPay() {
		return companyPay;
	}

	public void setCompanyPay(String companyPay) {
		this.companyPay = companyPay;
	}

	public String getPersonPay() {
		return personPay;
	}

	public void setPersonPay(String personPay) {
		this.personPay = personPay;
	}
	public String getDrawAmount() {
		return drawAmount;
	}

	public void setDrawAmount(String drawAmount) {
		this.drawAmount = drawAmount;
	}


	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingHeiheUserInfo [username=" + username + ", accountNum=" + accountNum + ", companyAccount="
				+ companyAccount + ", companyName=" + companyName + ", idnum=" + idnum + ", openTime=" + openTime
				+ ", accountState=" + accountState + ", crrountBalance=" + crrountBalance + ", lastPaytime="
				+ lastPaytime + ", monthpay=" + monthpay + ", companyPay=" + companyPay + ", personPay=" + personPay
				+ ", hasCard=" + hasCard + ", drawAmount=" + drawAmount + ", taskid=" + taskid + "]";
	}
	public HousingHeiheUserInfo(String username, String accountNum, String companyAccount, String companyName,
			String idnum, String openTime, String accountState, String crrountBalance, String lastPaytime,
			String monthpay, String companyPay, String personPay, String hasCard, String drawAmount, String taskid) {
		super();
		this.username = username;
		this.accountNum = accountNum;
		this.companyAccount = companyAccount;
		this.companyName = companyName;
		this.idnum = idnum;
		this.openTime = openTime;
		this.accountState = accountState;
		this.crrountBalance = crrountBalance;
		this.lastPaytime = lastPaytime;
		this.monthpay = monthpay;
		this.companyPay = companyPay;
		this.personPay = personPay;
		this.hasCard = hasCard;
		this.drawAmount = drawAmount;
		this.taskid = taskid;
	}

	public HousingHeiheUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
