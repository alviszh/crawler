package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cmbcchina_debitcard_userinfo",indexes = {@Index(name = "index_cmbcchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class CmbcChinaDebitcardUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -247086898453621224L;
	private String taskid;
//	户名
	private String cifname;
//	账户号
	private String acno;
//	银行账户类型代号
	private String bankactypecode;  //是个代号
//	银行账户类型名称
	private String bankactypename;  //代号的含义
//	开户行
	private String deptname;
//	卡类型名
	private String cardtypename;
//	账户类型等级名
	private String actypelevelname;
//	可用总余额(当前登录用户所有账户的余额总和)
	private String availtobalbal;
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCifname() {
		return cifname;
	}
	public void setCifname(String cifname) {
		this.cifname = cifname;
	}
	public String getAcno() {
		return acno;
	}
	public void setAcno(String acno) {
		this.acno = acno;
	}
	public String getBankactypecode() {
		return bankactypecode;
	}
	public void setBankactypecode(String bankactypecode) {
		this.bankactypecode = bankactypecode;
	}
	public String getBankactypename() {
		return bankactypename;
	}
	public void setBankactypename(String bankactypename) {
		this.bankactypename = bankactypename;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getCardtypename() {
		return cardtypename;
	}
	public void setCardtypename(String cardtypename) {
		this.cardtypename = cardtypename;
	}
	public String getActypelevelname() {
		return actypelevelname;
	}
	public void setActypelevelname(String actypelevelname) {
		this.actypelevelname = actypelevelname;
	}
	public String getAvailtobalbal() {
		return availtobalbal;
	}
	public void setAvailtobalbal(String availtobalbal) {
		this.availtobalbal = availtobalbal;
	}
	public CmbcChinaDebitcardUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CmbcChinaDebitcardUserInfo(String taskid, String cifname, String acno, String bankactypecode,
			String bankactypename, String deptname, String cardtypename, String actypelevelname, String availtobalbal) {
		super();
		this.taskid = taskid;
		this.cifname = cifname;
		this.acno = acno;
		this.bankactypecode = bankactypecode;
		this.bankactypename = bankactypename;
		this.deptname = deptname;
		this.cardtypename = cardtypename;
		this.actypelevelname = actypelevelname;
		this.availtobalbal = availtobalbal;
	}
}
