package com.microservice.dao.entity.crawler.telecom.hebei;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 河北电信-短信详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_hebei_msg")
public class TelecomHebeiMsg extends IdEntity{
	
	private String taskid;
	private String otherNum;					//对方号码
	private String msgType;						//话单类型
	private String msgTime;						//发送时间
	private String correspondence;				//通信费
	private String reducedUse;					//折算使用量
	private String msgPay;						//信息费（元）
	private String discountsFee;				//优惠信息费（元）
	private String packageCosts;				//包月费	
	private String total;						//合计
	
	
	@Override
	public String toString() {
		return "TelecomHebeiMsg [taskid=" + taskid + ", otherNum=" + otherNum + ", msgType=" + msgType + ", msgTime="
				+ msgTime + ", correspondence=" + correspondence + ", reducedUse=" + reducedUse + ", msgPay=" + msgPay
				+ ", discountsFee=" + discountsFee + ", packageCosts=" + packageCosts + ", total=" + total + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	
	public String getCorrespondence() {
		return correspondence;
	}
	public void setCorrespondence(String correspondence) {
		this.correspondence = correspondence;
	}
	public String getReducedUse() {
		return reducedUse;
	}
	public void setReducedUse(String reducedUse) {
		this.reducedUse = reducedUse;
	}
	public String getMsgPay() {
		return msgPay;
	}
	public void setMsgPay(String msgPay) {
		this.msgPay = msgPay;
	}
	public String getDiscountsFee() {
		return discountsFee;
	}
	public void setDiscountsFee(String discountsFee) {
		this.discountsFee = discountsFee;
	}
	public String getPackageCosts() {
		return packageCosts;
	}
	public void setPackageCosts(String packageCosts) {
		this.packageCosts = packageCosts;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}

}
