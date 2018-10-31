package com.microservice.dao.entity.crawler.telecom.guizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guizhou_paymonth" ,indexes = {@Index(name = "index_telecom_guizhou_paymonth_taskid", columnList = "taskid")})
public class TelecomGuizhouPaymonth extends IdEntity {

	private String cycle;// 账期
	private String itemName;// 账目项
	private String realAmout;//实收费用
	private String taskid;
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	
	public String getRealAmout() {
		return realAmout;
	}
	public void setRealAmout(String realAmout) {
		this.realAmout = realAmout;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@Override
	public String toString() {
		return "TelecomGuizhouPaymonth [cycle=" + cycle + ", itemName=" + itemName + ", realAmout=" + realAmout
				+ ", taskid=" + taskid + "]";
	}
	public TelecomGuizhouPaymonth(String cycle, String itemName, String realAmout, String taskid) {
		super();
		this.cycle = cycle;
		this.itemName = itemName;
		this.realAmout = realAmout;
		this.taskid = taskid;
	}
	public TelecomGuizhouPaymonth() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}