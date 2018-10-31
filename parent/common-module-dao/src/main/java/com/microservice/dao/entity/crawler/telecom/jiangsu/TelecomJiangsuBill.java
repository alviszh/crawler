package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 账单
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_bill" ,indexes = {@Index(name = "index_telecom_jiangsu_bill_taskid", columnList = "taskid")})
public class TelecomJiangsuBill extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 本期费用合计
	 */
	private String dcc_charge;
	/**
	 * 本期已付费用
	 */
	private String dcc_bal_owe_used;
	/**
	 * 本期应付费用
	 */
	private String dcc_owe_charge;

	/**
	 *  计费名目
	 */
	private String itemName;

	/**
	 * 金额
	 */
	private String itemCharge;
	
	/**
	 * 年月
	 */
	private String yearmonth;
	
	
	/**
	 * 序号
	 * (层级)
	 */
	private String treeNum;


	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public String getDcc_charge() {
		return dcc_charge;
	}


	public void setDcc_charge(String dcc_charge) {
		this.dcc_charge = dcc_charge;
	}


	public String getDcc_bal_owe_used() {
		return dcc_bal_owe_used;
	}


	public void setDcc_bal_owe_used(String dcc_bal_owe_used) {
		this.dcc_bal_owe_used = dcc_bal_owe_used;
	}


	public String getDcc_owe_charge() {
		return dcc_owe_charge;
	}


	public void setDcc_owe_charge(String dcc_owe_charge) {
		this.dcc_owe_charge = dcc_owe_charge;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemCharge() {
		return itemCharge;
	}


	public void setItemCharge(String itemCharge) {
		this.itemCharge = itemCharge;
	}


	public String getYearmonth() {
		return yearmonth;
	}


	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}


	public String getTreeNum() {
		return treeNum;
	}


	public void setTreeNum(String treeNum) {
		this.treeNum = treeNum;
	}


	public TelecomJiangsuBill(String taskid, String dcc_charge, String dcc_bal_owe_used, String dcc_owe_charge,
			String itemName, String itemCharge, String yearmonth, String treeNum) {
		super();
		this.taskid = taskid;
		this.dcc_charge = dcc_charge;
		this.dcc_bal_owe_used = dcc_bal_owe_used;
		this.dcc_owe_charge = dcc_owe_charge;
		this.itemName = itemName;
		this.itemCharge = itemCharge;
		this.yearmonth = yearmonth;
		this.treeNum = treeNum;
	}


	public TelecomJiangsuBill() {
		super();
		// TODO Auto-generated constructor stub
	}



}