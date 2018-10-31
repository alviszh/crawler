/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-29 15:34:20
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_cebitcard_userinfo",indexes = {@Index(name = "index_bocchina_cebitcard_userinfo_taskid", columnList = "taskid")})
public class BocchinaCebitCardUserInfoResult extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String waiveMemFeeStartDate;// 年费减免情况 起始日期
	private String waiveMemFeeEndDate;// 年费减免情况 截止日期
	private String nextMemFeeDate;// "2017-12-06" 未知含义
	private String acctNum;// 卡号
	private String acctName;// 姓名
	private String productName;// 长城环球通信用卡银卡 产品名称
	private String acctBank;// 开户银行
	private String startDate;// 启用日期
	private String carFlag;// 1 未知含义
	private String billDate;// 账单日
	private String carAvaiDate;//
	private String dueDate;// 未知含义
	private String carStatus;// 未知含义
	private String annualFee;// 1 未知含义

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@JsonBackReference
	private List<BocchinaCebitCardUserBillActList> actList;

	public void setWaiveMemFeeStartDate(String waiveMemFeeStartDate) {
		this.waiveMemFeeStartDate = waiveMemFeeStartDate;
	}

	public String getWaiveMemFeeStartDate() {
		return waiveMemFeeStartDate;
	}

	public void setWaiveMemFeeEndDate(String waiveMemFeeEndDate) {
		this.waiveMemFeeEndDate = waiveMemFeeEndDate;
	}

	public String getWaiveMemFeeEndDate() {
		return waiveMemFeeEndDate;
	}

	public void setNextMemFeeDate(String nextMemFeeDate) {
		this.nextMemFeeDate = nextMemFeeDate;
	}

	public String getNextMemFeeDate() {
		return nextMemFeeDate;
	}

	public void setAcctNum(String acctNum) {
		this.acctNum = acctNum;
	}

	public String getAcctNum() {
		return acctNum;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setAcctBank(String acctBank) {
		this.acctBank = acctBank;
	}

	public String getAcctBank() {
		return acctBank;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setCarFlag(String carFlag) {
		this.carFlag = carFlag;
	}

	public String getCarFlag() {
		return carFlag;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setCarAvaiDate(String carAvaiDate) {
		this.carAvaiDate = carAvaiDate;
	}

	public String getCarAvaiDate() {
		return carAvaiDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setCarStatus(String carStatus) {
		this.carStatus = carStatus;
	}

	public String getCarStatus() {
		return carStatus;
	}

	public void setAnnualFee(String annualFee) {
		this.annualFee = annualFee;
	}

	public String getAnnualFee() {
		return annualFee;
	}

	public void setActList(List<BocchinaCebitCardUserBillActList> actList) {
		this.actList = actList;
	}
	@Transient   
	public List<BocchinaCebitCardUserBillActList> getActList() {
		return actList;
	}

	@Override
	public String toString() {
		return "BocchinaCebitCardUserInfoResult [waiveMemFeeStartDate=" + waiveMemFeeStartDate + ", waiveMemFeeEndDate="
				+ waiveMemFeeEndDate + ", nextMemFeeDate=" + nextMemFeeDate + ", acctNum=" + acctNum + ", acctName="
				+ acctName + ", productName=" + productName + ", acctBank=" + acctBank + ", startDate=" + startDate
				+ ", carFlag=" + carFlag + ", billDate=" + billDate + ", carAvaiDate=" + carAvaiDate + ", dueDate="
				+ dueDate + ", carStatus=" + carStatus + ", annualFee=" + annualFee + ", taskid=" + taskid
				+ ", actList=" + actList + "]";
	}

}