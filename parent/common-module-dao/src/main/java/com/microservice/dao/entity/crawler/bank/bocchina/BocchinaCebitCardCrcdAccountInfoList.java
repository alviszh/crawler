/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-28 16:15:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Entity
@Table(name = "bocchina_cebitcard_accountinfolist",indexes = {@Index(name = "index_bocchina_cebitcard_accountinfolist_taskid", columnList = "taskid")})
public class BocchinaCebitCardCrcdAccountInfoList extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acntType;// CNY 人民币或其他币种
	private String cdtLmtAmt;//信用额度
	private String avaiBal;// 可用余额
	private String instalAvaiBal;// 分期可用余额
	private String billDt;// 账单日期
	private String reapyDt;// 到期还款日
	private String lastTermAcntBal;// 上期账单余额
	private String totalExpend;// 支出总计
	private String totalDeposit;// 存入总计
	private String periodAvailbleCreditLimit;//本期余额
	private String lowestRepayAmount;// 最低还款额
	private String periodGetCashCreditLimit;//取现额度
	private String autoRepayAccount;// 未知含义
	private String lastTermAcntBalflag;// "0" 未知含义 
	private String periodAvailbleCreditLimitflag;// "2" 未知含义与本期余额相关  推测为欠款

	private String cardNo;// 信用卡卡号

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setAcntType(String acntType) {
		this.acntType = acntType;
	}

	public String getAcntType() {
		return acntType;
	}

	public void setCdtLmtAmt(String cdtLmtAmt) {
		this.cdtLmtAmt = cdtLmtAmt;
	}

	public String getCdtLmtAmt() {
		return cdtLmtAmt;
	}

	public void setAvaiBal(String avaiBal) {
		this.avaiBal = avaiBal;
	}

	public String getAvaiBal() {
		return avaiBal;
	}

	public void setInstalAvaiBal(String instalAvaiBal) {
		this.instalAvaiBal = instalAvaiBal;
	}

	public String getInstalAvaiBal() {
		return instalAvaiBal;
	}

	public String getBillDt() {
		return billDt;
	}

	public void setBillDt(String billDt) {
		this.billDt = billDt;
	}

	public void setReapyDt(String reapyDt) {
		this.reapyDt = reapyDt;
	}

	public void setLastTermAcntBal(String lastTermAcntBal) {
		this.lastTermAcntBal = lastTermAcntBal;
	}

	public String getLastTermAcntBal() {
		return lastTermAcntBal;
	}

	public void setTotalExpend(String totalExpend) {
		this.totalExpend = totalExpend;
	}

	public String getTotalExpend() {
		return totalExpend;
	}

	public void setTotalDeposit(String totalDeposit) {
		this.totalDeposit = totalDeposit;
	}

	public String getTotalDeposit() {
		return totalDeposit;
	}

	public void setPeriodAvailbleCreditLimit(String periodAvailbleCreditLimit) {
		this.periodAvailbleCreditLimit = periodAvailbleCreditLimit;
	}

	public String getPeriodAvailbleCreditLimit() {
		return periodAvailbleCreditLimit;
	}

	public void setLowestRepayAmount(String lowestRepayAmount) {
		this.lowestRepayAmount = lowestRepayAmount;
	}

	public String getLowestRepayAmount() {
		return lowestRepayAmount;
	}

	public void setPeriodGetCashCreditLimit(String periodGetCashCreditLimit) {
		this.periodGetCashCreditLimit = periodGetCashCreditLimit;
	}

	public String getPeriodGetCashCreditLimit() {
		return periodGetCashCreditLimit;
	}

	public void setAutoRepayAccount(String autoRepayAccount) {
		this.autoRepayAccount = autoRepayAccount;
	}

	public String getAutoRepayAccount() {
		return autoRepayAccount;
	}

	public void setLastTermAcntBalflag(String lastTermAcntBalflag) {
		this.lastTermAcntBalflag = lastTermAcntBalflag;
	}

	public String getLastTermAcntBalflag() {
		return lastTermAcntBalflag;
	}

	public void setPeriodAvailbleCreditLimitflag(String periodAvailbleCreditLimitflag) {
		this.periodAvailbleCreditLimitflag = periodAvailbleCreditLimitflag;
	}

	public String getPeriodAvailbleCreditLimitflag() {
		return periodAvailbleCreditLimitflag;
	}

	@Override
	public String toString() {
		return "BocchinaCebitCardCrcdAccountInfoList [acntType=" + acntType + ", cdtLmtAmt=" + cdtLmtAmt + ", avaiBal="
				+ avaiBal + ", instalAvaiBal=" + instalAvaiBal + ", billDt=" + billDt + ", reapyDt=" + reapyDt
				+ ", lastTermAcntBal=" + lastTermAcntBal + ", totalExpend=" + totalExpend + ", totalDeposit="
				+ totalDeposit + ", periodAvailbleCreditLimit=" + periodAvailbleCreditLimit + ", lowestRepayAmount="
				+ lowestRepayAmount + ", periodGetCashCreditLimit=" + periodGetCashCreditLimit + ", autoRepayAccount="
				+ autoRepayAccount + ", lastTermAcntBalflag=" + lastTermAcntBalflag + ", periodAvailbleCreditLimitflag="
				+ periodAvailbleCreditLimitflag + "]";
	}

}