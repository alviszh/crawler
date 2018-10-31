package com.microservice.dao.entity.crawler.housing.jinan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 济南市公积金个人基本信息
 * @author: sln 
 * @date: 
 */
@Entity
@Table(name="housing_jinan_userinfo",indexes = {@Index(name = "index_housing_jinan_userinfo_taskid", columnList = "taskid")})
public class HousingJiNanUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4523112843732781341L;
	private String taskid;
//	单位账号
	private String unitaccnum;
//	单位名称
	private String unitaccname;
//	个人账号
	private String accnum;
//	姓名
	private String accname;
//	证件号码
	private String certinum;
//	联名卡卡号
	private String cardno;
//	个人账户状态
	private String peraccstate;
//	委托代办单位编号
	private String agentunitno;
//	开户时间
	private String opendate;
//	最后汇缴月
	private String lastpaydate;
//	单位比例
	private String unitprop;
//	个人比例
	private String indiprop;
//	月缴额
	private String monpaysum;
//	余额
	private String balance;
//	冻结原因
	private String frzreason;
//	冻结金额
	private String frzamount;
//	缴存基数
	private String basenumber;
//	开户银行
	private String accbankname;
//	最近6个月的平均缴存基数
	private String avgbasenumber;
//	连续缴存月数
	private String paynum;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getUnitaccnum() {
		return unitaccnum;
	}

	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}

	public String getUnitaccname() {
		return unitaccname;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getAccnum() {
		return accnum;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getCertinum() {
		return certinum;
	}

	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getPeraccstate() {
		return peraccstate;
	}

	public void setPeraccstate(String peraccstate) {
		this.peraccstate = peraccstate;
	}

	public String getAgentunitno() {
		return agentunitno;
	}

	public void setAgentunitno(String agentunitno) {
		this.agentunitno = agentunitno;
	}

	public String getOpendate() {
		return opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getLastpaydate() {
		return lastpaydate;
	}

	public void setLastpaydate(String lastpaydate) {
		this.lastpaydate = lastpaydate;
	}

	public String getUnitprop() {
		return unitprop;
	}

	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}

	public String getIndiprop() {
		return indiprop;
	}

	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}

	public String getMonpaysum() {
		return monpaysum;
	}

	public void setMonpaysum(String monpaysum) {
		this.monpaysum = monpaysum;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getFrzreason() {
		return frzreason;
	}

	public void setFrzreason(String frzreason) {
		this.frzreason = frzreason;
	}

	public String getFrzamount() {
		return frzamount;
	}

	public void setFrzamount(String frzamount) {
		this.frzamount = frzamount;
	}

	public String getBasenumber() {
		return basenumber;
	}

	public void setBasenumber(String basenumber) {
		this.basenumber = basenumber;
	}

	public String getAccbankname() {
		return accbankname;
	}

	public void setAccbankname(String accbankname) {
		this.accbankname = accbankname;
	}

	public String getAvgbasenumber() {
		return avgbasenumber;
	}

	public void setAvgbasenumber(String avgbasenumber) {
		this.avgbasenumber = avgbasenumber;
	}

	public String getPaynum() {
		return paynum;
	}

	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}

	public HousingJiNanUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
