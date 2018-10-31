package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="telecomGansu_phoneMonthBill",indexes = {@Index(name = "index_telecomGansu_phoneMonthBill_taskid", columnList = "taskid")}) 
public class TelecomGansuPhoneMonthBill extends IdEntity{

    private String netMoney;//流量费用
	
	private String tianYiMoney;//天翼套餐总费用
	
	private String messageMoney;//短信通信费
	
	private String calllocalMoney;//本地通话费
	
	private String callLongMoney;//国内长途费用
	
	private String month;
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomGansuPhoneMonthBill [netMoney=" + netMoney + ", tianYiMoney=" + tianYiMoney + ", messageMoney="
				+ messageMoney + ", calllocalMoney=" + calllocalMoney + ", callLongMoney=" + callLongMoney + ", month="
				+ month + ", taskid=" + taskid + "]";
	}

	public String getNetMoney() {
		return netMoney;
	}

	public void setNetMoney(String netMoney) {
		this.netMoney = netMoney;
	}

	public String getTianYiMoney() {
		return tianYiMoney;
	}

	public void setTianYiMoney(String tianYiMoney) {
		this.tianYiMoney = tianYiMoney;
	}

	public String getMessageMoney() {
		return messageMoney;
	}

	public void setMessageMoney(String messageMoney) {
		this.messageMoney = messageMoney;
	}

	public String getCalllocalMoney() {
		return calllocalMoney;
	}

	public void setCalllocalMoney(String calllocalMoney) {
		this.calllocalMoney = calllocalMoney;
	}

	public String getCallLongMoney() {
		return callLongMoney;
	}

	public void setCallLongMoney(String callLongMoney) {
		this.callLongMoney = callLongMoney;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
	
	
	
}
