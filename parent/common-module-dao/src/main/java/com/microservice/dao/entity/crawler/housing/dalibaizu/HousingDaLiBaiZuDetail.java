package com.microservice.dao.entity.crawler.housing.dalibaizu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_dalibaizu_detail")
public class HousingDaLiBaiZuDetail extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String time;                 	//发生日期
	private String unitName;                //单位
	private String remark;                  //业务摘要
	private String debit;                   //借方
	private String credit;                  //贷方
	private String lastBalance;             //上期余额
	private String thisBalance;				//本期余额
	private String totalBalance;			//总余额
	
	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getUnitName() {
		return unitName;
	}


	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getDebit() {
		return debit;
	}


	public void setDebit(String debit) {
		this.debit = debit;
	}


	public String getCredit() {
		return credit;
	}


	public void setCredit(String credit) {
		this.credit = credit;
	}


	public String getLastBalance() {
		return lastBalance;
	}


	public void setLastBalance(String lastBalance) {
		this.lastBalance = lastBalance;
	}


	public String getThisBalance() {
		return thisBalance;
	}


	public void setThisBalance(String thisBalance) {
		this.thisBalance = thisBalance;
	}


	public String getTotalBalance() {
		return totalBalance;
	}


	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}


	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	private String taskid;


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
