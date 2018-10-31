package com.microservice.dao.entity.crawler.bank.abcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="abcchinaCredit_transinfo",indexes = {@Index(name = "index_abcchinaCredit_transinfo_taskid", columnList = "taskid")})
public class AbcChinaCreditTransInfo  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**账户币种*/ 
	@Column(name="codCcyCn")
	private String codCcyCn ;
	
	/**信用额度*/ 
	@Column(name="amtCrlm")
	private String amtCrlm ;
	
	/**上期余额(欠款为-)*/ 
	@Column(name="balBen")
	private String balBen ;
	
	/**本期新增应还款额*/ 
	@Column(name="amtDbNew")
	private String amtDbNew ;
	
	

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCodCcyCn() {
		return codCcyCn;
	}

	public void setCodCcyCn(String codCcyCn) {
		this.codCcyCn = codCcyCn;
	}

	public String getAmtCrlm() {
		return amtCrlm;
	}

	public void setAmtCrlm(String amtCrlm) {
		this.amtCrlm = amtCrlm;
	}

	public String getBalBen() {
		return balBen;
	}

	public void setBalBen(String balBen) {
		this.balBen = balBen;
	}

	public String getAmtDbNew() {
		return amtDbNew;
	}

	public void setAmtDbNew(String amtDbNew) {
		this.amtDbNew = amtDbNew;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
