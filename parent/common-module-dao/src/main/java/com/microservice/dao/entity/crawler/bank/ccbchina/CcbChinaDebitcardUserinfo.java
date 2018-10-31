package com.microservice.dao.entity.crawler.bank.ccbchina;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="ccbchina_debitcard_userinfo")
public class CcbChinaDebitcardUserinfo extends IdEntity{
	
	@Override
	public String toString() {
		return "CcbChinaDebitcardUserinfo [taskid=" + taskid + ", SEX=" + SEX + ", TEL=" + TEL + ", NAME=" + NAME
				+ ", NICKNAME=" + NICKNAME + ", ECIF_CUSTNO=" + ECIF_CUSTNO + ", YXSJ_Flag=" + YXSJ_Flag + "]";
	}
	private String taskid;//uuid 前端通过uuid访问状态结果
	private String SEX;				//性别
	private String TEL;			//手机号
	private String NAME;			//姓名
	private String NICKNAME;		//登录名称
	private String ECIF_CUSTNO;		//
	private String YXSJ_Flag;
	
	private String cardNum;			//卡号
	private String accountType;		//账户类型
	private String currency;		//币种
	private String openDate;		//开户日期
	private String balance;			//账户余额
	private String accountStatus;	//账户状态
	
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String sEX) {
		SEX = sEX;
	}
	public String getTEL() {
		return TEL;
	}
	public void setTEL(String tEL) {
		TEL = tEL;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getNICKNAME() {
		return NICKNAME;
	}
	public void setNICKNAME(String nICKNAME) {
		NICKNAME = nICKNAME;
	}
	public String getECIF_CUSTNO() {
		return ECIF_CUSTNO;
	}
	public void setECIF_CUSTNO(String eCIF_CUSTNO) {
		ECIF_CUSTNO = eCIF_CUSTNO;
	}
	public String getYXSJ_Flag() {
		return YXSJ_Flag;
	}
	public void setYXSJ_Flag(String yXSJ_Flag) {
		YXSJ_Flag = yXSJ_Flag;
	}

}
