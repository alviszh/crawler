package com.microservice.dao.entity.crawler.housing.datong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 大同公积金用户信息
 */
@Entity
@Table(name="housing_datong_userinfo")
public class HousingDaTongUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;	

	private String companyName;//单位名称
	private String companyAccount;//单位账号
	private String staffName;//缴存管理部
	private String bankname;//缴存银行
	private String personalRatio;//缴存比例（个人）
	private String companyalRatio;//缴存比例（单位）
	private String salaryBase;//工资基数
	private String perdepmny;//月汇缴额(个人)
	private String corpdepmny;//月汇缴额(单位)
	private String depmny;//月汇缴额(合计)
	private String balance;//缴存余额
	private String state;//账户状态
	private String opendate;//开户日期
	private String lastmonth;//缴至年月
	private String bindingBankname;//绑定银行
	private String bindingCardnum;//绑定银行卡号
	
	private String taskid;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAccount() {
		return companyAccount;
	}
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getPersonalRatio() {
		return personalRatio;
	}
	public void setPersonalRatio(String personalRatio) {
		this.personalRatio = personalRatio;
	}
	public String getCompanyalRatio() {
		return companyalRatio;
	}
	public void setCompanyalRatio(String companyalRatio) {
		this.companyalRatio = companyalRatio;
	}
	public String getSalaryBase() {
		return salaryBase;
	}
	public void setSalaryBase(String salaryBase) {
		this.salaryBase = salaryBase;
	}
	
	public String getPerdepmny() {
		return perdepmny;
	}
	public void setPerdepmny(String perdepmny) {
		this.perdepmny = perdepmny;
	}
	public String getCorpdepmny() {
		return corpdepmny;
	}
	public void setCorpdepmny(String corpdepmny) {
		this.corpdepmny = corpdepmny;
	}
	public String getDepmny() {
		return depmny;
	}
	public void setDepmny(String depmny) {
		this.depmny = depmny;
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
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getLastmonth() {
		return lastmonth;
	}
	public void setLastmonth(String lastmonth) {
		this.lastmonth = lastmonth;
	}
	public String getBindingBankname() {
		return bindingBankname;
	}
	public void setBindingBankname(String bindingBankname) {
		this.bindingBankname = bindingBankname;
	}
	public String getBindingCardnum() {
		return bindingCardnum;
	}
	public void setBindingCardnum(String bindingCardnum) {
		this.bindingCardnum = bindingCardnum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "HousingDaTongUserInfo [companyName=" + companyName + ", companyAccount=" + companyAccount
				+ ", staffName=" + staffName + ", bankname=" + bankname + ", personalRatio=" + personalRatio
				+ ", companyalRatio=" + companyalRatio + ", salaryBase=" + salaryBase + ", perdepmny=" + perdepmny
				+ ", corpdepmny=" + corpdepmny + ", depmny=" + depmny + ", balance=" + balance + ", state=" + state
				+ ", opendate=" + opendate + ", lastmonth=" + lastmonth + ", bindingBankname=" + bindingBankname
				+ ", bindingCardnum=" + bindingCardnum + ", taskid=" + taskid + "]";
	}
	public HousingDaTongUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingDaTongUserInfo(String companyName, String companyAccount, String staffName, String bankname,
			String personalRatio, String companyalRatio, String salaryBase, String perdepmny, String corpdepmny,
			String depmny, String balance, String state, String opendate, String lastmonth, String bindingBankname,
			String bindingCardnum, String taskid) {
		super();
		this.companyName = companyName;
		this.companyAccount = companyAccount;
		this.staffName = staffName;
		this.bankname = bankname;
		this.personalRatio = personalRatio;
		this.companyalRatio = companyalRatio;
		this.salaryBase = salaryBase;
		this.perdepmny = perdepmny;
		this.corpdepmny = corpdepmny;
		this.depmny = depmny;
		this.balance = balance;
		this.state = state;
		this.opendate = opendate;
		this.lastmonth = lastmonth;
		this.bindingBankname = bindingBankname;
		this.bindingCardnum = bindingCardnum;
		this.taskid = taskid;
	}
}
