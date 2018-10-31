package com.microservice.dao.entity.crawler.housing.panjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 盘锦公积金用户信息
 */
@Entity
@Table(name="housing_panjin_userinfo")
public class HousingPanjinUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String username;// 姓名
	private String accountNum;// 个人账号
	private String companyAccount;// 单位账号
	private String companyName;// 单位
	private String idnum;// 身份证号码
	private String queryCardnum;// 查询卡卡号
	private String accountState;// 账户状态
	private String crrountBalance;// 当前余额
	private String lastBalance;// 往年余额
	private String yearBalance;// 本年余额
	private String openTime;// 账户开户时间
	private String lastPaytime;// 缴至年月
	private String companyRatio;// 单位缴率
	private String personRatio;// 职工缴率
	private String companyPay;// 单位缴额
	private String personPay;// 职工缴额
	private String monthPayBase;// 工资基数
	private String monthpay;// 月缴存额
	private String drawTimes;// 提取次数
	private String drawAmount;// 本年提取金额
	private String lastInterest;// 上年结息额
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

	public String getQueryCardnum() {
		return queryCardnum;
	}

	public void setQueryCardnum(String queryCardnum) {
		this.queryCardnum = queryCardnum;
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

	public String getLastBalance() {
		return lastBalance;
	}

	public void setLastBalance(String lastBalance) {
		this.lastBalance = lastBalance;
	}

	public String getYearBalance() {
		return yearBalance;
	}

	public void setYearBalance(String yearBalance) {
		this.yearBalance = yearBalance;
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

	public String getMonthPayBase() {
		return monthPayBase;
	}

	public void setMonthPayBase(String monthPayBase) {
		this.monthPayBase = monthPayBase;
	}

	public String getDrawTimes() {
		return drawTimes;
	}

	public void setDrawTimes(String drawTimes) {
		this.drawTimes = drawTimes;
	}

	public String getDrawAmount() {
		return drawAmount;
	}

	public void setDrawAmount(String drawAmount) {
		this.drawAmount = drawAmount;
	}

	public String getLastInterest() {
		return lastInterest;
	}

	public void setLastInterest(String lastInterest) {
		this.lastInterest = lastInterest;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HousingPanjinUserInfo [username=" + username + ", accountNum=" + accountNum + ", companyAccount="
				+ companyAccount + ", companyName=" + companyName + ", idnum=" + idnum + ", queryCardnum="
				+ queryCardnum + ", accountState=" + accountState + ", crrountBalance=" + crrountBalance
				+ ", lastBalance=" + lastBalance + ", yearBalance=" + yearBalance + ", openTime=" + openTime
				+ ", lastPaytime=" + lastPaytime + ", companyRatio=" + companyRatio + ", personRatio=" + personRatio
				+ ", companyPay=" + companyPay + ", personPay=" + personPay + ", monthPayBase=" + monthPayBase
				+ ", monthpay=" + monthpay + ", drawTimes=" + drawTimes + ", drawAmount=" + drawAmount
				+ ", lastInterest=" + lastInterest + ", taskid=" + taskid + "]";
	}

	public HousingPanjinUserInfo(String username, String accountNum, String companyAccount, String companyName,
			String idnum, String queryCardnum, String accountState, String crrountBalance, String lastBalance,
			String yearBalance, String openTime, String lastPaytime, String companyRatio, String personRatio,
			String companyPay, String personPay, String monthPayBase, String monthpay, String drawTimes,
			String drawAmount, String lastInterest, String taskid) {
		super();
		this.username = username;
		this.accountNum = accountNum;
		this.companyAccount = companyAccount;
		this.companyName = companyName;
		this.idnum = idnum;
		this.queryCardnum = queryCardnum;
		this.accountState = accountState;
		this.crrountBalance = crrountBalance;
		this.lastBalance = lastBalance;
		this.yearBalance = yearBalance;
		this.openTime = openTime;
		this.lastPaytime = lastPaytime;
		this.companyRatio = companyRatio;
		this.personRatio = personRatio;
		this.companyPay = companyPay;
		this.personPay = personPay;
		this.monthPayBase = monthPayBase;
		this.monthpay = monthpay;
		this.drawTimes = drawTimes;
		this.drawAmount = drawAmount;
		this.lastInterest = lastInterest;
		this.taskid = taskid;
	}

	public HousingPanjinUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
