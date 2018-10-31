package com.microservice.dao.entity.crawler.bank.bohcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="bohcchina_userinfo",indexes = {@Index(name = "index_bohcchina_userinfo_taskid", columnList = "taskid")})
public class BohcChinaUserInfo extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**账户编号*/ 
	@Column(name="acNo")
	private String acNo ;
	
	/**账户姓名*/ 
	@Column(name="acname")
	private String acname ;
	
	/**币种*/ 
	@Column(name="currName")
	private String currName ;
	
	/**卡号*/ 
	@Column(name="cardNo")
	private String cardNo ;

	/**开户地*/ 
	@Column(name="cityName")
	private String cityName ;
	
	/**开户时间*/ 
	@Column(name="openDate")
	private String openDate ;

	/**可用余额*/ 
	@Column(name="usableyue")
	private String usableyue ;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getUsableyue() {
		return usableyue;
	}

	public void setUsableyue(String usableyue) {
		this.usableyue = usableyue;
	}

	public String getAcNo() {
		return acNo;
	}

	public void setAcNo(String acNo) {
		this.acNo = acNo;
	}

	public String getAcname() {
		return acname;
	}

	public void setAcname(String acname) {
		this.acname = acname;
	}

	public String getCurrName() {
		return currName;
	}

	public void setCurrName(String currName) {
		this.currName = currName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
