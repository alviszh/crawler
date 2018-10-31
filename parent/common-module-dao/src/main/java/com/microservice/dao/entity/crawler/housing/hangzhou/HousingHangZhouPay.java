package com.microservice.dao.entity.crawler.housing.hangzhou;

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
@Table(name  ="housing_hangzhou_pay",indexes = {@Index(name = "index_housing_hangzhou_pay_taskid", columnList = "taskid")})
public class HousingHangZhouPay extends IdEntity implements Serializable{
	private String fundAccount;     //资金账号
	private String natureFunds;     //资金性质
	private String depositUnit;     //缴存单位
	private String company;         //月缴存额单位
	private String personal;        //月缴存额个人
	private String total;           //月缴存额合计
	private String depositStatus;   //缴存状态
	private String accountBalance;  //账户余额

	private Integer userid;

	private String taskid;
	@Override
	public String toString() {
		return "HousingHangzhouPay [fundAccount=" + fundAccount + ",natureFunds=" + natureFunds
				+ ", depositUnit=" + depositUnit + ", company=" + company + ", personal=" + personal + ", total="
				+ total+ ", depositStatus=" + depositStatus + ", accountBalance=" + accountBalance 
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	public String getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}

	public String getNatureFunds() {
		return natureFunds;
	}

	public void setNatureFunds(String natureFunds) {
		this.natureFunds = natureFunds;
	}

	public String getDepositUnit() {
		return depositUnit;
	}

	public void setDepositUnit(String depositUnit) {
		this.depositUnit = depositUnit;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDepositStatus() {
		return depositStatus;
	}

	public void setDepositStatus(String depositStatus) {
		this.depositStatus = depositStatus;
	}

	

	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
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

}
