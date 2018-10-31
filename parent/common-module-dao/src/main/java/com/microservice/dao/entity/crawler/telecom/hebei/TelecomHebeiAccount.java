package com.microservice.dao.entity.crawler.telecom.hebei;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 河北电信-账单详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_hebei_account")
public class TelecomHebeiAccount extends IdEntity{
	
	private String taskid;
	private String month;					//月份
	private String monthlyBasePay;			//月基本费
	private String communicationPay;		//语音通信费
	private String longDistancePay;			//长途通信费
	private String localPay;				//本地通信费
	private String total;					//总计
	private String msgPay;					//短信费
	private String mmsPay;					//短信彩信费
	private String internetPay;				//手机上网费
	
	public String getInternetPay() {
		return internetPay;
	}
	public void setInternetPay(String internetPay) {
		this.internetPay = internetPay;
	}
	public String getMmsPay() {
		return mmsPay;
	}
	public void setMmsPay(String mmsPay) {
		this.mmsPay = mmsPay;
	}
	public String getMsgPay() {
		return msgPay;
	}
	public void setMsgPay(String msgPay) {
		this.msgPay = msgPay;
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
	public String getMonthlyBasePay() {
		return monthlyBasePay;
	}
	public void setMonthlyBasePay(String monthlyBasePay) {
		this.monthlyBasePay = monthlyBasePay;
	}
	public String getCommunicationPay() {
		return communicationPay;
	}
	public void setCommunicationPay(String communicationPay) {
		this.communicationPay = communicationPay;
	}
	public String getLongDistancePay() {
		return longDistancePay;
	}
	public void setLongDistancePay(String longDistancePay) {
		this.longDistancePay = longDistancePay;
	}
	public String getLocalPay() {
		return localPay;
	}
	public void setLocalPay(String localPay) {
		this.localPay = localPay;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "TelecomHebeiAccount [taskid=" + taskid + ", month=" + month + ", monthlyBasePay=" + monthlyBasePay
				+ ", communicationPay=" + communicationPay + ", longDistancePay=" + longDistancePay + ", localPay="
				+ localPay + ", total=" + total + "]";
	}
	
	
}
