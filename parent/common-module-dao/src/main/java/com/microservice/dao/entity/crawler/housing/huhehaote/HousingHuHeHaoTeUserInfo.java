package com.microservice.dao.entity.crawler.housing.huhehaote;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_huhehaote_userinfo",indexes = {@Index(name = "index_housing_huhehaote_userinfo_taskid", columnList = "taskid")})
public class HousingHuHeHaoTeUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 7667455662058760759L;
	private String taskid;
//	单位账号
	private String unitaccnum;
//	个人账号
	private String accnum;
//	单位名称
	private String unitaccname;
//	姓名
	private String accname;
//	身份证号
	private String certinum;
//	账户状态
	private String peraccstate;
//	余额
	private String balance;
//	缴存基数
	private String basenumber;
//	比例总计
	private String prop;
//	个人比例
	private String indiprop;
//	单位比例
	private String unitprop;
//	最后汇缴月
	private String lastpaydate;
//	启用日期
	private String beginpaydate;
//	月缴额
	private String monpaysum;
//	是否有贷款
	private String isloan;
//	联名卡号
	private String cardno;
//	是否有担保
	private String isassure;
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
	public String getAccnum() {
		return accnum;
	}
	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
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
	public String getPeraccstate() {
		return peraccstate;
	}
	public void setPeraccstate(String peraccstate) {
		this.peraccstate = peraccstate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getBasenumber() {
		return basenumber;
	}
	public void setBasenumber(String basenumber) {
		this.basenumber = basenumber;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	public String getLastpaydate() {
		return lastpaydate;
	}
	public void setLastpaydate(String lastpaydate) {
		this.lastpaydate = lastpaydate;
	}
	public String getBeginpaydate() {
		return beginpaydate;
	}
	public void setBeginpaydate(String beginpaydate) {
		this.beginpaydate = beginpaydate;
	}
	public String getMonpaysum() {
		return monpaysum;
	}
	public void setMonpaysum(String monpaysum) {
		this.monpaysum = monpaysum;
	}
	public String getIsloan() {
		return isloan;
	}
	public void setIsloan(String isloan) {
		this.isloan = isloan;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getIsassure() {
		return isassure;
	}
	public void setIsassure(String isassure) {
		this.isassure = isassure;
	}
	public HousingHuHeHaoTeUserInfo() {
		super();
	}
}
