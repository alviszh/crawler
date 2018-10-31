package com.microservice.dao.entity.crawler.telecom.hebei;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 河北电信-缴费信息
 * @author zz
 *
 */
@Entity
@Table(name="telecom_hebei_payfee")
public class TelecomHebeiPayfee extends IdEntity{
	
	private String taskid;
	private String phoneNum;						//业务号码
	private String payType;							//缴费类型
	private String payBank;							//缴费银行
	private String transactionAmount;				//交易金额
	private String payTime;							//缴费时间
	
	@Override
	public String toString() {
		return "TelecomHebeiPayfee [taskid=" + taskid + ", phoneNum=" + phoneNum + ", payType=" + payType + ", payBank="
				+ payBank + ", transactionAmount=" + transactionAmount + ", payTime=" + payTime + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayBank() {
		return payBank;
	}
	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

}
