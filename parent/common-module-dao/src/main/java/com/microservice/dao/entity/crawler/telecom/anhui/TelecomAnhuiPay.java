package com.microservice.dao.entity.crawler.telecom.anhui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecom_anhui_pay",indexes = {@Index(name = "index_telecom_anhui_pay_taskid", columnList = "taskid")}) 
public class TelecomAnhuiPay extends IdEntity{

	private  String status;//交费方式
	
	private String payTime;//交费时间
	
	private String outMoney;//支出
	
	private  String getMoney;//收入
	
	private String banlance;//余额
	
	private String typePay;//支付渠道
	
	private String taskid;

	@Override
	public String toString() {
		return "TelecomAnhuiPay [status=" + status + ", payTime=" + payTime + ", outMoney=" + outMoney + ", getMoney="
				+ getMoney + ", banlance=" + banlance + ", typePay=" + typePay + ", taskid=" + taskid + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOutMoney() {
		return outMoney;
	}

	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}

	public String getGetMoney() {
		return getMoney;
	}

	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}

	public String getBanlance() {
		return banlance;
	}

	public void setBanlance(String banlance) {
		this.banlance = banlance;
	}

	public String getTypePay() {
		return typePay;
	}

	public void setTypePay(String typePay) {
		this.typePay = typePay;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
	
	
}
