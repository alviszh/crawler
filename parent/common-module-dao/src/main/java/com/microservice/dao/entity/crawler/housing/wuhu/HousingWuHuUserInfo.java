package com.microservice.dao.entity.crawler.housing.wuhu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 芜湖公积金用户信息
 */
@Entity
@Table(name="housing_wuhu_userinfo")
public class HousingWuHuUserInfo extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1221206979457794498L;
	private String username;// 姓名
	private String gender;// 性别
	private String idnum;// 身份证
	private String state;// 账户状态
	private String unitaccount;// 单位公积金账号
	private String personaccount;// 个人公积金账号
	private String monthpay;// 月缴额
	private String lastaccountdate;// 最近时间
	private String creditramount;// 最新到账金额
	private String accountbalance;// 账户余额
	private String foundloan;// 公积金贷款情况
	private String company;// 单位名称
	private String acountamount;// 公积金账户总金额
	private String taskid;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUnitaccount() {
		return unitaccount;
	}

	public void setUnitaccount(String unitaccount) {
		this.unitaccount = unitaccount;
	}

	public String getPersonaccount() {
		return personaccount;
	}

	public void setPersonaccount(String personaccount) {
		this.personaccount = personaccount;
	}

	public String getMonthpay() {
		return monthpay;
	}

	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}

	public String getLastaccountdate() {
		return lastaccountdate;
	}

	public void setLastaccountdate(String lastaccountdate) {
		this.lastaccountdate = lastaccountdate;
	}

	public String getCreditramount() {
		return creditramount;
	}

	public void setCreditramount(String creditramount) {
		this.creditramount = creditramount;
	}

	public String getAccountbalance() {
		return accountbalance;
	}

	public void setAccountbalance(String accountbalance) {
		this.accountbalance = accountbalance;
	}

	public String getFoundloan() {
		return foundloan;
	}

	public void setFoundloan(String foundloan) {
		this.foundloan = foundloan;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAcountamount() {
		return acountamount;
	}

	public void setAcountamount(String acountamount) {
		this.acountamount = acountamount;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
    
	@Override
	public String toString() {
		return "HousingWuHuUserInfo [username=" + username + ", gender=" + gender + ", idnum=" + idnum + ", state="
				+ state + ", unitaccount=" + unitaccount + ", personaccount=" + personaccount + ", monthpay=" + monthpay
				+ ", lastaccountdate=" + lastaccountdate + ", creditramount=" + creditramount + ", accountbalance="
				+ accountbalance + ", foundloan=" + foundloan + ", company=" + company + ", acountamount="
				+ acountamount + ", taskid=" + taskid + "]";
	}
	public HousingWuHuUserInfo(String username, String gender, String idnum, String state, String unitaccount,
			String personaccount, String monthpay, String lastaccountdate, String creditramount, String accountbalance,
			String foundloan, String company, String acountamount, String taskid) {
		super();
		this.username = username;
		this.gender = gender;
		this.idnum = idnum;
		this.state = state;
		this.unitaccount = unitaccount;
		this.personaccount = personaccount;
		this.monthpay = monthpay;
		this.lastaccountdate = lastaccountdate;
		this.creditramount = creditramount;
		this.accountbalance = accountbalance;
		this.foundloan = foundloan;
		this.company = company;
		this.acountamount = acountamount;
		this.taskid = taskid;
	}
	public HousingWuHuUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
