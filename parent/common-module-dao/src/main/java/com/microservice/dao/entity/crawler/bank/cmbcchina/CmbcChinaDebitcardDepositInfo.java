package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 存款信息（活期/定期）
 * @author sln
 *
 */
@Entity
@Table(name="cmbcchina_debitcard_depositinfo",indexes = {@Index(name = "index_cmbcchina_debitcard_depositinfo_taskid", columnList = "taskid")})
public class CmbcChinaDebitcardDepositInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -247086898453621224L;
	private String taskid;
//	账户号
	private String acno;
//	储种
	private String savetypename;
//	币种
	private String currencyname;
//	利率
	private String rate;
//	开户日
	private String opendate;
//	到期日
	private String expiredate;
//	账户状态
	private String acstatename;
//	账户余额
	private String balance;
//	可用余额
	private String availbal;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAcno() {
		return acno;
	}

	public void setAcno(String acno) {
		this.acno = acno;
	}

	public String getSavetypename() {
		return savetypename;
	}

	public void setSavetypename(String savetypename) {
		this.savetypename = savetypename;
	}

	public String getCurrencyname() {
		return currencyname;
	}

	public void setCurrencyname(String currencyname) {
		this.currencyname = currencyname;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getOpendate() {
		return opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getExpiredate() {
		return expiredate;
	}

	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}

	public String getAcstatename() {
		return acstatename;
	}

	public void setAcstatename(String acstatename) {
		this.acstatename = acstatename;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAvailbal() {
		return availbal;
	}

	public void setAvailbal(String availbal) {
		this.availbal = availbal;
	}

	public CmbcChinaDebitcardDepositInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
