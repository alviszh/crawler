package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 每期账单明细
 * @author: sln 
 * @date: 2017年11月14日 下午4:23:37 
 */
@Entity
@Table(name="cmbcchina_creditcard_billdetail",indexes = {@Index(name = "index_cmbcchina_creditcard_billdetail_taskid", columnList = "taskid")})
public class CmbcChinaCreditCardBillDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = -155211240706106079L;
	private String taskid;
//	记账日
	private String recordDate;
//	交易日（月日）
	private String consumeDate;
//	交易摘要
	private String transDescribe;
//	交易金额
	private String transAmt;
//	卡号末四位
	private String cardnoRearFour;
//	授权码
	private String authCode;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getConsumeDate() {
		return consumeDate;
	}
	public void setConsumeDate(String consumeDate) {
		this.consumeDate = consumeDate;
	}
	public String getTransDescribe() {
		return transDescribe;
	}
	public void setTransDescribe(String transDescribe) {
		this.transDescribe = transDescribe;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getCardnoRearFour() {
		return cardnoRearFour;
	}
	public void setCardnoRearFour(String cardnoRearFour) {
		this.cardnoRearFour = cardnoRearFour;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
}
