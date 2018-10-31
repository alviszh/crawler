package com.microservice.dao.entity.crawler.telecom.hubei.wap;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_wap_paymonths" ,indexes = {@Index(name = "index_telecom_hubei_wap_paymonths_taskid", columnList = "taskid")})
public class TelecomHubeiWapPaymonths extends IdEntity {

	private String cycle;// 账期
	private String itemName;// 费用项目
	private String amount;// 金额

	private String taskid;
	
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomHubeiPaymonths [cycle=" + cycle + ", itemName=" + itemName + ", amount=" + amount + ", taskid="
				+ taskid + "]";
	}
	public TelecomHubeiWapPaymonths(String cycle, String itemName, String amount, String taskid) {
		super();
		this.cycle = cycle;
		this.itemName = itemName;
		this.amount = amount;
		this.taskid = taskid;
	}
	public TelecomHubeiWapPaymonths() {
		super();
		// TODO Auto-generated constructor stub
	}
}