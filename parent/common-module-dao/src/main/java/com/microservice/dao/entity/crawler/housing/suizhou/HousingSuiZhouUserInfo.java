package com.microservice.dao.entity.crawler.housing.suizhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 公积金用户信息
 */
@Entity
@Table(name="housing_suizhou_userinfo")
public class HousingSuiZhouUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	private String username;// 姓名
	private String idnum;// 身份证号
	private String companyName;// 单位
	private String fundNum;// 个人公积金账号
	private String banlance;// 公积金余额
	private String state;// 个人账户状态
	private String incomeBase;// 工资基数
	private String lastMonth;// 缴至年月
	private String telephone;// 手机号码	
	private String taskid;
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

	public String getFundNum() {
		return fundNum;
	}

	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}

	public String getBanlance() {
		return banlance;
	}

	public void setBanlance(String banlance) {
		this.banlance = banlance;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public String getIncomeBase() {
		return incomeBase;
	}

	public void setIncomeBase(String incomeBase) {
		this.incomeBase = incomeBase;
	}

	public String getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(String lastMonth) {
		this.lastMonth = lastMonth;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingSuiZhouUserInfo [username=" + username + ", idnum=" + idnum + ", companyName=" + companyName
				+ ", fundNum=" + fundNum + ", banlance=" + banlance + ", state=" + state + ", crrountBalance="
				+ incomeBase + ", lastMonth=" + lastMonth + ", telephone="
				+ telephone + ", taskid=" + taskid + "]";
	}

	public HousingSuiZhouUserInfo(String username, String idnum, String companyName, String fundNum, String banlance,
			String state,  String incomeBase, String lastMonth, String telephone, String taskid) {
		super();
		this.username = username;
		this.idnum = idnum;
		this.companyName = companyName;
		this.fundNum = fundNum;
		this.banlance = banlance;
		this.state = state;
		this.incomeBase = incomeBase;
		this.lastMonth = lastMonth;
		this.telephone = telephone;
		this.taskid = taskid;
	}

	public HousingSuiZhouUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
