package com.microservice.dao.entity.crawler.housing.mianyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 绵阳公积金用户信息
 */
@Entity
@Table(name="housing_mianyang_userinfo",indexes = {@Index(name = "index_housing_mianyang_userinfo_taskid", columnList = "taskid")})
public class HousingMianYangUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;	
	private String personalAccount;//个人账号
	private String username;//姓名
	private String idtype;//证件类型
	private String idnum;//证件号码
	private String sex;//性别
	private String birthDate;//出生年月
	private String balance;//个人账户余额
	private String payBase;//缴存基数
	private String companyMonthpayAmount;//单位月缴存额
	private String personMonthpayAmount;//个人月缴存额
	private String financeMonthpayAmount;//财政月缴存额
	private String monthpaySum;//月缴存额合计
	private String state;//个人账户状态
	private String loanState;//贷款状态
	private String freezeState;//冻结状态
	private String openDate;//开户日期
	private String sealDate;//封存日期
	private String startDate;//个人起缴年月
	private String telphone;//手机号码
	private String taskid;

	public String getPersonalAccount() {
		return personalAccount;
	}
	public void setPersonalAccount(String personalAccount) {
		this.personalAccount = personalAccount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getCompanyMonthpayAmount() {
		return companyMonthpayAmount;
	}

	public void setCompanyMonthpayAmount(String companyMonthpayAmount) {
		this.companyMonthpayAmount = companyMonthpayAmount;
	}

	public String getPersonMonthpayAmount() {
		return personMonthpayAmount;
	}

	public void setPersonMonthpayAmount(String personMonthpayAmount) {
		this.personMonthpayAmount = personMonthpayAmount;
	}

	public String getFinanceMonthpayAmount() {
		return financeMonthpayAmount;
	}

	public void setFinanceMonthpayAmount(String financeMonthpayAmount) {
		this.financeMonthpayAmount = financeMonthpayAmount;
	}

	public String getMonthpaySum() {
		return monthpaySum;
	}

	public void setMonthpaySum(String monthpaySum) {
		this.monthpaySum = monthpaySum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLoanState() {
		return loanState;
	}

	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}

	public String getFreezeState() {
		return freezeState;
	}

	public void setFreezeState(String freezeState) {
		this.freezeState = freezeState;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getSealDate() {
		return sealDate;
	}

	public void setSealDate(String sealDate) {
		this.sealDate = sealDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HousingMianYangUserInfo [personalAccount=" + personalAccount + ", username=" + username + ", idtype="
				+ idtype + ", idnum=" + idnum + ", sex=" + sex + ", birthDate=" + birthDate + ", balance=" + balance
				+ ", payBase=" + payBase + ", companyMonthpayAmount=" + companyMonthpayAmount
				+ ", personMonthpayAmount=" + personMonthpayAmount + ", financeMonthpayAmount=" + financeMonthpayAmount
				+ ", monthpaySum=" + monthpaySum + ", state=" + state + ", loanState=" + loanState + ", freezeState="
				+ freezeState + ", openDate=" + openDate + ", sealDate=" + sealDate + ", startDate=" + startDate
				+ ", telphone=" + telphone + ", taskid=" + taskid + "]";
	}
	public HousingMianYangUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingMianYangUserInfo(String personalAccount, String username, String idtype, String idnum, String sex,
			String birthDate, String balance, String payBase, String companyMonthpayAmount, String personMonthpayAmount,
			String financeMonthpayAmount, String monthpaySum, String state, String loanState, String freezeState,
			String openDate, String sealDate, String startDate, String telphone, String taskid) {
		super();
		this.personalAccount = personalAccount;
		this.username = username;
		this.idtype = idtype;
		this.idnum = idnum;
		this.sex = sex;
		this.birthDate = birthDate;
		this.balance = balance;
		this.payBase = payBase;
		this.companyMonthpayAmount = companyMonthpayAmount;
		this.personMonthpayAmount = personMonthpayAmount;
		this.financeMonthpayAmount = financeMonthpayAmount;
		this.monthpaySum = monthpaySum;
		this.state = state;
		this.loanState = loanState;
		this.freezeState = freezeState;
		this.openDate = openDate;
		this.sealDate = sealDate;
		this.startDate = startDate;
		this.telphone = telphone;
		this.taskid = taskid;
	}
	
}
