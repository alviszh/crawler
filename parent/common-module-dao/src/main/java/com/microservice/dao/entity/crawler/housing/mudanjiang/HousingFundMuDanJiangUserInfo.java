package com.microservice.dao.entity.crawler.housing.mudanjiang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_mudanjiang_userinfo",indexes = {@Index(name = "index_housing_mudanjiang_userinfo_taskid", columnList = "taskid")})
public class HousingFundMuDanJiangUserInfo extends IdEntity implements Serializable{

	private String taskid;
	
	private String unitaccname;//公司
	
	private String unitaccnum;//单位账号
	
	private String balance;//账户余额
	
	private String basenumber;//缴存基数
	
	private String indiprop;//个人缴存比例
	
	private String lastpaydate;//最后汇缴月
	
	private String monpaysum;//月缴存额
	
	private String opendate;//开户日期
	
	private String peraccstate;//账户状态0正常
	
	private String transdate;//最后缴存日期
	
	private String unitprop;//单位缴存比例
	

	private String indisnum;//个人缴存额
	private String unitsum;//单位缴存额
	@Override
	public String toString() {
		return "HousingFundMuDanJiangUserInfo [taskid=" + taskid + ", unitaccname=" + unitaccname + ", unitaccnum="
				+ unitaccnum + ", balance=" + balance + ", basenumber=" + basenumber + ", indiprop=" + indiprop
				+ ", lastpaydate=" + lastpaydate + ", monpaysum=" + monpaysum + ", opendate=" + opendate
				+ ", peraccstate=" + peraccstate + ", transdate=" + transdate + ", unitprop=" + unitprop + ", indisnum="
				+ indisnum + ", unitsum=" + unitsum + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUnitaccname() {
		return unitaccname;
	}
	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}
	public String getUnitaccnum() {
		return unitaccnum;
	}
	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
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
	public String getIndiprop() {
		return indiprop;
	}
	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}
	public String getLastpaydate() {
		return lastpaydate;
	}
	public void setLastpaydate(String lastpaydate) {
		this.lastpaydate = lastpaydate;
	}
	public String getMonpaysum() {
		return monpaysum;
	}
	public void setMonpaysum(String monpaysum) {
		this.monpaysum = monpaysum;
	}
	public String getOpendate() {
		return opendate;
	}
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	public String getPeraccstate() {
		return peraccstate;
	}
	public void setPeraccstate(String peraccstate) {
		this.peraccstate = peraccstate;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public String getUnitprop() {
		return unitprop;
	}
	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}
	public String getIndisnum() {
		return indisnum;
	}
	public void setIndisnum(String indisnum) {
		this.indisnum = indisnum;
	}
	public String getUnitsum() {
		return unitsum;
	}
	public void setUnitsum(String unitsum) {
		this.unitsum = unitsum;
	}
	
	
}
