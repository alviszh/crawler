package com.microservice.dao.entity.crawler.telecom.hubei;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_services" ,indexes = {@Index(name = "index_telecom_hubei_services_taskid", columnList = "taskid")})
public class TelecomHubeiServices extends IdEntity {

	private String itemName;// 产品名称
	private String amount;// 产品资费
	private String startDate;// 生效时间
	private String operate;// 操作
	private String type;// 类型(0已开通功能，1增值业务)

	private String taskid;


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
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
		return "TelecomHubeiServices [itemName=" + itemName + ", amount=" + amount + ", startDate=" + startDate
				+ ", operate=" + operate + ", type=" + type + ", taskid=" + taskid + "]";
	}
	public TelecomHubeiServices(String itemName, String amount, String startDate, String operate, String type,
			String taskid) {
		super();
		this.itemName = itemName;
		this.amount = amount;
		this.startDate = startDate;
		this.operate = operate;
		this.type = type;
		this.taskid = taskid;
	}
	public TelecomHubeiServices() {
		super();
		// TODO Auto-generated constructor stub
	}
}