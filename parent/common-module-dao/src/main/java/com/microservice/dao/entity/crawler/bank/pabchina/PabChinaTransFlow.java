package com.microservice.dao.entity.crawler.bank.pabchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="pabchina_transflow",indexes = {@Index(name = "index_pabchina_transflow_taskid", columnList = "taskid")})
public class PabChinaTransFlow  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**交易时间*/ 
	@Column(name="dealTime")
	private String dealTime ;
	
	/**交易方姓名*/ 
	@Column(name="dealname")
	private String dealname ;
	
	/**币种*/ 
	@Column(name="currency")
	private String currency ;
	
	/**交易金额*/ 
	@Column(name="dealMoney")
	private String dealMoney ;
	
	/**账户余额*/ 
	@Column(name="yue")
	private String yue ;
	
	/**用户备注*/ 
	@Column(name="userremark")
	private String userremark ;
	
	/**交易类型*/ 
	@Column(name="dealtype")
	private String dealtype ;
	
	/**流水号*/ 
	@Column(name="serial")
	private String serial;

	
	
	
	
	
	public String getDealname() {
		return dealname;
	}

	public void setDealname(String dealname) {
		this.dealname = dealname;
	}

	public String getDealMoney() {
		return dealMoney;
	}

	public void setDealMoney(String dealMoney) {
		this.dealMoney = dealMoney;
	}

	public String getUserremark() {
		return userremark;
	}

	public void setUserremark(String userremark) {
		this.userremark = userremark;
	}

	public String getDealtype() {
		return dealtype;
	}

	public void setDealtype(String dealtype) {
		this.dealtype = dealtype;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}


	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
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
