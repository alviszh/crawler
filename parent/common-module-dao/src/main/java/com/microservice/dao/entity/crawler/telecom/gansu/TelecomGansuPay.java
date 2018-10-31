package com.microservice.dao.entity.crawler.telecom.gansu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="telecomGansu_pay",indexes = {@Index(name = "index_telecomGansu_pay_taskid", columnList = "taskid")}) 
public class TelecomGansuPay extends IdEntity{

	private String taskid;
	private String time;//交易时间
	private String money;//交易金额
	private String payRoad;//缴费渠道
	private String payType;//付款方式
	@Override
	public String toString() {
		return "TelecomGansuPay [taskid=" + taskid + ", time=" + time + ", money=" + money + ", payRoad=" + payRoad
				+ ", payType=" + payType + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Column(columnDefinition="text")
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Column(columnDefinition="text")
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	@Column(columnDefinition="text")
	public String getPayRoad() {
		return payRoad;
	}
	public void setPayRoad(String payRoad) {
		this.payRoad = payRoad;
	}
	@Column(columnDefinition="text")
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public TelecomGansuPay(String taskid, String time, String money, String payRoad, String payType) {
		super();
		this.taskid = taskid;
		this.time = time;
		this.money = money;
		this.payRoad = payRoad;
		this.payType = payType;
	}
	public TelecomGansuPay() {
		super();
	}
	
	
	
}
