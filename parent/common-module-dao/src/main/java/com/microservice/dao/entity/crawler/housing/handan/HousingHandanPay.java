package com.microservice.dao.entity.crawler.housing.handan;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_handan_pay")
public class HousingHandanPay extends IdEntity{
	
	private String taskid;
	private String transdate;						//业务日期
	private String paybegindate;					//起始年月
	private String payenddate;						//结束年月
	private String balance;							//余额
	private String summary;							//摘要
	private String damt;							//支出金额
	private String camt;							//收入金额
	
	@Override
	public String toString() {
		return "HousingHandanPay [taskid=" + taskid + ", transdate=" + transdate + ", paybegindate=" + paybegindate
				+ ", payenddate=" + payenddate + ", balance=" + balance + ", summary=" + summary + ", damt=" + damt
				+ ", camt=" + camt + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public String getPaybegindate() {
		return paybegindate;
	}
	public void setPaybegindate(String paybegindate) {
		this.paybegindate = paybegindate;
	}
	public String getPayenddate() {
		return payenddate;
	}
	public void setPayenddate(String payenddate) {
		this.payenddate = payenddate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDamt() {
		return damt;
	}
	public void setDamt(String damt) {
		this.damt = damt;
	}
	public String getCamt() {
		return camt;
	}
	public void setCamt(String camt) {
		this.camt = camt;
	}

}
