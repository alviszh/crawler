package com.microservice.dao.entity.crawler.bank.cgbchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cgb_credit_china_transflow",indexes = {@Index(name = "index_cgb_credit_china_transflow_taskid", columnList = "taskid")})
public class Cgb_credit_ChinaTransFlow  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**交易时间*/ 
	@Column(name="transferDate")
	private String transferDate ;
	
	/**交易摘要*/ 
	@Column(name="noteCode")
	private String noteCode ;
	
	/**交易币种*/ 
	@Column(name="currency1")
	private String currency1 ;
	
	/** 交易金额*/ 
	@Column(name="transamt")
	private String transamt ;
	
	/** 入账币种*/ 
	@Column(name="currency3")
	private String currency3 ;
	
	/**入账金额*/ 
	@Column(name="debitAmt")
	private String debitAmt ;
	
	/**卡号后四位*/ 
	@Column(name="last4OfCardNo")
	private String last4OfCardNo ;

	
	


	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getNoteCode() {
		return noteCode;
	}

	public void setNoteCode(String noteCode) {
		this.noteCode = noteCode;
	}

	public String getCurrency1() {
		return currency1;
	}

	public void setCurrency1(String currency1) {
		this.currency1 = currency1;
	}

	public String getTransamt() {
		return transamt;
	}

	public void setTransamt(String transamt) {
		this.transamt = transamt;
	}

	public String getCurrency3() {
		return currency3;
	}

	public void setCurrency3(String currency3) {
		this.currency3 = currency3;
	}

	public String getDebitAmt() {
		return debitAmt;
	}

	public void setDebitAmt(String debitAmt) {
		this.debitAmt = debitAmt;
	}

	public String getLast4OfCardNo() {
		return last4OfCardNo;
	}

	public void setLast4OfCardNo(String last4OfCardNo) {
		this.last4OfCardNo = last4OfCardNo;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
