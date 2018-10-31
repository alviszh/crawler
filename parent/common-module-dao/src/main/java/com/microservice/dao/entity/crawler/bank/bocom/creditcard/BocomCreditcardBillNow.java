/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocom.creditcard;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-22 15:52:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocom_creditcard_billnow",indexes = {@Index(name = "index_bocom_creditcard_billnow_taskid", columnList = "taskid")})
public class BocomCreditcardBillNow extends IdEntity {

	@Override
	public String toString() {
		return "BocomCreditcardBillNow [mainCardFlag=" + mainCardFlag + ", cardNo=" + cardNo + ", isEMVCard="
				+ isEMVCard + ", canUseAmt=" + canUseAmt + ", inTmpLmtWtList=" + inTmpLmtWtList + ", inTmpAdjPeriod="
				+ inTmpAdjPeriod + ", adjFixOrTmp=" + adjFixOrTmp + ", inEjLoadWtList=" + inEjLoadWtList
				+ ", isEjLoadSigned=" + isEjLoadSigned + ", crdLmt=" + crdLmt + ", avacrdlmt=" + avacrdlmt
				+ ", avaTmpLmt=" + avaTmpLmt + ", ejLoadLmt=" + ejLoadLmt + ", avaEjLoadLmt=" + avaEjLoadLmt
				+ ", fixCrdLmt=" + fixCrdLmt + ", usedTmpLmt=" + usedTmpLmt + ", tmpLmtEndDate=" + tmpLmtEndDate
				+ ", crdLmtUseRate=" + crdLmtUseRate + ", cshLmtUseRate=" + cshLmtUseRate + ", ejLoadLmtUseRate="
				+ ejLoadLmtUseRate + ", is0LmtAglCus=" + is0LmtAglCus + ", ejLoanType=" + ejLoanType
				+ ", conTotalMoney=" + conTotalMoney + ", conCanUseMoney=" + conCanUseMoney
				+ ", available_credit_line_ren=" + available_credit_line_ren + ", available_credit_line_mei="
				+ available_credit_line_mei + ", available_withdrawal_limits_ren=" + available_withdrawal_limits_ren
				+ ", available_withdrawal_limits_mei=" + available_withdrawal_limits_mei + ", taskid=" + taskid + "]";
	}

	private String mainCardFlag; // 主卡类型
	private String cardNo; // 卡号
	private String isEMVCard;
	private String canUseAmt;
	private String inTmpLmtWtList;
	private String inTmpAdjPeriod;
	private String adjFixOrTmp;
	private String inEjLoadWtList;
	private String isEjLoadSigned;
	private String crdLmt;
	private String avacrdlmt;// 可用信用额度
	private String avaTmpLmt;
	private String ejLoadLmt;// 可开通好享贷额度
	private String avaEjLoadLmt;// 可开通好享贷额度
	private String fixCrdLmt;
	private String usedTmpLmt;
	private String tmpLmtEndDate;
	private String crdLmtUseRate;
	private String cshLmtUseRate;
	private String ejLoadLmtUseRate;
	private String is0LmtAglCus;
	private String ejLoanType;
	private String conTotalMoney;// 信用总额度
	private String conCanUseMoney;

	private String available_credit_line_ren; //可用总额度 人民币

	private String available_credit_line_mei; //可用总额度  美元

	private String available_withdrawal_limits_ren; //可用取现额度 人民币

	private String available_withdrawal_limits_mei;//可用取现额度 美元

	public String getAvailable_credit_line_ren() {
		return available_credit_line_ren;
	}

	public void setAvailable_credit_line_ren(String available_credit_line_ren) {
		this.available_credit_line_ren = available_credit_line_ren;
	}

	public String getAvailable_credit_line_mei() {
		return available_credit_line_mei;
	}

	public void setAvailable_credit_line_mei(String available_credit_line_mei) {
		this.available_credit_line_mei = available_credit_line_mei;
	}

	public String getAvailable_withdrawal_limits_ren() {
		return available_withdrawal_limits_ren;
	}

	public void setAvailable_withdrawal_limits_ren(String available_withdrawal_limits_ren) {
		this.available_withdrawal_limits_ren = available_withdrawal_limits_ren;
	}

	public String getAvailable_withdrawal_limits_mei() {
		return available_withdrawal_limits_mei;
	}

	public void setAvailable_withdrawal_limits_mei(String available_withdrawal_limits_mei) {
		this.available_withdrawal_limits_mei = available_withdrawal_limits_mei;
	}

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setMainCardFlag(String mainCardFlag) {
		this.mainCardFlag = mainCardFlag;
	}

	public String getMainCardFlag() {
		return mainCardFlag;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setIsEMVCard(String isEMVCard) {
		this.isEMVCard = isEMVCard;
	}

	public String getIsEMVCard() {
		return isEMVCard;
	}

	public void setCanUseAmt(String canUseAmt) {
		this.canUseAmt = canUseAmt;
	}

	public String getCanUseAmt() {
		return canUseAmt;
	}

	public void setInTmpLmtWtList(String inTmpLmtWtList) {
		this.inTmpLmtWtList = inTmpLmtWtList;
	}

	public String getInTmpLmtWtList() {
		return inTmpLmtWtList;
	}

	public void setInTmpAdjPeriod(String inTmpAdjPeriod) {
		this.inTmpAdjPeriod = inTmpAdjPeriod;
	}

	public String getInTmpAdjPeriod() {
		return inTmpAdjPeriod;
	}

	public void setAdjFixOrTmp(String adjFixOrTmp) {
		this.adjFixOrTmp = adjFixOrTmp;
	}

	public String getAdjFixOrTmp() {
		return adjFixOrTmp;
	}

	public void setInEjLoadWtList(String inEjLoadWtList) {
		this.inEjLoadWtList = inEjLoadWtList;
	}

	public String getInEjLoadWtList() {
		return inEjLoadWtList;
	}

	public void setIsEjLoadSigned(String isEjLoadSigned) {
		this.isEjLoadSigned = isEjLoadSigned;
	}

	public String getIsEjLoadSigned() {
		return isEjLoadSigned;
	}

	public void setCrdLmt(String crdLmt) {
		this.crdLmt = crdLmt;
	}

	public String getCrdLmt() {
		return crdLmt;
	}

	public void setAvacrdlmt(String avacrdlmt) {
		this.avacrdlmt = avacrdlmt;
	}

	public String getAvacrdlmt() {
		return avacrdlmt;
	}

	public void setAvaTmpLmt(String avaTmpLmt) {
		this.avaTmpLmt = avaTmpLmt;
	}

	public String getAvaTmpLmt() {
		return avaTmpLmt;
	}

	public void setEjLoadLmt(String ejLoadLmt) {
		this.ejLoadLmt = ejLoadLmt;
	}

	public String getEjLoadLmt() {
		return ejLoadLmt;
	}

	public void setAvaEjLoadLmt(String avaEjLoadLmt) {
		this.avaEjLoadLmt = avaEjLoadLmt;
	}

	public String getAvaEjLoadLmt() {
		return avaEjLoadLmt;
	}

	public void setFixCrdLmt(String fixCrdLmt) {
		this.fixCrdLmt = fixCrdLmt;
	}

	public String getFixCrdLmt() {
		return fixCrdLmt;
	}

	public void setUsedTmpLmt(String usedTmpLmt) {
		this.usedTmpLmt = usedTmpLmt;
	}

	public String getUsedTmpLmt() {
		return usedTmpLmt;
	}

	public void setTmpLmtEndDate(String tmpLmtEndDate) {
		this.tmpLmtEndDate = tmpLmtEndDate;
	}

	public String getTmpLmtEndDate() {
		return tmpLmtEndDate;
	}

	public void setCrdLmtUseRate(String crdLmtUseRate) {
		this.crdLmtUseRate = crdLmtUseRate;
	}

	public String getCrdLmtUseRate() {
		return crdLmtUseRate;
	}

	public void setCshLmtUseRate(String cshLmtUseRate) {
		this.cshLmtUseRate = cshLmtUseRate;
	}

	public String getCshLmtUseRate() {
		return cshLmtUseRate;
	}

	public void setEjLoadLmtUseRate(String ejLoadLmtUseRate) {
		this.ejLoadLmtUseRate = ejLoadLmtUseRate;
	}

	public String getEjLoadLmtUseRate() {
		return ejLoadLmtUseRate;
	}

	public void setIs0LmtAglCus(String is0LmtAglCus) {
		this.is0LmtAglCus = is0LmtAglCus;
	}

	public String getIs0LmtAglCus() {
		return is0LmtAglCus;
	}

	public void setEjLoanType(String ejLoanType) {
		this.ejLoanType = ejLoanType;
	}

	public String getEjLoanType() {
		return ejLoanType;
	}

	public void setConTotalMoney(String conTotalMoney) {
		this.conTotalMoney = conTotalMoney;
	}

	public String getConTotalMoney() {
		return conTotalMoney;
	}

	public void setConCanUseMoney(String conCanUseMoney) {
		this.conCanUseMoney = conCanUseMoney;
	}

	public String getConCanUseMoney() {
		return conCanUseMoney;
	}

}