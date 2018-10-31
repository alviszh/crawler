package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信账户信息
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_account" ,indexes = {@Index(name = "index_telecom_shanxi1_account_taskid", columnList = "taskid")})
public class TelecomShanxi1Account extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;

	
	/**
	 * 产品号
	 */
	private String accNbr;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 安装地址
	 */
	private String address;

	/**
	 * 安装日期
	 */
	private String servCreateDate;

	/**
	 * 状态
	 */
	private String productStatusCdName;

	/**
	 * 账户
	 */
	private String payAcctNbr;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccNbr() {
		return accNbr;
	}

	public void setAccNbr(String accNbr) {
		this.accNbr = accNbr;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getServCreateDate() {
		return servCreateDate;
	}

	public void setServCreateDate(String servCreateDate) {
		this.servCreateDate = servCreateDate;
	}

	public String getProductStatusCdName() {
		return productStatusCdName;
	}

	public void setProductStatusCdName(String productStatusCdName) {
		this.productStatusCdName = productStatusCdName;
	}

	public String getPayAcctNbr() {
		return payAcctNbr;
	}

	public void setPayAcctNbr(String payAcctNbr) {
		this.payAcctNbr = payAcctNbr;
	}

	@Override
	public String toString() {
		return "TelecomShanxi1Account [taskid=" + taskid + ", accNbr=" + accNbr + ", productName=" + productName
				+ ", address=" + address + ", servCreateDate=" + servCreateDate + ", productStatusCdName="
				+ productStatusCdName + ", payAcctNbr=" + payAcctNbr + "]";
	}

	public TelecomShanxi1Account(String taskid, String accNbr, String productName, String address,
			String servCreateDate, String productStatusCdName, String payAcctNbr) {
		super();
		this.taskid = taskid;
		this.accNbr = accNbr;
		this.productName = productName;
		this.address = address;
		this.servCreateDate = servCreateDate;
		this.productStatusCdName = productStatusCdName;
		this.payAcctNbr = payAcctNbr;
	}

	public TelecomShanxi1Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}