package com.microservice.dao.entity.crawler.bank.bohcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="bohcchina_transflow",indexes = {@Index(name = "index_bohcchina_transflow_taskid", columnList = "taskid")})
public class BohcChinaTransFlow  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**交易时间*/ 
	@Column(name="dealTime")
	private String dealTime ;
	
	/**交易金额*/ 
	@Column(name="tranAmt")
	private String tranAmt ;
	
	/**账户余额*/ 
	@Column(name="bal")
	private String bal ;
	
	/**对方账户*/ 
	@Column(name="oppAcctNo")
	private String oppAcctNo ;
	
	/**对方户名*/ 
	@Column(name="customName2")
	private String customName2 ;
	
	/**对方开户行*/ 
	@Column(name="oppBankName")
	private String oppBankName ;
	
	/**交易类型*/ 
	@Column(name="memo")
	private String memo ;
	
	/**附言*/ 
	@Column(name="tranBrief")
	private String tranBrief ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public String getOppAcctNo() {
		return oppAcctNo;
	}

	public void setOppAcctNo(String oppAcctNo) {
		this.oppAcctNo = oppAcctNo;
	}

	public String getCustomName2() {
		return customName2;
	}

	public void setCustomName2(String customName2) {
		this.customName2 = customName2;
	}

	public String getOppBankName() {
		return oppBankName;
	}

	public void setOppBankName(String oppBankName) {
		this.oppBankName = oppBankName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTranBrief() {
		return tranBrief;
	}

	public void setTranBrief(String tranBrief) {
		this.tranBrief = tranBrief;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
