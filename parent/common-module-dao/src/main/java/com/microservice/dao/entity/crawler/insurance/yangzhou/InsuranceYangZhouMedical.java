package com.microservice.dao.entity.crawler.insurance.yangzhou;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 扬州医保
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_yangzhou_medical")
public class InsuranceYangZhouMedical extends IdEntity {
	
	private String amount;//金额
	private String paymonth;//所属期
	private String accountday;//入账时间
	private String type;//类型
	private String taskid;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getAccountday() {
		return accountday;
	}
	public void setAccountday(String accountday) {
		this.accountday = accountday;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "InsuranceYangZhouMedical [amount=" + amount + ", paymonth=" + paymonth + ", accountday=" + accountday
				+ ", type=" + type + ", taskid=" + taskid + "]";
	}
	public InsuranceYangZhouMedical(String amount, String paymonth, String accountday, String type, String taskid) {
		super();
		this.amount = amount;
		this.paymonth = paymonth;
		this.accountday = accountday;
		this.type = type;
		this.taskid = taskid;
	}
	public InsuranceYangZhouMedical() {
		super();
	}
}
