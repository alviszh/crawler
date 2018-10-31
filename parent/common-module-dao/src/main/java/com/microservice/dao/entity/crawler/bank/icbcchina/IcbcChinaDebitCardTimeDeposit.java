package com.microservice.dao.entity.crawler.bank.icbcchina;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="icbcchina_debitcard_timedeposit" ,indexes = {@Index(name = "index_icbcchina_debitcard_timedeposit_taskid", columnList = "taskid")})
public class IcbcChinaDebitCardTimeDeposit extends IdEntity{
	private String taskid;
	private String cardNum;									//卡号
	private String acctNum;									//账号
	private String saveType;								//储蓄种类
	private String status;									//状态
	private String currency;								//币种
	private String principal;								//本金
	private String interestEnddate;							//到期日
	private String interestRate;							//利率
	private String storgePeriod;							//存期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getAcctNum() {
		return acctNum;
	}
	public void setAcctNum(String acctNum) {
		this.acctNum = acctNum;
	}
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getInterestEnddate() {
		return interestEnddate;
	}
	public void setInterestEnddate(String interestEnddate) {
		this.interestEnddate = interestEnddate;
	}
	public String getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	public String getStorgePeriod() {
		return storgePeriod;
	}
	public void setStorgePeriod(String storgePeriod) {
		this.storgePeriod = storgePeriod;
	}
	@Override
	public String toString() {
		return "IcbcChinaDebitCardTimeDeposit [taskid=" + taskid + ", cardNum=" + cardNum + ", acctNum=" + acctNum
				+ ", saveType=" + saveType + ", status=" + status + ", currency=" + currency + ", principal="
				+ principal + ", interestEnddate=" + interestEnddate + ", interestRate=" + interestRate
				+ ", storgePeriod=" + storgePeriod + "]";
	}
	
}
