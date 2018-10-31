package com.microservice.dao.entity.crawler.housing.anqing;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_anqing_userinfo",indexes = {@Index(name = "index_housing_anqing_userinfo_taskid", columnList = "taskid")})
public class HousingAnQingUserinfo  extends IdEntity implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name  ;//姓名
	private String companyname  ;//单位名称
	private String companynum  ;//单位登记号
	private String personnum   ;//个人代码
	private String accountbank;//单位银行账号
	private String personalbank;//个人银行账号
	private String bankname;//归集银行名称
	private String enddate;// ;//缴至日期
	private String ownness;//个人状态
	private String depositratio  ;//缴存比例
	private String monthpay  ;//月应缴额
	private String balance  ;//当前余额
	
	private Integer userid;

	private String taskid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCompanynum() {
		return companynum;
	}

	public void setCompanynum(String companynum) {
		this.companynum = companynum;
	}

	public String getPersonnum() {
		return personnum;
	}

	public void setPersonnum(String personnum) {
		this.personnum = personnum;
	}

	public String getAccountbank() {
		return accountbank;
	}

	public void setAccountbank(String accountbank) {
		this.accountbank = accountbank;
	}

	public String getPersonalbank() {
		return personalbank;
	}

	public void setPersonalbank(String personalbank) {
		this.personalbank = personalbank;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getOwnness() {
		return ownness;
	}

	public void setOwnness(String ownness) {
		this.ownness = ownness;
	}

	public String getDepositratio() {
		return depositratio;
	}

	public void setDepositratio(String depositratio) {
		this.depositratio = depositratio;
	}

	public String getMonthpay() {
		return monthpay;
	}

	public void setMonthpay(String monthpay) {
		this.monthpay = monthpay;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HousingAnQingUserinfo [name=" + name + ", companyname=" + companyname + ", companynum=" + companynum
				+ ", personnum=" + personnum + ", accountbank=" + accountbank + ", personalbank=" + personalbank
				+ ", bankname=" + bankname + ", enddate=" + enddate + ", ownness=" + ownness + ", depositratio="
				+ depositratio + ", monthpay=" + monthpay + ", balance=" + balance + ", userid=" + userid + ", taskid="
				+ taskid + "]";
	}

	
	

}
