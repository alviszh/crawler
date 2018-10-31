package com.microservice.dao.entity.crawler.housing.jinhua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 金华公积金用户信息
 */
@Entity
@Table(name="housing_jinhua_userinfo")
public class HousingJinHuaUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;

	private String userAccount;// 公积金账号
	private String type;// 账号类型
	private String username;// 姓名
	private String idnum;// 身份证号
	private String companyName;// 单位名称

	private String staffName;// 缴存机构
	private String personalAmount;// 个人月缴额
	private String companyAmount;// 单位月缴额
	private String totalAmount;// 合计月缴额
	private String balance;// 账户余额
	private String state;// 账户状态
	private String lastPaymonth;// 最后缴交月份
	private String taskid;

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getCompanyAmount() {
		return companyAmount;
	}

	public void setCompanyAmount(String companyAmount) {
		this.companyAmount = companyAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLastPaymonth() {
		return lastPaymonth;
	}

	public void setLastPaymonth(String lastPaymonth) {
		this.lastPaymonth = lastPaymonth;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingJinHuaUserInfo [userAccount=" + userAccount + ", type=" + type + ", username=" + username
				+ ", idnum=" + idnum + ", companyName=" + companyName + ", staffName=" + staffName + ", personalAmount="
				+ personalAmount + ", companyAmount=" + companyAmount + ", totalAmount=" + totalAmount + ", balance="
				+ balance + ", state=" + state + ", lastPaymonth=" + lastPaymonth + ", taskid=" + taskid + "]";
	}
	public HousingJinHuaUserInfo(String userAccount, String type, String username, String idnum, String companyName,
			String staffName, String personalAmount, String companyAmount, String totalAmount, String balance,
			String state, String lastPaymonth, String taskid) {
		super();
		this.userAccount = userAccount;
		this.type = type;
		this.username = username;
		this.idnum = idnum;
		this.companyName = companyName;
		this.staffName = staffName;
		this.personalAmount = personalAmount;
		this.companyAmount = companyAmount;
		this.totalAmount = totalAmount;
		this.balance = balance;
		this.state = state;
		this.lastPaymonth = lastPaymonth;
		this.taskid = taskid;
	}
	public HousingJinHuaUserInfo() {
		super();
		
	}
}
