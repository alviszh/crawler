package com.microservice.dao.entity.crawler.telecom.shanxi1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西电信缴费信息
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_shanxi1_payinfo" ,indexes = {@Index(name = "index_telecom_shanxi1_payinfo_taskid", columnList = "taskid")})
public class TelecomShanxi1PayInfo extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 姓名
	 */
	private String accountName;
	
	/**
	 * 产品号码
	 */
	private String accNbr;
	
	/**
	 * 支付时间
	 */
	private String paymentDate;
	
	/**
	 * 支付金额
	 */
	private String fee;
	
	/**
	 * 单位
	 */
	private String moneyUnit;

	/**
	 * 支付类型
	 */
	private String paymentType;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 支付来源
	 */
	private String regionDesc;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccNbr() {
		return accNbr;
	}

	public void setAccNbr(String accNbr) {
		this.accNbr = accNbr;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(String moneyUnit) {
		this.moneyUnit = moneyUnit;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRegionDesc() {
		return regionDesc;
	}

	public void setRegionDesc(String regionDesc) {
		this.regionDesc = regionDesc;
	}

	@Override
	public String toString() {
		return "TelecomShanxi1PayInfo [taskid=" + taskid + ", accountName=" + accountName + ", accNbr=" + accNbr
				+ ", paymentDate=" + paymentDate + ", fee=" + fee + ", moneyUnit=" + moneyUnit + ", paymentType="
				+ paymentType + ", state=" + state + ", regionDesc=" + regionDesc + "]";
	}

	public TelecomShanxi1PayInfo(String taskid, String accountName, String accNbr, String paymentDate, String fee,
			String moneyUnit, String paymentType, String state, String regionDesc) {
		super();
		this.taskid = taskid;
		this.accountName = accountName;
		this.accNbr = accNbr;
		this.paymentDate = paymentDate;
		this.fee = fee;
		this.moneyUnit = moneyUnit;
		this.paymentType = paymentType;
		this.state = state;
		this.regionDesc = regionDesc;
	}

	public TelecomShanxi1PayInfo() {
		super();
		// TODO Auto-generated constructor stub
	}


}