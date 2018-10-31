package com.microservice.dao.entity.crawler.housing.wuhan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_wuhan_paystatus",indexes = {@Index(name = "index_housing_wuhan_paystatus_taskid", columnList = "taskid")})
public class HousingWuHanPayStatus extends IdEntity implements Serializable {

	private String taskid;
	
	private Integer userid;
	
	private String TransDate;//时间
	
	private String SummaryCode;//交易码
	
	private String DebitAmount;//借方发生额
	
	private String Balance;//余额
	
	private String CreditAmount;//贷方发生额

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTransDate() {
		return TransDate;
	}

	public void setTransDate(String transDate) {
		TransDate = transDate;
	}

	public String getSummaryCode() {
		return SummaryCode;
	}

	public void setSummaryCode(String summaryCode) {
		SummaryCode = summaryCode;
	}

	public String getDebitAmount() {
		return DebitAmount;
	}

	public void setDebitAmount(String debitAmount) {
		DebitAmount = debitAmount;
	}

	public String getBalance() {
		return Balance;
	}

	public void setBalance(String balance) {
		Balance = balance;
	}

	public String getCreditAmount() {
		return CreditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		CreditAmount = creditAmount;
	}

	@Override
	public String toString() {
		return "HousingWuHanPayStatus [taskid=" + taskid + ", userid=" + userid + ", TransDate=" + TransDate
				+ ", SummaryCode=" + SummaryCode + ", DebitAmount=" + DebitAmount + ", Balance=" + Balance
				+ ", CreditAmount=" + CreditAmount + "]";
	}

	public HousingWuHanPayStatus(String taskid, Integer userid, String transDate, String summaryCode,
			String debitAmount, String balance, String creditAmount) {
		super();
		this.taskid = taskid;
		this.userid = userid;
		TransDate = transDate;
		SummaryCode = summaryCode;
		DebitAmount = debitAmount;
		Balance = balance;
		CreditAmount = creditAmount;
	}

	public HousingWuHanPayStatus() {
		super();
	}
	
}
