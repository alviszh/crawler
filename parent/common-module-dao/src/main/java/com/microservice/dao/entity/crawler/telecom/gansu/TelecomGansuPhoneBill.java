package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGansu_bill",indexes = {@Index(name = "index_telecomGansu_bill_taskid", columnList = "taskid")}) 
public class TelecomGansuPhoneBill extends IdEntity {
	private String money;//总费用

	private String month;//账期

	private String payState;//支付状态

	
	
	private String stylepx;

	private String taskid;

	@Override
	public String toString() {
		return "TelecomGansuPhoneBill [money=" + money + ", month=" + month + ", payState=" + payState + ", stylepx="
				+ stylepx + ", taskid=" + taskid + "]";
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getStylepx() {
		return stylepx;
	}

	public void setStylepx(String stylepx) {
		this.stylepx = stylepx;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	

	
	
	

}
