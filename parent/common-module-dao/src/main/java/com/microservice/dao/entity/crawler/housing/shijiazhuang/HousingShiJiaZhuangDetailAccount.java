package com.microservice.dao.entity.crawler.housing.shijiazhuang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:41:41 
 */
@Entity
@Table(name="housing_shijiazhuang_detailaccount",indexes = {@Index(name = "index_housing_shijiazhuang_detailaccount_taskid", columnList = "taskid")})
public class HousingShiJiaZhuangDetailAccount extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5489158452811499470L;
	private String taskid;
//	行号
	private Integer rownum;
//	缴交年月
	private String chargeyearmonth;
//	摘要
	private String  summary;
//	收入金额
	private String receivemoney;
//	支出金额
	private String  amount;
//	余额
	private String balance;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public Integer getRownum() {
		return rownum;
	}
	public void setRownum(Integer rownum) {
		this.rownum = rownum;
	}
	public String getChargeyearmonth() {
		return chargeyearmonth;
	}
	public void setChargeyearmonth(String chargeyearmonth) {
		this.chargeyearmonth = chargeyearmonth;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getReceivemoney() {
		return receivemoney;
	}
	public void setReceivemoney(String receivemoney) {
		this.receivemoney = receivemoney;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public HousingShiJiaZhuangDetailAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HousingShiJiaZhuangDetailAccount(String taskid, Integer rownum, String chargeyearmonth, String summary,
			String receivemoney, String amount, String balance) {
		super();
		this.taskid = taskid;
		this.rownum = rownum;
		this.chargeyearmonth = chargeyearmonth;
		this.summary = summary;
		this.receivemoney = receivemoney;
		this.amount = amount;
		this.balance = balance;
	}
	
}
