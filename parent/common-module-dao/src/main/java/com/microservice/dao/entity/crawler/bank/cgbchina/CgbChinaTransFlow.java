package com.microservice.dao.entity.crawler.bank.cgbchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cgbchina_transflow",indexes = {@Index(name = "index_cgbchina_transflow_taskid", columnList = "taskid")})
public class CgbChinaTransFlow  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**交易时间*/ 
	@Column(name="dealTime")
	private String dealTime ;
	
	/**交易渠道*/ 
	@Column(name="dealWay")
	private String dealWay ;
	
	/**币种*/ 
	@Column(name="currency")
	private String currency ;
	
	/**转入*/ 
	@Column(name="shiftTo")
	private String shiftTo ;
	
	/**转出*/ 
	@Column(name="rollOut")
	private String rollOut ;
	
	/**账户余额*/ 
	@Column(name="yue")
	private String yue ;
	
	/**对方姓名*/ 
	@Column(name="oppName")
	private String oppName ;
	
	/**对方账号*/ 
	@Column(name="oppNumber")
	private String oppNumber ;

	/**摘要*/ 
	@Column(name="digest")
	private String digest ;

	
	
	
	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getDealWay() {
		return dealWay;
	}

	public void setDealWay(String dealWay) {
		this.dealWay = dealWay;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getShiftTo() {
		return shiftTo;
	}

	public void setShiftTo(String shiftTo) {
		this.shiftTo = shiftTo;
	}

	public String getRollOut() {
		return rollOut;
	}

	public void setRollOut(String rollOut) {
		this.rollOut = rollOut;
	}

	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
	}

	public String getOppName() {
		return oppName;
	}

	public void setOppName(String oppName) {
		this.oppName = oppName;
	}

	public String getOppNumber() {
		return oppNumber;
	}

	public void setOppNumber(String oppNumber) {
		this.oppNumber = oppNumber;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
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
