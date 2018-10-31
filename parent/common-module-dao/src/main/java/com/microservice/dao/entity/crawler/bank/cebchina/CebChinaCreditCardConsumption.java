package com.microservice.dao.entity.crawler.bank.cebchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name="cebchina_creditcard_consumption")
public class CebChinaCreditCardConsumption  extends IdEntity implements Serializable{

	private String taskid;
	
	private String month;//月份
	
	private String rmb_money;//人民币本期应还总额
	
	private String us_money;//美元本期应还总额
	
	private String eur_money;//欧元本期应还总额
	
	private String enddate;
	
	private String num;
	
	

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getRmb_money() {
		return rmb_money;
	}

	public void setRmb_money(String rmb_money) {
		this.rmb_money = rmb_money;
	}

	public String getUs_money() {
		return us_money;
	}

	public void setUs_money(String us_money) {
		this.us_money = us_money;
	}

	public String getEur_money() {
		return eur_money;
	}

	public void setEur_money(String eur_money) {
		this.eur_money = eur_money;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	@Override
	public String toString() {
		return "CebChinaCreditCardConsumption [taskid=" + taskid + ", month=" + month + ", rmb_money=" + rmb_money
				+ ", us_money=" + us_money + ", eur_money=" + eur_money + ", enddate=" + enddate + "]";
	}

	public CebChinaCreditCardConsumption(String taskid, String month, String rmb_money, String us_money,
			String eur_money, String enddate, String num) {
		super();
		this.taskid = taskid;
		this.month = month;
		this.rmb_money = rmb_money;
		this.us_money = us_money;
		this.eur_money = eur_money;
		this.enddate = enddate;
		this.num = num;
	}

	public CebChinaCreditCardConsumption() {
		super();
	}


	
	
}
